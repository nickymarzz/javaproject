package entity;

import main.Panel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.KeyHandler;

public class Player extends Entity {
	
	Panel p;
	KeyHandler keyH;
	
	public Player(Panel p, KeyHandler keyH) {
		this.p = p;
		this.keyH = keyH;
		
		setDefaultValues();
		getPlayerImage();
	}
	public void setDefaultValues() {
		x = 100;
		y = 100;
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
				y -= speed; // -y axis
			}
			if(keyH.downPressed == true) {
				direction = "down";
				y += speed; // +y axis
			}
			if(keyH.leftPressed == true) {
				direction = "left";
				x -= speed; // -x axis
			}
			if(keyH.rightPressed == true) {
				direction = "right";
				x += speed; // +x axis
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
	public void draw(Graphics2D g2) {
		
			BufferedImage image = null; //initially null
			
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
			g2.drawImage(image, x, y, p.tileSize, p.tileSize, null);
	}

}
