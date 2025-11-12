package object;

import java.awt.image.BufferedImage;
import main.Panel;


public class ParentObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    public void draw(java.awt.Graphics2D g2, Panel gp) {
       int screenX = worldX - gp.player.worldX + gp.player.screenX; 
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		//draw tile only if it is within the visible screen area
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && //right side
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && //left side
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && //bottom side
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { //top side
		
            //draw the object
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); 
			
    }
}
}   
