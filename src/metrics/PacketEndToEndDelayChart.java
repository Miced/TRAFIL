package metrics;

import java.sql.SQLException;
import static metrics.Chart.seriesList;
import org.jfree.chart.ChartFactory;
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
public class PacketEndToEndDelayChart extends Chart {

    /**
     * This constructor creates the packet delay chart between two nodes that
     * are selected by the user via the GUI.
     *
     * @param startNode the start node selected by the user
     * @param endNode the end node selected by the user
     * @param level the trace level to which the information will refer.
     * @param sampleRate the time interval in which the information will be
     * collected
     * @param metaHandler the meta handler instance for the current trace file.
     * @param traceFile the trace file instance for the current trace file.
     */
    public PacketEndToEndDelayChart(int startNode, int endNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	try {
	    series = new XYSeries("End to End Delay");
	    XYDataset xyDataset;
	    series.add(0, 0);
	    st = DatabaseConnection.getSt();
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {

		if (level.equalsIgnoreCase("Link Layer")) {
		    st.execute("select main." + metaHandler.getReceivedPackets().get(2) + ",(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			    + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " "
			    + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
			    + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + ";");
		} else {
		    st.execute("select main." + metaHandler.getReceivedPackets().get(2) + ",(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			    + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " "
			    + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
			    + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + ""
			    + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");
		}
	    } else {

		st.execute("select main." + metaHandler.getReceivedPackets().get(0) + ",(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			+ "join (select distinct " + metaHandler.getReceivedPackets().get(0) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " "
			+ " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getReceivedPackets().get(2) + "=" + endNode + " and " + metaHandler.getNode().get(1) + "='" + level + "')as temp where main." + metaHandler.getReceivedPackets().get(0) + "=temp." + metaHandler.getReceivedPackets().get(0) + " and"
			+ " event='s' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ""
			+ " and " + metaHandler.getNode().get(1) + "='" + level + "';");
	    }

	    rs = st.getResultSet();
	    while (rs.next()) {
		series.add(rs.getInt(1), rs.getDouble(2));
	    }

	    seriesList.add(series);
	    //xyDataset = new XYSeriesCollection(series);
	    xyDataset = createDataset();
	    chart = ChartFactory.createXYLineChart(null, "Packet Id", "End to End Delay(sec)", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("End to End Delay over Packet Id", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));

	} catch (SQLException ex) {
	    ex.getSQLState();
	    ex.printStackTrace();

	}
    }

    /**
     * This constructor is employed when a packet delay chart for a specific
     * node must be created.
     *
     * @param startNode the node the user selected
     * @param level the trace level to which the information will refer.
     * @param sampleRate the time interval un which the information will be
     * collected
     * @param metaHandler the meta handler instance for the current trace file.
     * @param traceFile the trace file instance for the current trace file.
     */
    public PacketEndToEndDelayChart(int startNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	try {
	    series = new XYSeries("End to End Delay");
	    XYDataset xyDataset;
	    series.add(0, 0);
	    st = DatabaseConnection.getSt();
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {

		if (level.equalsIgnoreCase("Link Layer")) {
		    st.execute("select main." + metaHandler.getReceivedPackets().get(2) + ",(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			    + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode
			    + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
			    + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + ";");
		} else {
		    st.execute("select main." + metaHandler.getReceivedPackets().get(2) + ",(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			    + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and "
			    + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
			    + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");
		}
	    } else {

		st.execute("select main." + metaHandler.getReceivedPackets().get(0) + ",(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			+ "join (select distinct " + metaHandler.getReceivedPackets().get(0) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode
			+ " and " + metaHandler.getNode().get(1) + "='" + level + "')as temp where main." + metaHandler.getReceivedPackets().get(0) + "=temp." + metaHandler.getReceivedPackets().get(0) + " and"
			+ " event='s' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode
			+ " and " + metaHandler.getNode().get(1) + "='" + level + "';");
	    }

	    rs = st.getResultSet();
	    while (rs.next()) {
		series.add(rs.getInt(1), rs.getDouble(2));
	    }
	    seriesList.add(series);
	    //xyDataset = new XYSeriesCollection(series);
	    xyDataset = createDataset();
	    chart = ChartFactory.createXYLineChart(null, "Packet Id", "End to End Delay(sec)", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("End to End Delay over Packet Id", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));

	} catch (SQLException ex) {
	    ex.getSQLState();
	    ex.printStackTrace();

	}
    }
}
