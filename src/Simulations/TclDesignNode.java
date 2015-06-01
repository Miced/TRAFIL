/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulations;

import static Simulations.TclDesignerPanel.connectedAgentsModel;
import static Simulations.TclDesignerPanel.connectedAgentsWindow;
import static Simulations.TclDesignerPanel.linkListModel;
import UI.NodePropertiesWindow;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Drakoulelis
 */
public abstract class TclDesignNode {

    protected int width = 20;
    protected int height = 20;
    protected NodePropertiesWindow properties;
    protected Color borderColor = Color.BLACK;
    protected String name;
    protected String attachedAgent;
    protected String attachedApp;
    protected ArrayList<TclDesignNode> adjacentNodes = new ArrayList<>();
    private DefaultCellEditor dce;
    private boolean rowExists = false;

    public TclDesignNode() {
        this.properties = new NodePropertiesWindow(this);
        properties.setNewAgent();
    }

    /**
     * This method uses a recursive algorithm to discover the tree formed by the
     * adjacent nodes.The root of the tree is the one end of a new link, and the
     * rest of the tree is formed by any adjacent nodes that already exist
     * before the link is formed.
     *
     * @param nodesToAdd: The nodes to be added to the adjacent nodes list.
     * @param exploredNodesTree: The nodes that we have traversed so far in the
     * node tree.
     */
    public ArrayList addAdjacentNodes(ArrayList<TclDesignNode> nodesToAdd, ArrayList<TclDesignNode> exploredNodesTree) {
        exploredNodesTree.add(this);

        for (TclDesignNode adjacent : adjacentNodes) {
            if (!exploredNodesTree.contains(adjacent)) {
                exploredNodesTree = adjacent.addAdjacentNodes(nodesToAdd, exploredNodesTree);
            }
        }

        for (TclDesignNode treeNode : nodesToAdd) {
            if (!adjacentNodes.contains(treeNode) && treeNode != this) {
                adjacentNodes.add(treeNode);
            }
        }

        if (rowExists == true) {
            updateAdjacentNodes();
        }

        return exploredNodesTree;
    }

    /**
     * This method refreshes the combobox that contains the adjacent nodes
     */
    public void updateAdjacentNodes() {

        DefaultTableModel model = connectedAgentsWindow.getModel();
        int index = -1;
        for (int row = model.getRowCount() - 1; row >= 0; row--) {
            if (name.equals(model.getValueAt(row, 0))) {
                index = row;
                break;
            }
        }

        ArrayList<String> adjacentNodeNames = new ArrayList<>();
        adjacentNodeNames.add("");
        for (TclDesignNode adjacentNode : adjacentNodes) {
            if (adjacentNode.getAttachedAgent().contains("null")) {
                adjacentNodeNames.add(adjacentNode.getName());
            }
            // TODO: clean any existing selections if agent's not null.
        }
        JComboBox comboBox = new JComboBox(adjacentNodeNames.toArray());
        dce = new DefaultCellEditor(comboBox);
        connectedAgentsWindow.getAttachedNodeEditors().add(index, dce);
    }

    public void addToConnectedAgents() {
        removeFromConnectedAgents();
        JComboBox comboBox;
        if (rowExists == false) {
            connectedAgentsModel.addRow(new Object[]{name, attachedAgent, null, null});
            rowExists = true;
            if (adjacentNodes.isEmpty()) {
                comboBox = new JComboBox(new String[]{null});
                dce = new DefaultCellEditor(comboBox);
                connectedAgentsWindow.getAttachedNodeEditors().add(dce);
            }
        }
        updateAdjacentNodes();
    }

    public void removeFromConnectedAgents() {
        DefaultTableModel model = connectedAgentsWindow.getModel();
        for (int row = model.getRowCount() - 1; row >= 0; row--) {
            if (name.equals(model.getValueAt(row, 0))) {
                model.removeRow(row);
                connectedAgentsWindow.getAttachedNodeEditors().remove(row);
                rowExists = false;
                break;
            }
        }
    }

    public ArrayList<TclDesignNode> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void deleteNode() {
        removeFromConnectedAgents();
        removeFromLinkListWindow();
    }

    private void removeFromLinkListWindow() {
        DefaultTableModel model = linkListModel;
        for (int row = model.getRowCount() - 1; row >= 0; row--) {
            if (name.equals(model.getValueAt(row, 4)) || name.equals(model.getValueAt(row, 5))) {
                model.removeRow(row);
            }
        }
    }

    public void setAttachedAgent(String agent) {
        attachedAgent = agent;
    }

    public String getAttachedAgent() {
        return attachedAgent;
    }

    public String getAttachedApp() {
        return attachedApp;
    }

    public void setAttachedApp(String attachedApp) {
        this.attachedApp = attachedApp;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public NodePropertiesWindow getProperties() {
        return properties;
    }

    public void setProperties(NodePropertiesWindow properties) {
        this.properties = properties;
    }

    public void setName(String name) {
        this.name = name;
        this.properties.getNodeNameField().setText(name);
    }

    public String getName() {
        return name;
    }
}
