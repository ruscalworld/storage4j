package ru.ruscalworld.storagelib;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Storage {
    void setup() throws Exception;
    <T> T find(@NotNull Class<T> clazz, String key, Object value) throws Exception;
    <T> List<T> findAll(@NotNull Class<T> clazz, String key, Object value) throws Exception;
    <T> T retrieve(Class<T> clazz, long id) throws Exception;
    <T> List<T> retrieveAll(Class<T> clazz) throws Exception;
    <T extends DefaultModel> long save(T model) throws Exception;
    <T extends DefaultModel> void delete(T model) throws Exception;
    <T> void deleteAll(Class<T> clazz, String key, Object value) throws Exception;
    ConverterProvider getConverterProvider();
    void registerConverter(Class<?> clazz, Converter<?> converter);
    void actualizeStorageSchema() throws Exception;
}
