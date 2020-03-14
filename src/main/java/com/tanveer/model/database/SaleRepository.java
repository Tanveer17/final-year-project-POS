package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.sale.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class SaleRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Sale> sales;
    private ItemRepository itemRepository;

    public SaleRepository() {
        sales = FXCollections.observableArrayList();
        itemRepository = new ItemRepository();
        setDatabaseSalesData();

    }

    private void setDatabaseSalesData(){
        try(Statement statement = connection.createStatement()){
            String sql = "SELECT * FROM sales";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                int itemId = resultSet.getInt(2);
                Item item = itemRepository.getItems().stream().filter(item1 -> item1.getId() == itemId).findAny().get();
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

    public ObservableList<Sale> getSales() {
        return sales;
    }
}
