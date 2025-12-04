package main;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Utility class for managing database connections and operations.
 * This class handles the initialization of the JDBC driver and loads
 * database connection properties from 'db.properties'.
 */
public final class DatabaseManager {
    private static final Properties props = new Properties();

    static {
        // Load the MySQL JDBC driver. This is necessary for establishing
        // database connections.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC driver not found: " + e.getMessage());
        }

        // Load database connection properties from the db.properties file.
        // This file should be located in the classpath under the 'config' directory.
        try (InputStream in = DatabaseManager.class
                .getClassLoader()
                .getResourceAsStream("config/db.properties")) {
            if (in == null) throw new IllegalStateException("Missing db.properties file in classpath.");
            props.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load db.properties: " + e.getMessage());
        }
    }

    private DatabaseManager() { }

    /**
     * Establishes and returns a new database connection using the properties
     * loaded from 'db.properties'.
     *
     * @return A new Connection object to the database.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }

    /**
     * Creates a new game session entry in the 'game_sessions' table
     * with the current timestamp as the start time.
     *
     * @return The auto-generated ID of the newly created game session.
     * @throws SQLException If a database access error occurs.
     */
    public static int createGameSession() throws SQLException {
        // SQL query to insert a new game session with a start time.
        String sql = "INSERT INTO game_sessions (start_time) VALUES (?)";
        int gameId = -1; // Default value if gameId is not retrieved.

        // Use try-with-resources for automatic closing of Connection and PreparedStatement.
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set the current timestamp for the start_time column.
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate(); // Execute the insert statement.

            // Retrieve the auto-generated key (game_id) for the new session.
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    gameId = rs.getInt(1); // Get the first generated key.
                }
            }
        }
        return gameId;
    }

    /**
     * Updates the 'end_time' for a specific game session identified by its ID.
     * The end time is set to the current timestamp.
     *
     * @param gameId The ID of the game session to update.
     * @throws SQLException If a database access error occurs.
     */
    public static void updateEndTime(int gameId) throws SQLException {
        // SQL query to update the end_time for a specific game session.
        String sql = "UPDATE game_sessions SET end_time = ? WHERE game_id = ?";

        // Use try-with-resources for automatic closing of Connection and PreparedStatement.
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the current timestamp for the end_time column.
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            // Set the game ID for the WHERE clause.
            pstmt.setInt(2, gameId);
            pstmt.executeUpdate(); // Execute the update statement.
        }
    }
}