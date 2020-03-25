package com.tanveer.model.database;

import com.tanveer.model.purchases.Item;
import com.tanveer.model.purchases.ItemSupplier;
import com.tanveer.model.purchases.PurchaseItem;
import com.tanveer.model.sale.Sale;
import com.tanveer.model.stocks.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ItemRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<Item> items;

    private static SupplierRepository supplierRepository = SupplierRepository.getInstance();
    private static ItemRepository itemRepository = new ItemRepository();
    private static PurchaseRepository purchaseRepository = PurchaseRepository.getInstance();
    private static ItemSaleRepository itemSaleRepository = ItemSaleRepository.getInstance();
    private static ItemStockRepository itemStockRepository = ItemStockRepository.getInstance();

//    static {
//        supplierRepository = SupplierRepository.getInstance();
//        itemRepository = new ItemRepository();
//        stockRepository = StockRepository.getInstance();
//        purchaseRepository = PurchaseRepository.getInstance();
//        saleRepository = SaleRepository.getInstance();
//
//    }


    private ItemRepository() {
        items = FXCollections.observableArrayList();
        setDatabaseItemsData();
    }

    public static ItemRepository getInstance() {
        return itemRepository;
    }

    private void setDatabaseItemsData() {
        if (items.isEmpty()) {
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

    public void addItem(Item item) {
        String sql = "INSERT INTO items VALUES (?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int supplierId = item.getSupplier().getId();
            preparedStatement.setInt(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setInt(3, supplierId);
            preparedStatement.setDouble(4, item.getPricePerMeter());
            preparedStatement.setDouble(5, item.getPricePerSuit());
            preparedStatement.setDouble(6, item.getMetersInASuit());

            preparedStatement.execute();

            items.add(item);
            itemStockRepository.addStockListing(item);
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }



    public void deleteItem(Item item) {
        String sql4 = "DELETE FROM items WHERE id = ?";
        int id = item.getId();


            executeQuery(sql4,id);
            items.removeIf(item1 -> item1.equals(item));
            itemStockRepository.removeItemFromStocks(item);
            itemSaleRepository.removeItemFromSales(item);
            purchaseRepository.getPurchases().removeIf(purchaseItem -> purchaseItem.getItemId() == item.getId());

        }


    public void updateItem(Item item, Item oldItem){
        String sql1= "UPDATE items SET id = ?, name = ?, supplier_id = ?, price_per_meter = ?," +
                      "  price_per_suit = ?,  meters_in_a_suit = ? WHERE id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql1)){
            preparedStatement.setInt(1,item.getId());
            preparedStatement.setString(2,item.getName());
            preparedStatement.setInt(3,item.getSupplier().getId());
            preparedStatement.setDouble(4,item.getPricePerMeter());
            preparedStatement.setDouble(5,item.getPricePerSuit());
            preparedStatement.setDouble(6,item.getMetersInASuit());
            preparedStatement.setInt(7,oldItem.getId());
            preparedStatement.execute();
            int index = items.indexOf(oldItem);
            System.out.println(index);
            items.set(index,item);
            PurchaseItem purchaseItem = purchaseRepository.getPurchases().stream().filter(purchase -> purchase.getItemId() == oldItem.getId()).findAny().get();
            purchaseItem.setItemId(item.getId());

            itemStockRepository.updateItem(item,oldItem);

            itemSaleRepository.updateItemInSales(oldItem,item);


        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    private boolean executeQuery(String sql, int id) {
        boolean isQuerySuccessfull = false;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
             preparedStatement.execute();
            isQuerySuccessfull =true;
        }
        catch (SQLException s){
            isQuerySuccessfull = false;
            s.printStackTrace();
        }
        return isQuerySuccessfull;
    }
}
