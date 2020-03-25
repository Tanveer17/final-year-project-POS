package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.stocks.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemStockRepository {
    private Connection connection = Database.getConnection();
    private ItemRepository itemRepository = ItemRepository.getInstance();
    private StockRepository stockRepository = StockRepository.getInstance();
    private static ItemStockRepository itemStockRepository = new ItemStockRepository();


    private ItemStockRepository() {
    }

    public static ItemStockRepository getInstance(){
        return itemStockRepository;
    }

    public void addStockListing(Item item){
        String sql = "INSERT INTO stocks VALUES(?,?,?,?,?)";


        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,item.getId());
            preparedStatement.setDouble(2,0);
            preparedStatement.setDouble(3,0);
            preparedStatement.setDouble(4,0);
            preparedStatement.setDouble(5,0);

            preparedStatement.execute();
            Stock stock = new Stock(item,0.0,0.0,0.0,0.0);
            System.out.println(stockRepository);
            System.out.println("stock : "+stock);
            stockRepository.getStock().add(stock);
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    public void removeItemFromStocks(Item item){
        System.out.println("Stock Repo items : " + stockRepository.getStock());
        System.out.println(item);
        stockRepository.getStock().removeIf(stock -> stock.getItem().equals(item));
    }

    public void updateItem(Item oldItem,Item item){
        Stock stock = stockRepository.getStock().stream().filter(stock1 -> stock1.getItem().equals(oldItem)).findAny().get();
        stock.setItem(item);
    }
}
