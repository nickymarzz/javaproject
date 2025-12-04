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
        String sql = "INSERT INTO games (game_name, description, end_time) VALUES (?, ?, NULL)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, gameName);
            statement.setString(2, description);
            // end_time is explicitly set to NULL in the SQL query
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

        // Removed "AND end_time IS NULL" to force update regardless of current end_time value for diagnostic purposes.
        String sql = "UPDATE games SET end_time = CURRENT_TIMESTAMP WHERE game_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, gameId);
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                System.err.println("Warning: No rows updated when closing game session for game_id=" + gameId + ". This might be because the gameId is invalid.");
            }
            // Removed "else" print statement as it's not relevant to diagnosing the problem of "not updated".
        } catch (SQLException ex) {
            System.err.println("Failed to close game session: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    public static void recordCollection(int gameId, ResourceType resourceType, int quantity, String notes) {
        if (gameId <= 0 || resourceType == null || quantity <= 0) {
            return;
        }

        // Insert into collection_events. The trigger will update game_resources.
        String insertSql = "INSERT INTO collection_events (game_id, resource_type, quantity, notes) VALUES (?, ?, ?, ?)";
        // Keep updating games table directly for resource quantities as per previous user request.
        String sqlUpdateGames = "UPDATE games SET " + resourceType.getDbColumnName() + " = " + resourceType.getDbColumnName()
                + " + ? WHERE game_id = ?";


        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql);
             PreparedStatement updateGamesStatement = connection.prepareStatement(sqlUpdateGames)) {

            // Insert into collection_events
            insertStatement.setInt(1, gameId);
            insertStatement.setString(2, resourceType.name()); // Use enum name for resource_type
            insertStatement.setInt(3, quantity);
            insertStatement.setString(4, notes);
            insertStatement.executeUpdate();

            // Update games table
            updateGamesStatement.setInt(1, quantity);
            updateGamesStatement.setInt(2, gameId);
            int updatedGamesRows = updateGamesStatement.executeUpdate();

            if (updatedGamesRows == 0) {
                System.err.println("Warning: No rows updated in 'games' table for game_id=" + gameId + " during collection event.");
            }

        } catch (SQLException ex) {
            System.err.println("Failed to record collection event: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Record quiz results for a game session. This writes total marks and passing_state
     * into the games and game_resources tables for the provided gameId.
     *
     * @param gameId the id of the game session
     * @param totalMarks number of correct answers
     * @param passingState true if the player passed, false if failed
     */
    public static void recordQuizResult(int gameId, int totalMarks, boolean passingState) {
        if (gameId <= 0) return;

        try (Connection connection = DatabaseManager.getConnection()) {
            // delegate to the connection-aware overload so tests can provide a fake connection
            recordQuizResult(gameId, totalMarks, passingState, connection);
        } catch (SQLException ex) {
            System.err.println("Failed to record quiz result (getConnection): " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Overload used for testing or callers that provide their own Connection.
     * This method will write the quiz totals to the games table and also to
     * the game_resources table so both places contain the final quiz summary.
     *
     * NOTE: The schema change for the game_resources table is assumed to be done
     * by migrations (you mentioned you'll handle SQL). This method simply sets
     * the values in both tables.
     */
    public static void recordQuizResult(int gameId, int totalMarks, boolean passingState, Connection connection) {
        if (gameId <= 0 || connection == null) return;

        String sql1 = "UPDATE games SET total_marks = ?, passing_state = ? WHERE game_id = ?";
        String sql2 = "UPDATE game_resources SET total_marks = ?, passing_state = ? WHERE game_id = ?";

        boolean originalAutoCommit = true;
        try (PreparedStatement stmt1 = connection.prepareStatement(sql1);
             PreparedStatement stmt2 = connection.prepareStatement(sql2)) {

            try {
                originalAutoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                // update games
                stmt1.setInt(1, totalMarks);
                // Use setBoolean so the JDBC driver writes an integer/boolean compatible value
                stmt1.setBoolean(2, passingState);
                stmt1.setInt(3, gameId);
                int updatedGames = stmt1.executeUpdate();

                // update game_resources
                stmt2.setInt(1, totalMarks);
                stmt2.setBoolean(2, passingState);
                stmt2.setInt(3, gameId);
                int updatedResources = stmt2.executeUpdate();

                // If no rows were updated, it means no entry exists for this gameId in game_resources, so insert one.
                if (updatedResources == 0) {
                    String insertSql = "INSERT INTO game_resources (game_id, total_marks, passing_state, cheat_sheet_qty, pencil_qty, coffee_qty) VALUES (?, ?, ?, 0, 0, 0)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, gameId);
                        insertStmt.setInt(2, totalMarks);
                        insertStmt.setBoolean(3, passingState);
                        insertStmt.executeUpdate();
                    }
                }

                // Informational logging
                if (updatedGames <= 0) {
                    System.err.println("Warning: no rows updated in 'games' for game_id=" + gameId);
                }

                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException r) {
                    System.err.println("Failed to rollback after quiz update failure: " + r.getMessage());
                }
                throw e;
            } finally {
                try {
                    connection.setAutoCommit(originalAutoCommit);
                } catch (SQLException ex) {
                    // ignore - leaving auto-commit in a consistent state is best-effort
                }
            }

        } catch (SQLException ex) {
            System.err.println("Failed to record quiz result (transaction): " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}



