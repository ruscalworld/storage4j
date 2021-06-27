package ru.ruscalworld.storagelib.builder.expressions;

import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.builder.SerializedExpression;

import java.util.ArrayList;
import java.util.List;

public class StringEx implements Expression {
    private final String string;

    public StringEx(String string) {
        this.string = string;
    }

    @Override
    public SerializedExpression serialize(List<String> placeholders) {
        placeholders.add(this.getString());
        return new SerializedExpression("?", placeholders);
    }

    @Override
    public SerializedExpression serialize() {
        return this.serialize(new ArrayList<>());
    }

    public String getString() {
        return string;
    }
}
