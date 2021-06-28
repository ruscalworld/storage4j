package ru.ruscalworld.storagelib.builder.expressions;

import org.junit.jupiter.api.Test;
import ru.ruscalworld.storagelib.builder.SerializedExpression;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparisonTest {
    @Test
    void equalString() {
        Comparison comparison = Comparison.equal("first string", "second string");
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`first string` = ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("second string", placeholders.get(0));
    }
}