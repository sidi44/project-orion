package physics;

import logic.powerup.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsBodyPredatorPowerUp extends PhysicsBodyPowerUp {

	public PhysicsBodyPredatorPowerUp(World world, Vector2 worldPos, 
			float powerUpRadius, PowerUp powerUp) {
		super(PhysicsBodyType.PowerUpPredator, world, worldPos, powerUpRadius, 
				powerUp);
	}

}
