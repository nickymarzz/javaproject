package object;

public class Pencil extends ParentObject {
    public Pencil() {
        name = "Pencil";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/pencil.png"));
        } catch (java.io.IOException e) {
            e.printStackTrace();
         }
        collision = true;
    }
    
}