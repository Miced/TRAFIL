package src;


import java.util.ArrayList;


/**
 * Contains all the information that might be present in meta file's line.
 * It used by the meta data handler to log all the information of its 
 * metafile.
 * @author charalampi
 */
public class MetaEntry {
    private String name,type,index,startsWith,endsWith,convertHex,numberOfFields,unique,delimiter,uniqueCounter,columnCounter;
    private String time,packetSize;
    private ArrayList<String> node = new ArrayList();

    public ArrayList<String> getNode() {
        return node;
    }

    public void setNode(ArrayList<String> node) {
        this.node = node;
    }

    public String getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(String packetSize) {
        this.packetSize = packetSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    public String getColumnCounter() {
        return columnCounter;
    }

    public void setColumnCounter(String columnCounter) {
        this.columnCounter = columnCounter;
    }

    public String getUniqueCounter() {
        return uniqueCounter;
    }

    public void setUniqueCounter(String uniqueCounter) {
        this.uniqueCounter = uniqueCounter;
    }
    
    public String getConvertHex() {
        return convertHex;
    }

    public void setConvertHex(String convertHex) {
        this.convertHex = convertHex;
    }

    public String getEndsWith() {
        return endsWith;
    }

    public void setEndsWith(String endsWith) {
        this.endsWith = endsWith;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOfFields() {
        return numberOfFields;
    }

    public void setNumberOfFields(String numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(String startsWith) {
        this.startsWith = startsWith;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }
    
    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    public MetaEntry(){
        
    }
    
    
}
