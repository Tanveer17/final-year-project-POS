package com.tanveer.model.stocks;

import com.tanveer.model.purchases.Item;

public class Stock {
    private Item item;
    private double totalMeterPurchases;
    private double totalPiecesPurchases;
    private double currentlyInStockMeters;
    private double currentlyInStockPieces;

    public Stock() {
    }

    public Stock(Item item) {
        this.item = item;
    }

    public Stock(Item item, double totalMeterPurchases, double totalPiecesPurchases, double currentlyInStockMeters, double currentlyInStockPieces) {
        this.item = item;
        this.totalMeterPurchases = totalMeterPurchases;
        this.totalPiecesPurchases = totalPiecesPurchases;
        this.currentlyInStockMeters = currentlyInStockMeters;
        this.currentlyInStockPieces = currentlyInStockPieces;
    }

    public Stock(double totalMeterPurchases, double totalPiecesPurchases, double currentlyInStockMeters, double currentlyInStockPieces) {
        this.totalMeterPurchases = totalMeterPurchases;
        this.totalPiecesPurchases = totalPiecesPurchases;
        this.currentlyInStockMeters = currentlyInStockMeters;
        this.currentlyInStockPieces = currentlyInStockPieces;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getTotalMeterPurchases() {
        return totalMeterPurchases;
    }

    public void setTotalMeterPurchases(double totalMeterPurchases) {
        this.totalMeterPurchases = totalMeterPurchases;
    }

    public double getTotalPiecesPurchases() {
        return totalPiecesPurchases;
    }

    public void setTotalPiecesPurchases(double totalPiecesPurchases) {
        this.totalPiecesPurchases = totalPiecesPurchases;
    }

    public double getCurrentlyInStockMeters() {
        return currentlyInStockMeters;
    }

    public void setCurrentlyInStockMeters(double currentlyInStockMeters) {
        this.currentlyInStockMeters = currentlyInStockMeters;
    }

    public double getCurrentlyInStockPieces() {
        return currentlyInStockPieces;
    }

    public void setCurrentlyInStockPieces(double currentlyInStockPieces) {
        this.currentlyInStockPieces = currentlyInStockPieces;
    }

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj ){
            return true;
        }

        if(obj instanceof Stock) {
            Stock stock = (Stock) obj;
            if(stock.getItem().getId() == this.getItem().getId()){
                return true;
            }
        }

        return false;

    }
}
