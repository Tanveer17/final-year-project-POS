package com.tanveer.controllers.dialogcontrollers;

import com.tanveer.model.database.ItemRepository;
import com.tanveer.model.database.PurchaseRepository;
import com.tanveer.model.database.StockRepository;
import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.PurchaseItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdatePurchaseItemController implements Initializable {
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
    @FXML
    private DatePicker purchaseDate;
    private PurchaseItem purchaseItem;
    private ItemRepository itemRepository;
    private PurchaseRepository purchaseRepository;
    private StockRepository stockRepository;

    public UpdatePurchaseItemController(PurchaseItem purchaseItem){
        this.purchaseItem = purchaseItem;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemRepository  = ItemRepository.getInstance();
        purchaseRepository = PurchaseRepository.getInstance();
        stockRepository = StockRepository.getInstance();
        initdata();
    }

    private void initdata(){

        String pricePMeter = String.valueOf(purchaseItem.getPricePerMeter());
        String metersPurchasd = String.valueOf(purchaseItem.getNoOfMetersPurchased());
        String pricePPiece = String.valueOf(purchaseItem.getPricePerPiece());
        String piecesPurchasd = String.valueOf(purchaseItem.getNoOfPiecesPurchased());
        String total = String.valueOf(purchaseItem.getTotalPrice());
        String paid = String.valueOf(purchaseItem.getPricePaid());
        LocalDate date = purchaseItem.getPurchaseDate();

        pricePerMeter.setText(pricePMeter);
        metersPurchased.setText(metersPurchasd);
        pricePerPiece.setText(pricePPiece);
        piecesPurchased.setText(piecesPurchasd);
        totalPrice.setText(total);
        pricePaid.setText(paid);
        purchaseDate.setValue(date);

    }

    public void updatePurchaseItem(){  if(isDataValid()){

        double pricePerMeter = Double.valueOf(this.pricePerMeter.getText());
        double metersPurchased = Double.valueOf(this.metersPurchased.getText());
        double pricePerPiece = Double.valueOf(this.pricePerPiece.getText());
        double piecesPurchased = Double.valueOf(this.piecesPurchased.getText());
        double totalPrice = Double.valueOf(this.totalPrice.getText());
        double pricePaid = Double.valueOf(this.pricePaid.getText());
        double priceRem  = totalPrice - pricePaid;
        LocalDate date = purchaseDate.getValue();
        if(stockRepository.removeStock(purchaseItem))
            purchaseRepository.updatePurchaseItem(new PurchaseItem(purchaseItem.getItemId(),purchaseItem.getPurchaseId(),date,pricePerMeter,metersPurchased,pricePerPiece,piecesPurchased,totalPrice,pricePaid,priceRem));


    }


    }

    private boolean isDataValid(){
        if(!pricePerMeter.getText().isEmpty() && !metersPurchased.getText().isEmpty() &&
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
}
