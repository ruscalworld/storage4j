package ru.ruscalworld.storagelib.builder.expressions;

import org.junit.jupiter.api.Test;
import ru.ruscalworld.storagelib.builder.SerializedExpression;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConditionTest {
    @Test
    void or() {
        Condition condition = Condition.or(getLeftExpression(), getRightExpression());
        SerializedExpression expression = condition.serialize();
        assertEquals("((`first` = ?) OR (`third` = ?))", expression.toString());
        checkPlaceholders(expression);
    }

    @Test
    void and() {
        Condition condition = Condition.and(getLeftExpression(), getRightExpression());
        SerializedExpression expression = condition.serialize();
        assertEquals("((`first` = ?) AND (`third` = ?))", expression.toString());
        checkPlaceholders(expression);
    }

    @Test
    void recursive() {
        Condition condition = Condition.or(
                Condition.and(
                        Comparison.equal("1", "2"),
                        Comparison.equal("3", "4")
                ),
                Condition.or(
                        Comparison.equal("5", "6"),
                        Comparison.equal("7", "8")
                )
        );
        SerializedExpression expression = condition.serialize();
        assertEquals("(((`1` = ?) AND (`3` = ?)) OR ((`5` = ?) OR (`7` = ?)))", expression.toString());

        List<String> placeholders = expression.getPlaceholders();
        assertEquals("2", placeholders.get(0));
        assertEquals("4", placeholders.get(1));
        assertEquals("6", placeholders.get(2));
        assertEquals("8", placeholders.get(3));
    }

    Comparison getLeftExpression() {
        return Comparison.equal("first", "second");
    }

    Comparison getRightExpression() {
        return Comparison.equal("third", "fourth");
    }

    void checkPlaceholders(SerializedExpression expression) {
        List<String> placeholders = expression.getPlaceholders();
        assertEquals("second", placeholders.get(0));
        assertEquals("fourth", placeholders.get(1));
    }
}