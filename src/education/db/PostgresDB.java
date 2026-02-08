package education.db;

import education.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDB {
    private static PostgresDB instance;
    private Connection connection;

    private final String url = "jdbc:postgresql://localhost:5432/education";
    private final String user = "postgres";
    private final String pass = "2007";

    private PostgresDB() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized PostgresDB getInstance() {
        if (instance == null) {
            instance = new PostgresDB();
        }
        return instance;
    }

    public Connection getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, pass);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Connection failed", e);
        }
    }
}