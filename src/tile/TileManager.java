package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import main.Panel;

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
	
	public void getTileImage() {
	
		try {
			
			//grass tile
			tile[0] = new Tile();
			tile[0].image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			
			//wall tile
			tile[1] = new Tile();
			tile[1].image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[1].collision = true; //wall is not walkable
			
			//path tile
			tile[2] = new Tile();
			tile[2].image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/tiles/path.png"));
			
			//asphalt tile
			tile[3] = new Tile();
			tile[3].image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/tiles/asphalt.png"));
			
			//tree tile
			tile[4] = new Tile();
			tile[4].image = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
			tile[4].collision = true; //tree is not walkable
			
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
			g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null); 
			
		}
			worldCol++; 
		
			
			if(worldCol == gp.maxWorldCol) { //end of the row
				worldCol =0;
				worldRow++;
			}
		}
	}
}


