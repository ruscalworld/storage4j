package ru.ruscalworld.storagelib.builder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SerializedExpression {
    private final String string;
    private final List<String> placeholders;

    public SerializedExpression(String string, List<String> placeholders) {
        this.string = string;
        this.placeholders = placeholders;
    }

    public void processStatement(PreparedStatement statement, int offset) throws SQLException {
        List<String> placeholders = this.getPlaceholders();
        for (int i = 0; i < placeholders.size(); i++) {
            String string = placeholders.get(i);
            statement.setString(offset + i + 1, string);
        }
    }

    public int getPlaceholderOffset() {
        return this.getPlaceholderOffset(0);
    }

    public int getPlaceholderOffset(int previous) {
        return this.getPlaceholders().size() + previous;
    }

    public String toString() {
        return this.string;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }
}
