package Simulations;

import UI.TRAFIL;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilities.PathLocator;
import utilities.StreamConsumer;

/**
 *
 * @author charalampi
 */
public class VideoPostSimulator extends Simulator {

    public VideoPostSimulator(Map<String, String> simulationInformation) {
        this.simulationInformation = simulationInformation;
    }

    @Override
    public boolean updateSimulationProperties() {
        System.out.println("Trying to update simulation properties");
        prop = VideoSimulator.convertMapToProperty(simulationInformation);
        System.out.println("Updated Simulation properties");
        try {
            prop.store(new FileOutputStream(externalToolsPath + "postSimulationConfiguration.properties"), null);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(VideoPostSimulator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean execute() {
        InputStream errorStream;
        BufferedReader bufferedError;
        Process process = null;
        StreamConsumer outputConsumer = null;
        int exitValue;
        try {
            System.out.println("executing simulation");
            ProcessBuilder builder = new ProcessBuilder(new String[]{externalToolsPath + "postSimulation.sh", externalToolsPath + "preSimulationConfiguration.properties", externalToolsPath + "postSimulationConfiguration.properties"});
            builder.redirectErrorStream(true);
            process = builder.start();
            outputConsumer = new StreamConsumer(process.getInputStream(), 1);
            outputConsumer.start();
            System.out.println("Waiting for completion");
            exitValue = process.waitFor();
            System.out.println("Finished and reading buffer");

            outputConsumer.setTerminate(true);
            simulationMessage.append(outputConsumer.getProcessOutput().toString());
            errorStream = process.getInputStream();
            bufferedError = new BufferedReader(new InputStreamReader(errorStream));

            if (errorStream.available() > 0) {
                String lineIn;
                while ((lineIn = bufferedError.readLine()) != null) {
                    simulationMessage.append(lineIn);
                    simulationMessage.append(System.getProperty("line.separator"));
                }
            }
            process.destroy();
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
        String currentKey;
        Set<Map.Entry<String, String>> set = simulationInformation.entrySet();
        for (Map.Entry<String, String> entry : set) {
            if (entry.getValue().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The " + entry.getKey() + " is empty!", "Empty Parameter", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            currentKey = entry.getKey();
            if (currentKey.matches("postSimulationFrameRate") && (!isNumber(entry.getValue()))) {
                JOptionPane.showMessageDialog(null, "The " + entry.getKey() + " must be a number!", "Empty Parameter", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (entry.getKey().equalsIgnoreCase("senderFile") || entry.getKey().equalsIgnoreCase("receiverFile") || entry.getKey().equalsIgnoreCase("dataFile1") || entry.getKey().equalsIgnoreCase("dataFile2")) {
                if (!(new File(PathLocator.getSimulationOutputsPath(System.getProperty("user.dir")) + entry.getValue())).exists()) {
                    JOptionPane.showMessageDialog(null, "The " + entry.getValue() + " does not exist!", "File does not exist.", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        return true;

    }
}
