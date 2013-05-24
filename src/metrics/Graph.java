package metrics;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import src.MetaDataHandler;
import src.TraceFileInfo;

/**
 * This class handles the creation of new charts that are requested via TRAFIL's
 * chart tab.
 *
 * @author charalampi
 */
public class Graph {

    private MetaDataHandler metaHandler;
    private TraceFileInfo traceFile;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    /**
     * The constructor of the class initializes the variables of the class.
     *
     * @param metaHandler reference to the metaHandler that was matched to
     * current trace file
     * @param TraceFile the instance of the TraceFileInfo class for the current
     * trace file.
     */
    public Graph(MetaDataHandler metaHandler, TraceFileInfo TraceFile) {
        this.metaHandler = metaHandler;
        this.traceFile = TraceFile;
//	String temporaryLevel;
//	if (metaHandler.getNode().indexOf("SourceNode") != -1) {
//	    levels.add("Link Layer");
//	    levels.add("Network Layer");
//	} else {
//	    try {
//		st.execute("select distinct " + metaHandler.getNode().get(1) + " from " + traceFile.getTraceFileName() + ";");
//		rs = st.getResultSet();
//		while (rs.next()) {
//		    temporaryLevel = rs.getString(1);
//		    if (!temporaryLevel.equalsIgnoreCase("IFQ")) {
//			levels.add(temporaryLevel);
//		    }
//
//		}
//
//	    } catch (SQLException ex) {
//	    }
//	}

    }

    /**
     * This method creates charts between two nodes that were chosen by the
     * user. Depending in the chart selected it calls the appropriate method to
     * create the chart.
     *
     * @param startNode the start node selected by the user
     * @param endNode the end node selected by the user
     * @param level the trace level to which the information will refer
     * @param samplingRate the time interval in which the information will be
     * collected
     * @param chartType which type of chart was requested
     * @param lineTitle what title the new line has (optional)
     */
    public void createNodeToNodeChart(int startNode, int endNode, String level, int samplingRate, String chartType, String lineTitle) {

        if (chartType.equalsIgnoreCase("Packet Delivery Rate(packets/sec)")) {
            ThroughputChart throughput = new ThroughputChart(startNode, endNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = throughput.getChart();
        } else if (chartType.equalsIgnoreCase("Delay Jitter")) {
            DelayJitterChart delayjitter = new DelayJitterChart(startNode, endNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = delayjitter.getChart();
        } else if (chartType.equalsIgnoreCase("Packet End to End Delay")) {
            PacketEndToEndDelayChart packetdelay = new PacketEndToEndDelayChart(startNode, endNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = packetdelay.getChart();
        } else if (chartType.equalsIgnoreCase("Throughput(bits/sec)")) {
            ThroughputBitsChart throughputbits = new ThroughputBitsChart(startNode, endNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = throughputbits.getChart();
        }
    }

    /**
     * This method is called when a user requests a chart for a specific node.
     * depending on the chart type the proper method is called to create that
     * chart.
     *
     * @param startNode the specific node the user selected
     * @param level he trace level to which the information will refer
     * @param samplingRate the time interval in which the information will be
     * collected
     * @param chartType which type of chart was requested
     * @param lineTitle what title the new line has (optional)
     */
    public void createNodeSpecificChart(int startNode, String level, int samplingRate, String chartType, String lineTitle) {

        if (chartType.equalsIgnoreCase("Packet Delivery Rate(packets/sec)")) {
            ThroughputChart throughput = new ThroughputChart(startNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = throughput.getChart();
        } else if (chartType.equalsIgnoreCase("Delay Jitter")) {
            DelayJitterChart delayjitter = new DelayJitterChart(startNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = delayjitter.getChart();
        } else if (chartType.equalsIgnoreCase("Packet End to End Delay")) {
            PacketEndToEndDelayChart packetdelay = new PacketEndToEndDelayChart(startNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = packetdelay.getChart();
        } else if (chartType.equalsIgnoreCase("Throughput(bits/sec)")) {
            ThroughputBitsChart throughputbits = new ThroughputBitsChart(startNode, level, samplingRate, metaHandler, traceFile, lineTitle);
            chart = throughputbits.getChart();
        }
    }

    public JFreeChart getChart() {
        return chart;
    }

    public ChartPanel getGraph() {
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(693, 256));
        return chartPanel;
    }
}
