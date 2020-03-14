package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.SupplierRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddItemController {
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

    public void initialize(){
        supplierRepository = new SupplierRepository();
        itemRepository = new ItemRepository();

        supplierCombobox.setItems(supplierRepository.getSuppliers());
    }

    public void addItem(){
        if(isInputValid()){
            int code = Integer.valueOf(itemCode.getText());
            String itemName =  name.getText();
            ItemSupplier supplier = supplierCombobox.getSelectionModel().getSelectedItem();
            double pricePSuit = Double.valueOf(pricePerSuit.getText());
            double pricePMeter = Double.valueOf(pricePerMeter.getText());
            double metersIsuit = Double.valueOf(metersPerSuit.getText());

            itemRepository.addItem(new Item(code,itemName,supplier,pricePSuit,pricePMeter,metersIsuit));
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add valid data");
            alert.setContentText("Please fill the fields with valid data");
            alert.show();
        }

    }

    private boolean isInputValid(){
        boolean isValid = false;

        if(!itemCode.getText().isEmpty() && !name.getText().isEmpty() &&
                supplierCombobox != null && !pricePerMeter.getText().isEmpty() &&
                !pricePerSuit.getText().isEmpty() && ! metersPerSuit.getText().isEmpty()){
            try{
                Integer.parseInt(itemCode.getText());
                Double.parseDouble(pricePerMeter.getText());
                Double.parseDouble(pricePerSuit.getText());
                Double.parseDouble(metersPerSuit.getText());
                return true;
            }
            catch(NumberFormatException s){
                return false;
            }

        }

        return isValid;
    }

}
