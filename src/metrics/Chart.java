/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import src.MetaDataHandler;
import src.TraceFileInfo;
import utilities.DatabaseConnection;

/**
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
}
