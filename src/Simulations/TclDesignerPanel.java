/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulations;

import UI.LinkListWindow;
import UI.ConnectedAgentsWindow;
import UI.SimulationWirelessSettingsWindow;
import UI.TRAFIL;
import static UI.TRAFIL.statusBar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Drakoulelis
 */
public class TclDesignerPanel extends JPanel {

    public static TRAFIL trafil;
    public static LinkListWindow linkWindow;
    public static DefaultTableModel linkListModel;
    public static ConnectedAgentsWindow connectedAgentsWindow;
    public static DefaultTableModel connectedAgentsModel;
    private static SimulationWirelessSettingsWindow wirelessSettings;
    private Collection<TclDesignWiredNode> wiredNodeList = new ArrayList<>();
    private Collection<TclDesignWirelessNode> wirelessNodeList = new ArrayList<>();
    private Collection<TclDesignLink> linkList = new ArrayList<>();
    private boolean linkPaintFlag = false;
    private TclDesignLink tempLink;
    private int x, y, nodeID = 0;

    public TclDesignerPanel() {
	setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
	setBackground(Color.GRAY);
	setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

	/* Adding the mouseListeners that react to the Design Panel. */
	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {

		x = e.getX();
		y = e.getY();

		/* If node already exists, popup properties, or show right click menu. */
		if (!trafil.getNewLinkButton().isSelected()) {
		    for (TclDesignWiredNode wnode : wiredNodeList) {
			if (wnode.getRectangle().contains(e.getPoint())) {
			    if (e.isPopupTrigger()) {
				RightClickMenu menu = new RightClickMenu((TclDesignNode) wnode);
				menu.show(e.getComponent(), e.getX(), e.getY());
			    } else if (e.getClickCount() == 2) {
				wnode.getProperties().setVisible(true);
			    }
			    return;
			}
		    }

		    for (TclDesignWirelessNode wlnode : wirelessNodeList) {
			if (wlnode.getCircle().contains(e.getPoint())) {
			    if (e.isPopupTrigger()) {
				RightClickMenu menu = new RightClickMenu((TclDesignNode) wlnode);
				menu.show(e.getComponent(), e.getX(), e.getY());
			    } else if (e.getClickCount() == 2) {
				wlnode.getProperties().setVisible(true);
			    }
			    return;
			}
		    }
		}

		/* If node does not exist, create a new item depending on the pressed button. */
		if (trafil.getNewWiredNodeButton().isSelected()) {

		    System.out.println("New Wired Node");
		    TclDesignWiredNode wnode = new TclDesignWiredNode(nodeID++);
		    wnode.setRectangle(new Rectangle2D.Double(x, y, wnode.getWidth(), wnode.getHeight()));
		    wiredNodeList.add(wnode);
		    repaint(x, y, wnode.getWidth() + 1, wnode.getHeight() + 1);	// Repainting on the newly drawn rectangle

		} else if (trafil.getNewWirelessNodeButton().isSelected()) {

		    System.out.println("New Wireless Node");
		    TclDesignWirelessNode wlnode = new TclDesignWirelessNode(nodeID++);
		    wlnode.setCircle(new Ellipse2D.Double(x, y, wlnode.getWidth(), wlnode.getHeight()));
		    wirelessNodeList.add(wlnode);
		    ArrayList<TclDesignNode> exploredNodesTree = new ArrayList<>();
		    ArrayList<TclDesignNode> nodesToAdd;
		    nodesToAdd = new ArrayList<TclDesignNode>(wirelessNodeList);
		    wlnode.addAdjacentNodes(nodesToAdd, exploredNodesTree);
		    nodesToAdd = new ArrayList<TclDesignNode>(wirelessNodeList);
		    nodesToAdd.add(wlnode);
		    wirelessNodeList.iterator().next().addAdjacentNodes(nodesToAdd, exploredNodesTree);
		    repaint(x, y, wlnode.getWidth() + 1, wlnode.getHeight() + 1);   // Repainting on the newly drawn circle

		} else if (trafil.getNewLinkButton().isSelected()) {

		    /* Checking if the user is pressing an existing node */
		    for (TclDesignWiredNode wnode : wiredNodeList) {
			// TODO: Check if line already exists!!!
			if (wnode.getRectangle().getBounds2D().contains(x, y)) {
			    if (!linkPaintFlag) {
				System.out.println("New Link");
				tempLink = new TclDesignLink();
				tempLink.setStartingNode(wnode);
				tempLink.getStartingNode().setBorderColor(Color.GREEN);
				linkPaintFlag = true;	// Mark that a link is being drawn
				repaint();
			    } else {
				if (tempLink.getStartingNode() == wnode) {
				    linkPaintFlag = false;
				    return;
				} else {
				    System.out.println("Link finished");
				    tempLink.setEndingNode(wnode);

				    // In case user clicks the same node, no link is commited.
				    if (tempLink.getStartingNode() == tempLink.getEndingNode()) {
					tempLink.getStartingNode().setBorderColor(tempLink.getStartingNode().borderColor);
					return;
				    }
				    tempLink.getEndingNode().setBorderColor(new Color(0, 128, 0));
				    tempLink.getStartingNode().setBorderColor(new Color(0, 128, 0));
				    tempLink.setLine(new Line2D.Double(tempLink.getStartingNode().getRectangle().getX() + tempLink.getStartingNode().getWidth() / 2, tempLink.getStartingNode().getRectangle().getY() + tempLink.getStartingNode().getHeight() / 2, tempLink.getEndingNode().getRectangle().getX() + tempLink.getEndingNode().getWidth() / 2, tempLink.getEndingNode().getRectangle().getY() + tempLink.getEndingNode().getHeight() / 2));
				    linkList.add(tempLink);

				    // Update links list
				    linkListModel.addRow(new Object[]{
					"Duplex-link",
					"DropTail",
					"1Mb",
					"10ms",
					tempLink.getStartingNode().getProperties().getNodeNameField().getText(),
					tempLink.getEndingNode().getProperties().getNodeNameField().getText()});
				    linkWindow.setRowCount(linkWindow.getRowCount() + 1);

				    // Update connectedAgentsWindow attached nodes and available agents
				    ArrayList<TclDesignNode> exploredNodesTree = new ArrayList<>();
				    ArrayList<TclDesignNode> nodesToAdd = new ArrayList<>(tempLink.getEndingNode().getAdjacentNodes());
				    nodesToAdd.add(tempLink.getEndingNode());
				    tempLink.getStartingNode().addAdjacentNodes(nodesToAdd, exploredNodesTree);
				    exploredNodesTree.clear();
				    nodesToAdd = new ArrayList<>(tempLink.getStartingNode().getAdjacentNodes());
				    nodesToAdd.add(tempLink.getStartingNode());
				    tempLink.getEndingNode().addAdjacentNodes(nodesToAdd, exploredNodesTree);

				    linkPaintFlag = false;	// Mark that the link has been drawn
				    repaint();  // Paint the new line
				}
			    }
			    return;
			}
		    }
		}
	    }
	});

	addMouseMotionListener(new MouseAdapter() {
	    @Override
	    public void mouseDragged(MouseEvent e) {

		/* Checking if the user is pressing an existing node */
		int dx = e.getX() - x;
		int dy = e.getY() - y;

		for (TclDesignWiredNode wnode : wiredNodeList) {
		    if (wnode.getRectangle().getBounds2D().contains(x, y)) {
			wnode.getRectangle().x += dx;
			wnode.getRectangle().y += dy;

			/* Move any connected to this node links */
			for (TclDesignLink link : linkList) {
			    if (link.getStartingNode() == wnode) {
				link.getLine().setLine(link.getLine().getX1() + dx, link.getLine().getY1() + dy, link.getLine().getX2(), link.getLine().getY2());
			    } else if (link.getEndingNode() == wnode) {
				link.getLine().setLine(link.getLine().getX1(), link.getLine().getY1(), link.getLine().getX2() + dx, link.getLine().getY2() + dy);
			    }
			}
			repaint();
			x += dx;
			y += dy;
			return;
		    }
		}

		for (TclDesignWirelessNode wlnode : wirelessNodeList) {
		    if (wlnode.getCircle().getBounds2D().contains(x, y)) {
			wlnode.getCircle().x += dx;
			wlnode.getCircle().y += dy;
			repaint();
			x += dx;
			y += dy;
			return;
		    }
		}
	    }
	});
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	Graphics2D g2d = (Graphics2D) g;

	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	for (TclDesignWiredNode wnode : wiredNodeList) {
	    wnode.paintSquare(g2d);
	}

	for (TclDesignWirelessNode wlnode : wirelessNodeList) {
	    wlnode.paintCircle(g2d);
	}

	for (TclDesignLink link : linkList) {
	    link.paintLink(g2d);
	}
    }

    public static void setTrafil(TRAFIL trafil) {
	TclDesignerPanel.trafil = trafil;
    }

    public static void setLinkWindow(LinkListWindow linkWindow) {
	TclDesignerPanel.linkWindow = linkWindow;
	linkListModel = linkWindow.getModel();
    }

    public void setWiredNodeList(Collection<TclDesignWiredNode> wiredNodeList) {
	this.wiredNodeList = wiredNodeList;
	repaint();
    }

    public void setWirelessNodeList(Collection<TclDesignWirelessNode> wirelessNodeList) {
	this.wirelessNodeList = wirelessNodeList;
	repaint();
    }

    public void setLinkList(Collection<TclDesignLink> linkList) {
	this.linkList = linkList;
	repaint();
    }

    public static void setConnectedAgents(ConnectedAgentsWindow connectedAgents) {
	TclDesignerPanel.connectedAgentsWindow = connectedAgents;
	connectedAgentsModel = connectedAgents.getModel();
    }

    public static void setWirelessSettings(SimulationWirelessSettingsWindow wirelessSettings) {
	TclDesignerPanel.wirelessSettings = wirelessSettings;
    }

    public String getNodeAgentByNodeName(String name) {
	for (TclDesignWiredNode node : wiredNodeList) {
	    if (node.getName().equals(name)) {
		return node.getAttachedAgent();
	    }
	}
	return null;
    }

    public String getScript() {
	String outputContent = "";

	outputContent += "# ======================================================================\n"
		+ "# Main Program\n"
		+ "# ======================================================================\n";

	// Start our simulation file
	outputContent += "#Create a simulator object\n"
		+ "set ns [new Simulator]\n"
		+ "set tracefd     [open tracefile.tr w]\n"
		+ "$ns trace-all $tracefd\n\n";

	// Create the finish procedure of the script. It is a default procedure.
	outputContent += "#Define a 'finish' procedure\n"
		+ "proc finish {} {\n"
		+ "        global ns\n"
		+ "        $ns flush-trace\n"
		+ "	#Close the trace file\n"
		+ "        exit 0\n"
		+ "}\n\n";

	// If wireless mode, add topography and node config
	if (wirelessNodeList.size() > 0) {
	    outputContent += "# set up topography object\n"
		    + "set topo       [new Topography]\n"
		    + "\n";
	    if (!wirelessSettings.getTopologyInstance().equals("")) {
		outputContent += "$topo load_flatgrid " + wirelessSettings.getTopologyInstance() + " \n";
	    } else {
		statusBar.setText("Topography size not set. Please define it at the wireless settings menu.");
		statusBar.paintImmediately(statusBar.getVisibleRect());
		return "error";
	    }

	    outputContent += "# configure node\n"
		    + "\n"
		    + "        $ns_ node-config -adhocRouting " + wirelessSettings.getAdhocRouting().getSelectedItem().toString() + " \\\n"
		    + "			 -llType " + wirelessSettings.getLlType().getText() + " \\\n"
		    + "			 -macType " + wirelessSettings.getMacType().getText() + " \\\n"
		    + "			 -ifqType " + wirelessSettings.getIfqType().getText() + " \\\n"
		    + "			 -ifqLen " + wirelessSettings.getIfqLen().getText() + " \\\n"
		    + "			 -antType " + wirelessSettings.getAntType().getText() + " \\\n"
		    + "			 -propType " + wirelessSettings.getPropagationType().getText() + " \\\n"
		    + "			 -phyType " + wirelessSettings.getPhyType().getText() + " \\\n"
		    + "			 -channelType " + wirelessSettings.getChannelType().getSelectedItem().toString() + " \\\n"
		    + "			 -topoInstance $topo \\\n";
	    if (!wirelessSettings.getChannel().getText().equals("")) {
		outputContent += "			 -channel " + wirelessSettings.getChannel().getText() + " \\\n";
	    }
	    if (!wirelessSettings.getAddressingType().getSelectedItem().toString().equals("")) {
		outputContent += "			 -addressType " + wirelessSettings.getAddressingType().getSelectedItem().toString() + " \\\n";
	    }
	    if (!wirelessSettings.getEnergyModel().getText().equals("")) {
		outputContent += "			 -energyModel " + wirelessSettings.getEnergyModel().getText() + " \\\n";
	    }
	    if (!wirelessSettings.getInitialEnergy().getText().equals("")) {
		outputContent += "			 -initialEnergy " + wirelessSettings.getInitialEnergy().getText() + " \\\n";
	    }
	    if (!wirelessSettings.getRxPower().getText().equals("")) {
		outputContent += "			 -rxPower " + wirelessSettings.getRxPower().getText() + " \\\n";
	    }
	    if (!wirelessSettings.getTxPower().getText().equals("")) {
		outputContent += "			 -txPower " + wirelessSettings.getTxPower().getText() + " \\\n";
	    }
	    if (!wirelessSettings.getIdlePower().getText().equals("")) {
		outputContent += "			 -idlePower " + wirelessSettings.getIdlePower().getText() + " \\\n";
	    }
	    if (!wirelessSettings.getSleepPower().getText().equals("")) {
		outputContent += "			 -sleepPower " + wirelessSettings.getSleepPower().getText() + " \\\n";
	    }
	    if (!wirelessSettings.getSleepTime().getText().equals("")) {
		outputContent += "			 -sleepTime " + wirelessSettings.getSleepTime().getText() + " \\\n";
	    }
	    if (wirelessSettings.getWiredRouting().isSelected()) {
		outputContent += "			 -wiredRouting ON \\\n";
	    } else {
		outputContent += "			 -wiredRouting OFF \\\n";
	    }
	    if (wirelessSettings.getMobileIP().isSelected()) {
		outputContent += "			 -mobileIP ON \\\n";
	    } else {
		outputContent += "			 -mobileIP OFF \\\n";
	    }
	    if (wirelessSettings.getAgentTrace().isSelected()) {
		outputContent += "			 -agentTrace ON \\\n";
	    } else {
		outputContent += "			 -agentTrace OFF \\\n";
	    }
	    if (wirelessSettings.getRouterTrace().isSelected()) {
		outputContent += "			 -routerTrace ON \\\n";
	    } else {
		outputContent += "			 -routerTrace OFF \\\n";
	    }
	    if (wirelessSettings.getMacTrace().isSelected()) {
		outputContent += "			 -macTrace ON \\\n";
	    } else {
		outputContent += "			 -macTrace OFF \\\n";
	    }
	    if (wirelessSettings.getPhyTrace().isSelected()) {
		outputContent += "			 -phyTrace ON \\\n";
	    } else {
		outputContent += "			 -phyTrace OFF \\\n";
	    }
	    if (wirelessSettings.getMovementTrace().isSelected()) {
		outputContent += "			 -movementTrace ON \\\n";
	    } else {
		outputContent += "			 -movementTrace OFF \\\n";
	    }
	    if (wirelessSettings.getEotTrace().isSelected()) {
		outputContent += "			 -eotTrace ON \n";
	    } else {
		outputContent += "			 -eotTrace OFF \n";
	    }

	    outputContent += "\n";
	}

	// Add nodes according to TclDesignerPanel's nodelist, if they exist
	if (wiredNodeList.size() > 0 || wirelessNodeList.size() > 0) {
	    outputContent += "#Create nodes\n";
	}

	for (TclDesignWiredNode wnode : wiredNodeList) {
	    outputContent += "set " + wnode.getProperties().getNodeNameField().getText() + " [$ns node] #{TRAFIL} xpos=" + wnode.getRectangle().getX() + " ypos=" + wnode.getRectangle().getY() + " wired\n";
	}

	for (TclDesignWirelessNode wlnode : wirelessNodeList) {
	    outputContent += "set " + wlnode.getProperties().getNodeNameField().getText() + " [$ns node] #{TRAFIL} xpos=" + wlnode.getCircle().getX() + " ypos=" + wlnode.getCircle().getY() + " wireless\n";
	    outputContent += "$" + wlnode.getProperties().getNodeNameField().getText() + " random-motion 0\n";
	}
	outputContent += "\n";

	// Add links to our script, if they exist
	DefaultTableModel model = linkWindow.getModel();
	if (model.getRowCount() > 0) {
	    outputContent += "#Create links between the nodes\n";
	}
	for (int i = 0; i < model.getRowCount(); i++) {
	    String type = model.getValueAt(i, 0).toString().toLowerCase();
	    String startingNode = model.getValueAt(i, 4).toString();
	    String endingNode = model.getValueAt(i, 5).toString();
	    String bandwidth = model.getValueAt(i, 2).toString();
	    String queue = model.getValueAt(i, 1).toString();
	    String delay = model.getValueAt(i, 3).toString();

	    outputContent += "$ns " + type + " $" + startingNode + " $" + endingNode + " " + bandwidth + " " + delay + " " + queue + " #{TRAFIL} link\n";
	}
	outputContent += "\n";

	for (TclDesignWiredNode wnode : wiredNodeList) {
	    outputContent = addScriptNodeDetails(outputContent, wnode);
	}

	for (TclDesignWirelessNode wlnode : wirelessNodeList) {
	    outputContent = addScriptNodeDetails(outputContent, wlnode);
	}

	// Check for possible connections with sink agents, then connect them
	outputContent += "#Connect the traffic source with the traffic sink\n";
	for (int i = 0; i < connectedAgentsModel.getRowCount(); i++) {
	    outputContent += "$ns connect $" + connectedAgentsModel.getValueAt(i, 1) + " $" + connectedAgentsModel.getValueAt(i, 3) + "\n";
	}
	outputContent += "\n";

	return outputContent;
    }

    public static String addScriptNodeDetails(String outputContent, TclDesignNode node) {
	String agent = node.getAttachedAgent();
	String name = node.getName();
	String app = node.getAttachedApp();
	// Add agents and attach them to nodes
	outputContent += "#Create agent " + agent + " and attach them to node " + name + "\n";
	switch (node.getProperties().getAgentBox().getSelectedItem().toString()) {
	    case "UDP":
		outputContent += "set " + agent + " [new Agent/UDP]\n"
			+ "$ns attach-agent $" + name + " $" + agent + "\n\n";
		break;
	    case "Null":
		outputContent += "set " + agent + " [new Agent/Null]\n"
			+ "$ns attach-agent $" + name + " $" + agent + "\n\n";
		break;
	    default:
		outputContent += "set " + agent + " [new Agent/TCP]\n"
			+ "$ns attach-agent $" + name + " $" + agent + "\n\n";
		break;
	}


	if (!node.getProperties().getAgentBox().getSelectedItem().toString().equals("Null")) {
	    // Create traffic sources and attach them to agent
	    outputContent += "#Create traffic sources and attach them to agent " + agent + "\n";
	    switch (node.getProperties().getAppBox().getSelectedItem().toString()) {
		case "CBR":
		    if (node.getProperties().getPacketSize().getText().equals("")) {
			statusBar.setText("No packet size selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "set " + app + " [new Application/Traffic/CBR]\n"
				+ "$" + app + " set packetSize_ " + node.getProperties().getPacketSize().getText() + "\n";
		    }
		    if (node.getProperties().getRateRadioButton().isSelected()) {
			if (node.getProperties().getSendingRate().getText().equals("")) {
			    statusBar.setText("No rate selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			    statusBar.paintImmediately(statusBar.getVisibleRect());
			    return "error";
			} else {
			    outputContent += "$" + app + " set rate_ " + node.getProperties().getSendingRate().getText() + "\n";
			}
		    } else {
			if (node.getProperties().getSendingInterval().getText().equals("")) {
			    statusBar.setText("No interval selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			    statusBar.paintImmediately(statusBar.getVisibleRect());
			    return "error";
			} else {
			    outputContent += "$" + app + " set interval_ " + node.getProperties().getSendingInterval().getText() + "\n";
			}
		    }
		    if (node.getProperties().getRandomBox().isSelected()) {
			outputContent += "$" + app + " set random_ 1\n";
		    }
		    if (!node.getProperties().getPacketNumberBox().getText().equals("")) {
			outputContent += "$" + app + " set packetSize_ " + node.getProperties().getPacketNumberBox().getText() + "\n";
		    }
		    outputContent += "$" + app + " attach-agent $" + agent + "\n\n";
		    outputContent += "#Schedule events for the " + app + " source\n";
		    outputContent += "$ns at " + node.getProperties().getAppStartTime().getText() + " \"$" + app + " start\"\n";
		    outputContent += "$ns at " + node.getProperties().getAppStopTime().getText() + " \"$" + app + " stop\"\n\n";
		    break;
		case "FTP":
		    outputContent += "set " + app + " [new Application/FTP]\n";
		    if (!node.getProperties().getFTPpacketNumber().getText().equals("")) {
			outputContent += "$" + app + " set maxpkts_ " + node.getProperties().getFTPpacketNumber().getText() + "\n";
		    }
		    break;
		case "Telnet":
		    outputContent += "set " + app + " [new Application/Telnet]\n";
		    if (!node.getProperties().getTelnetInterval().getText().equals("")) {
			outputContent += "$" + app + " set interval_ " + node.getProperties().getTelnetInterval().getText() + "\n";
		    }
		    break;
		case "Exponential":
		    if (node.getProperties().getExpoPacketSize().getText().equals("")) {
			statusBar.setText("No packet size selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "set " + app + " [new Application/Traffic/Exponential]\n"
				+ "$" + app + " set packetSize_ " + node.getProperties().getExpoPacketSize().getText() + "\n";
		    }
		    if (node.getProperties().getExpoSendingRate().getText().equals("")) {
			statusBar.setText("No rate selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "$" + app + " set rate_ " + node.getProperties().getExpoSendingRate().getText() + "\n";
		    }
		    if (node.getProperties().getBurstTime().getText().equals("")) {
			statusBar.setText("No burst time selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "$" + app + " set burst_time_ " + node.getProperties().getBurstTime().getText() + "\n";
		    }
		    if (node.getProperties().getIdleTime().getText().equals("")) {
			statusBar.setText("No idle time selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "$" + app + " set idle_time_ " + node.getProperties().getIdleTime().getText() + "\n";
		    }
		    break;
		case "Pareto":
		    if (node.getProperties().getExpoPacketSize().getText().equals("")) {
			statusBar.setText("No packet size selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "set " + app + " [new Application/Traffic/Pareto]\n"
				+ "$" + app + " set packetSize_ " + node.getProperties().getExpoPacketSize().getText() + "\n";
		    }
		    if (node.getProperties().getExpoSendingRate().getText().equals("")) {
			statusBar.setText("No rate selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "$" + app + " set rate_ " + node.getProperties().getExpoSendingRate().getText() + "\n";
		    }
		    if (node.getProperties().getBurstTime().getText().equals("")) {
			statusBar.setText("No burst time selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "$" + app + " set burst_time_ " + node.getProperties().getBurstTime().getText() + "\n";
		    }
		    if (node.getProperties().getIdleTime().getText().equals("")) {
			statusBar.setText("No idle time selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "$" + app + " set idle_time_ " + node.getProperties().getIdleTime().getText() + "\n";
		    }
		    if (node.getProperties().getParetoShape().getText().equals("")) {
			statusBar.setText("No shape selected at node " + node.getProperties().getNodeNameField().getText() + ".");
			statusBar.paintImmediately(statusBar.getVisibleRect());
			return "error";
		    } else {
			outputContent += "$" + app + " set shape_ " + node.getProperties().getParetoShape().getText() + "\n";
		    }
		    break;
		default:
		    break;
	    }
	}

	return outputContent;
    }

    public void deleteNode(TclDesignNode node) {

	node.deleteNode();
	if (node instanceof TclDesignWiredNode) {
	    wiredNodeList.remove((TclDesignWiredNode) node);
	    TclDesignWiredNode temp = (TclDesignWiredNode) node;
	    for (TclDesignLink link : linkList) {
		if (link.getStartingNode() == temp || link.getEndingNode() == temp) {
		    linkList.remove(link);
		}
	    }
	} else {
	    wirelessNodeList.remove((TclDesignWirelessNode) node);
	}
	repaint();
    }

    class RightClickMenu extends JPopupMenu {

	JMenuItem properties, delete;

	public RightClickMenu(final TclDesignNode node) {
	    properties = new JMenuItem("Properties");
	    delete = new JMenuItem("Delete");

	    properties.addActionListener(new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    propertiesActionPerformed(evt);
		}

		private void propertiesActionPerformed(ActionEvent evt) {
		    node.properties.setVisible(true);
		}
	    });

	    delete.addActionListener(new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    propertiesActionPerformed(evt);
		}

		private void propertiesActionPerformed(ActionEvent evt) {
		    deleteNode(node);
		}
	    });

	    add(properties);
	    add(delete);
	}
    }
}
