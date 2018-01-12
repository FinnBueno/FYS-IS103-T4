/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageDataTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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
    private TextField xAxisField;

    @FXML
    private TextField yAxisField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> timespanSpinner;

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
    public void postInit() {
        setEnterButton(generateButton);

        typeBox.setItems(FXCollections.observableArrayList(names()));

        timespanSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void generateChart() {
        if (typeBox.getSelectionModel().getSelectedItem() == null) {
            alert("Please select a graph type first!");
            return;
        }

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
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(new ArrayList<>());

        FetchLuggageDataTask task = new FetchLuggageDataTask(datePicker.getValue(), timespanSpinner.getValue(),
                showLost.isSelected(), showFound.isSelected(), showDamaged.isSelected(), showHandled.isSelected(),
                showDestroyed.isSelected(), showDepot.isSelected());
        task.setOnSucceeded(event -> {
            // chart data sorted per status, per month in amounts
            Map<String, Map<Month, Integer>> map = (Map<String, Map<Month, Integer>>) task.getValue();
            List<String> statusses = new ArrayList<>(map.keySet());
            Collections.sort(statusses);
            Collections.reverse(statusses);
            for (String status : statusses) {
                Map<Month, Integer> innerMap = map.get(status);
                int amount = innerMap.keySet().stream().mapToInt(innerMap::get).sum();
                if (amount == 0)
                    continue;
                list.add(new PieChart.Data(status, amount));
            }
        });

        chart = new PieChart(list);
        chart.setTitle(graphTitleField.getText());
    }

    private void generateBarChart() {
        xAxis = new CategoryAxis();
        xAxis.setLabel(xAxisField.getText());
        yAxis = new NumberAxis();
        yAxis.setLabel(yAxisField.getText());
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(graphTitleField.getText());

        FetchLuggageDataTask task = new FetchLuggageDataTask(datePicker.getValue(), timespanSpinner.getValue(),
                showLost.isSelected(), showFound.isSelected(), showDamaged.isSelected(), showHandled.isSelected(),
                showDestroyed.isSelected(), showDepot.isSelected());
        task.setOnSucceeded(event -> {
            // chart data sorted per status, per month in amounts
            Map<String, Map<Month, Integer>> map = (Map<String, Map<Month, Integer>>) task.getValue();
            for (String status : map.keySet()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(status);
                Map<Month, Integer> innerMap = map.get(status);
                List<Month> months;
                Collections.sort(months = new ArrayList<>(innerMap.keySet()));
                for (Month month : months) {
                    series.getData().add(new XYChart.Data<String, Number>(month.getDisplayName(TextStyle.FULL, Locale
                            .ENGLISH), innerMap.get(month)));
                }
                ((BarChart) chart).getData().add(series);
            }
        });
    }

    private void generateLineChart() {
        xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        yAxis = new NumberAxis();
        yAxis.setLabel(yAxisField.getText());
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(graphTitleField.getText());

        FetchLuggageDataTask task = new FetchLuggageDataTask(datePicker.getValue(), timespanSpinner.getValue(),
                showLost.isSelected(), showFound.isSelected(), showDamaged.isSelected(), showHandled.isSelected(),
                showDestroyed.isSelected(), showDepot.isSelected());
        task.setOnSucceeded(event -> {
            // chart data sorted per status, per month in amounts
            Map<String, Map<Month, Integer>> map = (Map<String, Map<Month, Integer>>) task.getValue();
            for (String status : map.keySet()) {
                XYChart.Series series = new XYChart.Series();
                series.setName(status);
                Map<Month, Integer> innerMap = map.get(status);
                List<Month> months;
                Collections.sort(months = new ArrayList<>(innerMap.keySet()));
                for (Month month : months) {
                    series.getData().add(new XYChart.Data<String, Number>(month.getDisplayName(TextStyle.FULL, Locale
                            .ENGLISH), innerMap.get(month)));
                }
                ((LineChart) chart).getData().add(series);
            }
        });
    }

    public enum ChartType {
        LINE, BAR, PIE;

        public static List<String> names() {
            return Arrays.stream(values()).map(ChartType::toString).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return name().toCharArray()[0] + name().toLowerCase().substring(1, name().length());
        }
    }

}
