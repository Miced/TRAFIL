package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This class creates the handler of each meta file that is used by other
 * classes to process trace files and create information and metrics.
 *
 * @author charalampi
 */
public class MetaDataHandler {

    private ArrayList<String> metadata = new ArrayList();
    private String createTableString = "";
    private String metaFilePath;
    private String metaFileName;
    private int fields_index;

    /**
     * Returns the field index.
     *
     * @return integer field index
     */
    public int getFields_index() {
	return fields_index;
    }
    private int columns_index;

    /**
     * Sets the path oof the metafile used for the handler.
     *
     * @param metaFilePath string the path of the metafile
     */
    public void setMetaFilePath(String metaFilePath) {
	this.metaFilePath = metaFilePath;
    }
    private String[][] columns;
    private String[] model;
    private String[] table;
    private ArrayList<MetaEntry> metaEntries = new ArrayList();
    private ArrayList<SubMetaDataHandler> subMetaHandlers = new ArrayList();
    private ArrayList<String> node = new ArrayList();
    private ArrayList<String> sendingNode = new ArrayList();
    private ArrayList<String> generatePackets = new ArrayList();
    private ArrayList<String> receivedPackets = new ArrayList();
    private ArrayList<String> sentPackets = new ArrayList();
    private ArrayList<String> forwardedPackets = new ArrayList();
    private ArrayList<String> droppedPackets = new ArrayList();

    /**
     * Returns the fields related with the Dropped Packets information that are
     * present in the metafile.
     *
     * @return a list of strings of all the fields related with Dropped Packets
     */
    public ArrayList<String> getDroppedPackets() {
	return droppedPackets;
    }

    /**
     * Returns the fields related with the Forwarded Packets information that
     * are present in the metafile.
     *
     * @return
     */
    public ArrayList<String> getForwardedPackets() {
	return forwardedPackets;
    }

    /**
     * Returns the fields related with the Generated Packets information that
     * are present in the metafile.
     *
     * @return a list of strings of all the fields related with Generated
     * Packets
     */
    public ArrayList<String> getGeneratePackets() {
	return generatePackets;
    }

    /**
     * Returns the fields related with the Received Packets information that are
     * present in the metafile.
     *
     * @return a list of strings of all the fields related with Received Packets
     */
    public ArrayList<String> getReceivedPackets() {
	return receivedPackets;
    }

    /**
     * Returns the fields related with the Sending Nodes information that are
     * present in the metafile.
     *
     * @return a list of strings of all the fields related with Sending
     */
    public ArrayList<String> getSendingNode() {
	return sendingNode;
    }

    /**
     * Returns the fields related with the Sent Packets information that are
     * present in the metafile.
     *
     * @return a list of strings of all the fields related with Sent Packets
     */
    public ArrayList<String> getSentPackets() {
	return sentPackets;
    }
    /**
     *
     */
    private String timeRelated, packetSize;

    /**
     * Returns the fields related with the information of the Nodes that are
     * present in the metafile.
     *
     * @return a list of strings of all the fields related with Nodes of the
     * simulation
     * @return
     */
    public ArrayList<String> getNode() {
	return node;
    }

    /**
     * Returns the fields related with the Packet Size information that are
     * present in the metafile.
     *
     * @return a list of strings of all the fields related with the Packet Size
     */
    public String getPacketSize() {
	return packetSize;
    }

    /**
     * Returns the fields related with the Time information that are present in
     * the metafile.
     *
     * @return a list of strings of all the fields related with Time
     */
    public String getTimeRelated() {
	return timeRelated;
    }

    /**
     * Returns the list of all sub meta handlers who belong to this metadata
     * handler.
     *
     * @return a list of sub metadata handlers
     */
    public ArrayList<SubMetaDataHandler> getSubMetaHandlers() {
	return subMetaHandlers;
    }

    /**
     * Returns the path of the metafile for this metadata handler.
     *
     * @return a string that is the path of this metadata handler.
     */
    public String getMetaFilePath() {
	return metaFilePath;
    }

    /**
     * Returns the string that has the names of all columns of the table created
     * in the database based on this metafile and its sub metafiles.
     *
     * @return string
     */
    public String getCreateTableString() {
	return createTableString;
    }

    /**
     * The table model for the trace files matched with this metafile.
     *
     * @return a string array with the names of the columns for the table model
     */
    public String[] getModel() {
	return table;
    }

