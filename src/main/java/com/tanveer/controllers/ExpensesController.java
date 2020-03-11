package com.tanveer.controllers;

import com.tanveer.model.database.ExpenseRepository;
import com.tanveer.model.expanses.Expense;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
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


    public void initialize(){
        expenseRepository = new ExpenseRepository();
        expenseList = expenseRepository.getExpenseList();
        setExpensesTable();

        setContextMenu();

        eventHandlers();


    }


    // set the top menu bar
    private void setContextMenu(){
        contextMenu = new ContextMenu();
        MenuItem edit  = new MenuItem("Edit");
        contextMenu.getItems().add(edit);

        edit.setOnAction(event -> {
                Expense expense = (Expense) expansesTable.getSelectionModel().getSelectedItem();
            });
        }


        //setting event handlers
    private void eventHandlers(){
        expenseList.addListener((ListChangeListener<? super Expense>) c -> {
                expansesTable.getItems().add(expenseList.get(expenseList.size() - 1));
                setLabels();
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

        expansesTable.getColumns().addAll(descriptionColumn,dateColumn,amountColumn);
        expansesTable.getItems().addAll(expenseList);
        setLabels();


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
