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

    //trigger end(pass and fail)
    public boolean gameFinishedPass = false;
    public boolean gameFinishedFail = false;


    public String currentDialogue = "";

    public UI(Panel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
       

        //load item images
        Coffee coffee = new Coffee(gp);
        coffeeImage = coffee.image;

        CheatSheet cheatSheet = new CheatSheet(gp);
        cheatSheetImage = cheatSheet.image;

        Pencil pencil = new Pencil(gp);
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

       if (!(gameFinishedPass == true || gameFinishedFail == true)) {
        drawPlayScreen();
        }
        
        //PLAY 
        if (gp.gameState == gp.playState) {
          //nothing
        }
        //PAUSE
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        //DIALOGUE
        if (gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }
        
        // QUIZ STATE
        if (gp.gameState == gp.quizState) {
            drawQuizScreen();
        }

        //GAME FINISH SCREEN
        if (gameFinishedPass == true|| gameFinishedFail==true) {

           
            Color c = new Color(0, 0, 0, 150); // Black, 150/255 opacity
            g2.setColor(c);
            // Draw rectangle covering the entire screen
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);


            g2.setFont(arial_40);
            g2.setColor(Color.white);

            String text;
            String scoreText;
            int textLength;
            int x;
            int y;
            
            int score = gp.correctAnswers;
            int total = gp.questions.length;
            scoreText = "Score: " + score + " out of " + total;

        if (gameFinishedPass == true) {
             text = "You Passed Your Finals!";
             g2.setColor(Color.white);
        } else { // gameFinishedFail == true
             text = "You Failed Your Finals...";
             g2.setColor(Color.RED);
             g2.setFont(g2.getFont().deriveFont(55F));
        }
        
        // Draw Main Result Title
        textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth/2 - textLength/2;
        y = gp.screenHeight/2 - (gp.tileSize*3);
        g2.drawString(text, x, y);

        // Draw Score Sub-text
        g2.setColor(Color.LIGHT_GRAY);
        textLength = (int)g2.getFontMetrics().getStringBounds(scoreText, g2).getWidth();
        x = gp.screenWidth/2 - textLength/2;
        g2.drawString(scoreText, x, y + gp.tileSize);


        g2.setFont(arial_80B);

        if (gameFinishedPass == true) {
            text = "Congratulations!";
            g2.setColor(Color.YELLOW);
        } else {
            text = "Try Again Next Time.";
            g2.setColor(Color.ORANGE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        }
        
        textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth/2 - textLength/2;         
        y = gp.screenHeight/2 + (gp.tileSize*2);
        g2.drawString(text, x, y);

        // stop the game thread
        gp.gameThread = null;
        }
        else{
         //INVENTORY DISPLAY
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        g2.drawImage(coffeeImage, gp.tileSize/2, gp.screenHeight - gp.tileSize-10, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasCoffee, 74, gp.screenHeight-15);

         g2.drawImage(cheatSheetImage, gp.tileSize*3, gp.screenHeight - gp.tileSize-5, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasCheatSheet, 194, gp.screenHeight-15);

         g2.drawImage(pencilImage, 260, gp.screenHeight - gp.tileSize-5, gp.tileSize, gp.tileSize, null);
        g2.drawString("x " + gp.player.hasPencil, 310, gp.screenHeight-15);

        //MESSAGE DISPLAY
        if (messageOn==true) {
            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString(message, gp.tileSize/2, gp.tileSize*10);

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

    //DRAW DIALOGUE SCREEN
    public void drawDialogueScreen(){
        //SIZE SETTINGS
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;
        
        drawSubWindow(x, y, width, height);

        //TEXT SETTINGS
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,30));
        x+= gp.tileSize;
        y+= gp.tileSize;
        
        //Split text in dialogue and does \n when "\n" is used in dialogue
        //cuz \n does not work in graphics g2 
        for (String line : currentDialogue.split("\n")){
             g2.drawString(line, x, y);
            //next line via +y axis
             y+= 40;

        }
       

    }
    public void drawSubWindow(int x, int y, int width, int height ){
        //BLACK (TRANSLUCENT)
        Color c = new Color(0,0,0,220);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        //WHITE BORDER
        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void drawQuizScreen() {
        
        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth - gp.tileSize * 2;
        int frameHeight = gp.screenHeight - gp.tileSize * 4;

        if (gp.questions == null) {
        return; 
    }
        
        //draw in a subwindow
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        // Text start position
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize;
        
        // Set Font for Quiz
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
      
        // Retrieve the current question text
        String question = gp.questions[gp.currentQuestion];
        g2.setColor(Color.YELLOW); // Highlight the question
        
        // Draw the question 
        for(String line : question.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 45; // Move down for the next line
        }
        
        textY += gp.tileSize / 2; // Space between question and options

       //Draw answers
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
        String[] options = gp.options[gp.currentQuestion];

        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            
            // Highlight the currently selected option
            if (i == gp.quizSelection) {
                g2.setColor(Color.RED);
                // Draw the selection arrow
                g2.drawString(">", textX - gp.tileSize / 2, textY);
                g2.setColor(Color.CYAN); // Selected option text color
            } else {
                g2.setColor(Color.WHITE); // Unselected option text color
            }
            
            g2.drawString(option, textX, textY);
            textY += 40; // Line height for options
        }
    }

    public void drawPlayScreen(){
        int x = 0;
        int y = gp.tileSize*9;
        int width = gp.screenWidth - (gp.tileSize*8);
        int height = gp.tileSize*3;

         drawSubWindow(x, y, width, height);
    }
}
