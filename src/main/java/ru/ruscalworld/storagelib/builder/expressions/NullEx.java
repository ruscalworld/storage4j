package ru.ruscalworld.storagelib.builder.expressions;

import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.builder.SerializedExpression;

import java.util.ArrayList;
import java.util.List;

public class NullEx implements Expression {
    @Override
    public SerializedExpression serialize(List<String> placeholders) {
        return new SerializedExpression("NULL", placeholders);
    }

    @Override
    public SerializedExpression serialize() {
        return this.serialize(new ArrayList<>());
    }
}
