package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
	
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Don't exit on close, handle it manually		
		window.setResizable(false); // Prevent user from resizing window
		window .setTitle("Sejong Student Simulator"); // Set title of window
		
		Panel mainPanel = new Panel();
		window.add(mainPanel);
		
		window.pack(); // Adjust window to fit
		
		window.setLocationRelativeTo(null); // center window on screen
		window.setVisible(true);
		
		// The game-specific session in the 'games' table will now only be created
		// when the user explicitly selects "NEW GAME" from the title screen,
		// to avoid creating entries that might skip numbering if the user exits immediately.
		// String sessionName = "Game Session - " + System.currentTimeMillis();
		// int gameId = GameDataClient.createGameSession(sessionName, "Player started the application");
		// mainPanel.ui.setGameId(gameId);
		
		// Add a WindowListener to handle closing the game-specific session
		// MyWindowCloser is designed to handle cases where gp.ui.gameId is -1 (no game started yet).
		window.addWindowListener(new MyWindowCloser(mainPanel, window)); 
		
		mainPanel.setupGame(); // setup game objects
		mainPanel.startGameThread(); // start the game loop
	}
}
