package com.tanveer.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button dashBoardBtn;
    @FXML
    private Button expansesBtn;
    @FXML
    private Button purchasesBtn;
    @FXML
    private Button saleBtn;


    public void initialize(){
        setCursors();

    }

    public void setCursors(){
        expansesBtn.setCursor(Cursor.HAND);
        dashBoardBtn.setCursor(Cursor.HAND);
        purchasesBtn.setCursor(Cursor.HAND);
        saleBtn.setCursor(Cursor.HAND);
    }

    @FXML
    public void showPurchaseStage() throws Exception{
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/purchase.fxml"));
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Purchases");
        stage.setScene(scene);
        stage.show();

    }

}
