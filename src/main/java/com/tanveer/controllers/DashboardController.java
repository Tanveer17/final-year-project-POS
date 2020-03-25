package com.tanveer.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.ResourceBundle;

import com.tanveer.Main;
import com.tanveer.model.database.SaleRepository;
import com.tanveer.model.sale.Sale;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController{
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML
    private LineChart<String,Number> saleChart;
    private SaleRepository saleRepository;
    private ObservableList<Sale> sales;


    public void initialize() {
        makeStageDrageable();
        saleRepository = SaleRepository.getInstance();
        sales = saleRepository.getSales();
        settingSaleChart();


    }

    private void settingSaleChart(){
        System.out.println(LocalDate.now().getMonth().minus(12));
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Number of Month");
        saleChart.setTitle("Sale Monitoring");

        XYChart.Series series = new XYChart.Series();
//        series.getData().add(new XYChart.Data(1,sales.stream().filter(s -> s.getSaleDate().getMonth().equals(LocalDate.now().getMonth().minus(11))).mapToDouble(s -> s.getSaleAmount()).sum()));
//        series.getData().add(new XYChart.Data(2,sales.stream().filter(s -> s.getSaleDate().getMonth().equals(LocalDate.now().getMonth().minus(10))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(12).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(12).minusDays(LocalDate.now().minusMonths(12).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(12).plusDays(YearMonth.now().minusMonths(12).lengthOfMonth()-LocalDate.now().minusMonths(12).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(11).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(11).minusDays(LocalDate.now().minusMonths(11).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(11).plusDays(YearMonth.now().minusMonths(11).lengthOfMonth()-LocalDate.now().minusMonths(11).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(10).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(10).minusDays(LocalDate.now().minusMonths(10).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(10).plusDays(YearMonth.now().minusMonths(10).lengthOfMonth()-LocalDate.now().minusMonths(10).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(9).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(9).minusDays(LocalDate.now().minusMonths(9).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(9).plusDays(YearMonth.now().minusMonths(9).lengthOfMonth()-LocalDate.now().minusMonths(9).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(8).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(8).minusDays(LocalDate.now().minusMonths(8).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(8).plusDays(YearMonth.now().minusMonths(8).lengthOfMonth()-LocalDate.now().minusMonths(8).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(7).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(7).minusDays(LocalDate.now().minusMonths(7).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(7).plusDays(YearMonth.now().minusMonths(7).lengthOfMonth()-LocalDate.now().minusMonths(7).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(6).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(6).minusDays(LocalDate.now().minusMonths(6).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(6).plusDays(YearMonth.now().minusMonths(6).lengthOfMonth()-LocalDate.now().minusMonths(6).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(5).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(5).minusDays(LocalDate.now().minusMonths(5).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(5).plusDays(YearMonth.now().minusMonths(5).lengthOfMonth()-LocalDate.now().minusMonths(5).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(4).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(4).minusDays(LocalDate.now().minusMonths(4).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(4).plusDays(YearMonth.now().minusMonths(4).lengthOfMonth()-LocalDate.now().minusMonths(4).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(3).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(3).minusDays(LocalDate.now().minusMonths(3).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(3).plusDays(YearMonth.now().minusMonths(3).lengthOfMonth()-LocalDate.now().minusMonths(3).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(2).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(2).minusDays(LocalDate.now().minusMonths(2).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(2).plusDays(YearMonth.now().minusMonths(2).lengthOfMonth()-LocalDate.now().minusMonths(2).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));
        series.getData().add(new XYChart.Data(LocalDate.now().getMonth().minus(1).toString(),sales.stream().filter(s -> s.getSaleDate().isAfter(LocalDate.now().minusMonths(1).minusDays(LocalDate.now().minusMonths(1).getDayOfMonth()+1)) && s.getSaleDate().isBefore(LocalDate.now().minusMonths(1).plusDays(YearMonth.now().minusMonths(1).lengthOfMonth()-LocalDate.now().minusMonths(1).getDayOfMonth()))).mapToDouble(s -> s.getSaleAmount()).sum()));

        saleChart.getData().add(series);
    }

    @FXML
    public void settingPanel(){}

    private void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.getPrimaryStage().setX(event.getScreenX() - xOffset);
                Main.getPrimaryStage().setY(event.getScreenY() - yOffset);
                Main.getPrimaryStage().setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            Main.getPrimaryStage().setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            Main.getPrimaryStage().setOpacity(1.0f);
        });

    }

    @FXML
    public void showPurchaseStage()throws Exception{
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/purchase.fxml"));
        Scene scene = new Scene(root,1200,600);
        stage.setTitle("Purchases");
        stage.setScene(scene);
        stage.show();
        System.out.println("inside that");
    }

    @FXML
    public void showExpenseStage() throws Exception{
        Stage stage  = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/expanses.fxml"));
        Scene scene = new Scene(root,800,655);
        stage.setTitle("Expanses");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void showSaleStage() throws Exception{
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("/fxml/sales.fxml"));
        Scene scene = new Scene(root,800,655);
        stage.setTitle("SALES");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void showBillingStage() throws Exception{
        System.out.println("shown");
        Stage stage = new Stage();
        Parent root  = FXMLLoader.load(getClass().getResource("/fxml/billing.fxml"));
        Scene scene = new Scene(root,330,250);
        stage.setTitle("Billing");
        stage.setScene(scene);
        stage.show();
    }
}

