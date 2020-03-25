package com.tanveer.model.purchases;

public class Item {
    private int id;
    private String name;
    private ItemSupplier supplier;
    private double pricePerSuit;
    private double pricePerMeter;
    private double metersInASuit;
    private double profit_percentage;

    public Item(int id) {
        this.id = id;
    }

    public Item(int id, String name, ItemSupplier supplier, double pricePerSuit, double pricePerMeter, double metersInASuit, double profit_percentage) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.pricePerSuit = pricePerSuit;
        this.pricePerMeter = pricePerMeter;
        this.metersInASuit = metersInASuit;
        this.profit_percentage = profit_percentage;
    }

    public Item(int id, String name, ItemSupplier supplier, double pricePerSuit, double pricePerMeter, double metersInASuit) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.pricePerSuit = pricePerSuit;
        this.pricePerMeter = pricePerMeter;
        this.metersInASuit = metersInASuit;
    }

    public Item(String name, ItemSupplier supplier, double pricePerSuit, double pricePerMeter, double metersInASuit, double profit_percentage) {
        this.name = name;
        this.supplier = supplier;
        this.pricePerSuit = pricePerSuit;
        this.pricePerMeter = pricePerMeter;
        this.metersInASuit = metersInASuit;
        this.profit_percentage = profit_percentage;
    }

    public Item(String name, ItemSupplier supplier, double pricePerSuit, double pricePerMeter, double metersInASuit) {
        this.name = name;
        this.supplier = supplier;
        this.pricePerSuit = pricePerSuit;
        this.pricePerMeter = pricePerMeter;
        this.metersInASuit = metersInASuit;
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

    public double getPricePerSuit() {
        return pricePerSuit;
    }

    public void setPricePerSuit(double pricePerSuit) {
        this.pricePerSuit = pricePerSuit;
    }

    public double getPricePerMeter() {
        return pricePerMeter;
    }

    public void setPricePerMeter(double pricePerMeter) {
        this.pricePerMeter = pricePerMeter;
    }

    public double getMetersInASuit() {
        return metersInASuit;
    }

    public void setMetersInASuit(double metersInASuit) {
        this.metersInASuit = metersInASuit;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Item) ){
            return false;
        }

        Item item = (Item)o;

        if(item.getId() == this.getId()){
            return true;
        }

        return false;
    }
}
