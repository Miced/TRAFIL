/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DesignWindow.java
 *
 * Created on 13 Μαρ 2012, 8:04:19 μμ
 */
package UI;

import java.awt.event.MouseEvent;
import java.awt.MouseInfo;
import javax.swing.*;
import javax.swing.tree.*;
import src.QueryHandler;
import java.util.*;
import javax.swing.text.*;

/**
 *
 * @author Σοφία
 */
//abstract public class Design {}
public class DesignWindow extends javax.swing.JFrame {

    private TreePath path;
    private QueryHandler handler;
    private ArrayList<String> result;
    private JList PopupMenuList;
    private DefaultTreeModel treeModel;
    private DefaultListModel listModel;
    private DefaultListModel fieldModel;
    private DefaultListModel popupMenuListModel;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode mNode;
    private DefaultMutableTreeNode conditionParent;
    private String query;
    private String condition;
    private ConditionWindow where;

    DesignWindow() {

	handler = new QueryHandler();
	result = new ArrayList();

	where = new ConditionWindow();
	where.addWindowListener(new java.awt.event.WindowAdapter() {
	    public void windowDeactivated(java.awt.event.WindowEvent evt) {

		condition = where.getCondition();
		createNode(condition, conditionParent);
	    }
	});

	initComponents();

	root = new DefaultMutableTreeNode("ROOTQUERY");
	TreeNode node1 = createNode("SELECT", root);
	TreeNode node2 = createNode("FROM", root);
	TreeNode node3 = createNode("WHERE", root);
	TreeNode node4 = createNode("Add condition...", (DefaultMutableTreeNode) node3);
	TreeNode node5 = createNode("GROUP BY", root);
	TreeNode node6 = createNode("HAVING", root);
	TreeNode node7 = createNode("Add condition...", (DefaultMutableTreeNode) node6);
	TreeNode node8 = createNode("SORT BY", root);


	result = handler.executeQuery("Show tables;");
	listModel = new DefaultListModel();
	for (int i = 0; i < result.size(); i = i + 2) {
	    listModel.addElement(result.get(i));
	}
	tables.setModel(listModel);

	fieldModel = new DefaultListModel();
	fields.setModel(fieldModel);

	PopupMenuList = new JList();
	DefaultListModel PopupMenuListModel = new DefaultListModel();
	PopupMenuListModel.addElement("Add to GROUP BY");
	PopupMenuListModel.addElement("Add to SORT BY (ASC)");
	PopupMenuListModel.addElement("Add to SORT BY (DESC)");
	PopupMenuList.setModel(PopupMenuListModel);
	PopupMenuList.addMouseListener(new java.awt.event.MouseAdapter() {
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
		PopupMenuListMouseClicked(evt);
	    }
	});
	jPopupMenu1.add(PopupMenuList);
	jPopupMenu1.pack();

