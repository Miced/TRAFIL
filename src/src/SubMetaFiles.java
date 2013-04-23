package src;

/**
 * Contains the information of the actual sub meta files used to create the sub meta data handlers.
 * Has information about their paths and names.
 * @author charalampi
 */
import java.io.File;
import java.util.ArrayList;
import utilities.PathLocator;

public class SubMetaFiles {
    /**
     * List of all the sub meta files of a specific meta data handler.
     */
    private ArrayList<File> SubmetaFiles = new ArrayList();
    /**
     * List of all the sub meta file paths of a specific meta data handler.
     */
    private ArrayList<String> SubmetaFilePaths = new ArrayList();
    /**
     * The constructor of the class, when invoked it fills the Submetafiles and SubmetaFilePaths
     * lists with the necessary information.
     */
    public SubMetaFiles(String FolderName) {
        //String path = getClass().getClassLoader().getResource(".").getPath();
        String path = System.getProperty("user.dir");
        //path = path.replace("%20"," ")+"..\\..\\Resources\\MetaFiles\\";
        //String[] filenames = this.fileNames(path.replace("%20", " ") + "../../Resources/MetaFiles/" + FolderName + "/");
        String[] filenames = this.fileNames(path + PathLocator.getMetafilesPath(path) + FolderName + "/");
        for (int i = 0; i < filenames.length; i++) {
            if (filenames[i].contains("meta")) {
                //SubmetaFiles.add(new File(path.replace("%20", " ") + "../../Resources/MetaFiles/" + FolderName + "/" + filenames[i]));
                //SubmetaFilePaths.add(path.replace("%20", " ") + "../../Resources/MetaFiles/" + FolderName + "/" + filenames[i]);
                SubmetaFiles.add(new File(path + PathLocator.getMetafilesPath(path) + FolderName + "/" + filenames[i]));
                SubmetaFilePaths.add(path + PathLocator.getMetafilesPath(path) + FolderName + "/" + filenames[i]);
            }
        }
    }

    public File getSubMetaFile(int index) {
        return this.SubmetaFiles.get(index);
    }

    public int getMetaFileNumber() {
        return this.SubmetaFiles.size();
    }

    public String getMetaFilePaths(int index) {
        return SubmetaFilePaths.get(index);
    }
    /**
     * Returns the names of the sub meta files located  for a specific meta data handler.
     * @param directoryName the path of the directory where the sub meta files are located.
     * @return an array of strings with the names of the sub meta files.
     */
    public String[] fileNames(String directoryName) {
        File dir = new File(directoryName);
        String[] children = dir.list();
        return children;
    }
}
