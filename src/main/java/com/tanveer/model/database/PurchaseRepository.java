package com.tanveer.model.database;

import com.tanveer.model.purchases.PurchaseItem;;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PurchaseRepository {
    private Connection connection = Database.getConnection();
    private ObservableList<PurchaseItem> purchases;
    private static PurchaseRepository purchaseRepository;
    private static StockRepository stockRepository;

    private PurchaseRepository(){
        purchases  = FXCollections.observableArrayList();


        setDatabasePurchasesData();
    }

    static {
        purchaseRepository = new PurchaseRepository();
        stockRepository = StockRepository.getInstance();
    }

    public static PurchaseRepository getInstance(){
        return purchaseRepository;
    }

    public ObservableList<PurchaseItem> getPurchases(){
        return purchases;
    }

    public void setDatabasePurchasesData(){

        try(Statement statement = connection.createStatement()){
            purchases.clear();
            String query = "SELECT * FROM purchases";
             ResultSet rs  = statement.executeQuery(query);
             while(rs.next()){
                 int id = rs.getInt(1);
                 int itemId = rs.getInt(2);
                 LocalDate date = rs.getDate(3).toLocalDate();
                 double pricePerMeter = rs.getDouble(4);
                 double noOfMetersPurchased = rs.getDouble(5);
                 double pricePerPiece = rs.getDouble(6);
                 double noOfPiecesPurchased = rs.getDouble(7);
                 double totalPrice = rs.getDouble(8);
                 double pricePaid = rs.getDouble(9);
                 double priceRem = rs.getDouble(10);

                 purchases.add(new PurchaseItem(itemId,id,date,pricePerMeter,noOfMetersPurchased,pricePerPiece,noOfPiecesPurchased,
                                                 totalPrice,pricePaid,priceRem));

             }
        }
        catch(SQLException s){
            s.printStackTrace();
        }
    }

    public void addPurchaseItem(PurchaseItem purchaseItem){
        String sql = "INSERT INTO purchases(item_id, purchase_date, price_per_meter, no_of_meter_purchases," +
                     " price_per_piece, no_of_pieces_purchases, total_price, price_paid, price_rem ) " +
                    " VALUES(?,?,?,?,?,?,?,?,?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,purchaseItem.getItemId());
            preparedStatement.setDate(2,Date.valueOf(purchaseItem.getPurchaseDate()));
            preparedStatement.setDouble(3,purchaseItem.getPricePerMeter());
            preparedStatement.setDouble(4,purchaseItem.getNoOfMetersPurchased());
            preparedStatement.setDouble(5,purchaseItem.getPricePerPiece());
            preparedStatement.setDouble(6,purchaseItem.getNoOfPiecesPurchased());
            preparedStatement.setDouble(7,purchaseItem.getTotalPrice());
            preparedStatement.setDouble(8,purchaseItem.getPricePaid());
            preparedStatement.setDouble(9,purchaseItem.getPriceRem());

            preparedStatement.execute();
            purchases.add(purchaseItem);
            stockRepository.updateStock(purchaseItem);
            addJustPriceHistory(purchaseItem);


        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    private void addJustPriceHistory(PurchaseItem purchaseItem) throws SQLException {
        String sql1 = "SELECT * FROM item_price_history WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql1)) {
            statement.setInt(1, purchaseItem.getItemId());

             statement.execute();
             ResultSet resultSet = statement.getResultSet();

            if (!resultSet.next()) {
                System.out.println("not resultset");
                String sql2 = "INSERT INTO item_price_history(item_id, price_per_meter,price_per_suit,fromDate)" +
                        " VALUES(?,?,?,?)";
                try (PreparedStatement statement1 = connection.prepareStatement(sql2)) {
                    statement1.setInt(1, purchaseItem.getItemId());
                    statement1.setDouble(2, purchaseItem.getPricePerMeter());
                    statement1.setDouble(3, purchaseItem.getPricePerPiece());
                    statement1.setDate(4, Date.valueOf(purchaseItem.getPurchaseDate()));

                    statement1.execute();
                    System.out.println("executed");
                } catch (SQLException s) {
                    s.printStackTrace();
                    System.out.println("inner try");
                }
            } else {
                resultSet.afterLast();

                if (resultSet.previous()) {
                    int id = resultSet.getInt("price_id");
                    double pricePerMeter = resultSet.getDouble("price_per_meter");
                    double pricePerPiece = resultSet.getDouble("price_per_suit");

                    if (pricePerMeter != purchaseItem.getPricePerMeter() || pricePerPiece != purchaseItem.getPricePerPiece()) {
                        String sql3 = "UPDATE item_price_history SET toDate = ? WHERE price_id = ?";

                        try (PreparedStatement statement1 = connection.prepareStatement(sql3)) {
                            statement1.setDate(1, Date.valueOf(LocalDate.now()));
                            statement1.setInt(2, id);
                            statement1.execute();

                            String sql4 = "INSERT INTO item_price_history(item_id, price_per_meter,price_per_suit,fromDate)" +
                                    " VALUES(?,?,?,?)";
                            try (PreparedStatement statement2 = connection.prepareStatement(sql4)) {
                                statement2.setInt(1, purchaseItem.getItemId());
                                statement2.setDouble(2, purchaseItem.getPricePerMeter());
                                statement2.setDouble(3, purchaseItem.getPricePerPiece());
                                statement2.setDate(4, Date.valueOf(purchaseItem.getPurchaseDate()));

                                statement2.execute();
                            } catch (SQLException s) {
                                s.printStackTrace();
                                System.out.println("inner try 2");
                            }
                        }
                    }

                }
            }

        }
    }

   public void updatePurchaseItem(PurchaseItem purchaseItem){
        String sql = "UPDATE purchases SET purchase_date = ?, price_per_meter = ?, no_of_meter_purchases = ?, price_per_piece = ?," +
                "  no_of_pieces_purchases = ?, total_price = ?, price_paid = ?, price_rem = ? WHERE id = ? ";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setDate(1,Date.valueOf(purchaseItem.getPurchaseDate()));
            preparedStatement.setDouble(2,purchaseItem.getPricePerMeter());
            preparedStatement.setDouble(3,purchaseItem.getNoOfMetersPurchased());
            preparedStatement.setDouble(4,purchaseItem.getPricePerPiece());
            preparedStatement.setDouble(5,purchaseItem.getNoOfPiecesPurchased());
            preparedStatement.setDouble(6,purchaseItem.getTotalPrice());
            preparedStatement.setDouble(7,purchaseItem.getPricePaid());
            preparedStatement.setDouble(8,purchaseItem.getPriceRem());
            preparedStatement.setInt(9,purchaseItem.getPurchaseId());

            preparedStatement.execute();
            stockRepository.updateStock(purchaseItem);

            int index = purchases.indexOf(purchaseItem);
            purchases.set(index,purchaseItem);
        }
        catch (SQLException s){
            s.printStackTrace();
        }



   }


    public void deletePurchaseItem(PurchaseItem purchaseItem){
        String sql = "DELETE FROM purchases WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,purchaseItem.getPurchaseId());

            preparedStatement.execute();

            purchases.remove(purchaseItem);
        }
        catch (SQLException s){
            s.printStackTrace();
        }


    }

}
