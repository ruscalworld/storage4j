package ru.ruscalworld.storagelib.impl;

import org.jetbrains.annotations.NotNull;
import org.sqlite.JDBC;
import ru.ruscalworld.storagelib.Converter;
import ru.ruscalworld.storagelib.ConverterProvider;
import ru.ruscalworld.storagelib.DefaultModel;
import ru.ruscalworld.storagelib.Storage;
import ru.ruscalworld.storagelib.annotations.Model;
import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.builder.SerializedExpression;
import ru.ruscalworld.storagelib.builder.expressions.Comparison;
import ru.ruscalworld.storagelib.exceptions.InvalidModelException;
import ru.ruscalworld.storagelib.exceptions.NotFoundException;
import ru.ruscalworld.storagelib.util.DatabaseUtil;
import ru.ruscalworld.storagelib.util.FileUtil;
import ru.ruscalworld.storagelib.util.ReflectUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteStorage implements Storage {
    private final String connectionUrl;
    private Connection connection;
    private final ConverterProvider converterProvider = new ConverterProvider();

    private final List<String> migrations = new ArrayList<>();

    public SQLiteStorage(String url) {
        this.connectionUrl = url;
    }

    public void setup() throws SQLException, IOException {
        // Create connection
        DriverManager.registerDriver(new JDBC());
        this.connection = this.getConnection();

        // Create tables if they doesn't exist
        this.actualizeStorageSchema();
    }

    public <T> T find(@NotNull Class<T> clazz, String key, Object value) throws SQLException, NotFoundException, InvalidModelException {
        Model model = ReflectUtil.getModelInfo(clazz);

        PreparedStatement statement = this.makeSearchStatement(model, key, value);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) throw new NotFoundException(key, value);

        return this.parseRow(clazz, resultSet);
    }

    @Override
    public <T> List<T> findAll(@NotNull Class<T> clazz, String key, Object value) throws InvalidModelException, SQLException {
        return this.findAll(clazz, Comparison.equal(key, value.toString()));
    }

    @Override
    public <T> List<T> findAll(@NotNull Class<T> clazz, Expression condition) throws InvalidModelException, SQLException {
        Model model = ReflectUtil.getModelInfo(clazz);

        PreparedStatement statement = this.makeSearchStatement(model, condition);
        ResultSet resultSet = statement.executeQuery();
        List<T> result = new ArrayList<>();
        while (resultSet.next()) result.add(this.parseRow(clazz, resultSet));

        return result;
    }

    public <T> T retrieve(@NotNull Class<T> clazz, long id) throws SQLException, InvalidModelException, NotFoundException {
        ReflectUtil.getModelInfo(clazz);
        Model model = clazz.getAnnotation(Model.class);

        PreparedStatement statement = this.makeSearchStatement(model, "id", id);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) throw new NotFoundException("id", id);

        return this.parseRow(clazz, resultSet);
    }

    private PreparedStatement makeSearchStatement(Model model, String key, Object value) throws SQLException {
        return this.makeSearchStatement(model, Comparison.equal(key, value.toString()));
    }

    private PreparedStatement makeSearchStatement(Model model, Expression condition) throws SQLException {
        SerializedExpression expression = condition.serialize();
        String query = String.format("SELECT * FROM `%s` WHERE %s", model.table(), expression.toString());
        PreparedStatement statement = this.getConnection().prepareStatement(query);
        expression.processStatement(statement, 0);
        return statement;
    }

    public <T> List<T> retrieveAll(@NotNull Class<T> clazz) throws InvalidModelException, SQLException {
        Model model = ReflectUtil.getModelInfo(clazz);

        String query = String.format("SELECT * FROM `%s`", model.table());
        PreparedStatement statement = this.getConnection().prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();
        List<T> result = new ArrayList<>();
        while (resultSet.next()) result.add(this.parseRow(clazz, resultSet));

        return result;
    }

    public <T extends DefaultModel> long save(@NotNull T model) throws InvalidModelException, SQLException {
        Model modelInfo = ReflectUtil.getModelInfo(model.getClass());
        Class<? extends DefaultModel> clazz = model.getClass();
        ReflectUtil.getModelInfo(clazz);

        HashMap<String, Field> fields = ReflectUtil.getClassFields(clazz, model.getId() == 0);
        HashMap<String, String> values = new HashMap<>();

        for (String name : fields.keySet()) {
            Field field = fields.get(name);
            Object value = null;

            try {
                field.setAccessible(true);
                value = field.get(model);
            } catch (IllegalAccessException ignored) { }

            values.put(name, value == null ? null : value.toString());
        }

        String table = modelInfo.table();
        PreparedStatement statement;

        if (model.getId() == 0) {
            values.remove("id");
            statement = DatabaseUtil.makeInsertStatement(values, table, this.getConnection());
        } else statement = DatabaseUtil.makeUpdateStatement("id", "" + model.getId(), values, table, this.getConnection());

        statement.executeUpdate();
        return model.getId() == 0 ? statement.getGeneratedKeys().getLong(1) : model.getId();
    }

    @Override
    public <T extends DefaultModel> void delete(T model) throws InvalidModelException, SQLException {
        Model modelInfo = ReflectUtil.getModelInfo(model.getClass());
        String table = modelInfo.table();

        String query = String.format("DELETE FROM `%s` WHERE `id` = ?", table);
        PreparedStatement statement = this.getConnection().prepareStatement(query);
        statement.setLong(1, model.getId());
        statement.executeUpdate();
    }

    @Override
    public <T> void deleteAll(Class<T> clazz, Expression condition) throws InvalidModelException, SQLException {
        Model model = ReflectUtil.getModelInfo(clazz);
        String table = model.table();

        SerializedExpression expression = condition.serialize();
        String query = String.format("DELETE FROM `%s` WHERE %s", table, expression.toString());
        PreparedStatement statement = this.getConnection().prepareStatement(query);
        expression.processStatement(statement, 0);
        statement.executeUpdate();
    }

    @Override
    public <T> void deleteAll(Class<T> clazz, String key, Object value) throws InvalidModelException, SQLException {
        this.deleteAll(clazz, Comparison.equal(key, value.toString()));
    }

    @Override
    public ConverterProvider getConverterProvider() {
        return this.converterProvider;
    }

    @Override
    public void registerConverter(Class<?> clazz, Converter<?> converter) {
        this.converterProvider.registerConverter(clazz, converter);
    }

    public <T> T parseRow(Class<T> clazz, ResultSet resultSet) throws SQLException {
        try {
            T instance = clazz.getConstructor().newInstance();
            final HashMap<String, Field> fields = ReflectUtil.getClassFields(instance.getClass());

            for (String column : DatabaseUtil.getColumnNames(resultSet)) {
                if (!fields.containsKey(column)) continue;
                Object value = resultSet.getString(column);

                Field field = fields.get(column);
                field.setAccessible(true);
                ReflectUtil.setFieldValue(instance, field, value, this.getConverterProvider());
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void actualizeStorageSchema() throws SQLException, IOException {
        for (String migration : this.getMigrations()) {
            InputStream stream = getClass().getResourceAsStream("/schema/" + migration + ".sql");
            List<String> script = FileUtil.getLinesFromStream(stream);
            DatabaseUtil.executeScript(this.getConnection(), script);
        }
    }

    public void registerMigration(String name) {
        this.migrations.add(name);
    }

    public List<String> getMigrations() {
        return migrations;
    }

    public Connection getConnection() throws SQLException {
        if (this.connection == null || !this.connection.isValid(3)) {
            this.connection = DriverManager.getConnection(this.getConnectionUrl());
        }

        return this.connection;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }
}
