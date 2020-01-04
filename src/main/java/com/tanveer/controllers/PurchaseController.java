package com.tanveer.controllers;

import com.tanveer.entities.list.Item;
import com.tanveer.entities.list.ItemSupplier;
import com.tanveer.entities.list.PurchaseItem;
import com.tanveer.entities.list.ListItem;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;


public class PurchaseController {
    @FXML
    private ListView list;
    @FXML
    private TableView purchaseTable;
    @FXML
    private ToolBar toolbar;
    @FXML
    private Label itemCode;
    @FXML
    private Label itemName;
    @FXML
    private Label itemSupplier;
    @FXML
    private Button backBtn;
    @FXML
    private Label itemTotalPaidLabel;
    @FXML
    private Label itemTotalRemLabel;
    @FXML
    private Label itemTotalPaidFigure;
    @FXML
    private Label itemTotalRemFigure;
    @FXML
    private Label grandTotalPaid;
    @FXML
    private Label grandTotalRem;
    @FXML
    private TitledPane statsSection;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Label supplierNameLabel;
    @FXML
    private Label supplierAddressLabel;
    @FXML
    private ListView supplierItemsList;
    @FXML
    private TitledPane supplierSection;
    private Deque<ObservableList<ListItem>> observableLists;
    private ObservableList<ListItem> listItems;
    private boolean isPurchasedList = false;
    private boolean isSupplierList = false;


