package physics;

import geometry.PointXY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import logic.PowerUp;
import logic.PredatorPowerUpType;
import logic.PredatorPowerUp;
import logic.PreyPowerUp;
import logic.PreyPowerUpType;

/**
 * Class to apply the affects of a given power up to the world.
 * 
 * @author Simon Dicken
 * @version 2015-08-15
 */
public class PowerUpProcessor {

	private World world;
	private float squareSize;
	
	private final float predatorSpeedUpFactor = 2f;
	private final float preySlowDownFactor = 2f;
	private final float preySpeedUpFactor = 2f;
	private final float predatorSlowDownFactor = 2f;
	private final PointXY teleportPoint = new PointXY(0, 0);
	
	/**
	 * Constructor for PowerUpProcessor.
	 * 
	 * @param world - the Box2D world to which to apply the effects of power 
	 * ups.
	 * @param squareSize - the length of the side of a maze square in the Box2D 
	 * world.
	 */
	public PowerUpProcessor(World world, float squareSize) {
		this.world = world;
		this.squareSize = squareSize;
	}
	
	/**
	 * Apply the effects of the provided power up, which was activated by the 
	 * provided body, to the world.
	 * 
	 * @param powerUp - the power up to process.
	 * @param body - the associated body which activated the power up.
	 */
	public void processPowerUp(PowerUp powerUp, Body body) {
		if (powerUp instanceof PredatorPowerUp) {
			PredatorPowerUp predPowerUp = (PredatorPowerUp) powerUp;
			processPredatorPowerUp(predPowerUp, body);
		} else if (powerUp instanceof PreyPowerUp) {
			PreyPowerUp preyPowerUp = (PreyPowerUp) powerUp;
			processPreyPowerUp(preyPowerUp, body);
		}
	}
	
	/**
	 * Process a predator power up. Depending on the type of the power up, an 
	 * appropriate action is applied to the world.
	 * 
	 * @param powerUp - the predator power up to process.
	 * @param body - the associated body which activated the power up.
	 */
	private void processPredatorPowerUp(PredatorPowerUp powerUp, Body body) {
		
		PredatorPowerUpType type = powerUp.getPType();
		
		switch (type) {
			case Freeze:
				freezeBodies(PhysicsBodyType.Prey);
				break;
			case Reverse:
				reverseBodies(PhysicsBodyType.Prey);
				break;
			case SlowDownPrey:
				slowDownBodies(PhysicsBodyType.Prey, preySlowDownFactor);
				break;
			case SpeedUpPredator:
				speedUpBody(body, predatorSpeedUpFactor);
				break;
			case Teleport:
				teleportBody(body, teleportPoint);
				break;
			default:
				throw new IllegalArgumentException("Unsupported power up type");
		}
	}
	
	/**
	 * Process a prey power up. Depending on the type of the power up, an 
	 * appropriate action is applied to the world.
	 * 
	 * @param powerUp - the prey power up to process.
	 * @param body - the associated body which activated the power up.
	 */
	private void processPreyPowerUp(PreyPowerUp powerUp, Body body) {
		
		PreyPowerUpType type = powerUp.getPType();
		
		switch (type) {
			case Freeze:
				freezeBodies(PhysicsBodyType.Predator);
				break;
			case Reverse:
				reverseBodies(PhysicsBodyType.Predator);
				break;
			case SlowDownPredator:
				slowDownBodies(PhysicsBodyType.Predator, 
						predatorSlowDownFactor);
				break;
			case SpeedUpPrey:
				speedUpBody(body, preySpeedUpFactor);
				break;
			case Teleport:
				teleportBody(body, teleportPoint);
				break;
			default:
				throw new IllegalArgumentException("Unsupported power up type");
		}
		
	}

	/**
	 * Set all the bodies of the given type to have zero velocity.
	 * 
	 * @param type - the type of bodies to freeze.
	 */
	private void freezeBodies(PhysicsBodyType type) {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				b.setLinearVelocity(0, 0);
			}
		}
	}
	
	/**
	 * Set all of the bodies of the given type to move in the opposite direction
	 * of travel to that which they are currently travelling in.
	 * 
	 * @param type - the type of bodies to reverse.
	 */
	private void reverseBodies(PhysicsBodyType type) {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				Vector2 velocity = b.getLinearVelocity();
				float newXVelocity = -velocity.x;
				float newYVelocity = -velocity.y;
				b.setLinearVelocity(newXVelocity, newYVelocity);
			}
		}
	}
	
	/**
	 * Reduce the linear velocity of bodies of the given type by the provided
	 * factor.
	 * 
	 * @param type - the type of bodies to slow down.
	 * @param slowDownFactor - how much to slow bodies down by.
	 */
	private void slowDownBodies(PhysicsBodyType type, float slowDownFactor) {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				Vector2 velocity = b.getLinearVelocity();
				float newXVelocity = velocity.x / slowDownFactor;
				float newYVelocity = velocity.y / slowDownFactor;
				b.setLinearVelocity(newXVelocity, newYVelocity);
			}
		}
	}
	
	/**
	 * Increase the linear velocity of the given body by the provided factor.
	 * 
	 * @param body - the body to speed up.
	 * @param speedUpFactor - how much to speed the body up by.
	 */
	private void speedUpBody(Body body, float speedUpFactor) {
		Vector2 velocity = body.getLinearVelocity();
		float newXVelocity = velocity.x * speedUpFactor;
		float newYVelocity = velocity.y * speedUpFactor;
		body.setLinearVelocity(newXVelocity, newYVelocity);
	}
	
	/**
	 * Move the given body to the given point.
	 * 
	 * @param body - the body to move.
	 * @param teleportPoint - the point in the back-end logic coordinate system
	 * to move the body to.
	 */
	private void teleportBody(Body body, PointXY teleportPoint) {
		Vector2 newPos = 
			PhysicsProcessorBox2D.stateToWorld(teleportPoint, squareSize);
		body.setTransform(newPos, body.getAngle());
	}
	
}
