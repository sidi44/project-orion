package physics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import geometry.PointXY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import logic.GameState;
import logic.MazeNode;
import logic.powerup.PowerUpVisitor;
import logic.powerup.PredatorPowerUpFreezePrey;
import logic.powerup.PredatorPowerUpMagnet;
import logic.powerup.PredatorPowerUpSlowDownPrey;
import logic.powerup.PredatorPowerUpSpeedUp;
import logic.powerup.PredatorPowerUpTeleport;
import logic.powerup.PreyPowerUpFreezePredator;
import logic.powerup.PreyPowerUpMagnet;
import logic.powerup.PreyPowerUpSlowDownPredator;
import logic.powerup.PreyPowerUpSpeedUp;
import logic.powerup.PreyPowerUpTeleport;

/**
 * Class to apply the affects of a given power up to the world.
 * 
 * @author Simon Dicken
 * @version 2015-12-28
 */
public class PowerUpProcessor implements PowerUpVisitor {

	private Body body;
	private World world;
	private PhysicsProcessor physProc;
	private GameState state;
	
	private Map<Integer, Vector2> cachedVelocities;
	
	/**
	 * Constructor for PowerUpProcessor.
	 * 
	 * @param world - the Box2D world to which to apply the effects of power 
	 * ups.
	 * @param physProc - the physics processor which uses this power up 
	 * processor.
	 */
	public PowerUpProcessor(World world, PhysicsProcessor physProc) {
		this.world = world;
		this.physProc = physProc;
		this.body = null;
		this.state = null;
		
		this.cachedVelocities = new HashMap<Integer, Vector2>();
	}

	/**
	 * Set the physics body which has activated the power up to process.
	 * 
	 * @param body - the body which has activated the power up to process.
	 */
	public void setBody(Body body) {
		this.body = body;
	}
	
	/**
	 * Set the current game state when the power up is being applied.
	 * 
	 * @param state - the current game state.
	 */
	public void setState(GameState state) {
		this.state = state;
	}
	
	@Override
	public void visit(PredatorPowerUpSpeedUp powerUp) {		
		float speedUpFactor = (float) powerUp.getSpeedUpFactor();
		
		// If the power up is just starting or just about to end, the body's 
		// speed is about to change. The current body position may be 
		// incompatible with the new speed. Make sure it's ok by 'jogging' the 
		// position of the body by a small amount if necessary. (As long as the 
		// simulation step is sufficiently small, this will be imperceptible).
		if (powerUp.getTimeRemaining() == powerUp.getTimeLimit()) {
			jogBody(body, speedUpFactor);
		} else if (powerUp.getTimeRemaining() == 1) {
			jogBody(body, 1.0f);
		} else {
			speedUpBody(body, speedUpFactor);	
		}
		
	}

	@Override
	public void visit(PredatorPowerUpSlowDownPrey powerUp) {
		float slowDownFactor = (float) powerUp.getSlowDownFactor();
		
		if (powerUp.getTimeRemaining() == powerUp.getTimeLimit()) {
			jogBody(body, slowDownFactor);
		} else if (powerUp.getTimeRemaining() == 1) {
			jogBody(body, 1.0f);
		} else {
			slowDownBodies(PhysicsBodyType.Prey, slowDownFactor);
		}
		
	}

	@Override
	public void visit(PredatorPowerUpFreezePrey powerUp) {
		
		// We store the velocities of the bodies when they are frozen so we know
		// what to set them back to when the power up ends.
		if (powerUp.getTimeRemaining() == powerUp.getTimeLimit()) {
			cacheVelocities(PhysicsBodyType.Prey);
		} else if (powerUp.getTimeRemaining() == 1) {
			restoreVelocities(PhysicsBodyType.Prey);
		} else {
			freezeBodies(PhysicsBodyType.Prey);
		}
	}

