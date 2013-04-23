/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulations;

import java.util.Map;
import java.util.Properties;
import utilities.PathLocator;

/**
 *
 * @author stois21
 */
public abstract class Simulator {

    String externalToolsPath = PathLocator.getExternalToolsPath(System.getProperty("user.dir"));
    StringBuffer simulationMessage = new StringBuffer();
    Properties prop = new Properties();
    Properties filenamesProperties = new Properties();
    Map<String, String> simulationInformation;

    public boolean executeSimulation() {
        if (validateExecution() && updateSimulationProperties() && execute()) {
            return true;
        } else {
            return false;
        }
    }

    public abstract boolean execute();

    public abstract boolean validateExecution();

    public abstract boolean updateSimulationProperties();

    public String getSimulationMessage() {
        return simulationMessage.toString();
    }

    public boolean isNumber(String value) {
        if (value.matches("[0-9]+")) {
            return true;
        } else {
            return false;
        }
    }
}
