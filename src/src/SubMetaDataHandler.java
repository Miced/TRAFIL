package src;

/**
 *
 * @author charalambi
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class SubMetaDataHandler {

    private String createTableString = "";
    private String subMetaFilePath;
    private int fields_index;
    private int columns_index;
    public String[] model;
    private ArrayList<MetaEntry> subMetaEntries = new ArrayList();
    
    public int getFields_index() {
        return fields_index;
    }
    
    public int getColumns_index() {
        return columns_index;
    }
      
    public SubMetaDataHandler() {
    }
    /**
     * Opens and loads all the sub meta data handlers for a specific meta data handler.
     * @param metaFile the file that was used for the specific meta data handler
     * @return boolean, true if the sub meta data handlers were loaded correctly, false otherwise
     */
    public boolean OpenMetaFile(File metaFile) {
        try {

            //JOptionPane.showMessageDialog(null, "Opening Metafile:"+metaFile.getName());
            BufferedReader br = new BufferedReader(new FileReader(metaFile));
            String data = new String();
            String[] elements = null;
            int modelColumnCounter = 0;
            boolean nameSet, typeSet, indexSet;
            int j = 0;
            data = br.readLine();
            data = data.trim();
            elements = data.split(" ");
            createTableString = "";
            subMetaEntries = new ArrayList();


            /*
             * Reading the NumberOfFields that describe the TraceFile and are independendant
             * of the number of columns of the trace file.
             */
            if (elements[0].equalsIgnoreCase("NumberOfFields") && (elements.length == 2)) {

                model = new String[Integer.parseInt(elements[1])];
                subMetaEntries.add(new MetaEntry());
                subMetaEntries.get(0).setNumberOfFields(elements[1]);
                fields_index = Integer.parseInt(elements[1]);
            } else {
                JOptionPane.showMessageDialog(null, "<html>Error in first line.<br/>Metadata should start with the NumberofColumns.<html>");
                return false;
            }
            /*
             * Reading the actual number of columns of the Trace File.
             */
            data = br.readLine();
            data = data.trim();
            elements = data.split(" ");
            if (elements[0].equalsIgnoreCase("NumberOfColumns") && (elements.length == 2)) {
                subMetaEntries.add(new MetaEntry());
                subMetaEntries.get(1).setColumnCounter(elements[1]);
                columns_index = Integer.parseInt(elements[1]);
            } else {
                JOptionPane.showMessageDialog(null, "<html>Error in second line.<br/>Metadata should contain the NumberofFields.<html>");
                return false;
            }
            /*
             * Reading the number of Unique characters that will distinguish the kind of Trace File.
             */
            data = br.readLine();
            data = data.trim();
            elements = data.split(" ");
            if (elements[0].equalsIgnoreCase("UniqueCounter") && (elements.length == 2)
                    && (Integer.parseInt(elements[1]) >= 0)) {
                subMetaEntries.add(new MetaEntry());
                subMetaEntries.get(2).setUniqueCounter(elements[1]);
                //subMetaEntries.get(2).setName("UniqueCounter");
            } else {
                JOptionPane.showMessageDialog(null, "<html>Error in third line.<br/>Metadata should contain the UniqueCounter and be >0.<html>");
                return false;
            }

            while ((data = br.readLine()) != null) {
                subMetaEntries.add(new MetaEntry());
                nameSet = typeSet = indexSet = false;
                data = data.trim();
                elements = data.split(" ");
                if (elements.length < 3) {
                    JOptionPane.showMessageDialog(null, "Error. Metadata not in the correct format.Elements missing.");
                    return false;
                } else {

                    for (int i = 0; i < elements.length; i++) {

                        if (elements[i].equalsIgnoreCase("-name") && (i == 0) && !nameSet) {
                            //columns[j][0]=elements[i+1];
                            model[modelColumnCounter++] = elements[i + 1];
                            subMetaEntries.get(j + 3).setName(elements[i + 1]);
                            createTableString = createTableString + elements[i + 1];
                            nameSet = true;
                            i++;
                        } else if (elements[i].equalsIgnoreCase("-type") && (i == 2) && !typeSet) {
                            if (elements[i + 1].equalsIgnoreCase("hexadecimal") || elements[i + 1].equalsIgnoreCase("hex")) {
                                elements[i + 1] = "int";
                                //columns[j][6]="1";
                                subMetaEntries.get(j + 3).setConvertHex("1");

                            } else {
                                //columns[j][6]="0";
                                subMetaEntries.get(j + 3).setConvertHex("0");
                            }

                            //columns[j][1]=elements[i+1];
                            subMetaEntries.get(j + 3).setType(elements[i + 1]);
                            createTableString = createTableString + " " + elements[i + 1] + ",";
                            typeSet = true;
                            i++;
                        } else if (elements[i].equalsIgnoreCase("-index") && (i == 4) && !indexSet) {
                            // columns[j][2]=elements[i+1];
                            subMetaEntries.get(j + 3).setIndex(elements[i + 1]);
                            indexSet = true;
                            i++;
                        } else if (elements[i].equalsIgnoreCase("-startsWith")) {
                            //columns[j][3]=elements[i+1];
                            subMetaEntries.get(j + 3).setStartsWith(elements[i + 1]);

                            i++;
                        } else if (elements[i].equalsIgnoreCase("-endsWith")) {
                            //columns[j][4]=elements[i+1];

                            subMetaEntries.get(j + 3).setEndsWith(elements[i + 1]);
                            i++;
                        } else if (elements[i].equalsIgnoreCase("-delimiter")) {
                            //columns[j][5]=elements[i+1];
                            subMetaEntries.get(j + 3).setDelimiter(elements[i + 1]);
                            i++;
                        } else if (elements[i].equalsIgnoreCase("-unique")) {
                            //JOptionPane.showMessageDialog(null, "FoundUnique:"+elements[i+1]);
                            subMetaEntries.get(j + 3).setUnique(elements[i + 1]);
                            i++;
                        } else {
                            JOptionPane.showMessageDialog(null, "Error. Sub Metadata not in the correct format. Flag not recognized:" + elements[i]);
                            Logger.getLogger(MetaDataHandler.class.getName()).severe("Error At:" + data);
                            //System.out.println("Error At:");
                            //System.out.println(data);
                            return false;
                        }

                    }

                }
                j++;
                if (!nameSet || !typeSet || !indexSet) {
                    JOptionPane.showMessageDialog(null, "Error. Sub metadata not in the correct format.Mandatory Flag missing.");
                    createTableString = "";
                    return false;
                }
            }

            if (j != fields_index) {
                JOptionPane.showMessageDialog(null, "<html>Error.NumberOfFields does not match the actual numer of fields.<html>");
                createTableString = "";
                return false;
            }

            return true;
            //JOptionPane.showMessageDialog(null, "<html>Create Table Strin:<br/><html>"+createTableString);

            /* System.out.println("*******PRINTING MODEL ARRAY*********");
            for (int k=0; k<model.length; k++){
            System.out.println(model[k]);
            }
            
            System.out.println("*******PRINTING METAENTRIES*********");
            for (int k=0; k<subMetaEntries.size(); k++){
            if(!(subMetaEntries.get(k).getName()==null))
            System.out.println("Index :"+k+" name:"+subMetaEntries.get(k).getName());
            }*/

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetaDataHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(MetaDataHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String getMetaFilePath() {
        return subMetaFilePath;
    }

    public void setMetaFilePath(String subMetaFilePath) {
        this.subMetaFilePath = subMetaFilePath;
    }

    public String getCreateTableString() {
        return createTableString;
    }

    public void setCreateTableString(String createTableString) {
        this.createTableString = createTableString;
    }

    public String[] getModel() {
        return model;
    }

    public ArrayList<MetaEntry> getSubMetaEntries() {
        return subMetaEntries;
    }
}