	@Override
	public void visit(PredatorPowerUpMagnet powerUp) {
		
		// It's likely the application of the magnet force will leave the bodies
		// in a location which is incompatible with the movement mechanism. The
		// bodies are therefore 'jogged' to the nearest compatible position.
		if (powerUp.getTimeRemaining() == 1) {
			jogBodies(PhysicsBodyType.Prey, 1.0f);
		} else {
			int force = powerUp.getMagnetForce();
			int range = powerUp.getMagnetRange();
			applyMagnetForce(PhysicsBodyType.Prey, force, range);
		}
	}

	@Override
	public void visit(PredatorPowerUpTeleport powerUp) {
		teleportBody(body, powerUp.getNextPoint());
	}
	
	@Override
	public void visit(PreyPowerUpSpeedUp powerUp) {
		System.err.println("Prey power ups not implemented yet");
	}

	@Override
	public void visit(PreyPowerUpSlowDownPredator powerUp) {
		System.err.println("Prey power ups not implemented yet");
	}

	@Override
	public void visit(PreyPowerUpFreezePredator powerUp) {
		System.err.println("Prey power ups not implemented yet");
	}

	@Override
	public void visit(PreyPowerUpMagnet powerUp) {
		System.err.println("Prey power ups not implemented yet");
	}

	@Override
	public void visit(PreyPowerUpTeleport powerUp) {
		teleportBody(body, powerUp.getNextPoint());
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
		// The velocity is set to zero as the current velocity may not be valid
		// at the new position.
		body.setLinearVelocity(0, 0);
		
		Vector2 newPos = physProc.stateToWorld(teleportPoint);
		body.setTransform(newPos, body.getAngle());
	}
	
