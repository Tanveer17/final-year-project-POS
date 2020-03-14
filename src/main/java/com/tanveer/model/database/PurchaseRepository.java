package com.tanveer.model.database;

import com.tanveer.model.purchases.PurchaseItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class PurchaseRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<PurchaseItem> purchases;

    public PurchaseRepository(){
        purchases  = FXCollections.observableArrayList();
        setDatabasePurchasesData();

    }

    public ObservableList<PurchaseItem> getPurchases(){
        return purchases;
    }

    private void setDatabasePurchasesData(){

        try(Statement statement = connection.createStatement()){
            String query = "SELECT * FROM purchases";
             ResultSet rs  = statement.executeQuery(query);
             while(rs.next()){
                 int id = rs.getInt(1);
                 int itemId = rs.getInt(2);
                 LocalDate date = rs.getDate(3).toLocalDate();
                 double pricePerMeter = rs.getDouble(4);
                 double noOfMetersPurchased = rs.getDouble(5);
                 double pricePerPiece = rs.getDouble(6);
                 double noOfPiecesPurchased = rs.getDouble(7);
                 double totalPrice = rs.getDouble(8);
                 double pricePaid = rs.getDouble(9);
                 double priceRem = rs.getDouble(10);

                 purchases.add(new PurchaseItem(itemId,id,date,pricePerMeter,noOfMetersPurchased,pricePerPiece,noOfPiecesPurchased,
                                                 totalPrice,pricePaid,priceRem));

             }
        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }
}
