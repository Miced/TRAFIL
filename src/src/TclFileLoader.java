/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import Simulations.TclDesignWiredNode;
import Simulations.TclDesignWirelessNode;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    private Collection<TclDesignWiredNode> wiredNodeList = new ArrayList<>();
    private Collection<TclDesignWirelessNode> wirelessNodeList = new ArrayList<>();

    public boolean OpenTclFile(File tclFile) {
	String data;

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
		    if (data.substring(data.lastIndexOf(" ") + 1).equals("wired")) {
			TclDesignWiredNode wnode = new TclDesignWiredNode(0);
			String[] split = data.split(" ");
			wnode.setName(split[1]);
			int xpos = Integer.parseInt(data.substring(data.indexOf("xpos=") + "xpos=".length(), data.indexOf(".", data.indexOf("xpos="))));
			int ypos = Integer.parseInt(data.substring(data.indexOf("ypos=") + "ypos=".length(), data.indexOf(".", data.indexOf("ypos="))));
			wnode.setRectangle(new Rectangle2D.Double(xpos, ypos, wnode.getWidth(), wnode.getHeight()));
			wiredNodeList.add(wnode);
			System.out.println("Wired Node: " + wnode.getName());
		    } else {
			TclDesignWirelessNode wlnode = new TclDesignWirelessNode(0);
			String[] split = data.split(" ");
			wlnode.setName(split[1]);
			int xpos = Integer.parseInt(data.substring(data.indexOf("xpos=") + "xpos=".length(), data.indexOf(".", data.indexOf("xpos="))));
			int ypos = Integer.parseInt(data.substring(data.indexOf("ypos=") + "ypos=".length(), data.indexOf(".", data.indexOf("ypos="))));
			wlnode.setCircle(new Ellipse2D.Double(xpos, ypos, wlnode.getWidth(), wlnode.getHeight()));
			wirelessNodeList.add(wlnode);
			System.out.println("Wireless Node: " + wlnode.getName());
		    }
		}


	    }
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(TclFileLoader.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(TracefileHandler.class.getName()).info("IO error while reading data from file.");
	    //System.out.println("[" + NOW() + "] IO Exception while reading data from file.");
	    return false;
	}

	return true;
    }

    public Collection<TclDesignWiredNode> getWiredNodeList() {
	return wiredNodeList;
    }

    public Collection<TclDesignWirelessNode> getWirelessNodeList() {
	return wirelessNodeList;
    }
}
