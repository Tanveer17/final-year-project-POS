package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;

public class ItemRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Item> items;
    private SupplierRepository supplierRepository;

    public ItemRepository() {
        items = FXCollections.observableArrayList();
        supplierRepository = new SupplierRepository();
        setDatabaseItemsData();
    }

    private void setDatabaseItemsData(){
        ObservableList<ItemSupplier> suppliers;
        suppliers = supplierRepository.getSuppliers();
        items.add(new Item(8, "Brace", new ItemSupplier(1, "gulzada", "mingora"),231,231/7,7));
        items.add(new Item(7, "Grace", new ItemSupplier(1, "gulzada", "mingora"),200,200/7,7));
    }

    public ObservableList<Item> getItems() {
        return items;
    }
}
