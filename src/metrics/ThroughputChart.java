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
 * This method creates the chart regarding Throughput(packets/sec)
 *
 * @author charalampi
 */
public class ThroughputChart extends Chart {

    /**
     * This constructor creates the throughput chart between two nodes that are
     * selected by the user via the GUI.
     *
     * @param startNode the start node selected by the user
     * @param endNode the end node selected by the user
     * @param level the trace level to which the information will refer.
     * @param sampleRate the time interval in which the information will be
     * collected
     * @param metaHandler the meta handler instance for the current trace file.
     * @param traceFile the trace file instance for the current trace file.
     */
    public ThroughputChart(int startNode, int endNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	try {
	    int packetNumber = 0;
	    double throughput;
	    double currentTime;
	    long calcStartTime, calcEndTime;
	    calcStartTime = System.currentTimeMillis();
	    st = DatabaseConnection.getSt();
	    series = new XYSeries("Throughput");
	    XYDataset xyDataset;
	    series.add(0, 0);
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {
		if (level.equalsIgnoreCase("Link Layer")) {
		    //JOptionPane.showMessageDialog(null, "traceFileName:"+traceFile.getTraceFileName());
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and "
			    + "" + metaHandler.getReceivedPackets().get(0) + "=" + endNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;

		    while (endTime > currentTime) {
			st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(2) + ") from " + traceFile.getTraceFileName() + " as main "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getGeneratePackets().get(0) + ""
				+ "=" + startNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and event='r';");

			rs = st.getResultSet();
			while (rs.next()) {
			    packetNumber = rs.getInt(1);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetNumber / (sampleRate);
			} else {
			    throughput = packetNumber / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		} else {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and "
			    + "" + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;
		    while (endTime > currentTime) {
			st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(2) + ") from " + traceFile.getTraceFileName() + " as main  "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getReceivedPackets().get(0) + ""
				+ "=" + endNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and event='r' and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");

			rs = st.getResultSet();
			while (rs.next()) {
			    packetNumber = rs.getInt(1);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetNumber / (sampleRate);
			} else {
			    throughput = packetNumber / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		}
	    } else {
		st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " and "
			+ "" + metaHandler.getReceivedPackets().get(2) + "=" + endNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");
		rs = st.getResultSet();
		while (rs.next()) {
		    startTime = rs.getDouble(1);
		    endTime = rs.getDouble(2);
		}
		if ((startTime == null) || (endTime == null)) {
		    return;
		}

		currentTime = startTime;
		System.out.println("select count(distinct " + metaHandler.getReceivedPackets().get(0) + ") from " + traceFile.getTraceFileName() + " as main "
			+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getGeneratePackets().get(2) + ""
			+ "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and event='r' and " + metaHandler.getReceivedPackets().get(2) + "=" + endNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");
		while (endTime > currentTime) {

		    st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(0) + ") from " + traceFile.getTraceFileName() + " as main "
			    + " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getGeneratePackets().get(2) + ""
			    + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and event='r' and " + metaHandler.getReceivedPackets().get(2) + "=" + endNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");


		    rs = st.getResultSet();
		    while (rs.next()) {
			packetNumber = rs.getInt(1);
		    }

		    if ((currentTime + sampleRate) < endTime) {
			throughput = packetNumber / (sampleRate);
		    } else {
			throughput = packetNumber / (endTime - currentTime);
		    }

		    currentTime += sampleRate;
		    series.add(currentTime, throughput);
		}
	    }
	    calcEndTime = System.currentTimeMillis();
	    System.out.println("Chart before library:" + (calcEndTime - startTime));
	    seriesList.add(series);
	    //xyDataset = new XYSeriesCollection(series);
	    xyDataset = createDataset();
	    chart = ChartFactory.createXYLineChart(null, "Time(sec)", "Packets Delivered", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("Packet Delivery Rate", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));
	    long tEndTime = System.currentTimeMillis();
	    System.out.println("Chart before library:" + (tEndTime - calcEndTime));
	} catch (SQLException ex) {
	    ex.getSQLState();
	    ex.printStackTrace();
	}

    }

    /**
     * This constructor is employed when a throughput chart for a specific node
     * must be created.
     *
     * @param startNode the node the user selected
     * @param level the trace level to which the information will refer.
     * @param sampleRate the time interval un which the information will be
     * collected
     * @param metaHandler the meta handler instance for the current trace file.
     * @param traceFile the trace file instance for the current trace file.
     */
    public ThroughputChart(int startNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	try {
	    int packetNumber = 0;
	    double throughput;
	    double currentTime;
	    st = DatabaseConnection.getSt();
	    series = new XYSeries("Throughput");
	    XYDataset xyDataset;
	    series.add(0, 0);
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {
		if (level.equalsIgnoreCase("Link Layer")) {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(0) + "=" + startNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;

		    while (endTime > currentTime) {
			st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(2) + ") from " + traceFile.getTraceFileName() + " as main "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getReceivedPackets().get(0) + ""
				+ "=" + startNode + " and event='r';");

			rs = st.getResultSet();
			while (rs.next()) {
			    packetNumber = rs.getInt(1);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetNumber / (sampleRate);
			} else {
			    throughput = packetNumber / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		} else {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(0) + "=" + startNode + " and "
			    + "" + metaHandler.getReceivedPackets().get(1) + "=" + startNode + ";");
		    rs = st.getResultSet();
		    while (rs.next()) {
			startTime = rs.getDouble(1);
			endTime = rs.getDouble(2);
		    }
		    if ((startTime == null) || (endTime == null)) {
			return;
		    }

		    currentTime = startTime;
		    while (endTime > currentTime) {
			st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(2) + ") from " + traceFile.getTraceFileName() + " as main  "
				+ " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getReceivedPackets().get(0) + ""
				+ "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + startNode + " and event='r' ;");

			rs = st.getResultSet();
			while (rs.next()) {
			    packetNumber = rs.getInt(1);
			}
			if ((currentTime + sampleRate) < endTime) {
			    throughput = packetNumber / (sampleRate);
			} else {
			    throughput = packetNumber / (endTime - currentTime);
			}

			currentTime += sampleRate;
			series.add(currentTime, throughput);
		    }
		}
	    } else {
//                st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + "" + metaHandler.getReceivedPackets().get(2) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + startNode + " and "+metaHandler.getNode().get(1) +"='" + level + "';");
		st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where (event='r' or event='s') and " + metaHandler.getReceivedPackets().get(2) + "=" + startNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");

		rs = st.getResultSet();
		while (rs.next()) {
		    startTime = rs.getDouble(1);
		    endTime = rs.getDouble(2);
		}
		if ((startTime == null) || (endTime == null)) {
		    return;
		}

		currentTime = startTime;

		while (endTime > currentTime) {
		    st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(0) + ") from " + traceFile.getTraceFileName() + " as main "
			    + " where main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + " and " + metaHandler.getReceivedPackets().get(2) + "=" + startNode + " and (event='r' or event='s') and " + " " + metaHandler.getNode().get(1) + "='" + level + "';");
		    rs = st.getResultSet();
		    while (rs.next()) {
			packetNumber = rs.getInt(1);
		    }

		    if ((currentTime + sampleRate) < endTime) {
			throughput = packetNumber / (sampleRate);
		    } else {
			throughput = packetNumber / (endTime - currentTime);
		    }

		    currentTime += sampleRate;
		    series.add(currentTime, throughput);
		}
	    }

	    seriesList.add(series);
	    //xyDataset = new XYSeriesCollection(series);
	    xyDataset = createDataset();
	    chart = ChartFactory.createXYLineChart(null, "Time(sec)", "Packets Delivered", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("Packet Delivery Rate", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));
	} catch (SQLException ex) {
	    ex.getSQLState();
	    ex.printStackTrace();
	}

    }
}
