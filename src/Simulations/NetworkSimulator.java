/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulations;

import UI.TRAFIL;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import utilities.GenericFileFilter;
import utilities.PathLocator;
import utilities.StreamConsumer;

/**
 *
 * @author charalampi
 */
public class NetworkSimulator extends Simulator {

    private String scriptFilePath;
    private ArrayList<File> resultTraceFile = new ArrayList<File>();

    public ArrayList<File> getResultTraceFile() {
	return resultTraceFile;
    }
    private File createdTraceFile;

    public NetworkSimulator(String path) {
	this.scriptFilePath = path;
    }

    public File getCreatedTraceFile() {
	return createdTraceFile;
    }

    @Override
    public boolean execute() {
	InputStream errorStream;
	BufferedReader bufferedError;
	Process process = null;
	StreamConsumer outputConsumer = null;
	int exitValue;
	try {
	    // Make sure network simulation script has appropriate permissions
	    File file = new File(externalToolsPath + "networkSimulation.sh");
	    Path path = file.toPath();
	    PosixFileAttributes attr =
		    Files.readAttributes(path, PosixFileAttributes.class);
	    if (!PosixFilePermissions.toString(attr.permissions()).contains("x")) {
		System.out.println("Setting networkSimulation.sh permissions to rwx------");
		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwx------");
		Files.setPosixFilePermissions(path, perms);
	    }
	    System.out.println("executing simulation");
	    ProcessBuilder builder = new ProcessBuilder(new String[]{externalToolsPath + "networkSimulation.sh", scriptFilePath});
	    builder.redirectErrorStream(true);
	    process = builder.start();
	    outputConsumer = new StreamConsumer(process.getInputStream(), 1);
	    outputConsumer.start();
	    System.out.println("Waiting for completion");
	    exitValue = process.waitFor();
	    System.out.println("Finished and reading buffer");

	    outputConsumer.setTerminate(true);
	    simulationMessage.append(outputConsumer.getProcessOutput().toString());
	    errorStream = process.getErrorStream();
	    bufferedError = new BufferedReader(new InputStreamReader(errorStream));

	    if (errorStream.available() > 0) {
		String lineIn;
		while ((lineIn = bufferedError.readLine()) != null) {
		    simulationMessage.append(lineIn);
		    simulationMessage.append(System.getProperty("line.separator"));
		}
	    }
	    process.destroy();
	    System.out.println(simulationMessage);
	    openResultTraceFile();
	    return true;
	} catch (IOException ex) {
	    Logger.getLogger(VideoPostSimulator.class.getName()).log(Level.SEVERE, null, ex);
	    if (outputConsumer != null) {
		simulationMessage.append(outputConsumer.getProcessOutput().toString());
		outputConsumer.setTerminate(true);
	    }
	    process.destroy();
	    return false;
	} catch (InterruptedException ex_in) {
	    if (outputConsumer != null) {
		simulationMessage.append(outputConsumer.getProcessOutput().toString());
		outputConsumer.setTerminate(true);
	    }
	    process.destroy();
	    ex_in.printStackTrace();
	    return false;
	}

    }

    @Override
    public boolean validateExecution() {
	if (!(new File(scriptFilePath).exists())) {
	    JOptionPane.showMessageDialog(null, "The script file does not exist!", "File does not exist.", JOptionPane.ERROR_MESSAGE);
	    return false;
	}
	File dir = new File(PathLocator.getNetworkSimulationOutputsPath(System.getProperty("user.dir")));
	File[] childrenFiles = dir.listFiles();
	if (childrenFiles.length != 0) {
	    int response = JOptionPane.showConfirmDialog(null, "<html>Previous Simulation files exist.<br/>Do you want to ovewrite them?</html>", "", JOptionPane.YES_NO_CANCEL_OPTION);
	    if (response == 0) {
		for (File file : childrenFiles) {
		    System.out.println("Deleting File:" + file.getAbsolutePath());
		    file.delete();
		}
		return true;
	    } else {
		return false;
	    }
	} else {
	    return true;
	}

    }

    @Override
    public boolean updateSimulationProperties() {
	return true;
    }

    public void openResultTraceFile() {
	File dir = new File(PathLocator.getNetworkSimulationOutputsPath(System.getProperty("user.dir")));
	File[] childrenFiles = dir.listFiles();
	if ((childrenFiles.length > 0)) {
	    for (File file : childrenFiles) {
		if (file.getName().endsWith(".tr")) {
		    resultTraceFile.add(file);
		}
	    }
	    if (resultTraceFile.size() == 1) {
		createdTraceFile = resultTraceFile.get(0);
	    } else {
		JFileChooser traceFileChooser = new JFileChooser(PathLocator.getNetworkSimulationOutputsPath(System.getProperty("user.dir")));
		traceFileChooser.setFileFilter(new GenericFileFilter(".tr"));
		traceFileChooser.setDialogTitle("Select Trace File...");
		int returnVal = traceFileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    try {
			createdTraceFile = new File(traceFileChooser.getSelectedFile().getCanonicalPath());
		    } catch (IOException ex) {
			Logger.getLogger(TRAFIL.class.getName()).log(Level.SEVERE, null, ex);
		    }

		}
	    }
	}
    }
}
