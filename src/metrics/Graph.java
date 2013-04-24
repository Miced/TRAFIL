package metrics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import src.MetaDataHandler;
import src.TraceFileInfo;
import utilities.DatabaseConnection;

/**
 * This class handles the creation of new charts that are requested via TRAFIL's
 * chart tab.
 *
 * @author charalampi
 */
public class Graph {

    private MetaDataHandler metaHandler;
    private Statement st;
    private TraceFileInfo traceFile;
    private int startingNode, endingNode;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private ArrayList<String> levels = new ArrayList();
    private ResultSet rs;
    private String level;
    private int sampleRate;

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
	this.st = DatabaseConnection.getSt();
	this.traceFile = TraceFile;
	String temporaryLevel;
	if (metaHandler.getNode().indexOf("SourceNode") != -1) {
	    levels.add("Link Layer");
	    levels.add("Network Layer");
	} else {
	    try {
		st.execute("select distinct " + metaHandler.getNode().get(1) + " from " + traceFile.getTraceFileName() + ";");
		rs = st.getResultSet();
		while (rs.next()) {
		    temporaryLevel = rs.getString(1);
		    if (!temporaryLevel.equalsIgnoreCase("IFQ")) {
			levels.add(temporaryLevel);
		    }

		}

	    } catch (SQLException ex) {
	    }
	}

    }

    /**
     * This method creates charts between two nodes that were chosen by the
     * user. Depending in the chart selected it calls the appropriate method to
     * create the chart.
     *
     * @param startNode the start node selected by the user
     * @param endNode the end node selected by the user
     * @param Level the trace level to which the information will refer
     * @param samplingRate the time interval in which the information will be
     * collected
     * @param chartType which type of chart was requested
     */
    public void createNodeToNodeChart(int startNode, int endNode, String Level, int samplingRate, String chartType) {
	this.startingNode = startNode;
	this.endingNode = endNode;
	this.level = Level;
	this.sampleRate = samplingRate;

	if (chartType.equalsIgnoreCase("Packet Delivery Rate(packets/sec)")) {
	    createThroughputChart("NodeToNode");
	} else if (chartType.equalsIgnoreCase("Delay Jitter")) {
	    createDelayJitterChart("NodeToNode");
	} else if (chartType.equalsIgnoreCase("Packet End to End Delay")) {
	    createPacketEndToEndDelay("NodeToNode");
	} else if (chartType.equalsIgnoreCase("Throughput(bits/sec)")) {
	    createThroughputBits("NodeToNode");
	}

    }

    /**
     * This method is called when a user requests a chart for a specific node.
     * depending on the chart type the proper method is called to create that
     * chart.
     *
     * @param startNode the specific node the user selected
     * @param Level he trace level to which the information will refer
     * @param smaplingRate the time interval in which the information will be
     * collected
     * @param chartType which type of chart was requested
     */
    public void createNodeSpecificChart(int startNode, String Level, int smaplingRate, String chartType) {
	this.startingNode = startNode;
	this.level = Level;
	this.sampleRate = smaplingRate;

	if (chartType.equalsIgnoreCase("Packet Delivery Rate(packets/sec)")) {
	    createThroughputChart("NodeSpecific");
	} else if (chartType.equalsIgnoreCase("Delay Jitter")) {
	    createDelayJitterChart("NodeSpecific");
	} else if (chartType.equalsIgnoreCase("Packet End to End Delay")) {
	    createPacketEndToEndDelay("NodeSpecific");
	} else if (chartType.equalsIgnoreCase("Throughput(bits/sec)")) {
	    createThroughputBits("NodeSpecific");
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

    /**
     * This method creates the chart Throughput(packets/sec). If the chart is
     * regarding 2 nodes or one specific node it calls the appropriate
     * constructor of the ThroughputChart class who will create the actual
     * chart.
     *
     * @param chartType indicates if the chart is between 2 nodes or for a
     * specific single node.
     */
    private void createThroughputChart(String chartType) {
	if (chartType.equalsIgnoreCase("NodetoNode")) {
	    ThroughputChart throughput = new ThroughputChart(startingNode, endingNode, level, sampleRate, metaHandler, traceFile);
	    chart = throughput.getChart();
	} else if (chartType.equalsIgnoreCase("NodeSpecific")) {
	    ThroughputChart throughput = new ThroughputChart(startingNode, level, sampleRate, metaHandler, traceFile);
	    chart = throughput.getChart();
	}
    }

    /**
     * This method creates the chart Delay Jitter. If the chart is regarding 2
     * nodes or one specific node it calls the appropriate constructor of the
     * DelayJitterChart class who will create the actual chart.
     *
     * @param chartType indicates if the chart is between 2 nodes or for a
     * specific single node.
     */
    private void createDelayJitterChart(String chartType) {
	if (chartType.equalsIgnoreCase("NodetoNode")) {
	    DelayJitterChart delayjitter = new DelayJitterChart(startingNode, endingNode, level, sampleRate, metaHandler, traceFile);
	    chart = delayjitter.getChart();
	} else if (chartType.equalsIgnoreCase("NodeSpecific")) {
	    DelayJitterChart delayjitter = new DelayJitterChart(startingNode, level, sampleRate, metaHandler, traceFile);
	    chart = delayjitter.getChart();
	}
    }

    /**
     * This method creates the chart Packet End to End Delay. If the chart is
     * regarding 2 nodes or one specific node it calls the appropriate
     * constructor of the PacketEndToEndDelayChart class who will create the
     * actual chart.
     *
     * @param chartType indicates if the chart is between 2 nodes or for a
     * specific single node.
     */
    private void createPacketEndToEndDelay(String chartType) {
	if (chartType.equalsIgnoreCase("NodetoNode")) {
	    PacketEndToEndDelayChart packetdelay = new PacketEndToEndDelayChart(startingNode, endingNode, level, sampleRate, metaHandler, traceFile);
	    chart = packetdelay.getChart();
	} else if (chartType.equalsIgnoreCase("NodeSpecific")) {
	    PacketEndToEndDelayChart packetdelay = new PacketEndToEndDelayChart(startingNode, level, sampleRate, metaHandler, traceFile);
	    chart = packetdelay.getChart();
	}
    }

    /**
     * This method creates the chart Throughput(bits/sec). If the chart is
     * regarding 2 nodes or one specific node it calls the appropriate
     * constructor of the ThroughputBitsChart class who will create the actual
     * chart.
     *
     * @param chartType indicates if the chart is between 2 nodes or for a
     * specific single node.
     */
    private void createThroughputBits(String chartType) {
	if (chartType.equalsIgnoreCase("NodetoNode")) {
	    ThroughputBitsChart throughputbits = new ThroughputBitsChart(startingNode, endingNode, level, sampleRate, metaHandler, traceFile);
	    chart = throughputbits.getChart();
	} else if (chartType.equalsIgnoreCase("NodeSpecific")) {
	    ThroughputBitsChart throughputbits = new ThroughputBitsChart(startingNode, level, sampleRate, metaHandler, traceFile);
	    chart = throughputbits.getChart();
	}
    }
}
