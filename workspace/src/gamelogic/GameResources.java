package gamelogic;

import java.awt.image.BufferedImage;

import gameengine.loaders.ImageLoader;
import gameengine.loaders.Tileset;
import gameengine.loaders.TilesetLoader;

public final class GameResources {

	public static Tileset tileset;
	
	public static BufferedImage enemy;
	public static BufferedImage pea;
	public static BufferedImage peaShooter;

	
	public static void load() {
		try {
			tileset = TilesetLoader.loadTileset("/workspaces/platformer/workspace/gfx/tileset.txt", ImageLoader.loadImage("/workspaces/platformer/workspace/gfx/tileset.png"));
			
			enemy = ImageLoader.loadImage("/workspaces/platformer/workspace/gfx/Enemy.png");
			pea = ImageLoader.loadImage("/workspaces/platformer/workspace/gfx/Pea.png");
			peaShooter = ImageLoader.loadImage("/workspaces/platformer/workspace/gfx/PeaShooter.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
