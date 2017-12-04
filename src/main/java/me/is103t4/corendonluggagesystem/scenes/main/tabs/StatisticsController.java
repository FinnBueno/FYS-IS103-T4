/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

public class StatisticsController extends Controller {
    
    @FXML
    private BarChart<?, ?> SalaryChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @Override
    public boolean isOpen() {
        return Tabs.STATISTICS.isOpen(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        XYChart.Series set1 = new XYChart.Series<>();
   
        set1.getData().add(new XYChart.Data("lost luggage", 101));
        set1.getData().add(new XYChart.Data("found luggage", 152));
        set1.getData().add(new XYChart.Data("damaged luggage", 135));
        
        
        SalaryChart.getData().addAll(set1);
    }    
    
}
