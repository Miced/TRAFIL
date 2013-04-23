/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import Simulations.TclDesignNode;
import Simulations.TclDesignWiredNode;
import Simulations.TclDesignWirelessNode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Drakoulelis
 */
public class TclFileLoader {

    public boolean OpenTclFile(File tclFile) {
	String data;
	Collection<TclDesignWiredNode> wiredNodeList = new ArrayList<TclDesignWiredNode>();
	Collection<TclDesignWirelessNode> wirelessNodeList = new ArrayList<TclDesignWirelessNode>();

	Pattern tracefilenamePattern = Pattern.compile("\\[open (.*?).tr w\\]");
	Pattern nodesPattern = Pattern.compile("\\[\\$ns node\\]");

	System.out.println("Reading file...");
	try {
	    BufferedReader br = new BufferedReader(new FileReader(tclFile));

	    while (((data = br.readLine()) != null)) {
		//data = data.trim();
		Matcher m = tracefilenamePattern.matcher(data);
		if (m.find()) {
		    String tracefilename = m.group(1);
		    System.out.println("Trace file: " + tracefilename);
		}

		m = nodesPattern.matcher(data);
		if (m.find()) {
		    TclDesignWiredNode wnode = new TclDesignWiredNode(0);
		    String[] split = data.split(" ");
		    wnode.setName(split[1]);
		    System.out.println(split[1]);
		}
	    }
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(TclFileLoader.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(TracefileHandler.class.getName()).info("IO error while reading data from file.");
	    //System.out.println("[" + NOW() + "] IO Exception while reading data from file.");
	    return false;
	}

	return false;

    }
}
