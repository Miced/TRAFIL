package src;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This class contains general information regarding the selected trace file at each time.
 * An instance is created upon TRAFIL startup and its variables are changed when the
 * currently selected trace file changes.
 * @author charalampi
 */
public class TraceFileInfo {
    private long filesizeInKB;
    private String TraceFileName;
    private File inputfile;
    private boolean is_right_format;
   
    public String getTraceFileName() {
        return TraceFileName;
    }

    public void setTraceFileName(String TraceFileName) {
        this.TraceFileName = TraceFileName.replace("-", "_");
        this.TraceFileName = this.TraceFileName.replace(".", "_");
        this.TraceFileName = this.TraceFileName.replace(" ", "_");
    }

    public long getFilesizeInKB() {
        return filesizeInKB;
    }

    public void setFilesizeInKB(long filesizeInKB) {
        this.filesizeInKB = filesizeInKB;
    }

    public File getInputfile() {
        return inputfile;
    }

    public void setInputfile(File inputfile) {
        this.inputfile = inputfile;
    }

    public boolean is_right_format() {
        return is_right_format;
    }

    public void setIs_right_format(boolean is_right_format) {
        this.is_right_format = is_right_format;
    }
  
    public TraceFileInfo(){
        
    }
    
    public void setInfo(File inputfile,String selectedTraceFile){
        this.inputfile=inputfile;
        TraceFileName=selectedTraceFile;
    }
   
}
