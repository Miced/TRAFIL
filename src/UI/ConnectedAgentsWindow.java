/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import Simulations.TclDesignerPanel;

/**
 *
 * @author Drakoulelis
 */
public class ConnectedAgentsWindow extends javax.swing.JFrame implements TableModelListener {

    private DefaultTableModel model;
    private int rowCount = 0;
    List<TableCellEditor> attachedNodeEditors;
    private TclDesignerPanel tclDesigner;

    /**
     * Creates new form ConnectedAgentsWindow
     */
    public ConnectedAgentsWindow(TclDesignerPanel tclDesigner) {
        this.tclDesigner = tclDesigner;
        this.attachedNodeEditors = new ArrayList<>();
        initComponents();
        this.setVisible(false);

        model = (DefaultTableModel) connectedAgentsTable.getModel();
        model.addTableModelListener(this);
        model.setRowCount(rowCount);
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public List<TableCellEditor> getAttachedNodeEditors() {
        return attachedNodeEditors;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        connectedAgentsTable = new javax.swing.JTable() {
            public TableCellEditor getCellEditor(int row, int column) {
                int modelColumn = convertColumnIndexToModel(column);

                if (modelColumn == 2) return attachedNodeEditors.get(row);
                else return super.getCellEditor(row, column);
            }
        };

        setAlwaysOnTop(true);

        connectedAgentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Node", "Agent", "Attached node", "Connected agent"
            }
        ));
        connectedAgentsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(connectedAgentsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable connectedAgentsTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        if (column == 2) {
            String data = (String) model.getValueAt(row, column);
            String agent = tclDesigner.getNodeAgentByNodeName(data);
            System.out.println("AGENT=" + agent);
            model.setValueAt(agent, row, 3);
        }
    }
}
