package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.SupplierRepository;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;


public class AddSupplierController {
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    private  SupplierRepository supplierRepository;


    public void initialize(){
        supplierRepository = SupplierRepository.getInstance();
    }

    public  void addSupplier(){
        if (!name.getText().isEmpty() && !address.getText().isEmpty() && !phone.getText().isEmpty()) {
            supplierRepository.addSupplier(new ItemSupplier(name.getText(),address.getText(),phone.getText()));
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fields Not Filled");
            alert.setContentText("Please fill all the fields");
            alert.show();
        }
    }
}
