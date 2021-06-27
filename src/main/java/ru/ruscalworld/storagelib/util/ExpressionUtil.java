package ru.ruscalworld.storagelib.util;

import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.builder.SerializedExpression;

import java.util.List;

public class ExpressionUtil {
    public static SerializedExpression serializeBinary(Expression left, String operation, Expression right, List<String> placeholders) {
        SerializedExpression sLeft = left.serialize(placeholders);
        SerializedExpression sRight = right.serialize(placeholders);
        return new SerializedExpression("(" + sLeft + " " + operation + " " + sRight + ")", placeholders);
    }
}
