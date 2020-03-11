package com.tanveer.model.sale;

import com.tanveer.model.purchases.Item;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Sale {
    private LongProperty id = new SimpleLongProperty();
    private Item item;
    private LocalDate saleDate;
    private String saleTime;
    private DoubleProperty noOfMetersSold = new SimpleDoubleProperty();
    private DoubleProperty noOfPiecesSold = new SimpleDoubleProperty();
    private DoubleProperty saleAmount = new SimpleDoubleProperty();
    private DoubleProperty profit = new SimpleDoubleProperty();

    public Sale(long id,Item item,LocalDate saleDate,String saleTime,double noOfMetersSold,double noOfPiecesSold, double saleAmount,double profit) {
        setId(id);
        this.item = item;
        this.saleDate = saleDate;
        this.saleTime = saleTime;
        setNoOfMetersSold(noOfMetersSold);
        setNoOfPiecesSold(noOfPiecesSold);
        setSaleAmount(saleAmount);
        setProfit(profit);
    }

    public Sale(Item item,LocalDate saleDate,String saleTime,double saleAmount,double profit) {
        this.item = item;
        this.saleDate = saleDate;
        this.saleTime = saleTime;
        setSaleAmount(saleAmount);
        setProfit(profit);
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public double getNoOfMetersSold() {
        return noOfMetersSold.get();
    }

    public DoubleProperty noOfMetersSoldProperty() {
        return noOfMetersSold;
    }

    public void setNoOfMetersSold(double noOfMetersSold) {
        this.noOfMetersSold.set(noOfMetersSold);
    }

    public double getNoOfPiecesSold() {
        return noOfPiecesSold.get();
    }

    public DoubleProperty noOfPiecesSoldProperty() {
        return noOfPiecesSold;
    }

    public void setNoOfPiecesSold(double noOfPiecesSold) {
        this.noOfPiecesSold.set(noOfPiecesSold);
    }

    public double getSaleAmount() {
        return saleAmount.get();
    }

    public DoubleProperty saleAmountProperty() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount.set(saleAmount);
    }

    public double getProfit() {
        return profit.get();
    }

    public DoubleProperty profitProperty() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit.set(profit);
    }


}
