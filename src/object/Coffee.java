package object;

public class Coffee extends ParentObject {

    public Coffee() {
        name = "Coffee";
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/objects/coffee.png"));
        } catch (java.io.IOException e) {
            e.printStackTrace();
         }
    }
     
     
}
