package com.tanveer.model.purchases;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;
import java.util.Objects;

public class PurchaseItem {
    private IntegerProperty itemId = new SimpleIntegerProperty();
    private IntegerProperty purchaseId = new SimpleIntegerProperty();
    private LocalDate purchaseDate;
    private DoubleProperty pricePerMeter = new SimpleDoubleProperty();
    private DoubleProperty noOfMetersPurchased = new SimpleDoubleProperty();
    private DoubleProperty pricePerPiece = new SimpleDoubleProperty();
    private DoubleProperty noOfPiecesPurchased = new SimpleDoubleProperty();
    private DoubleProperty totalPrice = new SimpleDoubleProperty();
    private DoubleProperty pricePaid = new SimpleDoubleProperty();
    private DoubleProperty priceRem  = new SimpleDoubleProperty();

    public PurchaseItem(int itemId, int purchaseId, LocalDate purchaseDate, double pricePerMeter, double noOfMetersPurchased, double pricePerPiece, double noOfPiecesPurchased,
                        double totalPrice, double pricePaid, double priceRem){
        this.setItemId(itemId);
        this.setPurchaseId(purchaseId);
        this.purchaseDate = purchaseDate;
        this.setPricePerMeter(pricePerMeter);
        this.setNoOfMetersPurchased(noOfMetersPurchased);
        this.setPricePerPiece(pricePerPiece);
        this.setNoOfPiecesPurchased(noOfPiecesPurchased);
        this.setTotalPrice(totalPrice);
        this.setPricePaid(pricePaid);
        this.setPriceRem(priceRem);

    }
    public PurchaseItem(int itemId, LocalDate purchaseDate, double pricePerMeter, double noOfMetersPurchased, double pricePerPiece, double noOfPiecesPurchased,
                        double totalPrice, double pricePaid, double priceRem){
        this.setItemId(itemId);
        this.purchaseDate = purchaseDate;
        this.setPricePerMeter(pricePerMeter);
        this.setNoOfMetersPurchased(noOfMetersPurchased);
        this.setPricePerPiece(pricePerPiece);
        this.setNoOfPiecesPurchased(noOfPiecesPurchased);
        this.setTotalPrice(totalPrice);
        this.setPricePaid(pricePaid);
        this.setPriceRem(priceRem);

    }

    public PurchaseItem(LocalDate purchaseDate, double pricePerMeter, double noOfMetersPurchased,
                        double totalPrice, double pricePaid, double priceRem){
        this.purchaseDate = purchaseDate;
        this.setPricePerMeter(pricePerMeter);
        this.setNoOfMetersPurchased(noOfMetersPurchased);
        this.setTotalPrice(totalPrice);
        this.setPricePaid(pricePaid);
        this.setPriceRem(priceRem);
    }


    public int getItemId() {
        return itemId.get();
    }

    public IntegerProperty itemIdProperty() {
        return itemId;
    }

    public void setItemId(int id) {
        this.itemId.set(id);
    }

    public int getPurchaseId() {
        return purchaseId.get();
    }

    public IntegerProperty purchaseIdProperty() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId.set(purchaseId);
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public double getNoOfMetersPurchased() {
        return noOfMetersPurchased.get();
    }

    public DoubleProperty noOfMetersPurchasedProperty() {
        return noOfMetersPurchased;
    }

    public void setNoOfMetersPurchased(double noOfMetersPurchased) {
        this.noOfMetersPurchased.set(noOfMetersPurchased);
    }

    public double getPricePerPiece() {
        return pricePerPiece.get();
    }

    public DoubleProperty pricePerPieceProperty() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece.set(pricePerPiece);
    }

    public double getNoOfPiecesPurchased() {
        return noOfPiecesPurchased.get();
    }

    public DoubleProperty noOfPiecesPurchasedProperty() {
        return noOfPiecesPurchased;
    }

    public void setNoOfPiecesPurchased(double noOfPiecesPurchased) {
        this.noOfPiecesPurchased.set(noOfPiecesPurchased);
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public double getPricePaid() {
        return pricePaid.get();
    }

    public DoubleProperty pricePaidProperty() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid.set(pricePaid);
    }

    public double getPriceRem() {
        return priceRem.get();
    }

    public DoubleProperty priceRemProperty() {
        return priceRem;
    }

    public void setPriceRem(double priceRem) {
        this.priceRem.set(priceRem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseItem item = (PurchaseItem) o;
        return Objects.equals(getPurchaseId(), item.getPurchaseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPurchaseId());
    }
}
