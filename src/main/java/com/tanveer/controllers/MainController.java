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
    @FXML
    private Button inventoryBtn;


    public void initialize(){
        setCursors();

    }

    private void setCursors(){
        expansesBtn.setCursor(Cursor.HAND);
        dashBoardBtn.setCursor(Cursor.HAND);
        purchasesBtn.setCursor(Cursor.HAND);
        saleBtn.setCursor(Cursor.HAND);
        inventoryBtn.setCursor(Cursor.HAND);
    }

    @FXML
    public void showPurchaseStage() throws Exception{
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/purchase.fxml"));
        Scene scene = new Scene(root,1200,600);
        stage.setTitle("Purchases");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void showExpansesStage() throws Exception{
        Stage stage  = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/expanses.fxml"));
        Scene scene = new Scene(root,800,655);
        stage.setTitle("Expanses");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void showSaleStage() throws Exception{
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("/fxml/sales.fxml"));
        Scene scene = new Scene(root,800,655);
        stage.setTitle("SALES");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void showInventoryStage() throws Exception{
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("/fxml/inventory.fxml"));
        Scene scene = new Scene(root,800,655);
        stage.setTitle("SALES");
        stage.setScene(scene);
        stage.show();

    }

}
