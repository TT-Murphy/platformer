package gamelogic.level;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import gameengine.PhysicsObject;
import gameengine.graphics.Camera;
import gameengine.loaders.Mapdata;
import gameengine.loaders.Tileset;
import gamelogic.GameResources;
import gamelogic.Main;
import gamelogic.enemies.Enemy;
import gamelogic.enemies.Pea;
import gamelogic.enemies.PeaShooter;
import gamelogic.player.Player;
import gamelogic.tiledMap.Map;
import gamelogic.tiles.Flag;
import gamelogic.tiles.Flower;
import gamelogic.tiles.Gas;
import gamelogic.tiles.SolidTile;
import gamelogic.tiles.Spikes;
import gamelogic.tiles.Tile;
import gamelogic.tiles.Water;

public class Level {

	public boolean touchingGas = false;

	private LevelData leveldata;
	private Map map;
	public static Player player;
	private Camera camera;

	private boolean active;
	private boolean playerDead;
	private boolean playerWin;

	private ArrayList<Enemy> enemiesList = new ArrayList<>();
	private ArrayList<Flower> flowers = new ArrayList<>();
	private ArrayList<Water> waters = new ArrayList<>();
	private ArrayList<Gas> gasses = new ArrayList<>();
	public ArrayList<Pea> peas = new ArrayList<>();

	private List<PlayerDieListener> dieListeners = new ArrayList<>();
	private List<PlayerWinListener> winListeners = new ArrayList<>();

	private Mapdata mapdata;
	private int width;
	private int height;
	private int tileSize;
	private Tileset tileset;
	public static float GRAVITY = 70;

	public Level(LevelData leveldata) {
		this.leveldata = leveldata;
		mapdata = leveldata.getMapdata();
		width = mapdata.getWidth();
		height = mapdata.getHeight();
		tileSize = mapdata.getTileSize();
		restartLevel();
	}

	public LevelData getLevelData(){
		return leveldata;
	}

	public void restartLevel() {
		int[][] values = mapdata.getValues();
		Tile[][] tiles = new Tile[width][height];
		waters = new ArrayList();
		gasses = new ArrayList();
		enemiesList.clear(); // Clear old enemies
    	peas.clear();

		for (int x = 0; x < width; x++) {
			int xPosition = x;
			for (int y = 0; y < height; y++) {
				int yPosition = y;

				tileset = GameResources.tileset;

				tiles[x][y] = new Tile(xPosition, yPosition, tileSize, null, false, this);
				if (values[x][y] == 0)
					tiles[x][y] = new Tile(xPosition, yPosition, tileSize, null, false, this); // Air
				else if (values[x][y] == 1)
					tiles[x][y] = new SolidTile(xPosition, yPosition, tileSize, tileset.getImage("Solid"), this);

				else if (values[x][y] == 2)
					tiles[x][y] = new Spikes(xPosition, yPosition, tileSize, Spikes.HORIZONTAL_DOWNWARDS, this);
				else if (values[x][y] == 3)
					tiles[x][y] = new Spikes(xPosition, yPosition, tileSize, Spikes.HORIZONTAL_UPWARDS, this);
				else if (values[x][y] == 4)
					tiles[x][y] = new Spikes(xPosition, yPosition, tileSize, Spikes.VERTICAL_LEFTWARDS, this);
				else if (values[x][y] == 5)
					tiles[x][y] = new Spikes(xPosition, yPosition, tileSize, Spikes.VERTICAL_RIGHTWARDS, this);
				else if (values[x][y] == 6)
					tiles[x][y] = new SolidTile(xPosition, yPosition, tileSize, tileset.getImage("Dirt"), this);
				else if (values[x][y] == 7)
					tiles[x][y] = new SolidTile(xPosition, yPosition, tileSize, tileset.getImage("Grass"), this);
				else if (values[x][y] == 8)
					enemiesList.add(new Enemy(xPosition*tileSize, yPosition*tileSize, this)); // TODO: objects vs tiles
				else if (values[x][y] == 9)
					tiles[x][y] = new Flag(xPosition, yPosition, tileSize, tileset.getImage("Flag"), this);
				else if (values[x][y] == 10) {
					tiles[x][y] = new Flower(xPosition, yPosition, tileSize, tileset.getImage("Flower1"), this, 1);
					flowers.add((Flower) tiles[x][y]);
				} else if (values[x][y] == 11) {
					tiles[x][y] = new Flower(xPosition, yPosition, tileSize, tileset.getImage("Flower2"), this, 2);
					flowers.add((Flower) tiles[x][y]);
				} else if (values[x][y] == 12)
					tiles[x][y] = new SolidTile(xPosition, yPosition, tileSize, tileset.getImage("Solid_down"), this);
				else if (values[x][y] == 13)
					tiles[x][y] = new SolidTile(xPosition, yPosition, tileSize, tileset.getImage("Solid_up"), this);
				else if (values[x][y] == 14)
					tiles[x][y] = new SolidTile(xPosition, yPosition, tileSize, tileset.getImage("Solid_middle"), this);
				else if (values[x][y] == 15){
					tiles[x][y] = new Gas(xPosition, yPosition, tileSize, tileset.getImage("GasOne"), this, 1);
					gasses.add((Gas)tiles[x][y]);
				}
				else if (values[x][y] == 16){
					tiles[x][y] = new Gas(xPosition, yPosition, tileSize, tileset.getImage("GasTwo"), this, 2);
					gasses.add((Gas)tiles[x][y]);
				}
				else if (values[x][y] == 17){
					tiles[x][y] = new Gas(xPosition, yPosition, tileSize, tileset.getImage("GasThree"), this, 3);
					gasses.add((Gas)tiles[x][y]);
				}
				else if (values[x][y] == 18){
					tiles[x][y] = new Water(xPosition, yPosition, tileSize, tileset.getImage("Falling_water"), this, 0);
					waters.add((Water)tiles[x][y]);
					}
				else if (values[x][y] == 19){
					tiles[x][y] = new Water(xPosition, yPosition, tileSize, tileset.getImage("Full_water"), this, 3);
					waters.add((Water)tiles[x][y]);
					}
				else if (values[x][y] == 20){
					tiles[x][y] = new Water(xPosition, yPosition, tileSize, tileset.getImage("Half_water"), this, 2);
					waters.add((Water)tiles[x][y]);
					}
				else if (values[x][y] == 21){
					tiles[x][y] = new Water(xPosition, yPosition, tileSize, tileset.getImage("Quarter_water"), this, 1);
					waters.add((Water)tiles[x][y]);
					}
				else if (values[x][y] == 22){
					enemiesList.add(new PeaShooter(xPosition*tileSize, yPosition*tileSize, this));
					System.out.println("tried to make a pea");	
				}
			}

		}
		
		map = new Map(width, height, tileSize, tiles);
		camera = new Camera(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, 0, map.getFullWidth(), map.getFullHeight());
		player = new Player(leveldata.getPlayerX() * map.getTileSize(), leveldata.getPlayerY() * map.getTileSize(),
				this);
		camera.setFocusedObject(player);

		active = true;
		playerDead = false;
		playerWin = false;
	}

