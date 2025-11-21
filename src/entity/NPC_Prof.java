package entity;

import main.Panel;
import java.util.Random;



public class NPC_Prof extends Entity {

    public NPC_Prof(Panel gp) {
        super(gp);

        // set default start position and speed
        direction = "down";
        speed = 1;

        getProfImage();
		setDialogue();
    }
    // load player images
	public void getProfImage() {
		
		up1 = setup("/npc/profBack1");
		up2 = setup("/npc/profBack2");
		down1 = setup("/npc/profFront1");
		down2 = setup("/npc/profFront2");
		left1 = setup("/npc/profLeft1");
		left2 = setup("/npc/profLeft2");
		right1 = setup("/npc/profRight1");
		right2 = setup("/npc/profRight2");
}

public void setDialogue(){

	dialogues[0] = "Hello.";
	dialogues[1] = "This is a dialogue";
	dialogues[2] = "Have you studied for your JAVA \nfinals?";
	dialogues[3] = "Only the best may get an A+...";
	dialogues[4] = "Show me what you got!!";
	dialogues[5] = "Question1";
	dialogues[6] = "Question2";




}



public void setAction(){

	actionCounter++;

	//move every 120seconds
	if(actionCounter == 120){

	Random random = new Random();
	int i = random.nextInt(100)+1;

	if (i <= 25){
		direction = "up";
	}
	if (i > 25 && i <= 50){
		direction = "down";
	} 
	if (i > 50 && i <= 75){
		direction = "left";
	} 
	if (i > 75 && i <= 100){
		direction = "right";
	} 

	actionCounter = 0;

		}

	}	

	//uses speak from entity
	public void speak(){
	super.speak();
		
		
	}



}
