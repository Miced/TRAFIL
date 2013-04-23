package src;

import UI.TRAFIL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utilities.DatabaseConnection;
import utilities.PathLocator;
import utilities.TableListener;

/**
 * This class handles most of TRAFIL's business logic. It is responsible to open
 * a trace file, recognize and match it with a meta file and then process and
 * store it to the database. It also invokes the DatabaseConnection class to
 * enable the connection to the database. Finally it handles the deletion and
 * loading of a trace file from the database as well as the update of changes
 * that have occurred to a trace file through the jTable.
 *
 * @author charalampi
 */
public class TracefileHandler {

    /*
     * Tracefile Specific Variables
     */
    public int currentLineCounter, pageLineLimit, pageCounter, linecounter, overallLineCounter;
    /*
     * MySQL specific variables
     */
    public Connection connection = null;
    public Statement st;
    public ResultSet rs;
    /*
     * Tool Specific Variables
     */
    // this variable is used to indicate to other processes that
    // trace file data transfer is taking place.
    public boolean inserting;
    private BufferedReader br;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private JTable jTable1;
    private DefaultListModel model;
    private JLabel statusBar;
    private TraceFileInfo TraceFile;
    private TableListener TableHandler;
    private TableHandler TableHan;
    private MetaDataHandler metaHandler = new MetaDataHandler();

    public MetaDataHandler getMetaHandler() {
        return metaHandler;
    }

    public TracefileHandler() {
        inserting = false;
    }

    /**
     * This method is called to establish the connection to the database. After
     * the connection is established some tables that should be present that
     * TRAFIL needs to operate are created if they are no already present.
     *
     * @param dbName the name of the database to connect
     * @param user the user of the database
     * @param pa the password to the database
     * @param modelList a reference to TRAFIL's list of trace files in order to
     * fill it
     * @param statusBar a reference to the TRAFIL's status bar in order to post
     * messages
     * @param jTable a reference to TRAFIL's jTable to enable interaction with
     * it
     * @param TraceFile the instance of the trace file info class for the
     * currently selected trace file
     * @param TableHandler the instance of TableListener class
     * @param TableHan the instance of TableHandler class
     * @return boolean, true if the connection was successful, false otherwise
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public boolean performConnection(String dbName, String user, String pa, DefaultListModel modelList, JLabel statusBar, JTable jTable, TraceFileInfo TraceFile, TableListener TableHandler, TableHandler TableHan) throws ClassNotFoundException, SQLException {

        model = modelList;
        jTable1 = jTable;
        this.TraceFile = TraceFile;
        this.TableHandler = TableHandler;
        this.TableHan = TableHan;
        //Logger.getLogger(TracefileHandler.class.getName()).addHandler(mainFrame.logHandler);

        /*
         * Initiallizing all table models
         */

        this.statusBar = statusBar;

