package metrics;

import UI.TRAFIL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import src.MetaDataHandler;
import src.TraceFileInfo;
import utilities.DatabaseConnection;

/**
 * This class handles the creation of metrics that are requested by the user via
 * the metrics tab of TRAFIL.
 *
 * @author charalampi
 */
public class Metrics {

    private MetaDataHandler metaHandler;
    private double throughput, maxEndToEndDealy, minEndToEndDelay, averageEndToEndDelay, delayJitter;
    private double minDelayJitter, maxDelayJitter, averageDelayJitter, packetLossRatio, throughputBits;
    //private Statement st;
    //private ResultSet rs;
    private TraceFileInfo traceFile;
    private int startingNode;
    private int endingNode;
    private String level;
    private boolean calculatingMetrics = false;
    private ArrayList<String> levels = new ArrayList();
    private boolean throughputCalculation, delaysCalculation, jitterCalculation, packetLossCalculation;

    public ArrayList<String> getLevels() {
        return levels;
    }

    /**
     * The constructor of the class initializes the class variables to the
     * appropriate values.
     *
     * @param meta instance of the meta data handler that was matched to the
     * current trace file.
     * @param TraceFile the trace file info instance for the current trace file.
     */
    public Metrics(MetaDataHandler meta, TraceFileInfo TraceFile) {

        metaHandler = meta;
        Statement st = DatabaseConnection.getSt();
        traceFile = TraceFile;
        String temporaryLevel;
        if (metaHandler.getNode().indexOf("SourceNode") != -1) {
            levels.add("Link Layer");
            levels.add("Network Layer");
        } else {
            try {
                st.execute("select distinct " + metaHandler.getNode().get(1) + " from " + traceFile.getTraceFileName() + ";");
                ResultSet rs = st.getResultSet();
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
     * this method calculates the metrics by calling all the available methods
     * that calculate each metric.
     *
     * @param startingNode the start node selected by the user
     * @param endingNode the end node selected by the user
     * @param Level the trace level to which the information will refer
     * @return boolean, true if the metrics were calculated successfully, false
     * otherwise
     */
    public boolean calculateMetrics(int startingNode, int endingNode, String Level) {
        throughputCalculation = delaysCalculation = jitterCalculation = packetLossCalculation = false;
        long startTime, endTime;
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.level = Level;
        calculatingMetrics = true;
        Thread thread = new Thread(r);
        thread.start();
        Logger.getLogger(Metrics.class.getName()).info(" Trying to create metrics regarding node " + startingNode + " and node " + endingNode);
        //System.out.println("[" + GeneralInformation.NOW() + "] Trying to create metrics regarding node " + startingNode + " and node " + endingNode);
        startTime = System.currentTimeMillis();
        if (startingNode == endingNode) {
            Logger.getLogger(Metrics.class.getName()).info("Error, start node and node are the same.");
            //System.out.println("[" + GeneralInformation.NOW() + "] Error, start node and node are the same.");
            calculatingMetrics = false;
            return false;
//        } else if (calculateThroughput() && calculateDelays() && calculateJitterMetrics() && calculatePacketLossRatio()) {
//            endTime = System.currentTimeMillis();
//            Logger.getLogger(Metrics.class.getName()).info("Succesfully created metrics regarding node " + startingNode + " and node " + endingNode);
//            //System.out.println("[" + GeneralInformation.NOW() + "] Succesfully created metrics regarding node " + startingNode + " and node " + endingNode);
//            Logger.getLogger(Metrics.class.getName()).info("Metric calculation time was:" + (endTime - startTime) + " ms.");
//            //System.out.println("[" + GeneralInformation.NOW() + "] Metric calculation time was:" + (endTime - startTime) + " ms.");
//            calculatingMetrics = false;
//            return true;
//        }
        } else {
            ExecutorService threadExecutor = Executors.newFixedThreadPool(3);
            threadExecutor.execute(calcThroughput);
            threadExecutor.execute(calcDelays);
            threadExecutor.execute(calcJitterMetrics);

            threadExecutor.shutdown();
            while (!threadExecutor.isTerminated()) {
            }
//            Thread threadCalcThroughput = new Thread(calcThroughput);
//            threadCalcThroughput.start();
//            Thread threadCalcDelays = new Thread(calcDelays);
//            threadCalcDelays.start();
//            Thread threadCalcJitterMetrics = new Thread(calcJitterMetrics);
//            threadCalcJitterMetrics.start();
//            
//            while (threadCalcThroughput.isAlive()||threadCalcDelays.isAlive()||threadCalcJitterMetrics.isAlive()) {
//                
//            }
            if (throughputCalculation && delaysCalculation && jitterCalculation && packetLossCalculation) {
                calculatingMetrics = false;
                endTime = System.currentTimeMillis();
                Logger.getLogger(Metrics.class.getName()).info("Succesfully created metrics regarding node " + startingNode + " and node " + endingNode);
                Logger.getLogger(Metrics.class.getName()).info("Metric calculation time was:" + (endTime - startTime) + " ms.");
                return true;
            }

        }
        calculatingMetrics = false;
        return false;


    }

    public double getAverageDelayJitter() {
        return averageDelayJitter;
    }

    public double getAverageEndToEndDelay() {
        return averageEndToEndDelay;
    }

    public double getDelayJitter() {
        return delayJitter;
    }

    public double getMaxDelayJitter() {
        return maxDelayJitter;
    }

    public double getMaxEndToEndDealy() {
        return maxEndToEndDealy;
    }

    public double getMinDelayJitter() {
        return minDelayJitter;
    }

    public double getMinEndToEndDelay() {
        return minEndToEndDelay;
    }

    public double getPacketLossRatio() {
        return packetLossRatio;
    }

    public double getThroughput() {
        return throughput;
    }
    Runnable r = new Runnable() {

        public void run() {
            try {
                int i = 0;
                while (calculatingMetrics) {
                    i++;
                    if ((i % 4) == 3) {
                        TRAFIL.statusBar.setText("<html>Calculating Metrics......<font size=+1>.....</font></html>");
                        TRAFIL.statusBar.paintImmediately(TRAFIL.statusBar.getVisibleRect());
                    } else if ((i % 4) == 2) {
                        TRAFIL.statusBar.setText("Calculating Metrics......");
                        TRAFIL.statusBar.paintImmediately(TRAFIL.statusBar.getVisibleRect());
                    } else if ((i % 4) == 0) {
                        i = 0;

                        TRAFIL.statusBar.setText("<html>Calculating Metrics......<font size=+1>.....</font><font size=+2>......</font></html>");
                        TRAFIL.statusBar.paintImmediately(TRAFIL.statusBar.getVisibleRect());
                    } else if ((i % 4) == 1) {
                        TRAFIL.statusBar.setText("Calculating Metrics");
                        TRAFIL.statusBar.paintImmediately(TRAFIL.statusBar.getVisibleRect());
                    }
                    Thread.sleep(300);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Metrics.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    };
    Runnable calcThroughput = new Runnable() {

        public void run() {
            throughputCalculation = calculateThroughput();
            packetLossCalculation = calculatePacketLossRatio();
        }
    };
    Runnable calcDelays = new Runnable() {

        public void run() {
            delaysCalculation = calculateDelays();
        }
    };
    Runnable calcJitterMetrics = new Runnable() {

        public void run() {
            jitterCalculation = calculateJitterMetrics();
        }
    };

    /**
     * This method calculates the throughput related metrics between the 2
     * selected nodes.
     *
     * @return boolean, true if the metrics were calculated successfully, false
     * otherwise
     */
    private boolean calculateThroughput() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //JOptionPane.showMessageDialog(null, "Url:"+DatabaseConnection.getUrl()+",User:"+DatabaseConnection.getUser()+",Pass:"+);
            Connection connection = DriverManager.getConnection(DatabaseConnection.getUrl(), DatabaseConnection.getUser(), DatabaseConnection.getPassword());
            Statement st = connection.createStatement();
            ResultSet rs;
            long calcStartTime, calcEndTime;
            calcStartTime = System.currentTimeMillis();
            int packetsNumber = 0;
            double endTime = 0.0;
            double startTime = 0.0;
            double packetSizeInBytes = 0.0;
            if (metaHandler.getNode().indexOf("SourceNode") != -1) {

                if (level.equalsIgnoreCase("Link Layer")) {
                    st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(2) + "), temp.min,temp.max from " + traceFile.getTraceFileName() + " as main inner join "
                            + "(select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and "
                            + "" + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ") as temp where main.time<=temp.max and main.time>=temp.min and " + metaHandler.getGeneratePackets().get(0) + ""
                            + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + " and event='r' GROUP BY temp.min;");

                } else {
                    st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(2) + "), temp.min,temp.max from " + traceFile.getTraceFileName() + " as main inner join "
                            + "(select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + " and "
                            + "" + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ") as temp where main.time<=temp.max and main.time>=temp.min and " + metaHandler.getReceivedPackets().get(0) + ""
                            + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and event='r' and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + " GROUP BY temp.min;");

                }


            } else {
                st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(0) + "), temp.min,temp.max from " + traceFile.getTraceFileName() + " as main inner join "
                        + "(select min(time) as min,max(time) as max from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " and "
                        + "" + metaHandler.getReceivedPackets().get(2) + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getNode().get(1) + "='" + level + "') as temp where main.time<=temp.max and main.time>=temp.min and " + metaHandler.getGeneratePackets().get(2) + ""
                        + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and event='r' and " + metaHandler.getReceivedPackets().get(2) + "=" + endingNode + " and " + metaHandler.getNode().get(1) + "='" + level + "' GROUP BY temp.min;");

            }


            rs = st.getResultSet();

            while (rs.next()) {
                packetsNumber = rs.getInt(1);
                startTime = rs.getDouble(2);
                endTime = rs.getDouble(3);
            }
            if (endTime != 0) {
                throughput = (packetsNumber / (endTime - startTime));
            } else {
                throughput = 0;
                throughputBits = 0;
                return true;
            }

            if (metaHandler.getNode().indexOf("SourceNode") != -1) {

                if (level.equalsIgnoreCase("Link Layer")) {
                    st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main  "
                            + " where main.time<=" + endTime + " and main.time>=" + startTime + " and " + metaHandler.getGeneratePackets().get(0) + ""
                            + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + " and event='r';");

                } else {
                    st.execute("select distinct " + metaHandler.getReceivedPackets().get(2) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main "
                            + " where main.time<=" + endTime + " and main.time>=" + startTime + " and " + metaHandler.getReceivedPackets().get(0) + ""
                            + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and event='r' and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ";");

                }


            } else {
                st.execute("select distinct " + metaHandler.getReceivedPackets().get(0) + "," + metaHandler.getPacketSize() + " from " + traceFile.getTraceFileName() + " as main  "
                        + " where main.time<=" + endTime + " and main.time>=" + startTime + " and " + metaHandler.getGeneratePackets().get(2) + ""
                        + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and event='r' and " + metaHandler.getReceivedPackets().get(2) + "=" + endingNode + " and " + metaHandler.getNode().get(1) + "='" + level + "';");

            }

            rs = st.getResultSet();

            while (rs.next()) {
                packetSizeInBytes += rs.getDouble(2);
            }

            throughputBits = ((packetSizeInBytes * 8) / (endTime - startTime));
            calcEndTime = System.currentTimeMillis();
            Logger.getLogger(Metrics.class.getName()).info("THROUGHPUT Metric calculation time was:" + (calcEndTime - calcStartTime) + " ms.");
            connection.close();
            return true;
        } catch (SQLException ex) {
            ex.getSQLState();
            ex.printStackTrace();
            return false;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    public double getThroughputBits() {
        return throughputBits;
    }

    /**
     * This method calculates the Delay related metrics between the 2 selected
     * nodes.
     *
     * @return boolean, true if the metrics were calculated successfully, false
     * otherwise
     */
    private boolean calculateDelays() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DatabaseConnection.getUrl(), DatabaseConnection.getUser(), DatabaseConnection.getPassword());
            Statement st = connection.createStatement();
            ResultSet rs;
            long startTime, endTime;
            startTime = System.currentTimeMillis();
            if (metaHandler.getNode().indexOf("SourceNode") != -1) {
                if (level.equalsIgnoreCase("Link Layer")) {
                    System.out.println("Query:" + "select min(temp.recTime-main.time), max(temp.recTime-main.time),avg(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
                            + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " "
                            + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
                            + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ";");
                    st.execute("select min(temp.recTime-main.time), max(temp.recTime-main.time),avg(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
                            + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " "
                            + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
                            + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ";");
                } else {
                    st.execute("select min(temp.recTime-main.time), max(temp.recTime-main.time),avg(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
                            + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " "
                            + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
                            + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + ""
                            + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ";");
                }
            } else {
                System.out.println("Query:" + "select min(temp.recTime-main.time), max(temp.recTime-main.time),avg(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
                        + "join (select distinct " + metaHandler.getReceivedPackets().get(0) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " "
                        + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(2) + "=" + endingNode + " and " + metaHandler.getNode().get(1) + "='" + level + "')as temp where main." + metaHandler.getReceivedPackets().get(0) + "=temp." + metaHandler.getReceivedPackets().get(0) + " and"
                        + " event='s' and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ""
                        + " and " + metaHandler.getNode().get(1) + "='" + level + "';");
                st.execute("select min(temp.recTime-main.time), max(temp.recTime-main.time),avg(temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
                        + "join (select distinct " + metaHandler.getReceivedPackets().get(0) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " "
                        + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(2) + "=" + endingNode + " and " + metaHandler.getNode().get(1) + "='" + level + "')as temp where main." + metaHandler.getReceivedPackets().get(0) + "=temp." + metaHandler.getReceivedPackets().get(0) + " and"
                        + " event='s' and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ""
                        + " and " + metaHandler.getNode().get(1) + "='" + level + "';");

            }
            long sqlTime = System.currentTimeMillis();
            System.out.println("Delay SQL Query executed in:" + (System.currentTimeMillis() - startTime));
            minEndToEndDelay = 0;
            maxEndToEndDealy = 0;
            averageEndToEndDelay = 0;
            rs = st.getResultSet();
            while (rs.next()) {
                minEndToEndDelay = rs.getDouble(1) * 1000;
                maxEndToEndDealy = rs.getDouble(2) * 1000;
                averageEndToEndDelay = rs.getDouble(3) * 1000;
            }
            System.out.println("Java calculations executed in:" + (System.currentTimeMillis() - sqlTime));
            endTime = System.currentTimeMillis();
            Logger.getLogger(Metrics.class.getName()).info("DELAYS Metric calculation time was:" + (endTime - startTime) + " ms.");
            connection.close();
            return true;
        } catch (SQLException ex) {
            ex.getSQLState();
            ex.printStackTrace();
            return false;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }

    /**
     * This method calculates the Jitter related metrics between the 2 selected
     * nodes.
     *
     * @return boolean, true if the metrics were calculated successfully, false
     * otherwise
     */
    private boolean calculateJitterMetrics() {

        ArrayList<Double> endToEndDelay = new ArrayList();
        ArrayList<Double> jitter = new ArrayList();
        double sum = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DatabaseConnection.getUrl(), DatabaseConnection.getUser(), DatabaseConnection.getPassword());
            Statement st = connection.createStatement();
            ResultSet rs;
            long startTime, endTime;
            startTime = System.currentTimeMillis();
            if (metaHandler.getNode().indexOf("SourceNode") != -1) {
                if (level.equalsIgnoreCase("Link Layer")) {
                    st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main "
                            + "inner join (select UniquePacketId,time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " "
                            + "and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ")as temp where main." + metaHandler.getGeneratePackets().get(2) + "=temp." + metaHandler.getGeneratePackets().get(2) + " "
                            + "and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + " and event='-'");
                } else {
                    st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
                            + "join (select " + metaHandler.getReceivedPackets().get(2) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " "
                            + " and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ")as temp where main." + metaHandler.getReceivedPackets().get(2) + "=temp." + metaHandler.getReceivedPackets().get(2) + " and"
                            + " event='-' and " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + ""
                            + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ";");
                }

            } else {
                st.execute("select (temp.recTime-main.time) from " + traceFile.getTraceFileName() + " as main inner "
                        + "join (select distinct " + metaHandler.getReceivedPackets().get(0) + ",time as recTime from " + traceFile.getTraceFileName() + " where event='r' and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " "
                        + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(2) + "=" + endingNode + " and " + metaHandler.getNode().get(1) + "='" + level + "')as temp where main." + metaHandler.getReceivedPackets().get(0) + "=temp." + metaHandler.getReceivedPackets().get(0) + " and"
                        + " event='s' and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + ""
                        + " and " + metaHandler.getNode().get(1) + "='" + level + "';");
            }
            long sqlTime = System.currentTimeMillis();
            System.out.println("Jitter SQL Query executed in:" + (System.currentTimeMillis() - startTime));
            minDelayJitter = 0.0;
            maxDelayJitter = 0.0;
            delayJitter = 0.0;
            rs = st.getResultSet();
            while (rs.next()) {
                endToEndDelay.add(rs.getDouble(1));
            }
            if (endToEndDelay.size() > 0) {
                minDelayJitter = Integer.MAX_VALUE;
                maxDelayJitter = Integer.MIN_VALUE;
            }

            for (int i = 0; i < endToEndDelay.size() - 1; i++) {
                jitter.add(Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)));
                delayJitter = delayJitter + (Math.abs(endToEndDelay.get(i + 1) - endToEndDelay.get(i)) - delayJitter) / 16;
                if (jitter.get(i) < minDelayJitter) {
                    minDelayJitter = jitter.get(i);
                }
                if (jitter.get(i) > maxDelayJitter) {
                    maxDelayJitter = jitter.get(i);
                }

            }

            for (int i = 0; i < jitter.size(); i++) {
                sum += jitter.get(i);
            }
            if (jitter.size() != 0) {
                averageDelayJitter = sum / jitter.size();
                //delayJitter = maxEndToEndDealy - minEndToEndDelay;
            } else {
                averageDelayJitter = 0;

            }
            delayJitter *= 1000;
            minDelayJitter *= 1000;
            maxDelayJitter *= 1000;
            averageDelayJitter *= 1000;
            System.out.println("Java calculations executed in:" + (System.currentTimeMillis() - sqlTime));
            endTime = System.currentTimeMillis();
            Logger.getLogger(Metrics.class.getName()).info("JITTER Metric calculation time was:" + (endTime - startTime) + " ms.");
            connection.close();
            return true;

        } catch (SQLException ex2) {
            ex2.printStackTrace();
            return false;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }



    }

    /**
     * This method calculates the Packet loss ratio metric between the 2
     * selected nodes.
     *
     * @return boolean, true if the metric were calculated successfully, false
     * otherwise
     */
    private boolean calculatePacketLossRatio() {
        try {
            long startTime, endTime;
            startTime = System.currentTimeMillis();
            double packetsSent = 0;
            double packetsReceived = 0;
            //Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DatabaseConnection.getUrl(), DatabaseConnection.getUser(), DatabaseConnection.getPassword());
            Statement st = connection.createStatement();
            ResultSet rs;
            if (metaHandler.getNode().indexOf("SourceNode") != -1) {

                if (level.equalsIgnoreCase("Link Layer")) {
                    st.execute("select count(distinct " + metaHandler.getGeneratePackets().get(2) + ") from " + traceFile.getTraceFileName() + " where " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and "
                            + " event='-' and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ";");
                    rs = st.getResultSet();
                    while (rs.next()) {
                        packetsSent = rs.getDouble(1);
                    }
                    st.execute("select count(distinct " + metaHandler.getReceivedPackets().get(2) + ") from " + traceFile.getTraceFileName() + " where " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and "
                            + " event='r' and " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + ";");
                    rs = st.getResultSet();
                    while (rs.next()) {
                        packetsReceived = rs.getDouble(1);
                    }
                    //System.out.println("Packets sent:"+packetsSent+" Packets received:"+packetsReceived);
                    if (packetsSent == 0) {
                        packetLossRatio = 0.0;
                    } else {
                        packetLossRatio = (Math.abs(packetsSent - packetsReceived) / packetsSent) * 100;
                    }

                } else {

                    st.execute("select count(distinct " + metaHandler.getGeneratePackets().get(2) + ") from " + traceFile.getTraceFileName() + " where "
                            + " " + metaHandler.getGeneratePackets().get(0) + "=" + startingNode + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + " "
                            + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and event='-';");
                    rs = st.getResultSet();
                    while (rs.next()) {
                        packetsSent = rs.getDouble(1);
                    }
                    st.execute("select count(distinct " + metaHandler.getGeneratePackets().get(2) + ") from " + traceFile.getTraceFileName() + " where "
                            + " " + metaHandler.getReceivedPackets().get(0) + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " "
                            + " and " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + " and event='r';");
                    rs = st.getResultSet();
                    while (rs.next()) {
                        packetsReceived = rs.getDouble(1);
                    }
                    //System.out.println("Packets sent:"+packetsSent+" Packets received:"+packetsReceived);
                    if (packetsSent == 0) {
                        packetLossRatio = 0.0;
                    } else {
                        packetLossRatio = ((packetsSent - packetsReceived) / packetsSent) * 100;
                    }
                    //System.out.println("Packets sent:"+packetsSent+" Packets received:"+packetsReceived+" LossRatio:"+packetLossRatio);
                }


            } else {

                st.execute("select count(distinct " + metaHandler.getGeneratePackets().get(0) + ") from " + traceFile.getTraceFileName() + " where "
                        + " " + metaHandler.getGeneratePackets().get(1) + "=" + startingNode + " and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " "
                        + " and " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and event='s';");
                rs = st.getResultSet();
                while (rs.next()) {
                    packetsSent = rs.getInt(1);
                }
                st.execute("select count(distinct " + metaHandler.getGeneratePackets().get(0) + ") from " + traceFile.getTraceFileName() + " where "
                        + " " + metaHandler.getReceivedPackets().get(1) + "=" + endingNode + " and " + metaHandler.getReceivedPackets().get(2) + "=" + endingNode + " "
                        + " and " + metaHandler.getGeneratePackets().get(2) + "=" + startingNode + " and event='r';");
                rs = st.getResultSet();
                while (rs.next()) {
                    packetsReceived = rs.getInt(1);
                }
                if (packetsSent == 0) {
                    packetLossRatio = 0.0;
                } else {
                    packetLossRatio = (Math.abs(packetsSent - packetsReceived) / packetsSent);
                }

            }
            endTime = System.currentTimeMillis();
            Logger.getLogger(Metrics.class.getName()).info("PACKET LOSS RATIO Metric calculation time was:" + (endTime - startTime) + " ms.");
            connection.close();
            return true;
        } catch (SQLException ex) {
            ex.getSQLState();
            ex.printStackTrace();
            return false;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            return false;
        }
    }
}
