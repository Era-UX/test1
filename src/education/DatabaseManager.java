package education;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private String url = "jdbc:postgresql://localhost:5432/education";
    private String user = "postgres";
    private String pass = "2007";

    public Connection getConnection() throws DatabaseConnectionException {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database: " + e.getMessage());
        }
    }
}