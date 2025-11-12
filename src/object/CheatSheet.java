package object;


public class CheatSheet extends ParentObject {
     public CheatSheet() {
        name = "Cheat Sheet";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/CheatSheet.png"));
        } catch (java.io.IOException e) {
            e.printStackTrace();
         }
        collision = true;
    }
}
