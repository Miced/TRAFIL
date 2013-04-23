package src;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.DatabaseConnection;

/**
 *
 * @author charalampi
 */
public class QueryHandler {

    ArrayList<String> columnNames = new ArrayList();

    public ArrayList<String> executeQuery(String query) {
        Statement st = DatabaseConnection.getSt();
        if (query.contains("delete") || query.contains("DELETE") || query.contains("drop")
                || query.contains("alter") || query.contains("insert") || query.contains("create") || query.contains("rename")
                || query.contains("truncate") || query.contains("call") || query.contains("infile") || query.contains("replace")) {
            columnNames.add("Invalid query.Only select statements are allowed.");

        } else {
            try {
                ResultSet rs = st.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= numberOfColumns; i++) {
                        columnNames.add(rs.getString(i) + "\t");
                    }
                    columnNames.add("\n");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(QueryHandler.class.getName()).log(Level.SEVERE, null, ex);
                columnNames.clear();
                columnNames.add(ex.getMessage());
                return columnNames;
            }
        }
        return columnNames;
    }
}
