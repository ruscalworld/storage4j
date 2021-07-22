package ru.ruscalworld.storagelib;

import org.jetbrains.annotations.NotNull;
import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.exceptions.InvalidModelException;
import ru.ruscalworld.storagelib.exceptions.NotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Storage {
    void setup() throws SQLException, IOException;
    <T> T find(@NotNull Class<T> clazz, String key, Object value) throws SQLException, NotFoundException, InvalidModelException;
    @Deprecated
    <T> List<T> findAll(@NotNull Class<T> clazz, String key, Object value) throws InvalidModelException, SQLException;
    <T> List<T> findAll(@NotNull Class<T> clazz, Expression condition) throws InvalidModelException, SQLException;
    <T> T retrieve(Class<T> clazz, long id) throws SQLException, InvalidModelException, NotFoundException;
    <T> List<T> retrieveAll(Class<T> clazz) throws SQLException, InvalidModelException;
    <T extends DefaultModel> long save(T model) throws InvalidModelException, SQLException;
    <T extends DefaultModel> void delete(T model) throws InvalidModelException, SQLException;
    <T> void deleteAll(Class<T> clazz, Expression condition) throws InvalidModelException, SQLException;
    @Deprecated
    <T> void deleteAll(Class<T> clazz, String key, Object value) throws InvalidModelException, SQLException;
    ConverterProvider getConverterProvider();
    void registerConverter(Class<?> clazz, Converter<?> converter);
    void actualizeStorageSchema() throws SQLException, IOException;
}
