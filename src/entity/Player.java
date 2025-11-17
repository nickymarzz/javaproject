package entity;

import main.Panel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Rectangle;


import javax.imageio.ImageIO;

import main.KeyHandler;

public class Player extends Entity {
	
	Panel gp;
	KeyHandler keyH;
	
	public final int screenX; //player's position on screen (fixed)
	public final int screenY;

	// amt of items player has
	public int hasCoffee = 0;
	public int hasCheatSheet = 0;
	public int hasPencil = 0;
	
	public Player(Panel gp, KeyHandler keyH) {
		this.gp = gp;
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
		
		// try-catch for IOException
		try {
			// ImageIO.read() to read image file
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/playerBack1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/playerBack2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/playerFront1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/playerFront2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/playerLeft1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/playerLeft2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/playerRight1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/playerRight2.png"));
			
		} catch(IOException e) { //input-output error catch
			e.printStackTrace(); //print error details
		}
		
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
			

		
			collisionOn = false; //reset collision flag before checking
			gp.cChecker.checkTile(this); //check tile collision

			int objIndex = gp.cChecker.checkObject(this, true); //check object collision
			pickUpObject(objIndex);


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
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}

}
