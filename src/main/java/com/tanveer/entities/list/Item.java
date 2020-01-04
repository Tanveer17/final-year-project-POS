package com.tanveer.entities.list;

import java.util.function.Supplier;

public class Item {
    private int id;
    private String name;
    private ItemSupplier supplier;

    public Item(int id, String name, ItemSupplier supplier) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
    }

    public Item(String name, ItemSupplier supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int  id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ItemSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return name;
    }
}
