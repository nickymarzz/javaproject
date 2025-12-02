package main;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
}