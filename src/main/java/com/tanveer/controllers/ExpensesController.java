package com.tanveer.controllers;

import com.tanveer.controllers.dialogcontrollers.AddExpenseController;
import com.tanveer.controllers.dialogcontrollers.AddSupplierController;
import com.tanveer.controllers.dialogcontrollers.UpdateExpenseController;
import com.tanveer.controllers.dialogcontrollers.UpdateItemController;
import com.tanveer.model.database.ExpenseRepository;
import com.tanveer.model.expanses.Expense;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.border.Border;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpensesController {
    private ContextMenu contextMenu;
    private ObservableList<Expense> expenseList;
    private ExpenseRepository expenseRepository;

    @FXML
    private MenuBar menuBar;
    @FXML
    private DatePicker expansesFrom;
    @FXML
    private DatePicker expansesTo;
    @FXML
    private TableView expansesTable;
    @FXML
    private Label todayExpansesLabel;
    @FXML
    private Label thisMonthExpansesLabel;
    @FXML
    private Label lastMonthExpansesLabel;
    @FXML
    private Label totalExpansesInTheTableLabel;
    @FXML
    private BorderPane parent;


    public void initialize(){
        expenseRepository = ExpenseRepository.getInstance();
        expenseList = expenseRepository.getExpenseList();
        setExpensesTable();

        setContextMenu();

        eventHandlers();


    }


    // set the top menu bar
    private void setContextMenu(){
        contextMenu = new ContextMenu();
        MenuItem edit  = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit,delete);

        edit.setOnAction(event -> {
                Expense expense = (Expense) expansesTable.getSelectionModel().getSelectedItem();
                editExpense(expense);
            });

        delete.setOnAction(event -> {
            Expense expense = (Expense) expansesTable.getSelectionModel().getSelectedItem();
                 deleteExpense(expense);
        });
        }


        //setting event handlers
    private void eventHandlers(){
        expenseList.addListener((ListChangeListener<? super Expense>) c -> {
            expansesTable.getColumns().clear();
            expansesTable.getItems().clear();
            setExpensesTable();
        });


    }

    // set expenses period
    private void setExpensesTable() {
        setRowFactory();

        TableColumn<Expense, StringProperty> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Expense, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Expense, DoubleProperty> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
//

        expansesTable.getColumns().addAll(descriptionColumn,dateColumn,amountColumn);
        expansesTable.getItems().addAll(expenseList);
        setLabels();


    }

    private void editExpense(Expense expense){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Update Expense");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/updateExpenseDialog.fxml"));
        UpdateExpenseController controller = new UpdateExpenseController(expense);
        fxmlLoader.setController(controller);



        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException i) {
            i.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateExpense();

        }

    }

    private void deleteExpense(Expense expense){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Expense will be deleted");
        alert.setContentText("Expense will be removed fron history and you cant access it again");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            expenseRepository.deleteExpense(expense);
        }
    }




    // filter expenses accroding to the specified period
    @FXML
    public void filterExpansesPeriod(){
        LocalDate from = expansesFrom.getValue();
        LocalDate to  = expansesTo.getValue();

        if(from == null || to == null){
            Alert invalid = new Alert(Alert.AlertType.WARNING);
            invalid.setHeaderText("Select Period");
            invalid.setContentText("Please Select the period to filter");
            invalid.showAndWait();
        }
        else if( from.isAfter(to)){
            Alert invalid = new Alert(Alert.AlertType.WARNING);
            invalid.setHeaderText("Invalid Period");
            invalid.setContentText("From is After To");
            invalid.showAndWait();
        }
        else{
            ObservableList<Expense> filteredexpanses;
            filteredexpanses = FXCollections.observableArrayList(expenseList.stream()
                    .filter(expense -> (expense.getDate().isAfter(from) || expense.getDate().isEqual(from)) && (expense.getDate().isBefore(to) || expense.getDate().isEqual(to)))
                    .collect(Collectors.toList()));
            expansesTable.getItems().clear();
            expansesTable.getItems().addAll(filteredexpanses);
            setTotalExpensesLabel(filteredexpanses);



        }

    }

    //set the total expenses label which changes frequently due to the filter
    private void setTotalExpensesLabel(ObservableList<Expense> list){
        double totalExpanses = list.stream().mapToDouble(expense -> expense.getAmount()).sum();
        totalExpansesInTheTableLabel.setText(String.valueOf(totalExpanses));
    }

    //set all the labesls
    private void setLabels(){

        double todaysExpanses = expenseList.stream().filter(expense -> expense.getDate().isEqual(LocalDate.now()))
                .mapToDouble(expense -> expense.getAmount()).sum();

        double thisMonthExpanses = expenseList.stream().filter(expense -> expense.getDate().getMonth().equals(LocalDate.now().getMonth()))
                .mapToDouble(expense -> expense.getAmount()).sum();

        double lastMonthExpanses = expenseList.stream().filter(expense -> expense.getDate().getMonth().equals(LocalDate.now().getMonth().minus(1)))
                .mapToDouble(expense -> expense.getAmount()).sum();
        setTotalExpensesLabel(expenseList);
        todayExpansesLabel.setText((String.valueOf(todaysExpanses)));
        thisMonthExpansesLabel.setText(String.valueOf(thisMonthExpanses));
        lastMonthExpansesLabel.setText(String.valueOf(lastMonthExpanses));
    }
    
    //clear period filter
    @FXML
    public void clearFilter(){
        expansesFrom.getEditor().clear();
        expansesTo.getEditor().clear();
        expansesTable.getItems().clear();
        expansesTable.getItems().addAll(expenseList);
        expansesFrom.setValue(null);
        expansesTo.setValue(null);
       setTotalExpensesLabel(expenseList);
    }


    @FXML
    public void addExpanse(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Add Expense");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/addExpenseDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException i) {
            i.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        AddExpenseController controller = fxmlLoader.getController();


        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.addExpense();
        }

    }

    @FXML
    public void close(){
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();

    }


    private void setRowFactory(){

        expansesTable.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView param) {
                TableRow tableRow = new TableRow(){
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                    }
                };
                tableRow.emptyProperty().addListener((obs,wasEmpty,isEmpty) ->{
                    if(isEmpty){
                        tableRow.setContextMenu(null);
                    }
                    else{

                        tableRow.setContextMenu(contextMenu);
                    }
                });
                return tableRow;
            }
        });
    }




}
