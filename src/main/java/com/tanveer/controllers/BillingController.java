package com.tanveer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class BillingController {
    @FXML
    private TextField itemCodeText;


    public void initialize(){

    }

    @FXML
    public void generateBill(){
        String defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
        System.out.println(defaultPrinter);

        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
    }
}
