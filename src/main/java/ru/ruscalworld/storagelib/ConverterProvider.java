package ru.ruscalworld.storagelib;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

public class ConverterProvider {
    private final HashMap<Class<?>, Converter<?>> converters = new HashMap<>();

    public ConverterProvider() {
        // Register default types
        this.registerConverter(int.class, (v) -> Integer.parseInt(v.toString()));
        this.registerConverter(long.class, (v) -> Long.parseLong(v.toString()));
        this.registerConverter(float.class, (v) -> Float.parseFloat(v.toString()));
        this.registerConverter(double.class, (v) -> Double.parseDouble(v.toString()));
        this.registerConverter(boolean.class, (v) -> v.equals(1) || v.equals("true"));

        this.registerConverter(UUID.class, (v) -> v == null ? null : UUID.fromString(v.toString()));
        this.registerConverter(Timestamp.class, (v) -> v == null ? null : Timestamp.valueOf(v.toString()));
    }

    public void registerConverter(Class<?> clazz, Converter<?> converter) {
        this.converters.put(clazz, converter);
    }

    public Converter<?> getConverter(Class<?> clazz) {
        return this.converters.get(clazz);
    }
}
