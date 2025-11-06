package tile;

import main.Panel;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager{

    Panel gp;
    Tile[] tile;
    int mapTileNum[][];

    public TileManager(Panel gp){
        this.gp = gp;

        tile = new Tile[10]; // 10 tile types
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow]; //map array

		getTileImage(); //load tile images
		loadMap(); //load map from text file

}

public void getTileImage(){ //load tile images
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
					
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

//load map from text file
	public void loadMap() {
		try {
			//read map file
			InputStream is = getClass().getResourceAsStream("/maps/map01.txt");
			
			//buffered reader to read file line by line
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col =0;
			int row =0;
			
			while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
				
				String line = br.readLine();
				
				while(col < gp.maxScreenCol) {
					String numbers[] = line.split(" "); //split string at " "
					
					int num = Integer.parseInt(numbers[col]); //parse to int
					
					mapTileNum[col][row] = num; //store number in map array
					col++;
				}
				if(col == gp.maxScreenCol) {
					col =0;
					row++;
				}
			}
			br.close(); //close buffered reader
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//draw all tiles
	public void draw(Graphics2D g2) {
		
		int col =0;
		int row =0;
		int x =0;
		int y =0;
		
		//loop through all columns and rows of the screen
		while(col < gp.maxScreenCol && row < gp.maxScreenRow) { 
			
		int tileNum = mapTileNum[col][row];
		
		//tile[tileNum] gets the tile image from the array
			g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null); 
			col++; 
			x += gp .tileSize; 
			
			if(col == gp.maxScreenCol) { //end of the row
				col =0;
				x=0;
				row++;
				y += gp.tileSize;
			}
		}
	}
}

