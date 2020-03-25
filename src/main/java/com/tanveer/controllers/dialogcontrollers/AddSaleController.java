package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.PurchaseRepository;
import com.tanveer.model.database.SaleRepository;
import com.tanveer.model.database.StockRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.PurchaseItem;
import com.tanveer.model.sale.Sale;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

public class AddSaleController {
    @FXML
    private ComboBox item;
    @FXML
    private DatePicker date;
    @FXML
    private TextField suitsSold;
    @FXML
    private TextField metersSold;
    @FXML
    private TextField amount;
    @FXML
    private DialogPane parent;
    private boolean isStockHasEnough;
    private SaleRepository saleRepository;
    private ItemRepository itemRepository;
    private StockRepository stockRepository;


    public void initialize(){
        saleRepository = SaleRepository.getInstance();
        itemRepository = ItemRepository.getInstance();
        stockRepository = StockRepository.getInstance();
        item.setItems(itemRepository.getItems());

    }

    public void addSuitSale(){
        if(isDataValid() && isStockHasEnough){
            Item item1 = (Item)item.getValue();
            LocalDate dte = date.getValue();
            double noOfSuits = Double.valueOf(suitsSold.getText());
            double noOfMetersSold = item1.getMetersInASuit() * noOfSuits;
            double amt = Double.valueOf(amount.getText());
            List<PurchaseItem> purchases = PurchaseRepository.getInstance().getPurchases().stream().filter(p ->p.getItemId()==item1.getId()).collect(Collectors.toList());
            PurchaseItem purchaseItem1 = purchases.get(purchases.size()-1);
            double profit = amt - (purchaseItem1.getPricePerPiece() * noOfSuits);

            saleRepository.addSale(new Sale(item1,dte,null,noOfMetersSold,noOfSuits,amt,profit));

        }
        else if(!isStockHasEnough){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock is Not enough");
            alert.setContentText("The Stock is less Than specified Stock");
            alert.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add valid data");
            alert.setContentText("Please fill the fields with valid data");

            alert.show();
        }
    }

    public void addMetersSale(){
        if(isDataValid2() && isStockHasEnough){
            Item item1 = (Item)item.getValue();
            LocalDate dte = date.getValue();
            double noOfMetersSold = Double.valueOf(metersSold.getText());
            double noOfSuits =  noOfMetersSold/item1.getMetersInASuit();
            double amt = Double.valueOf(amount.getText());
            List<PurchaseItem> purchases = PurchaseRepository.getInstance().getPurchases().stream().filter(p ->p.getItemId()==item1.getId()).collect(Collectors.toList());
            PurchaseItem purchaseItem1 = purchases.get(purchases.size()-1);
            double profit = amt - (purchaseItem1.getPricePerMeter()* noOfMetersSold);

            saleRepository.addSale(new Sale(item1,dte,null,noOfMetersSold,noOfSuits,amt,profit));
        }
        else if(!isStockHasEnough){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock is Not enough");
            alert.setContentText("The Stock is less Than specified Stock");
            alert.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add valid data");
            alert.setContentText("Please fill the fields with valid data");
            alert.show();
        }

    }

    private boolean isDataValid(){
        boolean isValid;
        if(item.getValue() !=  null && date.getValue() != null && !suitsSold.getText().isEmpty() && !amount.getText().isEmpty()){
            try{
                Double.parseDouble(suitsSold.getText());
                Double.parseDouble(amount.getText());

                double noOfPiecesInStock = stockRepository.getStock().stream().filter(s ->s.getItem().equals(item.getValue())).findAny().get().getCurrentlyInStockPieces();

                if(noOfPiecesInStock - Double.valueOf(suitsSold.getText()) < 0){
                    isStockHasEnough = false;
                }
                else{
                    isStockHasEnough = true;
                }

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

    private boolean isDataValid2(){
        boolean isValid;
        if(item.getValue() !=  null && date.getValue() != null && !metersSold.getText().isEmpty() && !amount.getText().isEmpty()){
            try{
                Double.parseDouble(metersSold.getText());
                Double.parseDouble(amount.getText());
                double noOfMetersInStock = stockRepository.getStock().stream().filter(s ->s.getItem().equals(item.getValue())).findAny().get().getCurrentlyInStockMeters();

                if(noOfMetersInStock - Double.valueOf(metersSold.getText()) < 0){
                    isStockHasEnough = false;
                }
                else{
                    isStockHasEnough = true;
                }

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


}
