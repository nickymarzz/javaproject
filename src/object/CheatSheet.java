package object;
import main.Panel;


public class CheatSheet extends ParentObject {
    Panel gp;
     public CheatSheet(Panel gp) {
         this.gp = gp;  
        name = "Cheat Sheet";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/CheatSheet.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (java.io.IOException e) {
            e.printStackTrace();
         }
        collision = true;
    }
}
