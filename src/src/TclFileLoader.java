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
    private ArrayList<String[]> connectedAgentsList = new ArrayList<>();
    private ArrayList<String[]> wirelessSettings = new ArrayList<>();
    private String scriptFileName = null;
    private String scriptFinishTime = null;

    public boolean OpenTclFile(File tclFile) {
        String data;

        ArrayList<String[]> unassignedAgents = new ArrayList<>();
        ArrayList<String[]> agentsList = new ArrayList<>();

        Pattern tracefilenamePattern = Pattern.compile("\\[open (.*?).tr w\\]");
        Pattern nodesPattern = Pattern.compile("#\\{TRAFIL\\} node n([0-9]+) xpos=([0-9]+.[0-9]+) ypos=([0-9]+.[0-9]+) ([a-zA-Z0-9]+)");
        Pattern linksPattern = Pattern.compile("\\$ns (.+?) \\$(n[0-9]+) \\$(n[0-9]+) (.+?) (.+?) (.+)");
        Pattern agentsPattern = Pattern.compile("set (.+?) \\[new Agent/(.+?)\\]");
        Pattern assignAgentPattern = Pattern.compile("attach-agent \\$(n[0-9]+) \\$(.+)");
        Pattern applicationsPattern = Pattern.compile("set (.+?) \\[new Application/(.+?)\\]");
        Pattern applicationSettingsPattern = Pattern.compile("\\$(.+?) set (.+?)_ (.+)");
        Pattern assignApplicationPattern = Pattern.compile("\\$(.+?) attach-agent \\$\\b([a-zA-Z0-9]+)\\b");
        Pattern nodeSchedulesPattern = Pattern.compile("\\$ns at (.+?) \"\\$(.+?) (.+?)\"");
        Pattern simulationFinishPattern = Pattern.compile("\\$ns at (.+?) \"finish\"");
        Pattern agentConnectionPattern = Pattern.compile("\\$ns connect \\$(.+?) \\$(.+)");
        Pattern topographyPattern = Pattern.compile("\\$topo load_flatgrid ([0-9]+) ([0-9]+)");
        Pattern nodeConfigPattern = Pattern.compile("\\$ns node-config");
        Pattern nodeConfigParametersPattern = Pattern.compile(" -(.+?) \\b([a-zA-Z0-9/\\$]+)\\b");

        System.out.println("Reading file...");
        try {
            BufferedReader br = new BufferedReader(new FileReader(tclFile));

            while (((data = br.readLine()) != null)) {
                //data = data.trim();
                Matcher m = tracefilenamePattern.matcher(data);
                if (m.find()) {
                    scriptFileName = m.group(1);
                    System.out.println("Trace file: " + scriptFileName + ".tr");
                }

                /* DETECTING NODES */
                m = nodesPattern.matcher(data);
                if (m.find()) {
                    if ("wired".equals(m.group(4))) {
                        TclDesignWiredNode wnode = new TclDesignWiredNode(Integer.parseInt(m.group(1)));
                        wnode.setRectangle(new Rectangle2D.Double(Float.parseFloat(m.group(2)), Float.parseFloat(m.group(3)), wnode.getWidth(), wnode.getHeight()));
                        wiredNodeList.add(wnode);
                        System.out.println("Wired Node: " + wnode.getName());
                    } else {
                        TclDesignWirelessNode wlnode = new TclDesignWirelessNode(Integer.parseInt(m.group(1)));
                        wlnode.setCircle(new Ellipse2D.Double(Float.parseFloat(m.group(2)), Float.parseFloat(m.group(3)), wlnode.getWidth(), wlnode.getHeight()));
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
                    for (String[] agentDetails : unassignedAgents) {
                        if (agentDetails[0].equals(m.group(2))) {
                            String[] agent = new String[]{m.group(1), m.group(2), agentDetails[1]};
                            agentsList.add(agent);
                            unassignedAgents.remove(agentDetails);
                            if (agentDetails[1].equalsIgnoreCase("Null")) {
                                setNodeProperties(agent, null, null, null);
                            }
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
                    for (String[] temp : agentsList) {
                        if (m.group(2).equals(temp[1])) {
                            agent = temp;
                            break;
                        }
                    }

                    while (((data = br.readLine()) != null)) {
                        m = nodeSchedulesPattern.matcher(data);
                        if (m.find()) {
                            break;
                        }
                    }

                    do {
                        System.out.println("Adding " + m.group(3) + " time on agent " + m.group(2) + ": " + m.group(1));
                        settings.add(new String[]{m.group(3), m.group(1)});
                        if ((data = br.readLine()) != null) {
                            m = nodeSchedulesPattern.matcher(data);
                        } else {
                            break;
                        }
                    } while (m.find());

                    if (agent != null) {
                        setNodeProperties(agent, appName, appType, settings);
                    }
                }

                /* DETECTING SIMULATION SCHEDULE */
                m = simulationFinishPattern.matcher(data);
                if (m.find()) {
                    System.out.println("Adding finish time: " + m.group(1));
                    scriptFinishTime = m.group(1);
                }

                /* DETECTING CONNECTED AGENTS */
                m = agentConnectionPattern.matcher(data);
                if (m.find()) {
                    String sendingNode = null;
                    String receivingNode = null;
                    for (TclDesignNode wnode : wiredNodeList) {
                        if (wnode.getAttachedAgent().equals(m.group(1))) {
                            sendingNode = wnode.getName();
                        } else if (wnode.getAttachedAgent().equals(m.group(2))) {
                            receivingNode = wnode.getName();
                        }
                    }
                    for (TclDesignNode wlnode : wirelessNodeList) {
                        if (wlnode.getAttachedAgent().equals(m.group(1))) {
                            sendingNode = wlnode.getName();
                        } else if (wlnode.getAttachedAgent().equals(m.group(2))) {
                            receivingNode = wlnode.getName();
                        }
                    }

                    connectedAgentsList.add(new String[]{sendingNode, receivingNode});
                    System.out.println("Connecting agent " + m.group(1) + " of " + sendingNode + " to agent " + m.group(2) + " of " + receivingNode);
                }

                /* DETECTING TOPOGRAPHY */
                m = topographyPattern.matcher(data);
                if (m.find()) {
                    wirelessSettings.add(new String[]{"width", m.group(1)});
                    wirelessSettings.add(new String[]{"height", m.group(2)});
                }

                m = nodeConfigPattern.matcher(data);
                if (m.find()) {
                    m = nodeConfigParametersPattern.matcher(data);
                    while (m.find()) {
                        System.out.println("Parameter: " + m.group(1) + " Value: " + m.group(2));
                        wirelessSettings.add(new String[]{m.group(1), m.group(2)});
                        if ((data = br.readLine()) != null) {
                            m = nodeConfigParametersPattern.matcher(data);
                        } else {
                            break;
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

    private void setNodeProperties(String[] agent, String appName, String appType, ArrayList<String[]> settings) {
        TclDesignNode node;
        boolean found = false;
        for (TclDesignWiredNode wnode : wiredNodeList) {
            if (wnode.getName().equals(agent[0])) {
                node = wnode;
                NodePropertiesWindow properties = new NodePropertiesWindow(node, agent[1], agent[2], appName, appType, settings);
                node.setProperties(properties);
                found = true;
                break;
            }
        }
        if (!found) {
            for (TclDesignWirelessNode wlnode : wirelessNodeList) {
                if (wlnode.getName().equals(agent[0])) {
                    node = wlnode;
                    NodePropertiesWindow properties = new NodePropertiesWindow(node, agent[1], agent[2], appName, appType, settings);
                    node.setProperties(properties);
                    break;
                }
            }
        }
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

    public String getScriptFileName() {
        return scriptFileName;
    }

    public String getScriptFinishTime() {
        return scriptFinishTime;
    }

    public ArrayList<String[]> getConnectedAgentsList() {
        return connectedAgentsList;
    }

    public ArrayList<String[]> getWirelessSettings() {
        return wirelessSettings;
    }
}