        //String driverName = "com.mysql.jdbc.Driver";
        //Class.forName(driverName);
        //String url = "jdbc:mysql://localhost/" + dbName + "?allowMultiQueries=True";
        //if (dbName.equalsIgnoreCase("") || (dbName.length() < 2) || url.equalsIgnoreCase("") || user.equalsIgnoreCase("") || (user.length() < 2) || (url.length() < 2)) {
        // JOptionPane.showMessageDialog(null, "Please insert valid Login Data.");
        //return false;
        //} else {
        //connection = DriverManager.getConnection(url, user, pa);
        DatabaseConnection conn = new DatabaseConnection(dbName, user, pa);
        if (conn.connect()) {
            Logger.getLogger(TracefileHandler.class.getName()).log(Level.SEVERE, "Succesfully Connected to Database {0} as {1}", new Object[]{dbName, user});
        } else {
            JOptionPane.showMessageDialog(null, "The login credentials were not correct.", "Wrong login information.", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //System.out.println("[" + NOW() + "] Succesfully Connected to Database " + dbName + " as " + user);
        st = DatabaseConnection.getSt();
        //st = connection.createStatement();
        st.execute("create table if not exists standard_metrics (name varchar(40) NOT NULL,startTime double,endTime double,simulationTime double,numberOfNodes int,numberOfSendingNodes int,numberOfSentPackets int,numberOfSentBytes double,numberOfForwardedPackets int,numberOfForwardedBytes double,numberOfDroppedPackets int,numberOfDroppedBytes double,numberOfReceivedPackets int,numberOfGeneratedPackets int,minimumPacketSize double,"
                + "maximumPacketSize double,averagePacketSize double,numberOfGeneratedBytes double,numberOfReceivedBytes double,numberOfAGTGeneratedPackets int,numberOfRTRGeneratedPackets int,numberOfMACGeneratedPackets int,"
                + "numberOfAGTGeneratedBytes double,numberOfRTRGeneratedBytes double,numberOfMACGeneratedBytes double,numberOfAGTReceivedPackets int,numberOfRTRReceivedPackets int,numberOfMACReceivedPackets int,"
                + "numberOfAGTReceivedBytes double,numberOfRTRReceivedBytes double,numberOfMACReceivedBytes double,PRIMARY KEY(name));");
        st.execute("create table if not exists tracefiles (name varchar(40) NOT NULL,type varchar(200),linecounter int,PRIMARY KEY(name));");
        st.execute("select * from tracefiles;");
        rs = st.getResultSet();


        inserting = false;
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        statusBar.setText("<html><font color=#0033FF>No Trace File currently selected...</font></hmtl>");
        return true;
        //}

    }

    public String getSelectedTraceFile() {
        return TraceFile.getTraceFileName();
    }

    public int getLineCounter() {
        return linecounter;
    }

    public int getOveralLineCounter() {
        return overallLineCounter;
    }

    public int getCurrentLineCounter() {
        return currentLineCounter;
    }

    public ResultSet getResultSet() {
        return rs;
    }

    public int getPageCounter() {
        return pageCounter;
    }

    public int getPageLineLimit() {
        return pageLineLimit;
    }

    /**
     * This method handles the opening of a new trace file. First checks if the
     * trace file is already present in the database and informs the user. Then
     * it tries to match the trace file with a meta file and it retrieves the
     * meta data handler for the trace file. Using the meta data handler it
     * creates the table that will hold the trace file's data in the database
     * and starts the data transfer procedure. Finally it sets all the necessary
     * utility variables to the correct values.
     *
     * @param inputfile a reference to the trace file to be processed
     * @return boolean, true if the open procedure was successful, false
     * otherwise
     */
    public boolean OpenTraceFile(File inputfile) {
        long start, end, filesize;
        start = 0;
        end = 0;
        TraceFile.setInputfile(inputfile);
        TraceFile.setTraceFileName(inputfile.getName().replace('.', '_'));

        try {
            Logger.getLogger(TracefileHandler.class.getName()).info("Checking table availability.");
            //System.out.println("[" + NOW() + "] Checking table availability.");
            st.executeQuery("select name from tracefiles where name=\"" + TraceFile.getTraceFileName() + "\";");
            rs = st.getResultSet();
            Logger.getLogger(TracefileHandler.class.getName()).info("Trace Fils's Name:" + TraceFile.getTraceFileName());
            //System.out.println("[" + NOW() + "] Trace Fils's Name:" + TraceFile.getTraceFileName());
            if (rs.next()) {

                int response = JOptionPane.showConfirmDialog(null, "<html>The trace file you have selected already exists.<br/>Do you want to ovewrite it?</html>");
                if (response == 0) {
                    st.execute("drop table " + TraceFile.getTraceFileName() + ";");
                    st.execute("delete from tracefiles where name=\"" + TraceFile.getTraceFileName() + "\";");
                    Logger.getLogger(TracefileHandler.class.getName()).info("Discarding previous trace file.");
                    //System.out.println("[" + NOW() + "] Discarding previous trace file.");
                    st.execute("delete from standard_metrics where name='" + TraceFile.getTraceFileName() + "';");
                    Logger.getLogger(TracefileHandler.class.getName()).info("Discarding previous metrics about trace file.");
                    //System.out.println("[" + NOW() + "] Discarding previous metrics about trace file.");
                } else {
                    System.out.println("[" + TRAFIL.NOW() + "] Canceling open procedure.");
                    return false;
                }
            }
            Logger.getLogger(TracefileHandler.class.getName()).info(" Attempting to match " + TraceFile.getTraceFileName() + " with a metafile.");
            //System.out.println("[" + NOW() + "] Attempting to match " + TraceFile.getTraceFileName() + " with a metafile.");
            metaHandler = matchMetaHandler(inputfile);
            if (metaHandler == null) {
                Logger.getLogger(TracefileHandler.class.getName()).info("Trace File does not Match any MetaFile.");
                //System.out.println("[" + NOW() + "] Trace File does not Match any MetaFile.");
                return false;
            }
            Logger.getLogger(TracefileHandler.class.getName()).info("Attempting to load sub metafiles for matched metafile.");
            //System.out.println("[" + NOW() + "] Attempting to load sub metafiles for matched metafile.");
            if (metaHandler.loadSubMetaHanlders()) {
                Logger.getLogger(TracefileHandler.class.getName()).info("Succesfully loaded sub metafiles for matched metafile.");
                //System.out.println("[" + NOW() + "] Succesfully loaded sub metafiles for matched metafile.");
            } else {
                Logger.getLogger(TracefileHandler.class.getName()).info("Error in loading sub metafiles for " + metaHandler.getMetaFilePath());
                //System.out.println("[" + NOW() + "] Error in loading sub metafiles for " + metaHandler.getMetaFilePath());
                return false;
            }
            //JOptionPane.showMessageDialog(null,"Table string inside metadatahandler:"+ metaHandler.getCreateTableString());

            if (TraceFile.is_right_format()) {
                //create_table();
                TraceFile.setTraceFileName(inputfile.getName());
                metaHandler.createTable(TraceFile.getTraceFileName(), st);
                TableHan.setTestModel(new DefaultTableModel(new Object[][]{}, metaHandler.getModel()));


                filesize = inputfile.length();
                TraceFile.setFilesizeInKB(filesize / 1024);
                //JOptionPane.showMessageDialog(null,"Size of File is: "+ filesizeInKB + " KB");


                try {
                    start = System.currentTimeMillis();
                    //read_and_insert(inputfile);
                    Logger.getLogger(TracefileHandler.class.getName()).info("Attempting to transfer data from the selected trace file to local database.");
                    //System.out.println("[" + NOW() + "] Attempting to transfer data from the selected trace file to local database.");
                    if (!transferTraceFileData(inputfile)) {
                        Logger.getLogger(TracefileHandler.class.getName()).info("Error in read and insert function excecution.");
                        //System.out.println("[" + NOW() + "] Error in read and insert function excecution.");
                        Logger.getLogger(TracefileHandler.class.getName()).info("Clearing contents of main table.");
                        System.out.println("[" + TRAFIL.NOW() + "] Clearing contents of main table.");
                        TableHan.getTestModel().setNumRows(0);
                        Logger.getLogger(TracefileHandler.class.getName()).info("Attempting to destroynig table.");
                        System.out.println("[" + TRAFIL.NOW() + "] Attempting to destroynig table.");
                        try {
                            st.execute("drop table " + TraceFile.getTraceFileName() + ";");
                            return false;
                        } catch (SQLException ex) {
                            Logger.getLogger(TracefileHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return false;
                    }
                    end = System.currentTimeMillis();
                } catch (Exception ex) {
                    Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
                }

                overallLineCounter = linecounter;
                if (linecounter > 10000) {
                    pageLineLimit = 10000;
                    pageCounter = 1;
                    currentLineCounter = pageLineLimit * pageCounter;
                } else {
                    pageCounter = 1;
                    currentLineCounter = linecounter;
                }

                st.execute("insert into tracefiles (name,type,linecounter) values (\"" + TraceFile.getTraceFileName() + "\",\"" + metaHandler.getMetaFilePath() + "\"," + linecounter + ");");
                st.execute("select * from tracefiles;");
                TableHandler.setSqlupdatequery("");
                rs = st.getResultSet();
                Logger.getLogger(TracefileHandler.class.getName()).info("Succesfully opened and inserted Trace File Content. Execution time was " + (end - start) + " ms.");
                //System.out.println("[" + NOW() + "] Succesfully opened and inserted Trace File Content.Execution time was " + (end - start) + " ms.");
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Trace File.");
                return false;
            }

            return true;
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Mysql error in open");
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, e1);
            return false;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "IO error in open");
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    /**
     * This method is used to delete a specific trace file that was selected
     * from the list of tRAFIL's gui.
     *
     * @param tracefilename string, the name of the trace file to be deleted
     */
    public void DeleteTraceFile(String tracefilename) {
        try {
            st.execute("drop table " + tracefilename + ";");
            st.execute("delete from tracefiles where name=\"" + tracefilename + "\";");
            st.execute("delete from standard_metrics where name=\"" + tracefilename + "\";");
            st.execute("select * from tracefiles;");
            model.clear();
            TableHandler.setSqlupdatequery("");
            rs = st.getResultSet();
            while (rs.next()) {
                model.addElement(rs.getString(1));
            }
            if (tracefilename.equalsIgnoreCase(TraceFile.getTraceFileName())) {
                TableHan.getModel3().setRowCount(0);
                jTable1.setModel(TableHan.getModel3());
            }
            Logger.getLogger(TracefileHandler.class.getName()).info("Deleted trace file: " + tracefilename);
            statusBar.setText("<html><font color=#0033FF>No Trace File currently selected...</font></hmtl>");
        } catch (SQLException ex) {
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is used to retrieve the next 10000 lines of the trace file
     * from the database and update the contents of the JTable.
     */
    public void LoadNextTraceFileLines() {
        try {
            Statement st1 = DatabaseConnection.getSt();
            pageCounter++;
            currentLineCounter = pageLineLimit * pageCounter;
            //JOptionPane.showConfirmDialog(null, "Ovearall Line Counter:" + overallLineCounter);
            st1.execute("select * from " + TraceFile.getTraceFileName() + " LIMIT " + (pageLineLimit * (pageCounter - 1) + 1) + "," + pageLineLimit + ";");
            TableHan.addToTable2(st1, metaHandler.getModel().length);
        } catch (SQLException ex) {
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is used to retrieve the previous 10000 lines of the trace
     * file from the database and update the contents of the JTable.
     */
    public void LoadPreviousTraceFileLines() {
        try {
            Statement st1 = DatabaseConnection.getSt();
            if (pageCounter == 2) {
                pageCounter--;
                currentLineCounter = pageLineLimit * pageCounter;
            } else {
                pageCounter--;
                currentLineCounter = pageLineLimit * pageCounter;
            }
            st1.execute("select * from " + TraceFile.getTraceFileName() + " LIMIT " + (pageLineLimit * (pageCounter - 1) + 1) + "," + pageLineLimit + ";");
            TableHan.addToTable2(st1, metaHandler.getModel().length);

        } catch (SQLException ex) {
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isInserting() {
        return inserting;
    }

    public void setInserting(boolean inserting) {
        this.inserting = inserting;
    }

    /**
     * This method is invoked when the user wants to save the changes done on
     * the trace file via the jTable. It uses the update string of the
     * TableHandler to execute it.
     *
     * @throws SQLException
     */
    public void saveChanges() throws SQLException {

        if (TableHandler.getSqlupdatequery().isEmpty()) {
            JOptionPane.showMessageDialog(null, "<html>You have not made any changes to the table yet.<br/>If you have indeed made any changes,<br/> you must unfocus the cell for them to be taken into account</html>");
        } else {
            //JOptionPane.showConfirmDialog(null, "inside savechanges:" + TableHandler.getSqlupdatequery());
            st.execute(TableHandler.getSqlupdatequery());
            Logger.getLogger(TracefileHandler.class.getName()).info(" Succesfully save changes done to " + TraceFile.getTraceFileName());
        }

        TableHandler.setSqlupdatequery("");
    }

    public Statement getStatement() {
        return this.st;
    }

    /**
     * This method is responsible for transferring the transfer of data from the
     * trace file to the database. The trace file is read line by line. For each
     * line it attempts to match it either with the meta file alone or with the
     * meta file and a number of sub meta files based on the number of elements
     * of the line and the unique flags of the meta file and sub meta files.
     * When a match is found for that line it is parsed using the handlers it
     * was matched to and then the data are written to an external tempfile and
     * the model of the jTable is updated until we reach 10000 lines. Finally
     * when all the trace file is read the transfer of the information to the
     * database starts using the DATA INFILE command that transfers the contents
     * of the tempfile to the database.
     *
     * @param inputFile a reference to the trace file whose contents will be
     * transferred
     * @return boolean, true if the transfer was successful, false otherwise
     */
    public boolean transferTraceFileData(File inputFile) {
        //String path = getClass().getClassLoader().getResource(".").getPath();
        //path = path.replace("%20", " ") + "../../Resources/Utility/tempfile.txt";
        String path = System.getProperty("user.dir");
        path = PathLocator.getTempFilePath(path);
        File temp = new File(path);
        String data = new String();
        String[] elements = null;
        int errorsFound = 0;
        String[] splitValues = null;
        int i, localcounter;
        String fileOutput = new String();
        int eventIndex = 0;
        ArrayList<MetaEntry> metaEntries;
        Object[] modelLine = new Object[metaHandler.getModel().length];
        int modelElementCounter = 0;
        boolean subMetaFileUse = false;
        boolean lineError = false;
        int subMetafileIndex = 0;
        int remainingElements, startIndex;
        ArrayList<Integer> subMetaHandlersIndex = new ArrayList();
        ArrayList<Integer> startingIndexList = new ArrayList();
        try {

            if (metaHandler == null) {
                JOptionPane.showMessageDialog(null, "Trace File does not Match any MetaFile.");
                return false;
            }
            br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            out.flush();
            metaEntries = metaHandler.getMetaEntries();

            /*
             * Initiallizing counters
             */

            linecounter = 0;
            localcounter = 0;
            TableHan.getTestModel().setNumRows(0);
            for (i = 0; i < metaEntries.size(); i++) {
                if (metaEntries.get(i).getName() != null) {
                    if (metaEntries.get(i).getName().equalsIgnoreCase("event")) {
                        eventIndex = i;
                        break;
                    }
                }
            }

            /*
             * Starting the insertion procedure so we stop detecting the changes
             * on the table cells to stop the event triggering.
             */
            inserting = true;
            localcounter = 0;
            while (((data = br.readLine()) != null)) {

                /*
                 * Too time consuming task, we need a better way to implement
                 * progress... maybe use of threads?
                 */
                /*
                 * if ((linecounter % 2 == 0)) { statusBar.setText("Loading:" +
                 * TraceFile.getTraceFileName() + "........."); } else {
                 * statusBar.setText("Loading:" + TraceFile.getTraceFileName() +
                 * "..."); }
                statusBar.paintImmediately(statusBar.getVisibleRect());
                 */
                if (errorsFound > 50) {
                    Logger.getLogger(TracefileHandler.class.getName()).info("Errors encountered during trace file processing:" + errorsFound);
                    //System.out.println("[" + NOW() + "] Errors encountered during trace file processing:" + errorsFound);
                    Logger.getLogger(TracefileHandler.class.getName()).info("Stopping trace file processing.");
                    //System.out.println("[" + NOW() + "] Stopping trace file processing.");
                    return false;
                }
                fileOutput = "";
                elements = null;
                data = data.trim();
                lineError = false;
                //elements = data.split(" ");
                elements = data.split("\\s+");
                /*
                 * for (int k=0; k<elements2.length; k++){
                 * System.out.println(k+":"+elements2[k]);
                }
                 */
                modelElementCounter = 0;
                modelLine = new Object[metaHandler.getModel().length];
                subMetafileIndex = -1;
                int uniqueCounter = 0;
                subMetaFileUse = false;
                if (elements[Integer.parseInt(metaEntries.get(eventIndex).getIndex())].equalsIgnoreCase("r")
                        || elements[Integer.parseInt(metaEntries.get(eventIndex).getIndex())].equalsIgnoreCase("d")
                        || elements[Integer.parseInt(metaEntries.get(eventIndex).getIndex())].equalsIgnoreCase("e")
                        || elements[Integer.parseInt(metaEntries.get(eventIndex).getIndex())].equalsIgnoreCase("+")
                        || elements[Integer.parseInt(metaEntries.get(eventIndex).getIndex())].equalsIgnoreCase("-")
                        || elements[Integer.parseInt(metaEntries.get(eventIndex).getIndex())].equalsIgnoreCase("s")
                        || elements[Integer.parseInt(metaEntries.get(eventIndex).getIndex())].equalsIgnoreCase("f")) {
                    //System.out.println("Data:"+data);
                    if (elements.length == metaHandler.getColumnsIndex()) {
                        subMetaFileUse = false;
                    } else {

                        subMetaHandlersIndex.clear();
                        startingIndexList.clear();
//                        JOptionPane.showMessageDialog(null,"Elements length:"+elements.length);
                        remainingElements = elements.length - metaHandler.getColumnsIndex();
//                        JOptionPane.showMessageDialog(null,"Remaining Elements after metafile:"+remainingElements);
                        startIndex = metaHandler.getColumnsIndex() - 1;
//                        JOptionPane.showMessageDialog(null,"Starting Index:"+startIndex);
                        while (remainingElements != 0) {
                            int remainingTemp = remainingElements;
                            for (int j = 0; j < metaHandler.getSubMetaHandlers().size(); j++) {
                                uniqueCounter = 0;
//                                JOptionPane.showMessageDialog(null, "Checking sub:"+metaHandler.getSubMetaHandlers().get(j).getMetaFilePath());
//                                JOptionPane.showMessageDialog(null,"Remaining Elements after metafile:"+remainingElements+"\nSubMeta's ColIndex:"+metaHandler.getSubMetaHandlers().get(j).getColumns_index());
//                                  JOptionPane.showMessageDialog(jTable1, "element:"+elements[startIndex+remainingTemp-1]);
                                if (remainingElements >= (metaHandler.getSubMetaHandlers().get(j).getColumns_index())) {
                                    uniqueCounter = 0;
                                    for (i = 0; i < metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().size(); i++) {
                                        if (metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(i).getUnique() != null) {
                                            //JOptionPane.showMessageDialog(null,"Start Index:"+startIndex+"Checking Element:"+elements[Integer.parseInt(metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(i).getIndex())+startIndex]+" for unique:"+metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(i).getUnique());
                                            if (elements[Integer.parseInt(metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(i).getIndex()) + startIndex].indexOf(metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(i).getUnique()) != -1) {
                                                uniqueCounter++;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
//                                    JOptionPane.showMessageDialog(null,"UniueCounter:"+uniqueCounter+" Meta UniqueCounter:"+metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(2).getUniqueCounter());
                                }
                                //JOptionPane.showMessageDialog(null,"UniueCounter:"+uniqueCounter+" Meta UniqueCounter:"+metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(2).getUniqueCounter());
                                if (uniqueCounter == Integer.parseInt(metaHandler.getSubMetaHandlers().get(j).getSubMetaEntries().get(2).getUniqueCounter())) {
                                    //JOptionPane.showMessageDialog(null,"Matched Sub meta handler:"+metaHandler.getSubMetaHandlers().get(j).getMetaFilePath());

                                    subMetaHandlersIndex.add(j);
                                    startingIndexList.add(startIndex);
                                    //JOptionPane.showMessageDialog(null,"Linecounter:"+linecounter+"MetaHandlersIndex:"+subMetaHandlersIndex.size());
                                    subMetaFileUse = true;
                                    subMetafileIndex = j;

                                    remainingElements -= metaHandler.getSubMetaHandlers().get(j).getColumns_index();
                                    //JOptionPane.showMessageDialog(null,"Reaminning Elements:"+remainingElements);
                                    //JOptionPane.showMessageDialog(null,"Matched:"+metaHandler.getSubMetaHandlers().get(j).getMetaFilePath());
                                    startIndex += metaHandler.getSubMetaHandlers().get(j).getColumns_index();
                                    //JOptionPane.showMessageDialog(null,"Starting Index after sub metafile match:"+startIndex);
                                    break;
                                }
                            }
                            if (remainingTemp == remainingElements) {
                                Logger.getLogger(TracefileHandler.class.getName()).severe("ERROR at line: " + data);
                                //System.out.println("[" + NOW() + "] Error at line: " + data);
                                //JOptionPane.showMessageDialog(null, "Error. Remaining elements didnt change.");
                                //return false;
                                lineError = true;
                                break;
                            }
                        }
                        if (lineError || (subMetafileIndex == -1)) {
                            //JOptionPane.showMessageDialog(null, "Error. Trace file does not match trace type.");
                            //return false;
                            errorsFound++;
                            continue;
                        }
                    }
                    try {
                        for (i = 0; i < metaEntries.size(); i++) {
                            if (metaEntries.get(i).getName() != null) {
                                //System.out.println("Checkong element:"+elements[Integer.parseInt(metaEntries.get(i).getIndex())]);
                                if ((metaEntries.get(i).getStartsWith() != null)) {
                                    elements[Integer.parseInt(metaEntries.get(i).getIndex())] = elements[Integer.parseInt(metaEntries.get(i).getIndex())].replace(metaEntries.get(i).getStartsWith(), "");
                                }
                                if ((metaEntries.get(i).getEndsWith() != null)) {
                                    elements[Integer.parseInt(metaEntries.get(i).getIndex())] = elements[Integer.parseInt(metaEntries.get(i).getIndex())].replace(metaEntries.get(i).getEndsWith(), "");
                                }
                                if ((metaEntries.get(i).getDelimiter() == null)) {
                                    fileOutput = fileOutput + (elements[Integer.parseInt(metaEntries.get(i).getIndex())]) + ",";
                                    modelLine[modelElementCounter++] = elements[Integer.parseInt(metaEntries.get(i).getIndex())];
                                } else {
                                    //JOptionPane.showMessageDialog(null, "Splitting with Delimiter:"+metaEntries.get(i).getDelimiter());
                                    //System.out.println("Element to split:"+elements[Integer.parseInt(metaEntries.get(i).getIndex())]);
                                    if (metaEntries.get(i).getDelimiter().equals(".")) {
                                        splitValues = elements[Integer.parseInt(metaEntries.get(i).getIndex())].split("\\" + metaEntries.get(i).getDelimiter());
                                    } else {
                                        splitValues = elements[Integer.parseInt(metaEntries.get(i).getIndex())].split(metaEntries.get(i).getDelimiter());
                                    }

                                    fileOutput = fileOutput + splitValues[0] + "," + splitValues[1] + ",";
                                    modelLine[modelElementCounter++] = splitValues[0];
                                    modelLine[modelElementCounter++] = splitValues[1];
                                    i++;
                                }
                            }
                        }
                    } catch (Exception parseException) {
                        Logger.getLogger(TracefileHandler.class.getName()).log(Level.SEVERE, null, parseException);
                        errorsFound++;
                        continue;
                    }
                    //System.out.println("File Output:"+fileOutput);
                    int foundCounter = 0;
                    startIndex = metaHandler.getColumnsIndex() - 1;
                    /*
                     * if(subMetaHandlersIndex.size()>1){
                     * JOptionPane.showMessageDialog(null, "Matched
                     * "+subMetaHandlersIndex.size()+" sub metahandlers.");
                     * for(int q=0; q<subMetaHandlersIndex.size(); q++){
                     * JOptionPane.showMessageDialog(null,"SubeMeta "+q+" is
                     * "+metaHandler.getSubMetaHandlers().get(subMetaHandlersIndex.get(q)).getMetaFilePath());
                     * }
                    }
                     */
                    if (subMetaFileUse) {

                        for (int j = 0; j < metaHandler.getSubMetaHandlers().size(); j++) {
                            //System.out.println("Sub Meta Index:"+subMetaHandlersIndex.get(foundCounter));
                            int index = subMetaHandlersIndex.indexOf(j);
                            if (index != -1) {
                                //JOptionPane.showMessageDialog(null,"Using:"+metaHandler.getSubMetaHandlers().get(subMetaHandlersIndex.get(index)).getMetaFilePath()+"Starting Index:"+startingIndexList.get(index));
                                //JOptionPane.showMessageDialog(null,"Found sub metahandler in list,starting index:"+startIndex);
                                if (foundCounter < subMetaHandlersIndex.size()) {
                                    foundCounter++;
                                }
                                ArrayList<MetaEntry> subMetaEntries = metaHandler.getSubMetaHandlers().get(subMetaHandlersIndex.get(index)).getSubMetaEntries();
                                //System.out.println("****Sub meta Hnadler:"+metaHandler.getSubMetaHandlers().get(subMetaHandlersIndex.get(index)).getMetaFilePath());
                                for (i = 0; i < subMetaEntries.size(); i++) {
                                    if (subMetaEntries.get(i).getName() != null) {
                                        //System.out.println("Checking element:"+elements[Integer.parseInt(subMetaEntries.get(i).getIndex())+startingIndexList.get(index)]);
                                        if ((subMetaEntries.get(i).getStartsWith() != null)) {
                                            elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)] = elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)].replace(subMetaEntries.get(i).getStartsWith(), "");
                                        }
                                        if ((subMetaEntries.get(i).getEndsWith() != null)) {
                                            //JOptionPane.showMessageDialog(null,"Reaplacing character from element:"+elements[Integer.parseInt(subMetaEntries.get(i).getIndex())]);
                                            elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)] = elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)].replace(subMetaEntries.get(i).getEndsWith(), "");
                                        }

                                        if ((subMetaEntries.get(i).getDelimiter() == null)) {
                                            fileOutput = fileOutput + (elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)]) + ",";
                                            modelLine[modelElementCounter++] = elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)];
                                        } else {
                                            //JOptionPane.showMessageDialog(null, "Splitting with Delimiter:"+metaEntries.get(i).getDelimiter());
                                            //System.out.println("");
                                            //System.out.println("Elements length:"+elements.length);
                                            //System.out.println("Element Index:"+Integer.parseInt(subMetaEntries.get(i).getIndex())+"Start Index:"+startIndex);
                                            //System.out.println("Element to split:"+elements[Integer.parseInt(metaEntries.get(i).getIndex())+startIndex]);
                                            if (subMetaEntries.get(i).getDelimiter().equals(".")) {
                                                splitValues = elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)].split("\\" + subMetaEntries.get(i).getDelimiter());
                                            } else {
                                                splitValues = elements[Integer.parseInt(subMetaEntries.get(i).getIndex()) + startingIndexList.get(index)].split(subMetaEntries.get(i).getDelimiter());
                                            }

                                            fileOutput = fileOutput + splitValues[0] + "," + splitValues[1] + ",";
                                            modelLine[modelElementCounter++] = splitValues[0];
                                            modelLine[modelElementCounter++] = splitValues[1];
                                            i++;
                                        }
                                    }
                                }
                                //startIndex+=metaHandler.getSubMetaHandlers().get(j).getColumns_index();
                                //JOptionPane.showMessageDialog(null,"Found Counter:"+foundCounter+"Meta handlers Number:"+subMetaHandlersIndex.size());
                                //if((foundCounter)==subMetaHandlersIndex.size()){
                                //break;
                                //}

                            } else {
                                //JOptionPane.showMessageDialog(null,"Entering null for:"+metaHandler.getSubMetaHandlers().get(j).getMetaFilePath());
                                //JOptionPane.showMessageDialog(null,"Number of null that will be added:"+metaHandler.getSubMetaHandlers().get(j).getFields_index());
                                for (i = 0; i < metaHandler.getSubMetaHandlers().get(j).getFields_index(); i++) {
                                    fileOutput = fileOutput + "\\N,";
                                    modelLine[modelElementCounter++] = null;
                                }
                                //JOptionPane.showMessageDialog(null,"Number of nulls added:"+i);
                            }
                        }

                    }
                    //JOptionPane.showMessageDialog(null, "Row Elememnts:"+modelLine.length);
                    //fileOutput=fileOutput.trim();

                    fileOutput = fileOutput.substring(0, fileOutput.length() - 1);
                    fileOutput = fileOutput.trim();
                    linecounter++;
                    //System.out.println("File Output2:"+linecounter+","+fileOutput);
                    out.flush();
                    out.write(linecounter + "," + fileOutput + "\n");
                    if (localcounter < 10000) {
                        TableHan.getTestModel().insertRow(localcounter, modelLine);
                        localcounter++;
                    }

                }

            }

