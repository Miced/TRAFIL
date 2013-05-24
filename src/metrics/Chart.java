/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import utilities.DatabaseConnection;

/**
 * This class handles the creation of new charts that are requested via TRAFIL's
 * chart tab.
 *
 * @author Drakoulelis
 */
public abstract class Chart {

    protected Double startTime, endTime;
    protected ResultSet rs;
    protected Statement st;
    protected JFreeChart chart;
    protected XYSeries series;
    protected static Collection<XYSeries> seriesList = new ArrayList<>();
    private ChartPanel chartPanel;
    protected static String chartTitle;
    protected String lineTitle;

    public Chart() {
        this.st = DatabaseConnection.getSt();
    }

    protected XYDataset createDataset() {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (XYSeries xy : seriesList) {
            dataset.addSeries(xy);
        }

        System.out.println("Number of charts: " + seriesList.size());

        return dataset;
    }

    public JFreeChart getChart() {
        return chart;
    }

    public ChartPanel getGraph() {
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(693, 256));
        return chartPanel;
    }

    public static void resetGraph() {
        seriesList.removeAll(seriesList);
    }

    public static void setChartTitle(String chartTitle) {
        Chart.chartTitle = chartTitle;
    }
}
