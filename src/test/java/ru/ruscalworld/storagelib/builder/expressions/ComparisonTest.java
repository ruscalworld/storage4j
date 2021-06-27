package ru.ruscalworld.storagelib.builder.expressions;

import org.junit.jupiter.api.Test;
import ru.ruscalworld.storagelib.builder.SerializedExpression;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonTest {
    @Test
    void equalString() {
        Comparison comparison = Comparison.equal("first string", "second string");
        SerializedExpression expression = comparison.serialize();
        assertEquals("(? = ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("first string", placeholders.get(0));
        assertEquals("second string", placeholders.get(1));
    }
}