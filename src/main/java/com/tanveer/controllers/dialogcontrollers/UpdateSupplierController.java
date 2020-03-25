package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.SupplierRepository;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateSupplierController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    private SupplierRepository supplierRepository;
    private ItemSupplier itemSupplier;

    public UpdateSupplierController(ItemSupplier itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        supplierRepository = SupplierRepository.getInstance();
        initData();
    }

    public void updateSupplier(){
        if (!name.getText().isEmpty() && !address.getText().isEmpty() && !phone.getText().isEmpty()) {
            supplierRepository.updateSupplier(new ItemSupplier(name.getText(),address.getText(),phone.getText()), itemSupplier);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fields Not Filled");
            alert.setContentText("Please fill all the fields");
            alert.show();
        }
    }

    private void initData(){
        String nme = itemSupplier.getName();
        String addr = itemSupplier.getAddress();
        String ph = itemSupplier.getPhoneNumber();

        name.setText(nme);
        address.setText(addr);
        phone.setText(ph);

    }
}
