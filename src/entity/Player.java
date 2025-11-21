package entity;

import main.Panel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;


import main.KeyHandler;

public class Player extends Entity {
	
	KeyHandler keyH;
	
	public final int screenX; //player's position on screen (fixed)
	public final int screenY;

	// amt of items player has
	public int hasCoffee = 0;
	public int hasCheatSheet = 0;
	public int hasPencil = 0;
	
	public Player(Panel gp, KeyHandler keyH) {
		//because Player is a child class of Entity, 
		// need to call parent constructor
		super(gp);
		this.keyH = keyH;
		
		screenX = gp.screenWidth / 2 - (gp.tileSize / 2); //center of screen
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

		// hit box setup within player sprite
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;

		//prevents hit box drifting when player moves
		//by storing default x and y positions of hit box
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		setDefaultValues();
		getPlayerImage();
	}
	public void setDefaultValues() {
		worldX = gp.tileSize * 40; // starting position in the world
		worldY = gp.tileSize * 40;
		speed = 4;
		direction = "down"; // default direction
		
	}
	
	// load player images
	public void getPlayerImage() {

		
			up1 = setup("/player/playerBack1");
			up2 = setup("/player/playerBack2");
			down1 = setup("/player/playerFront1");
			down2 = setup("/player/playerFront2");
			left1 = setup("/player/playerLeft1");
			left2 = setup("/player/playerLeft2");
			right1 = setup("/player/playerRight1");
			right2 = setup("/player/playerRight2");
			
		
	}
	
	


	public void update() {
		
		// check if any movement key is pressed (stops character from running in place)
		if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {


			// player movement
			if(keyH.upPressed == true) {
				direction = "up";
				
			}
			if(keyH.downPressed == true) {
				direction = "down";
				
			}
			if(keyH.leftPressed == true) {
				direction = "left";
				
			}
			if(keyH.rightPressed == true) {
				direction = "right";
				
			}
			

			//CHECK TILE COLLISION (tree, wall )
			collisionOn = false; //reset collision flag before checking
			gp.cChecker.checkTile(this); //check tile collision

			//CHECK OBJ COLLISION 
			int objIndex = gp.cChecker.checkObject(this, true); //check object collision
			pickUpObject(objIndex);

			//CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);

			// if collision is false, player can move
			if (collisionOn == false) {
				switch(direction) { 
				case "up":
					worldY -= speed; //move up
					break;
				case "down":
					worldY += speed; //move down
					break;
				case "left":
					worldX -= speed; //move left
					break;
				case "right":
					worldX += speed; //move right
					break;
				}
			}

			// sprite animation
			spriteCounter++;
			if (spriteCounter >10) {
				if (spriteNum == 1) {
					spriteNum = 2;
				}
				else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0; //reset counter
			}
		}
		
		
		
	}

	public void pickUpObject(int i){

		if (i != 999){ //999 cuz max is 10 in object array(for now)
			
		String objectName = gp.obj[i].name;

		switch(objectName) {
		case "Coffee":
			hasCoffee++;
			gp.obj[i] = null; //remove coffee from game world
			gp.ui.showMessage("Coffee obtained!");
			break;

		case "Cheat Sheet":
			hasCheatSheet++;
			gp.obj[i] = null; //remove cheat sheet from game world
			gp.ui.showMessage("Cheat Sheet obtained!");
//---------------------------------------------------------------------------
			//JUST FOR TESTING PURPOSES (delete later)
			gp.ui.gameFinished = true; //trigger game finish
//---------------------------------------------------------------------------
			break;

		case "Pencil":
			hasPencil++;
			gp.obj[i] = null; //remove pencil from game world
			gp.ui.showMessage("Pencil obtained!");
			break;


			}
		}
	}

	
	public void interactNPC(int i){
		if (i != 999){
			//Dialogue when press  ENTER
			if(gp.keyH.enterPressed == true){
			gp.gameState = gp.dialogueState;
			gp.npc[i].speak();
			}	
		}
		//prevent perma ENTER pressed(reset)
		gp.keyH.enterPressed = false;
	}
	public void draw(Graphics2D g2) {
			
			BufferedImage image = null; //initially no image
		
			
			switch(direction) { //determine which image to draw based on direction
			case "up":
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
				break;
			case "down":
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
				break;
			case "left":
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
				break;
			case "right":
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
				break;
				
			}
			// draw the player image
			g2.drawImage(image, screenX, screenY, null);
	}

}
