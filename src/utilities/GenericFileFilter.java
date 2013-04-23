/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.File;

/**
 *
 * @author stois21
 */
public class GenericFileFilter extends javax.swing.filechooser.FileFilter {
    String fileType;
    public GenericFileFilter(String fileType) {
        this.fileType = fileType;
        
    }
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(fileType);

    }

    public String getDescription() {
        return "Trace files *.tr";
    }
}
