package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.SaleRepository;
import com.tanveer.model.sale.Sale;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateSaleController implements Initializable {
    @FXML
    private DatePicker date;
    @FXML
    private TextField metersSold;
    @FXML
    private TextField piecesSold;
    @FXML
    private TextField amount;
    private Sale sale;
    private SaleRepository saleRepository;

    public UpdateSaleController(Sale sale) {
        this.sale = sale;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saleRepository = SaleRepository.getInstance();
        initData();

    }

    public void updateSale(){
        if(isDataValid()){
            LocalDate dte = date.getValue();
            double noOfSuits = Double.valueOf(piecesSold.getText());
            double noOfMetersSold = Double.valueOf(metersSold.getText());
            double amt  = Double.valueOf(amount.getText());
            double profit = amt - (sale.getItem().getPricePerSuit() * noOfSuits);;

            saleRepository.updateSale(new Sale(sale.getItem(),dte,sale.getSaleTime(),noOfMetersSold,noOfSuits,amt,profit),sale);
        }

    }


    private boolean isDataValid(){
        boolean isValid;
        if(date.getValue() != null && !piecesSold.getText().isEmpty() && !amount.getText().isEmpty()){
            try{
                Double.parseDouble(piecesSold.getText());
                Double.parseDouble(amount.getText());
                isValid = true;

            }
            catch (NumberFormatException e){
                isValid  = false;
            }

        }
        else{
            isValid = false;
        }

        return isValid;


    }


    private void initData(){
        String mtrsSold = String.valueOf(sale.getNoOfMetersSold());
        String pcsSold = String.valueOf(sale.getNoOfPiecesSold());
        String amt = String.valueOf(sale.getSaleAmount());

        date.setValue(sale.getSaleDate());
        metersSold.setText(mtrsSold);
        piecesSold.setText(pcsSold);
        amount.setText(amt);


    }
}
