package physics;

import geometry.PointXY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import logic.Agent;
import logic.Direction;
import logic.GameState;
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
 * @version 2015-10-18
 */
public class PowerUpProcessor implements PowerUpVisitor {

	private Body body;
	private World world;
	private PhysicsProcessor physProc;
	private GameState state;
	
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
		speedUpBody(body, speedUpFactor);
	}

	@Override
	public void visit(PredatorPowerUpSlowDownPrey powerUp) {
		float slowDownFactor = (float) powerUp.getSlowDownFactor();
		slowDownBodies(PhysicsBodyType.Prey, slowDownFactor);
	}

	@Override
	public void visit(PredatorPowerUpFreezePrey powerUp) {
		freezeBodies(PhysicsBodyType.Prey);
	}

	@Override
	public void visit(PredatorPowerUpMagnet powerUp) {
		int force = powerUp.getMagnetForce();
		int range = powerUp.getMagnetRange();
		applyMagnetForce(PhysicsBodyType.Prey, force, range);
		
		// In certain circumstances, the affect of the magnet can leave 
		// AI-controlled bodies stuck. At the end of the power up, this method
		// is used to 'jog' them towards the nearest maze square centre, 
		// preventing them getting stuck.
		if (powerUp.getTimeRemaining() == 1) {
			jogBodies(PhysicsBodyType.Prey);
		}
	}

	@Override
	public void visit(PredatorPowerUpTeleport powerUp) {
		teleportBody(body, powerUp.getNextPoint());
	}
	
	@Override
	public void visit(PreyPowerUpSpeedUp powerUp) {
		float speedUpFactor = (float) powerUp.getSpeedUpFactor();
		speedUpBody(body, speedUpFactor);
	}

	@Override
	public void visit(PreyPowerUpSlowDownPredator powerUp) {
		float slowDownFactor = (float) powerUp.getSlowDownFactor();
		slowDownBodies(PhysicsBodyType.Predator, slowDownFactor);
	}

	@Override
	public void visit(PreyPowerUpFreezePredator powerUp) {
		freezeBodies(PhysicsBodyType.Predator);
	}

	@Override
	public void visit(PreyPowerUpMagnet powerUp) {
		int force = powerUp.getMagnetForce();
		int range = powerUp.getMagnetRange();
		applyMagnetForce(PhysicsBodyType.Predator, force, range);
		
		// In certain circumstances, the affect of the magnet can leave 
		// AI-controlled bodies stuck. At the end of the power up, this method
		// is used to 'jog' them towards the nearest maze square centre, 
		// preventing them getting stuck.
		if (powerUp.getTimeRemaining() == 1) {
			jogBodies(PhysicsBodyType.Predator);
		}
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
	 * Set all the bodies of the provided type to move towards the centre of 
	 * the closest maze square. Sets the next move direction of the 
	 * corresponding Agent to match.
	 * 
	 * @param type - the physics body type to 'jog'.
	 */
	private void jogBodies(PhysicsBodyType type) {
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == type) {
				
				PhysicsDataAgent agentData = (PhysicsDataAgent) data;
				int agentID = agentData.getID();
				Agent agent = state.getAgent(agentID);
				
				float speed = physProc.getBodySpeed(type);
				
				Vector2 truePos = b.getPosition();
				PointXY statePos = physProc.worldToState(truePos);
				Vector2 worldPos = physProc.stateToWorld(statePos);
				Vector2 vec = truePos.sub(worldPos);
				
				float magX = Math.abs(vec.x);
				float magY = Math.abs(vec.y);
				if (magX > magY) {
					if (vec.x < 0) {
						b.setLinearVelocity(speed, 0);
						agent.setNextMoveDirection(Direction.Right);
					} else {
						b.setLinearVelocity(-speed, 0);
						agent.setNextMoveDirection(Direction.Left);
					}
				} else {
					if (vec.y < 0) {
						b.setLinearVelocity(0, speed);
						agent.setNextMoveDirection(Direction.Up);
					} else {
						b.setLinearVelocity(0, -speed);
						agent.setNextMoveDirection(Direction.Down);
					}
				}
			}
		}
	}
	
	
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
