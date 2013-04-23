package metrics;

import metrics.GeneralInformation;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import src.MetaDataHandler;
import src.TraceFileInfo;

/**
 *
 * @author charalampi
 */
public class GeneralSimulationInformation extends GeneralInformation {

    private double startTime, endTime, simulationTime;
    private int numberOfNodes, numberOfSendingNodes;

    public GeneralSimulationInformation(MetaDataHandler metaHandler, Statement statement, TraceFileInfo TraceFile) {
        this.metaHandler = metaHandler;
        this.st = statement;
        this.traceFile = TraceFile;
    }

    @Override
    public void retrieveMetrics() {
        try {
            if (st.execute("select * from standard_metrics where name=\"" + traceFile.getTraceFileName() + "\"")) {
                rs = st.getResultSet();
                rs.next();
                startTime = rs.getDouble(2);
                endTime = rs.getDouble(3);
                simulationTime = rs.getDouble(4);
                numberOfNodes = rs.getInt(5);
                numberOfSendingNodes = rs.getInt(6);
                numberOfSentPackets = rs.getInt(7);
                numberOfSentBytes = rs.getDouble(8);
                numberOfForwardedPackets = rs.getInt(9);
                numberOfForwardedBytes = rs.getDouble(10);
                numberOfDroppedPackets = rs.getInt(11);
                numberOfDroppedBytes = rs.getDouble(12);
                numberOfReceivedPackets = rs.getInt(13);
                numberOfGeneratedPackets = rs.getInt(14);
                minimumPacketSize = rs.getInt(15);
                maximumPacketSize = rs.getDouble(16);
                averagePacketSize = rs.getDouble(17);
                numberOfGeneratedBytes = rs.getDouble(18);
                numberOfReceivedBytes = rs.getDouble(19);
                generatedPacketsAGT = rs.getInt(20);
                generatedPacketsRTR = rs.getInt(21);
                generatedPacketsMAC = rs.getInt(22);
                generatedPacketsSizeAGT = rs.getDouble(23);
                generatedPacketsSizeRTR = rs.getDouble(24);
                generatedPacketsSizeMAC = rs.getDouble(25);
                receivedPacketsAGT = rs.getInt(26);
                receivedPacketsRTR = rs.getInt(27);
                receivedPacketsMAC = rs.getInt(28);
                receivedPacketsSizeAGT = rs.getDouble(29);
                receivedPacketsSizeRTR = rs.getDouble(30);
                receivedPacketsSizeMAC = rs.getDouble(31);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GeneralSimulationInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean storeMetrics() {
        produceMetrics();
        try {
            if (((Object) numberOfNodes) == null) {
                JOptionPane.showMessageDialog(null, "Null!!!!!!!!!");
                st.execute("insert into standard_metrics(name) values('" + traceFile.getTraceFileName() + "');");
            } else {
                st.execute("insert into standard_metrics values('" + traceFile.getTraceFileName() + "'," + getStartTime() + "," + getEndTime() + "," + getSimulationTime() + "," + getNumberOfNodes() + ""
                        + "," + getNumberOfSendingNodes() + "," + getNumberOfSentPackets() + "," + getNumberOfSentBytes() + "," + getNumberOfForwardedPackets() + "," + getNumberOfForwardedBytes() + "," + getNumberOfDroppedPackets() + ""
                        + "," + getNumberOfDroppedBytes() + "," + getNumberOfReceivedPackets() + "," + getNumberOfGeneratedPackets() + "," + getMinimumPacketSize() + "," + getMaximumPacketSize() + "," + getAveragePacketSize() + ""
                        + "," + numberOfGeneratedBytes + "," + numberOfReceivedBytes + "," + generatedPacketsAGT + "," + generatedPacketsRTR + "," + generatedPacketsMAC + "," + generatedPacketsSizeAGT + ""
                        + "," + generatedPacketsSizeRTR + "," + generatedPacketsSizeMAC + "," + receivedPacketsAGT + "," + receivedPacketsRTR + "," + receivedPacketsMAC + "," + receivedPacketsSizeAGT + ""
                        + "," + receivedPacketsSizeRTR + "," + receivedPacketsSizeMAC + ");");
            }



            return true;
        } catch (SQLException ex) {
            Logger.getLogger(GeneralSimulationInformation.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getSQLState());
            ex.printStackTrace();

            return false;
        }
    }

    /**
     * This function is used to produce all the general metrics regarding a
     * trace file. The metrics produced are: Start Time, End Time, Simulation
     * Time, Number Of Sent Bytes, Number Of Forwarded Bytes, Number Of Dropped
     * Bytes, Minimum Packet Size, Maximum Packet Size, Average Packet Size,
     * Number Of Received Bytes, Number Of Nodes, Number Of Sending Nodes,
     * Number Of Sent Packets, Number Of Forwarded Packets, Number Of Dropped
     * Packets, Number Of Received Packets, Number Of Generated Packets,
     * Generated Packets RTR, Generated AGT Packets, Generated MAC Packets,
     * Received Packets RTR, Received Packets AGT, Received Packets MAC,
     * Generated Packets Size RTR, Generated Packets Size AGT, Generated Packets
     * Size MAC, Received Packets Size RTR, Received Packets Size AGT, Received
     * Packets Size MAC
     *
     * @return true for successful metrics extraction or false if an error has
     * occurred.
     */
    @Override
    public boolean produceMetrics() {
        int packetNumber = 0;
        int packetSize = 0;
        int tempMax;
        try {
            /**
             * Calculating the starting time of the simulation.
             */
            startTime = calculateStartTime();
            if (startTime == -1) {
                return false;
            } else {
                Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced staring time.");
                //System.out.println("[" + NOW() + "] Produced staring time.");
            }

            /**
             * Calculating the end time of the simulation.
             */
            endTime = calculateEndTime();
            if (endTime == -1) {
                return false;
            } else {
                Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced end time.");
                //System.out.println("[" + NOW() + "] Produced end time.");
            }


            /*
             * Calculating the overall time of the simulation.
             */
            simulationTime = endTime - startTime;
            Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced overall simulation time.");
            //System.out.println("[" + NOW() + "] Produced overall simulation time.");

            /*
             * Calculating the NUMBER OF NODES in the simulation.
             */

            numberOfNodes = calculateNumberOfNodes();
            if (numberOfNodes == -1) {
                return false;
            } else {
                Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced number of nodes in simulation.");
                //System.out.println("[" + NOW() + "] Produced number of nodes in simulation.");
            }


            /*
             * Calculating the number of SENDING NODES in the simulation.
             */

            numberOfSendingNodes = calculateNumberOfSendingNodes();
            if (numberOfSendingNodes == -1) {
                return false;
            } else {
                Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced number of sending nodes in simulation.");
                //System.out.println("[" + NOW() + "] Produced number of sending nodes in simulation.");
            }


            /*
             * Calculating the number of GENERATED PACKETS.
             */
            generatedPacketsRTR = 0;
            generatedPacketsAGT = 0;
            generatedPacketsMAC = 0;
            packetNumber = packetSize = 0;
            if ( (metaHandler.getGeneratePackets().indexOf("EventIdentifier") == -1) && (metaHandler.getGeneratePackets().indexOf("UniqueID") == -1)  ){
                st.execute("select distinct " + metaHandler.getGeneratePackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"+\" and " + metaHandler.getGeneratePackets().get(0) + "=" + metaHandler.getGeneratePackets().get(1) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    packetNumber++;
                    packetSize += rs.getInt(2);
                }
                numberOfGeneratedPackets = packetNumber;
                numberOfGeneratedBytes = packetSize;
            } else {


                //st.execute("select distinct "+metaHandler.getGeneratePackets().get(0)+","+metaHandler.getPacketSize()+" from "+traceFile.getTraceFileName()+" where event=\"s\" and "+metaHandler.getGeneratePackets().get(1)+"="+metaHandler.getGeneratePackets().get(2)+";");
                //st.execute("select temp2.devent,MAX("+metaHandler.getPacketSize()+") from "+traceFile.getTraceFileName()+" as temp1 inner join (select distinct "+metaHandler.getGeneratePackets().get(0)+" as devent from "+traceFile.getTraceFileName()+" where event=\"s\" and "+metaHandler.getGeneratePackets().get(1)+"="+metaHandler.getGeneratePackets().get(2)+") as temp2 on temp2.devent=temp1."+metaHandler.getGeneratePackets().get(0)+" GROUP BY devent;");
                st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getGeneratePackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='s' and "+metaHandler.getNode().get(1) +"='RTR' and " + metaHandler.getGeneratePackets().get(1) + "=" + metaHandler.getGeneratePackets().get(2) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    generatedPacketsRTR++;
                    generatedPacketsSizeRTR += rs.getInt(1);
                }
                st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getGeneratePackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='s' and "+metaHandler.getNode().get(1) +"='AGT' and " + metaHandler.getGeneratePackets().get(1) + "=" + metaHandler.getGeneratePackets().get(2) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    generatedPacketsAGT++;
                    generatedPacketsSizeAGT += rs.getInt(1);
                }
                st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getGeneratePackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='s' and "+metaHandler.getNode().get(1) +"='MAC' and " + metaHandler.getGeneratePackets().get(1) + "=" + metaHandler.getGeneratePackets().get(2) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    generatedPacketsMAC++;
                    generatedPacketsSizeMAC += rs.getInt(1);
                }

                numberOfGeneratedPackets = Math.max(generatedPacketsRTR, Math.max(generatedPacketsAGT, generatedPacketsMAC));
                numberOfGeneratedBytes = Math.max(generatedPacketsSizeRTR, Math.max(generatedPacketsSizeAGT, generatedPacketsSizeMAC));
                /*
                 * if (generatedPacketsRTR > 0) { numberOfGeneratedPackets =
                 * generatedPacketsRTR; numberOfGeneratedBytes =
                 * generatedPacketsSizeRTR; } else if (generatedPacketsMAC > 0)
                 * { numberOfGeneratedPackets = generatedPacketsMAC;
                 * numberOfGeneratedBytes = generatedPacketsSizeMAC; } else {
                 * numberOfGeneratedPackets = generatedPacketsAGT;
                 * numberOfGeneratedBytes = generatedPacketsSizeAGT;
                }
                 */
            }
            Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced number of  Generated Packets Information.");
            //System.out.println("[" + NOW() + "] Produced number of  Generated Packets Information.");
            /*
             * Calculating the number of DROPPED PACKETS.
             */

            packetNumber = packetSize = 0;
            st.execute("select " + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"d\";");
            rs = st.getResultSet();
            while (rs.next()) {
                packetNumber++;
                packetSize += rs.getInt(1);
            }
            numberOfDroppedPackets = packetNumber;
            numberOfDroppedBytes = packetSize;
            Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced number of  Dropped Packets Information.");
            //System.out.println("[" + NOW() + "] Produced number of  Dropped Packets Information.");

            /*
             * Calculating the number of DROPPED PACKETS in the same node in
             * order to calculate the actual sent packets;
             */
            int droppedPacketsBeforeSent = 0;
            int droppedPacketsSizeBeforeSent = 0;
            st.execute("select " + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"d\" and " + metaHandler.getGeneratePackets().get(1) + "=" + metaHandler.getGeneratePackets().get(2) + ";");
            rs = st.getResultSet();
            while (rs.next()) {
                droppedPacketsBeforeSent++;
                droppedPacketsSizeBeforeSent += rs.getInt(1);
            }


            /*
             * Calculating the number of SENT PACKETS.
             */
            packetNumber = packetSize = 0;
            if (metaHandler.getSentPackets().indexOf("SourceNode") != -1) {
                st.execute("select distinct " + metaHandler.getSentPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"-\" and " + metaHandler.getSentPackets().get(0) + "=" + metaHandler.getSentPackets().get(1) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    packetNumber++;
                    packetSize += rs.getInt(2);
                }
                numberOfSentPackets = packetNumber;
                numberOfSentBytes = packetSize;
            } else {
                //tempMax = Math.max(generatedPacketsRTR,Math.max(generatedPacketsAGT,generatedPacketsMAC));
                numberOfSentPackets = numberOfGeneratedPackets - droppedPacketsBeforeSent;
                numberOfSentBytes = numberOfGeneratedBytes - droppedPacketsSizeBeforeSent;
            }
            Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced number of  Sent Packets Information.");
            //System.out.println("[" + NOW() + "] Produced number of  Sent Packets Information.");
            /*
             * Calculating the number of RECEIVED PACKETS.
             */
            packetNumber = packetSize = 0;
            if (metaHandler.getReceivedPackets().indexOf("DestinationNode") != -1) {

                st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"-\" and " + metaHandler.getReceivedPackets().get(0) + "=" + metaHandler.getReceivedPackets().get(1) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    packetNumber++;
                    packetSize += rs.getInt(2);
                }
                numberOfReceivedPackets = packetNumber;
                numberOfReceivedBytes = packetSize;

            } else {

                //st.execute("select distinct "+metaHandler.getGeneratePackets().get(0)+","+metaHandler.getPacketSize()+" from "+traceFile.getTraceFileName()+" where event=\"r\" and "+metaHandler.getGeneratePackets().get(1)+"="+metaHandler.getGeneratePackets().get(2)+";");
                st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getReceivedPackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='r' and "+metaHandler.getNode().get(1) +"='RTR' and " + metaHandler.getReceivedPackets().get(1) + "=" + metaHandler.getReceivedPackets().get(2) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    receivedPacketsRTR++;
                    receivedPacketsSizeRTR += rs.getInt(1);
                }
                st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getReceivedPackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='r' and "+metaHandler.getNode().get(1) +"='AGT' and " + metaHandler.getReceivedPackets().get(1) + "=" + metaHandler.getReceivedPackets().get(2) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    receivedPacketsAGT++;
                    receivedPacketsSizeAGT += rs.getInt(1);
                }
                st.execute("select distinct " + metaHandler.getPacketSize() + "," + metaHandler.getReceivedPackets().get(0) + " from " + traceFile.getTraceFileName() + " where event='r' and "+metaHandler.getNode().get(1) +"='MAC' and " + metaHandler.getReceivedPackets().get(1) + "=" + metaHandler.getReceivedPackets().get(2) + ";");
                rs = st.getResultSet();
                while (rs.next()) {
                    receivedPacketsMAC++;
                    receivedPacketsSizeMAC += rs.getInt(1);
                }

