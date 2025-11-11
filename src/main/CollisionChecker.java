package main;

import entity.Entity;

public class CollisionChecker {

    Panel gp;
    
    public CollisionChecker(Panel gp) {
    
        this.gp = gp;


    }

    public void checkTile(Entity entity) {
        // Calculate the lenght of hit box edges in world coordinates  
        // by adding the entity's world map position to the hit box offsets
        // For example, if the entity is at worldX=150 and the hit box x offset is 8,
        // then the left edge of the hit box in world coordinates is at 150 + 8 = 158
        int entityLeftWorldX = entity.worldX + entity.solidArea.x; 
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Calculate the tile columns and rows that the edges of the hit box are in by dividing
        // the world coordinates by the tile size
        // For example, if the tile size is 48 and the left edge of the hit box is at worldX=150,
        // then 150/48 = 3.125, which means the left edge is in column 3 (0-indexed)
        // this helps to identify which tiles the entity is currently overlapping with
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;
        
        // checks if tiles have collision = true
        // depending on the direction of movement
        // if a collision tile is detected, set entity.collisionOn to true
        // to prevent movement in that direction
        int tileNum1, tileNum2;

        //1. Find player's hitbox edges in world coordinates.
        //2. Convert edges to tile indices: tileIndex = worldPos / tileSize
        //3. Look ahead by speed in the moving direction and check the relevant tiles.
        //4. If any tile has collision = true, set entity.collisionOn = true to stop movement.
        switch(entity.direction) { 
            case "up": 
                //Divide the player’s pixel position by tile size to get which tile they are in. 
                //Add/subtract speed to see the tile they’ll move into next.
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
        
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }


    }
}
