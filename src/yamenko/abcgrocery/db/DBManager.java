package yamenko.abcgrocery.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database manager
 *
 * @param <T>
 * @author Eugene Yamenko <yamenko@me.com>
 */
public final class DBManager<T> {
    // Fields
    private String connectionString;

    // Constructors
    public DBManager() {
        try {
            // physical path to the database
            String dbPath = getClass().getResource("/ABCGrocery").toURI().getPath();
            // jdbc connection string
            connectionString = "jdbc:derby://localhost" + dbPath;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Executes supplied query
     *
     * @param query    to be executed
     * @param parsable custom parser
     * @return list of parsed instances
     */
    public List<T> executeQuery(String query, Parsable<T> parsable) {
        List<T> items = new ArrayList<>();
        List<Object> row;

        try (Connection conn = DriverManager.getConnection(connectionString)) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    int columns = rs.getMetaData().getColumnCount(); // number of columns

                    // iterate through all rows
                    while (rs.next()) {
                        row = new ArrayList<>(columns); // all objects from current row

                        // iterate through all columns
                        for (int i = 1; i <= columns; i++) {
                            row.add(rs.getObject(i));
                        }

                        items.add(parsable.parse(row));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return items;
    }

    /**
     * Executes supplied query
     *
     * @param query to be executed
     */
    public void executeUpdate(String query) {
        try (Connection conn = DriverManager.getConnection(connectionString)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
