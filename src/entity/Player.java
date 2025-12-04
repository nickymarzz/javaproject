package entity;

import main.Panel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;


import main.KeyHandler;
import main.GameDataClient;
import main.ResourceType;

/**
 * Represents the player character in the game.
 * Handles player movement, collision detection with tiles, objects, and NPCs,
 * and interactions such as picking up items, which are recorded in the database.
 */
public class Player extends Entity {

	KeyHandler keyH; // KeyHandler for player input

	public final int screenX; // Player's fixed X position on screen
	public final int screenY; // Player's fixed Y position on screen

	// Player's inventory: amount of each item the player has
	public int hasCoffee = 0;
	public int hasCheatSheet = 0;
	public int hasPencil = 0;

	/**
	 * Constructs a Player object.
	 *
	 * @param gp The main game panel instance, providing access to game-wide settings and components.
	 * @param keyH The KeyHandler instance for receiving player input.
	 */
	public Player(Panel gp, KeyHandler keyH) {
		// Call the parent (Entity) class constructor
		super(gp);
		this.keyH = keyH;

		// Calculate player's fixed position on the screen (center)
		screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

		// Initialize player's solid area (hitbox) for collision detection
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;

		// Store default hitbox positions to prevent drifting
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		setDefaultValues();
		getPlayerImage();
	}

	/**
	 * Sets the player's default starting values, such as world position, speed, and direction.
	 */
	public void setDefaultValues() {
		worldX = gp.tileSize * 25; // Starting X position in the game world
		worldY = gp.tileSize * 25; // Starting Y position in the game world
		speed = 4; // Player movement speed
		direction = "down"; // Default facing direction
	}

	/**
	 * Loads the player character's animation images from resource files.
	 */
	public void getPlayerImage() {
		up1 = setup("/player/playerBack1");
		up2 = setup("/player/playerBack2");
		down1 = setup("/player/playerFront1");
		down2 = setup("/player/playerFront2");
		left1 = setup("/player/playerLeft1");
		left2 = setup("/player/playerLeft2");
		right1 = setup("/player/playerRight1");
		right2 = setup("/player/playerRight2");
	}

	/**
	 * Updates the player's state, including movement, collision checks,
	 * object pickups, NPC interactions, and sprite animation.
	 */
	public void update() {
		// Only update player if the game is in the play state
		if (gp.gameState != gp.playState) {
        return;
    }

		// Check if any movement key is pressed
		if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

			// Determine player direction based on key press
			if(keyH.upPressed) {
				direction = "up";
			}
			if(keyH.downPressed) {
				direction = "down";
			}
			if(keyH.leftPressed) {
				direction = "left";
			}
			if(keyH.rightPressed) {
				direction = "right";
			}

			// CHECK TILE COLLISION (e.g., with trees, walls)
			collisionOn = false; // Reset collision flag before checking
			gp.cChecker.checkTile(this); // Perform tile collision check

			// CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true); // Perform object collision check
			pickUpObject(objIndex); // Handle object pickup if collision occurs

			// CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex); // Handle NPC interaction if collision occurs

			// If no collision, player can move
			if (!collisionOn) {
				switch(direction) {
				case "up":
					worldY -= speed; // Move up
					break;
				case "down":
					worldY += speed; // Move down
					break;
				case "left":
					worldX -= speed; // Move left
					break;
				case "right":
					worldX += speed; // Move right
					break;
				}
			}

			// Update sprite animation frame
			spriteCounter++;
			if (spriteCounter > 10) {
				if (spriteNum == 1) {
					spriteNum = 2;
				}
				else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0; // Reset counter
			}
		}
	}

	/**
	 * Handles the event of the player picking up an object.
	 * Increases the player's item count and removes the object from the game world.
	 * Also records the collection event in the database.
	 *
	 * @param i The index of the object in the game's object array, or 999 if no object was hit.
	 */
	public void pickUpObject(int i){
		if (i != 999){ // If an object was hit
			String objectName = gp.obj[i].name;

			switch(objectName) {
			case "Coffee":
				hasCoffee++; // Increment coffee count
				gp.obj[i] = null; // Remove coffee from game world
				gp.ui.showMessage("Coffee obtained!");
				// Log to database immediately using GameDataClient
				if (gp.ui.gameId > 0) {
					GameDataClient.recordCollection(gp.ui.gameId, ResourceType.COFFEE, 1, "Collected during gameplay");
				}
				break;

			case "Cheat Sheet":
				hasCheatSheet++; // Increment cheat sheet count
				gp.obj[i] = null; // Remove cheat sheet from game world
				gp.ui.showMessage("Cheat Sheet obtained!");
				// Log to database immediately using GameDataClient
				if (gp.ui.gameId > 0) {
					GameDataClient.recordCollection(gp.ui.gameId, ResourceType.CHEAT_SHEET, 1, "Collected during gameplay");
				}
				break;

			case "Pencil":
				hasPencil++; // Increment pencil count
				gp.obj[i] = null; // Remove pencil from game world
				gp.ui.showMessage("Pencil obtained!");
				// Log to database immediately using GameDataClient
				if (gp.ui.gameId > 0) {
					GameDataClient.recordCollection(gp.ui.gameId, ResourceType.PENCIL, 1, "Collected during gameplay");
				}
				break;
			}
		}
	}

	/**
	 * Handles interaction with an NPC (Non-Player Character).
	 * If the player presses enter while colliding with an NPC, it initiates dialogue.
	 *
	 * @param i The index of the NPC in the game's NPC array, or 999 if no NPC was hit.
	 */
	public void interactNPC(int i){
		if (i != 999){
			// Dialogue is initiated when ENTER is pressed
			if(gp.keyH.enterPressed) {
				gp.currentNPCIndex = i; // Set the currently interacting NPC
				gp.gameState = gp.dialogueState; // Change game state to dialogue
				gp.npc[i].speak(); // Trigger NPC's dialogue
				gp.keyH.enterPressed = false; // Consume the enter press
			}
		}
	}

	/**
	 * Draws the player character on the screen.
	 *
	 * @param g2 The Graphics2D object used for drawing.
	 */
	public void draw(Graphics2D g2) {
		BufferedImage image = null; // Initialize image to null

		// Determine which sprite image to draw based on current direction and animation frame
		switch(direction) {
		case "up":
			image = (spriteNum == 1) ? up1 : up2;
			break;
		case "down":
			image = (spriteNum == 1) ? down1 : down2;
			break;
		case "left":
			image = (spriteNum == 1) ? left1 : left2;
			break;
		case "right":
			image = (spriteNum == 1) ? right1 : right2;
			break;
		}
		// Draw the player image at the calculated screen position
		g2.drawImage(image, screenX, screenY, null);
	}
}