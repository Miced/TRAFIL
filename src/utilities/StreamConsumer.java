/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author charalampi
 */
public class StreamConsumer extends Thread {

    InputStream is;
    int type;
    StringBuffer processOutput = new StringBuffer();
    boolean terminate = false;

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    public StreamConsumer(InputStream is, int type) {
        this.is = is;
        this.type = type;
    }

    public StringBuffer getProcessOutput() {
        return processOutput;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while (!terminate) {
                if ((line = br.readLine()) != null) {
                    processOutput.append(line);
                    processOutput.append(System.getProperty("line.separator"));
                }

            }
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
