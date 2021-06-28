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

    public static Comparison notEqual(Expression left, Expression right) {
        return new Comparison(left, "!=", right);
    }

    public static Comparison notEqual(String column, Object value) {
        return notEqual(new ColumnEx(column), new StringEx(value.toString()));
    }

    public static Comparison biggerThan(Expression left, Expression right) {
        return new Comparison(left, ">", right);
    }

    public static Comparison biggerThan(String column, Object value) {
        return biggerThan(new ColumnEx(column), new StringEx(value.toString()));
    }

    public static Comparison lessThan(Expression left, Expression right) {
        return new Comparison(left, "<", right);
    }

    public static Comparison lessThan(String column, Object value) {
        return lessThan(new ColumnEx(column), new StringEx(value.toString()));
    }

    public static Comparison biggerThanOrEqual(Expression left, Expression right) {
        return new Comparison(left, ">=", right);
    }

    public static Comparison biggerThanOrEqual(String column, Object value) {
        return biggerThanOrEqual(new ColumnEx(column), new StringEx(value.toString()));
    }

    public static Comparison lessThanOrEqual(Expression left, Expression right) {
        return new Comparison(left, "<=", right);
    }

    public static Comparison lessThanOrEqual(String column, Object value) {
        return lessThanOrEqual(new ColumnEx(column), new StringEx(value.toString()));
    }

    public static Comparison like(Expression left, Expression right) {
        return new Comparison(left, "LIKE", right);
    }

    public static Comparison like(String column, Object value) {
        return like(new ColumnEx(column), new StringEx(value.toString()));
    }

    public static Comparison notLike(Expression left, Expression right) {
        return new Comparison(left, "NOT LIKE", right);
    }

    public static Comparison notLike(String column, Object value) {
        return notLike(new ColumnEx(column), new StringEx(value.toString()));
    }

    public static Comparison isNull(Expression expression) {
        return new Comparison(expression, "IS", new NullEx());
    }

    public static Comparison isNull(String column) {
        return isNull(new ColumnEx(column));
    }

    public static Comparison isNotNull(Expression expression) {
        return new Comparison(expression, "IS NOT", new NullEx());
    }

    public static Comparison isNotNull(String column) {
        return isNotNull(new ColumnEx(column));
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
