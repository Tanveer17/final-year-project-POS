package com.tanveer.controllers;

import com.tanveer.controllers.dialogcontrollers.AddExpenseController;
import com.tanveer.controllers.dialogcontrollers.AddSaleController;
import com.tanveer.controllers.dialogcontrollers.UpdateExpenseController;
import com.tanveer.controllers.dialogcontrollers.UpdateSaleController;
import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.SaleRepository;
import com.tanveer.model.expanses.Expense;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.sale.Sale;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Optional;
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
    private TableView salesTable;
    @FXML
    private Label numOfItemsSoldLabel;
    @FXML
    private  Label totalAmountGainedLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private MenuBar menuBar;
    @FXML
    private BorderPane parent;

    private ContextMenu contextMenu;


    public void initialize(){
        //init model
        itemRepository = ItemRepository.getInstance();
        saleRepository  = SaleRepository.getInstance();

        //init observable lists
        sales = saleRepository.getSales();

        setContextMenu();
        populateList();
        setupSalesTable();
        eventHandlers();

    }

    //populate this list of checkboxes to be filtered
    private void populateList(){
        items = itemRepository.getItems().stream().collect(Collectors.toMap(Item :: getId, item -> item));
        for(Item item : items.values()){
            CheckBox checkBox  = new CheckBox(item.toString());
            checkBox.setAccessibleHelp(String.valueOf(item.getId()));
            checkBox.setFocusTraversable(false);
            itemsListToBeFiltered.getItems().add(checkBox);
        }
    }

    private void setupSalesTable(){
        setRowFactory();
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

    @FXML
    public void addSuitSale(){
        addSale("Add Suit Sale","/fxml/dialogs/addSuitSaleDialog.fxml","suit sale");

    }

    @FXML
    public void addMetersSale(){
        addSale("Add Meters Sale","/fxml/dialogs/addMetersSaleDialog.fxml","meters sale");

    }

    private void addSale(String label, String resource, String cntlr){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle(label);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(resource));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException i) {
            i.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(cntlr.equalsIgnoreCase("suit sale")) {
            AddSaleController controller = fxmlLoader.getController();


            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.addSuitSale();
            }
        }
        else{
            AddSaleController controller = fxmlLoader.getController();


            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.addMetersSale();
            }
        }
    }

    private void editSale(Sale sale){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Update Sale");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/updateSaleDialog.fxml"));
        UpdateSaleController controller = new UpdateSaleController(sale);
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
            controller.updateSale();

        }



    }

    private void deleteSale(Sale sale){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Sale will be deleted");
        alert.setContentText("Sale will be removed fron history and you cant access it again");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            saleRepository.deleteSale(sale);
        }

    }

    private void eventHandlers(){
        sales.addListener((ListChangeListener<? super Sale>) c ->{
            salesTable.getItems().clear();
            salesTable.getColumns().clear();
            itemsListToBeFiltered.getItems().clear();
            populateList();
            setupSalesTable();
        });

        itemRepository.getItems().addListener(new ListChangeListener<Item>() {
            @Override
            public void onChanged(Change<? extends Item> c) {
                System.out.println("list changed");
                itemsListToBeFiltered.getItems().clear();
                populateList();
            }
        });
    }

    @FXML
    public void close(){
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    private void setContextMenu(){
        contextMenu = new ContextMenu();
        MenuItem edit  = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit,delete);

        edit.setOnAction(event -> {
            Sale sale = (Sale) salesTable.getSelectionModel().getSelectedItem();
            editSale(sale);
        });

        delete.setOnAction(event -> {
            Sale sale= (Sale) salesTable.getSelectionModel().getSelectedItem();
            deleteSale(sale);
        });
    }

    private void setRowFactory() {

        salesTable.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView param) {
                TableRow tableRow = new TableRow() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                    }
                };
                tableRow.emptyProperty().addListener((obs, wasEmpty, isEmpty) -> {
                    if (isEmpty) {
                        tableRow.setContextMenu(null);
                    } else {

                        tableRow.setContextMenu(contextMenu);
                    }
                });
                return tableRow;
            }
        });

    }


}

