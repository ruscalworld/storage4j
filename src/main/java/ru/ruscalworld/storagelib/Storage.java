package ru.ruscalworld.storagelib;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Storage {
    void setup() throws Exception;
    <T> T find(@NotNull Class<T> clazz, String key, Object value) throws Exception;
    <T> T retrieve(Class<T> clazz, int id) throws Exception;
    <T> List<T> retrieveAll(Class<T> clazz) throws Exception;
    <T extends DefaultModel> int save(T model) throws Exception;
    void actualizeStorageSchema() throws Exception;
}
