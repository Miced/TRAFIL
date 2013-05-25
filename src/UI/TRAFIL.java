package UI;

import Simulations.NetworkSimulator;
import Simulations.VideoPostSimulator;
import Simulations.VideoSimulator;
import Simulations.TclDesignerPanel;
import java.io.IOException;
import metrics.GeneralSimulationInformation;
import metrics.GeneralNodeInformation;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import metrics.Graph;
import metrics.Metrics;
import src.*;
import utilities.ExportData;
import java.io.*;
import java.util.*;
import metrics.Chart;
import utilities.*;

/**
 *
 * @author charalampi The main class of TRAFIL, contains all the swing objects
 * and handles the user interface.
 */
public class TRAFIL extends javax.swing.JFrame {

    public static FileHandler logHandler;
    public static TracefileHandler TraceHandler;
    public static TclFileLoader TclFileLoader;
    public static TraceFileInfo TraceFile;
    public static TableHandler TableHan;
    public static GeneralSimulationInformation simulationInfo;
    public static MetaDataHandler MetaHandler;
    public static GeneralNodeInformation nodeInfo;
    public static Metrics metric;
    public static Graph graph;
    public static ExportData exporter;
    public JFileChooser fc = new JFileChooser();
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    String selectedTraceType = new String();
    public ResultSet rs;
    public String input;
    public DefaultListModel model = new DefaultListModel();
    public TableListener tmh;
    public SimulationPropertiesWindow simProperties;
    public LinkListWindow linkWindow;
    public ConnectedAgentsWindow connectedAgents;
    public SimulationWirelessSettingsWindow wirelessWindow;
    DefaultTableModel model3;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private javax.swing.ButtonGroup chartEnableButtonGroup = new ButtonGroup();
    private DesignWindow design;
    private static File loginCredentials = new File(PathLocator.getTRAFILPropertiesPath(System.getProperty("user.dir")));

    /**
     * TRAFIL class constructor, it initializes the GUI, and retrieves a pointer
     * to the GUI's main table in order to change it depending on each trace
     * files structure.
     */
    public TRAFIL() {
        model3 = TableHan.getModel3();

        //this.setExtendedState((int)screenSize.getWidth(),(int)screenSize.getHeight());
        initComponents();
        traceFileList.addMouseListener(new DoubleClickHandler(selecttracefile));
        statusBar.setVisible(false);
        username.setFocusable(true);
        linkWindow = new LinkListWindow(this);
        simProperties = new SimulationPropertiesWindow(this);
        connectedAgents = new ConnectedAgentsWindow(tclDesigner);
        wirelessWindow = new SimulationWirelessSettingsWindow();
        TclDesignerPanel.setTrafil(this);
        TclDesignerPanel.setLinkWindow(linkWindow);
        TclDesignerPanel.setConnectedAgents(connectedAgents);
        TclDesignerPanel.setWirelessSettings(wirelessWindow);

        // TODO: find better remember login solution
        if (loginCredentials.exists()) {
            try {
                FileInputStream fstream = new FileInputStream(loginCredentials);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                try {
                    if ((strLine = br.readLine()) != null) {
                        username.setText(strLine);
                    }
                    if ((strLine = br.readLine()) != null) {
                        password.setText(strLine);
                    }
                    if ((strLine = br.readLine()) != null) {
                        dbname.setText(strLine);
                    }
                    if ((strLine = br.readLine()) != null && strLine.equals("y")) {
                        rememberLoginCheckBox.setSelected(true);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public ConnectedAgentsWindow getConnectedAgents() {
        return connectedAgents;
    }

    public TclDesignerPanel getTclDesigner() {
        return tclDesigner;
    }

    public LinkListWindow getLinkWindow() {
        return linkWindow;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chartTypeButtonGroup = new javax.swing.ButtonGroup();
        mainpanel = new javax.swing.JPanel();
        tabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        traceFileList = new javax.swing.JList();
        selecttracefile = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        m2 = new javax.swing.JLabel();
        savechanges = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        next = new javax.swing.JButton();
        previous = new javax.swing.JButton();
        simulationINfoPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        startTime = new javax.swing.JLabel();
        endTime = new javax.swing.JLabel();
        simulationTime = new javax.swing.JLabel();
        numberOfNodes = new javax.swing.JLabel();
        numberOfSendingNodes = new javax.swing.JLabel();
        numberOfSentPackets = new javax.swing.JLabel();
        numberOfSentBytes = new javax.swing.JLabel();
        numberOfGeneratedPackets = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        numberOfForwardedPackets = new javax.swing.JLabel();
        numberOfForwardedBytes = new javax.swing.JLabel();
        numberOfDroppedPackets = new javax.swing.JLabel();
        numberOfDroppedBytes = new javax.swing.JLabel();
        numberOfReceivedPackets = new javax.swing.JLabel();
        maximumPacketSize = new javax.swing.JLabel();
        minimumPacketSize = new javax.swing.JLabel();
        averagePacketSize = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        simulationINfoPanel1 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        startTime1 = new javax.swing.JLabel();
        endTime1 = new javax.swing.JLabel();
        simulationTime1 = new javax.swing.JLabel();
        numberOfNodes1 = new javax.swing.JLabel();
        numberOfSendingNodes1 = new javax.swing.JLabel();
        numberOfSentPackets1 = new javax.swing.JLabel();
        numberOfSentBytes1 = new javax.swing.JLabel();
        numberOfGeneratedPackets1 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        numberOfForwardedPackets1 = new javax.swing.JLabel();
        numberOfForwardedBytes1 = new javax.swing.JLabel();
        numberOfDroppedPackets1 = new javax.swing.JLabel();
        numberOfDroppedBytes1 = new javax.swing.JLabel();
        numberOfReceivedPackets1 = new javax.swing.JLabel();
        maximumPacketSize1 = new javax.swing.JLabel();
        minimumPacketSize1 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        numberOfGeneratedBytes = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        numberOfReceivedBytes = new javax.swing.JLabel();
        numberOfAGTGeneratedPackets = new javax.swing.JLabel();
        numberOfRTRGeneratedPackets = new javax.swing.JLabel();
        numberOfMACGeneratedPackets = new javax.swing.JLabel();
        numberOfAGTGeneratedBytes = new javax.swing.JLabel();
        numberOfRTRGeneratedBytes = new javax.swing.JLabel();
        numberOfMACGeneratedBytes = new javax.swing.JLabel();
        numberOfAGTReceivedPackets = new javax.swing.JLabel();
        numberOfRTRReceivedPackets = new javax.swing.JLabel();
        numberOfMACReceivedPackets = new javax.swing.JLabel();
        numberOfAGTReceivedBytes = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        numberOfRTRReceivedBytes = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        numberOfMACReceivedBytes = new javax.swing.JLabel();
        averagePacketSize1 = new javax.swing.JLabel();
        nodeInformation = new javax.swing.JPanel();
        nodeSelector = new javax.swing.JComboBox();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        nodeSentPackets = new javax.swing.JLabel();
        nodeSentBytes = new javax.swing.JLabel();
        nodeGeneratedPackets = new javax.swing.JLabel();
        nodeGeneratedBytes = new javax.swing.JLabel();
        nodeForwardedPackets = new javax.swing.JLabel();
        nodeForwardedBytes = new javax.swing.JLabel();
        nodeDroppedPackets = new javax.swing.JLabel();
        nodeDroppedBytes = new javax.swing.JLabel();
        nodeReceivedPackets = new javax.swing.JLabel();
        nodeReceivedBytes = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        nodeAGTGeneratedPackets = new javax.swing.JLabel();
        nodeRTRGeneratedPackets = new javax.swing.JLabel();
        nodeMACGeneratedPackets = new javax.swing.JLabel();
        nodeAGTGeneratedBytes = new javax.swing.JLabel();
        nodeRTRGeneratedBytes = new javax.swing.JLabel();
        nodeMACGeneratedBytes = new javax.swing.JLabel();
        nodeAGTReceivedPackets = new javax.swing.JLabel();
        nodeRTRReceivedPackets = new javax.swing.JLabel();
        nodeMACReceivedPackets = new javax.swing.JLabel();
        nodeAGTReceivedBytes = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        nodeRTRReceivedBytes = new javax.swing.JLabel();
        nodeMACReceivedBytes = new javax.swing.JLabel();
        nodeMaximumPacket = new javax.swing.JLabel();
        nodeMinimumPacket = new javax.swing.JLabel();
        nodeAveragePacket = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        startNodes = new javax.swing.JComboBox();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        endNodes = new javax.swing.JComboBox();
        jLabel65 = new javax.swing.JLabel();
        levels = new javax.swing.JComboBox();
        calculateMetrics = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        metricsTable = new javax.swing.JTable();
        sqlQueryPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        sqlQueryArea = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        queryResultArea = new javax.swing.JTextArea();
        jLabel71 = new javax.swing.JLabel();
        executeSQL = new javax.swing.JButton();
        clearSQL = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        rawVideoPath = new javax.swing.JTextField();
        browseRawVideo = new javax.swing.JButton();
        jLabel83 = new javax.swing.JLabel();
        simulationScriptPath = new javax.swing.JTextField();
        browseSimulationScript = new javax.swing.JButton();
        clearRawVideoPath = new javax.swing.JButton();
        clearSimulationScriptPath = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        inputVideoSize = new javax.swing.JComboBox();
        jLabel85 = new javax.swing.JLabel();
        inputFrameRate = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        outputGroupOfPictures = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        qualityScaleStartValue = new javax.swing.JTextField();
        qualityScaleEndValue = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        outputVideoSize = new javax.swing.JComboBox();
        jLabel92 = new javax.swing.JLabel();
        outputFrameRate = new javax.swing.JTextField();
        outputVideoCodec = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        jLabel98 = new javax.swing.JLabel();
        mp4PortNumber = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        mp4IpAddress = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        mp4MTU = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        mp4Fps = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel102 = new javax.swing.JLabel();
        senderPrefix = new javax.swing.JTextField();
        jLabel103 = new javax.swing.JLabel();
        receiverPrefix = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        ffmpegVideoName = new javax.swing.JTextField();
        jLabel105 = new javax.swing.JLabel();
        mp4OutputFileName = new javax.swing.JTextField();
        executePreAndSimulation = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel107 = new javax.swing.JLabel();
        receiverFileSelect = new javax.swing.JComboBox();
        mp4TraceSelect = new javax.swing.JComboBox();
        jLabel104 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        videoDataFile1 = new javax.swing.JComboBox();
        jLabel110 = new javax.swing.JLabel();
        postSimulationVideoOutputName = new javax.swing.JTextField();
        jLabel106 = new javax.swing.JLabel();
        senderFileSelect = new javax.swing.JComboBox();
        jLabel109 = new javax.swing.JLabel();
        videoDataFile2 = new javax.swing.JComboBox();
        jPanel17 = new javax.swing.JPanel();
        jLabel111 = new javax.swing.JLabel();
        postSimulationFrameRate = new javax.swing.JTextField();
        jLabel112 = new javax.swing.JLabel();
        postSimulationVideoCodec = new javax.swing.JComboBox();
        jLabel113 = new javax.swing.JLabel();
        postSimulationOutputVideoName = new javax.swing.JTextField();
        executepostSimulation = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        designScrollPanel = new javax.swing.JScrollPane();
        tclDesigner = new Simulations.TclDesignerPanel();
        jToolBar1 = new javax.swing.JToolBar();
        newWiredNodeButton = new javax.swing.JToggleButton();
        newWirelessNodeButton = new javax.swing.JToggleButton();
        newLinkButton = new javax.swing.JToggleButton();
        jPanel20 = new javax.swing.JPanel();
        simParamButton = new javax.swing.JButton();
        linkListButton = new javax.swing.JButton();
        connectedAgentsButton = new javax.swing.JButton();
        wirelessSettingsButton = new javax.swing.JButton();
        resetTclDesignButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        chartParameters = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        chartStartNode = new javax.swing.JComboBox();
        jLabel68 = new javax.swing.JLabel();
        chartEndNode = new javax.swing.JComboBox();
        enableNodetoNode = new javax.swing.JRadioButton();
        chartPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        throughputChart = new javax.swing.JRadioButton();
        delayJitterChart = new javax.swing.JRadioButton();
        endToEndDelayChart = new javax.swing.JRadioButton();
        throughputBitsChart = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        nodeChartStartNode = new javax.swing.JComboBox();
        enableNodeSpecific = new javax.swing.JRadioButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        createChart = new javax.swing.JButton();
        loadMoreTracesButton = new javax.swing.JButton();
        addMoreGraphsButton = new javax.swing.JButton();
        jLabel114 = new javax.swing.JLabel();
        chartTitle = new javax.swing.JTextField();
        jLabel115 = new javax.swing.JLabel();
        lineTitle = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        chartLevel = new javax.swing.JComboBox();
        samplingRateSelector = new javax.swing.JComboBox();
        jLabel69 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        availableTraceFiles = new javax.swing.JList();
        connect_panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        connect = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        password = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        dbname = new javax.swing.JTextField();
        rememberLoginCheckBox = new javax.swing.JCheckBox();
        statusBar = new javax.swing.JLabel();
        menuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        OpenTraceFile = new javax.swing.JMenuItem();
        openTCLFile = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        Exit = new javax.swing.JMenuItem();
        exportToFile = new javax.swing.JMenu();
        exportTraceFile = new javax.swing.JMenu();
        exportToExcell = new javax.swing.JMenuItem();
        exportToTxtFile = new javax.swing.JMenuItem();
        exportSimlutaionInfo = new javax.swing.JMenuItem();
        exportMetrics = new javax.swing.JMenuItem();
        exportChart = new javax.swing.JMenuItem();
        NsTool = new javax.swing.JMenu();
        openOTCLScript = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TRAFIL");
        setResizable(false);

        mainpanel.setMaximumSize(new java.awt.Dimension(42767, 42767));

        tabbed.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        tabbed.setMinimumSize(new java.awt.Dimension(0, 0));

        jPanel1.setFont(new java.awt.Font("Calibri", 0, 10)); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 0));

        jTable1.setFont(new java.awt.Font("Calibri", 1, 8)); // NOI18N
        jTable1.setModel(model3);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(5);

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 255));
        jLabel3.setText("Showing Current Trace File Information:");

        traceFileList.setModel(model);
        jScrollPane2.setViewportView(traceFileList);

        selecttracefile.setText("Select");
        selecttracefile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selecttracefileActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 255));
        jLabel4.setText("Trace Files Available:");