	public void onPlayerDeath() {
		active = false;
		playerDead = true;
		throwPlayerDieEvent();
	}

	public void onPlayerWin() {
		active = false;
		playerWin = true;
		throwPlayerWinEvent();
	}

	public void update(float tslf) {
		if (active) {
			// Update the player
			player.update(tslf);

			// Player death
			if (map.getFullHeight() + 100 < player.getY())
				onPlayerDeath();
			if (player.getCollisionMatrix()[PhysicsObject.BOT] instanceof Spikes)
				onPlayerDeath();
			if (player.getCollisionMatrix()[PhysicsObject.TOP] instanceof Spikes)
				onPlayerDeath();
			if (player.getCollisionMatrix()[PhysicsObject.LEF] instanceof Spikes)
				onPlayerDeath();
			if (player.getCollisionMatrix()[PhysicsObject.RIG] instanceof Spikes)
				onPlayerDeath();

			
			for (int i = 0; i < flowers.size(); i++) {
				if (flowers.get(i).getHitbox().isIntersecting(player.getHitbox())) {
					if(flowers.get(i).getType() == 1)
						water(flowers.get(i).getCol(), flowers.get(i).getRow(), map, 3);
					else
						addGas(flowers.get(i).getCol(), flowers.get(i).getRow(), map, 20, new ArrayList<Gas>());
					flowers.remove(i);
					i--;
				}
			}
			
			boolean didITouchWater = false;
			for (Water w : waters){
				if (w.getHitbox().isIntersecting(player.getHitbox())){
					if (w.getFullness() == 3){
						player.walkSpeed = 100;
					}
					if (w.getFullness() == 2){
						player.walkSpeed = 200;
					}
					if (w.getFullness() == 1){
						player.walkSpeed = 300;
					}
					didITouchWater = true;
				}
			}

			if (!didITouchWater){
				player.walkSpeed = 400;
			}

			boolean didITouchGas = false;
			for (Gas gas : gasses){
				if (gas.getHitbox().isIntersecting(player.getHitbox())){
					didITouchGas = true;
					touchingGas = true;
				}
			}
			if (!didITouchGas){
				touchingGas = false;
			}

			// Update the enemies
			for (int i = 0; i < enemiesList.size(); i++) {
				enemiesList.get(i).update(tslf);
				if (player.getHitbox().isIntersecting(enemiesList.get(i).getHitbox())) {
					onPlayerDeath();
				}
			}
			// Update the peas
			for (int i = 0; i < peas.size(); i++) {
				peas.get(i).update(tslf);
				if (player.getHitbox().isIntersecting(peas.get(i).getHitbox())) {
					onPlayerDeath();
				}
				peas.removeIf(p -> p.removed);
			}

			// Update the map
			map.update(tslf);

			// Update the camera
			camera.update(tslf);
		}
	}
	
	
	//#############################################################################################################
	// Your code goes here! 
	// Please make sure you read the rubric/directions carefully and implement the solution recursively!
	// pre-condition: 0<=fullness<=3, map is populated, the block at col,row must not already be a water block, and cannot be solid. 
	// post-condition: creates a water block that spreads according to the given instructions 
	
