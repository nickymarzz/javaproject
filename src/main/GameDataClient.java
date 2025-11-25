package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class GameDataClient {

    private GameDataClient() {
    }

    public static int createGameSession(String gameName, String description) {
        String sql = "INSERT INTO games (game_name, description) VALUES (?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, gameName);
            statement.setString(2, description);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Failed to create game session: " + ex.getMessage());
        }

        return -1;
    }

    public static void closeGameSession(int gameId) {
        if (gameId <= 0) {
            return;
        }

        String sql = "UPDATE games SET end_time = CURRENT_TIMESTAMP WHERE game_id = ? AND end_time IS NULL";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, gameId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Failed to close game session: " + ex.getMessage());
        }
    }

    public static void recordCollection(int gameId, ResourceType resourceType, int quantity, String notes) {
        if (gameId <= 0 || resourceType == null || quantity <= 0) {
            return;
        }

        String sql = """
                INSERT INTO collection_events (game_id, resource_type, quantity, notes)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, gameId);
            statement.setString(2, resourceType.name());
            statement.setInt(3, quantity);
            statement.setString(4, notes);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Failed to record collection event: " + ex.getMessage());
        }
    }
}

