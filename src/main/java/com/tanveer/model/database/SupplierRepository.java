package com.tanveer.model.database;

import com.tanveer.model.purchases.ItemSupplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SupplierRepository {
    private Connection connection = Database.getConnection();
    private static ObservableList<ItemSupplier> suppliers = FXCollections.observableArrayList();

    public SupplierRepository(){
        setDatabaseSuppliersData();
    }



    private void setDatabaseSuppliersData(){
        if(suppliers.isEmpty()) {
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT * FROM suppliers";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String address = resultSet.getString(3);
                    String phoneNumber = resultSet.getString(4);
                    suppliers.add(new ItemSupplier(id, name, address, phoneNumber));
                }
            } catch (SQLException s) {
                s.printStackTrace();
            }
        }
    }

    public ObservableList<ItemSupplier> getSuppliers() {
        return suppliers;
    }

    public void addSupplier(ItemSupplier supplier){
        String sql = "INSERT INTO suppliers (name,address,phone_number) VALUES(?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,supplier.getName());
            preparedStatement.setString(2,supplier.getAddress());
            preparedStatement.setString(3,supplier.getPhoneNumber());

            preparedStatement.execute();

            suppliers.add(supplier);
        }
        catch(SQLException s){
            s.printStackTrace();
        }


    }
}