                if (receivedPacketsRTR > 0) {
                    numberOfReceivedPackets = receivedPacketsRTR;
                    numberOfReceivedBytes = receivedPacketsSizeRTR;
                } else if (receivedPacketsMAC > 0) {
                    numberOfReceivedPackets = receivedPacketsMAC;
                    numberOfReceivedBytes = receivedPacketsSizeMAC;
                } else {
                    numberOfReceivedPackets = receivedPacketsAGT;
                    numberOfReceivedBytes = receivedPacketsSizeAGT;
                }
            }
            Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced number of  Received Packets Information.");
            //System.out.println("[" + NOW() + "] Produced number of  Received Packets Information.");


            /*
             * Calculating the number of FORWARDED PACKETS.
             */
            packetNumber = packetSize = 0;
            if (metaHandler.getForwardedPackets().indexOf("SourceNode") != -1) {
                st.execute("select distinct " + metaHandler.getForwardedPackets().get(3) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"-\" and " + metaHandler.getForwardedPackets().get(0) + "!=" + metaHandler.getForwardedPackets().get(1) + " and " + metaHandler.getForwardedPackets().get(0) + "!=" + metaHandler.getForwardedPackets().get(2) + ";");
            } else {

                if (st.execute("select distinct " + metaHandler.getForwardedPackets().get(0) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"f\" ;")) {
                } else {
                    st.execute("select distinct " + metaHandler.getForwardedPackets().get(0) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " where event=\"s\" and " + metaHandler.getForwardedPackets().get(1) + "!=" + metaHandler.getForwardedPackets().get(2) + ";");
                }
            }
            rs = st.getResultSet();
            while (rs.next()) {
                packetNumber++;
                packetSize += rs.getInt(2);
            }
            numberOfForwardedPackets = packetNumber;
            numberOfForwardedBytes = packetSize;
            Logger.getLogger(GeneralSimulationInformation.class.getName()).info("Produced number of  Forwarded Packets Information.");
            //System.out.println("[" + NOW() + "] Produced number of  Forwarded Packets Information.");


            /*
             * Calculating the minimum, mximum and average packet size.
             */

            //JOptionPane.showMessageDialog(null,"Packet info:"+"select MAX("+metaHandler.getPacketSize()+"),MIN("+metaHandler.getPacketSize()+") from "+traceFile.getTraceFileName());
            st.execute("select MAX(" + metaHandler.getPacketSize() + "),MIN(" + metaHandler.getPacketSize() + ") from " + traceFile.getTraceFileName());
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
        } catch (SQLException ex) {
            Logger.getLogger(GeneralSimulationInformation.class.getName()).log(Level.SEVERE, null, ex);
            return false;

        }
    }

    public double getEndTime() {
        return endTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getNumberOfSendingNodes() {
        return numberOfSendingNodes;
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    private double calculateStartTime() {
        try {
            st.execute("select MIN(" + metaHandler.getTimeRelated() + ") from " + traceFile.getTraceFileName() + ";");
            rs = st.getResultSet();
            rs.next();
            return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("[" + NOW() + "] Error producing staring time.");
            e.printStackTrace();
            return -1;

        }
    }

    private double calculateEndTime() {
        try {
            st.execute("select MAX(" + metaHandler.getTimeRelated() + ") from " + traceFile.getTraceFileName() + ";");
            rs = st.getResultSet();
            rs.next();
            return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("[" + NOW() + "] Error producing end time.");
            e.printStackTrace();
            return -1;

        }
    }

    private int calculateNumberOfNodes() {
        try {
            if (metaHandler.getNode().indexOf("SourceNode") != -1) {
                st.execute("select count(distinct node) from ( (select " + metaHandler.getNode().get(0) + " as node from " + traceFile.getTraceFileName() + ") union (select " + metaHandler.getNode().get(1) + " as node from " + traceFile.getTraceFileName() + ")) as temp;");
            } else {
                st.execute("select count(distinct " + metaHandler.getNode().get(0) + ") from " + traceFile.getTraceFileName());
            }
            rs = st.getResultSet();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("[" + NOW() + "] Error producing Number of Nodes.");
            e.printStackTrace();
            return -1;

        }
    }

    private int calculateNumberOfSendingNodes() {
        try {
            if (metaHandler.getSendingNode().indexOf("SourceNode") != -1) {
                st.execute("select count(distinct " + metaHandler.getSendingNode().get(0) + ") from " + traceFile.getTraceFileName() + " where event = \"-\" and " + metaHandler.getSendingNode().get(0) + "=" + metaHandler.getSendingNode().get(1) + ";");

            } else {

                st.execute("select count(distinct " + metaHandler.getNode().get(0) + ") from " + traceFile.getTraceFileName() + " where event=\"s\" and " + metaHandler.getSendingNode().get(1) + "=" + metaHandler.getSendingNode().get(2) + ";");

            }
            rs = st.getResultSet();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            Logger.getLogger(GeneralSimulationInformation.class.getName()).severe("Error producing Number of Sending Nodes.");
            //System.out.println("[" + NOW() + "] Error producing Number of Sending Nodes.");
            e.printStackTrace();
            return -1;
        }
    }
}
