package gamelogic.enemies;
import gamelogic.GameResources;
import gamelogic.level.Level;

public class Pea extends Enemy{
    
    private int speed = 40;
    public boolean removed = false;
    
    public Pea(float x, float y, Level l){
        super(x,y,l);
        this.image = GameResources.pea;
        this.movementVector.x = speed;
    }

    @Override
	public void update(float tslf) {
        updateCollisionMatrix(tslf);

		position.x += speed * tslf;
		if(collisionMatrix[RIG] != null) {
			removed = true;
        }
        if(collisionMatrix[LEF] != null) {
			level.peas.remove(this);
		} 
        hitbox.update();
	}

}
