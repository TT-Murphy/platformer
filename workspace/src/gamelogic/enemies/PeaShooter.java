package gamelogic.enemies;
import gamelogic.GameResources;
import gamelogic.level.Level;
import java.time.Duration;
import java.time.Instant;

public class PeaShooter extends Enemy{

    private Instant lastShotTime;


    public PeaShooter(float x, float y, Level level) {
        super(x,y,level);
		this.image = GameResources.peaShooter;
        lastShotTime = Instant.now();
    }

    public void update(float tslf){
        super.update(tslf);
        if (Duration.between(lastShotTime, Instant.now()).toMillis() >= 2000) {
            Pea myPea = new Pea(position.x,position.y,level);
            level.peas.add(myPea);
            lastShotTime = Instant.now(); // reset timer
        }
    }


}