package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for performing various game-related data operations with the database.
 * This includes creating and closing game sessions, recording resource collections,
 * and storing quiz results.
 */
public final class GameDataClient {

    private GameDataClient() {
    }

    /**
     * Creates a new game session in the database with the provided game name and description.
     * The start time is automatically set by the database.
     *
     * @param gameName    The name of the game session.
     * @param description A description for the game session.
     * @return The auto-generated game ID for the new session, or -1 if the session creation failed.
     */
    public static int createGameSession(String gameName, String description) {
        // SQL query to insert a new game into the 'games' table.
        // end_time is initially set to NULL.
        String sql = "INSERT INTO games (game_name, description, end_time) VALUES (?, ?, NULL)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, gameName);
            statement.setString(2, description);
            statement.executeUpdate(); // Execute the insert statement.

            // Retrieve the auto-generated game ID.
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated game ID.
                }
            }
        } catch (SQLException ex) {
            System.err.println("Failed to create game session: " + ex.getMessage());
            // It's good practice to log the full stack trace for debugging.
            ex.printStackTrace(System.err);
        }

        return -1; // Indicate failure to create a game session.
    }

    /**
     * Closes an active game session by setting its end_time to the current timestamp.
     * This method forces the update regardless of the current end_time value.
     *
     * @param gameId The ID of the game session to close.
     */
    public static void closeGameSession(int gameId) {
        // Validate gameId to prevent unnecessary database operations.
        if (gameId <= 0) {
            System.err.println("Invalid gameId provided for closing session: " + gameId);
            return;
        }

        // SQL query to update the 'end_time' for a specific game session.
        String sql = "UPDATE games SET end_time = CURRENT_TIMESTAMP WHERE game_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, gameId);
            int updatedRows = statement.executeUpdate(); // Execute the update statement.

            // Provide a warning if no rows were updated, suggesting an invalid gameId.
            if (updatedRows == 0) {
                System.err.println("Warning: No rows updated when closing game session for game_id=" + gameId + ". This might be because the gameId is invalid.");
            }
        } catch (SQLException ex) {
            System.err.println("Failed to close game session: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Records a resource collection event for a specific game session.
     * This method inserts an entry into the 'collection_events' table and
     * updates the resource quantity in the 'games' table.
     *
     * @param gameId       The ID of the game session.
     * @param resourceType The type of resource collected (e.g., CHEAT_SHEET, PENCIL, COFFEE).
     * @param quantity     The quantity of the resource collected. Must be greater than 0.
     * @param notes        Optional notes about the collection event.
     */
    public static void recordCollection(int gameId, ResourceType resourceType, int quantity, String notes) {
        // Validate input parameters.
        if (gameId <= 0 || resourceType == null || quantity <= 0) {
            System.err.println("Invalid input for recording collection: gameId=" + gameId + ", resourceType=" + resourceType + ", quantity=" + quantity);
            return;
        }

        // SQL query to insert a new collection event into the 'collection_events' table.
        String insertSql = "INSERT INTO collection_events (game_id, resource_type, quantity, notes) VALUES (?, ?, ?, ?)";
        // SQL query to update the quantity of the specific resource in the 'games' table.
        String sqlUpdateGames = "UPDATE games SET " + resourceType.getDbColumnName() + " = " + resourceType.getDbColumnName()
                + " + ? WHERE game_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql);
             PreparedStatement updateGamesStatement = connection.prepareStatement(sqlUpdateGames)) {

            // Set parameters for the insert statement and execute.
            insertStatement.setInt(1, gameId);
            insertStatement.setString(2, resourceType.name()); // Use enum name for resource_type
            insertStatement.setInt(3, quantity);
            insertStatement.setString(4, notes);
            insertStatement.executeUpdate();

            // Set parameters for the update statement and execute.
            updateGamesStatement.setInt(1, quantity);
            updateGamesStatement.setInt(2, gameId);
            int updatedGamesRows = updateGamesStatement.executeUpdate();

            // Log a warning if no rows were updated in the 'games' table.
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
                // Store original auto-commit state and set to false for transaction management.
                originalAutoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                // Update 'games' table with quiz results.
                stmt1.setInt(1, totalMarks);
                // Use setBoolean so the JDBC driver writes an integer/boolean compatible value
                stmt1.setBoolean(2, passingState);
                stmt1.setInt(3, gameId);
                int updatedGames = stmt1.executeUpdate();

                // Update 'game_resources' table with quiz results.
                stmt2.setInt(1, totalMarks);
                stmt2.setBoolean(2, passingState);
                stmt2.setInt(3, gameId);
                int updatedResources = stmt2.executeUpdate();

                // If no rows were updated in 'game_resources', it means no entry exists for this gameId, so insert one.
                if (updatedResources == 0) {
                    String insertSql = "INSERT INTO game_resources (game_id, total_marks, passing_state, cheat_sheet_qty, pencil_qty, coffee_qty) VALUES (?, ?, ?, 0, 0, 0)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, gameId);
                        insertStmt.setInt(2, totalMarks);
                        insertStmt.setBoolean(3, passingState);
                        insertStmt.executeUpdate();
                    }
                }

                // Informational logging for 'games' table update.
                if (updatedGames <= 0) {
                    System.err.println("Warning: no rows updated in 'games' for game_id=" + gameId);
                }

                connection.commit(); // Commit the transaction if all operations are successful.
            } catch (SQLException e) {
                // Rollback the transaction in case of any SQL exception.
                try {
                    connection.rollback();
                } catch (SQLException r) {
                    System.err.println("Failed to rollback after quiz update failure: " + r.getMessage());
                }
                throw e; // Re-throw the original exception after rollback attempt.
            } finally {
                // Restore the original auto-commit state.
                try {
                    connection.setAutoCommit(originalAutoCommit);
                } catch (SQLException ex) {
                    // Ignore exceptions during auto-commit restoration as it's best-effort.
                }
            }

        } catch (SQLException ex) {
            System.err.println("Failed to record quiz result (transaction): " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}