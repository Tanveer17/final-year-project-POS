package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.SupplierRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UpdateItemController implements Initializable {
    @FXML
    private TextField itemCode;
    @FXML
    private TextField name;
    @FXML
    private ComboBox<ItemSupplier> supplierCombobox;
    @FXML
    private TextField pricePerSuit;
    @FXML
    private TextField pricePerMeter;
    @FXML
    private TextField metersPerSuit;
    private SupplierRepository supplierRepository;
    private ItemRepository itemRepository;
    private Item item;
    private boolean  isIdExists;

    public UpdateItemController(Item item) {
        this.item = item;
    }

    public void initialize(URL url, ResourceBundle rb){
        supplierRepository = SupplierRepository.getInstance();
        itemRepository = ItemRepository.getInstance();
        supplierCombobox.getItems().addAll(supplierRepository.getSuppliers());
        initData();
    }


    public void setItem(Item item) {
        this.item = item;
    }

    public boolean updateItem(){
        if(isInputValid()){
            if(isIdExists){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add valid Id");
                alert.setContentText("The Item Code you entered allready exists");
                alert.show();
                return false;
            }
            int code = Integer.valueOf(itemCode.getText());
            String itemName =  name.getText();
            ItemSupplier supplier = supplierCombobox.getSelectionModel().getSelectedItem();
            double pricePSuit = Double.valueOf(pricePerSuit.getText());
            double pricePMeter = Double.valueOf(pricePerMeter.getText());
            double metersIsuit = Double.valueOf(metersPerSuit.getText());

            itemRepository.updateItem(new Item(code,itemName,supplier,pricePSuit,pricePMeter,metersIsuit),item);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add valid data");
            alert.setContentText("Please fill the fields with valid data");
            alert.show();
            return false;
        }
        return true;

    }

    private boolean isInputValid(){
        boolean isValid = false;
        int item_id = -1;

        if(!itemCode.getText().isEmpty() && !name.getText().isEmpty() &&
                supplierCombobox != null && !pricePerMeter.getText().isEmpty() &&
                !pricePerSuit.getText().isEmpty() && ! metersPerSuit.getText().isEmpty()){
            try{
                item_id = Integer.parseInt(itemCode.getText());
                Double.parseDouble(pricePerMeter.getText());
                Double.parseDouble(pricePerSuit.getText());
                Double.parseDouble(metersPerSuit.getText());
                isValid = true;

            }
            catch(NumberFormatException s){
                isValid = false;
            }

        }

        if(item_id != -1){
            int id = item_id;
            List<Integer> idsList = itemRepository.getItems().stream().map(value -> value.getId()).filter(i -> i != id).collect(Collectors.toList());
            if(idsList.contains(item_id)){
                isIdExists = true;
            }
        }

        return isValid;
    }

    private void initData(){
        String itemCod = String.valueOf(item.getId());
        String itemName = item.getName();
        ItemSupplier supplier = item.getSupplier();
        String pricePSuit = String.valueOf(item.getPricePerSuit());
        String pricePMeter = String.valueOf(item.getPricePerMeter());
        String metersInASuit = String.valueOf(item.getMetersInASuit());

        int supplierIndex = supplierRepository.getSuppliers().indexOf(supplier);

        itemCode.setText(itemCod);
        name.setText(itemName);
        supplierCombobox.getSelectionModel().select(supplierIndex);
        pricePerSuit.setText(pricePSuit);
        pricePerMeter.setText(pricePMeter);
        metersPerSuit.setText(metersInASuit);
    }
}
