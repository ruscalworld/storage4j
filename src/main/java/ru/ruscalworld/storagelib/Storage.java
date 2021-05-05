package ru.ruscalworld.storagelib;

import java.util.List;

public interface Storage {
    void setup() throws Exception;
    <T> T retrieve(Class<T> clazz, int id) throws Exception;
    <T> List<T> retrieveAll(Class<T> clazz) throws Exception;
    <T extends DefaultModel> int save(T model) throws Exception;
    void actualizeStorageSchema() throws Exception;
}
