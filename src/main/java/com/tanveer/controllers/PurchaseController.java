package com.tanveer.controllers;

import com.tanveer.entities.PurchaseItem;
import com.tanveer.entities.ListItem;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Deque;
import java.util.LinkedList;


public class PurchaseController {
    @FXML
    private ListView list;
    @FXML
    private TableView purchaseTable;
    private ObservableList<PurchaseItem> purchaseItems;
    private Deque<ObservableList<ListItem>> observableLists;
    private ObservableList<ListItem> listItems;
    private boolean isPurchasedList = false;


    public void initialize(){

        listItems = FXCollections.observableArrayList();

        purchaseItems = FXCollections.observableArrayList();
        observableLists  = new LinkedList<>();
        setListItems();

        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                clearData();


                Platform.runLater(() -> {
                    ListItem selectedItem = (ListItem) observable.getValue();
                    if(selectedItem != null) {
                        System.out.println(selectedItem);
                        if (selectedItem.toString().equalsIgnoreCase("listItems")) {
                            listItems.clear();
                            listItems.add(new ListItem(1, "grase"));
                            observableLists.addFirst(listItems);
                            list.setItems(listItems);
                            isPurchasedList = false;
                        }
                        else if (selectedItem.toString().equalsIgnoreCase("suppliers")) {
                            listItems.clear();
                            listItems.add(new ListItem(1, "gulzada"));
                            observableLists.addFirst(copy(listItems));
                            list.setItems(listItems);
                            isPurchasedList = false;
                        }
                        else if (selectedItem.toString().equalsIgnoreCase("purchases")) {
                            listItems.clear();
                            listItems.add(new ListItem(1, "Grase"));
                            observableLists.addFirst(copy(listItems));
                            list.setItems(listItems);
                            isPurchasedList = true;
                        }
                        else if (isPurchasedList) {
                            int selectedItemId = selectedItem.getId();
                            PurchaseItem purchaseItem = new PurchaseItem(1, 1, LocalDate.now(), 5.0, 100,
                                    5.0 * 100, 500.0 / 2.0, 500.0 / 2.0);
                            purchaseItems.add(purchaseItem);

                            if (selectedItem.getId() == purchaseItems.get(0).getItemId()) {
                                setPurchaseTable(purchaseItem);
                            }
                        }
                    }
                });
            }
        });
    }

    private void setListItems(){
        listItems.add(new ListItem(1,"listItems"));
        listItems.add(new ListItem(2,"suppliers"));
        listItems.add(new ListItem(3,"purchases"));
        list.setItems(listItems);
        observableLists.addFirst(copy(listItems));
    }

    private void setPurchaseTable(PurchaseItem purchaseItem){
        ObservableList<PurchaseItem> list;

        TableColumn<PurchaseItem, LocalDate> purchaseDateColumn = new TableColumn<>("Purchase Date");
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        TableColumn<PurchaseItem, DoubleProperty> pricePerMeterColumn = new TableColumn<>("Price Per Meter");
        pricePerMeterColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerMeter"));
        TableColumn<PurchaseItem, IntegerProperty> noOfMetersPurchasedColumn = new TableColumn<>("No Of Meters Purchased");
        noOfMetersPurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("noOfMetersPurchased"));
        TableColumn<PurchaseItem, DoubleProperty> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        TableColumn<PurchaseItem, DoubleProperty> pricePaidColumn = new TableColumn<>("Price Paid");
        pricePaidColumn.setCellValueFactory(new PropertyValueFactory<>("pricePaid"));
        TableColumn<PurchaseItem, DoubleProperty> priceRemColumn = new TableColumn<>("Price Remaining");
        priceRemColumn.setCellValueFactory(new PropertyValueFactory<>("priceRem"));

        purchaseTable.getColumns().addAll(purchaseDateColumn,pricePerMeterColumn,noOfMetersPurchasedColumn,totalPriceColumn,pricePaidColumn,priceRemColumn);
        purchaseTable.getItems().add(purchaseItem);


    }

    private ObservableList<ListItem> copy(ObservableList<ListItem> list){
        ObservableList<ListItem> copyOfListItems =FXCollections.observableArrayList();
        list.forEach(item -> copyOfListItems.add(item));
        return copyOfListItems;
    }

    private void clearData(){
        purchaseTable.getColumns().clear();
        purchaseTable.getItems().clear();

    }

    @FXML
    public void getBack(){
        if(observableLists.size() > 1) {
            observableLists.pop();
            list.setItems(observableLists.peek());
        }
    }



}
