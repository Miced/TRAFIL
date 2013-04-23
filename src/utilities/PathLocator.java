/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import javax.swing.JOptionPane;

/**
 *
 * @author charalampi
 */
public class PathLocator {
//#TODO mpla2

    public static String getLogPath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/Resources/Utility/trafilLog.txt";
    }

    public static String getExportPath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/Resources/Exported/";
    }

    public static String getMetafilesPath(String workingDirectoryPath) {
	return "/Resources/MetaFiles/";
    }

    public static String getTempFilePath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/Resources/Utility/tempfile.txt";
    }

    public static String getExternalToolsPath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/ExternalTools/";
    }

    public static String getSimulationOutputsPath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/VideoSimulationOutputs/";
    }

    public static String getNetworkSimulationOutputsPath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/NetworkSimulationOutputs/";
    }

    public static String getTRAFILPropertiesPath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/settings.properties";
    }

    public static String getDesignOutputPath(String workingDirectoryPath) {
	return workingDirectoryPath.replace("\\", "/") + "/SimulationDesignOutputs/";
    }

    public static String validatePath(String path) {
	String temp = "\\ ";
	if (System.getProperty("os.name").contains("Windows") || System.getProperty("os.name").contains("windows")) {
	    path = "\"" + path + "\"";
	} else {
	    path = "\"" + path + "\"";
	}
	System.out.println("Path:" + path);
	return path;
    }
}
