package entity;

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
	
	// default solid area position
	public int solidAreaDefaultX, solidAreaDefaultY;

	// constructor for Entity class
	public Entity(Panel gp) {
		this.gp = gp;
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
