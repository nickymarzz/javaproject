package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


//to handle keyboard input
public class KeyHandler implements KeyListener {
	
	Panel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;


	//constructor
	public KeyHandler(Panel gp) {
		this.gp = gp;
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode();

		//PLAY STATE
		if (gp.gameState == gp.playState){
			
		if(code == KeyEvent.VK_W) { 
			upPressed = true; //W key is pressed(up)
		}
		if (code == KeyEvent.VK_S) {
			downPressed = true; // S key is pressed(down)
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = true; // A key is pressed(left)
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = true; // D key is pressed(right)
		}
		if (code == KeyEvent.VK_P) {
			gp.gameState = gp.pauseState; //change to pause state
		}
		if (code == KeyEvent.VK_ENTER){
			enterPressed = true;
		}	
	}
		//PAUSE STATE
		if (gp.gameState==gp.pauseState){
			if (code == KeyEvent.VK_P) {
			gp.gameState = gp.playState; //change to play state
		}	
	}
		//DIALOGUE STATE
		if (gp.gameState == gp.dialogueState){
		if (code == KeyEvent.VK_ENTER){
			enterPressed = true;
		}
	}

		//QUIZ STATE
		if (gp.gameState == gp.quizState){
    	if (code == KeyEvent.VK_W) { // Move selection Up
        	gp.quizSelection--;
        if (gp.quizSelection < 0) {
            	gp.quizSelection = gp.options[gp.currentQuestion].length - 1; // Wrap around
        	}
    	}
    	if (code == KeyEvent.VK_S) { // Move selection Down
       		gp.quizSelection++;
        if (gp.quizSelection >= gp.options[gp.currentQuestion].length) {
           		 gp.quizSelection = 0; // Wrap around
        	}
    	}
   		 if (code == KeyEvent.VK_ENTER){
        	enterPressed = true; // Set flag to be handled in Panel update/logic
   		 }		
	}
		//GAME FINSHED
		if (gp.ui.gameFinishedPass || gp.ui.gameFinishedFail) {
    	if (code == KeyEvent.VK_ENTER) {
        // Reset game variables
        	gp.resetGame(); 
        
        // Change the game state back to the Title Screen
        	gp.gameState = gp.titleState; 
			
			return;
    }
}

		//TITLE STATE
		if (gp.gameState == gp.titleState){
    	if (code == KeyEvent.VK_W) { // Move selection Up
        	gp.ui.commandNum--;
        if (gp.ui.commandNum < 0) {
            gp.ui.commandNum = 1; // Wrap around
        }
    }
    	if (code == KeyEvent.VK_S) { // Move selection Down
       		 gp.ui.commandNum++;
        if (gp.ui.commandNum > 1) {
            gp.ui.commandNum = 0; // Wrap around
        }
    }
    if (code == KeyEvent.VK_ENTER){
		if (gp.ui.commandNum == 0){
			gp.gameState = gp.playState;
			if (gp.gameThread == null) {
                gp.startGameThread(); 
            }
		}
		if (gp.ui.commandNum == 1){
			System.exit(0);
		}
        
    }
}



}

	@Override
	public void keyReleased(KeyEvent e) {
		
		int code =  e.getKeyCode();
		
		if(code == KeyEvent.VK_W) { 
			upPressed = false; //W key is released
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false; // S key is released
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = false; // A key is released
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false; // D key is released
		}
		if (code == KeyEvent.VK_ENTER){
			enterPressed = false;
		}
		
	}
	

}
