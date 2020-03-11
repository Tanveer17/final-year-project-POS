package com.tanveer.model.expanses;

import com.sun.org.glassfish.gmbal.Description;
import javafx.beans.property.*;

import java.time.LocalDate;

public class Expense {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty description = new SimpleStringProperty();
    private LocalDate date;
    private DoubleProperty amount = new SimpleDoubleProperty();

    public Expense(int id, String description, LocalDate date, double amount) {
        this.setId(id);
        this.setDescription(description);
        this.date = date;
        this.setAmount(amount);
    }

    public Expense(String description, LocalDate date, double amount) {
        this.setDescription(description);
        this.date = date;
        this.setAmount(amount);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }
}