	private void water(int col, int row, Map map, int fullness) {

		Water w=null;

		if (fullness == 3){
			w = new Water (col, row, tileSize, tileset.getImage("Full_water"), this, fullness);
			waters.add(w);
		}
		else if (fullness == 2){
			w = new Water (col, row, tileSize, tileset.getImage("Half_water"), this, fullness);
			waters.add(w);
		}
		else if (fullness == 1){
			w = new Water (col, row, tileSize, tileset.getImage("Quarter_water"), this, fullness);
			waters.add(w);
		}
		else {
			w = new Water (col, row, tileSize, tileset.getImage("Falling_water"), this, fullness);
			waters.add(w);
		}

		//  water (You’ll need modify this to make different kinds of water such as half water and quarter water)
		
		map.addTile(col, row, w);
        //check if we can go down, go down if we can. 
		// if the block under the next block is solid, place a full water block, not a falling one.
		if (row+1 < map.getTiles()[0].length && !(map.getTiles()[col][row+1] instanceof Water) && !(map.getTiles()[col][row+1].isSolid())){
			if(row+2 < map.getTiles()[0].length && (map.getTiles()[col][row+2].isSolid())){
				water(col, row+1, map, 3);
			}
			else {
				water(col, row+1, map, 0);
			}

	
		
		}

			else if (row+1 < map.getTiles()[0].length && map.getTiles()[col][row+1].isSolid()){

			//if we can’t go down go left and right.
			//right
			if(col+1 < map.getTiles().length && !(map.getTiles()[col+1][row] instanceof Water) &&
			!(map.getTiles()[col+1][row].isSolid()) ) {
				// if the current fullness is a quarter block. 
				if (fullness == 1){
					water(col+1, row, map, fullness);
				}
				else {water(col+1, row, map, fullness-1);}
			}
			//left
			if(col-1 >= 0 && !(map.getTiles()[col-1][row] instanceof Water) &&
			!(map.getTiles()[col-1][row].isSolid())) {
				// if the current fullness is a quarter block.
				if (fullness == 1){
					water(col-1, row, map, fullness);
				}
				else {water(col-1, row, map, fullness-1);}
			}
		}
	}



	public void draw(Graphics g) {
	   	 g.translate((int) -camera.getX(), (int) -camera.getY());
	   	 // Draw the map
	   	 for (int x = 0; x < map.getWidth(); x++) {
	   		 for (int y = 0; y < map.getHeight(); y++) {
	   			 Tile tile = map.getTiles()[x][y];
	   			 if (tile == null)
	   				 continue;
	   			 if(tile instanceof Gas) {
	   				
	   				 int adjacencyCount =0;
	   				 for(int i=-1; i<2; i++) {
	   					 for(int j =-1; j<2; j++) {
	   						 if(j!=0 || i!=0) {
	   							 if((x+i)>=0 && (x+i)<map.getTiles().length && (y+j)>=0 && (y+j)<map.getTiles()[x].length) {
	   								 if(map.getTiles()[x+i][y+j] instanceof Gas) {
	   									 adjacencyCount++;
	   								 }
	   							 }
	   						 }
	   					 }
	   				 }
	   				 if(adjacencyCount == 8) {
	   					 ((Gas)(tile)).setIntensity(2);
	   					 tile.setImage(tileset.getImage("GasThree"));
	   				 }
	   				 else if(adjacencyCount >5) {
	   					 ((Gas)(tile)).setIntensity(1);
	   					tile.setImage(tileset.getImage("GasTwo"));
	   				 }
	   				 else {
	   					 ((Gas)(tile)).setIntensity(0);
	   					tile.setImage(tileset.getImage("GasOne"));
	   				 }
	   			 }
	   			 if (camera.isVisibleOnCamera(tile.getX(), tile.getY(), tile.getSize(), tile.getSize()))
	   				 tile.draw(g);
	   		 }
	   	 }


	   	 // Draw the enemies
	   	 for (int i = 0; i < enemiesList.size(); i++) {
	   		 enemiesList.get(i).draw(g);
	   	 }

		  for (int i = 0; i < peas.size(); i++) {
	   		 peas.get(i).draw(g);
	   	 }

	   	 // Draw the player
	   	 player.draw(g);




	   	 // used for debugging
	   	 if (Camera.SHOW_CAMERA)
	   		 camera.draw(g);
	   	 g.translate((int) +camera.getX(), (int) +camera.getY());
	    }


