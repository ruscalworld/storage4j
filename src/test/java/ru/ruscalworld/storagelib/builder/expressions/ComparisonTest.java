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

    @Test
    void notEqualString() {
        Comparison comparison = Comparison.notEqual("first string", "second string");
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`first string` != ?)", expression.toString());
    }

    @Test
    void biggerThanInteger() {
        Comparison comparison = Comparison.biggerThan("column", 1);
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`column` > ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("" + 1, placeholders.get(0));
    }

    @Test
    void lessThanInteger() {
        Comparison comparison = Comparison.lessThan("column", 1);
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`column` < ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("" + 1, placeholders.get(0));
    }

    @Test
    void biggerThanOrEqualInteger() {
        Comparison comparison = Comparison.biggerThanOrEqual("column", 1);
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`column` >= ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("" + 1, placeholders.get(0));
    }

    @Test
    void lessThanOrEqualInteger() {
        Comparison comparison = Comparison.lessThanOrEqual("column", 1);
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`column` <= ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("" + 1, placeholders.get(0));
    }

    @Test
    void isLikeString() {
        Comparison comparison = Comparison.like("first string", "second string");
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`first string` LIKE ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("second string", placeholders.get(0));
    }

    @Test
    void isNotLikeString() {
        Comparison comparison = Comparison.notLike("first string", "second string");
        SerializedExpression expression = comparison.serialize();
        assertEquals("(`first string` NOT LIKE ?)", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("second string", placeholders.get(0));
    }
}