            jTable1.setModel(TableHan.getTestModel());
            out.close();
            Logger.getLogger(TracefileHandler.class.getName()).info("Starting procedure Data Infile.");
            //System.out.println("[" + NOW() + "] Starting procedure Data Infile.");

            //st.execute("LOAD DATA LOCAL INFILE \"C:/tempfile.txt\" INTO TABLE "+inputFile.getName().replace('.', '_') +" FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';");
            //st.execute("LOAD DATA LOCAL INFILE \"" + getClass().getClassLoader().getResource(".").getPath().replace("%20", " ") + "../../Resources/Utility/tempfile.txt\" INTO TABLE " + TraceFile.getTraceFileName() + " FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';");
            st.execute("LOAD DATA LOCAL INFILE \"" + PathLocator.getTempFilePath(System.getProperty("user.dir").replace("\\", "/")) + "\" INTO TABLE " + TraceFile.getTraceFileName() + " FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n';");
            Logger.getLogger(TracefileHandler.class.getName()).info("Succesfully Transfered data from " + temp.getPath() + " to Database.");
            //System.out.println("[" + NOW() + "] Succesfully Transfered data from " + temp.getPath() + " to Database.");
            //JOptionPane.showMessageDialog(null, "Succesfully transfered trace file. Errors Found:" + errorsFound);

