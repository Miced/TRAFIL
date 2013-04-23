package utilities;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import src.MetaDataHandler;
import src.TableHandler;
import src.TraceFileInfo;
import src.TracefileHandler;
/**
 * This class handles the changes on jTable when a trace file is loaded and 
 * creates un update string that can be used to save the changes made to the trace file
 * to the database.
 * @author charalampi
 */
public class TableListener implements TableModelListener{
	 String s;
         TraceFileInfo TraceFile;
         TracefileHandler TraceHandler;
         TableHandler TableHan;
         MetaDataHandler metaHandler;
         private String sqlUpdateQuery;
           
 public TableListener(MetaDataHandler metaHandler,TraceFileInfo TraceFile,TableHandler TableHan,TracefileHandler TraceHandler){
                    this.TraceFile=TraceFile;
                    this.TableHan=TableHan;
                    this.TraceHandler=TraceHandler;
                    sqlUpdateQuery="";
                }
    @Override
    /**
     * This method handles the event of the change in a jTable's cell.
     * When a value is changed and it is not due to inserting new data
     * is an open or load procedure, then an appropriate update sql query
     * is created and added to the sqlUpdateQuery string.
     */
 public void tableChanged(TableModelEvent e){
                     this.metaHandler=TraceHandler.getMetaHandler();
                    int indexOfFirstElement=0;
                    if ( (e.getType()==0) && (!TraceHandler.isInserting()) )
                    {
                        //JOptionPane.showMessageDialog(null,"Table Changed2");
                        int columnChanged=e.getColumn();
                        //JOptionPane.showMessageDialog(null,"Meta Handler:"+metaHandler.getMetaFilePath());
                        String columnName = metaHandler.getModel()[columnChanged]; 
                        boolean Varchar = false;
                        int sum = 0;
                        
                        
                        if((TableHan.getTestModel().getValueAt(e.getFirstRow()+1, e.getColumn())==null)||(TableHan.getTestModel().getValueAt(e.getFirstRow()-1, e.getColumn())==null)){
                            int response = JOptionPane.showConfirmDialog(null,"<html>The column you are about to change is not used in the original trace file."
                                    + "<br/>Changing this column might cause serious errros.<br/>"
                                    + "Are you sure you want to change this value?</html>");
                            if (response !=0){
                                TraceHandler.setInserting(true);
                                TableHan.getTestModel().setValueAt(null, e.getFirstRow(), e.getColumn());
                                TraceHandler.setInserting(false);
                                return;
                            }
                        }
                        
                        
                        if(columnChanged<metaHandler.getFields_index()){
                            for(int i=0; i<metaHandler.getMetaEntries().size(); i++){
                                if(metaHandler.getMetaEntries().get(i).getName()!=null){
                                    if(metaHandler.getMetaEntries().get(i).getName().equalsIgnoreCase(columnName)){
                                        //JOptionPane.showMessageDialog(null, "Type:"+metaHandler.getMetaEntries().get(i).getType());
                                        if(metaHandler.getMetaEntries().get(i).getType().contains("varchar")||metaHandler.getMetaEntries().get(i).getType().contains("VARCHAR")||metaHandler.getMetaEntries().get(i).getType().contains("char")){
                                            Varchar = true;
                                            break;
                                        }
                                    }
                                    
                                }
                            }
                        }
                        else{
                            sum+=metaHandler.getFields_index();
                            for(int i=0; i<metaHandler.getSubMetaHandlers().size(); i++){
                                if((metaHandler.getSubMetaHandlers().get(i).getFields_index()+sum)>columnChanged){
                                for(int j=0; j<metaHandler.getSubMetaHandlers().get(i).getSubMetaEntries().size(); j++){
                                    if(metaHandler.getSubMetaHandlers().get(i).getSubMetaEntries().get(j).getName()!=null){
                                        //JOptionPane.showMessageDialog(null, "Type:"+metaHandler.getMetaEntries().get(i).getType());
                                        if(metaHandler.getSubMetaHandlers().get(i).getSubMetaEntries().get(j).getName().equalsIgnoreCase(columnName)){
                                            if(metaHandler.getSubMetaHandlers().get(i).getSubMetaEntries().get(j).getType().contains("varchar")||metaHandler.getSubMetaHandlers().get(i).getSubMetaEntries().get(j).getType().contains("VARCHAR")||metaHandler.getSubMetaHandlers().get(i).getSubMetaEntries().get(j).getType().contains("char")){
                                                Varchar = true;
                                                break;
                                            }
                                            
                                        }
                                    }
                                }
                             if(Varchar){
                                 break;
                             }   
                            }
                            else{
                                    sum+=metaHandler.getSubMetaHandlers().get(i).getFields_index();
                                }
                            }
                        }
                                                
                        if(Varchar){
                            sqlUpdateQuery=sqlUpdateQuery+"update "+TraceFile.getTraceFileName()+" set "+metaHandler.getModel()[e.getColumn()]+"=\""+TableHan.getTestModel().getValueAt(e.getFirstRow(), e.getColumn())+"\" where id="+(e.getFirstRow()+1)+";";
                        }
                        else{
                            sqlUpdateQuery=sqlUpdateQuery+"update "+TraceFile.getTraceFileName()+" set "+metaHandler.getModel()[e.getColumn()]+"="+TableHan.getTestModel().getValueAt(e.getFirstRow(), e.getColumn())+" where id="+(e.getFirstRow()+1)+";";

                        }
                        //JOptionPane.showConfirmDialog(null,sqlUpdateQuery);
                        
                        
                   
                   }
	        }               
 public String getSqlupdatequery(){
        return sqlUpdateQuery;
    }
 public void setSqlupdatequery(String sqlupdatequery){
        this.sqlUpdateQuery = sqlupdatequery;
    }       
}
