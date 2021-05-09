package ru.ruscalworld.storagelib;

import ru.ruscalworld.storagelib.annotations.Property;

public class DefaultModel {
    @Property(column = "id")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
