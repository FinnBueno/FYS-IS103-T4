/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;

public class StatisticsController extends Controller {

    // line chart

    // bar chart
    private XYChart<?, ?> chart;
    private Axis xAxis;
    private Axis yAxis;

    @Override
    public boolean isOpen() {
        return Tabs.STATISTICS.isOpen(0);
    }

    @Override
    public void postInit() {
    }

    public void generateChart(ChartType type, boolean showLost, boolean showFound, boolean showDamaged) {

    }

    public static enum ChartType {
        LINE, BAR, PIE
    }

}
