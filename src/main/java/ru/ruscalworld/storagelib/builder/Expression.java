package ru.ruscalworld.storagelib.builder;

import java.util.List;

public interface Expression {
    SerializedExpression serialize(List<String> placeholders);
    SerializedExpression serialize();
}
