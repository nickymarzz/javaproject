package object;

import main.Panel;

public class Pencil extends ParentObject {
    Panel gp;
    public Pencil(Panel gp) {
        this.gp = gp;   
        name = "Pencil";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/pencil.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (java.io.IOException e) {
            e.printStackTrace();
         }
        collision = true;
    }
    
}