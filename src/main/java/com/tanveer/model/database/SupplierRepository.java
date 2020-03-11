package com.tanveer.model.database;

import com.tanveer.model.purchases.ItemSupplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

public class SupplierRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<ItemSupplier> suppliers;

    public SupplierRepository(){
        suppliers = FXCollections.observableArrayList();
        setDatabaseSuppliersData();
    }

    private void setDatabaseSuppliersData(){
        suppliers.add(new ItemSupplier(8, "gulzada", "mingora"));
    }

    public ObservableList<ItemSupplier> getSuppliers() {
        return suppliers;
    }
}
