package ru.ruscalworld.storagelib;

import ru.ruscalworld.storagelib.annotations.Property;

public class DefaultModel {
    @Property(column = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
