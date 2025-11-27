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
	public KeyHandler keyH = new KeyHandler(this); //key handler instance
	Thread gameThread; //keeps the game running like a clock 
	public CollisionChecker cChecker = new CollisionChecker(this); //collision checker instance
	public AssetSetter aSetter = new AssetSetter(this); //asset setter instance
	public UI ui = new UI(this); //user interface instance



	//ENTITY AND OBJECT
	public Player player = new Player(this, keyH); //player instance
	public ParentObject obj[] = new ParentObject[10]; //array of objects in the game
	public Entity npc[] = new Entity[10]; //array of NPCs in the game
	public int currentNPCIndex = 999; //track which npc

	//QUIZ SETTINGS
	public String[] questions;
	public String[][] options;
	public int[] answers;
	public int currentQuestion = 0;
	public int quizSelection = 0; // 0, 1, 2, or 3 for option A, B, C, D
	public int correctAnswers = 0;
	final int passScore = 2;

	//GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int quizState = 4;

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

		setupQuiz();

		aSetter.setObject(); //place objects in the game world
		aSetter.setNPC(); //place NPCs in the game world

		gameState = titleState; //start game in play state

	}

	//setup quiz data
	public void setupQuiz() {
    questions = new String[] {
        "What is the keyword for inheritance \nin Java?",
        "Which access modifier makes a \nfield visible everywhere?",
        "How do you declare a constant in \nJava?"
    };

    options = new String[][] {
        {"A. extends", "B. implements", "C. inherits", "D. uses"},
        {"A. private", "B. protected", "C. default", "D. public"},
        {"A. const int", "B. final static int", "C. int const", "D. static int"}
    };

    // Index of the correct option for each question (0=A, 1=B, 2=C, 3=D)
    answers = new int[] {0, 3, 1}; 
}

	public void resetGame() {
    ui.gameFinishedPass = false;
    ui.gameFinishedFail = false;
    
    currentQuestion = 0;
    quizSelection = 0;
    correctAnswers = 0;
    
    ui.commandNum = 0;
    
    player.hasCoffee = 0;
    player.hasCheatSheet = 0;
    player.hasPencil = 0;
    

    player.setDefaultValues();
    setupGame();
    
    if (gameThread == null) {
        startGameThread();
    }
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
		if (gameState == dialogueState) {
            if (keyH.enterPressed == true) {
                
                // Advance the dialogue line for the current NPC
                if (currentNPCIndex != 999 && npc[currentNPCIndex] != null) {
                    npc[currentNPCIndex].speak(); 
                } 
                
                // Consume the ENTER keypress
                keyH.enterPressed = false; 
            }
        }
		if (gameState == quizState) {
        // Handle quiz logic
        if (keyH.enterPressed == true) {
            checkAnswerAndAdvance();
            keyH.enterPressed = false; // Consume the key press
        }
		}
		if (gameState == pauseState) {
			//nothing for now
			}
	}

	public void checkAnswerAndAdvance() {

	if (currentQuestion >= questions.length) {
         return;
    }
	
    // Check if the selected option is the correct answer
    if (quizSelection == answers[currentQuestion]) {
        correctAnswers++;
        ui.showMessage("Correct!");
    } else {
        ui.showMessage("Incorrect.");
    }

    // Move to the next question
    currentQuestion++;

    // Check if the quiz is finished
    if (currentQuestion >= questions.length) {
        if (correctAnswers >= passScore) {
            // Finals Passed
            ui.gameFinishedPass = true;
            ui.showMessage("Finals finished! You passed!");
        } else {
            // Finals Failed
            ui.gameFinishedFail = true;
            ui.showMessage("Finals finished! You failed.");
        }
		gameState = playState;
    } else {

        // Reset selection for the new question
        quizSelection = 0;
    }
}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); //calls the parent of JPanel class
		
		//Graphics2D extends Graphics class-> more control over graphics
		Graphics2D g2 = (Graphics2D) g;
		

		//TITLE SCREEN
		if (gameState == titleState){
			ui.draw(g2);

		}
		else {
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
	

}
