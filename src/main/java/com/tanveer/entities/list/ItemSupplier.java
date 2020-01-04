package com.tanveer.entities.list;

import java.util.List;

public class ItemSupplier {
    private int id;
    private String name;
    private String address;

    public ItemSupplier(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;

    }

    public ItemSupplier(String name, String address) {
        this.name = name;
        this.address = address;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name;
    }
}
