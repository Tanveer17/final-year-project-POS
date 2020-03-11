package com.tanveer.controllers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.SaleRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.sale.Sale;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

public class SalesController {
    private ItemRepository itemRepository;
    private SaleRepository saleRepository;
    private ObservableList<Sale> sales;
    private Map<Integer,Item> items;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private ListView<CheckBox> itemsListToBeFiltered;
    @FXML
    private TableView<Sale> salesTable;
    @FXML
    private Label numOfItemsSoldLabel;
    @FXML
    private  Label totalAmountGainedLabel;
    @FXML
    private Label profitLabel;


    public void initialize(){
        //init model
        itemRepository = new ItemRepository();
        saleRepository  = new SaleRepository();

        //init observable lists
        items = itemRepository.getItems().stream().collect(Collectors.toMap(Item :: getId, item -> item));
        sales = saleRepository.getSales();

        populateList();
        setupSalesTable();

    }

    //populate this list of checkboxes to be filtered
    private void populateList(){
        for(Item item : items.values()){
            CheckBox checkBox  = new CheckBox(item.toString());
            checkBox.setAccessibleHelp(String.valueOf(item.getId()));
            checkBox.setFocusTraversable(false);
            itemsListToBeFiltered.getItems().add(checkBox);
        }
    }

    private void setupSalesTable(){
        TableColumn<Sale, StringProperty> itemColumn = new TableColumn<>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        TableColumn<Sale, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        TableColumn<Sale, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("saleTime"));
        TableColumn<Sale, DoubleProperty> noOfMetersSoldColumn = new TableColumn<>("Meters Sold");
        noOfMetersSoldColumn.setCellValueFactory(new PropertyValueFactory<>("noOfMetersSold"));
        TableColumn<Sale, DoubleProperty> noOfPiecesSoldColumn = new TableColumn<>("Pieces Sold");
        noOfPiecesSoldColumn.setCellValueFactory(new PropertyValueFactory<>("noOfPiecesSold"));
        TableColumn<Sale, DoubleProperty> saleAmountColumn = new TableColumn<>("Amount");
        saleAmountColumn.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        TableColumn<Sale, DoubleProperty> profitColumn = new TableColumn<>("Profit");
        profitColumn.setCellValueFactory(new PropertyValueFactory<>("profit"));

        salesTable.getColumns().addAll(itemColumn,dateColumn,timeColumn,noOfMetersSoldColumn,noOfPiecesSoldColumn,saleAmountColumn,profitColumn);
        salesTable.getItems().addAll(sales);
        setLabels(sales);
    }

    //filter by date and/or items
    @FXML
    public void filter(){
        List<Item> itemList = new LinkedList<>();//list for selected item
        ObservableList<Sale> filteredList;


        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();

        boolean datesPresent = false;
        // checking of the dates are not null both
        if(from != null && to != null){
            if( from.isAfter(to)){
                Alert invalid = new Alert(Alert.AlertType.WARNING);
                invalid.setHeaderText("Invalid Period");
                invalid.setContentText("From is After To");
                invalid.showAndWait();
                return;
            }

            else{
                datesPresent = true;
            }
        }

        //checking if one of the date is not selcted
        else if((from == null && to != null) || (from != null && to == null)){
            Alert invalid = new Alert(Alert.AlertType.WARNING);
            invalid.setHeaderText("Invalid Period");
            invalid.setContentText("Please Select both dates");
            invalid.showAndWait();
            return;
        }


        //get the selected items fro  list
        for(CheckBox checkBox : itemsListToBeFiltered.getItems()){
            if(checkBox.isSelected()) {
                int itemId = Integer.valueOf(checkBox.getAccessibleHelp());
                Item item = items.get(itemId);
                itemList.add(item);
            }
        }


        //if nothing is selected at all
        if(!datesPresent && itemList.isEmpty()){
            Alert invalid = new Alert(Alert.AlertType.WARNING);
            invalid.setHeaderText("Nothing Selected");
            invalid.setContentText("No Date Or Item Selected");
            invalid.showAndWait();
            return;
        }


        if(datesPresent && !itemList.isEmpty()){
            System.out.println(itemList.contains(sales.get(0)));
            filteredList = FXCollections.observableArrayList(sales.stream()
                    .filter(s -> (s.getSaleDate().isEqual(from) || s.getSaleDate().isAfter(from) && s.getSaleDate().isBefore(to)))
                    .filter(s -> itemList.contains(s.getItem())).collect(Collectors.toList()));
        }

        else if(datesPresent){
            filteredList = FXCollections.observableArrayList(sales.stream()
                    .filter(s -> (s.getSaleDate().isEqual(from) || s.getSaleDate().isAfter(from) && s.getSaleDate().isBefore(to)))
                    .collect(Collectors.toList()));
        }
        else{
            System.out.println(itemList.contains(sales.get(0)));
            filteredList = FXCollections.observableArrayList(sales.stream()
                    .filter(s -> itemList.contains(s.getItem())).collect(Collectors.toList()));
        }
        salesTable.getItems().clear();

        salesTable.getItems().addAll(filteredList);

        setLabels(filteredList);
        System.out.println(filteredList);



    }

    //setting the labels data
    private void setLabels(ObservableList<Sale> sales){
        numOfItemsSoldLabel.setText(String.valueOf(sales.size()));

        totalAmountGainedLabel.setText(String.valueOf(sales.stream().mapToDouble(Sale :: getSaleAmount).sum()));

        profitLabel.setText(String.valueOf(sales.stream().mapToDouble(Sale :: getProfit).sum()));
    }

    //clear all filters ie date and items
    @FXML
    public void clearFilters(){
        //clearing list of item
        for(CheckBox checkBox : itemsListToBeFiltered.getItems()){
            checkBox.setSelected(false);
        }

        //clearing dates
        fromDate.getEditor().clear();
        toDate.getEditor().clear();
        fromDate.setValue(null);
        toDate.setValue(null);
        System.out.println(fromDate.getValue());

        salesTable.getItems().clear();
        salesTable.getItems().addAll(sales);
        setLabels(sales);


    }


}
