package src;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 * This class handles the information about TRAFIL's main table. It handles the changes of
 * the jTable's model to handle the data of each trace file and handles the insertion of data
 * inside that the table.
 * @author charalampi
 */
public class TableHandler {
/**
 *This is the initial model of the jTable when TRAFIL first starts. After the first trace file is loaded
 *the mainModel is used to hold each new trace file's model for the jTable. 
 */
private DefaultTableModel model = new DefaultTableModel(new Object[][] {},new String []{
"Event","Time","SN","DN","PNanme","PSize","Flags","FI","SAN","SAP","DAN","DAP","SeqN","UPI","TCP_AN","TCP_Flags","TCP_HL","TCP_SAL",
"Sat._SLat","Sat._SLon","Sat._DLat","Sat._DLon"});
private DefaultTableModel mainModel;

public DefaultTableModel getTestModel() {
        return mainModel;
    }

public void setTestModel(DefaultTableModel testModel) {
        this.mainModel = testModel;
    }

public DefaultTableModel getModel3() {
        return model;
    }
    
public TableHandler(){
 } 
/**
 * This method is used when a new trace file is opened or loaded from the database
 * in order to fill the jTable with these new information.
 * @param st1 the statement executed to draw the information from the database.
 * @param modelLength the length of the elements that will be present on the jTable
 * @throws SQLException 
 */
public void addToTable2(Statement st1, int modelLength) throws SQLException{
         int counter=0;
         ResultSet rs;
         Object[] element = new Object[modelLength];
         rs = st1.getResultSet();
         mainModel.setNumRows(0);
         while(rs.next())
            {
               for(int i=0; i<modelLength; i++){
                   element[i]=rs.getString(i+2);
                   
               } 
               //JOptionPane.showMessageDialog(null, "Ading Row,elemnts"+element.length);
               mainModel.insertRow(counter,element);
               counter++;
            }
       
    }
}