        savechanges.setText("Save Changes");
        savechanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savechangesActionPerformed(evt);
            }
        });

        Delete.setText("Delete");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        next.setText("Next");
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        previous.setText("Previous");
        previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousActionPerformed(evt);
            }
        });

        jLabel6.setText("Start Time:");

        jLabel7.setText("End Time:");

        jLabel8.setText("Overall Time:");

        jLabel9.setText("Number of Nodes:");

        jLabel10.setText("Number of Sending Nodes:");

        jLabel11.setText("Number of Sent Packets:");

        jLabel12.setText("Number of Sent Bytes:");

        jLabel13.setText("Number of Generated Packets:");

        jLabel14.setText("Number of Forwarded Packets:");

        jLabel15.setText("Number of Forwarded Bytes:");

        jLabel16.setText("Number of Dropped Packets:");

        jLabel17.setText("Number of Dropped Bytes:");

        jLabel18.setText("Number of Received Packets:");

        jLabel19.setText("Maximum Packet Size:");

        jLabel20.setText("Minimum Packet Size:");

        jLabel21.setText("Average Packet Size:");

        javax.swing.GroupLayout simulationINfoPanelLayout = new javax.swing.GroupLayout(simulationINfoPanel);
        simulationINfoPanel.setLayout(simulationINfoPanelLayout);
        simulationINfoPanelLayout.setHorizontalGroup(
            simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simulationINfoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(numberOfGeneratedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfSentBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfSentPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfSendingNodes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfNodes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(simulationTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(endTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startTime, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(averagePacketSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minimumPacketSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(maximumPacketSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfDroppedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfDroppedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfForwardedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfForwardedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
        );
        simulationINfoPanelLayout.setVerticalGroup(
            simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simulationINfoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(startTime)
                    .addComponent(jLabel14)
                    .addComponent(numberOfForwardedPackets))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(endTime)
                    .addComponent(jLabel15)
                    .addComponent(numberOfForwardedBytes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(simulationTime)
                    .addComponent(jLabel16)
                    .addComponent(numberOfDroppedPackets))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(numberOfNodes)
                    .addComponent(jLabel17)
                    .addComponent(numberOfDroppedBytes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(numberOfSendingNodes)
                    .addComponent(jLabel18)
                    .addComponent(numberOfReceivedPackets))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(numberOfSentPackets)
                    .addComponent(jLabel19)
                    .addComponent(maximumPacketSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(numberOfSentBytes)
                    .addComponent(jLabel20)
                    .addComponent(minimumPacketSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationINfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(numberOfGeneratedPackets)
                    .addComponent(jLabel21)
                    .addComponent(averagePacketSize)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selecttracefile)
                            .addComponent(Delete))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(previous)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(savechanges)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(simulationINfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1043, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(next)
                        .addComponent(previous)
                        .addComponent(savechanges)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(selecttracefile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(196, 196, 196))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(simulationINfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        tabbed.addTab("Trace File Info", jPanel1);

        simulationINfoPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "General Simulation Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14)))); // NOI18N

        jLabel22.setText("Start Time:");

        jLabel23.setText("End Time:");

        jLabel24.setText("Overall Time:");

        jLabel25.setText("Number of Nodes:");

        jLabel26.setText("Number of Sending Nodes:");

        jLabel27.setText("Number of Sent Packets:");

        jLabel28.setText("Number of Sent Bytes:");

        jLabel29.setText("Number of Generated Packets:");

        jLabel30.setText("Number of Forwarded Packets:");

        jLabel31.setText("Number of Forwarded Bytes:");

        jLabel32.setText("Number of Dropped Packets:");

        jLabel33.setText("Number of Dropped Bytes:");

        jLabel34.setText("Number of Received Packets:");

        jLabel35.setText("Maximum Packet Size:");

        jLabel36.setText("Minimum Packet Size:");

        jLabel37.setText("Average Packet Size:");

        jLabel38.setText("Number of AGT Generated Packets:");

        jLabel39.setText("Number of RTR Generated Packets:");

        jLabel40.setText("Number of MAC Generated Packets:");

        jLabel41.setText("Number of AGT Generated Bytes:");

        jLabel42.setText("Number of RTR Generated Bytes:");

        jLabel43.setText("Number of MAC Generated Bytes:");

        jLabel44.setText("Number of AGT  Received Packets:");

        jLabel45.setText("Number of RTR  Received Packets:");

        jLabel46.setText("Number of MAC  Received Packets:");

        jLabel47.setText("Number of Generated Bytes:");

        jLabel49.setText("Number of Received Bytes:");

        jLabel61.setText("Number of AGT Received Bytes:");

        jLabel62.setText("Number of RTR Received Bytes:");

        jLabel64.setText("Number of MAC Received Bytes:");

        javax.swing.GroupLayout simulationINfoPanel1Layout = new javax.swing.GroupLayout(simulationINfoPanel1);
        simulationINfoPanel1.setLayout(simulationINfoPanel1Layout);
        simulationINfoPanel1Layout.setHorizontalGroup(
            simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(numberOfGeneratedPackets1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(numberOfSentBytes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfSentPackets1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfSendingNodes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfNodes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(simulationTime1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(endTime1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startTime1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numberOfAGTReceivedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel30))
                .addGap(18, 18, 18)
                .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(numberOfRTRReceivedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                        .addComponent(minimumPacketSize1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(maximumPacketSize1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfReceivedPackets1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfDroppedBytes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfDroppedPackets1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfForwardedBytes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfForwardedPackets1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfReceivedBytes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(averagePacketSize1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(numberOfRTRGeneratedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addGap(10, 10, 10)
                        .addComponent(numberOfAGTGeneratedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40)
                            .addComponent(jLabel41)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numberOfAGTGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfMACGeneratedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfRTRGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel43)
                            .addComponent(jLabel44)
                            .addComponent(jLabel45))
                        .addGap(18, 18, 18)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numberOfAGTReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfMACGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfRTRReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46)
                            .addComponent(jLabel64))
                        .addGap(18, 18, 18)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numberOfMACReceivedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numberOfMACReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        simulationINfoPanel1Layout.setVerticalGroup(
            simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simulationINfoPanel1Layout.createSequentialGroup()
                .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(numberOfAGTGeneratedPackets))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(numberOfRTRGeneratedPackets))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(numberOfMACGeneratedPackets))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(numberOfAGTGeneratedBytes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(numberOfRTRGeneratedBytes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(numberOfMACGeneratedBytes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(numberOfAGTReceivedPackets))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(numberOfRTRReceivedPackets))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(numberOfMACReceivedPackets))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel64)
                            .addComponent(numberOfMACReceivedBytes)))
                    .addGroup(simulationINfoPanel1Layout.createSequentialGroup()
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(startTime1)
                            .addComponent(jLabel30)
                            .addComponent(numberOfForwardedPackets1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(endTime1)
                            .addComponent(jLabel31)
                            .addComponent(numberOfForwardedBytes1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(simulationTime1)
                            .addComponent(jLabel32)
                            .addComponent(numberOfDroppedPackets1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(numberOfNodes1)
                            .addComponent(jLabel33)
                            .addComponent(numberOfDroppedBytes1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(numberOfSendingNodes1)
                            .addComponent(jLabel34)
                            .addComponent(numberOfReceivedPackets1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(numberOfSentPackets1)
                            .addComponent(jLabel35)
                            .addComponent(maximumPacketSize1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(numberOfSentBytes1)
                            .addComponent(jLabel36)
                            .addComponent(minimumPacketSize1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(numberOfGeneratedPackets1)
                            .addComponent(jLabel37)
                            .addComponent(averagePacketSize1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(numberOfGeneratedBytes)
                            .addComponent(jLabel49)
                            .addComponent(numberOfReceivedBytes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationINfoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numberOfAGTReceivedBytes)
                            .addComponent(jLabel61)
                            .addComponent(jLabel62)
                            .addComponent(numberOfRTRReceivedBytes))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nodeInformation.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Node Based Simulation Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N

        nodeSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeSelectorActionPerformed(evt);
            }
        });

        jLabel48.setText("Current Node:");

        jLabel50.setText("Number of Sent Packets:");

        jLabel51.setText("Number of Sent Bytes:");

        jLabel52.setText("Number of Generated Packets:");

        jLabel53.setText("Number of Generated Bytes:");

        jLabel63.setText("Number of AGT Received Bytes:");

        jLabel54.setText("Number of Forwarded Packets:");

        jLabel55.setText("Number of Forwarded Bytes:");

        jLabel56.setText("Number of Dropped Packets:");

        jLabel57.setText("Number of Dropped Bytes:");

        jLabel58.setText("Number of Received Packets:");

        jLabel72.setText("Number of Received Bytes:");
        jLabel72.setMaximumSize(new java.awt.Dimension(202, 18));
        jLabel72.setMinimumSize(new java.awt.Dimension(202, 18));
        jLabel72.setPreferredSize(new java.awt.Dimension(202, 18));

        jLabel74.setText("Number of AGT Generated Packets:");

        jLabel75.setText("Number of RTR Generated Packets:");

        jLabel76.setText("Number of MAC Generated Packets:");

        jLabel77.setText("Number of AGT Generated Bytes:");

        jLabel78.setText("Number of RTR Generated Bytes:");

        jLabel79.setText("Number of MAC Generated Bytes:");

        jLabel80.setText("Number of AGT  Received Packets:");

        jLabel81.setText("Number of RTR  Received Packets:");

        jLabel82.setText("Number of MAC  Received Packets:");

        jLabel93.setText("Number of RTR Received Bytes:");

        jLabel94.setText("Number of MAC Received Bytes:");

        jLabel95.setText("Maximum Packet Size:");

        jLabel96.setText("Minimum Packet Size:");

        jLabel97.setText("Average Packet Size:");

        javax.swing.GroupLayout nodeInformationLayout = new javax.swing.GroupLayout(nodeInformation);
        nodeInformation.setLayout(nodeInformationLayout);
        nodeInformationLayout.setHorizontalGroup(
            nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nodeInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nodeInformationLayout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nodeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(nodeInformationLayout.createSequentialGroup()
                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel57, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel72, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nodeInformationLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nodeReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeDroppedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeDroppedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeForwardedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeForwardedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeGeneratedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeSentBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nodeSentPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(nodeInformationLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nodeReceivedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel75)
                            .addComponent(jLabel74)
                            .addComponent(jLabel80))
                        .addGap(18, 18, 18)
                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nodeAGTReceivedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeMACReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeRTRReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeAGTReceivedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeMACGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeRTRGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeAGTGeneratedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeMACGeneratedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeRTRGeneratedPackets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nodeAGTGeneratedPackets))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel95))
                            .addGroup(nodeInformationLayout.createSequentialGroup()
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nodeMaximumPacket, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(nodeMinimumPacket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nodeAveragePacket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nodeMACReceivedBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nodeRTRReceivedBytes)))))))
                .addGap(60, 60, 60))
        );
        nodeInformationLayout.setVerticalGroup(
            nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nodeInformationLayout.createSequentialGroup()
                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(nodeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(nodeInformationLayout.createSequentialGroup()
                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nodeInformationLayout.createSequentialGroup()
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nodeRTRReceivedBytes, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel74)
                                        .addComponent(nodeAGTGeneratedPackets)
                                        .addComponent(jLabel93)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel75)
                                    .addComponent(nodeRTRGeneratedPackets)
                                    .addComponent(jLabel94)
                                    .addComponent(nodeMACReceivedBytes))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(nodeInformationLayout.createSequentialGroup()
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel76)
                                            .addComponent(nodeMACGeneratedPackets))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel77)
                                            .addComponent(nodeAGTGeneratedBytes))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel78)
                                            .addComponent(nodeRTRGeneratedBytes))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel79)
                                            .addComponent(nodeMACGeneratedBytes))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel80)
                                            .addComponent(nodeAGTReceivedPackets))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel81)
                                            .addComponent(nodeRTRReceivedPackets))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel82)
                                            .addComponent(nodeMACReceivedPackets))
                                        .addGap(5, 5, 5))
                                    .addGroup(nodeInformationLayout.createSequentialGroup()
                                        .addComponent(jLabel95)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel96)
                                            .addComponent(nodeMinimumPacket))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel97)
                                            .addComponent(nodeAveragePacket))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(nodeInformationLayout.createSequentialGroup()
                                        .addComponent(nodeMaximumPacket)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(nodeInformationLayout.createSequentialGroup()
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel50)
                                    .addComponent(nodeSentPackets))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel51)
                                    .addComponent(nodeSentBytes))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel52)
                                    .addComponent(nodeGeneratedPackets))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel53)
                                    .addComponent(nodeGeneratedBytes))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel54)
                                    .addComponent(nodeForwardedPackets))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel55)
                                    .addComponent(nodeForwardedBytes))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel56)
                                    .addComponent(nodeDroppedPackets))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel57)
                                    .addComponent(nodeDroppedBytes))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nodeReceivedPackets)
                                    .addComponent(jLabel58))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(nodeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel63)
                        .addComponent(nodeAGTReceivedBytes))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nodeInformationLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(nodeReceivedBytes))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(simulationINfoPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nodeInformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(294, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(simulationINfoPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nodeInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        tabbed.addTab("Simulation Information", jPanel3);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Parameter Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N

        jLabel59.setText("Starting Node:");

        jLabel60.setText("Ending Node:");

        jLabel65.setText("Level:");

        calculateMetrics.setText("Calculate");
        calculateMetrics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateMetricsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startNodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(levels, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel60)
                        .addGap(26, 26, 26)
                        .addComponent(endNodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(231, 231, 231)
                        .addComponent(calculateMetrics)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(jLabel60)
                    .addComponent(endNodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startNodes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(levels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(calculateMetrics))
        );

        metricsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Throughput(packets/sec)", null},
                {"Throughput(bits/sec)", null},
                {"Minimum End to End Delay(miliseconds)", null},
                {"Maximum End to End Delay(miliseconds)", null},
                {"Average End to End Delay(miliseconds)", null},
                {"DelayJitter(miliseconds)", null},
                {"Minimum Delay Jitter(miliseconds)", null},
                {"Maximum Delay Jitter(miliseconds)", null},
                {"Average Delay Jitter(miliseconds)", null},
                {"Packet Loss Ratio(miliseconds)", null}
            },
            new String [] {
                "", ""
            }
        ));
        jScrollPane3.setViewportView(metricsTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(293, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addGap(158, 158, 158))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(219, Short.MAX_VALUE))
        );

        tabbed.addTab("Metrics", jPanel4);

        sqlQueryArea.setColumns(20);
        sqlQueryArea.setRows(5);
        sqlQueryArea.setBorder(javax.swing.BorderFactory.createTitledBorder("SQL Query text:"));
        jScrollPane4.setViewportView(sqlQueryArea);

        queryResultArea.setColumns(20);
        queryResultArea.setRows(5);
        jScrollPane5.setViewportView(queryResultArea);

        jLabel71.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel71.setForeground(java.awt.Color.blue);
        jLabel71.setText("Query Results:");

        executeSQL.setText("Execute Query");
        executeSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeSQLActionPerformed(evt);
            }
        });

        clearSQL.setText("Clear");
        clearSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSQLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sqlQueryPanelLayout = new javax.swing.GroupLayout(sqlQueryPanel);
        sqlQueryPanel.setLayout(sqlQueryPanelLayout);
        sqlQueryPanelLayout.setHorizontalGroup(
            sqlQueryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sqlQueryPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(sqlQueryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(sqlQueryPanelLayout.createSequentialGroup()
                        .addComponent(executeSQL, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clearSQL, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(sqlQueryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(sqlQueryPanelLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(353, Short.MAX_VALUE))
                    .addGroup(sqlQueryPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        sqlQueryPanelLayout.setVerticalGroup(
            sqlQueryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sqlQueryPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel71)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sqlQueryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sqlQueryPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(sqlQueryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(executeSQL)
                            .addComponent(clearSQL)))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        tabbed.addTab("SQL Queries", sqlQueryPanel);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Simulation Input", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N

        jLabel73.setText("Raw Video File:");

        rawVideoPath.setEnabled(false);

        browseRawVideo.setText("Browse");
        browseRawVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseRawVideoActionPerformed(evt);
            }
        });

        jLabel83.setText("Simulation Script:");

        simulationScriptPath.setEnabled(false);

        browseSimulationScript.setText("Browse");
        browseSimulationScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseSimulationScriptActionPerformed(evt);
            }
        });

        clearRawVideoPath.setText("Clear");

        clearSimulationScriptPath.setText("Clear");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel73, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel83, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(simulationScriptPath))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(rawVideoPath, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(browseRawVideo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearRawVideoPath))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(browseSimulationScript)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearSimulationScriptPath)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(rawVideoPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseRawVideo)
                    .addComponent(clearRawVideoPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(simulationScriptPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseSimulationScript)
                    .addComponent(clearSimulationScriptPath))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pre-Simulation Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14), java.awt.Color.blue)); // NOI18N
        jPanel11.setForeground(new java.awt.Color(0, 51, 255));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "FFmpeg Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel13.setForeground(new java.awt.Color(0, 51, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(340, 292));

        jLabel84.setText("Input Size(-s):");

        inputVideoSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "sqcif", "qcif", "cif", "4cif", "16cif", "qqvga", "qvga", "vga", "svga", "hd480" }));

        jLabel85.setText("Input Frame Rate(-r):");

        inputFrameRate.setText("25");

        jLabel86.setText("Output Codec(-vcodec):");

        jLabel87.setText("Output GOP(-g):");

        outputGroupOfPictures.setText("12");

        jLabel89.setText("Quality Scale Start Value:");

        jLabel90.setText("Quality Scale End Value:");

        qualityScaleStartValue.setText("2");

        qualityScaleEndValue.setText("31");

        jLabel91.setText("Output Size(-s):");

        outputVideoSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "sqcif", "qcif", "cif", "4cif", "16cif", "qqvga", "qvga", "vga", "svga", "hd480" }));

        jLabel92.setText("Output Frame Rate(-r):");

        outputFrameRate.setText("25");

        outputVideoCodec.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mpeg4" }));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel92, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel87, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                            .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputVideoSize, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(qualityScaleStartValue)
                    .addComponent(outputGroupOfPictures)
                    .addComponent(inputFrameRate)
                    .addComponent(outputVideoCodec, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(qualityScaleEndValue, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outputVideoSize, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(outputFrameRate))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputVideoSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84))
                .addGap(5, 5, 5)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel85))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputVideoCodec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputGroupOfPictures, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel87))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qualityScaleStartValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel89))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(qualityScaleEndValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputVideoSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel91))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(outputFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MP4 Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12), java.awt.Color.blue)); // NOI18N
        jPanel14.setForeground(java.awt.Color.blue);

        jLabel98.setText("Port #:");

        mp4PortNumber.setText("1234");

        jLabel99.setText("IP :");

        mp4IpAddress.setText("224.168.1.100");

        jLabel100.setText("MTU:");

        mp4MTU.setText("2500");

        jLabel101.setText("Frame Rate:");

        mp4Fps.setText("1000");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel98)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mp4PortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel101))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel99, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mp4IpAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel100)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mp4MTU, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(mp4Fps))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel98)
                    .addComponent(mp4PortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel101)
                    .addComponent(mp4Fps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(mp4IpAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel100)
                    .addComponent(mp4MTU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Simulation Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 12), java.awt.Color.blue)); // NOI18N
        jPanel15.setForeground(new java.awt.Color(0, 51, 255));

        jLabel102.setText("Sender Output Prefix:");

        senderPrefix.setText("sd_be_");

        jLabel103.setText("Receiver Output Prefix:");

        receiverPrefix.setText("rd_be_");

        jLabel88.setText("FFmpeg Video Output Name:");

        ffmpegVideoName.setText("${videoName}_Q${qualityIndex}");

        jLabel105.setText("MP4 Output Name");

        mp4OutputFileName.setText("st_${videoName}_Q${qualityIndex}");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(receiverPrefix, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel103, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel102, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(senderPrefix, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ffmpegVideoName)
                    .addComponent(jLabel105)
                    .addComponent(mp4OutputFileName, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel102)
                    .addComponent(jLabel88))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(senderPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ffmpegVideoName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(jLabel105))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(receiverPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mp4OutputFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        executePreAndSimulation.setText("Execute Simulation");
        executePreAndSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executePreAndSimulationActionPerformed(evt);
            }
        });

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Post Simulation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), java.awt.Color.blue)); // NOI18N

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "et_ra properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), java.awt.Color.blue)); // NOI18N

        jLabel107.setText("Rx File:");

        jLabel104.setText("Output Name:");

        jLabel108.setText("Mp4 Trace:");

        jLabel110.setText("Data 2:");

        postSimulationVideoOutputName.setText("test1.m4v");

        jLabel106.setText("Tx File:");

        senderFileSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                senderFileSelectActionPerformed(evt);
            }
        });

        jLabel109.setText(" Data 1:");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel109, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(jLabel106, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(videoDataFile1, 0, 173, Short.MAX_VALUE)
                            .addComponent(senderFileSelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel104)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(postSimulationVideoOutputName, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel108, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel110, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel107, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(videoDataFile2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mp4TraceSelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(receiverFileSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel106)
                            .addComponent(senderFileSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel109)
                            .addComponent(videoDataFile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel104)
                            .addComponent(postSimulationVideoOutputName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel107)
                            .addComponent(receiverFileSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel108)
                            .addComponent(mp4TraceSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel110)
                            .addComponent(videoDataFile2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "FFmpeg Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), java.awt.Color.blue)); // NOI18N

        jLabel111.setText("Frame Rate:");

        postSimulationFrameRate.setText("25");

        jLabel112.setText("Video Codec:");

        postSimulationVideoCodec.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "rawvideo", "mpeg4" }));

        jLabel113.setText("Output Name:");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel111)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(postSimulationFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel112)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(postSimulationVideoCodec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel113)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(postSimulationOutputVideoName, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 9, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel111)
                    .addComponent(postSimulationFrameRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel112)
                    .addComponent(postSimulationVideoCodec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel113)
                    .addComponent(postSimulationOutputVideoName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        executepostSimulation.setText("Execute Post Simulation");
        executepostSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executepostSimulationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(executepostSimulation))
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(executepostSimulation)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(executePreAndSimulation)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(executePreAndSimulation))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbed.addTab("Evalvid-RA Simulation", jPanel9);
        jPanel9.getAccessibleContext().setAccessibleName("evalvidSimulation");

        designScrollPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout tclDesignerLayout = new javax.swing.GroupLayout(tclDesigner);
        tclDesigner.setLayout(tclDesignerLayout);
        tclDesignerLayout.setHorizontalGroup(
            tclDesignerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 919, Short.MAX_VALUE)
        );
        tclDesignerLayout.setVerticalGroup(
            tclDesignerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        designScrollPanel.setViewportView(tclDesigner);

        jToolBar1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.setRollover(true);

        newWiredNodeButton.setBackground(java.awt.Color.red);
        newWiredNodeButton.setText("Wired Node");
        newWiredNodeButton.setFocusable(false);
        newWiredNodeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newWiredNodeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newWiredNodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWiredNodeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(newWiredNodeButton);

        newWirelessNodeButton.setBackground(java.awt.Color.blue);
        newWirelessNodeButton.setText("Wireless Node");
        newWirelessNodeButton.setFocusable(false);
        newWirelessNodeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newWirelessNodeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newWirelessNodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWirelessNodeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(newWirelessNodeButton);

        newLinkButton.setText("Link");
        newLinkButton.setFocusable(false);
        newLinkButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newLinkButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newLinkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newLinkButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(newLinkButton);

        simParamButton.setText("Create Simulation");
        simParamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simParamButtonActionPerformed(evt);
            }
        });

        linkListButton.setText("Link List");
        linkListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkListButtonActionPerformed(evt);
            }
        });

        connectedAgentsButton.setText("Connected Agents");
        connectedAgentsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectedAgentsButtonActionPerformed(evt);
            }
        });

        wirelessSettingsButton.setText("Wireless Settings");
        wirelessSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wirelessSettingsButtonActionPerformed(evt);
            }
        });

        resetTclDesignButton.setText("Reset All");
        resetTclDesignButton.setToolTipText("Resets everything in the Tcl Design panel");
        resetTclDesignButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetTclDesignButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(simParamButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(linkListButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(connectedAgentsButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
            .addComponent(wirelessSettingsButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(resetTclDesignButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addGap(0, 71, Short.MAX_VALUE)
                .addComponent(resetTclDesignButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wirelessSettingsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectedAgentsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(linkListButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simParamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(designScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(designScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbed.addTab("TCL Design", jPanel19);

        chartParameters.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Node to Node Parameter Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N

        jLabel66.setText("Starting Node:");

        chartStartNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chartStartNodeActionPerformed(evt);
            }
        });

        jLabel68.setText("Ending Node:");

        enableNodetoNode.setSelected(true);
        enableNodetoNode.setText("Enable");
        enableNodetoNode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableNodetoNodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout chartParametersLayout = new javax.swing.GroupLayout(chartParameters);
        chartParameters.setLayout(chartParametersLayout);
        chartParametersLayout.setHorizontalGroup(
            chartParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartParametersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chartParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chartParametersLayout.createSequentialGroup()
                        .addGroup(chartParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel68, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                            .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chartParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chartStartNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chartEndNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(136, 136, 136))
                    .addGroup(chartParametersLayout.createSequentialGroup()
                        .addComponent(enableNodetoNode)
                        .addContainerGap(215, Short.MAX_VALUE))))
        );
        chartParametersLayout.setVerticalGroup(
            chartParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartParametersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chartParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(chartStartNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chartParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(chartEndNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enableNodetoNode)
                .addGap(20, 20, 20))
        );

        chartPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chart Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 14))); // NOI18N
        chartPanel.setPreferredSize(new java.awt.Dimension(720, 234));

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 889, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chart Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12)))); // NOI18N

        throughputChart.setText("Packet Delivery Rate(packets/sec)");

        delayJitterChart.setText("Delay Jitter");

        endToEndDelayChart.setText("Packet End to End Delay");

        throughputBitsChart.setText("Throughput(bits/sec)");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(throughputBitsChart)
                    .addComponent(delayJitterChart)
                    .addComponent(endToEndDelayChart)
                    .addComponent(throughputChart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(throughputChart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(throughputBitsChart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delayJitterChart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(endToEndDelayChart)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "Node Specific Parameter Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12)))); // NOI18N

        jLabel70.setText("Start Node:");

        enableNodeSpecific.setText("Enable");
        enableNodeSpecific.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableNodeSpecificActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addGap(18, 18, 18)
                        .addComponent(nodeChartStartNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(enableNodeSpecific))
                .addContainerGap(123, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(nodeChartStartNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(enableNodeSpecific)
                .addGap(32, 32, 32))
        );

        createChart.setText("Create New Chart");
        createChart.setToolTipText("Create a new chart using the currently selected trace file info.");
        createChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createChartActionPerformed(evt);
            }
        });

        loadMoreTracesButton.setText("Load New Trace File");
        loadMoreTracesButton.setToolTipText("Load a new trace file but keep existing chart to add more graphs.");
        loadMoreTracesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMoreTracesButtonActionPerformed(evt);
            }
        });

        addMoreGraphsButton.setText("Add More Lines");
        addMoreGraphsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMoreGraphsButtonActionPerformed(evt);
            }
        });

        jLabel114.setText("Chart title:");

        chartTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chartTitleFocusLost(evt);
            }
        });

        jLabel115.setText("Line title:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(136, Short.MAX_VALUE)
                .addComponent(createChart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addMoreGraphsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadMoreTracesButton))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel115)
                    .addComponent(jLabel114))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chartTitle)
                    .addComponent(lineTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel114)
                    .addComponent(chartTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel115)
                    .addComponent(lineTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createChart)
                    .addComponent(loadMoreTracesButton)
                    .addComponent(addMoreGraphsButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel67.setText("Level:");

        samplingRateSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "5" }));

        jLabel69.setText("Sampling Rate(sec):");

        availableTraceFiles.setModel(model);
        jScrollPane6.setViewportView(availableTraceFiles);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chartLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel69)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(samplingRateSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(245, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(samplingRateSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67)
                    .addComponent(jLabel69)
                    .addComponent(chartLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(chartParameters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chartParameters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbed.addTab("Charts", jPanel2);

        connect_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Database Connection Panel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        connect_panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                connect_panelKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel1.setText("Username:");

        username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                usernameKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel2.setText("Password:");

        connect.setText("Connect");
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwordKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Database Name:");

        dbname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dbnameKeyReleased(evt);
            }
        });

        rememberLoginCheckBox.setText("Remember login");

        javax.swing.GroupLayout connect_panelLayout = new javax.swing.GroupLayout(connect_panel);
        connect_panel.setLayout(connect_panelLayout);
        connect_panelLayout.setHorizontalGroup(
            connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connect_panelLayout.createSequentialGroup()
                .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(connect_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(connect_panelLayout.createSequentialGroup()
                                .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dbname))
                                .addGap(112, 112, 112))
                            .addGroup(connect_panelLayout.createSequentialGroup()
                                .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rememberLoginCheckBox))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(connect_panelLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(connect)
                        .addGap(32, 32, 32)
                        .addComponent(cancel)))
                .addContainerGap())
        );
        connect_panelLayout.setVerticalGroup(
            connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connect_panelLayout.createSequentialGroup()
                .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dbname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rememberLoginCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connect_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connect)
                    .addComponent(cancel)))
        );

        dbname.getAccessibleContext().setAccessibleName("dbName");

        javax.swing.GroupLayout mainpanelLayout = new javax.swing.GroupLayout(mainpanel);
        mainpanel.setLayout(mainpanelLayout);
        mainpanelLayout.setHorizontalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbed, javax.swing.GroupLayout.DEFAULT_SIZE, 1057, Short.MAX_VALUE))
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addGap(343, 343, 343)
                .addComponent(connect_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 352, Short.MAX_VALUE))
        );
        mainpanelLayout.setVerticalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connect_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabbed, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabbed.setVisible(false);

        statusBar.setBackground(new java.awt.Color(204, 255, 255));
        statusBar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        statusBar.setAlignmentY(0.0F);
        statusBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        menuBar1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jMenu1.setText("File");

        OpenTraceFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        OpenTraceFile.setText("Open Trace File");
        OpenTraceFile.setToolTipText("Select a trace file for analysis.");
        OpenTraceFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenTraceFileActionPerformed(evt);
            }
        });
        jMenu1.add(OpenTraceFile);

        openTCLFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        openTCLFile.setText("Open TRAFIL Tcl File");
        openTCLFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openTCLFileActionPerformed(evt);
            }
        });
        jMenu1.add(openTCLFile);
        jMenu1.add(jSeparator1);

        Exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        jMenu1.add(Exit);

        menuBar1.add(jMenu1);

        exportToFile.setText("Export");

        exportTraceFile.setText("Export Selected trace file");

        exportToExcell.setText("Excell File");
        exportToExcell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToExcellActionPerformed(evt);
            }
        });
        exportTraceFile.add(exportToExcell);

        exportToTxtFile.setText("Export to text file");
        exportToTxtFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToTxtFileActionPerformed(evt);
            }
        });
        exportTraceFile.add(exportToTxtFile);

        exportToFile.add(exportTraceFile);

        exportSimlutaionInfo.setText("Export Simulation Information");
        exportSimlutaionInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportSimlutaionInfoActionPerformed(evt);
            }
        });
        exportToFile.add(exportSimlutaionInfo);

        exportMetrics.setText("Export Metrics");
        exportMetrics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMetricsActionPerformed(evt);
            }
        });
        exportToFile.add(exportMetrics);

        exportChart.setText("Export Chart");
        exportChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportChartActionPerformed(evt);
            }
        });
        exportToFile.add(exportChart);

        menuBar1.add(exportToFile);

        NsTool.setText("Execute NS2 Simulation");

        openOTCLScript.setText("Open OTcl Script");
        openOTCLScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openOTCLScriptActionPerformed(evt);
            }
        });
        NsTool.add(openOTCLScript);

        menuBar1.add(NsTool);

        setJMenuBar(menuBar1);
        menuBar1.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(0, 0, 1111, 665);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Method that handles the connect to database event. It invokes the
     * TraceFileHandler class perform connection method. If the connection is
     * successful it initializes some of the GUI swing components.
     *
     * @param evt connect command button event
     */