    @SuppressWarnings("unchecked")
    public void initialize(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object listItem  = list.getSelectionModel().getSelectedItem();
                editListItem(listItem);
            }
        });
        setItems();
        contextMenu.getItems().add(editItem);

        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                clearData();


                Platform.runLater(() -> {
                    Object selectedItem = observable.getValue();
                    if(selectedItem != null) {
                        if(selectedItem instanceof ListItem ) {
                            backBtn.disableProperty().setValue(false);
                            if (selectedItem.toString().equalsIgnoreCase("suppliers")) {
                                listItems.clear();
                                ObservableList<ItemSupplier> supplersList = FXCollections.observableArrayList();
                                supplersList.add(new ItemSupplier(8, "gulzada", "mingora"));
                                observableLists.addFirst(copy(supplersList));
                                list.setItems(supplersList);
                                isPurchasedList = false;
                                isSupplierList = true;
                            }

                            else if (selectedItem.toString().equalsIgnoreCase("purchases")) {
                                listItems.clear();
                                ObservableList<Item> items = FXCollections.observableArrayList();
                                items.add(new Item(7, "Grase", new ItemSupplier(1, "gulzada", "mingora")));
                                observableLists.addFirst(copy(items));
                                list.setItems(items);
                                isPurchasedList = true;
                                isSupplierList = false;
                            }
                        }
                        else if (isPurchasedList) {
                            ObservableList<PurchaseItem> purchaseItems = FXCollections.observableArrayList();
                            Item item = (Item)observable.getValue();
                            PurchaseItem purchaseItem = new PurchaseItem(7, 1, LocalDate.now(), 5.0, 100,
                                    5.0 * 100, 500.0 / 2.0, 500.0 / 2.0);
                            purchaseItems.add(purchaseItem);
                            Button btn = purchaseItem.getEditButton();
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println(btn.getId());
                                }
                            });

                            if (item.getId() == purchaseItems.get(0).getItemId()) {
                                settingItemAttributes(item,purchaseItems);
                                makingComponentsVisible();
                                setPurchaseTable(purchaseItem);
                            }
                        }

                        else if(isSupplierList){
                            ItemSupplier supplier = (ItemSupplier)observable.getValue();
                            ObservableList<String> supplierItems = FXCollections.observableArrayList();
                            supplierNameLabel.setText(supplier.getName());
                            supplierAddressLabel.setText(supplier.getAddress());
                            supplierItems.addAll("grase","srass","saam,d","furana","khurana");
                            supplierItemsList.setItems(supplierItems);
                            supplierSection.setVisible(true);



                        }
                    }
                });
            }
        });
        list.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                ListCell listCell = new ListCell(){
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);

                        if(empty){
                            setText(null);
                        }
                        else{
                            setText(item.toString());
                        }

                    }
                };
                listCell.emptyProperty().addListener((obs,wasEmpty,isEmpty) ->{
                    if(isEmpty){
                        listCell.setContextMenu(null);
                    }
                    else{

                        listCell.setContextMenu(contextMenu);
                    }
                });
                return listCell;
            }
        });

    }

    private void setListItems(){
        listItems.add(new ListItem(2,"suppliers"));
        listItems.add(new ListItem(3,"purchases"));
        list.setItems(listItems);
        observableLists.addFirst(copy(listItems));
    }



    private void setPurchaseTable(PurchaseItem purchaseItem){

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
        TableColumn<PurchaseItem, Button> editButtonColumn = new TableColumn<>("Action");
        editButtonColumn.setCellValueFactory(new PropertyValueFactory<>("editButton"));




        purchaseTable.getColumns().addAll(purchaseDateColumn,pricePerMeterColumn,noOfMetersPurchasedColumn,totalPriceColumn,pricePaidColumn,priceRemColumn,editButtonColumn);
        purchaseTable.getItems().add(purchaseItem);






    }




    private ObservableList copy(ObservableList list){
        ObservableList copyOfListItems =FXCollections.observableArrayList();
        list.forEach(item -> copyOfListItems.add(item));
        return copyOfListItems;
    }

    private void clearData(){
        purchaseTable.getColumns().clear();
        purchaseTable.getItems().clear();
        makingComponentsInvisible();

    }

    private void setItems(){

        listItems = FXCollections.observableArrayList();
        observableLists  = new LinkedList<>();
        setListItems();
        backBtn.disableProperty().setValue(true);
        makingComponentsInvisible();
    }


    private void makingComponentsVisible(){
        toolbar.setVisible(true);
        purchaseTable.setVisible(true);
        statsSection.setVisible(true);


    }

    private void makingComponentsInvisible(){
        toolbar.setVisible(false);
        purchaseTable.setVisible(false);
        statsSection.setVisible(false);
        supplierSection.setVisible(false);


    }

    private void settingItemAttributes(Item tempItem,ObservableList<PurchaseItem> purchaseItems){
        String paidLabelText = "Total price paid for ";
        String remLabelText = "Total price ramaining for ";
        double itemTotalPricePaid = purchaseItems.stream().mapToDouble(PurchaseItem :: getPricePaid).sum();
        double itemTotalPriceRem = purchaseItems.stream().mapToDouble(PurchaseItem :: getPriceRem).sum();
        itemCode.setText(String.valueOf(tempItem.getId()));
        itemName.setText(tempItem.toString());
        itemSupplier.setText(tempItem.getSupplier().getName());
        itemTotalPaidLabel.setText(paidLabelText + tempItem.toString());
        itemTotalRemLabel.setText(remLabelText + tempItem.toString());
        itemTotalPaidFigure.setText(String .valueOf(itemTotalPricePaid));
        itemTotalRemFigure.setText(String.valueOf(itemTotalPriceRem));
    }

    private void editListItem(Object item){
        if(item instanceof ItemSupplier){
            System.out.println("casting item to item supp");
            ItemSupplier itemSupplier = ((ItemSupplier) item);
            editSupplierItem(itemSupplier);
        }
        else if(item instanceof Item){
            System.out.println("casting to item");
            Item item1 = ((Item) item);
            editItem(item1);
        }
    }

    private void editSupplierItem(ItemSupplier itemSupplier){
        System.out.println("editing supplier item");
        System.out.println(itemSupplier.getId());
    }

    private void editItem(Item item){
        System.out.println("edit item");
        System.out.println(item.getId());
    }

    @FXML
    public void getBack(){
        if(observableLists.size() > 1) {
            observableLists.pop();
            list.setItems(observableLists.peek());
            if(observableLists.size() == 1){
                backBtn.disableProperty().setValue(true);
            }
        }
    }

    @FXML
    public void addSupplier(){

    }

    @FXML
    public void addItem(){

    }

    @FXML
    public void purchaseItem(){

    }

    @FXML
    public void close(){
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();


    }




}
