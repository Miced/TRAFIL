/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import Simulations.TclDesignLink;
import Simulations.TclDesignNode;
import Simulations.TclDesignWiredNode;
import Simulations.TclDesignWirelessNode;
import static Simulations.TclDesignerPanel.linkListModel;
import UI.NodePropertiesWindow;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
    private Collection<TclDesignLink> linkList = new ArrayList<>();

    public boolean OpenTclFile(File tclFile) {
	String data;

	ArrayList<String[]> unassignedAgents = new ArrayList<>();
	ArrayList<String[]> agentsList = new ArrayList<>();

	Pattern tracefilenamePattern = Pattern.compile("\\[open (.*?).tr w\\]");
	Pattern nodesPattern = Pattern.compile("\\[\\$ns node\\]");
	Pattern linksPattern = Pattern.compile("\\$ns (.+?) \\$(n[0-9]+) \\$(n[0-9]+) (.+?) (.+?) (.+?) #\\{TRAFIL\\} link");
	Pattern agentsPattern = Pattern.compile("set (.+?) \\[new Agent/(.+?)\\]");
	Pattern assignAgentPattern = Pattern.compile("attach-agent \\$(n[0-9]+) \\$(.+)");
	Pattern applicationsPattern = Pattern.compile("set (.+?) \\[new Application/(.+?)\\]");
	Pattern applicationSettingsPattern = Pattern.compile("\\$(.+?) set (.+?)_ (.+)");
	Pattern assignApplicationPattern = Pattern.compile("\\$(.+?) attach-agent \\$(.+)");
	Pattern nodeSchedulesPattern = Pattern.compile("\\$ns at (.+?) \"\\$(.+?) (.+?)\"");

	System.out.println("Reading file...");
	try {
	    BufferedReader br = new BufferedReader(new FileReader(tclFile));

	    while (((data = br.readLine()) != null)) {
		//data = data.trim();
		Matcher m = tracefilenamePattern.matcher(data);
		if (m.find()) {
		    String tracefilename = m.group(1);
		    System.out.println("Trace file: " + tracefilename + ".tr");
		}

		/* DETECTING NODES */
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

		/* DETECTING LINKS */
		m = linksPattern.matcher(data);
		if (m.find()) {
		    TclDesignLink link = new TclDesignLink();
		    String linkType = m.group(1);
		    String queue = m.group(6);
		    String bandwidth = m.group(4);
		    String delay = m.group(5);
		    int i = 0;
		    for (TclDesignWiredNode node : wiredNodeList) {
			if (node.getName().equals(m.group(2))) {
			    link.setStartingNode(node);
			    i++;
			} else if (node.getName().equals(m.group(3))) {
			    link.setEndingNode(node);
			    i++;
			}

			// If both starting node and ending node have been found in the current list, then add the link.
			// WARNING: This requires that the node declaration is always before the link declaration.
			if (i == 2) {
			    System.out.println("Link from node " + m.group(2) + " to node " + m.group(3));
			    link.setLine(new Line2D.Double(link.getStartingNode().getRectangle().getX() + link.getStartingNode().getWidth() / 2, link.getStartingNode().getRectangle().getY() + link.getStartingNode().getHeight() / 2, link.getEndingNode().getRectangle().getX() + link.getEndingNode().getWidth() / 2, link.getEndingNode().getRectangle().getY() + link.getEndingNode().getHeight() / 2));
			    linkList.add(link);
			    linkListModel.addRow(new Object[]{
				linkType,
				queue,
				bandwidth,
				delay,
				link.getStartingNode().getProperties().getNodeNameField().getText(),
				link.getEndingNode().getProperties().getNodeNameField().getText()});
			    break;
			}
		    }
		}

		/* DETECTING AGENTS */
		m = agentsPattern.matcher(data);
		if (m.find()) {
		    unassignedAgents.add(new String[]{m.group(1), m.group(2)});
		}

		m = assignAgentPattern.matcher(data);
		if (m.find()) {
		    for (String[] agent : unassignedAgents) {
			if (agent[0].equals(m.group(2))) {
			    agentsList.add(new String[]{m.group(1), m.group(2), agent[1]});
			    unassignedAgents.remove(agent);
			    break;
			}
		    }
		}

		/* DETECTING APPLICATIONS */
		m = applicationsPattern.matcher(data);
		if (m.find()) {
		    String appName = m.group(1);
		    String appType = m.group(2);

		    m = assignApplicationPattern.matcher(data);
		    ArrayList<String[]> settings = new ArrayList<>();
		    while (((data = br.readLine()) != null) && !m.find()) {
			m = assignApplicationPattern.matcher(data);
			Matcher m2 = applicationSettingsPattern.matcher(data);
			if (m2.find()) {
			    System.out.println("Setting parameter " + m2.group(2) + " to value: " + m2.group(3));
			    settings.add(new String[]{m2.group(2), m2.group(3)});
			}
		    }
		    System.out.println("Saving settings of application " + m.group(1) + " on agent " + m.group(2));

		    String[] agent = null;
		    TclDesignNode node;
		    for (String[] temp : agentsList) {
			if (m.group(2).equals(temp[1])) {
			    agent = temp;
			    break;
			}
		    }

		    m = nodeSchedulesPattern.matcher(data);
		    while (((data = br.readLine()) != null) && !m.find()) {
			m = nodeSchedulesPattern.matcher(data);
		    }

		    while (m.find()) {
			System.out.println("Adding " + m.group(3) + " time on agent " + m.group(2) + ": " + m.group(1));
			if ((data = br.readLine()) != null) {
			    m = nodeSchedulesPattern.matcher(data);
			}
		    }

		    if (agent != null) {
			boolean found = false;
			for (TclDesignWiredNode wnode : wiredNodeList) {
			    if (wnode.getName().equals(agent[0])) {
				node = wnode;
				setNodeProperties(node, agent, appName, appType, settings);
				found = true;
				break;
			    }
			}
			if (!found) {
			    for (TclDesignWirelessNode wlnode : wirelessNodeList) {
				if (wlnode.getName().equals(agent[0])) {
				    node = wlnode;
				    setNodeProperties(node, agent, appName, appType, settings);
				    break;
				}
			    }
			}
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

    private void setNodeProperties(TclDesignNode node, String[] agent, String appName, String appType, ArrayList<String[]> settings) {
	NodePropertiesWindow properties = new NodePropertiesWindow(node, agent[1], agent[2], appName, appType, settings);
	node.setProperties(properties);
    }

    public Collection<TclDesignWiredNode> getWiredNodeList() {
	return wiredNodeList;
    }

    public Collection<TclDesignWirelessNode> getWirelessNodeList() {
	return wirelessNodeList;
    }

    public Collection<TclDesignLink> getLinkList() {
	return linkList;
    }
}