private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        try {

            boolean success;

            String user = username.getText();
            String pa = password.getText();
            String dbName = dbname.getText();
            // Save login info to file
            if (loginCredentials.exists()) {
                loginCredentials.delete();
            }
            if (rememberLoginCheckBox.isSelected()) {
                try {
                    loginCredentials.createNewFile();
                    FileWriter fstream = new FileWriter(loginCredentials);
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(user + "\n" + pa + "\n" + dbName + "\n" + "y");
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, "Saving login info failed.", ex);
                }
            }

            try {
                //logHandler = new FileHandler(getClass().getClassLoader().getResource(".").getPath().replace("%20", " ") + "../../Resources/Utility/trafilLog.txt", true);
                logHandler = new FileHandler(PathLocator.getLogPath(System.getProperty("user.dir")), true);
                //logHandler.setFormatter(new SimpleFormatter());
                logHandler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        if (record.getMessage().contains("START")) {
                            return "\n[" + record.getLevel() + "]  :  "
                                    + record.getSourceClassName() + " -:- "
                                    + record.getSourceMethodName() + " -:- "
                                    + record.getMessage() + "\n";
                        } else {
                            return "[" + record.getLevel() + "]  :  "
                                    + record.getSourceClassName() + " -:- "
                                    + record.getSourceMethodName() + " -:- "
                                    + record.getMessage() + "\n";

                        }

                    }
                });
                Logger.getLogger("").addHandler(logHandler);
                Logger.getLogger(TRAFIL.class.getName()).log(Level.INFO, "START TIME:{0}", NOW());

            } catch (IOException ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            }
            tmh = new TableListener(TraceHandler.getMetaHandler(), TraceFile, TableHan, TraceHandler);

            success = TraceHandler.performConnection(dbName, user, pa, model, statusBar, jTable1, TraceFile, tmh, TableHan);

            if (success) {
                rs = TraceHandler.getResultSet();
                next.setEnabled(false);
                previous.setEnabled(false);
                model.clear();
                chartTypeButtonGroup.add(throughputChart);
                chartTypeButtonGroup.add(throughputBitsChart);
                chartTypeButtonGroup.add(delayJitterChart);
                chartTypeButtonGroup.add(endToEndDelayChart);

                chartEnableButtonGroup.add(enableNodetoNode);
                chartEnableButtonGroup.add(enableNodeSpecific);

                //nodeSelector.removeAll();
                /*
                 * adding the names of the tracefiles to the list of tracefiles
                 */

                while (rs.next()) {
                    input = rs.getString(1);
                    model.addElement(input);
                }
                levels.setSize(100, 50);
                connect_panel.setVisible(false);

                statusBar.setVisible(true);
                m2.setText("");
                menuBar1.setVisible(true);
                tabbed.setVisible(true);

                TableHan.getModel3().addTableModelListener(tmh);

                if (System.getProperty("os.name").contains("Windows") || System.getProperty("os.name").contains("windows")) {
                    NsTool.setVisible(false);
                    tabbed.remove(jPanel9);
                }
            }

        } catch (SQLException e1) {
            JOptionPane.showConfirmDialog(null, "" + e1.getMessage());
            Logger.getLogger(TRAFIL.class.getName()).severe("Error in TRAFIL conncetion to database attempt.");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TRAFIL.class.getName()).severe("Error in TRAFIL conncetion to database attempt.");
        }


}//GEN-LAST:event_connectActionPerformed

    /**
     * It stops the connection to the databases and exits TRAFIL.
     *
     * @param evt cancel command button event
     */
