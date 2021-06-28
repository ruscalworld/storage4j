package ru.ruscalworld.storagelib.builder.expressions;

import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.builder.SerializedExpression;

import java.util.ArrayList;
import java.util.List;

public class ColumnEx implements Expression {
    private final String string;

    public ColumnEx(String string) {
        this.string = string;
    }

    @Override
    public SerializedExpression serialize(List<String> placeholders) {
        return new SerializedExpression("`" + this.getString() + "`", placeholders);
    }

    @Override
    public SerializedExpression serialize() {
        return this.serialize(new ArrayList<>());
    }

    public String getString() {
        return string;
    }
}
