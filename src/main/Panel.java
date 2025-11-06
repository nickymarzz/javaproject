package main;

import javax.swing.JPanel;
import entity.Player;
import tile.TileManager;
import java.awt.*;

//implments Runnable->allows panel to be run by a Thread)
public class Panel extends JPanel implements Runnable {
	// size of one tile
	final int originalTileSize = 16; // 16x16
	final int scale = 3;
	
	// to fit in higher resolution screens
	public final int tileSize = originalTileSize * scale; // 48x48
	
	// size of the screen
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	
	// we have to scale the screen size
	final int screenWidth = tileSize * maxScreenCol;
	final int screenHeight = tileSize * maxScreenRow;
	
	int fps = 60; //frames per second
	
	TileManager tileM = new TileManager(this); //tile manager instance
	KeyHandler keyH = new KeyHandler(); //key handler instance
	Thread gameThread; //keeps the game running like a clock 
	Player player = new Player(this, keyH); //player instance
	
	
	// panel constructor
	public Panel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		
		// double buffering -> smoother rendering
		this.setDoubleBuffered(true);
		
		this.addKeyListener(keyH); //add key listener to panel
		this.setFocusable(true); //panel can receive focus to get key input
	}

	// start the game thread
	public void startGameThread() {
		gameThread = new Thread(this); //"this" refers to Panel class that implements Runnable
		gameThread.start();
	}

	
	// LOOP ("runnnable" or what will be executed in the thread)
	@Override
	public void run() {
		
		// delta/accumulator method for consistent fps
		double drawInterval = 1000000000/fps; //nanoseconds per frame
		double delta = 0; //time accumulator
		long lastTime = System.nanoTime(); //current time in nanoseconds
		long currentTime; // variable to store current time
		
		while(gameThread != null) {
			
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			
			if (delta >= 1) {
				//1 update + repaint cycle
				update(); // update game state 
				repaint(); // call paintComponent to redraw screen
				delta--;
			}
			
		}
		
	}
	public void update() {
		player.update();
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //calls the parent of JPanel class
		
	
		//Graphics2D extends Graphics class-> more control over graphics
		Graphics2D g2 = (Graphics2D) g;
		
		tileM.draw(g2); //draw tiles
		player.draw(g2); //draw player 
		
		
		g2.dispose(); //free() but for like window,graphics,etc not memory 
	}
	

}
