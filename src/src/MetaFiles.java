package src;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import utilities.PathLocator;

/**
 * Contains the information of the actual meta files used to create the meta data handlers.
 * Has information about their paths and names.
 * @author charalampi
 */
public class MetaFiles {

    /**
     * List of all the meta files located in the Resources/Metafiles path of TRAFIL.
     */
    private ArrayList<File> metaFiles = new ArrayList();
    /**
     * List of all the paths of the meta files located in the Resources/Metafiles path of TRAFIL.
     */
    private ArrayList<String> metaFilePaths = new ArrayList();

    /**
     * The constructor of the class, when invoked it fills the metafiles and metaFilePaths
     * lists with the necessary information.
     */
    public MetaFiles() {
        //String path = getClass().getClassLoader().getResource(".").getPath();
        String path = System.getProperty("user.dir");
        //path = path.replace("%20"," ")+"..\\..\\Resources\\MetaFiles\\";
        //JOptionPane.showMessageDialog(null, "Metafile Path:"+path);
        //String[] filenames = this.fileNames(path.replace("%20"," ")+"../../Resources/MetaFiles/");
        String[] filenames = this.fileNames(path + PathLocator.getMetafilesPath(path));
        for (int i = 0; i < filenames.length; i++) {
            if (filenames[i].contains("meta")) {
                //metaFiles.add(new File(path.replace("%20", " ") + "../../Resources/MetaFiles/" + filenames[i]));
                //metaFilePaths.add(path.replace("%20", " ") + "../../Resources/MetaFiles/" + filenames[i]);
                metaFiles.add(new File(path + PathLocator.getMetafilesPath(path) + filenames[i]));
                metaFilePaths.add(PathLocator.getMetafilesPath(path) + filenames[i]);
            }
        }
    }

    /**
     * Returns a specific meta file depending on the index given.
     * @param index the index of a specific metafile
     * @return File, the specific metafile that was requested
     */
    public File getMetaFile(int index) {
        return this.metaFiles.get(index);
    }

    /**
     * Returns the number of meta files that TRAFIL has.
     * @return integer, the number of meta files.
     */
    public int getMetaFileNumber() {
        return this.metaFiles.size();
    }

    /**
     * Returns a specific meta file path depending on the index given.
     * @param index the index of a specific metafile path
     * @return String, the specific meta file path that was requested
     */
    public String getMetaFilePaths(int index) {
        return metaFilePaths.get(index);
    }

    /**
     * Returns the names of the meta files located in the Resources/MetaFiles path of TRAFIL.
     * @param directoryName the path of the directory where the meta files are located.
     * @return an array of strings with the names of the meta files.
     */
    public String[] fileNames(String directoryName) {
        int fileCounter = 0;
        File dir = new File(directoryName);
        File[] childrenFiles = dir.listFiles();

        for (int i = 0; i < childrenFiles.length; i++) {
            if (childrenFiles[i].isFile()) {
                //JOptionPane.showMessageDialog(null, "FileName:"+childrenFiles[i].getName());
                fileCounter++;
            }
        }
        //JOptionPane.showMessageDialog(null, "FileCounter:"+fileCounter);
        String[] children = new String[fileCounter];
        fileCounter = 0;
        for (int i = 0; i < childrenFiles.length; i++) {
            if (childrenFiles[i].isFile()) {
                children[fileCounter++] = childrenFiles[i].getName();
            }
        }
        return children;
    }
}
