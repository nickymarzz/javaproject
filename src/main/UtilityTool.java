package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {

    // method to scale an image to desired width and height
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
       
        // create a new buffered image with desired size
		BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }
    
}
