package com.tanveer.model.database;

import com.tanveer.model.expanses.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        try(Statement statement = connection.createStatement()){
            String sql = "SELECT * FROM expenses";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String description = resultSet.getString(2);
                LocalDate date = resultSet.getDate(3).toLocalDate();
                double amount = resultSet.getDouble(4);

                expenses.add(new Expense(id,description,date,amount));

            }

        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }

}