	/**
	 * Apply the given magnet force to bodies of the provided type that lie 
	 * within the provided range (given in maze squares).
	 * 
	 * @param type - the physics body type to which to apply the magnet force.
	 * @param magnetForce - the force of the magnet.
	 * @param range - the distance measured in maze squares over which the 
	 * magnet force has an effect.
	 */
	private void applyMagnetForce(PhysicsBodyType type, float magnetForce,
			int range) {
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				
				// Get the position of the body the power up is being applied 
				// to and the body that is applying the power up.
				Vector2 bPos = b.getPosition();
				Vector2 bodyPos = body.getPosition();
				
				// Check whether this body is within range of the magnet.
				Vector2 dir = bodyPos.sub(bPos);
				if (dir.len() > range * physProc.getSquareSize()) {
					continue;
				}
				
				// Normalise the direction vector
				Vector2 normDir = dir.nor();
				
				// Apply the magnet force
				b.setLinearVelocity(0, 0);
				float xForce = normDir.x * magnetForce;
				float yForce = normDir.y * magnetForce;
				b.applyForceToCenter(xForce, yForce, true);
			}
		}
		
	}
	
	/**
	 * Move all the bodies of the specified type to the nearest position which
	 * is compatible with their movement mechanism. The speed factor is used to
	 * scale the base speed of the body to be the actual speed of the body in 
	 * the next simulation step.
	 * 
	 * @param type - the type of physics body to move.
	 * @param speedFactor - the factor by which to scale the base speed of the 
	 * physics body to give the actual speed of the body in the next simulation
	 * step.
	 */
	private void jogBodies(PhysicsBodyType type, float speedFactor) {
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				jogBody(b, speedFactor);
			}
		}
		
	}
	
	/**
	 * Move the specified body to the nearest position which is compatible with
	 * its movement mechanism. The speed factor is used to scale the base speed 
	 * of the body to be the actual speed of the body in the next simulation 
	 * step.
	 * 
	 * @param body - the body to jog.
	 * @param speedFactor - the factor by which to scale the base speed of the 
	 * physics body to give the actual speed of the body in the next simulation
	 * step.
	 */
	private void jogBody(Body body, float speedFactor) {
		
		// Work out some useful position information
		Vector2 bodyPos = body.getPosition();
		PointXY statePos = physProc.worldToState(bodyPos);
		Vector2 worldPos = physProc.stateToWorld(statePos);
		
		// Grab the neighbouring positions of the current maze square
		MazeNode node = state.getMaze().getNode(statePos);
		Set<PointXY> neighbours = node.getNeighbours();
		
		// Sanity check
		if (neighbours.size() == 0) {
			System.err.println("Each node should have at least one neighbour");
			body.setTransform(worldPos, 0);
			body.setLinearVelocity(0, 0);
			return;
		}
		
		// Find which of the neighbouring nodes is closest to this body. This 
		// tells us along which axis the body is currently transitioning.
		double shortestDist = Double.MAX_VALUE;
		PointXY nearestNeighbour = null;
		
		for (PointXY neighbour : neighbours) {
			
			Vector2 neighbourWorldPos = physProc.stateToWorld(neighbour);
			double dist = bodyPos.dst(neighbourWorldPos);
			
			if (dist < shortestDist) {
				shortestDist = dist;
				nearestNeighbour = neighbour;
			}
		}
		
		// Sanity check, there should be a neighbour.
		if (nearestNeighbour == null) {
			System.err.println("Couldn't find nearest neighbour.");
			return;
		}
		
		// Work out what distance (step) the body travels in a single simulation
		// step.
		PhysicsData data = (PhysicsData) body.getUserData();
		float speed = physProc.getBodySpeed(data.getType()) * speedFactor;
		float simStep = physProc.getSimulationStep();
		float step = speed * simStep;
		
		// Is the body transitioning vertically or horizontally.
		if (nearestNeighbour.getX() == statePos.getX()) {
			// Vertically, so we know exactly what the x-coordinate should be.
			float posX = worldPos.x;
			
			// Find the nearest valid y-coordinate to the body's position for
			// the calculated step size.
			float yDist = bodyPos.y - worldPos.y;
			float remainder = yDist % step;
			if (Math.abs(remainder - step) < 0.00001 || 
				Math.abs(remainder) < 0.00001) {
				remainder = 0;
			}
			
			float posY = worldPos.y + (yDist - remainder);
			body.setTransform(posX, posY, 0);
			
		} else {
			// Horizontally, so we know exactly what the y-coordinate should be.
			float posY = worldPos.y;

			// Find the nearest valid x-coordinate to the body's position for
			// the calculated step size.
			float xDist = bodyPos.x - worldPos.x;
			float remainder = xDist % step;
			if (Math.abs(remainder - step) < 0.00001 || 
				Math.abs(remainder) < 0.00001) {
				remainder = 0;
			}
			
			float posX = worldPos.x + (xDist - remainder);
			body.setTransform(posX, posY, 0);
			
		}
		
		// Work out what the velocity should be. This is worked out as the 
		// vector to the nearest square centre (worldPos) from the current body
		// position, multiplied by the magnitude (speed).
		Vector2 vel = getVelocity(body.getPosition(), worldPos, speed);
		body.setLinearVelocity(vel);
	}
	
	/**
	 * Calculate the velocity by working out the vector from the given location
	 * to the given square centre and multiplying it by the given magnitude.
	 * 
	 * @param loc - the coordinates of the current location of the body.
	 * @param centre - the coordinates of the nearest square centre.
	 * @param magnitude - the magnitude of the velocity.
	 * @return the velocity with given magnitude in the direction from the given
	 * location to the given centre.
	 */
	private Vector2 getVelocity(Vector2 loc, Vector2 centre, float magnitude) {
		
		Vector2 velocity = null;
		
		float xDiff = Math.abs(loc.x - centre.x);
		float yDiff = Math.abs(loc.y - centre.y);
		
		if (xDiff == 0 && yDiff == 0) {
			velocity = new Vector2(0, 0);
		} else if (xDiff > yDiff) {
			if (loc.x > centre.x) {
				velocity = new Vector2(-magnitude, 0);
			} else {
				velocity = new Vector2(magnitude, 0);		
			}
		} else {
			if (loc.y > centre.y) {
				velocity = new Vector2(0, -magnitude);
			} else {
				velocity = new Vector2(0, magnitude);
			}
		}
		
		return velocity;
	}
	
	/**
	 * Store all the current velocities of bodies of the provided type.
	 * 
	 * Precondition: cachedVelcocities.size() == 0
	 * 
	 * @param type - the type of physics body for which to store the velocities.
	 */
	private void cacheVelocities(PhysicsBodyType type) {
		
		if (cachedVelocities.size() != 0) {
			System.err.println("Something's gone wrong.");
			return;
		}
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				
				PhysicsDataAgent agentData = (PhysicsDataAgent) data;
				int agentID = agentData.getID();
				
				// We need to copy the current velocity before it's cached.
				Vector2 currentVel = b.getLinearVelocity();
				Vector2 velocity = new Vector2(currentVel.x, currentVel.y);
				cachedVelocities.put(agentID, velocity);
			}
		}
	}
	
	/**
	 * Apply the velocities that were cached earlier back to the physics bodies
	 * of the specified type.
	 * 
	 * Postcondition: cachedVelocities.size() == 0
	 * 
	 * @param type - the type of physics body for which to restore the 
	 * velocities.
	 */
	private void restoreVelocities(PhysicsBodyType type) {
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				
				PhysicsDataAgent agentData = (PhysicsDataAgent) data;
				int agentID = agentData.getID();
				
				Vector2 velocity = cachedVelocities.remove(agentID);
				if (velocity == null) {
					continue;
				}
				b.setLinearVelocity(velocity);
				
			}
		}
		
		// We're finished with the cache, so empty all the velocities.
		cachedVelocities.clear();
	}
	
	
