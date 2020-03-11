package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import com.tanveer.model.stocks.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

public class StockRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Stock> stock;

    public StockRepository(){
        stock  = FXCollections.observableArrayList();
        setDatabaseStockData();

    }

    private void setDatabaseStockData(){
        stock.add(new Stock(new Item(7, "Grace", new ItemSupplier(1, "gulzada", "mingora"),200,200/7,7),5000,5000/7,3000,3000/7));
        stock.add(new Stock(new Item(8, "Brace", new ItemSupplier(1, "gulzada", "mingora"),231,231/7,7),3000,3000/7,1300,1300/7));
    }

    public ObservableList<Stock> getStock() {
        return stock;
    }
}