private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?");
        if (response == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_cancelActionPerformed

    /**
     * It closes TRAFIL.
     *
     * @param evt exit button event
     */
private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?");
        if (response == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_ExitActionPerformed

    /**
     * When the open trace file menu item is selected this method handles the
     * opening of a new trace file procedure. It invokes TraceFileHandler class
     * OpenTraceFile method. If the trace file was opened and stored correctly
     * the swing components are informed accordingly.
     *
     * @param evt open trace file menu item action event
     */
private void OpenTraceFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenTraceFileActionPerformed
        fc.setFileFilter(new GenericFileFilter(".tr"));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            try {

                statusBar.setText("Loading:" + fc.getSelectedFile().getName() + "...");
                statusBar.paintImmediately(statusBar.getVisibleRect());

                if (!TraceHandler.OpenTraceFile(fc.getSelectedFile())) {
                    statusBar.setText("No Trace File selected.");
                    statusBar.paintImmediately(statusBar.getVisibleRect());
                    return;
                }
                TableHan.getTestModel().addTableModelListener(tmh);
                model.clear();
                tmh.setSqlupdatequery("");
                rs = TraceHandler.getResultSet();
                while (rs.next()) {
                    input = rs.getString(1);
                    model.addElement(input);
                }
                TraceFile.setInfo(fc.getSelectedFile(), TraceHandler.getSelectedTraceFile());
                statusBar.setText("Calculating General Simulation and Node Information...");
                statusBar.paintImmediately(statusBar.getVisibleRect());
                /*
                 * Clearing all previous information and chart.
                 */
                clearAllInformation();
                chartPanel.removeAll();
                nodeSelector.removeAllItems();
                startNodes.removeAllItems();
                endNodes.removeAllItems();
                levels.removeAllItems();
                chartStartNode.removeAllItems();
                nodeChartStartNode.removeAllItems();
                chartEndNode.removeAllItems();
                chartLevel.removeAllItems();
                next.setEnabled(false);
                previous.setEnabled(false);

                if (TraceHandler.getLineCounter() > 10000) {
                    next.setEnabled(true);
                    m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>0:" + TraceHandler.getCurrentLineCounter() + "<html>");
                } else {
                    m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>0:" + TraceHandler.getCurrentLineCounter() + "<html>");
                }


                //JOptionPane.showMessageDialog(null, "" + TraceHandler.getMetaHandler().getMetaFilePath());
//            if (!TraceHandler.getMetaHandler().getMetaFilePath().contains("normal") && !TraceHandler.getMetaHandler().getMetaFilePath().contains("oldwireless")) {
//                statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
//                simulationInfo.storeMetrics();
//                return;
//            }
                simulationInfo = new GeneralSimulationInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
                if (simulationInfo.storeMetrics()) {
                    updateStandardMetrics();
                } else {
                    Logger.getLogger(TRAFIL.class.getName()).severe("Error in producing-storing standard metrics.");
                    //System.out.println("[" + NOW() + "] Error in producing-storing standard metrics.");
                }

                nodeInfo = new GeneralNodeInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
                for (int i = 0; i < nodeInfo.getNodesIdentifiers().size(); i++) {
                    nodeSelector.addItem(nodeInfo.getNodesIdentifiers().get(i));
                    startNodes.addItem(nodeInfo.getNodesIdentifiers().get(i));
                    endNodes.addItem(nodeInfo.getNodesIdentifiers().get(i));
                    chartStartNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
                    nodeChartStartNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
                    chartEndNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
                }


                metric = new Metrics(TraceHandler.getMetaHandler(), TraceFile);
                graph = new Graph(TraceHandler.getMetaHandler(), TraceFile);
                for (int i = 0; i < metric.getLevels().size(); i++) {
                    levels.addItem(metric.getLevels().get(i));
                    chartLevel.addItem(metric.getLevels().get(i));
                }

                statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");

            } catch (Exception ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            }

        }





    }//GEN-LAST:event_OpenTraceFileActionPerformed

    /**
     * The previous button action event loads the 10000 previous lines of the
     * trace file to the main JTable of TRAFIL.
     *
     * @param evt previous command button action event
     */
private void previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousActionPerformed
        TraceHandler.LoadPreviousTraceFileLines();
        next.setEnabled(true);
        if (TraceHandler.getPageCounter() + 1 == 2) {
            m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>0:" + TraceHandler.getCurrentLineCounter() + "<html>");
            previous.setEnabled(false);
        } else {
            m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>" + (TraceHandler.getPageLineLimit() * (TraceHandler.getPageCounter() - 1) + 1) + ":" + TraceHandler.getCurrentLineCounter() + "<html>");
        }
        if ((TraceHandler.getPageLineLimit() * (TraceHandler.getPageCounter() - 1) + 1) > TraceHandler.getOveralLineCounter()) {
            next.setEnabled(false);
        }
}//GEN-LAST:event_previousActionPerformed

    /**
     * The next button action event loads the next 10000 lines of the trace file
     * to the main JTable of TRAFIL.
     *
     * @param evt next command button action event
     */
private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed

        TraceHandler.LoadNextTraceFileLines();
        previous.setEnabled(true);

        if (TraceHandler.getCurrentLineCounter() > TraceHandler.getOveralLineCounter()) {
            m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>" + (TraceHandler.getPageLineLimit() * (TraceHandler.getPageCounter() - 1) + 1) + ":" + TraceHandler.getOveralLineCounter() + "<html>");
            next.setEnabled(false);
        } else {
            m2.setText("<html><font color=#0033FF size=+1>Lines:</font>" + (TraceHandler.getPageLineLimit() * (TraceHandler.getPageCounter() - 1) + 1) + ":" + TraceHandler.getCurrentLineCounter() + "<html>");
        }
}//GEN-LAST:event_nextActionPerformed

    /**
     * The delete command button deletes the selected trace file from the list
     * of TRAFIL's GUI.
     *
     * @param evt delete command button action event
     */
private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        int[] selectedTclFiles = traceFileList.getSelectedIndices();
        for (int i = (selectedTclFiles.length - 1); i >= 0; i--) {
            Object selectedTcl = traceFileList.getModel().getElementAt(selectedTclFiles[i]);
            TraceHandler.DeleteTraceFile(selectedTcl.toString());
            clearAllInformation();
            Logger.getLogger(TRAFIL.class.getName()).info("Deleted Trace File:" + selectedTcl.toString());
        }

        //System.out.println("[" + NOW() + "] Deleted Trace File:" + selectedTcl.toString());

}//GEN-LAST:event_DeleteActionPerformed

    /**
     * The save changes command button saves any changes made to the trace file
     * through the main JTable of TRAFIL. If the changes are not saved when
     * loading a new trace file these changes will be lost.
     *
     * @param evt
     */
