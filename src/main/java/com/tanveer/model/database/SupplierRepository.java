package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.sql.*;

public class SupplierRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<ItemSupplier> suppliers;
    private static SupplierRepository supplierRepository;

    private SupplierRepository(){
        suppliers = FXCollections.observableArrayList();
        setDatabaseSuppliersData();
    }
    static {
        supplierRepository = new SupplierRepository();
    }

    public static SupplierRepository getInstance(){
        return supplierRepository;
    }



    private void setDatabaseSuppliersData(){
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

    public void deleteItem(ItemSupplier supplier){
        String sql = "DELETE FROM suppliers WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,supplier.getId());

            statement.execute();

            suppliers.remove(supplier);
        }

        catch (SQLException s){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeight(300);
            alert.setTitle("Error");
            alert.setHeaderText("There is an Item associated");
            alert.setContentText("If you want to delete this supplier first delete the item or change their Supplier");
            alert.show();
        }
    }

    public void updateSupplier(ItemSupplier supplier , ItemSupplier old){
        String sql = "UPDATE suppliers SET name = ?,address = ?,phone_number = ? WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,supplier.getName());
            statement.setString(2,supplier.getAddress());
            statement.setString(3,supplier.getPhoneNumber());

            statement.setInt(4,old.getId());

            statement.execute();

            int index = suppliers.indexOf(old);
            suppliers.set(index,supplier);
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }
}
