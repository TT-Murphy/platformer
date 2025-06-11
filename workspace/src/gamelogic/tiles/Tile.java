package gamelogic.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

import gameengine.GameObject;
import gameengine.hitbox.RectHitbox;
import gameengine.maths.Vector2D;
import gamelogic.level.Level;

public class Tile{

	protected Vector2D position;
	protected int size;
	protected RectHitbox hitbox;
	protected BufferedImage image;
	protected boolean solid;
	protected Level level;
	
	public Tile(float x, float y, int size, BufferedImage image, boolean solid, Level level) {
		this.position = new Vector2D(x*size, y*size);
		this.size = size;
		this.image = image;
		this.solid = solid;
		this.level = level;
	}
	
	public void update (float tslf) {};
	
	public void draw (Graphics g) {
		if(image != null && level.touchingGas) {

			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < width; x++) {
            	for (int y = 0; y < height; y++) {
                	int rgba = image.getRGB(x, y);
                	Color color = new Color(rgba, true);

                	int red = 255 - color.getRed();
                	int green = 255 - color.getGreen();
                	int blue = 255 - color.getBlue();

                	Color invertedColor = new Color(red, green, blue);
                	outputImage.setRGB(x, y, invertedColor.getRGB());
            	}
        	}

			g.drawImage(outputImage, (int)position.x, (int)position.y, size, size, null);
		} else if (image != null){
			g.drawImage(image, (int)position.x, (int)position.y, size, size, null);
		}
		
		if(hitbox != null) hitbox.draw(g);		
	}
	
	//setter for image
	public void setImage(BufferedImage x){
		image = x;
	}
	
	//------------------------------------Getters
	public boolean isSolid() {
		return solid;
	}
	
	public RectHitbox getHitbox() {
		return hitbox;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public int getRow() {
		return (int)(position.y/size);
	}
	
	public int getCol() {
		return (int)(position.x/size);
	}
	
	public int getSize() {
		return size;
	}
}
