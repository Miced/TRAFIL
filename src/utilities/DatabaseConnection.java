package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This class conducts the connection to the database actions. It can be used
 * by other classes to access the statement object to execute queries to he database. 
 * @author charalampi
 */
public class DatabaseConnection {

    private String driverName = "com.mysql.jdbc.Driver";
    private static Statement st;
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }
    private static String user, password, dbName, url;

    public static String getDbName() {
        return dbName;
    }

    public String getDriverName() {
        return driverName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public DatabaseConnection(String dbName, String user, String password) {
        try {
            Class.forName(driverName);
            this.dbName = dbName;
            this.user = user;
            this.password = password;
            url = "jdbc:mysql://localhost/" + dbName + "?allowMultiQueries=True";
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method tries to establish the connection to the local database based
     * on the information given by the user.
     * @return boolean, true if the connection was successful, false otherwise
     */
    public boolean connect() {
        try {
            if (dbName.equalsIgnoreCase("") || (dbName.length() < 2) || url.equalsIgnoreCase("") || user.equalsIgnoreCase("") || (user.length() < 2) || (url.length() < 2)) {
                JOptionPane.showMessageDialog(null, "Please insert valid Login Data.");
                return false;
            } else {
                connection = DriverManager.getConnection(url, user, password);
                Logger.getLogger(Class.class.getName()).severe("Succesfully Connected to Database " + dbName + " as " + user);
                //System.out.println("[" + NOW() + "] Succesfully Connected to Database " + dbName + " as " + user);
                st = connection.createStatement();
                return true;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    public static Statement getSt() {
        return st;
    }



}