	this.setVisible(true);

    }

    private TreeNode createNode(String label, DefaultMutableTreeNode parent) {

	DefaultMutableTreeNode child = null;
	path = QueryTree.getNextMatch(label, 0, Position.Bias.Forward);
	if (path != null && ((DefaultMutableTreeNode) path.getLastPathComponent()).getParent().equals(parent)) {
	    return null;
	} else {
	    child = new DefaultMutableTreeNode(label);

	    parent.add(child);
	    treeModel = (DefaultTreeModel) QueryTree.getModel();
	    treeModel.setRoot(root);
	    QueryTree.setModel(treeModel);
	    for (int i = 0; i < QueryTree.getRowCount(); i++) {
		QueryTree.expandRow(i);
	    }
	    return child;
	}
    }

    private void createQuery() {

	query = "SELECT\n";
	String value = null;
	Boolean flag = false;
	Boolean whereFlag = false;
	Boolean groupByFlag = false;
	Boolean havingFlag = false;
	Boolean sortByFlag = false;
	DefaultMutableTreeNode node;
	DefaultMutableTreeNode parent;

	path = QueryTree.getNextMatch("WHERE", 0, Position.Bias.Forward);
	node = (DefaultMutableTreeNode) path.getLastPathComponent();
	if (node.getChildCount() > 1) {
	    whereFlag = true;
	}
	path = QueryTree.getNextMatch("GROUP BY", 0, Position.Bias.Forward);
	node = (DefaultMutableTreeNode) path.getLastPathComponent();
	if (node.getChildCount() > 0) {
	    groupByFlag = true;
	}
	path = QueryTree.getNextMatch("HAVING", 0, Position.Bias.Forward);
	node = (DefaultMutableTreeNode) path.getLastPathComponent();
	if (node.getChildCount() > 1) {
	    havingFlag = true;
	}
	path = QueryTree.getNextMatch("SORT BY", 0, Position.Bias.Forward);
	node = (DefaultMutableTreeNode) path.getLastPathComponent();
	if (node.getChildCount() > 0) {
	    sortByFlag = true;
	}
	Enumeration en = root.depthFirstEnumeration();

	while (en.hasMoreElements()) {
	    node = (DefaultMutableTreeNode) en.nextElement();
	    parent = (DefaultMutableTreeNode) node.getParent();
	    if ((parent.getUserObject().toString().equals("SELECT")
		    || parent.getUserObject().toString().equals("FROM")
		    || parent.getUserObject().toString().equals("GROUP BY")
		    || parent.getUserObject().toString().equals("SORT BY"))
		    && (parent.getChildAfter(node) != null)) {

		flag = true;

	    } else {
		flag = false;
	    }
	    value = node.getUserObject().toString();
	    if (value.equals("SELECT")) {
		value = "FROM";
	    } else if (value.equals("FROM")) {
		if (whereFlag) {
		    value = "WHERE";
		} else {
		    continue;
		}
	    } else if (value.equals("WHERE")) {
		if (groupByFlag) {
		    value = "GROUP BY";
		} else {
		    continue;
		}
	    } else if (value.equals("GROUP BY")) {
		if (havingFlag) {
		    value = "HAVING";
		} else {
		    continue;
		}
	    } else if (value.equals("HAVING")) {
		if (sortByFlag) {
		    value = "ORDER BY";
		} else {
		    continue;
		}
	    } else if (value.equals("SORT BY")) {
		break;
	    } else if (value.equals("Add condition...")) {
		continue;
	    }

	    if (flag) {
		query = query + (value + ",\n");
	    } else {
		query = query + (value + "\n");
	    }
	}

	query = query + ";";
	return;
    }

    public String getQuery() {
	return query;
    }

    /**
     * Creates new form DesignWindow
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        tables = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        QueryTree = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        fields = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        CreateQueryButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        tables.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        tables.setName(""); // NOI18N
        tables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tables);

        QueryTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                QueryTreeMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(QueryTree);

        fields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fieldsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(fields);

        jLabel1.setForeground(new java.awt.Color(51, 0, 204));
        jLabel1.setText("Tables (double click to select)");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setForeground(new java.awt.Color(0, 0, 204));
        jLabel2.setText("Fields (double click to add)");

        CreateQueryButton.setText("Create Query");
        CreateQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateQueryButtonActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 0, 204));
        jLabel3.setText("Query Tree (double-click to remove node, right-click to add to group or sort by)");
        jLabel3.setAutoscrolls(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(CreateQueryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CreateQueryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void QueryTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QueryTreeMouseClicked

	jPopupMenu1.setVisible(false);

	try {
	    treeModel = (DefaultTreeModel) QueryTree.getModel();
	    path = QueryTree.getSelectionPath();
	    mNode = (DefaultMutableTreeNode) path.getLastPathComponent();
	} catch (NullPointerException ex) {
	    return;
	}

	if (evt.getClickCount() == 2) {

	    if (mNode.toString().equals("Add condition...")) {
		conditionParent = (DefaultMutableTreeNode) mNode.getParent();
		if (conditionParent.getChildCount() > 1) {
		    where.setLogicalOperator(true);
		}
		where.setVisible(true);
		return;
	    }
	    if (mNode.getLevel() > 1) {
		if (mNode.getParent().toString().equals("FROM")) {
		    String table = mNode.toString();
		    while ((path = QueryTree.getNextMatch(table, 0, Position.Bias.Forward)) != null) {
			treeModel.removeNodeFromParent((DefaultMutableTreeNode) path.getLastPathComponent());
		    }

		} else {
		    treeModel.removeNodeFromParent(mNode);
		}
	    }
	}
	if (evt.getButton() == MouseEvent.BUTTON3) {

	    if (mNode.getParent().toString().equals("SELECT")) {
		jPopupMenu1.setLocation(MouseInfo.getPointerInfo().getLocation());
		jPopupMenu1.setVisible(true);
	    }
	}

    }//GEN-LAST:event_QueryTreeMouseClicked

    private void PopupMenuListMouseClicked(java.awt.event.MouseEvent evt) {

	if (evt.getButton() == MouseEvent.BUTTON1) {

	    String selectedItem = QueryTree.getSelectionPath().getLastPathComponent().toString();
	    String selectedAction = (String) PopupMenuList.getSelectedValue();
	    if (selectedAction.equals("Add to GROUP BY")) {
		path = QueryTree.getNextMatch("GROUP BY", 0, Position.Bias.Forward);
		createNode(selectedItem, (DefaultMutableTreeNode) path.getLastPathComponent());
	    }
	    if (selectedAction.equals("Add to SORT BY (ASC)")) {
		path = QueryTree.getNextMatch("SORT BY", 0, Position.Bias.Forward);
		createNode(selectedItem + " ASC", (DefaultMutableTreeNode) path.getLastPathComponent());
	    }
	    if (selectedAction.equals("Add to SORT BY (DESC)")) {
		path = QueryTree.getNextMatch("SORT BY", 0, Position.Bias.Forward);
		createNode(selectedItem + " DESC", (DefaultMutableTreeNode) path.getLastPathComponent());
	    }
	    jPopupMenu1.setVisible(false);
	}

    }

    private void fieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fieldsMouseClicked
	if (evt.getClickCount() == 2) {

	    String selectedItem;

	    selectedItem = (String) fields.getSelectedValue();
	    treeModel = (DefaultTreeModel) QueryTree.getModel();

	    path = QueryTree.getNextMatch("SELECT", 0, Position.Bias.Forward);
	    mNode = (DefaultMutableTreeNode) path.getLastPathComponent();
	    String nodeName = fields.getName() + '.' + selectedItem;
	    createNode(nodeName, mNode);
	}
    }//GEN-LAST:event_fieldsMouseClicked

    private void tablesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablesMouseClicked
	String selectedItem;
	if (evt.getClickCount() == 2) {

	    selectedItem = (String) tables.getSelectedValue();
	    path = QueryTree.getNextMatch("FROM", 0, Position.Bias.Forward);
	    mNode = (DefaultMutableTreeNode) path.getLastPathComponent();
	    createNode(selectedItem, mNode);

	    fields.setName(selectedItem);
	    result.clear();
	    result = handler.executeQuery("Show columns from " + selectedItem + ";");

	    fieldModel = (DefaultListModel) fields.getModel();
	    fieldModel.clear();

	    for (int i = 0; i < result.size(); i = i + 7) {
		fieldModel.addElement(result.get(i));

	    }

	    fields.setModel(fieldModel);
	}
    }//GEN-LAST:event_tablesMouseClicked

    private void CreateQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateQueryButtonActionPerformed
	createQuery();
	//JOptionPane.showMessageDialog(null, query);
	//this.setVisible(false);
	this.dispose();
    }//GEN-LAST:event_CreateQueryButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CreateQueryButton;
    private javax.swing.JTree QueryTree;
    private javax.swing.JList fields;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList tables;
    // End of variables declaration//GEN-END:variables
}
