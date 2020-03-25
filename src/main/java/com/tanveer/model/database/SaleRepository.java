package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.sale.Sale;
import com.tanveer.model.stocks.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class SaleRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Sale> sales;
    private static ItemRepository itemRepository;
    private static StockRepository stockRepository;
    private static SaleRepository saleRepository;
    private ItemSaleRepository itemSaleRepository = ItemSaleRepository.getInstance();

    static{
        saleRepository = new SaleRepository();
        itemRepository = ItemRepository.getInstance();
        stockRepository =  StockRepository.getInstance();

    }


    private SaleRepository() {
        sales = FXCollections.observableArrayList();

        setDatabaseSalesData();

    }

    public static SaleRepository getInstance(){
        System.out.println("sale :"+saleRepository);
        return saleRepository;
    }

    public void setDatabaseSalesData(){
        try(Statement statement = connection.createStatement()){
            sales.clear();
            String sql = "SELECT * FROM sales";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                int itemId = resultSet.getInt(2);
                Item item = itemSaleRepository.getItem(itemId);
                LocalDate date = resultSet.getDate(3).toLocalDate();
                String time = resultSet.getTime(4).toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
                double noOfMetersSold = resultSet.getDouble(5);
                double noOfSuitsSold = resultSet.getDouble(6);
                double amount = resultSet.getDouble(7);
                double profit = resultSet.getDouble(8);

                sales.add(new Sale(id,item,date,time,noOfMetersSold,noOfSuitsSold,amount,profit));
            }
        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }

    public void addSale(Sale sale){
        String sql = "INSERT INTO sales(item_id,sale_date, sale_time,no_of_meters_sold,  no_of_suits_sold" +
                     ", amount, profit) VALUES(?,?,?,?,?,?,?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            LocalTime time = LocalTime.now();
            statement.setInt(1,sale.getItem().getId());
            statement.setDate(2,Date.valueOf(sale.getSaleDate()));
            statement.setTime(3,Time.valueOf(time));
            statement.setDouble(4,sale.getNoOfMetersSold());
            statement.setDouble(5,sale.getNoOfPiecesSold());
            statement.setDouble(6,sale.getSaleAmount());
            statement.setDouble(7,sale.getProfit());

            statement.execute();

            setDatabaseSalesData();

            String sql1 = "UPDATE stocks SET currently_in_stock_meters = ?,  currently_in_stock_pieces = ?" +
                    "   WHERE item_id = ?";

            try(PreparedStatement statement1 = connection.prepareStatement(sql1)){

                double currentlyInStockMeters = stockRepository.getStock().stream().filter(s -> s.getItem().equals(sale.getItem())).findAny().get().getCurrentlyInStockMeters() - sale.getNoOfMetersSold();
                double currentlyInStockPieces = stockRepository.getStock().stream().filter(s -> s.getItem().equals(sale.getItem())).findAny().get().getCurrentlyInStockPieces() - sale.getNoOfPiecesSold();


                statement1.setDouble(1,currentlyInStockMeters);
                statement1.setDouble(2,currentlyInStockPieces);
                statement1.setInt(3,sale.getItem().getId());

                statement1.execute();

                Stock stock = new Stock(sale.getItem());
                int index  = stockRepository.getStock().indexOf(stock);
                stockRepository.getStock().get(index).setCurrentlyInStockMeters(currentlyInStockMeters);
                stockRepository.getStock().get(index).setCurrentlyInStockPieces(currentlyInStockPieces);

            }

        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    public void deleteSale(Sale sale){
        String sql = "DELETE FROM sales WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,sale.getId());

            statement.execute();

            sales.remove(sale);


            String sql1 = "UPDATE stocks SET currently_in_stock_meters = ?,  currently_in_stock_pieces = ?" +
                    "   WHERE item_id = ?";

            try(PreparedStatement statement1 = connection.prepareStatement(sql1)){

                double currentlyInStockMeters = stockRepository.getStock().stream().filter(s -> s.getItem().equals(sale.getItem())).findAny().get().getCurrentlyInStockMeters() + sale.getNoOfMetersSold();
                double currentlyInStockPieces = stockRepository.getStock().stream().filter(s -> s.getItem().equals(sale.getItem())).findAny().get().getCurrentlyInStockPieces() + sale.getNoOfPiecesSold();


                statement1.setDouble(1,currentlyInStockMeters);
                statement1.setDouble(2,currentlyInStockPieces);
                statement1.setInt(3,sale.getItem().getId());

                statement1.execute();

                Stock stock = new Stock(sale.getItem());
                int index  = stockRepository.getStock().indexOf(stock);
                stockRepository.getStock().get(index).setCurrentlyInStockMeters(currentlyInStockMeters);
                stockRepository.getStock().get(index).setCurrentlyInStockPieces(currentlyInStockPieces);

            }


        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }

    public void updateSale(Sale sale, Sale old){
        String sql = "UPDATE sales SET sale_date = ?, no_of_meters_sold = ?," +
                " no_of_suits_sold = ?, amount = ?,profit = ? WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1,Date.valueOf(sale.getSaleDate()));
            statement.setDouble(2,sale.getNoOfMetersSold());
            statement.setDouble(3,sale.getNoOfPiecesSold());
            statement.setDouble(4,sale.getSaleAmount());
            statement.setDouble(5,sale.getProfit());
            statement.setLong(6,old.getId());

            statement.execute();

            ;
            sales.set(sales.indexOf(old),sale);

            String sql1 = "UPDATE stocks SET currently_in_stock_meters = ?,  currently_in_stock_pieces = ?" +
                    "   WHERE item_id = ?";

            try(PreparedStatement statement1 = connection.prepareStatement(sql1)){

                double currentlyInStockMeters = stockRepository.getStock().stream().filter(s -> s.getItem().equals(sale.getItem())).findAny().get().getCurrentlyInStockMeters() + sale.getNoOfMetersSold() - old.getNoOfMetersSold();
                double currentlyInStockPieces = stockRepository.getStock().stream().filter(s -> s.getItem().equals(sale.getItem())).findAny().get().getCurrentlyInStockPieces() + sale.getNoOfPiecesSold() - old.getNoOfPiecesSold();


                statement1.setDouble(1,currentlyInStockMeters);
                statement1.setDouble(2,currentlyInStockPieces);
                statement1.setInt(3,sale.getItem().getId());

                statement1.execute();

                Stock stock = new Stock(sale.getItem());
                int index  = stockRepository.getStock().indexOf(stock);
                stockRepository.getStock().get(index).setCurrentlyInStockMeters(currentlyInStockMeters);
                stockRepository.getStock().get(index).setCurrentlyInStockPieces(currentlyInStockPieces);

            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    public ObservableList<Sale> getSales() {
        return sales;
    }
}
