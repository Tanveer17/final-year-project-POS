package com.tanveer.model.database;

import com.tanveer.model.sale.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
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
        sales.add(new Sale(1,itemRepository.getItems().get(1),
                LocalDate.now(),
                LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                20,
                20/7,
                300,
                300-itemRepository.getItems().get(1).getPricePerSuit()));
    }

    public ObservableList<Sale> getSales() {
        return sales;
    }
}
