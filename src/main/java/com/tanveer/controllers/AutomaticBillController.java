package com.tanveer.controllers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.PurchaseRepository;
import com.tanveer.model.database.SaleRepository;
import com.tanveer.model.database.StockRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.PurchaseItem;
import com.tanveer.model.sale.Sale;
import com.tanveer.model.stocks.Stock;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AutomaticBillController {
    @FXML
    private TextField itemCode;
    private ItemRepository itemRepository;
    private SaleRepository saleRepository;
    private StockRepository stockRepository;


    public void initialize(){
        itemRepository = itemRepository.getInstance();
        saleRepository = SaleRepository.getInstance();
        stockRepository = StockRepository.getInstance();
    }


    @FXML
    public void sellItem(){
        int id = -1;
        try{
            id = Integer.parseInt(itemCode.getText());
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Code");
            alert.setContentText("Code must be a Number");
            alert.show();
            return;
        }

        Item item = new Item(id);
        int index = itemRepository.getItems().indexOf(item);
        if(index != -1) {
            Item item1 = itemRepository.getItems().get(index);

            if (isStockEnough(item1)) {

                LocalDate date = LocalDate.now();
                double noOfSuits = 1;
                double noOfMetersSold = item1.getMetersInASuit();
                double amt = item1.getPricePerSuit();
                List<PurchaseItem> purchases = PurchaseRepository.getInstance().getPurchases().stream().filter(p -> p.getItemId() == item1.getId()).collect(Collectors.toList());
                PurchaseItem purchaseItem1 = purchases.get(purchases.size() - 1);
                double profit = amt - (purchaseItem1.getPricePerPiece() * noOfSuits);

                saleRepository.addSale(new Sale(item1, date, null,  noOfMetersSold,noOfSuits, amt, profit));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Stock is Not enough");
                alert.setContentText("There is no Items in Stock");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Item Not found");
            alert.setContentText("There is no Items with the code");
            alert.show();
        }

    }

    private boolean isStockEnough(Item item){
        double noOfPiecesInStock = stockRepository.getStock().stream().filter(s ->s.getItem().equals(item)).findAny().get().getCurrentlyInStockPieces();

        if(noOfPiecesInStock - 1 < 0){
            return false;
        }
        return true;
    }
}
