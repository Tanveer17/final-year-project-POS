package com.tanveer.controllers;

import com.tanveer.controllers.dialogcontrollers.*;
import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.PurchaseRepository;
import com.tanveer.model.database.StockRepository;
import com.tanveer.model.database.SupplierRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import com.tanveer.model.purchases.PurchaseItem;
import com.tanveer.model.purchases.ListItem;
import com.tanveer.model.stocks.Stock;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;


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
    private TitledPane supplierSection;
    @FXML
    private BorderPane parent;
    @FXML
    private TitledPane stockSection;
    @FXML
    private SplitPane stats;
    @FXML
    private Label totalMeterPurchasesLabel;
    @FXML
    private Label totalPiecesPurchasesLabel;
    @FXML
    private Label currentlyInStockMetersLabel;
    @FXML
    private Label currentlyInStockPiecesLabel;
    @FXML
    private Label supplierPhone;
    private ObservableList<PurchaseItem> purchaseItems;
    private ObservableList<ItemSupplier> supplersList;
    private Deque<ObservableList<ListItem>> observableLists;
    private ObservableList<ListItem> listItems;
    private boolean isPurchasedList = false;
    private boolean isSupplierList = false;
    private ContextMenu contextMenu;
    private ContextMenu tableContextMenu;
    private PurchaseRepository purchaseRepository;
    private ItemRepository itemRepository;
    private StockRepository stockRepository;
    private SupplierRepository supplierRepository;
    private ObservableList<Item> items;
    private ObservableList<Stock> stocks;


    @SuppressWarnings("Unchecked")
    public void initialize(){

        //initializingdata models
        itemRepository = ItemRepository.getInstance();
        purchaseRepository = PurchaseRepository.getInstance();
        supplierRepository = SupplierRepository.getInstance();
        stockRepository = StockRepository.getInstance();

        //initializing collections
        items = itemRepository.getItems();
        purchaseItems = purchaseRepository.getPurchases();
        supplersList = supplierRepository.getSuppliers();
        stocks = stockRepository.getStock();

        System.out.println(supplersList == supplierRepository.getSuppliers());

        eventListeners();
        setContextMenu();

    }

    //set list items and managing going back and forth
    private void setListItems(){
        listItems.add(new ListItem(2,"suppliers"));
        listItems.add(new ListItem(3,"purchases"));
        list.setItems(listItems);
        observableLists.addFirst(copy(listItems));
    }



    private void setPurchaseTable(ObservableList<PurchaseItem> purchaseItems){
        setRowFactory();

        TableColumn<PurchaseItem, LocalDate> purchaseDateColumn = new TableColumn<>("Purchase Date");
        purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        TableColumn<PurchaseItem, DoubleProperty> pricePerMeterColumn = new TableColumn<>("Price Per Meter");
        pricePerMeterColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerMeter"));
        TableColumn<PurchaseItem, IntegerProperty> noOfMetersPurchasedColumn = new TableColumn<>("Meters Purchased");
        noOfMetersPurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("noOfMetersPurchased"));
        TableColumn<PurchaseItem, DoubleProperty> pricePerPieceColumn = new TableColumn<>("Price Per Piece");
        pricePerPieceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerPiece"));
        TableColumn<PurchaseItem, IntegerProperty> noOfPiecesPurchasedColumn = new TableColumn<>("Pieces Purchased");
        noOfPiecesPurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("noOfPiecesPurchased"));
        TableColumn<PurchaseItem, DoubleProperty> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        TableColumn<PurchaseItem, DoubleProperty> pricePaidColumn = new TableColumn<>("Price Paid");
        pricePaidColumn.setCellValueFactory(new PropertyValueFactory<>("pricePaid"));
        TableColumn<PurchaseItem, DoubleProperty> priceRemColumn = new TableColumn<>("Price Remaining");
        priceRemColumn.setCellValueFactory(new PropertyValueFactory<>("priceRem"));
        System.out.println("in set purchasetable");
        purchaseItems.sort((p1,p2) -> p2.getPurchaseDate().compareTo(p1.getPurchaseDate()));




        purchaseTable.getColumns().addAll(purchaseDateColumn,pricePerMeterColumn,noOfMetersPurchasedColumn,pricePerPieceColumn,
                noOfPiecesPurchasedColumn,totalPriceColumn,pricePaidColumn,priceRemColumn);
        purchaseTable.getItems().addAll(purchaseItems);


    }


    private void settingItemAttributes(Item tempItem,ObservableList<PurchaseItem> filteredPurchaseItems){

        //setting stats
        String paidLabelText = "Total price paid for ";
        String remLabelText = "Total price ramaining for ";

        double itemTotalPricePaid = filteredPurchaseItems.stream().mapToDouble(PurchaseItem :: getPricePaid).sum();
        double itemTotalPriceRem = filteredPurchaseItems.stream().mapToDouble(PurchaseItem :: getPriceRem).sum();
        double grandTotalP = purchaseItems.stream().mapToDouble(PurchaseItem :: getPricePaid).sum();
        double grandTotalR = purchaseItems.stream().mapToDouble(PurchaseItem :: getPriceRem).sum();

        itemCode.setText(String.valueOf(tempItem.getId()));
        itemName.setText(tempItem.toString());
        itemSupplier.setText(tempItem.getSupplier().getName());

        itemTotalPaidLabel.setText(paidLabelText + tempItem.toString());
        itemTotalRemLabel.setText(remLabelText + tempItem.toString());
        itemTotalPaidFigure.setText(String .valueOf(itemTotalPricePaid));
        itemTotalRemFigure.setText(String.valueOf(itemTotalPriceRem));

        grandTotalPaid.setText(String.valueOf(grandTotalP));
        grandTotalRem.setText(String.valueOf(grandTotalR));

        //setting stocks
        Optional<Stock> stock = stocks.stream().filter(s -> s.getItem().equals(tempItem)).findAny();
        if(stock.isPresent()) {
            System.out.println("present");
            double totalMeterPurchases = stock.get().getTotalMeterPurchases();
            double totalPiecesPurchases = stock.get().getTotalPiecesPurchases();
            double currentlyInStockMeters = stock.get().getCurrentlyInStockMeters();
            double currentlyInStockPieces = stock.get().getCurrentlyInStockPieces();
            totalMeterPurchasesLabel.setText(String.valueOf(totalMeterPurchases));
            totalPiecesPurchasesLabel.setText(String.valueOf(totalPiecesPurchases));
            currentlyInStockMetersLabel.setText(String.valueOf(currentlyInStockMeters));
            currentlyInStockPiecesLabel.setText(String.valueOf(currentlyInStockPieces));
        }




    }

    private void settingStockLabels(){

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
        stats.setVisible(true);


    }

    private void makingComponentsInvisible(){
        toolbar.setVisible(false);
        purchaseTable.setVisible(false);
        supplierSection.setVisible(false);
        stats.setVisible(false);



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

    private void deleteListItem(Object item){
        System.out.println("purchase");
        if(item instanceof ItemSupplier){
            System.out.println("casting item to item supp");
            ItemSupplier itemSupplier = ((ItemSupplier) item);
            deleteSupplierItem(itemSupplier);
        }
        else if(item instanceof Item){
            System.out.println("casting to item");
            Item item1 = ((Item) item);
            deleteItem(item1);
        }
    }


    private void editItem(Item item){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Update Item");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/updateItemDialog.fxml"));
        UpdateItemController controller = new UpdateItemController(item);
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
            controller.updateItem();

        }
    }
    private void editSupplierItem(ItemSupplier itemSupplier){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Update Item");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/updateSupplierDialog.fxml"));
        UpdateSupplierController controller = new UpdateSupplierController(itemSupplier);
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
            controller.updateSupplier();

        }


    }




    private void deleteSupplierItem(ItemSupplier itemSupplier){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Item will be deleted");
        alert.setContentText("Item and all of its related data will be lost");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            supplierRepository.deleteItem(itemSupplier);
        }

    }

    private void deleteItem(Item item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Item will be deleted");
        alert.setContentText("Item and all of its related data will be lost");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
           itemRepository.deleteItem(item);
        }

    }


    @FXML
    public void addSupplier(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Add Supplier");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/addSupplierDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException i) {
            i.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        AddSupplierController controller = fxmlLoader.getController();


        if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.addSupplier();
        }

    }

    @FXML
    public void addItem(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Add new Item");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/addItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException i) {
            i.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        AddItemController controller = fxmlLoader.getController();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.addItem();

        }
    }

    @FXML
    public void purchaseItem(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Add Supplier");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/addPurchaseDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException i) {
            i.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        AddPurchaseController controller = fxmlLoader.getController();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.addPurchaseItem();

        }

    }

    private void editPurchaseItem(PurchaseItem purchaseItem){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Update Purchase");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/dialogs/updatePurchaseItemDialog.fxml"));
        UpdatePurchaseItemController controller = new UpdatePurchaseItemController(purchaseItem);
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
            controller.updatePurchaseItem();

        }
    }

    private void deletePurchaseItem(PurchaseItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Purchase will be deleted");
        alert.setContentText("Purchase will be removed fron history and you cant access it again");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            purchaseRepository.deletePurchaseItem(item);
        }
    }


    @FXML
    public void close(){
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();


    }

    private void setRowFactory(){

        purchaseTable.setRowFactory(new Callback<TableView, TableRow>() {
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
                        tableRow.setContextMenu(tableContextMenu);
                    }
                });
                return tableRow;
            }
        });
    }

    private void eventListeners(){
        stocks.addListener(new ListChangeListener<Stock>() {
            @Override
            public void onChanged(Change<? extends Stock> c) {
                if (isPurchasedList) {
                    System.out.println("changed");
                    purchaseTable.getColumns().clear();
                    purchaseTable.getItems().clear();
                    Item item = (Item)list.getSelectionModel().getSelectedItem();
                    System.out.println(purchaseItems);
                    ObservableList<PurchaseItem> filtered = FXCollections.observableArrayList(purchaseItems.stream()
                            .filter(i -> i.getItemId() == item.getId()).collect(Collectors.toList()));

                    settingItemAttributes(item, filtered);
                    setPurchaseTable(filtered);



                }
            }
        });
        purchaseRepository.getPurchases().addListener(new ListChangeListener<PurchaseItem>() {
            @Override
            public void onChanged(Change<? extends PurchaseItem> c) {
                if (isPurchasedList) {
                    clearData();
                    Item item = (Item)list.getSelectionModel().getSelectedItem();
                    ObservableList<PurchaseItem> filtered = FXCollections.observableArrayList(purchaseItems.stream()
                            .filter(i -> i.getItemId() == item.getId()).collect(Collectors.toList()));

                    setPurchaseTable(filtered);
                    settingItemAttributes(item, filtered);
                    makingComponentsVisible();
                }
            }
        });


        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                clearData();


                Platform.runLater(() -> {
                    Object selectedItem = observable.getValue();
                    if(selectedItem != null) {
                        clearData();
                        if(selectedItem instanceof ListItem ) {
                            backBtn.disableProperty().setValue(false);
                            if (selectedItem.toString().equalsIgnoreCase("suppliers")) {
                                listItems.clear();

                                observableLists.addFirst(copy(supplersList));
                                list.setItems(supplersList);
                                isPurchasedList = false;
                                isSupplierList = true;
                            }

                            else if (selectedItem.toString().equalsIgnoreCase("purchases")) {
                                listItems.clear();
                                observableLists.addFirst(copy(items));
                                list.setItems(items);
                                isPurchasedList = true;
                                isSupplierList = false;
                            }
                        }
                        else if (isPurchasedList) {
                            Item item = (Item)observable.getValue();
                            ObservableList<PurchaseItem> filtered = FXCollections.observableArrayList(purchaseItems.stream()
                                    .filter(i -> i.getItemId() == item.getId()).collect(Collectors.toList()));

                            setPurchaseTable(filtered);
                            settingItemAttributes(item, filtered);
                            makingComponentsVisible();
                        }

                        else if(isSupplierList){
                            ItemSupplier supplier = (ItemSupplier)observable.getValue();
                            supplierNameLabel.setText(supplier.getName());
                            supplierAddressLabel.setText(supplier.getAddress());
                            supplierPhone.setText(supplier.getPhoneNumber());
                            supplierSection.setVisible(true);



                        }
                    }
                });
            }
        });
    }

    private void setContextMenu(){
        contextMenu = new ContextMenu();
        tableContextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object listItem  = list.getSelectionModel().getSelectedItem();
                editListItem(listItem);
            }
        });

        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object listItem  = list.getSelectionModel().getSelectedItem();
                deleteListItem(listItem);
            }
        });
        setItems();
        contextMenu.getItems().addAll(editItem,deleteItem);

        MenuItem tableEditMenuItem = new MenuItem("Edit");
        tableEditMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PurchaseItem item = (PurchaseItem) purchaseTable.getSelectionModel().getSelectedItem();
                editPurchaseItem(item);
            }
        });

        MenuItem tableDeleteMenuItem = new MenuItem("Delete");
        tableDeleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PurchaseItem item = (PurchaseItem) purchaseTable.getSelectionModel().getSelectedItem();
                deletePurchaseItem(item);

            }
        });

        tableContextMenu.getItems().addAll(tableEditMenuItem,tableDeleteMenuItem);


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




}
