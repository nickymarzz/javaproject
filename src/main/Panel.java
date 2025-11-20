package main;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.ParentObject;
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
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;
	
	
	//World map size
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	
	int fps = 60; //frames per second
	
	//SYSTEM
	TileManager tileM = new TileManager(this); //tile manager instance
	KeyHandler keyH = new KeyHandler(this); //key handler instance
	Thread gameThread; //keeps the game running like a clock 
	public CollisionChecker cChecker = new CollisionChecker(this); //collision checker instance
	public AssetSetter aSetter = new AssetSetter(this); //asset setter instance
	public UI ui = new UI(this); //user interface instance



	//ENTITY AND OBJECT
	public Player player = new Player(this, keyH); //player instance
	public ParentObject obj[] = new ParentObject[10]; //array of objects in the game
	public Entity npc[] = new Entity[10]; //array of NPCs in the game

	//GAME STATE
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 2;


	// panel constructor
	public Panel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		
		// double buffering -> smoother rendering
		this.setDoubleBuffered(true);
		
		this.addKeyListener(keyH); //add key listener to panel
		this.setFocusable(true); //panel can receive focus to get key input
	}


	// setup game objects (items, etc)
	public void setupGame() {
		aSetter.setObject(); //place objects in the game world
		aSetter.setNPC(); //place NPCs in the game world

		gameState = playState; //start game in play state

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
		if (gameState == playState) {
			//update player
			player.update();

			//update npc
			for(int i = 0; i < npc.length; i ++){
				if(npc[i] != null){
					npc[i].update();
				}
			}
		}
		if (gameState == pauseState) {
			//nothing for now
		}

		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //calls the parent of JPanel class
		
		//Graphics2D extends Graphics class-> more control over graphics
		Graphics2D g2 = (Graphics2D) g;
		
		//tile
		tileM.draw(g2); //draw tiles first (background)

		//objects
		for (int i =0; i<obj.length; i++) {//loop through all objects
			if (obj[i] != null) { //draw only if object exists
				obj[i].draw(g2, this); //draw objects (middle layer)
			}
		}

		//NPC
		for (int i =0; i<npc.length; i++) {//loop through all NPCs
			if (npc[i] != null) { //draw only if NPC exists
				npc[i].draw(g2); //draw NPCs 
			}
		}

		//player
		player.draw(g2); //draw player (top most)

		//UI
		ui.draw(g2); //draw user interface (top most)
		
		g2.dispose(); //free() but for like window,graphics,etc not memory 
	}
	

}
