package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import main.Panel;
import main.UtilityTool;

public class TileManager {
	
	Panel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	
	public TileManager(Panel gp) {
		this.gp = gp;
		
		tile = new Tile[10]; //10 different tiles
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; //map array
		getTileImage(); //load tile images
		loadMap("/maps/map01.txt"); //load map from text file
	}
	
	//load tile images
	public void getTileImage() {

			setup(0, "grass", false);
			setup(1, "wall", true);
			setup(2, "path", false);
			setup(3, "asphalt", false);
			setup(4, "tree", true);
			
	}

	//generic method to setup tiles
	public void setup(int index, String imageName, boolean collision) {
		UtilityTool uTool = new UtilityTool();

		try{
			//create new tile object
			//loads tile image from resources folder
			//scale tile image to fit tile size
			//set collision property 

			tile[index] = new Tile();
			tile[index].image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName+".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
 			tile[index].collision = collision;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	//load map from text file
	public void loadMap(String filePath) {
		try {
			//read map file
			InputStream is = getClass().getResourceAsStream(filePath);
			
			//buffered reader to read file line by line
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int row = 0;
			while(row < gp.maxWorldRow) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				String numbers[] = line.split(" ");
				
				for (int col = 0; col < gp.maxWorldCol; col++) {
					if (col < numbers.length) {
						int num = Integer.parseInt(numbers[col]);
						mapTileNum[col][row] = num;
					}
				}
				row++;
			}
			br.close(); //close buffered reader
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//draw all tiles
	public void draw(Graphics2D g2) {
		
		int worldCol =0;
		int worldRow =0;

		
		
		//loop through all columns and rows of the screen
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) { 
			
		int tileNum = mapTileNum[worldCol][worldRow];
		
		//calculate tile position in the world. (0,0), (0,32), (32,0) etc
		int worldX = worldCol * gp.tileSize; 
		int worldY = worldRow * gp.tileSize;
		
		/*screenX and screenY -> visible tiles on the screen
		-worldX and worldY -> actual position of the tile in the world map
		-gp.player.worldX/Y -> player's position in the world
		-gp.player.screenX/Y -> player's position on the screen (fixed) 
		-calculate where to draw each tile on the screen based
		 on the player's position in the world*/
		int screenX = worldX - gp.player.worldX + gp.player.screenX; 
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		//draw tile only if it is within the visible screen area
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && //right side
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && //left side
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && //bottom side
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { //top side
		
			//tile[tileNum] gets the tile image from the array
			g2.drawImage(tile[tileNum].image, screenX, screenY, null); 
			
		}
			worldCol++; 
		
			
			if(worldCol == gp.maxWorldCol) { //end of the row
				worldCol =0;
				worldRow++;
			}
		}
	}
}


