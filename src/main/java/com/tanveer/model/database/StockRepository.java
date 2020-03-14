package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import com.tanveer.model.stocks.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Stock> stock;
    ItemRepository itemRepository;

    public StockRepository(){
        stock  = FXCollections.observableArrayList();
        itemRepository = new ItemRepository();
        setDatabaseStockData();

    }

    private void setDatabaseStockData(){
        try(Statement statement = connection.createStatement()){
            String sql = "SELECT * FROM stocks";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                int itemId = resultSet.getInt(1);
                Item item = itemRepository.getItems().stream().filter((item1 -> item1.getId() == itemId)).findAny().get();
                double totalMeterPurchased = resultSet.getDouble(2);
                double totalPiecesPurchased = resultSet.getDouble(3);
                double currentlyInStockMeters = resultSet.getDouble(4);
                double currentlyInStockPieces = resultSet.getDouble(4);

                stock.add(new Stock(item,totalMeterPurchased,totalPiecesPurchased,currentlyInStockMeters,currentlyInStockPieces));
            }
        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }

    public ObservableList<Stock> getStock() {
        return stock;
    }
}
