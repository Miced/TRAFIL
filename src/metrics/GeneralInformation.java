package metrics;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import src.MetaDataHandler;
import src.TraceFileInfo;

/**
 *
 * @author charalampi
 */
public abstract class GeneralInformation {
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    protected MetaDataHandler metaHandler;
    protected Statement st;
    protected TraceFileInfo traceFile;

    public double getAveragePacketSize() {
        return roundDecimals(averagePacketSize);
    }

    public double getMaximumPacketSize() {
        return roundDecimals(maximumPacketSize);
    }

    public MetaDataHandler getMetaHandler() {
        return metaHandler;
    }

    public double getMinimumPacketSize() {
        return roundDecimals(minimumPacketSize);
    }

    public double getNumberOfDroppedBytes() {
        return roundDecimals(numberOfDroppedBytes);
    }

    public int getNumberOfDroppedPackets() {
        return numberOfDroppedPackets;
    }

    public double getNumberOfForwardedBytes() {
        return roundDecimals(numberOfForwardedBytes);
    }

    public int getGeneratedPacketsAGT() {
        return generatedPacketsAGT;
    }

    public int getNumberOfForwardedPackets() {
        return numberOfForwardedPackets;
    }

    public int getNumberOfGeneratedPackets() {
        return numberOfGeneratedPackets;
    }

    public int getNumberOfReceivedPackets() {
        return numberOfReceivedPackets;
    }

    public double getNumberOfSentBytes() {
        return roundDecimals(numberOfSentBytes);
    }

    public int getNumberOfSentPackets() {
        return numberOfSentPackets;
    }

    public int getGeneratedPacketsMAC() {
        return generatedPacketsMAC;
    }

    public int getGeneratedPacketsRTR() {
        return generatedPacketsRTR;
    }

    public double getGeneratedPacketsSizeAGT() {
        return roundDecimals(generatedPacketsSizeAGT);
    }

    public double getGeneratedPacketsSizeMAC() {
        return roundDecimals(generatedPacketsSizeMAC);
    }

    public double getGeneratedPacketsSizeRTR() {
        return roundDecimals(generatedPacketsSizeRTR);
    }

    public double getNumberOfGeneratedBytes() {
        return roundDecimals(numberOfGeneratedBytes);
    }

    public double getNumberOfReceivedBytes() {
        return roundDecimals(numberOfReceivedBytes);
    }

    public int getReceivedPacketsAGT() {
        return receivedPacketsAGT;
    }

    public int getReceivedPacketsMAC() {
        return receivedPacketsMAC;
    }

    public int getReceivedPacketsRTR() {
        return receivedPacketsRTR;
    }

    public double getReceivedPacketsSizeAGT() {
        return roundDecimals(receivedPacketsSizeAGT);
    }

    public double getReceivedPacketsSizeMAC() {
        return roundDecimals(receivedPacketsSizeMAC);
    }

    public double getReceivedPacketsSizeRTR() {
        return receivedPacketsSizeRTR;
    }
    protected ResultSet rs;
    protected double numberOfSentBytes, numberOfForwardedBytes, numberOfDroppedBytes, minimumPacketSize, maximumPacketSize, averagePacketSize, numberOfReceivedBytes;
    protected int  numberOfSentPackets, numberOfForwardedPackets, numberOfDroppedPackets, numberOfReceivedPackets, numberOfGeneratedPackets;
    protected int generatedPacketsRTR, generatedPacketsAGT, generatedPacketsMAC;
    protected int receivedPacketsRTR, receivedPacketsAGT, receivedPacketsMAC;
    protected double generatedPacketsSizeRTR, generatedPacketsSizeAGT, generatedPacketsSizeMAC, receivedPacketsSizeRTR, receivedPacketsSizeAGT, receivedPacketsSizeMAC;
    protected double numberOfGeneratedBytes;

    public abstract void retrieveMetrics();

    
    public abstract boolean storeMetrics();

    public abstract boolean produceMetrics();

    public static double roundDecimals(double d,int decimalAccuracy) {
        //DecimalFormat formatter = new DecimalFormat("#.####");
        //NumberFormat formatter = new DecimalFormat("#0.0000");
        //return Double.valueOf(twoDForm.format(d));
        //String temp = formatter.format(d);
        //float t2 = Float.valueOf(temp);
        //System.out.println("float:"+t2);
        BigDecimal bd = new BigDecimal(d).setScale(decimalAccuracy, RoundingMode.HALF_EVEN);
        d = bd.doubleValue();

        return d;
    }
    public static double roundDecimals(double d) {
        //DecimalFormat formatter = new DecimalFormat("#.####");
        //NumberFormat formatter = new DecimalFormat("#0.0000");
        //return Double.valueOf(twoDForm.format(d));
        //String temp = formatter.format(d);
        //float t2 = Float.valueOf(temp);
        //System.out.println("float:"+t2);
        BigDecimal bd = new BigDecimal(d).setScale(4, RoundingMode.HALF_EVEN);
        d = bd.doubleValue();

        return d;
    }
    public static String NOW() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateF = new SimpleDateFormat(DATE_FORMAT_NOW);
        return dateF.format(cal.getTime());

    }
}
