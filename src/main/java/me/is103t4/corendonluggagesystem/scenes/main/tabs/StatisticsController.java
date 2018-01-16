/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageDataTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.FetchAirlinesTask;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.MonthYear;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static me.is103t4.corendonluggagesystem.scenes.main.tabs.StatisticsController.ChartType.*;

public class StatisticsController extends Controller {

    @FXML
    private AnchorPane chartHolder;

    // chart settings
    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private CheckBox showLost;

    @FXML
    private CheckBox showFound;

    @FXML
    private CheckBox showDamaged;

    @FXML
    private CheckBox showHandled;

    @FXML
    private CheckBox showDestroyed;

    @FXML
    private CheckBox showDepot;

    @FXML
    private TextField graphTitleField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> timespanSpinner;

    @FXML
    private ComboBox<String> airportBox;

    @FXML
    private Button generateButton;

    // chart
    private Chart chart;
    private Axis<String> xAxis;
    private Axis<Number> yAxis;

    @Override
    public boolean isOpen() {
        return Tabs.STATISTICS.isOpen(0);
    }

    @Override
    public void postInit(ResourceBundle bundle) {
        setEnterButton(generateButton);

        typeBox.setItems(FXCollections.observableArrayList(names()));

        timespanSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 36, 1));
        datePicker.setValue(LocalDate.now());

        FetchAirlinesTask task = new FetchAirlinesTask();
        task.setOnSucceeded(event -> {
            List<String> airlines = (List<String>) task.getValue();
            airlines.add(0, bundle.getString("dontCheck"));
            airportBox.setItems(FXCollections.observableList(airlines));
        });
    }

    @FXML
    public void generateChart() {
        if (typeBox.getSelectionModel().getSelectedItem() == null) {
            alert(bundle.getString("selectGraphType"));
            return;
        }

        // switch between graph types and run code to create one
        switch (ChartType.valueOf(typeBox.getSelectionModel().getSelectedItem().toUpperCase())) {
            case LINE:
                generateLineChart();
                break;
            case BAR:
                generateBarChart();
                break;
            case PIE:
                generatePieChart();
                break;
        }

        chartHolder.getChildren().clear();
        chartHolder.getChildren().add(chart);

        AnchorPane.setBottomAnchor(chart, 0D);
        AnchorPane.setRightAnchor(chart, 0D);
        AnchorPane.setLeftAnchor(chart, 0D);
        AnchorPane.setTopAnchor(chart, 0D);
    }

    private void generatePieChart() {
        // get data list ready
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(new ArrayList<>());

        // run db task to fetch data from db sorted per status per month
        FetchLuggageDataTask task = new FetchLuggageDataTask(datePicker.getValue(), timespanSpinner.getValue(),
                showLost.isSelected(), showFound.isSelected(), showDamaged.isSelected(), showHandled.isSelected(),
                showDestroyed.isSelected(), showDepot.isSelected(), airportBox.getSelectionModel().getSelectedItem());
        task.setOnSucceeded(event -> {
            // chart data sorted per status, per month in amounts
            // key in first map is the status (each status is entered once) and has a Map<MonthYear, Integer>, which has
            // MonthYear wrapper as key, holding a month with it's year, and an integer representing how many entries with
            // the specified status appear in that month
            Map<String, Map<MonthYear, Integer>> map = (Map<String, Map<MonthYear, Integer>>) task.getValue();
            // sort statusses
            List<String> statusses = new ArrayList<>(map.keySet());
            Collections.sort(statusses);
            Collections.reverse(statusses);
            // since a pie chart doesnt sort by month, just get the sum of every integer in the inner map
            for (String status : statusses) {
                Map<MonthYear, Integer> innerMap = map.get(status);
                int amount = innerMap.keySet().stream().mapToInt(innerMap::get).sum();
                if (amount == 0)
                    continue;
                // add to data list
                list.add(new PieChart.Data(status, amount));
            }
        });

        // create piechart
        chart = new PieChart(list);
        chart.setTitle(graphTitleField.getText());
    }

    private void generateBarChart() {
        // create axis
        xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        yAxis = new NumberAxis();
        yAxis.setLabel("Luggage");
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(graphTitleField.getText());

        // run db task to fetch data from db sorted per status per month
        FetchLuggageDataTask task = new FetchLuggageDataTask(datePicker.getValue(), timespanSpinner.getValue(),
                showLost.isSelected(), showFound.isSelected(), showDamaged.isSelected(), showHandled.isSelected(),
                showDestroyed.isSelected(), showDepot.isSelected(), airportBox.getSelectionModel().getSelectedItem());
        task.setOnSucceeded(event -> {
            // chart data sorted per status, per month in amounts
            // key in first map is the status (each status is entered once) and has a Map<MonthYear, Integer>, which has
            // MonthYear wrapper as key, holding a month with it's year, and an integer representing how many entries with
            // the specified status appear in that month
            Map<String, Map<MonthYear, Integer>> map = (Map<String, Map<MonthYear, Integer>>) task.getValue();
            for (String status : map.keySet()) {
                // create and set series name
                XYChart.Series series = new XYChart.Series();
                series.setName(status);
                // sort
                Map<MonthYear, Integer> innerMap = map.get(status);
                List<MonthYear> months = new ArrayList<>(innerMap.keySet());
                Collections.sort(months);
                // input values
                for (MonthYear monthYear : months)
                    series.getData().add(new XYChart.Data<String, Number>(monthYear.getMonth().getDisplayName
                            (TextStyle.FULL, Locale.ENGLISH), task.get(innerMap, monthYear)));
                // add to chart
                ((BarChart) chart).getData().add(series);
            }
        });
    }

    private void generateLineChart() {
        // create axis
        xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        yAxis = new NumberAxis();
        yAxis.setLabel("Luggage");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(graphTitleField.getText());

        // run db task to fetch data from db sorted per status per month
        FetchLuggageDataTask task = new FetchLuggageDataTask(datePicker.getValue(), timespanSpinner.getValue(),
                showLost.isSelected(), showFound.isSelected(), showDamaged.isSelected(), showHandled.isSelected(),
                showDestroyed.isSelected(), showDepot.isSelected(), airportBox.getSelectionModel().getSelectedItem());
        task.setOnSucceeded(event -> {
            // chart data sorted per status, per month in amounts
            // key in first map is the status (each status is entered once) and has a Map<MonthYear, Integer>, which has
            // MonthYear wrapper as key, holding a month with it's year, and an integer representing how many entries with
            // the specified status appear in that month
            Map<String, Map<MonthYear, Integer>> map = (Map<String, Map<MonthYear, Integer>>) task.getValue();
            for (String status : map.keySet()) {
                // create and set series name
                XYChart.Series series = new XYChart.Series();
                series.setName(status);
                // sort
                Map<MonthYear, Integer> innerMap = map.get(status);
                List<MonthYear> months = new ArrayList<>(innerMap.keySet());
                Collections.sort(months);
                // input values
                for (MonthYear monthYear : months)
                    series.getData().add(new XYChart.Data<String, Number>(monthYear.getMonth().getDisplayName
                            (TextStyle.FULL, Locale.ENGLISH), task.get(innerMap, monthYear)));
                // add to chart
                ((LineChart) chart).getData().add(series);
            }
        });
    }

    /**
     * Inner enum containing all available chart types
     */
    public enum ChartType {
        LINE, BAR, PIE;

        /**
         * @return Human readable names of each chart type
         */
        public static List<String> names() {
            return Arrays.stream(values()).map(ChartType::toString).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return name().toCharArray()[0] + name().toLowerCase().substring(1, name().length());
        }
    }

}
