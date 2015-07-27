package physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import logic.PowerUp;
import logic.PredatorPowerUpType;
import logic.PredatorPowerUp;
import logic.PreyPowerUp;

/**
 * Class to apply the affects of a given power up to the world.
 * 
 * @author Simon Dicken
 * @version 2015-07-19
 */
public class PowerUpProcessor {

	private World world;
	
	private final float speedUpRatio = 10f;
	
	public PowerUpProcessor(World world) {
		this.world = world;
	}
	
	public void processPowerUp(PowerUp powerUp, Body body) {
		if (powerUp instanceof PredatorPowerUp) {
			PredatorPowerUp predPowerUp = (PredatorPowerUp) powerUp;
			processPredatorPowerUp(predPowerUp, body);
		} else if (powerUp instanceof PreyPowerUp) {
			// Need to add prey powerUps here...
		}
	}
	
	private void processPredatorPowerUp(PredatorPowerUp powerUp, Body body) {
		
		PredatorPowerUpType type = powerUp.getPType();
		
		switch (type) {
			case Freeze:
				break;
			case Random:
				break;
			case Reverse:
				break;
			case SlowDownPrey:
				break;
			case SpeedUpPredator:
				speedUpPredator(body);
				break;
			case Teleport:
				break;
			default:
				break;
		}
		
	}
	
	private void speedUpPredator(Body body) {
		
		Vector2 velocity = body.getLinearVelocity();
		float newXVelocity = velocity.x * speedUpRatio;
		float newYVelocity = velocity.y * speedUpRatio;
		body.setLinearVelocity(newXVelocity, newYVelocity);
		
	}
}
