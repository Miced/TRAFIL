package Simulations;

import UI.TRAFIL;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilities.PathLocator;
import utilities.StreamConsumer;

/**
 *
 * @author charalampi
 */
public class VideoSimulator extends Simulator {

    String mp4Pre;
    String mp4Middle;
    String mp4Trail;

    public VideoSimulator(Map<String, String> simulationInformation) {
        this.simulationInformation = simulationInformation;
    }

    @Override
    public boolean updateSimulationProperties() {
        String ffmpegFileName = (String) simulationInformation.get("ffmpegFileName");
        String ffmpegPre;
        String ffmpegMiddle;
        String ffmpegTrail;

        String[] ffmpegTokens = ffmpegFileName.split("\\$\\{videoName\\}");
        ffmpegPre = ffmpegTokens[0];
        String[] ffmpegtemp = ffmpegTokens[1].split("\\$\\{qualityIndex\\}");
        if (ffmpegtemp.length == 2) {
            ffmpegMiddle = ffmpegtemp[0];
            ffmpegTrail = ffmpegtemp[1];
        } else {
            ffmpegMiddle = ffmpegtemp[0];
            ffmpegTrail = "";
        }

        filenamesProperties.setProperty("ffmpegVideoNameOutput", "$ffmpegPre$videoName$ffmpegMiddle$counter$ffmpegTrail");

        simulationInformation.put("ffmpegPre", ffmpegPre);
        simulationInformation.put("ffmpegMiddle", ffmpegMiddle);
        simulationInformation.put("ffmpegTrail", ffmpegTrail);
        String mp4FileName = (String) simulationInformation.get("mp4FileName");

        if (mp4FileName.indexOf("${videoName}") < mp4FileName.indexOf("${qualityIndex}")) {
            String[] mp4Tokens = mp4FileName.split("\\$\\{videoName\\}");
            mp4Pre = mp4Tokens[0];
            String[] mp4temp = mp4Tokens[1].split("\\$\\{qualityIndex\\}");
            if (mp4temp.length == 2) {
                mp4Middle = mp4temp[0];
                mp4Trail = mp4temp[1];
            } else {
                mp4Middle = mp4temp[0];
                mp4Trail = "";
            }
            filenamesProperties.setProperty("mp4OutputFileName", "$mp4Pre$videoName$mp4Middle$counter$mp4Trail");
        }
        simulationInformation.put("mp4Pre", mp4Pre);
        simulationInformation.put("mp4Middle", mp4Middle);
        simulationInformation.put("mp4Trail", mp4Trail);
        try {
            filenamesProperties.store(new FileOutputStream(externalToolsPath + "filenamesConfiguration.properties"), null);
            prop = convertMapToProperty(simulationInformation);
            prop.store(new FileOutputStream(externalToolsPath + "preSimulationConfiguration.properties"), null);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(VideoSimulator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public static Properties convertMapToProperty(Map<String, String> simulationInformation) {
        Properties prop = new Properties();
        Set<Map.Entry<String, String>> set = simulationInformation.entrySet();
        for (Map.Entry<String, String> entry : set) {
            prop.put(entry.getKey(), entry.getValue());
        }

        return prop;
    }

    @Override
    public boolean execute() {
        BufferedReader bufferedError;
        InputStream errorStream;
        int exitValue = -1;
        Process process = null;
        StreamConsumer outputConsumer = null;
        try {
            System.out.println("executing script");
            ProcessBuilder builder = new ProcessBuilder(new String[]{externalToolsPath + "preSimulation.sh", externalToolsPath + "preSimulationConfiguration.properties", externalToolsPath + "filenamesConfiguration.properties"});
            builder.redirectErrorStream(true);
            process = builder.start();
            outputConsumer = new StreamConsumer(process.getInputStream(), 1);
            outputConsumer.start();

            exitValue = process.waitFor();

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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Exit Value:" + exitValue);
            if (outputConsumer != null) {
                simulationMessage.append(outputConsumer.getProcessOutput().toString());
                outputConsumer.setTerminate(true);
            }
            process.destroy();
            ex.printStackTrace();
            return false;
        }
    }

    public Map<String, ArrayList<File>> getSimulationOutputs() {
        Map<String, ArrayList<File>> simulationOutputs = new HashMap<String, ArrayList<File>>();
        File dir = new File(PathLocator.getSimulationOutputsPath(System.getProperty("user.dir")));
        File[] childrenFiles = dir.listFiles();
        ArrayList<File> senderFiles = new ArrayList<File>();
        ArrayList<File> receiverFiles = new ArrayList<File>();
        ArrayList<File> mp4OutFiles = new ArrayList<File>();
        ArrayList<File> datFiles = new ArrayList<File>();
        for (int i = 0; i < childrenFiles.length; i++) {
            if (childrenFiles[i].isFile()) {
                if (childrenFiles[i].getName().contains((String) simulationInformation.get("senderPrefix"))) {
                    senderFiles.add(childrenFiles[i]);
                } else if (childrenFiles[i].getName().contains((String) simulationInformation.get("receiverPrefix"))) {
                    receiverFiles.add(childrenFiles[i]);
                } else if (childrenFiles[i].getName().matches(mp4Pre + ".*" + mp4Middle + ".*" + mp4Trail)) {
                    mp4OutFiles.add(childrenFiles[i]);
                } else if (childrenFiles[i].getName().matches(".*\\.dat")) {
                    datFiles.add(childrenFiles[i]);
                }
            }
        }
        simulationOutputs.put("senderFiles", senderFiles);
        simulationOutputs.put("receiverFiles", receiverFiles);
        simulationOutputs.put("mp4OutFiles", mp4OutFiles);
        simulationOutputs.put("datFiles", datFiles);
        return simulationOutputs;

    }

    @Override
    public boolean validateExecution() {
        String currentKey;
        String ffmpegFileName = (String) simulationInformation.get("ffmpegFileName");
        String mp4FileName = (String) simulationInformation.get("mp4FileName");
        Set<Map.Entry<String, String>> set = simulationInformation.entrySet();
        for (Map.Entry<String, String> entry : set) {
            if (entry.getValue().isEmpty()) {
                JOptionPane.showMessageDialog(null, "The " + entry.getKey() + " is empty!", "Empty Parameter", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            currentKey = entry.getKey();
            if ((currentKey.matches("inFrameRate") || currentKey.matches("mp4MTU") || currentKey.matches("mp4FrameRate") || currentKey.matches("mp4Port") || currentKey.matches("outFrameRate") || currentKey.matches("outQualityEnd") || currentKey.matches("outQualityStart") || currentKey.matches("outGOP")) && (!isNumber(entry.getValue()))) {
                JOptionPane.showMessageDialog(null, "The " + entry.getKey() + " must be a number!", "Empty Parameter", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (ffmpegFileName.matches("[a-zA-Z0-9]*\\$\\{videoName\\}[a-zA-Z0-9]*_Q\\$\\{qualityIndex\\}")) {
            System.out.println("FFmpeg filename passed test.");
        } else {
            JOptionPane.showMessageDialog(null, "The name of FFmpeg's output is not in \n the correct format!"
                    + "The accepted format for the filenames must be in the form of " + "[a-zA-Z0-9]*\\$\\{videoName\\}[a-zA-Z0-9]*_Q\\$\\{qualityIndex\\}."
                    + "It is vital that after _Q no user added characters must exist because the functionality of et_ra will not be possible.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (mp4FileName.matches("[a-zA-Z0-9_-]*\\$\\{videoName\\}[a-zA-Z0-9]*_Q\\$\\{qualityIndex\\}")) {
            System.out.println("FFmpeg filename passed test.");
        } else {
            JOptionPane.showMessageDialog(null, "The name of MP4's output is not in \n the correct format!"
                    + "The accepted format for the filenames must be in the form of " + "[a-zA-Z0-9]*\\$\\{videoName\\}[a-zA-Z0-9]*_Q\\$\\{qualityIndex\\}."
                    + "It is vital that after _Q no user added characters must exist because the functionality of et_ra will not be possible.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        File dir = new File(PathLocator.getSimulationOutputsPath(System.getProperty("user.dir")));
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
}
