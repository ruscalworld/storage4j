package ru.ruscalworld.storagelib.builder.expressions;

import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.builder.SerializedExpression;
import ru.ruscalworld.storagelib.util.ExpressionUtil;

import java.util.ArrayList;
import java.util.List;

public class Condition implements Expression {
    private Expression left;
    private Expression right;
    private String operation;

    public Condition(Expression left, String operation, Expression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public static Condition or(Expression left, Expression right) {
        return new Condition(left, "OR", right);
    }

    public static Condition and(Expression left, Expression right) {
        return new Condition(left, "AND", right);
    }

    @Override
    public SerializedExpression serialize(List<String> placeholders) {
        return ExpressionUtil.serializeBinary(this.getLeft(), this.getOperation(), this.getRight(), placeholders);
    }

    @Override
    public SerializedExpression serialize() {
        return this.serialize(new ArrayList<>());
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