//	/**
//	 * Move all the bodies of the provided type to the nearest square centre.
//	 * 
//	 * @param type - the type of physics body to move.
//	 */
//	private void jogBodiesToCentre(PhysicsBodyType type) {
//		
//		Array<Body> bodies = new Array<Body>();
//		world.getBodies(bodies);
//		
//		for (Body b : bodies) {
//			PhysicsData data = (PhysicsData) b.getUserData();
//			if (data.getType() == type) {
//				
//				Vector2 bodyPos = b.getPosition();
//				PointXY statePos = physProc.worldToState(bodyPos);
//				Vector2 worldPos = physProc.stateToWorld(statePos);
//				
//				b.setTransform(worldPos, 0);
//				b.setLinearVelocity(0, 0);
//				b.applyForceToCenter(0, 0, true);
//			}
//			
//		}
//	}
	
	
// *****************************************************************************
// The following methods were used to reverse a body to go in the opposite 
// direction to that which the AI was trying to go. This includes going round 
// corners (i.e. the Agent should end up as far away from it's desired maze 
// square as possible).
// *****************************************************************************
	
//	/**
//	 * Set all of the bodies of the given type to move in the opposite direction
//	 * of travel to that which they are currently travelling in.
//	 * 
//	 * @param type - the type of bodies to reverse.
//	 */
//	private void reverseBodies(PhysicsBodyType type, boolean firstCall) {
//		
//		// Make sure we've got a type that makes sense
//		if (type != PhysicsBodyType.Predator && type != PhysicsBodyType.Prey) {
//			return;
//		}
//		
//		Array<Body> bodies = new Array<Body>();
//		world.getBodies(bodies);
//		
//		for (Body b : bodies) {
//			PhysicsData data = (PhysicsData) b.getUserData();
//			
//			if (data.getType() == type) {
//				// If the body's not moving, we don't need to reverse anything
//				if (b.getLinearVelocity().len() == 0) {
//					continue;
//				}
//				
//				PhysicsDataAgent agentData = (PhysicsDataAgent) data;
//				int agentID = agentData.getID();
//				
//				if (firstCall) {
//					agentData.setPreviousVelocity(b.getLinearVelocity());
//				}
//				
//				Agent agent;
//				if (type == PhysicsBodyType.Predator) {
//					agent = state.getPredator(agentID);
//				} else {
//					agent = state.getPrey(agentID);
//				}
//				
//				if(agent.isInTransition()) {
//					b.setLinearVelocity(agentData.getPreviousVelocity());
//				} else {
//					Vector2 velocity = b.getLinearVelocity();
//					if (velocity.x > 0) {
//						reverseRight(b);
//					} else if (velocity.x < 0) {
//						reverseLeft(b);
//					} else if (velocity.y > 0) {
//						reverseUp(b);	
//					} else if (velocity.y < 0){
//						reverseDown(b);
//					}
//				}
//				agentData.setPreviousVelocity(b.getLinearVelocity());
//			}
//		}
//	}
//	
//	private void reverseLeft(Body body) {
//		Maze maze = state.getMaze();
//		
//		Vector2 worldPos = body.getPosition();
//		PointXY statePos = physProc.worldToState(worldPos);
//		
//		PointXY right = new PointXY(statePos.getX() + 1, statePos.getY());
//		PointXY up = new PointXY(statePos.getX(), statePos.getY() + 1);
//		PointXY down = new PointXY(statePos.getX(), statePos.getY() - 1);
//		
//		Vector2 velocity = body.getLinearVelocity();
//		float speed = velocity.len();
//		
//		if (maze.areNeighbours(statePos, right)) {
//			body.setLinearVelocity(speed, 0);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, up)) {
//			body.setLinearVelocity(0, speed);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, down)) {
//			body.setLinearVelocity(0, -speed);
//			return;
//		}
//	}
//	
//	private void reverseRight(Body body) {
//		Maze maze = state.getMaze();
//		
//		Vector2 worldPos = body.getPosition();
//		PointXY statePos = physProc.worldToState(worldPos);
//		
//		PointXY left = new PointXY(statePos.getX() - 1, statePos.getY());
//		PointXY down = new PointXY(statePos.getX(), statePos.getY() - 1);
//		PointXY up = new PointXY(statePos.getX(), statePos.getY() + 1);		
//		
//		Vector2 velocity = body.getLinearVelocity();
//		float speed = velocity.len();
//		
//		if (maze.areNeighbours(statePos, left)) {
//			body.setLinearVelocity(-speed, 0);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, down)) {
//			body.setLinearVelocity(0, -speed);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, up)) {
//			body.setLinearVelocity(0, speed);
//			return;
//		}
//	}
//	
//	private void reverseUp(Body body) {
//		Maze maze = state.getMaze();
//		
//		Vector2 worldPos = body.getPosition();
//		PointXY statePos = physProc.worldToState(worldPos);
//		
//		PointXY down = new PointXY(statePos.getX(), statePos.getY() - 1);
//		PointXY left = new PointXY(statePos.getX() - 1, statePos.getY());
//		PointXY right = new PointXY(statePos.getX() + 1, statePos.getY());		
//		
//		Vector2 velocity = body.getLinearVelocity();
//		float speed = velocity.len();
//		
//		if (maze.areNeighbours(statePos, down)) {
//			body.setLinearVelocity(0, -speed);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, left)) {
//			body.setLinearVelocity(-speed, 0);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, right)) {
//			body.setLinearVelocity(speed, 0);
//			return;
//		}
//	}
//	
//	private void reverseDown(Body body) {
//		Maze maze = state.getMaze();
//		
//		Vector2 worldPos = body.getPosition();
//		PointXY statePos = physProc.worldToState(worldPos);
//		
//		PointXY up = new PointXY(statePos.getX(), statePos.getY() + 1);
//		PointXY right = new PointXY(statePos.getX() + 1, statePos.getY());	
//		PointXY left = new PointXY(statePos.getX() - 1, statePos.getY());	
//		
//		Vector2 velocity = body.getLinearVelocity();
//		float speed = velocity.len();
//		
//		if (maze.areNeighbours(statePos, up)) {
//			body.setLinearVelocity(0, speed);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, right)) {
//			body.setLinearVelocity(speed, 0);
//			return;
//		}
//		
//		if (maze.areNeighbours(statePos, left)) {
//			body.setLinearVelocity(-speed, 0);
//			return;
//		}
//		
//	}
	
}
