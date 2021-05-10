package ru.ruscalworld.storagelib.impl;

import org.junit.jupiter.api.*;
import ru.ruscalworld.storagelib.exceptions.InvalidModelException;
import ru.ruscalworld.storagelib.exceptions.NotFoundException;
import ru.ruscalworld.storagelib.models.TestModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLiteStorageTest {
    private static final String URL = System.getProperty("java.io.tmpdir") + "/" + System.currentTimeMillis() + ".db";
    private static final SQLiteStorage storage = new SQLiteStorage("jdbc:sqlite:" + URL);

    @Test
    @Order(1)
    void setup() {
        storage.registerMigration("sqlite");
        assertDoesNotThrow(storage::setup);
    }

    @Test
    @Order(2)
    void save() throws InvalidModelException, SQLException, NotFoundException {
        TestModel model = new TestModel();
        model.setFloatProperty(2.5F);
        model.setDoubleProperty(7.5D);
        model.setTimestamp(new Timestamp(System.currentTimeMillis()));
        model.setStringProperty("Some String");
        model.setUuidProperty(UUID.randomUUID());

        assertDoesNotThrow(() -> storage.save(model));
        assertEquals(2, storage.save(model));

        TestModel savedModel = storage.retrieve(TestModel.class, 1);
        assertTrue(savedModel.getDefaultTimestamp().after(new Timestamp(System.currentTimeMillis() - 60 * 1000)));
    }

    @Test
    @Order(3)
    void find() {
        assertDoesNotThrow(() -> storage.find(TestModel.class, "string", "Some String"));
        assertThrows(NotFoundException.class, () -> storage.find(TestModel.class, "string", "Some Another String"));
    }

    @Test
    @Order(3)
    void retrieve() {
        assertDoesNotThrow(() -> storage.retrieve(TestModel.class, 1));
    }

    @Test
    @Order(3)
    void retrieveAll() {
        final List<TestModel> result = new ArrayList<>();
        assertDoesNotThrow(() -> result.addAll(storage.retrieveAll(TestModel.class)));
        assertEquals(2, result.size());
    }

    @Test
    @Order(4)
    void delete() throws InvalidModelException, SQLException, NotFoundException {
        TestModel model = storage.retrieve(TestModel.class, 1);
        assertDoesNotThrow(() -> storage.delete(model));
        assertThrows(NotFoundException.class, () -> storage.retrieve(TestModel.class, 1));
    }

    @AfterAll
    public static void clean() throws IOException, SQLException {
        storage.getConnection().close();
        Files.delete(Path.of(URL));
    }
}