package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Panel;
import main.UtilityTool;


public class Entity { // Base class for all entities(Player, NPC etc) in the game
	
	public int worldX,worldY;
	public int speed;
	

	Panel gp;
	
	// store image files
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;
	
	// sprite animation
	public int spriteCounter = 0;
	public int spriteNum = 1; //which sprite to display
	
	// hit box for all entities
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public boolean collisionOn = false;

	// gives a timer for npc to make an action
	public int actionCounter = 0;

	// default solid area position
	public int solidAreaDefaultX, solidAreaDefaultY;

	//Dialogue
	String dialogues[] = new String[20];
	int dialogueIndex = 0;

	// constructor for Entity class
	public Entity(Panel gp) {
		this.gp = gp;
		solidAreaDefaultX = solidArea.x;
    	solidAreaDefaultY = solidArea.y;
	}


	public void setAction(){}
	public void speak(){
		//prevent null exeption error
		if (dialogues[dialogueIndex]==null){
			dialogueIndex = 0;
		}

		//loops dialogue
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		//make npc face player during dialogue
		switch(gp.player.direction){
			case "up":
				direction = "down";
				break;
			case "down":
				direction = "up";
				break;
			case "left":
				direction = "right";
				break;
			case "right":
				direction = "left";
				break;
		}
	}
	public void update(){

		setAction();

		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkPlayer(this);
	

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
			if (spriteCounter >12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				}
				else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0; //reset counter
			}


	}

	public void draw(Graphics2D g2){

		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX; 
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		//draw tile only if it is within the visible screen area
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && //right side
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && //left side
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && //bottom side
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { //top side
		
				switch(direction) { 
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
				
				//draw the npc
				g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
		}
		   	 
			
	
	
	// method to load and scale images
	public BufferedImage setup(String imagePath) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}

