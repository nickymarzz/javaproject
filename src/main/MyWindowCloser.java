package main;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyWindowCloser extends WindowAdapter {
    private final int gameId;
    private final JFrame window;

    public MyWindowCloser(int gameId, JFrame window) {
        this.gameId = gameId;
        this.window = window;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        GameDataClient.closeGameSession(gameId);
        window.dispose();
        System.exit(0);
    }
}
