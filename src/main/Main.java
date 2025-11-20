package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
	
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close window when user click "x"		
		window.setResizable(false); // Prevent user from resizing window
		window .setTitle("Sejong Student Simulator"); // Set title of window
		
		Panel mainPanel = new Panel();
		window.add(mainPanel);
		
		window.pack(); // Adjust window to fit
		
		window.setLocationRelativeTo(null); // center window on screen
		window.setVisible(true);
		
		// Create a new game session with a unique name based on timestamp
		String sessionName = "Game Session - " + System.currentTimeMillis();
		int gameId = GameDataClient.createGameSession(sessionName, "Player completed finals");
		mainPanel.ui.setGameId(gameId);
		
		mainPanel.setupGame(); // setup game objects
		mainPanel.startGameThread(); // start the game loop
	}
}
