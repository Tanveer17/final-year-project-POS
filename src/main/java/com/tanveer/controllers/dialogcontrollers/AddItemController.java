package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.SupplierRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

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
    private boolean isIdExists;

    public void initialize(){
        supplierRepository = SupplierRepository.getInstance();
        itemRepository = ItemRepository.getInstance();

        supplierCombobox.setItems(supplierRepository.getSuppliers());
    }

    public void addItem(){


        if(isInputValid()){
            if(isIdExists){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add valid Id");
                alert.setContentText("The Item Code you entered allready exists");
                alert.show();
                return;
            }
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
            List<Integer> idsList = itemRepository.getItems().stream().map(value -> value.getId()).collect(Collectors.toList());
            if(idsList.contains(item_id)){
                isIdExists = true;
            }
        }

        return isValid;
    }

}