            /*
             * Finished inserting the trace file data to the database and now
             * allowing handling of events on the table.
             */
            TableHandler.setSqlupdatequery("");
            inserting = false;
            return true;
        } catch (SQLException ex1) {
            ex1.getNextException();
            ex1.getMessage();
            ex1.printStackTrace();
            Logger.getLogger(TracefileHandler.class.getName()).info("Error to data transfer to MySQL.");
            //System.out.println("[" + NOW() + "] Error to data transfer to MySQL.");
            Logger.getLogger(TracefileHandler.class.getName()).info("Attempting to destroynig table.");
            //System.out.println("[" + NOW() + "] Attempting to destroynig table.");

            try {
                st.execute("drop table " + TraceFile.getTraceFileName() + ";");
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(TracefileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex1);
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(TracefileHandler.class.getName()).info("IO Exception while reading data from file.");
            //System.out.println("[" + NOW() + "] IO Exception while reading data from file.");
            return false;
        }
        catch(Exception ex) {
            System.out.println("linecounter: "+linecounter);
            ex.printStackTrace();
            ex.getMessage();
            return false;
        }
    }

    /**
     * This method loads the contents of the selected trace file in TRAFIL's
     * list of trace files from the database to the jTable and updates the all
     * the handlers to contain the information of this trace file. It loads the
     * meta data handler that was used to parse this trace file and updates the
     * TraceFile instance of the TraceFileInfo class.
     *
     * @param selectedtracefile String, the name of the selected trace file.
     * @return boolean, true if the load procedure was successful, false
     * otherwise.
     */
    public boolean loadSelectedTraceFile(String selectedtracefile) {
        try {
            Logger.getLogger(TracefileHandler.class.getName()).info(" Attempting to update JTable with " + selectedtracefile + ".");
            //System.out.println("[" + NOW() + "] Attempting to update JTable with " + selectedtracefile + ".");
            TraceFile.setTraceFileName(selectedtracefile);
            st.execute("select type,linecounter from tracefiles where name=\"" + TraceFile.getTraceFileName() + "\";");
            rs = st.getResultSet();
            rs.next();
            //JOptionPane.showMessageDialog(null, rs.getString(1));
            metaHandler = new MetaDataHandler();
            if (!metaHandler.OpenMetaFile(new File(System.getProperty("user.dir") + rs.getString(1)))) {
                Logger.getLogger(TracefileHandler.class.getName()).info("Error in Loading meta files for " + TraceFile.getTraceFileName());
                //System.out.println("[" + NOW() + "] Error in Loading meta files for " + TraceFile.getTraceFileName());
                return false;
            }
            if (!metaHandler.loadSubMetaHanlders()) {
                Logger.getLogger(TracefileHandler.class.getName()).info("Error in Loading sub meta files for " + metaHandler.getMetaFilePath());
                //System.out.println("[" + NOW() + "] Error in Loading sub meta files for " + metaHandler.getMetaFilePath());
                return false;
            }
            overallLineCounter = rs.getInt(2);
            //JOptionPane.showMessageDialog(null,"New Model:"+metaHandler.getModel());
            TableHan.setTestModel(new DefaultTableModel(new Object[][]{}, metaHandler.getModel()));

            //JOptionPane.showMessageDialog(null,"Openning table:"+TraceFile.getTraceFileName());
            if (overallLineCounter > 10000) {
                pageLineLimit = 10000;
                pageCounter = 1;
                currentLineCounter = pageLineLimit * pageCounter;
                st.execute("select * from " + TraceFile.getTraceFileName() + " LIMIT 0," + currentLineCounter + ";");
            } else {
                pageCounter = 1;
                pageLineLimit = 10000;
                currentLineCounter = overallLineCounter;
                st.execute("select * from " + TraceFile.getTraceFileName() + ";");
            }


            inserting = true;
            TableHan.addToTable2(st, metaHandler.getModel().length);
            inserting = false;
            TableHandler.setSqlupdatequery("");
            jTable1.setModel(TableHan.getTestModel());
            statusBar.setText("<html><font color=#0033FF>Selected Trace File:" + TraceFile.getTraceFileName() + "...</font></html>");
            Logger.getLogger(TracefileHandler.class.getName()).info("Succesfully updated JTable with Trace File from database.");
            //System.out.println("[" + NOW() + "] Succesfully updated JTable with Trace File from database.");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    /**
     * This method is called when it is needed to identify which meta file
     * matches the inputed trace file. It reads the trace file line by line
     * until a match has been established. It uses the number of elements of
     * each trace file and the unique flags to identify if the trace file
     * structure matches a meta files contents.
     *
     * @param inputFile the trace file that needs to be identified
     * @return MetaDataHandler, the meta data handler that matches the trace
     * file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public MetaDataHandler matchMetaHandler(File inputFile) throws FileNotFoundException, IOException {
        MetaFiles metaFiles = new MetaFiles();
        ArrayList<MetaDataHandler> metaHandler = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String data = new String();
        String[] elements = null;
        int uniqueCounter = 0;
        int matchingMetaFileIndex = -1;
        int i;
        int metaEntriesStartPos = 0;

        for (i = 0; i < metaFiles.getMetaFileNumber(); i++) {
            MetaDataHandler meta = new MetaDataHandler();
            meta.OpenMetaFile(metaFiles.getMetaFile(i));
            meta.setMetaFilePath(metaFiles.getMetaFilePaths(i));
            metaHandler.add(meta);
        }
        //JOptionPane.showMessageDialog(null,"Number of MetaHandlers:"+metaHandler.size());

        while ((data = br.readLine()) != null) {

            data = data.trim();
            elements = data.split("\\s+");

            for (i = 0; i < metaHandler.size(); i++) {
                metaEntriesStartPos = metaHandler.get(i).getMetaEntries().size() - metaHandler.get(i).getFields_index();
                //JOptionPane.showMessageDialog(null,"Starting Position for metaEntries:"+metaEntriesStartPos);
                //System.out.println("NumberOfFields:"+metaHandler.get(i).getMetaEntries().get(0).getNumberOfFields());
                //System.out.println("UniqueCounter:"+metaHandler.get(i).getMetaEntries().get(1).getUniqueCounter());
                //System.out.println("******Checking for match MetaFile:"+i);
                //JOptionPane.showMessageDialog(null, "Checking Metafile:"+metaFiles.getMetaFile(i).getName());
                //JOptionPane.showMessageDialog(null,"Checking Line:"+data);
                uniqueCounter = 0;
                //JOptionPane.showMessageDialog(null, "Elements Length"+elements.length+"Column Counter"+metaHandler.get(i).getMetaEntries().get(1).getColumnCounter());

                if (!(elements.length >= Integer.parseInt(metaHandler.get(i).getMetaEntries().get(1).getColumnCounter()))) {
                    //System.out.println("Element Counter matched.UniqueCounter="+uniqueCounter);
                    continue;

                }
                //int elementCounter=0;
                uniqueCounter = 0;
                int j;
                for (j = 0; j < elements.length; j++) {
                    if (metaHandler.get(i).getMetaEntries().get(j + metaEntriesStartPos).getName() != null) //JOptionPane.showMessageDialog(null,"Checking MetaEntry for -unique flag:"+metaHandler.get(i).getMetaEntries().get(j+metaEntriesStartPos).getName());
                    {
                        if (metaHandler.get(i).getMetaEntries().get(j + metaEntriesStartPos).getUnique() != null) {
                            //System.out.println(metaHandler.get(i).getMetaEntries().get(j+metaEntriesStartPos).getName()+":"+metaHandler.get(i).getMetaEntries().get(j+metaEntriesStartPos).getUnique());
                            //System.out.println(elements[Integer.parseInt(metaHandler.get(i).getMetaEntries().get(j+metaEntriesStartPos).getIndex())]);
                            //JOptionPane.showMessageDialog(null,"Checking for unique character:"+metaHandler.get(i).getMetaEntries().get(j+metaEntriesStartPos).getUnique());
                            if (elements[Integer.parseInt(metaHandler.get(i).getMetaEntries().get(j + metaEntriesStartPos).getIndex())].indexOf(metaHandler.get(i).getMetaEntries().get(j + metaEntriesStartPos).getUnique()) != -1) {
                                uniqueCounter++;
                                if (uniqueCounter == Integer.parseInt(metaHandler.get(i).getMetaEntries().get(2).getUniqueCounter())) {
                                    break;
                                }

                                //JOptionPane.showMessageDialog(null, "Matched a unique:"+uniqueCounter);
                                //System.out.println("Unique Element Matched matched.UniqueCounter="+uniqueCounter);
                            } else {
                                break;
                            }
                        }
                    }
                    //JOptionPane.showMessageDialog(null,"J="+j+"Field Index:"+metaHandler.get(i).getFields_index());
                    if (j == metaHandler.get(i).getFields_index() - 1) {
                        break;
                    }
                }

                //JOptionPane.showMessageDialog(null, "Uniq.CounterMetaFile:"+metaHandler.get(i).getMetaEntries().get(2).getUniqueCounter()+"UniqueCounter:"+uniqueCounter);
                if (uniqueCounter == Integer.parseInt(metaHandler.get(i).getMetaEntries().get(2).getUniqueCounter())) {
                    matchingMetaFileIndex = i;
                    break;
                }

            }
            //JOptionPane.showMessageDialog(null, "Match Index:"+matchingMetaFileIndex);
            if (matchingMetaFileIndex != -1) {
                break;
            }
        }

        if (matchingMetaFileIndex != -1) {
            TraceFile.setIs_right_format(true);
            Logger.getLogger(TracefileHandler.class.getName()).info("Succesfully matched trace file with" + metaFiles.getMetaFile(matchingMetaFileIndex).getName() + " metafile.");
            //System.out.println("[" + NOW() + "] Succesfully matched trace file with" + metaFiles.getMetaFile(matchingMetaFileIndex).getName() + " metafile.");
            return metaHandler.get(matchingMetaFileIndex);
        } else {
            JOptionPane.showMessageDialog(null, "Trace File did not match with any MetaFile.", "Error.", JOptionPane.ERROR_MESSAGE);
            TraceFile.setIs_right_format(false);
            return null;
        }

    }
}
