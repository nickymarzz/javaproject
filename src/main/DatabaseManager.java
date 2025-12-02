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

public final class DatabaseManager {
    private static final Properties props = new Properties();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC driver not found: " + e.getMessage());
        }
        
        try (InputStream in = DatabaseManager.class
                .getClassLoader()
                .getResourceAsStream("config/db.properties")) {
            if (in == null) throw new IllegalStateException("Missing db.properties");
            props.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseManager() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }

    public static int createGameSession() throws SQLException {
        String sql = "INSERT INTO game_sessions (start_time) VALUES (?)";
        int gameId = -1;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    gameId = rs.getInt(1);
                }
            }
        }
        return gameId;
    }

    public static void updateEndTime(int gameId) throws SQLException {
        String sql = "UPDATE game_sessions SET end_time = ? WHERE game_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, gameId);
            pstmt.executeUpdate();
        }
    }
}