    /**
     * Constructor of the metadata handler.
     */
    public MetaDataHandler() {
    }

    /**
     * Opens the metafile and reads all the information regarding the metadata
     * handler. Fills the metadata list and all the related variables.
     *
     * @param metaFile the file used to read the data for the metadata handler
     * @return boolean value, true if the metadata handler was loaded
     * successfully, false if not
     */
    public boolean OpenMetaFile(File metaFile) {
	try {

//            JOptionPane.showMessageDialog(null, "Opening Metafile:"+metaFile.getName());
	    metaFileName = metaFile.getName();
	    BufferedReader br = new BufferedReader(new FileReader(metaFile));
	    String data = new String();
	    String[] elements = null;
	    int modelColumnCounter = 0;
	    boolean nameSet, typeSet, indexSet, timeRelatedSet, nodeRelatedSet, packetSizeSet;
	    int j = 0;
	    data = br.readLine();
	    data = data.trim();
	    elements = data.split(" ");
	    createTableString = "";
	    metaEntries = new ArrayList();
	    metadata = new ArrayList();


	    /*
	     * Reading the NumberOfFields that describe the TraceFile and are
	     * independendant of the number of columns of the trace file.
	     */
	    if (elements[0].equalsIgnoreCase("NumberOfFields") && (elements.length == 2)) {
		columns = new String[Integer.parseInt(elements[1])][7];
		model = new String[Integer.parseInt(elements[1])];
		metaEntries.add(new MetaEntry());
		metaEntries.get(0).setNumberOfFields(elements[1]);
		//metaEntries.get(0).setName("NumberOfFields");
		fields_index = Integer.parseInt(elements[1]);
	    } else {
		JOptionPane.showMessageDialog(null, "<html>Error in first line.<br/>Metadata should start with the NumberofColumns.<html>");
		return false;
	    }
	    /*
	     * Reading the actual number of columns of the Trace File.
	     */
	    data = br.readLine();
	    data = data.trim();
	    elements = data.split(" ");
	    if (elements[0].equalsIgnoreCase("NumberOfColumns") && (elements.length == 2)) {
		metaEntries.add(new MetaEntry());
		metaEntries.get(1).setColumnCounter(elements[1]);
		//metaEntries.get(1).setName("ColumnCounter");
		columns_index = Integer.parseInt(elements[1]);
	    } else {
		JOptionPane.showMessageDialog(null, "<html>Error in second line.<br/>Metadata should contain the NumberofFields.<html>");
		return false;
	    }
	    /*
	     * Reading the number of Unique characters that will distinguish the
	     * kind of Trace File.
	     */
	    data = br.readLine();
	    data = data.trim();
	    elements = data.split(" ");
	    if (elements[0].equalsIgnoreCase("UniqueCounter") && (elements.length == 2)
		    && (Integer.parseInt(elements[1]) >= 0)) {
		metaEntries.add(new MetaEntry());
		metaEntries.get(2).setUniqueCounter(elements[1]);
		//metaEntries.get(2).setName("UniqueCounter");
	    } else {
		JOptionPane.showMessageDialog(null, "<html>Error in third line.<br/>Metadata should contain the UniqueCounter and be >0.<html>");
		return false;
	    }

	    while ((data = br.readLine()) != null) {

		nameSet = typeSet = indexSet = timeRelatedSet = nodeRelatedSet = packetSizeSet = false;
		data = data.trim();
		elements = data.split(" ");
		if (elements.length < 3) {
		    JOptionPane.showMessageDialog(null, "Error. Metadata not in the correct format.Elements missing.");
		    JOptionPane.showMessageDialog(null, metaFile.getAbsolutePath());
		    return false;
		} else {
		    metaEntries.add(new MetaEntry());
		    for (int i = 0; i < elements.length; i++) {
			if (elements[i].equalsIgnoreCase("-name") && (i == 0) && !nameSet) {

			    model[modelColumnCounter++] = elements[i + 1];
			    metaEntries.get(j + 3).setName(elements[i + 1]);
			    createTableString = createTableString + elements[i + 1];
			    nameSet = true;

			    i++;
			} else if (elements[i].equalsIgnoreCase("-type") && (i == 2) && !typeSet) {
			    if (elements[i + 1].equalsIgnoreCase("hexadecimal") || elements[i + 1].equalsIgnoreCase("hex")) {
				elements[i + 1] = "int";

				metaEntries.get(j + 3).setConvertHex("1");

			    } else {

				metaEntries.get(j + 3).setConvertHex("0");
			    }


			    metaEntries.get(j + 3).setType(elements[i + 1]);
			    createTableString = createTableString + " " + elements[i + 1] + ",";
			    typeSet = true;

			    i++;
			} else if (elements[i].equalsIgnoreCase("-index") && (i == 4) && !indexSet) {

			    metaEntries.get(j + 3).setIndex(elements[i + 1]);
			    indexSet = true;
			    i++;
			} else if (elements[i].equalsIgnoreCase("-startsWith")) {

			    metaEntries.get(j + 3).setStartsWith(elements[i + 1]);

			    i++;
			} else if (elements[i].equalsIgnoreCase("-endsWith")) {

			    metaEntries.get(j + 3).setEndsWith(elements[i + 1]);
			    i++;
			} else if (elements[i].equalsIgnoreCase("-delimiter")) {

			    metaEntries.get(j + 3).setDelimiter(elements[i + 1]);
			    i++;
			} else if (elements[i].equalsIgnoreCase("-unique")) {

			    metaEntries.get(j + 3).setUnique(elements[i + 1]);
			    i++;
			} else if (elements[i].equalsIgnoreCase("-TimeRelated")) {
			    if ((elements.length == 3) && (elements[i + 1].equalsIgnoreCase("-column"))) {
				timeRelated = elements[i + 2];
				i = i + 3;
				timeRelatedSet = true;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading TimeRelated flag.");
				return false;
			    }

			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-NodeRelated")) {
			    if ((elements.length == 3) && (elements[i + 1].equalsIgnoreCase("-column"))) {
				node.add(elements[i + 2]);
				i = i + 3;

			    } else if (elements.length == 5) {
				node.add(elements[i + 2]);
				node.add(elements[i + 4]);
				i = 5;
				nodeRelatedSet = true;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading NodeRelated flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-PacketSize")) {
			    if ((elements.length == 3) && (elements[i + 1].equalsIgnoreCase("-column"))) {
				packetSize = elements[i + 2];
				i = i + 3;
				packetSizeSet = true;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading PacketSize flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-SendingNodes")) {
			    if ((elements.length == 5) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column"))) {
				sendingNode.add(elements[i + 2]);
				sendingNode.add(elements[i + 4]);
				i = i + 5;
			    } else if ((elements.length == 7) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column")) && (elements[i + 5].equalsIgnoreCase("-column"))) {
				sendingNode.add(elements[i + 2]);
				sendingNode.add(elements[i + 4]);
				sendingNode.add(elements[i + 6]);
				i = i + 7;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading Sending nodes flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-GeneratedPackets")) {
			    if ((elements.length == 5) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column"))) {
				generatePackets.add(elements[i + 2]);
				generatePackets.add(elements[i + 4]);
				i = i + 5;
			    } else if ((elements.length == 7) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column")) && (elements[i + 5].equalsIgnoreCase("-column"))) {
				generatePackets.add(elements[i + 2]);
				generatePackets.add(elements[i + 4]);
				generatePackets.add(elements[i + 6]);
				i = i + 7;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading GeneratedPackets flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-ReceivedPackets")) {
			    if ((elements.length == 5) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column"))) {
				receivedPackets.add(elements[i + 2]);
				receivedPackets.add(elements[i + 4]);
				i = i + 5;
			    } else if ((elements.length == 7) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column")) && (elements[i + 5].equalsIgnoreCase("-column"))) {
				receivedPackets.add(elements[i + 2]);
				receivedPackets.add(elements[i + 4]);
				receivedPackets.add(elements[i + 6]);
				i = i + 7;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading Received Packets flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-ForwardedPackets")) {
			    if ((elements.length == 7) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column")) && (elements[i + 5].equalsIgnoreCase("-column"))) {
				forwardedPackets.add(elements[i + 2]);
				forwardedPackets.add(elements[i + 4]);
				forwardedPackets.add(elements[i + 6]);
				i = i + 7;
			    } else if ((elements.length == 9) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column")) && (elements[i + 5].equalsIgnoreCase("-column"))) {
				forwardedPackets.add(elements[i + 2]);
				forwardedPackets.add(elements[i + 4]);
				forwardedPackets.add(elements[i + 6]);
				forwardedPackets.add(elements[i + 8]);
				i = i + 9;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading ForwardedPackets flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-SentPackets")) {
			    if ((elements.length == 5) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column"))) {
				sentPackets.add(elements[i + 2]);
				sentPackets.add(elements[i + 4]);
				i = i + 5;
			    } else if ((elements.length == 7) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column")) && (elements[i + 5].equalsIgnoreCase("-column"))) {
				sentPackets.add(elements[i + 2]);
				sentPackets.add(elements[i + 4]);
				sentPackets.add(elements[i + 6]);
				i = i + 7;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading SentPackets flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else if (elements[i].equalsIgnoreCase("-DroppedPackets")) {
			    if ((elements.length == 3) && (elements[i + 1].equalsIgnoreCase("-column"))) {
				droppedPackets.add(elements[i + 2]);

				i = i + 3;
			    } else if ((elements.length == 7) && (elements[i + 1].equalsIgnoreCase("-column")) && (elements[i + 3].equalsIgnoreCase("-column")) && (elements[i + 5].equalsIgnoreCase("-column"))) {
				sentPackets.add(elements[i + 2]);
				sentPackets.add(elements[i + 4]);
				sentPackets.add(elements[i + 6]);
				i = i + 7;
			    } else {
				JOptionPane.showMessageDialog(null, "Error while reading DroppedPackets flag.");
				return false;
			    }
			    metaEntries.remove(metaEntries.size() - 1);
			} else {
			    JOptionPane.showMessageDialog(null, "Error. Metadata not in the correct format. Flag not recognized:" + elements[i]);
			    System.out.println("Error detected at:");
			    System.out.println(data);
			    return false;
			}


		    }

		}
		j++;
		/*
		 * if((!nameSet||!typeSet||!indexSet)&&(!timeRelatedSet||!nodeRelatedSet||!packetSizeSet)){
		 * JOptionPane.showMessageDialog(null,"Error. Metadata not in
		 * the correct format.Mandatory Flag missing.");
		 * createTableString=""; metadata.clear(); return false; }
		 */
	    }

	    if ((metaEntries.size() - 3) != fields_index) {
		JOptionPane.showMessageDialog(null, "<html>Error.NumberOfFields does not match the actual numer of fields.<html>");
		//createTableString = "";
		metadata.clear();
		return false;
	    }


//            JOptionPane.showMessageDialog(null, "<html>Create Table Strin:<br/><html>"+createTableString);
//
//            System.out.println("*******PRINTING MODEL ARRAY*********");
//            for (int k=0; k<model.length; k++){
//            System.out.println(model[k]);
//            }
//
//            System.out.println("*******PRINTING METAENTRIES*********");
//            for (int k=0; k<metaEntries.size(); k++){
//            if(!(metaEntries.get(k).getName()==null))
//            System.out.println("Index :"+k+" name:"+metaEntries.get(k).getName());
//            }
	    return true;
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(MetaDataHandler.class.getName()).log(Level.SEVERE, null, ex);
	    return false;
	} catch (IOException ex) {
	    Logger.getLogger(MetaDataHandler.class.getName()).log(Level.SEVERE, null, ex);
	    return false;
	}
    }

    /**
     * Creates the table to the database for the trace file that the metahandler
     * was matched to.
     *
     * @param FileName the name of the trace file
     * @param st the statement to execute the queries to the database
     */
    public void createTable(String FileName, Statement st) {
	String sql;
	if (createTableString.contains("UniquePacketID")) {
	    sql = "create table " + FileName + " (Id int NOT NULL AUTO_INCREMENT," + createTableString + "PRIMARY KEY(Id), INDEX(UniquePacketID),INDEX(time)) ENGINE = MYISAM;";

	} else if (createTableString.contains("EventIdentifier")) {
	    sql = "create table " + FileName + " (Id int NOT NULL AUTO_INCREMENT," + createTableString + "PRIMARY KEY(Id),INDEX(EventIdentifier),INDEX(time)) ENGINE = MYISAM;";
	} else {
	    sql = "create table " + FileName + " (Id int NOT NULL AUTO_INCREMENT," + createTableString + "PRIMARY KEY(Id),INDEX(UniqueID),INDEX(time)) ENGINE = MYISAM;";

	}
	System.out.println(sql);
	try {
	    if (!st.execute(sql)) {
		//JOptionPane.showMessageDialog(null, "Succesfull MySQL table Creation.");
		Logger.getLogger(MetaDataHandler.class.getName()).info("Succesfull MySQL table Creation for trace file:" + FileName + "\n" + createTableString);
		//System.out.println("[" + date.toString() + "] Succesfull MySQL table Creation for trace file:"+FileName);
	    }

	} catch (SQLException ex) {
	    Logger.getLogger(MetaDataHandler.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * returns the column index
     *
     * @return integer, column index of the metafile
     */
    public int getColumnsIndex() {
	return this.columns_index;
    }

    /**
     * Returns the list of meta entries of the metadata handler.
     *
     * @return a list of metaentries for the metahandler
     */
    public ArrayList<MetaEntry> getMetaEntries() {
	return metaEntries;
    }

    /**
     * Loads all the sub metadatahandlers who belong to the metadatahandler.
     *
     * @return boolean, true if they sub metahandlers were loaded without
     * problems, false if there were problems during the loading
     */
    public boolean loadSubMetaHanlders() {
	SubMetaFiles subMetaFiles = new SubMetaFiles(metaFileName.replace(".txt", ""));
	SubMetaDataHandler subMeta;
	subMetaHandlers = new ArrayList();
	int tableElements = model.length;
	try {
	    for (int i = 0; i < subMetaFiles.getMetaFileNumber(); i++) {
		subMeta = new SubMetaDataHandler();
		if (!subMeta.OpenMetaFile(subMetaFiles.getSubMetaFile(i))) {
		    Logger.getLogger(MetaDataHandler.class.getName()).severe("Error opening " + subMetaFiles.getSubMetaFile(i).getName() + " sub mtafile.");
		    //System.out.println("["+date.toString()+"] Error opening "+subMetaFiles.getSubMetaFile(i).getName()+" sub mtafile.");
		    return false;
		}
		subMeta.setMetaFilePath(subMetaFiles.getMetaFilePaths(i));
		createTableString += subMeta.getCreateTableString();
		tableElements += subMeta.getModel().length;
		subMetaHandlers.add(subMeta);
	    }
	    //JOptionPane.showMessageDialog(null,"Table Columns:"+tableElements);
	    table = new String[tableElements];

	    System.arraycopy(model, 0, table, 0, model.length);
	    tableElements = model.length;
	    //JOptionPane.showMessageDialog(null,"Table Elements after basic model:"+tableElements);
	    if (subMetaHandlers.size() > 0) {
		System.arraycopy(subMetaHandlers.get(0).getModel(), 0, table, model.length, subMetaHandlers.get(0).getModel().length);
		tableElements += subMetaHandlers.get(0).getModel().length;
	    }
	    //JOptionPane.showMessageDialog(null,"Table Elements after first sub handler:"+tableElements+"New Model Length:"+subMetaHandlers.get(1).getModel().length);
	    //JOptionPane.showMessageDialog(null,"Table Model:"+table.length);
	    //JOptionPane.showMessageDialog(null,"Number Of sub Metahandlers:"+subMetaHandlers.size());
	    for (int i = 1; i < subMetaHandlers.size(); i++) {
		System.arraycopy(subMetaHandlers.get(i).getModel(), 0, table, tableElements, subMetaHandlers.get(i).getModel().length);
		tableElements += subMetaHandlers.get(i).getModel().length;
		//JOptionPane.showMessageDialog(null,"Table Elements after "+i+" sub handler:"+tableElements);
	    }
	    return true;
	} catch (Exception ex) {
	    Logger.getLogger(MetaDataHandler.class.getName()).severe("Error opening sub metafiles.");
	    //System.out.println("["+date.toString()+"] Error opening sub metafiles.");
	    return false;

	}
	//System.out.println("****PRINTING TABLE MODEL");
	//for (int i=0; i<table.length; i++){
	//System.out.println(table[i]);
	//}
	//JOptionPane.showMessageDialog(null,"Number of SubMetaHandlers:"+subMetaHandlers.size());
	//JOptionPane.showMessageDialog(null,"New Table String including sub meta:"+createTableString);

    }
}
