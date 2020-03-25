package com.tanveer.model.purchases;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.time.LocalDate;

public class ItemPriceHistory {
    private int priceId;
    private Item item;
    private DoubleProperty pricePerSuit = new SimpleDoubleProperty();
    private DoubleProperty pricePerMeter = new SimpleDoubleProperty();
    private LocalDate fromDate;
    private LocalDate toDate;

    public ItemPriceHistory(int priceId, Item item,double pricePerSuit,double pricePerMeter,LocalDate fromDate, LocalDate toDate) {
        this.priceId = priceId;
        this.item = item;
        setPricePerSuit(pricePerSuit);
        setPricePerMeter(pricePerMeter);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    public ItemPriceHistory(Item item,double pricePerSuit,double pricePerMeter,LocalDate fromDate, LocalDate toDate) {
        this.priceId = priceId;
        this.item = item;
        setPricePerSuit(pricePerSuit);
        setPricePerMeter(pricePerMeter);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getPricePerSuit() {
        return pricePerSuit.get();
    }

    public DoubleProperty pricePerSuitProperty() {
        return pricePerSuit;
    }

    public void setPricePerSuit(double pricePerSuit) {
        this.pricePerSuit.set(pricePerSuit);
    }

    public double getPricePerMeter() {
        return pricePerMeter.get();
    }

    public DoubleProperty pricePerMeterProperty() {
        return pricePerMeter;
    }

    public void setPricePerMeter(double pricePerMeter) {
        this.pricePerMeter.set(pricePerMeter);
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