	// --------------------------Die-Listener
	public void throwPlayerDieEvent() {
		for (PlayerDieListener playerDieListener : dieListeners) {
			playerDieListener.onPlayerDeath();
		}
	}

	public void addPlayerDieListener(PlayerDieListener listener) {
		dieListeners.add(listener);
	}

	// ------------------------Win-Listener
	public void throwPlayerWinEvent() {
		for (PlayerWinListener playerWinListener : winListeners) {
			playerWinListener.onPlayerWin();
		}
	}

	public void addPlayerWinListener(PlayerWinListener listener) {
		winListeners.add(listener);
	}

	// ---------------------------------------------------------Getters
	public boolean isActive() {
		return active;
	}

	public boolean isPlayerDead() {
		return playerDead;
	}

	public boolean isPlayerWin() {
		return playerWin;
	}

	public Map getMap() {
		return map;
	}

	public Player getPlayer() {
		return player;
	}

// add gas method
// preconditions: col and row must me non-null, map must be populated, the object at col row must not be a gas block or a solid block. 
// post conditions: creates numSquaresToFill number of gas blocks in the specified pattern, unless there is nowhere for the gas to spread. 
	private void addGas(int col, int row, Map map, int numSquaresToFill, ArrayList<Gas> placedThisRound) {
		Gas g = new Gas (col, row, tileSize, tileset.getImage("GasOne"), this, 0);
		gasses.add(g);
		map.addTile(col, row, g);
		numSquaresToFill--;
		placedThisRound.add(g);

		while (placedThisRound.size()>0 && numSquaresToFill>0){
			row = placedThisRound.get(0).getRow();
			col = placedThisRound.get(0).getCol();

			placedThisRound.remove(0);


		
				
				if (row-1 < map.getTiles()[0].length && !(map.getTiles()[col][row-1].isSolid()) && !(map.getTiles()[col][row-1] instanceof Gas)&& numSquaresToFill>0){
					Gas x = new Gas (col, row-1, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col, row-1, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
				if (row-1 < map.getTiles()[0].length && col+1 < map.getTiles().length &&
				!(map.getTiles()[col+1][row-1].isSolid()) && !(map.getTiles()[col+1][row-1] instanceof Gas) && numSquaresToFill>0){
					Gas x = new Gas (col+1, row-1, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col+1, row-1, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
				if (row-1 < map.getTiles()[0].length && col-1 < map.getTiles().length &&
				!(map.getTiles()[col-1][row-1].isSolid()) && !(map.getTiles()[col-1][row-1] instanceof Gas) && numSquaresToFill>0){
					Gas x = new Gas (col-1, row-1, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col-1, row-1, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
				if (col+1 < map.getTiles().length &&
				!(map.getTiles()[col+1][row].isSolid()) && !(map.getTiles()[col+1][row] instanceof Gas) && numSquaresToFill>0){
					Gas x = new Gas(col+1, row, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col+1, row, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
				if (col-1 < map.getTiles().length &&
				!(map.getTiles()[col-1][row].isSolid()) && !(map.getTiles()[col-1][row] instanceof Gas) && numSquaresToFill>0){
					Gas x = new Gas (col-1, row, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col-1, row, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
				if (row+1 < map.getTiles()[0].length &&
				!(map.getTiles()[col][row+1].isSolid()) && !(map.getTiles()[col][row+1] instanceof Gas) && numSquaresToFill>0){
					Gas x = new Gas (col, row+1, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col, row+1, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
				 if (row+1 < map.getTiles()[0].length && col+1 < map.getTiles().length &&
				!(map.getTiles()[col+1][row+1].isSolid()) && !(map.getTiles()[col+1][row+1] instanceof Gas) && numSquaresToFill>0){
					Gas x = new Gas (col+1, row+1, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col+1, row+1, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
				if (row+1 < map.getTiles()[0].length && col-1 < map.getTiles().length &&
				!(map.getTiles()[col-1][row+1].isSolid()) && !(map.getTiles()[col-1][row+1] instanceof Gas) && numSquaresToFill>0){
					Gas x = new Gas (col-1, row+1, tileSize, tileset.getImage("GasOne"), this, 0);
					gasses.add(x);
					map.addTile(col-1, row+1, x);
					placedThisRound.add(x);
					numSquaresToFill--;
				}
			
		}
	}

}