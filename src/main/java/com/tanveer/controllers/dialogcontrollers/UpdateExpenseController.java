package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ExpenseRepository;
import com.tanveer.model.expanses.Expense;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateExpenseController implements Initializable {
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    @FXML
    private DatePicker date;
    private ExpenseRepository expenseRepository;
    private Expense expense;

    public UpdateExpenseController(Expense expense) {
        this.expense = expense;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expenseRepository = ExpenseRepository.getInstance();
        initData();
    }

    public void updateExpense(){
        if(isDataValid()) {
            String description = this.description.getText();
            double amount = Double.valueOf(this.amount.getText());
            LocalDate date = LocalDate.now();
            expenseRepository.updateExpense(new Expense(description, date, amount),expense);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add valid data");
            alert.setContentText("Please fill the fields with valid data");
            alert.show();
        }

    }

    private boolean isDataValid(){

        if(!description.getText().isEmpty() && !amount.getText().isEmpty() && date.getValue() !=null){
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

    private void initData(){
        String des = expense.getDescription();
        String amt = String.valueOf(expense.getAmount());
        LocalDate dte = expense.getDate();

        description.setText(des);
        amount.setText(amt);
        date.setValue(dte);
    }
}
