package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ExpenseRepository;
import com.tanveer.model.expanses.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDate;


public class AddExpenseController {
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    private ExpenseRepository expenseRepository;

    public void initialize(){
        expenseRepository = ExpenseRepository.getInstance();
    }



    public void addExpense(){
        if(isDataValid()) {
            String description = this.description.getText();
            double amount = Double.valueOf(this.amount.getText());
            LocalDate date = LocalDate.now();
            expenseRepository.addExpense(new Expense(description, date, amount));
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add valid data");
            alert.setContentText("Please fill the fields with valid data");
            alert.show();
        }


    }

    private boolean isDataValid(){

        if(!description.getText().isEmpty() && !amount.getText().isEmpty()){
            try{
                Double.parseDouble(amount.getText());
            }
            catch (NumberFormatException e){
                return false;
            }
        }
        else{
            return false;
        }
        return true;
    }
}