private void savechangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savechangesActionPerformed
        try {
            TraceHandler.saveChanges();


        } catch (SQLException ex) {
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_savechangesActionPerformed

    /**
     * When the select command button is fired the selected trace file from the
     * list is loaded into TRAFIL from the database and all the necessary swing
     * components are updated.
     *
     * @param evt select command button action event
     */
private void selecttracefileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selecttracefileActionPerformed

        Object sfile = traceFileList.getSelectedValue();
        statusBar.setText("Loading trace file: " + sfile.toString());
        statusBar.paintImmediately(statusBar.getVisibleRect());
        if (!TraceHandler.loadSelectedTraceFile(sfile.toString())) {
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, "Error in loading {0}", sfile.toString());
            //System.out.println("[" + NOW() + "] Error in loading " + sfile.toString());
            return;
        }
        TraceFile.setInfo(null, sfile.toString());
        chartPanel.removeAll();
        simulationInfo = new GeneralSimulationInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
        nodeSelector.removeAllItems();
        startNodes.removeAllItems();
        endNodes.removeAllItems();
        levels.removeAllItems();
        chartStartNode.removeAllItems();
        nodeChartStartNode.removeAllItems();
        chartEndNode.removeAllItems();
        chartLevel.removeAllItems();
        nodeInfo = new GeneralNodeInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
        metric = new Metrics(TraceHandler.getMetaHandler(), TraceFile);
        graph = new Graph(TraceHandler.getMetaHandler(), TraceFile);
        for (int i = 0; i < nodeInfo.getNodesIdentifiers().size(); i++) {
            nodeSelector.addItem(nodeInfo.getNodesIdentifiers().get(i));
            startNodes.addItem(nodeInfo.getNodesIdentifiers().get(i));
            endNodes.addItem(nodeInfo.getNodesIdentifiers().get(i));
            chartStartNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
            nodeChartStartNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
            chartEndNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
        }
        for (int i = 0; i < metric.getLevels().size(); i++) {
            levels.addItem(metric.getLevels().get(i));
            chartLevel.addItem(metric.getLevels().get(i));
        }
        TableHan.getTestModel().addTableModelListener(tmh);
        next.setEnabled(false);
        previous.setEnabled(false);
        if (TraceHandler.getOveralLineCounter() > 10000) {
            next.setEnabled(true);
        }
        simulationInfo.retrieveMetrics();
        updateStandardMetrics();
        m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>0:" + TraceHandler.getCurrentLineCounter() + "</html>");
        statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
        statusBar.paintImmediately(statusBar.getVisibleRect());
        //int response2 =JOptionPane.showConfirmDialog(null,"After select:"+jTable1.getRowCount()+" "+model3.getRowCount());
}//GEN-LAST:event_selecttracefileActionPerformed

    /**
     * When a node is selected from the node selector combo box of TRAFIL's
     * Simulation Information tab, this method calls the produce metrics method
     * of GeneralNodeInformation class to produce the metrics for that node.
     *
     * @param evt node selector combo box action event
     */
private void nodeSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeSelectorActionPerformed
        if (nodeSelector.getSelectedItem() != null) {
            Logger.getLogger(TRAFIL.class.getName()).info("Trying to create Node information for node:" + nodeSelector.getSelectedItem());
            //System.out.println("[" + NOW() + "] Trying to create Node information for node:" + nodeSelector.getSelectedItem());
            if (!nodeInfo.produceMetrics(((Integer) nodeSelector.getSelectedItem()))) {
                Logger.getLogger(TRAFIL.class.getName()).severe("Error in creating Node information for node:" + nodeSelector.getSelectedItem());
                //System.out.println("[" + NOW() + "] Error in creating Node information for node:" + nodeSelector.getSelectedItem());
            } else {
                //Logger.getLogger(mainFrame.class.getName()).addHandler(logHandler);
                //Logger.getLogger(mainFrame.class.getName()).setUseParentHandlers(false);
                Logger.getLogger(TRAFIL.class.getName()).info("Success in creating Node information for node:" + nodeSelector.getSelectedItem());
                //System.out.println("[" + NOW() + "] Success in creating Node information for node:" + nodeSelector.getSelectedItem());

            }
            updateNodeInformation();
        }


}//GEN-LAST:event_nodeSelectorActionPerformed

    /**
     * When the calculate button is pressed in TRAFIL's Metrics tab, this method
     * calculates the metrics regarding the selected trace file by invoking the
     * calculate metrics method of Metrics class and then updates the necessary
     * swing components.
     *
     * @param evt calculate command button event
     */
private void calculateMetricsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateMetricsActionPerformed

        if ((TraceFile.getTraceFileName() != null) && (metric.calculateMetrics((Integer) startNodes.getSelectedItem(), (Integer) endNodes.getSelectedItem(), (String) levels.getSelectedItem()))) {
            //metric.calculateMetrics((Integer) startNodes.getSelectedItem(), (Integer) endNodes.getSelectedItem(), (String) levels.getSelectedItem());
            statusBar.setText("<html><font color=#0033FF>Selected Trace File:" + TraceFile.getTraceFileName() + "...</font></html>");
            metricsTable.getModel().setValueAt(metric.getThroughput(), 0, 1);
            metricsTable.getModel().setValueAt(metric.getThroughputBits(), 1, 1);
            metricsTable.getModel().setValueAt(metric.getMinEndToEndDelay(), 2, 1);
            metricsTable.getModel().setValueAt(metric.getMaxEndToEndDealy(), 3, 1);
            metricsTable.getModel().setValueAt(metric.getAverageEndToEndDelay(), 4, 1);
            metricsTable.getModel().setValueAt(metric.getDelayJitter(), 5, 1);
            metricsTable.getModel().setValueAt(metric.getMinDelayJitter(), 6, 1);
            metricsTable.getModel().setValueAt(metric.getMaxDelayJitter(), 7, 1);
            metricsTable.getModel().setValueAt(metric.getAverageDelayJitter(), 8, 1);
            metricsTable.getModel().setValueAt(metric.getPacketLossRatio(), 9, 1);
            Logger.getLogger(TRAFIL.class.getName()).info("Success in creating metrics for trace file: " + TraceFile.getTraceFileName());
            //System.out.println("[" + NOW() + "] Success in creating metrics for trace file: " + TraceFile.getTraceFileName());
        } else {
            Logger.getLogger(TRAFIL.class.getName()).severe("Error in creating metrics for trace file: " + TraceFile.getTraceFileName());
            //System.out.println("[" + NOW() + "] Error in creating metrics for trace file: " + TraceFile.getTraceFileName());
        }
}//GEN-LAST:event_calculateMetricsActionPerformed

    /**
     * When the export to excel menu item is selected this method handles the
     * creation of the excel file that will contain the data of the selected
     * trace file by invoking the exportToExcell method of the ExportData class.
     *
     * @param evt
     */
private void exportToExcellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToExcellActionPerformed
        exporter = new ExportData(TraceFile, TraceHandler.getStatement(), TraceHandler.getMetaHandler());
        if (exporter.exportToExcell()) {
            JOptionPane.showMessageDialog(null, "Succesfully exported the trace file.");
        }

}//GEN-LAST:event_exportToExcellActionPerformed

    /**
     * When the export to text file menu item is selected this method handles
     * the creation of the text file that will contain the data of the selected
     * trace file by invoking the exportToTextFile method of the ExportData
     * class.
     *
     * @param evt
     */
private void exportToTxtFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToTxtFileActionPerformed
        exporter = new ExportData(TraceFile, TraceHandler.getStatement(), TraceHandler.getMetaHandler());
        if (exporter.exportToTextFile()) {
            JOptionPane.showMessageDialog(null, "Succesfully exported the trace file.");
        }

}//GEN-LAST:event_exportToTxtFileActionPerformed

    /**
     * When the export simulation information menu item is selected this method
     * handles the creation of the text file that will contain the simulation
     * information of the selected trace file by invoking the
     * exportSimulationInformation method of the ExportData class.
     *
     * @param evt
     */
private void exportSimlutaionInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportSimlutaionInfoActionPerformed
        exporter = new ExportData(TraceFile, TraceHandler.getStatement(), TraceHandler.getMetaHandler());
        if (exporter.exportSimulationInformation(simulationInfo)) {
            JOptionPane.showMessageDialog(null, "Succesfully exported General Simulation Information.");
        }

}//GEN-LAST:event_exportSimlutaionInfoActionPerformed

    /**
     * When the export metrics menu item is selected this method handles the
     * creation of the text file that will contain the metrics of the selected
     * trace file by invoking the exportMetrics method of the ExportData class.
     *
     * @param evt
     */
private void exportMetricsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMetricsActionPerformed
        exporter = new ExportData(TraceFile, TraceHandler.getStatement(), TraceHandler.getMetaHandler());
        if (exporter.exportMetrics(metric)) {
            JOptionPane.showMessageDialog(null, "Succesfully exported Metrics.");
        }
}//GEN-LAST:event_exportMetricsActionPerformed

    /**
     * When the create chart command button in the Charts tab of TRAFIL is
     * fired, this method handles the creation of the chart by invoking the
     * suitable method of the Graph class.
     *
     * @param evt
     */
private void createChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createChartActionPerformed

        Chart.resetGraph();
        if (TraceFile.getTraceFileName() != null) {

            if (enableNodetoNode.isSelected()) {
                statusBar.setText("Creating chart between nodes " + chartStartNode.getSelectedItem() + " and " + chartEndNode.getSelectedItem());
                statusBar.paintImmediately(statusBar.getVisibleRect());
                if (throughputChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputChart.getText(), lineTitle.getText());
                } else if (delayJitterChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), delayJitterChart.getText(), lineTitle.getText());
                } else if (endToEndDelayChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), endToEndDelayChart.getText(), lineTitle.getText());
                } else if (throughputBitsChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputBitsChart.getText(), lineTitle.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "You have not selected a chart type.");
                    statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
                    statusBar.paintImmediately(statusBar.getVisibleRect());
                    return;
                }
            } else if (enableNodeSpecific.isSelected()) {
                statusBar.setText("Creating chart for node " + nodeChartStartNode.getSelectedItem());
                statusBar.paintImmediately(statusBar.getVisibleRect());
                if (throughputChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputChart.getText(), lineTitle.getText());
                } else if (delayJitterChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), delayJitterChart.getText(), lineTitle.getText());
                } else if (endToEndDelayChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), endToEndDelayChart.getText(), lineTitle.getText());
                } else if (throughputBitsChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputBitsChart.getText(), lineTitle.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "You have not selected a chart type.");
                    statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
                    statusBar.paintImmediately(statusBar.getVisibleRect());
                    return;
                }
            }

            chartPanel.setLayout(new java.awt.BorderLayout());
            chartPanel.removeAll();
            chartPanel.add(graph.getGraph());
            chartPanel.revalidate();
        } else {
            statusBar.setText("<html><font color=#0033FF>No Trace File Selected.</font></html>");
            statusBar.paintImmediately(statusBar.getVisibleRect());
        }
}//GEN-LAST:event_createChartActionPerformed

    /**
     * When the export chart menu item is selected this method handles the
     * creation of the jpeg file that will contain the data of the selected
     * trace file by invoking the exportChart method of the ExportData class.
     *
     * @param evt
     */
