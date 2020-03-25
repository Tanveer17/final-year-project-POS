package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.PurchaseRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemPriceHistory;
import com.tanveer.model.purchases.PurchaseItem;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.swing.event.DocumentEvent;
import java.time.LocalDate;

public class AddPurchaseController {
    @FXML
    private ComboBox<Item> item;
    @FXML
    private TextField pricePerMeter;
    @FXML
    private TextField metersPurchased;
    @FXML
    private TextField pricePerPiece;
    @FXML
    private TextField piecesPurchased;
    @FXML
    private TextField totalPrice;
    @FXML
    private TextField pricePaid;
    private ItemRepository itemRepository;
    private PurchaseRepository purchaseRepository;

    public void initialize(){
        itemRepository = ItemRepository.getInstance();
        purchaseRepository = PurchaseRepository.getInstance();
        item.getItems().addAll(itemRepository.getItems());
        eventListeners();

    }

    public void addPurchaseItem(){
        if(isDataValid()){
            int itemId = item.getSelectionModel().getSelectedItem().getId();
            LocalDate date = LocalDate.now();
            double pricePerMeter = Double.valueOf(this.pricePerMeter.getText());
            double metersPurchased = Double.valueOf(this.metersPurchased.getText());
            double pricePerPiece = Double.valueOf(this.pricePerPiece.getText());
            double piecesPurchased = Double.valueOf(this.piecesPurchased.getText());
            double totalPrice = Double.valueOf(this.totalPrice.getText());
            double pricePaid = Double.valueOf(this.pricePaid.getText());
            double priceRem  = totalPrice - pricePaid;
            purchaseRepository.addPurchaseItem(new PurchaseItem(itemId,date,pricePerMeter,metersPurchased,pricePerPiece,piecesPurchased,totalPrice,pricePaid,priceRem));


        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add valid data");
            alert.setContentText("Please fill the fields with valid data");
            alert.show();
        }

    }

    private boolean isDataValid(){
        if(item.getSelectionModel().getSelectedItem() != null && !pricePerMeter.getText().isEmpty() && !metersPurchased.getText().isEmpty() &&
                !pricePerMeter.getText().isEmpty() && !pricePerPiece.getText().isEmpty() &&
                !piecesPurchased.getText().isEmpty() && ! totalPrice.getText().isEmpty() && !pricePaid.getText().isEmpty()){
            try{
                Double.parseDouble(pricePerMeter.getText());
                Double.parseDouble(metersPurchased.getText());
                Double.parseDouble(piecesPurchased.getText());
                Double.parseDouble(pricePerPiece.getText());
                Double.parseDouble(totalPrice.getText());
                Double.parseDouble(pricePaid.getText());
                return true;
            }
            catch (NumberFormatException e){
                return false;
            }
        }

        return false;
    }

    private void eventListeners(){
      item.setOnAction(e ->{

      });
    }
}
