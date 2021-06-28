package ru.ruscalworld.storagelib.builder.expressions;

import ru.ruscalworld.storagelib.builder.Expression;
import ru.ruscalworld.storagelib.builder.SerializedExpression;
import ru.ruscalworld.storagelib.util.ExpressionUtil;

import java.util.ArrayList;
import java.util.List;

public class Comparison implements Expression {
    private Expression left;
    private Expression right;
    private String operation;

    public Comparison(Expression left, String operation, Expression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public static Comparison equal(Expression left, Expression right) {
        return new Comparison(left, "=", right);
    }

    public static Comparison equal(String column, Object value) {
        return equal(new ColumnEx(column), new StringEx(value.toString()));
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
