package metrics;

import metrics.GeneralInformation;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.MetaDataHandler;
import src.TraceFileInfo;

/**
 * This class is used to produce the statistical information regarding nodes of
 * the simulation. It is used to provide all the nodes of the simulation
 * scenario in a form of a list and provide methods to produce the statistics
 * for any given node of the simulation.
 *
 * @author charalampi
 */
public class GeneralNodeInformation extends GeneralInformation {

    private int NodeId;
    private ArrayList<Integer> NodesIdentifiers = new ArrayList();

    public ArrayList<Integer> getNodesIdentifiers() {
	return NodesIdentifiers;
    }

    /**
     * Constructor of NodeInfo class.
     *
     * @param metaHandler The meta data handler that is related to the current
     * trace file.
     * @param statement The statement related to the database connection.
     * @param TraceFile The trace file class instance related to the current
     * trace file.
     * @return boolean Value that denotes successful NodeInfo object creation.
     */
    public GeneralNodeInformation(MetaDataHandler metaHandler, Statement statement, TraceFileInfo TraceFile) {
	this.metaHandler = metaHandler;
	this.st = statement;
	this.traceFile = TraceFile;
	retrieveNodesIdentifiers(NodesIdentifiers);

    }

    @Override
    public void retrieveMetrics() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean storeMetrics() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * This method is used to calculate the metrics regarding a simulation node
     * who is specified by its ID.
     *
     * @param NodeId The ID of the node for which to calculate the metrics.
     * @return boolean True if the metrics where produced correctly, False
     * otherwise
     */
    public boolean produceMetrics(int NodeId) {
	this.NodeId = NodeId;
	return produceMetrics();

    }

