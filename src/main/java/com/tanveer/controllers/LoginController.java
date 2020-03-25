package com.tanveer.controllers;

import com.tanveer.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private Label errorMessage;
    @FXML
    private Button forgotPasswordBtn;


    public void initialize(){

        forgotPasswordBtn.setCursor(Cursor.HAND);
    }



    public void login() throws Exception {
        if(true) {
            Stage primaryStage = Main.getPrimaryStage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
            primaryStage.setScene(new Scene(root, 1200, 600));
            primaryStage.setTitle("Attire Cloths");
            primaryStage.show();
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            double x = bounds.getMinX() + (bounds.getWidth() - primaryStage.getWidth())/2.0;
            double y = bounds.getMinY() + (bounds.getHeight() - primaryStage.getHeight())/2.0;
            primaryStage.setX(x);
            primaryStage.setY(y);

            init();
        }
        else{
            errorMessage.setVisible(true);

        }
    }
    private void init() throws ClassNotFoundException{
//        Class.forName("com.tanveer.model.database.Database");
//        Class.forName("com.tanveer.model.database.SupplierRepository");
//        Class.forName("com.tanveer.model.database.ExpenseRepository");
//        Class.forName("com.tanveer.model.database.ItemRepository");
//        Class.forName("com.tanveer.model.database.StockRepository");
//        Class.forName("com.tanveer.model.database.SaleRepository");
//        Class.forName("com.tanveer.model.database.PurchaseRepository");
    }

}
