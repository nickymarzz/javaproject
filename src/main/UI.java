package main;

import java.awt.*;
import java.awt.image.BufferedImage;

import object.CheatSheet;
import object.Coffee;
import object.Pencil;



public class UI {

    Panel gp;
    Font arial_40, arial_80B;    
    BufferedImage coffeeImage;
    BufferedImage cheatSheetImage;
    BufferedImage pencilImage;
    Graphics2D g2;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    //end the game(not implemented yet)
    public boolean gameFinished = false;

    public UI(Panel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        //load item images
        Coffee coffee = new Coffee();
        coffeeImage = coffee.image;

        CheatSheet cheatSheet = new CheatSheet();
        cheatSheetImage = cheatSheet.image;

        Pencil pencil = new Pencil();
        pencilImage = pencil.image;
    }

    //display message on screen
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    //draws the user interface
    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.playState) {
          //nothing to draw in play state
        }
        //PAUSE SCREEN
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        //GAME FINISH SCREEN
        if (gameFinished == true) {
            g2.setFont(arial_40);
            g2.setColor(Color.white);

            String text;
            int textLength;
            int x;
            int y;

            //display congratulations message
            text = "You Passed Your Finals!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            
            //center the text by subtracting half of message length
            //from half of the screen width
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize*3);
            g2.drawString(text, x, y);

            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Congratulations!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;        
            y = gp.screenHeight/2 + (gp.tileSize*2);
            g2.drawString(text, x, y);

            //stop the game
            gp.gameThread = null;
        }
        else{
            //INVENTORY DISPLAY
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        g2.drawImage(coffeeImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasCoffee, 74, 65);

         g2.drawImage(cheatSheetImage, gp.tileSize*3, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasCheatSheet, 194, 65);

         g2.drawImage(pencilImage, 260, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasPencil, 310, 65);

        //MESSAGE DISPLAY
        if (messageOn==true) {
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.tileSize/2, gp.tileSize*5);

            //timer for message to disappear
            messageCounter++;

            if (messageCounter >120) { //display for 2 seconds
                messageCounter = 0;
                messageOn = false;
                
            }
       
        }
        }
    }
    //DRAW PAUSE SCREEN
    public void drawPauseScreen() {
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;;
        g2.drawString(text, x, y);
    }
    //get x coordinate to center text on screen
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}
