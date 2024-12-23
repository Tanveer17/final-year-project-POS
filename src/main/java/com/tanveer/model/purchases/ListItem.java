package com.tanveer.model.purchases;

public class ListItem {
    private int id;
    private String name;

    public ListItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ListItem(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }
}
