package com.tanveer.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class BillingController {



    public void initialize(){

    }

    @FXML
    public void automaticBill() throws Exception{
        Stage stage = new Stage();
        stage.setTitle("Automatic Bill");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/automaticBill.fxml"));
        Scene scene = new Scene(root,250,150);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }

    @FXML
    public void customBill(){

    }


}