private void exportChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportChartActionPerformed
        exporter = new ExportData(TraceFile, TraceHandler.getStatement(), TraceHandler.getMetaHandler());
        if (throughputChart.isSelected()) {
            exporter.exportChart(graph.getChart(), throughputChart.getText());
        } else if (throughputBitsChart.isSelected()) {
            exporter.exportChart(graph.getChart(), throughputBitsChart.getText());
        } else if (delayJitterChart.isSelected()) {
            exporter.exportChart(graph.getChart(), delayJitterChart.getText());
        } else if (endToEndDelayChart.isSelected()) {
            exporter.exportChart(graph.getChart(), "EndToEndDelay");
        } else {
            JOptionPane.showMessageDialog(null, "You have not selected a chart type.");
        }
}//GEN-LAST:event_exportChartActionPerformed

    /**
     * This method is invoked when the Run NS code menu item is fired. It
     * creates a file chooser that enables the input of a tcl file.The tcl
     * script is executed by TRAFIL using executeNS2Simulation method and the
     * resulted trace is passed to the TraceFileHandler's class openTraceFile
     * method to store the trace file. Finally the information of the trace file
     * is calculated.
     *
     * @param evt
     */
    private void openOTCLScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openOTCLScriptActionPerformed
        fc.setFileFilter(new GenericFileFilter(".tcl"));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String path = fc.getSelectedFile().getCanonicalPath();
                executeNS2Simulation(path);
            } catch (IOException ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_openOTCLScriptActionPerformed

    public void executeNS2Simulation(String path) {
        try {
            String tempString = statusBar.getText();
            statusBar.setText("Running Ns with file:" + path + "...");
            statusBar.paintImmediately(statusBar.getVisibleRect());
            NetworkSimulator ns = new NetworkSimulator(path);
            ns.executeSimulation();
            File createdTraceFile = ns.getCreatedTraceFile();
            if (createdTraceFile == null) {
                statusBar.setText(tempString);
                statusBar.paintImmediately(statusBar.getVisibleRect());
                return;
            }
            System.out.println("Simulation complete.");
            statusBar.setText("Loading:" + createdTraceFile.getName() + "...");
            statusBar.paintImmediately(statusBar.getVisibleRect());

            if (!TraceHandler.OpenTraceFile(createdTraceFile)) {
                statusBar.setText(tempString);
                statusBar.paintImmediately(statusBar.getVisibleRect());
                return;
            }
            TableHan.getTestModel().addTableModelListener(tmh);
            model.clear();
            tmh.setSqlupdatequery("");
            rs = TraceHandler.getResultSet();
            while (rs.next()) {
                input = rs.getString(1);
                model.addElement(input);
            }
            TraceFile.setInfo(createdTraceFile, TraceHandler.getSelectedTraceFile());
            statusBar.setText("Calculating General Simulation and Node Information...");
            statusBar.paintImmediately(statusBar.getVisibleRect());
            /*
             * Clearing all previous information and chart.
             */
            clearAllInformation();
            chartPanel.removeAll();
            nodeSelector.removeAllItems();
            startNodes.removeAllItems();
            endNodes.removeAllItems();
            levels.removeAllItems();
            chartStartNode.removeAllItems();
            chartEndNode.removeAllItems();
            chartLevel.removeAllItems();
            next.setEnabled(false);
            previous.setEnabled(false);

            if (TraceHandler.getLineCounter() > 10000) {
                next.setEnabled(true);
                m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>0:" + TraceHandler.getCurrentLineCounter() + "<html>");
            } else {
                m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>0:" + TraceHandler.getCurrentLineCounter() + "<html>");
            }


            //JOptionPane.showMessageDialog(null, "" + TraceHandler.getMetaHandler().getMetaFilePath());
            if (!TraceHandler.getMetaHandler().getMetaFilePath().contains("normal") && !TraceHandler.getMetaHandler().getMetaFilePath().contains("oldwireless")) {
                statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
                simulationInfo.storeMetrics();
                return;
            }
            simulationInfo = new GeneralSimulationInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
            if (simulationInfo.storeMetrics()) {
                updateStandardMetrics();
            } else {
                Logger.getLogger(TRAFIL.class.getName()).severe("Error in producing-storing standard metrics.");
                //System.out.println("[" + NOW() + "] Error in producing-storing standard metrics.");
            }

            nodeInfo = new GeneralNodeInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
            for (int j = 0; j < nodeInfo.getNodesIdentifiers().size(); j++) {
                nodeSelector.addItem(nodeInfo.getNodesIdentifiers().get(j));
                startNodes.addItem(nodeInfo.getNodesIdentifiers().get(j));
                endNodes.addItem(nodeInfo.getNodesIdentifiers().get(j));
                chartStartNode.addItem(nodeInfo.getNodesIdentifiers().get(j));
                chartEndNode.addItem(nodeInfo.getNodesIdentifiers().get(j));
            }


            metric = new Metrics(TraceHandler.getMetaHandler(), TraceFile);
            graph = new Graph(TraceHandler.getMetaHandler(), TraceFile);
            for (int j = 0; j < metric.getLevels().size(); j++) {
                levels.addItem(metric.getLevels().get(j));
                chartLevel.addItem(metric.getLevels().get(j));
            }
            if (!ns.getSimulationMessage().isEmpty()) {
                SimulationResultPresenter simulationResults = new SimulationResultPresenter(this, null, PathLocator.getNetworkSimulationOutputsPath(System.getProperty("user.dir")));
                simulationResults.setSimulationOuput(ns.getSimulationMessage());
            }

            statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "TRAFIL could not find the Ns application.\n"
                    + "Please make sure that Ns is installed in the system\n"
                    + "or else that the Ns executable is located in the directory\n"
                    + "/usr/bin",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            ex.printStackTrace(System.err);
        }
    }

private void enableNodeSpecificActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableNodeSpecificActionPerformed
        //throughputBitsChart.setVisible(false);
        //delayJitterChart.setVisible(false);
        //endToEndDelayChart.setVisible(false);
}//GEN-LAST:event_enableNodeSpecificActionPerformed

private void executeSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeSQLActionPerformed
        queryResultArea.setText("");
        QueryHandler queryHandler = new QueryHandler();
        ArrayList<String> queryResult = queryHandler.executeQuery(sqlQueryArea.getText());
        for (int i = 0; i < queryResult.size(); i++) {
            queryResultArea.append(queryResult.get(i));
        }

}//GEN-LAST:event_executeSQLActionPerformed

private void clearSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSQLActionPerformed
        sqlQueryArea.setText("");
}//GEN-LAST:event_clearSQLActionPerformed

private void enableNodetoNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableNodetoNodeActionPerformed
        throughputBitsChart.setVisible(true);
        delayJitterChart.setVisible(true);
        endToEndDelayChart.setVisible(true);
}//GEN-LAST:event_enableNodetoNodeActionPerformed

    private void DesignQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DesignQueryButtonActionPerformed

        design = new DesignWindow();
        design.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                //JOptionPane.showMessageDialog(null, design.getQuery());
                sqlQueryArea.setText(design.getQuery());
            }
        });

    }//GEN-LAST:event_DesignQueryButtonActionPerformed

    private void browseRawVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseRawVideoActionPerformed

        JFileChooser rawVideoChooser = new JFileChooser();
        rawVideoChooser.setFileFilter(new GenericFileFilter(".yuv"));
        int returnVal = rawVideoChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                rawVideoPath.setText(rawVideoChooser.getSelectedFile().getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_browseRawVideoActionPerformed

    private void browseSimulationScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseSimulationScriptActionPerformed
        JFileChooser scriptChooser = new JFileChooser();
        scriptChooser.setFileFilter(new GenericFileFilter(".tcl"));
        int returnVal = scriptChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                simulationScriptPath.setText(scriptChooser.getSelectedFile().getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_browseSimulationScriptActionPerformed

    private void executePreAndSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executePreAndSimulationActionPerformed
        try {
            String currentMessage = statusBar.getText();
            statusBar.setText("Running Evalvid Simulation...");
            statusBar.paintImmediately(statusBar.getVisibleRect());
            String externalToolsPath = PathLocator.getExternalToolsPath(System.getProperty("user.dir"));
            Map<String, String> simulationInformation = new HashMap<String, String>();
            Map<String, ArrayList<File>> outputs = null;
            String videoPath = new String("\"" + rawVideoPath.getText() + "\"");

            simulationInformation.put("videoName", "akiyo_cif.yuv");
            simulationInformation.put("tclScript", PathLocator.validatePath(simulationScriptPath.getText()));
            simulationInformation.put("videoPath", PathLocator.validatePath(videoPath));
            simulationInformation.put("ffmpegPath", PathLocator.validatePath(externalToolsPath + "ffmpeg.exe"));
            simulationInformation.put("mp4Path", PathLocator.validatePath(externalToolsPath + "MP4.exe"));
            simulationInformation.put("inVideoSize", (String) inputVideoSize.getSelectedItem());
            simulationInformation.put("outVideoCodec", (String) outputVideoCodec.getSelectedItem());
            simulationInformation.put("outGOP", outputGroupOfPictures.getText());
            simulationInformation.put("videoPath", PathLocator.validatePath(rawVideoPath.getText()));
            simulationInformation.put("outQualityStart", qualityScaleStartValue.getText());
            simulationInformation.put("outQualityEnd", qualityScaleEndValue.getText());
            simulationInformation.put("outVideoSize", (String) outputVideoSize.getSelectedItem());
            simulationInformation.put("outFrameRate", outputFrameRate.getText());
            simulationInformation.put("mp4IpAddr", mp4IpAddress.getText());
            simulationInformation.put("mp4Port", mp4PortNumber.getText());
            simulationInformation.put("mp4FrameRate", mp4Fps.getText());
            simulationInformation.put("mp4MTU", mp4MTU.getText());
            simulationInformation.put("inFrameRate", inputFrameRate.getText());
            simulationInformation.put("ffmpegFileName", ffmpegVideoName.getText());
            simulationInformation.put("mp4FileName", mp4OutputFileName.getText());
            simulationInformation.put("senderPrefix", senderPrefix.getText());
            simulationInformation.put("receiverPrefix", receiverPrefix.getText());

            VideoSimulator videoSimulator = new VideoSimulator(simulationInformation);
            if (videoSimulator.executeSimulation()) {
                outputs = videoSimulator.getSimulationOutputs();
                updateVideoSimulationResults(outputs);
            } else {
                JOptionPane.showMessageDialog(null, "Video Simulation was not executed succesfully.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.print("Error executing Simulation");
            }
            //JOptionPane.showMessageDialog(Exit, "Executed Script");
            if (!videoSimulator.getSimulationMessage().isEmpty()) {
                SimulationResultPresenter videoResults = new SimulationResultPresenter(this, outputs, PathLocator.getSimulationOutputsPath(System.getProperty("user.dir")));
                videoResults.setSimulationOuput(videoSimulator.getSimulationMessage());
            }
            statusBar.setText(currentMessage);
            statusBar.paintImmediately(statusBar.getVisibleRect());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "TRAFIL could not execute the simulation.\n", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(System.err);
        }
    }//GEN-LAST:event_executePreAndSimulationActionPerformed

    private void executepostSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executepostSimulationActionPerformed
        try {
            String currentMessage = statusBar.getText();
            statusBar.setText("Running Evalvid Post Simulation...");
            statusBar.paintImmediately(statusBar.getVisibleRect());
            String externalToolsPath = PathLocator.getExternalToolsPath(System.getProperty("user.dir"));
            Map<String, String> simulationInformation = new HashMap<String, String>();
            //Properties prop = new Properties();
            simulationInformation.put("senderFile", (String) senderFileSelect.getSelectedItem());
            simulationInformation.put("receiverFile", (String) receiverFileSelect.getSelectedItem());
            simulationInformation.put("mp4TraceFile", (String) mp4TraceSelect.getSelectedItem());
            simulationInformation.put("dataFile1", (String) videoDataFile1.getSelectedItem());
            simulationInformation.put("dataFile2", (String) videoDataFile2.getSelectedItem());
            simulationInformation.put("et_raVideoOutputFile", postSimulationVideoOutputName.getText());
            simulationInformation.put("postSimulationFrameRate", postSimulationFrameRate.getText());
            simulationInformation.put("postSimulationVideoCodec", (String) postSimulationVideoCodec.getSelectedItem());
            simulationInformation.put("ffmpegPostSimulationOutputVideoName", postSimulationOutputVideoName.getText());
            simulationInformation.put("psnrVideoWidth", "352");
            simulationInformation.put("psnrVideoHeight", "288");
            simulationInformation.put("et_raPath", PathLocator.validatePath(externalToolsPath + "et_ra.exe"));
            simulationInformation.put("psnrPath", PathLocator.validatePath(externalToolsPath + "psnr.exe"));

            VideoPostSimulator videoPostSimulator = new VideoPostSimulator(simulationInformation);

            if (videoPostSimulator.executeSimulation()) {
                System.out.print("Executed Post Simulation");
                JOptionPane.showMessageDialog(null, "Results extracted succesfully.", "TRAFIL message", JOptionPane.PLAIN_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(null, "Results were not extraceted succesfully.", "Error.", JOptionPane.ERROR_MESSAGE);
                System.out.print("Error executing Post Simulation");
            }
            SimulationResultPresenter videoResults = new SimulationResultPresenter(this, null, PathLocator.getSimulationOutputsPath(System.getProperty("user.dir")));
            videoResults.setSimulationOuput(videoPostSimulator.getSimulationMessage());

            statusBar.setText(currentMessage);
            statusBar.paintImmediately(statusBar.getVisibleRect());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }//GEN-LAST:event_executepostSimulationActionPerformed

    private void dbnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dbnameKeyReleased
        if (evt.getKeyCode() == 10) {
            connect.doClick();
        }

    }//GEN-LAST:event_dbnameKeyReleased

    private void connect_panelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_connect_panelKeyReleased
        if (evt.getKeyCode() == 10) {
            connect.doClick();
        }
    }//GEN-LAST:event_connect_panelKeyReleased

    private void usernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameKeyReleased
        if (evt.getKeyCode() == 10) {
            connect.doClick();
        }
    }//GEN-LAST:event_usernameKeyReleased

    private void passwordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyReleased
        if (evt.getKeyCode() == 10) {
            connect.doClick();
        }
    }//GEN-LAST:event_passwordKeyReleased

    private void senderFileSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_senderFileSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_senderFileSelectActionPerformed

    private void newWiredNodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWiredNodeButtonActionPerformed
        newWirelessNodeButton.setSelected(false);
        newLinkButton.setSelected(false);
    }//GEN-LAST:event_newWiredNodeButtonActionPerformed

    private void chartStartNodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chartStartNodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chartStartNodeActionPerformed

    private void simParamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simParamButtonActionPerformed
        simProperties.setVisible(true);
    }//GEN-LAST:event_simParamButtonActionPerformed

    private void linkListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkListButtonActionPerformed
        linkWindow.setVisible(true);
    }//GEN-LAST:event_linkListButtonActionPerformed

    private void newWirelessNodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWirelessNodeButtonActionPerformed
        newWiredNodeButton.setSelected(false);
        newLinkButton.setSelected(false);
    }//GEN-LAST:event_newWirelessNodeButtonActionPerformed

    private void newLinkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newLinkButtonActionPerformed
        newWirelessNodeButton.setSelected(false);
        newWiredNodeButton.setSelected(false);
    }//GEN-LAST:event_newLinkButtonActionPerformed

    private void connectedAgentsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectedAgentsButtonActionPerformed
        connectedAgents.setVisible(true);
    }//GEN-LAST:event_connectedAgentsButtonActionPerformed

    private void wirelessSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wirelessSettingsButtonActionPerformed
        wirelessWindow.setVisible(true);
    }//GEN-LAST:event_wirelessSettingsButtonActionPerformed

    private void openTCLFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openTCLFileActionPerformed
        fc.setFileFilter(new GenericFileFilter(".tcl"));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            try {

                statusBar.setText("Loading: " + fc.getSelectedFile().getName() + "...");
                statusBar.paintImmediately(statusBar.getVisibleRect());

                tclDesigner.resetPanel();
                if (!TclFileLoader.OpenTclFile(fc.getSelectedFile())) {
                    statusBar.setText("No Tcl file selected.");
                    statusBar.paintImmediately(statusBar.getVisibleRect());
                    return;
                }

                tclDesigner.setWiredNodeList(TclFileLoader.getWiredNodeList());
                tclDesigner.setWirelessNodeList(TclFileLoader.getWirelessNodeList());
                tclDesigner.setLinkList(TclFileLoader.getLinkList());
                DefaultTableModel connectedAgentsModel = TclDesignerPanel.getConnectedAgentsModel();
                for (String[] str : TclFileLoader.getConnectedAgentsList()) {
                    System.out.println("Sending: " + str[0] + " Receiving: " + str[1]);
                    for (int row = 0; row < connectedAgentsModel.getRowCount(); row++) {
                        if (connectedAgentsModel.getValueAt(row, 0).equals(str[0])) {
                            connectedAgentsModel.setValueAt(str[1], row, 2);
                        }
                    }
                }
                wirelessWindow.loadSettings(TclFileLoader.getWirelessSettings());
                simProperties.setOutputFileField(TclFileLoader.getScriptFileName());
                simProperties.setSimEndField(TclFileLoader.getScriptFinishTime());
                statusBar.setText("<html><font color=#0033FF>Opened Tcl file!</font></html>");
                statusBar.paintImmediately(statusBar.getVisibleRect());
                tabbed.setSelectedComponent(jPanel19);

            } catch (Exception ex) {
                Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_openTCLFileActionPerformed

    private void loadMoreTracesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMoreTracesButtonActionPerformed

        Object sfile = availableTraceFiles.getSelectedValue();
        statusBar.setText("Loading trace file: " + sfile.toString());
        statusBar.paintImmediately(statusBar.getVisibleRect());
        if (!TraceHandler.loadSelectedTraceFile(sfile.toString())) {
            Logger.getLogger(TRAFIL.class.getName()).severe("Error in loading " + sfile.toString());
            //System.out.println("[" + NOW() + "] Error in loading " + sfile.toString());
            return;
        }
        TraceFile.setInfo(null, sfile.toString());
        chartPanel.removeAll();
        simulationInfo = new GeneralSimulationInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
        nodeSelector.removeAllItems();
        startNodes.removeAllItems();
        endNodes.removeAllItems();
        levels.removeAllItems();
        chartStartNode.removeAllItems();
        nodeChartStartNode.removeAllItems();
        chartEndNode.removeAllItems();
        chartLevel.removeAllItems();
        nodeInfo = new GeneralNodeInformation(TraceHandler.getMetaHandler(), TraceHandler.getStatement(), TraceFile);
        metric = new Metrics(TraceHandler.getMetaHandler(), TraceFile);
        graph = new Graph(TraceHandler.getMetaHandler(), TraceFile);
        for (int i = 0; i < nodeInfo.getNodesIdentifiers().size(); i++) {
            nodeSelector.addItem(nodeInfo.getNodesIdentifiers().get(i));
            startNodes.addItem(nodeInfo.getNodesIdentifiers().get(i));
            endNodes.addItem(nodeInfo.getNodesIdentifiers().get(i));
            chartStartNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
            nodeChartStartNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
            chartEndNode.addItem(nodeInfo.getNodesIdentifiers().get(i));
        }
        for (int i = 0; i < metric.getLevels().size(); i++) {
            levels.addItem(metric.getLevels().get(i));
            chartLevel.addItem(metric.getLevels().get(i));
        }
        TableHan.getTestModel().addTableModelListener(tmh);
        next.setEnabled(false);
        previous.setEnabled(false);
        if (TraceHandler.getOveralLineCounter() > 10000) {
            next.setEnabled(true);
        }
        simulationInfo.retrieveMetrics();
        updateStandardMetrics();
        m2.setText("<html><font color=#0033FF size=+1>Showing Lines:</font>0:" + TraceHandler.getCurrentLineCounter() + "</html>");
        statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
        statusBar.paintImmediately(statusBar.getVisibleRect());
    }//GEN-LAST:event_loadMoreTracesButtonActionPerformed

    private void addMoreGraphsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMoreGraphsButtonActionPerformed

        if (TraceFile.getTraceFileName() != null) {
            if (enableNodetoNode.isSelected()) {
                statusBar.setText("Creating chart between nodes " + chartStartNode.getSelectedItem() + " and " + chartEndNode.getSelectedItem());
                statusBar.paintImmediately(statusBar.getVisibleRect());
                if (throughputChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputChart.getText(), lineTitle.getText());
                } else if (delayJitterChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), delayJitterChart.getText(), lineTitle.getText());
                } else if (endToEndDelayChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), endToEndDelayChart.getText(), lineTitle.getText());
                } else if (throughputBitsChart.isSelected()) {
                    graph.createNodeToNodeChart((Integer) chartStartNode.getSelectedItem(), (Integer) chartEndNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputBitsChart.getText(), lineTitle.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "You have not selected a chart type.");
                    statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
                    statusBar.paintImmediately(statusBar.getVisibleRect());
                    return;
                }
            } else if (enableNodeSpecific.isSelected()) {
                statusBar.setText("Creating chart for node " + nodeChartStartNode.getSelectedItem());
                statusBar.paintImmediately(statusBar.getVisibleRect());
                if (throughputChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputChart.getText(), lineTitle.getText());
                } else if (delayJitterChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), delayJitterChart.getText(), lineTitle.getText());
                } else if (endToEndDelayChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), endToEndDelayChart.getText(), lineTitle.getText());
                } else if (throughputBitsChart.isSelected()) {
                    graph.createNodeSpecificChart((Integer) nodeChartStartNode.getSelectedItem(), (String) chartLevel.getSelectedItem(), Integer.parseInt(samplingRateSelector.getSelectedItem().toString()), throughputBitsChart.getText(), lineTitle.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "You have not selected a chart type.");
                    statusBar.setText("<html><font color=#0033FF>Selected Trace File: " + TraceFile.getTraceFileName() + "...</font></html>");
                    statusBar.paintImmediately(statusBar.getVisibleRect());
                    return;
                }
            }

