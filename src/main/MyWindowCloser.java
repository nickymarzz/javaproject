package main;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException; // Added import

public class MyWindowCloser extends WindowAdapter {
    private final Panel gp; // Reference to the Panel
    private final JFrame window;

    public MyWindowCloser(Panel gp, JFrame window) { // Modified constructor
        this.gp = gp;
        this.window = window;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        // Close current overall game session (game_sessions table) if active
        if (gp.gameId != -1) {
            try {
                DatabaseManager.updateEndTime(gp.gameId);
                System.out.println("Overall game session " + gp.gameId + " ended from window close.");
            } catch (SQLException ex) {
                System.err.println("Error updating end time for overall game session on window close: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // Close current game-specific session ('games' table) if active
        if (gp.ui.gameId != -1) {
            GameDataClient.closeGameSession(gp.ui.gameId);
            System.out.println("Game-specific session " + gp.ui.gameId + " ended from window close.");
        }
        
        window.dispose();
        System.exit(0);
    }
}
