package metrics;

import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author MIKE
 */
public class DelayJitterChart extends Chart {

    /**
     * This constructor creates the delay jitter chart between two nodes that
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
    public DelayJitterChart(int startNode, int endNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	ArrayList<Double> endToEndDelay = new ArrayList();
	Double currentTime;
	Double delayJitter = 0.0;
	st = DatabaseConnection.getSt();
	series = new XYSeries("Delay Jitter");
	XYDataset xyDataset;
	long cstartTime, cendTime;

	series.add(0, 0);
	try {
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {
		if (level.equalsIgnoreCase("Link Layer")) {
		    System.out.println("Tracefilename:" + traceFile.getTraceFileName());
		    System.out.println("Metahandlerinfo:" + metaHandler.getGeneratePackets().get(0));
		    System.out.println("stratnode:" + startNode);
		    System.out.println("Endnode:" + endNode);
		    System.out.println("Level:" + level);
		    System.out.println("SampleRate:" + sampleRate);



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

			st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main "
				+ "inner join (select UniquePacketId,time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " "
				+ "and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and time<" + (currentTime + sampleRate) + " and time>=" + currentTime + ")as temp where main." + metaHandler.getGeneratePackets().get(2) + "=temp." + metaHandler.getGeneratePackets().get(2) + " "
				+ "and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and event='-' and main.time<=" + (currentTime + sampleRate) + " and main.time>" + currentTime + ";");

			System.out.println("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main "
				+ "inner join (select UniquePacketId,time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " "
				+ "and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and time<" + (currentTime + sampleRate) + " and time>=" + currentTime + ")as temp where main." + metaHandler.getGeneratePackets().get(2) + "=temp." + metaHandler.getGeneratePackets().get(2) + " "
				+ "and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and event='-' and main.time<=" + (currentTime + sampleRate) + " and main.time>" + currentTime + ";");
			rs = st.getResultSet();
			delayJitter = 0.0;
			endToEndDelay.clear();
			while (rs.next()) {
			    endToEndDelay.add(rs.getDouble(1));

			}

			for (int i = 0; i < endToEndDelay.size() - 1; i++) {

			    delayJitter = delayJitter + (Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)) - delayJitter) / 16;
			}
			currentTime += sampleRate;
			series.add(currentTime, delayJitter);
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
		    System.out.println("StartTime:" + startTime + "EndTime" + endTime);
		    currentTime = startTime;
//                    System.out.println("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
//                            + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " "
//                            + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and recTime<=" + (currentTime + sampleRate) + " "
//                            + " and recTime>" + currentTime + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
//                            + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + ""
//                            + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and main.time<=" + (currentTime + sampleRate) + " and main.time>" + currentTime + ";");
		    System.out.println("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner join " + traceFile.getTraceFileName() + " as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and main.event='-' and temp.event='r'"
			    + " and temp.time>" + currentTime + " and temp.time<=" + (currentTime + sampleRate) + " main." + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " main." + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and main." + metaHandler.getGeneratePackets().get(0) + "=" + startNode + ""
			    + " and temp." + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and temp." + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and temp." + metaHandler.getReceivedPackets().get(1) + "=" + endNode + ";");
		    while (endTime > currentTime) {
			cstartTime = System.currentTimeMillis();
//                        st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
//                                + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " "
//                                + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and time<" + (currentTime + sampleRate) + " "
//                                + " and time<" + (currentTime + sampleRate) + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
//                                + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + ""
//                                + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and main.time<=" + (currentTime + sampleRate) + " and main.time>" + currentTime + ";");
//
			st.execute("select (temp.time-main.time) from " + traceFile.getTraceFileName() + " as main inner join " + traceFile.getTraceFileName() + " as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and main.event='-' and temp.event='r'"
				+ " and temp.time>" + currentTime + " and temp.time<=" + (currentTime + sampleRate) + " and main." + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and main." + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and main." + metaHandler.getGeneratePackets().get(0) + "=" + startNode + ""
				+ " and temp." + metaHandler.getReceivedPackets().get(0) + "=" + endNode + " and temp." + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and temp." + metaHandler.getReceivedPackets().get(1) + "=" + endNode + ";");

			cendTime = System.currentTimeMillis();
			System.out.println("QueryTime:" + (cendTime - cstartTime));
			rs = st.getResultSet();
			delayJitter = 0.0;
			endToEndDelay.clear();
			while (rs.next()) {
			    endToEndDelay.add(rs.getDouble(1));

			}

			for (int i = 0; i < endToEndDelay.size() - 1; i++) {
			    delayJitter = delayJitter + (Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)) - delayJitter) / 16;
			}
			currentTime += sampleRate;
			series.add(currentTime, delayJitter);
			cstartTime = System.currentTimeMillis();
			System.out.println("JvaTime:" + (cstartTime - cendTime));
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

		while (endTime > currentTime) {
		    st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			    + "join (select distinct " + metaHandler.getReceivedPackets().get(0) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " "
			    + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getReceivedPackets().get(2) + "=" + endNode + " and " + metaHandler.getNode().get(1) + "='" + level + "' and time<" + (currentTime + sampleRate) + " and time>=" + currentTime + ")as temp where main." + metaHandler.getReceivedPackets().get(0) + "=temp." + metaHandler.getReceivedPackets().get(0) + " and"
			    + " event='s' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ""
			    + " and " + metaHandler.getNode().get(1) + "='" + level + "' and main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + ";");

		    rs = st.getResultSet();
		    delayJitter = 0.0;
		    endToEndDelay.clear();
		    while (rs.next()) {
			endToEndDelay.add(rs.getDouble(1));

		    }

		    for (int i = 0; i < endToEndDelay.size() - 1; i++) {

			delayJitter = delayJitter + (Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)) - delayJitter) / 16;
		    }
		    currentTime += sampleRate;
		    series.add(currentTime, delayJitter);
		}
	    }

	    xyDataset = new XYSeriesCollection(series);
	    chart = ChartFactory.createXYLineChart(null, "Time(sec)", "Delay Jitter(sec)", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("Delay Jitter vs Time", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));
	} catch (SQLException ex2) {
	    ex2.printStackTrace();

	}
    }

    /**
     * This constructor is employed when a delay jitter chart for a specific
     * node must be created.
     *
     * @param startNode the node the user selected
     * @param level the trace level to which the information will refer.
     * @param sampleRate the time interval un which the information will be
     * collected
     * @param metaHandler the meta handler instance for the current trace file.
     * @param traceFile the trace file instance for the current trace file.
     */
    public DelayJitterChart(int startNode, String level, int sampleRate, MetaDataHandler metaHandler, TraceFileInfo traceFile) {
	ArrayList<Double> endToEndDelay = new ArrayList();
	Double currentTime;
	Double delayJitter = 0.0;
	series = new XYSeries("Delay Jitter");
	XYDataset xyDataset;
	st = DatabaseConnection.getSt();
	series.add(0, 0);
	try {
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {
		if (level.equalsIgnoreCase("Link Layer")) {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + ";");
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
			st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main "
				+ "inner join (select UniquePacketId,time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode
				+ " and time<" + (currentTime + sampleRate) + " and time>=" + currentTime + ")as temp where main." + metaHandler.getGeneratePackets().get(2) + "=temp." + metaHandler.getGeneratePackets().get(2) + " "
				+ "and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode + " and event='-' and main.time<=" + (currentTime + sampleRate) + " and main.time>" + currentTime + ";");

			rs = st.getResultSet();
			delayJitter = 0.0;
			endToEndDelay.clear();
			while (rs.next()) {
			    endToEndDelay.add(rs.getDouble(1));

			}

			for (int i = 0; i < endToEndDelay.size() - 1; i++) {

			    delayJitter = delayJitter + (Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)) - delayJitter) / 16;
			}
			currentTime += sampleRate;
			series.add(currentTime, delayJitter);
		    }

		} else {
		    st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ";");
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
			st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
				+ "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and "
				+ metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and time<" + (currentTime + sampleRate) + " "
				+ " and time<" + (currentTime + sampleRate) + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
				+ " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startNode
				+ " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + " and main.time<=" + (currentTime + sampleRate) + " and main.time>" + currentTime + ";");
			rs = st.getResultSet();
			delayJitter = 0.0;
			endToEndDelay.clear();
			while (rs.next()) {
			    endToEndDelay.add(rs.getDouble(1));

			}

			for (int i = 0; i < endToEndDelay.size() - 1; i++) {
			    delayJitter = delayJitter + (Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)) - delayJitter) / 16;
			}
			currentTime += sampleRate;
			series.add(currentTime, delayJitter);
		    }
		}

	    } else {
		st.execute("select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode
			+ " and " + metaHandler.getNode().get(1) + "='" + level + "';");
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
		    st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
			    + "join (select distinct " + metaHandler.getReceivedPackets().get(0) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode
			    + " and " + metaHandler.getNode().get(1) + "='" + level + "' and time<" + (currentTime + sampleRate) + " and time>=" + currentTime + ")as temp where main." + metaHandler.getReceivedPackets().get(0) + "=temp." + metaHandler.getReceivedPackets().get(0) + " and"
			    + " event='s' and " + metaHandler.getGeneratePackets().get(2) + "=" + startNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startNode + ""
			    + " and " + metaHandler.getNode().get(1) + "='" + level + "' and main.time<" + (currentTime + sampleRate) + " and main.time>=" + currentTime + ";");

		    rs = st.getResultSet();
		    delayJitter = 0.0;
		    endToEndDelay.clear();
		    while (rs.next()) {
			endToEndDelay.add(rs.getDouble(1));

		    }

		    for (int i = 0; i < endToEndDelay.size() - 1; i++) {

			delayJitter = delayJitter + (Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)) - delayJitter) / 16;
		    }
		    currentTime += sampleRate;
		    series.add(currentTime, delayJitter);
		}
	    }

	    xyDataset = new XYSeriesCollection(series);
	    chart = ChartFactory.createXYLineChart(null, "Time(sec)", "Delay Jitter(sec)", xyDataset, PlotOrientation.VERTICAL, true, false, false);
	    chart.setTitle(new org.jfree.chart.title.TextTitle("Delay Jitter vs Time", new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16)));
	} catch (SQLException ex2) {
	    ex2.printStackTrace();

	}
    }
}
