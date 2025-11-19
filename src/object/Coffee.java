package object;

import main.Panel;


public class Coffee extends ParentObject {

    Panel gp;

    public Coffee(Panel gp) {
         this.gp = gp;  
        name = "Coffee";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/coffee.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (java.io.IOException e) {
            e.printStackTrace();
         }
        collision = true;
    }
}
