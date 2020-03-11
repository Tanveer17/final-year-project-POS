package com.tanveer.model.database;

import com.tanveer.model.purchases.PurchaseItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class PurchaseRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<PurchaseItem> purchases;

    public PurchaseRepository(){
        purchases  = FXCollections.observableArrayList();
        setDatabaseExpensesData();

    }

    public ObservableList<PurchaseItem> getPurchases(){
        return purchases;
    }

    private void setDatabaseExpensesData(){
        purchases.add(new  PurchaseItem(7, 1, LocalDate.now(), 100.0, 100,100*7,100/7,
                100 * 100, 500.0 / 2.0, 500.0 / 2.0));
//        try(Statement statement = connection.createStatement()){
//            String query = "SELECT * FROM purchases";
//             ResultSet rs  = statement.executeQuery(query);
//             while(rs.next()){
//                 int id = rs.getInt(1);
//                 int itemId = rs.getInt(2);
//                 LocalDate date = rs.getDate(3).toLocalDate();
//                 double

//             }
//        }
    }
}
