package ru.ruscalworld.storagelib;

public interface Converter<T> {
    T convert(Object value);
}
