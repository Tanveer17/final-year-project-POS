package com.tanveer.model.database;

import com.tanveer.model.expanses.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class ExpenseRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Expense> expenses;
    private static ExpenseRepository expenseRepository  = new ExpenseRepository();

    private ExpenseRepository(){
        expenses  = FXCollections.observableArrayList();
        setDatabaseExpensesData();

    }

    public static ExpenseRepository getInstance(){
        return expenseRepository;
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

    public void addExpense(Expense expense){
        String sql = "INSERT INTO expenses(description,expense_Date,amount) VALUES(?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,expense.getDescription());
            preparedStatement.setDate(2,Date.valueOf(expense.getDate()));
            preparedStatement.setDouble(3,expense.getAmount());

            preparedStatement.execute();
            expenses.add(expense);

        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    public void updateExpense(Expense expense, Expense oldExpense){
        String sql = "UPDATE expenses SET description = ?, expense_Date = ?, amount = ? WHERE id  = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,expense.getDescription());
            preparedStatement.setDate(2,Date.valueOf(expense.getDate()));
            preparedStatement.setDouble(3,expense.getAmount());
            preparedStatement.setInt(4,oldExpense.getId());

            preparedStatement.execute();
            int index = expenses.indexOf(oldExpense);
            expenses.set(index,expense);
        }
        catch (SQLException s){
            s.printStackTrace();
        }

    }

    public void deleteExpense(Expense expense){
        String sql = "DELETE FROM expenses  WHERE id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,expense.getId());

            preparedStatement.execute();

            expenses.remove(expense);
        }
        catch (SQLException s){
            s.printStackTrace();
        }

    }

}
