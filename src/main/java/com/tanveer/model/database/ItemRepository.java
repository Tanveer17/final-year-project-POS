package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ItemRepository {
    private Connection connection = Database.getConnection();
    private static ObservableList<Item> items = FXCollections.observableArrayList();
    private SupplierRepository supplierRepository;

    public ItemRepository() {
        supplierRepository = new SupplierRepository();
        setDatabaseItemsData();
    }

    private void setDatabaseItemsData(){
        if(items.isEmpty()) {
            ObservableList<ItemSupplier> suppliers;
            suppliers = supplierRepository.getSuppliers();

            try (Statement statement = connection.createStatement()) {
                String query = "SELECT * FROM items";
                ResultSet rs = statement.executeQuery(query);

                while (rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    int supplierId = rs.getInt(3);
                    ItemSupplier itemSupplier = suppliers.stream().filter(s -> s.getId() == supplierId).findAny().get();
                    double pricePerMeter = rs.getDouble(4);
                    double pricePerPiece = rs.getDouble(5);
                    double metersInASuit = rs.getDouble(6);

                    items.add(new Item(id, name, itemSupplier, pricePerMeter, pricePerPiece, metersInASuit));
                }
            } catch (SQLException s) {
                s.printStackTrace();
            }
        }
    }


    public ObservableList<Item> getItems() {
        return items;
    }

    public void addItem(Item item){
        String sql = "INSERT INTO items VALUES (?,?,?,?,?,?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            int supplierId = item.getSupplier().getId();
            preparedStatement.setInt(1,item.getId());
            preparedStatement.setString(2,item.getName());
            preparedStatement.setInt(3,supplierId);
            preparedStatement.setDouble(4,item.getPricePerMeter());
            preparedStatement.setDouble(5,item.getPricePerSuit());
            preparedStatement.setDouble(6,item.getMetersInASuit());

            preparedStatement.execute();

            items.add(item);
        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }
}
