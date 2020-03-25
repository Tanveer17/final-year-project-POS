package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import com.tanveer.model.purchases.PurchaseItem;
import com.tanveer.model.stocks.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StockRepository {
    private Connection connection = Database.getConnection();
    private static ItemRepository itemRepository = ItemRepository.getInstance();
    private static StockRepository stockRepository = new StockRepository();
    private ObservableList<Stock> stock;

    private StockRepository() {
        stock = FXCollections.observableArrayList();
        setDatabaseStockData();

    }

//    static {
//        itemRepository = ItemRepository.getInstance();
//        stockRepository = new StockRepository();
//
//    }

    public static StockRepository getInstance() {
        return stockRepository;
    }

    public void setDatabaseStockData() {
        try (Statement statement = connection.createStatement()) {
            stock.clear();
            String sql = "SELECT * FROM stocks";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int itemId = resultSet.getInt(1);
                System.out.println(itemRepository);
                Item item = itemRepository.getItems().stream().filter((item1 -> item1.getId() == itemId)).findAny().get();
                double totalMeterPurchased = resultSet.getDouble(2);
                double totalPiecesPurchased = resultSet.getDouble(3);
                double currentlyInStockMeters = resultSet.getDouble(4);
                double currentlyInStockPieces = resultSet.getDouble(5);

                stock.add(new Stock(item, totalMeterPurchased, totalPiecesPurchased, currentlyInStockMeters, currentlyInStockPieces));
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public ObservableList<Stock> getStock() {
        return stock;
    }

    //updating stock after purchasing items
    public void updateStock(PurchaseItem purchaseItem) {
        Stock ItemStock = getDataBaseStock(purchaseItem.getItemId());

        double updatedTotalMeterPurchased = ItemStock.getTotalMeterPurchases() + purchaseItem.getNoOfMetersPurchased();
        double updatedTotalPiecesPurchased = ItemStock.getTotalPiecesPurchases() + purchaseItem.getNoOfPiecesPurchased();
        double updatedCurrentlyInStockMeters = ItemStock.getCurrentlyInStockMeters() + purchaseItem.getNoOfMetersPurchased();
        double updatedCurrentlyInStockPieces = ItemStock.getCurrentlyInStockPieces() + purchaseItem.getNoOfPiecesPurchased();

        String sql = "UPDATE stocks SET  total_meter_purchases = ? , total_pieces_purchases = ? ,currently_in_stock_meters = ? ,currently_in_stock_pieces = ?" +
                " WHERE item_id = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, updatedTotalMeterPurchased);
            preparedStatement.setDouble(2, updatedTotalPiecesPurchased);
            preparedStatement.setDouble(3, updatedCurrentlyInStockMeters);
            preparedStatement.setDouble(4, updatedCurrentlyInStockPieces);
            preparedStatement.setInt(5, purchaseItem.getItemId());

            preparedStatement.execute();
            Item item = itemRepository.getItems().stream().filter((item1 -> item1.getId() == purchaseItem.getItemId())).findAny().get();

            Stock newStock = new Stock(item,updatedTotalMeterPurchased,updatedTotalPiecesPurchased,
                    updatedCurrentlyInStockMeters,updatedCurrentlyInStockPieces);
            int index = stock.indexOf(newStock);
            stock.add(index,newStock);

        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public boolean removeStock(PurchaseItem purchaseItem){
        Stock ItemStock = getDataBaseStock(purchaseItem.getItemId());

        double updatedTotalMeterPurchased = ItemStock.getTotalMeterPurchases() - purchaseItem.getNoOfMetersPurchased();
        double updatedTotalPiecesPurchased = ItemStock.getTotalPiecesPurchases() - purchaseItem.getNoOfPiecesPurchased();
        double updatedCurrentlyInStockMeters = ItemStock.getCurrentlyInStockMeters() - purchaseItem.getNoOfMetersPurchased();
        double updatedCurrentlyInStockPieces = ItemStock.getCurrentlyInStockPieces() - purchaseItem.getNoOfPiecesPurchased();

        String sql = "UPDATE stocks SET  total_meter_purchases = ? , total_pieces_purchases = ? ,currently_in_stock_meters = ? ,currently_in_stock_pieces = ?" +
                " WHERE item_id = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, updatedTotalMeterPurchased);
            preparedStatement.setDouble(2, updatedTotalPiecesPurchased);
            preparedStatement.setDouble(3, updatedCurrentlyInStockMeters);
            preparedStatement.setDouble(4, updatedCurrentlyInStockPieces);
            preparedStatement.setInt(5, purchaseItem.getItemId());

            preparedStatement.execute();
            Item item = itemRepository.getItems().stream().filter((item1 -> item1.getId() == purchaseItem.getItemId())).findAny().get();

            Stock newStock = new Stock(item,updatedTotalMeterPurchased,updatedTotalPiecesPurchased,
                    updatedCurrentlyInStockMeters,updatedCurrentlyInStockPieces);
            int index = stock.indexOf(newStock);
            stock.add(index,newStock);

        } catch (SQLException s) {
            s.printStackTrace();
            return false;
        }
        return true;
    }

    //get the stock in the database which is updated so to update that
    public Stock getDataBaseStock(int id) {
        String sql = "SELECT * FROM stocks WHERE item_id = ?";
        Stock stock = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                double totalMeterPurchased = rs.getDouble(2);
                double totalPiecesPurchased = rs.getDouble(3);
                double currentlyInStockMeters = rs.getDouble(4);
                double currentlyInStockPieces = rs.getDouble(5);
                stock = new Stock(totalMeterPurchased, totalPiecesPurchased, currentlyInStockMeters, currentlyInStockPieces);
            }

        } catch (SQLException s) {
            s.printStackTrace();
        }
        return stock;
    }
}