//		chartPanel.setLayout(new java.awt.BorderLayout());
//		chartPanel.removeAll();
            chartPanel.add(graph.getGraph());
            chartPanel.revalidate();
        } else {
            statusBar.setText("<html><font color=#0033FF>You have not selected a trace file.</font></html>");
            statusBar.paintImmediately(statusBar.getVisibleRect());
        }
    }//GEN-LAST:event_addMoreGraphsButtonActionPerformed

    private void resetTclDesignButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetTclDesignButtonActionPerformed
        tclDesigner.resetPanel();
    }//GEN-LAST:event_resetTclDesignButtonActionPerformed

    private void chartTitleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chartTitleFocusLost
        Chart.setChartTitle(chartTitle.getText());
    }//GEN-LAST:event_chartTitleFocusLost

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                TraceHandler = new TracefileHandler();
                TclFileLoader = new TclFileLoader();
                TraceFile = new TraceFileInfo();
                TableHan = new TableHandler();
                MetaHandler = new MetaDataHandler();
                new TRAFIL().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Delete;
    private javax.swing.JMenuItem Exit;
    private javax.swing.JMenu NsTool;
    private javax.swing.JMenuItem OpenTraceFile;
    private javax.swing.JButton addMoreGraphsButton;
    private javax.swing.JList availableTraceFiles;
    private javax.swing.JLabel averagePacketSize;
    private javax.swing.JLabel averagePacketSize1;
    private javax.swing.JButton browseRawVideo;
    private javax.swing.JButton browseSimulationScript;
    private javax.swing.JButton calculateMetrics;
    private javax.swing.JButton cancel;
    private javax.swing.JComboBox chartEndNode;
    private javax.swing.JComboBox chartLevel;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JPanel chartParameters;
    private javax.swing.JComboBox chartStartNode;
    private javax.swing.JTextField chartTitle;
    private javax.swing.ButtonGroup chartTypeButtonGroup;
    private javax.swing.JButton clearRawVideoPath;
    private javax.swing.JButton clearSQL;
    private javax.swing.JButton clearSimulationScriptPath;
    private javax.swing.JButton connect;
    public javax.swing.JPanel connect_panel;
    private javax.swing.JButton connectedAgentsButton;
    private javax.swing.JButton createChart;
    private javax.swing.JTextField dbname;
    private javax.swing.JRadioButton delayJitterChart;
    private javax.swing.JScrollPane designScrollPanel;
    private javax.swing.JRadioButton enableNodeSpecific;
    private javax.swing.JRadioButton enableNodetoNode;
    private javax.swing.JComboBox endNodes;
    private javax.swing.JLabel endTime;
    private javax.swing.JLabel endTime1;
    private javax.swing.JRadioButton endToEndDelayChart;
    private javax.swing.JButton executePreAndSimulation;
    private javax.swing.JButton executeSQL;
    private javax.swing.JButton executepostSimulation;
    private javax.swing.JMenuItem exportChart;
    private javax.swing.JMenuItem exportMetrics;
    private javax.swing.JMenuItem exportSimlutaionInfo;
    private javax.swing.JMenuItem exportToExcell;
    private javax.swing.JMenu exportToFile;
    private javax.swing.JMenuItem exportToTxtFile;
    private javax.swing.JMenu exportTraceFile;
    private javax.swing.JTextField ffmpegVideoName;
    private javax.swing.JTextField inputFrameRate;
    private javax.swing.JComboBox inputVideoSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox levels;
    private javax.swing.JTextField lineTitle;
    private javax.swing.JButton linkListButton;
    private javax.swing.JButton loadMoreTracesButton;
    public javax.swing.JLabel m2;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JLabel maximumPacketSize;
    private javax.swing.JLabel maximumPacketSize1;
    public javax.swing.JMenuBar menuBar1;
    private javax.swing.JTable metricsTable;
    private javax.swing.JLabel minimumPacketSize;
    private javax.swing.JLabel minimumPacketSize1;
    private javax.swing.JTextField mp4Fps;
    private javax.swing.JTextField mp4IpAddress;
    private javax.swing.JTextField mp4MTU;
    private javax.swing.JTextField mp4OutputFileName;
    private javax.swing.JTextField mp4PortNumber;
    private javax.swing.JComboBox mp4TraceSelect;
    private javax.swing.JToggleButton newLinkButton;
    private javax.swing.JToggleButton newWiredNodeButton;
    private javax.swing.JToggleButton newWirelessNodeButton;
    private javax.swing.JButton next;
    private javax.swing.JLabel nodeAGTGeneratedBytes;
    private javax.swing.JLabel nodeAGTGeneratedPackets;
    private javax.swing.JLabel nodeAGTReceivedBytes;
    private javax.swing.JLabel nodeAGTReceivedPackets;
    private javax.swing.JLabel nodeAveragePacket;
    private javax.swing.JComboBox nodeChartStartNode;
    private javax.swing.JLabel nodeDroppedBytes;
    private javax.swing.JLabel nodeDroppedPackets;
    private javax.swing.JLabel nodeForwardedBytes;
    private javax.swing.JLabel nodeForwardedPackets;
    private javax.swing.JLabel nodeGeneratedBytes;
    private javax.swing.JLabel nodeGeneratedPackets;
    private javax.swing.JPanel nodeInformation;
    private javax.swing.JLabel nodeMACGeneratedBytes;
    private javax.swing.JLabel nodeMACGeneratedPackets;
    private javax.swing.JLabel nodeMACReceivedBytes;
    private javax.swing.JLabel nodeMACReceivedPackets;
    private javax.swing.JLabel nodeMaximumPacket;
    private javax.swing.JLabel nodeMinimumPacket;
    private javax.swing.JLabel nodeRTRGeneratedBytes;
    private javax.swing.JLabel nodeRTRGeneratedPackets;
    private javax.swing.JLabel nodeRTRReceivedBytes;
    private javax.swing.JLabel nodeRTRReceivedPackets;
    private javax.swing.JLabel nodeReceivedBytes;
    private javax.swing.JLabel nodeReceivedPackets;
    private javax.swing.JComboBox nodeSelector;
    private javax.swing.JLabel nodeSentBytes;
    private javax.swing.JLabel nodeSentPackets;
    private javax.swing.JLabel numberOfAGTGeneratedBytes;
    private javax.swing.JLabel numberOfAGTGeneratedPackets;
    private javax.swing.JLabel numberOfAGTReceivedBytes;
    private javax.swing.JLabel numberOfAGTReceivedPackets;
    private javax.swing.JLabel numberOfDroppedBytes;
    private javax.swing.JLabel numberOfDroppedBytes1;
    private javax.swing.JLabel numberOfDroppedPackets;
    private javax.swing.JLabel numberOfDroppedPackets1;
    private javax.swing.JLabel numberOfForwardedBytes;
    private javax.swing.JLabel numberOfForwardedBytes1;
    private javax.swing.JLabel numberOfForwardedPackets;
    private javax.swing.JLabel numberOfForwardedPackets1;
    private javax.swing.JLabel numberOfGeneratedBytes;
    private javax.swing.JLabel numberOfGeneratedPackets;
    private javax.swing.JLabel numberOfGeneratedPackets1;
    private javax.swing.JLabel numberOfMACGeneratedBytes;
    private javax.swing.JLabel numberOfMACGeneratedPackets;
    private javax.swing.JLabel numberOfMACReceivedBytes;
    private javax.swing.JLabel numberOfMACReceivedPackets;
    private javax.swing.JLabel numberOfNodes;
    private javax.swing.JLabel numberOfNodes1;
    private javax.swing.JLabel numberOfRTRGeneratedBytes;
    private javax.swing.JLabel numberOfRTRGeneratedPackets;
    private javax.swing.JLabel numberOfRTRReceivedBytes;
    private javax.swing.JLabel numberOfRTRReceivedPackets;
    private javax.swing.JLabel numberOfReceivedBytes;
    private javax.swing.JLabel numberOfReceivedPackets;
    private javax.swing.JLabel numberOfReceivedPackets1;
    private javax.swing.JLabel numberOfSendingNodes;
    private javax.swing.JLabel numberOfSendingNodes1;
    private javax.swing.JLabel numberOfSentBytes;
    private javax.swing.JLabel numberOfSentBytes1;
    private javax.swing.JLabel numberOfSentPackets;
    private javax.swing.JLabel numberOfSentPackets1;
    private javax.swing.JMenuItem openOTCLScript;
    private javax.swing.JMenuItem openTCLFile;
    private javax.swing.JTextField outputFrameRate;
    private javax.swing.JTextField outputGroupOfPictures;
    private javax.swing.JComboBox outputVideoCodec;
    private javax.swing.JComboBox outputVideoSize;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField postSimulationFrameRate;
    private javax.swing.JTextField postSimulationOutputVideoName;
    private javax.swing.JComboBox postSimulationVideoCodec;
    private javax.swing.JTextField postSimulationVideoOutputName;
    private javax.swing.JButton previous;
    private javax.swing.JTextField qualityScaleEndValue;
    private javax.swing.JTextField qualityScaleStartValue;
    private javax.swing.JTextArea queryResultArea;
    private javax.swing.JTextField rawVideoPath;
    private javax.swing.JComboBox receiverFileSelect;
    private javax.swing.JTextField receiverPrefix;
    private javax.swing.JCheckBox rememberLoginCheckBox;
    private javax.swing.JButton resetTclDesignButton;
    private javax.swing.JComboBox samplingRateSelector;
    private javax.swing.JButton savechanges;
    private javax.swing.JButton selecttracefile;
    private javax.swing.JComboBox senderFileSelect;
    private javax.swing.JTextField senderPrefix;
    private javax.swing.JButton simParamButton;
    private javax.swing.JPanel simulationINfoPanel;
    private javax.swing.JPanel simulationINfoPanel1;
    private javax.swing.JTextField simulationScriptPath;
    private javax.swing.JLabel simulationTime;
    private javax.swing.JLabel simulationTime1;
    private javax.swing.JTextArea sqlQueryArea;
    private javax.swing.JPanel sqlQueryPanel;
    private javax.swing.JComboBox startNodes;
    private javax.swing.JLabel startTime;
    private javax.swing.JLabel startTime1;
    public static javax.swing.JLabel statusBar;
    public javax.swing.JTabbedPane tabbed;
    private Simulations.TclDesignerPanel tclDesigner;
    private javax.swing.JRadioButton throughputBitsChart;
    private javax.swing.JRadioButton throughputChart;
    public javax.swing.JList traceFileList;
    public javax.swing.JTextField username;
    private javax.swing.JComboBox videoDataFile1;
    private javax.swing.JComboBox videoDataFile2;
    private javax.swing.JButton wirelessSettingsButton;
    // End of variables declaration//GEN-END:variables

    /**
     * This method updates the swing objects related with a trace file's
     * simulations information.
     */
    public void updateStandardMetrics() {
        startTime.setText("" + simulationInfo.getStartTime());
        startTime1.setText("" + simulationInfo.getStartTime());
        endTime.setText("" + simulationInfo.getEndTime());
        endTime1.setText("" + simulationInfo.getEndTime());
        simulationTime.setText("" + simulationInfo.getSimulationTime());
        simulationTime1.setText("" + simulationInfo.getSimulationTime());
        numberOfNodes.setText("" + simulationInfo.getNumberOfNodes());
        numberOfSendingNodes.setText("" + simulationInfo.getNumberOfSendingNodes());
        numberOfSentPackets.setText("" + simulationInfo.getNumberOfSentPackets());
        numberOfSentBytes.setText("" + simulationInfo.getNumberOfSentBytes());
        numberOfForwardedPackets.setText("" + simulationInfo.getNumberOfForwardedPackets());
        numberOfForwardedBytes.setText("" + simulationInfo.getNumberOfForwardedBytes());
        numberOfDroppedPackets.setText("" + simulationInfo.getNumberOfDroppedPackets());
        numberOfDroppedBytes.setText("" + simulationInfo.getNumberOfDroppedBytes());
        numberOfReceivedPackets.setText("" + simulationInfo.getNumberOfReceivedPackets());
        numberOfGeneratedPackets.setText("" + simulationInfo.getNumberOfGeneratedPackets());
        minimumPacketSize.setText("" + simulationInfo.getMinimumPacketSize());
        maximumPacketSize.setText("" + simulationInfo.getMaximumPacketSize());
        averagePacketSize.setText("" + simulationInfo.getAveragePacketSize());
        numberOfNodes1.setText("" + simulationInfo.getNumberOfNodes());
        numberOfSendingNodes1.setText("" + simulationInfo.getNumberOfSendingNodes());
        numberOfSentPackets1.setText("" + simulationInfo.getNumberOfSentPackets());
        numberOfSentBytes1.setText("" + simulationInfo.getNumberOfSentBytes());
        numberOfForwardedPackets1.setText("" + simulationInfo.getNumberOfForwardedPackets());
        numberOfForwardedBytes1.setText("" + simulationInfo.getNumberOfForwardedBytes());
        numberOfDroppedPackets1.setText("" + simulationInfo.getNumberOfDroppedPackets());
        numberOfDroppedBytes1.setText("" + simulationInfo.getNumberOfDroppedBytes());
        numberOfReceivedPackets1.setText("" + simulationInfo.getNumberOfReceivedPackets());
        numberOfGeneratedPackets1.setText("" + simulationInfo.getNumberOfGeneratedPackets());
        minimumPacketSize1.setText("" + simulationInfo.getMinimumPacketSize());
        maximumPacketSize1.setText("" + simulationInfo.getMaximumPacketSize());
        averagePacketSize1.setText("" + simulationInfo.getAveragePacketSize());
        numberOfAGTGeneratedPackets.setText("" + simulationInfo.getGeneratedPacketsAGT());
        numberOfRTRGeneratedPackets.setText("" + simulationInfo.getGeneratedPacketsRTR());
        numberOfMACGeneratedPackets.setText("" + simulationInfo.getGeneratedPacketsMAC());
        numberOfAGTGeneratedBytes.setText("" + simulationInfo.getGeneratedPacketsSizeAGT());
        numberOfRTRGeneratedBytes.setText("" + simulationInfo.getGeneratedPacketsSizeRTR());
        numberOfMACGeneratedBytes.setText("" + simulationInfo.getGeneratedPacketsSizeMAC());
        numberOfAGTReceivedPackets.setText("" + simulationInfo.getReceivedPacketsAGT());
        numberOfMACReceivedPackets.setText("" + simulationInfo.getReceivedPacketsMAC());
        numberOfRTRReceivedPackets.setText("" + simulationInfo.getReceivedPacketsRTR());
        numberOfAGTReceivedBytes.setText("" + simulationInfo.getReceivedPacketsSizeAGT());
        numberOfRTRReceivedBytes.setText("" + simulationInfo.getReceivedPacketsSizeRTR());
        numberOfMACReceivedBytes.setText("" + simulationInfo.getReceivedPacketsSizeMAC());
        numberOfReceivedBytes.setText("" + simulationInfo.getNumberOfReceivedBytes());
        numberOfGeneratedBytes.setText("" + simulationInfo.getNumberOfGeneratedBytes());

    }

    /**
     * This method updates the swing objects related with a trace file's
     * simulations information regarding a specific node.
     */
    private void updateNodeInformation() {
        nodeSentPackets.setText("" + nodeInfo.getNumberOfSentPackets());
        nodeSentBytes.setText("" + nodeInfo.getNumberOfSentBytes());
        nodeGeneratedPackets.setText("" + nodeInfo.getNumberOfGeneratedPackets());
        nodeGeneratedBytes.setText("" + nodeInfo.getNumberOfGeneratedBytes());
        nodeDroppedPackets.setText("" + nodeInfo.getNumberOfDroppedPackets());
        nodeDroppedBytes.setText("" + nodeInfo.getNumberOfDroppedBytes());
        nodeReceivedPackets.setText("" + nodeInfo.getNumberOfReceivedPackets());
        nodeReceivedBytes.setText("" + nodeInfo.getNumberOfReceivedBytes());
        nodeForwardedPackets.setText("" + nodeInfo.getNumberOfForwardedPackets());
        nodeForwardedBytes.setText("" + nodeInfo.getNumberOfForwardedBytes());
        nodeAGTGeneratedPackets.setText("" + nodeInfo.getGeneratedPacketsAGT());
        nodeRTRGeneratedPackets.setText("" + nodeInfo.getGeneratedPacketsRTR());
        nodeMACGeneratedPackets.setText("" + nodeInfo.getGeneratedPacketsMAC());
        nodeAGTGeneratedBytes.setText("" + nodeInfo.getGeneratedPacketsSizeAGT());
        nodeRTRGeneratedBytes.setText("" + nodeInfo.getGeneratedPacketsSizeRTR());
        nodeMACGeneratedBytes.setText("" + nodeInfo.getGeneratedPacketsSizeMAC());
        nodeAGTReceivedPackets.setText("" + nodeInfo.getReceivedPacketsAGT());
        nodeRTRReceivedPackets.setText("" + nodeInfo.getReceivedPacketsRTR());
        nodeMACReceivedPackets.setText("" + nodeInfo.getReceivedPacketsMAC());
        nodeAGTReceivedBytes.setText("" + nodeInfo.getReceivedPacketsSizeAGT());
        nodeRTRReceivedBytes.setText("" + nodeInfo.getReceivedPacketsSizeRTR());
        nodeMACReceivedBytes.setText("" + nodeInfo.getReceivedPacketsSizeMAC());
        nodeMaximumPacket.setText("" + nodeInfo.getMaximumPacketSize());
        nodeMinimumPacket.setText("" + nodeInfo.getMinimumPacketSize());
        nodeAveragePacket.setText("" + nodeInfo.getAveragePacketSize());
    }

    /**
     * This method clears all the swing objects related with simulation
     * information.
     */
    public void clearAllInformation() {
        m2.setText("");
        startTime.setText("");
        startTime1.setText("");
        endTime.setText("");
        endTime1.setText("");
        simulationTime.setText("");
        simulationTime1.setText("");
        numberOfNodes.setText("");
        numberOfSendingNodes.setText("");
        numberOfSentPackets.setText("");
        numberOfSentBytes.setText("");
        numberOfForwardedPackets.setText("");
        numberOfForwardedBytes.setText("");
        numberOfDroppedPackets.setText("");
        numberOfDroppedBytes.setText("");
        numberOfReceivedPackets.setText("");
        numberOfGeneratedPackets.setText("");
        minimumPacketSize.setText("");
        maximumPacketSize.setText("");
        averagePacketSize.setText("");
        numberOfNodes1.setText("");
        numberOfSendingNodes1.setText("");
        numberOfSentPackets1.setText("");
        numberOfSentBytes1.setText("");
        numberOfForwardedPackets1.setText("");
        numberOfForwardedBytes1.setText("");
        numberOfDroppedPackets1.setText("");
        numberOfDroppedBytes1.setText("");
        numberOfReceivedPackets1.setText("");
        numberOfGeneratedPackets1.setText("");
        minimumPacketSize1.setText("");
        maximumPacketSize1.setText("");
        averagePacketSize1.setText("");
        numberOfAGTGeneratedPackets.setText("");
        numberOfRTRGeneratedPackets.setText("");
        numberOfMACGeneratedPackets.setText("");
        numberOfAGTGeneratedBytes.setText("");
        numberOfRTRGeneratedBytes.setText("");
        numberOfMACGeneratedBytes.setText("");
        numberOfAGTReceivedPackets.setText("");
        numberOfMACReceivedPackets.setText("");
        numberOfRTRReceivedPackets.setText("");
        numberOfAGTReceivedBytes.setText("");
        numberOfRTRReceivedBytes.setText("");
        numberOfMACReceivedBytes.setText("");
        numberOfReceivedBytes.setText("");
        numberOfGeneratedBytes.setText("");
        nodeSentPackets.setText("");
        nodeSentBytes.setText("");
        nodeGeneratedPackets.setText("");
        nodeGeneratedBytes.setText("");
        nodeDroppedPackets.setText("");
        nodeDroppedBytes.setText("");
        nodeReceivedPackets.setText("");
        nodeReceivedBytes.setText("");
        nodeForwardedPackets.setText("");
        nodeForwardedBytes.setText("");
        nodeAGTGeneratedPackets.setText("");
        nodeRTRGeneratedPackets.setText("");
        nodeMACGeneratedPackets.setText("");
        nodeAGTGeneratedBytes.setText("");
        nodeRTRGeneratedBytes.setText("");
        nodeMACGeneratedBytes.setText("");
        nodeAGTReceivedPackets.setText("");
        nodeRTRReceivedPackets.setText("");
        nodeMACReceivedPackets.setText("");
        nodeAGTReceivedBytes.setText("");
        nodeRTRReceivedBytes.setText("");
        nodeMACReceivedBytes.setText("");
        nodeMaximumPacket.setText("");
        nodeMinimumPacket.setText("");
        nodeAveragePacket.setText("");
        chartStartNode.removeAllItems();
        chartEndNode.removeAllItems();
        nodeChartStartNode.removeAllItems();
        nodeSelector.removeAllItems();
        startNodes.removeAllItems();
        endNodes.removeAllItems();
        levels.removeAllItems();
        chartLevel.removeAllItems();

    }

    /**
     * this method is used to get the local time for logging purposes.
     *
     * @return it returns the time.
     */
    public static String NOW() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

    }

    public void updateVideoSimulationResults(Map<String, ArrayList<File>> results) {

        senderFileSelect.removeAllItems();
        mp4TraceSelect.removeAllItems();
        videoDataFile1.removeAllItems();
        videoDataFile2.removeAllItems();
        receiverFileSelect.removeAllItems();
        if (!(results == null) && !(results.isEmpty())) {
            System.out.print("Updating Simulation Results");
            ArrayList<File> senderFiles = (ArrayList<File>) results.get("senderFiles");
            ArrayList<File> receiverFiles = (ArrayList<File>) results.get("receiverFiles");
            ArrayList<File> mp4OutFiles = (ArrayList<File>) results.get("mp4OutFiles");
            ArrayList<File> datFiles = (ArrayList<File>) results.get("datFiles");
            for (int i = 0; i < senderFiles.size(); i++) {
                senderFileSelect.addItem(senderFiles.get(i).getName());
            }
            for (int i = 0; i < receiverFiles.size(); i++) {
                receiverFileSelect.addItem(receiverFiles.get(i).getName());
            }
            for (int i = 0; i < mp4OutFiles.size(); i++) {
                mp4TraceSelect.addItem(mp4OutFiles.get(i).getName());
            }
            for (int i = 0; i < datFiles.size(); i++) {
                videoDataFile1.addItem(datFiles.get(i).getName());
                videoDataFile2.addItem(datFiles.get(i).getName());
            }
        }
    }

    /**
     * @return the newLinkButton
     */
    public JToggleButton getNewLinkButton() {
        return newLinkButton;
    }

    /**
     * @return the newWiredNodeButton
     */
    public javax.swing.JToggleButton getNewWiredNodeButton() {
        return newWiredNodeButton;
    }

    /**
     * @return the newWirelessNodeButton
     */
    public javax.swing.JToggleButton getNewWirelessNodeButton() {
        return newWirelessNodeButton;
    }
}
