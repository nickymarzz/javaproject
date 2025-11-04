package entity;

import java.awt.image.BufferedImage;

public class Entity { // Base class for all entities(Player, NPC etc) in the game
	
	public int x,y;
	public int speed;
	
	
	// store image files
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;
	
	// sprite animation
	public int spriteCounter = 0;
	public int spriteNum = 1; //which sprite to display
	

}
