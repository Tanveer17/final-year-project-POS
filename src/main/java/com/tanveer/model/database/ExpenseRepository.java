package com.tanveer.model.database;

import com.tanveer.model.expanses.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.time.LocalDate;

public class ExpenseRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Expense> expenses;

    public ExpenseRepository(){
        expenses  = FXCollections.observableArrayList();
        setDatabaseExpensesData();

    }

    public ObservableList<Expense> getExpenseList(){
        return expenses;
    }

    private void setDatabaseExpensesData(){
        expenses.add(new Expense(1,"salaery", LocalDate.now(),44.4));
        expenses.add(new Expense(2,"salaery", LocalDate.now(),105.4));
    }

}