    /**
     * This method finds all the nodes of the simulation and inserts them in a
     * list.
     *
     * @param Nodes List to which all the nodes of the simulation will be added.
     */
    private void retrieveNodesIdentifiers(ArrayList<Integer> Nodes) {
	try {
	    if (metaHandler.getNode().indexOf("SourceNode") != -1) {

		st.execute("select distinct node from ( (select " + metaHandler.getNode().get(0) + " as node from " + traceFile.getTraceFileName() + ") union (select " + metaHandler.getNode().get(1) + " as node from " + traceFile.getTraceFileName() + ")) as temp;");
		rs = st.getResultSet();
		while (rs.next()) {
		    Nodes.add(rs.getInt(1));
		}
	    } else {

		st.execute("select distinct " + metaHandler.getNode().get(0) + " from " + traceFile.getTraceFileName());
		rs = st.getResultSet();
		while (rs.next()) {
		    Nodes.add(rs.getInt(1));
		}

	    }
	} catch (SQLException ex) {
	    Nodes = null;
	    Logger.getLogger(GeneralNodeInformation.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Override
    public boolean produceMetrics() {


	if (produceGeneratedInformation()
		&& produceReceivedInformation()
		&& produceDroppedInformation()
		&& produceSentInformation()
		&& produceForwardedInformation()
		&& producePacketInformation()) {
	    return true;
	} else {
	    return false;
	}


    }

    /**
     * This method is used to produce the number of sent packets and sent bytes
     * of a specified node.
     *
     * @return boolean True if no error occurred and False otherwise.
     */
    private boolean produceSentInformation() {
	try {
	    numberOfSentPackets = 0;
	    numberOfSentBytes = 0;
	    if (metaHandler.getSentPackets().indexOf("SourceNode") != -1) {
		st.execute("select distinct " + metaHandler.getSentPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"-\" and " + metaHandler.getSentPackets().get(0) + "=" + NodeId + " and " + metaHandler.getSentPackets().get(0) + "=" + metaHandler.getSentPackets().get(1) + ";");
		rs = st.getResultSet();
		while (rs.next()) {
		    numberOfSentPackets++;
		    numberOfSentBytes += rs.getInt(2);
		}


	    } else {
		numberOfSentPackets = numberOfGeneratedPackets - numberOfDroppedPackets;
		numberOfSentBytes = roundDecimals(numberOfGeneratedBytes - numberOfDroppedBytes);

	    }
	    return true;
	} catch (SQLException e) {
	    return false;
	}
    }

    /**
     * This method produces all the metrics related to generated packets.
     * regarding a specific node.
     *
     * @return boolean True if no error occurred and False otherwise.
     */
    private boolean produceGeneratedInformation() {
	numberOfGeneratedPackets = 0;
	numberOfGeneratedBytes = 0;
	try {
	    if ((metaHandler.getGeneratePackets().indexOf("EventIdentifier") == -1) && (metaHandler.getGeneratePackets().indexOf("UniqueID") == -1)) {
		st.execute("select distinct " + metaHandler.getGeneratePackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"+\" and " + metaHandler.getGeneratePackets().get(0) + "=" + NodeId + " and " + metaHandler.getGeneratePackets().get(1) + "=" + NodeId + ";");
		rs = st.getResultSet();
		while (rs.next()) {
		    numberOfGeneratedPackets++;
		    numberOfGeneratedBytes += rs.getInt(2);

		}

	    } else {
		produceGeneratedPacketsRTRinfo();
		produceGeneratedPacketsAGTinfo();
		produceGeneratedPacketsMACinfo();
		setGeneralGeneratedInformation();
	    }
	    return true;
	} catch (SQLException e) {
	    Logger.getLogger(GeneralNodeInformation.class.getName()).severe("Error in creating Generated Packet Node information");
	    //System.out.println("[" + NOW() + "] Error in creating Generated Packet Node information ");
	    e.printStackTrace();
	    return false;

	}
    }

    /**
     * This method produces all the metrics related to received packets
     * regarding a specific node.
     *
     * @return boolean True if no error occurred and False otherwise.
     */
    private boolean produceReceivedInformation() {
	try {
	    numberOfReceivedPackets = 0;
	    numberOfReceivedBytes = 0;
	    if (metaHandler.getReceivedPackets().indexOf("DestinationNode") != -1) {
		st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"-\" and " + metaHandler.getReceivedPackets().get(0) + "=" + NodeId + " and " + metaHandler.getReceivedPackets().get(1) + "=" + NodeId + ";");
		rs = st.getResultSet();
		while (rs.next()) {
		    numberOfReceivedPackets++;
		    numberOfReceivedBytes += rs.getInt(2);

		}


	    } else {
		produceReceivedPacketsRTRinfo();
		produceReceivedPacketsAGTinfo();
		produceReceivedPacketsMACinfo();
		setGeneralReceivedInformation();
	    }
	    return true;
	} catch (SQLException e) {
	    Logger.getLogger(GeneralNodeInformation.class.getName()).severe("Error in creating Received Packet Node information");
	    //System.out.println("[" + NOW() + "] Error in creating Received Packet Node information ");
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * This method produces all the metrics related to dropped packets regarding
     * a specific node.
     *
     * @return boolean True if no error occurred and False otherwise.
     */
    private boolean produceDroppedInformation() {
	try {
	    numberOfDroppedPackets = 0;
	    numberOfDroppedBytes = 0;
	    st.execute("select " + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"d\" and " + metaHandler.getGeneratePackets().get(1) + "=" + NodeId + ";");
	    rs = st.getResultSet();
	    while (rs.next()) {
		numberOfDroppedPackets++;
		numberOfDroppedBytes += rs.getInt(1);
	    }

	    return true;
	} catch (SQLException e) {
	    Logger.getLogger(GeneralNodeInformation.class.getName()).severe(" Error in creating Dropped Packet Node information");
	    //System.out.println("[" + NOW() + "] Error in creating Dropped Packet Node information ");
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * This method produces all the metrics related to forwarded packets
     * regarding a specific node.
     *
     * @return boolean True if no error occurred and False otherwise.
     */
    private boolean produceForwardedInformation() {
	try {
	    int packetNumber = 0;
	    int packetSize = 0;
	    if (metaHandler.getForwardedPackets().indexOf("SourceNode") != -1) {
		st.execute("select distinct " + metaHandler.getForwardedPackets().get(3) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"-\" and " + metaHandler.getForwardedPackets().get(0) + "=" + NodeId + " and " + metaHandler.getForwardedPackets().get(1) + "!=" + NodeId + " and " + metaHandler.getForwardedPackets().get(0) + "!=" + metaHandler.getForwardedPackets().get(2) + ";");
	    } else {
		if (st.execute("select distinct " + metaHandler.getForwardedPackets().get(0) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"f\" and " + metaHandler.getForwardedPackets().get(2) + "=" + NodeId)) {
		} else {
		    st.execute("select distinct " + metaHandler.getForwardedPackets().get(0) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"s\" and " + metaHandler.getForwardedPackets().get(1) + "!=" + metaHandler.getForwardedPackets().get(2) + " and " + metaHandler.getForwardedPackets().get(2) + "=" + NodeId);
		}
	    }
	    rs = st.getResultSet();
	    while (rs.next()) {
		packetNumber++;
		packetSize += rs.getInt(2);
	    }
	    numberOfForwardedPackets = packetNumber;
	    numberOfForwardedBytes = roundDecimals(packetSize);

	    return true;
	} catch (SQLException e) {
	    Logger.getLogger(GeneralNodeInformation.class.getName()).severe(" Error in creating Forwrded Packet Node information");
	    //System.out.println("[" + NOW() + "] Error in creating Forwrded Packet Node information ");
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * This method produces all the metrics related to generated packets/size in
     * the RTR level of a wireless scenario regarding a specific node.
     *
     * @throws SQLException
     */
    private void produceGeneratedPacketsRTRinfo() throws SQLException {
	generatedPacketsRTR = 0;
	generatedPacketsSizeRTR = 0;
	st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getGeneratePackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='s' and " + metaHandler.getNode().get(1) + "='RTR' and " + metaHandler.getGeneratePackets().get(1) + "=" + NodeId + " and " + metaHandler.getGeneratePackets().get(2) + "=" + NodeId + ";");
	rs = st.getResultSet();
	while (rs.next()) {
	    generatedPacketsRTR++;
	    generatedPacketsSizeRTR += rs.getInt(1);
	}
    }

    /**
     * This method produces all the metrics related to generated packets/size in
     * the AGT level of a wireless scenario regarding a specific node.
     *
     * @throws SQLException
     */
    private void produceGeneratedPacketsAGTinfo() throws SQLException {
	generatedPacketsAGT = 0;
	generatedPacketsSizeAGT = 0;
	st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getGeneratePackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='s' and " + metaHandler.getNode().get(1) + "='AGT' and " + metaHandler.getGeneratePackets().get(1) + "=" + NodeId + " and " + metaHandler.getGeneratePackets().get(2) + "=" + NodeId + ";");
	rs = st.getResultSet();
	while (rs.next()) {
	    generatedPacketsAGT++;
	    generatedPacketsSizeAGT += rs.getInt(1);
	}
    }

    /**
     * This method produces all the metrics related to generated packets/size in
     * the MAC level of a wireless scenario regarding a specific node.
     *
     * @throws SQLException
     */
    private void produceGeneratedPacketsMACinfo() throws SQLException {
	generatedPacketsMAC = 0;
	generatedPacketsSizeMAC = 0;
	st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getGeneratePackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='s' and " + metaHandler.getNode().get(1) + "='MAC' and " + metaHandler.getGeneratePackets().get(1) + "=" + NodeId + " and " + metaHandler.getGeneratePackets().get(2) + "=" + NodeId + ";");
	rs = st.getResultSet();
	while (rs.next()) {
	    generatedPacketsMAC++;
	    generatedPacketsSizeMAC += rs.getInt(1);
	}

    }

    /**
     * This method sets the value for the generated packets/bytes of the
     * simulation when the scenario is wireless. In this case the value of the
     * generated packets/bytes is the maximum of the the metrics for AGT/RTR/MAC
     * generated information.
     */
    private void setGeneralGeneratedInformation() {
	numberOfGeneratedPackets = Math.max(generatedPacketsRTR, Math.max(generatedPacketsAGT, generatedPacketsMAC));
	numberOfGeneratedBytes = Math.max(generatedPacketsSizeRTR, Math.max(generatedPacketsSizeAGT, generatedPacketsSizeMAC));
	/*if (generatedPacketsRTR > 0) {
	 numberOfGeneratedPackets = generatedPacketsRTR;
	 numberOfGeneratedBytes = roundDecimals(generatedPacketsSizeRTR);
	 } else if (generatedPacketsMAC > 0) {
	 numberOfGeneratedPackets = generatedPacketsMAC;
	 numberOfGeneratedBytes = roundDecimals(generatedPacketsSizeMAC);
	 } else {
	 numberOfGeneratedPackets = generatedPacketsAGT;
	 numberOfGeneratedBytes = roundDecimals(generatedPacketsSizeAGT);
	 }*/
    }

    /**
     * This method produces all the metrics related to received packets/size in
     * the RTR level of a wireless scenario regarding a specific node.
     *
     * @throws SQLException
     */
    private void produceReceivedPacketsRTRinfo() throws SQLException {
	receivedPacketsRTR = 0;
	receivedPacketsSizeRTR = 0;
	st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getReceivedPackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getNode().get(1) + "='RTR' and " + metaHandler.getReceivedPackets().get(1) + "=" + NodeId + " and " + metaHandler.getReceivedPackets().get(2) + "=" + NodeId + ";");
	rs = st.getResultSet();
	while (rs.next()) {
	    receivedPacketsRTR++;
	    receivedPacketsSizeRTR += rs.getInt(1);
	}
    }

    /**
     * This method produces all the metrics related to received packets/size in
     * the AGT level of a wireless scenario regarding a specific node.
     *
     * @throws SQLException
     */
    private void produceReceivedPacketsAGTinfo() throws SQLException {
	receivedPacketsAGT = 0;
	receivedPacketsSizeAGT = 0;
	st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getReceivedPackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getNode().get(1) + "='AGT' and " + metaHandler.getReceivedPackets().get(1) + "=" + NodeId + " and " + metaHandler.getReceivedPackets().get(2) + "=" + NodeId + ";");
	rs = st.getResultSet();
	while (rs.next()) {
	    receivedPacketsAGT++;
	    receivedPacketsSizeAGT += rs.getInt(1);
	}
    }

    /**
     * This method produces all the metrics related to received packets/size in
     * the MAC level of a wireless scenario regarding a specific node.
     *
     * @throws SQLException
     */
    private void produceReceivedPacketsMACinfo() throws SQLException {
	receivedPacketsMAC = 0;
	receivedPacketsSizeMAC = 0;
	st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getReceivedPackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getNode().get(1) + "='MAC' and " + metaHandler.getReceivedPackets().get(1) + "=" + NodeId + " and " + metaHandler.getReceivedPackets().get(2) + "=" + NodeId + ";");
	rs = st.getResultSet();
	while (rs.next()) {
	    receivedPacketsMAC++;
	    receivedPacketsSizeMAC += rs.getInt(1);
	}
    }

    /**
     * This method sets the value for the received packets/bytes of the
     * simulation when the scenario is wireless. In this case the value of the
     * received packets/bytes is the maximum of the the metrics for AGT/RTR/MAC
     * received information.
     */
    private void setGeneralReceivedInformation() {
	if (receivedPacketsRTR > 0) {
	    numberOfReceivedPackets = receivedPacketsRTR;
	    numberOfReceivedBytes = roundDecimals(receivedPacketsSizeRTR);
	} else if (receivedPacketsMAC > 0) {
	    numberOfReceivedPackets = receivedPacketsMAC;
	    numberOfReceivedBytes = roundDecimals(receivedPacketsSizeMAC);
	} else {
	    numberOfReceivedPackets = receivedPacketsAGT;
	    numberOfReceivedBytes = roundDecimals(receivedPacketsSizeAGT);
	}
    }

    /**
     * This method produces all the metrics related to packet sizes regarding a
     * specific node.
     *
     * @return boolean True if no error occurred and False otherwise.
     */
    private boolean producePacketInformation() {
	try {
	    st.execute("select MAX(" + metaHandler.getPacketSize() + "),MIN(" + metaHandler.getPacketSize() + ") from " + traceFile.getTraceFileName() + " where " + metaHandler.getNode().get(0) + "=" + NodeId + ";");
	    rs = st.getResultSet();
	    rs.next();
	    maximumPacketSize = rs.getInt(1);
	    minimumPacketSize = rs.getInt(2);
	    if (numberOfSentPackets == 0) {
		averagePacketSize = 0;
	    } else {
		averagePacketSize = numberOfSentBytes / numberOfSentPackets;
	    }
	    return true;
	} catch (SQLException e) {
	    Logger.getLogger(GeneralNodeInformation.class.getName()).severe(" Error in creating Packet Realated Node information");
	    //System.out.println("[" + NOW() + "] Error in creating Packet Realated Node information ");
	    e.printStackTrace();
	    return false;
	}
    }
}
