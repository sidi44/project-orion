package physics;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import logic.Agent;
import logic.GameState;
import logic.Maze;
import logic.MazeNode;
import logic.Move;
import logic.Direction;
import logic.powerup.Magnet;

/**
 * PhysicsProcessorBox2D class.
 * 
 * An implementation of the PhysicsProcessor interface using Box2D. 
 * 
 * The class creates all the Box2D bodies from the initial GameState on 
 * construction. Every time it processes a GameState, it updates the position 
 * of the Agents, runs the simulation for one timestep, processes any collisions 
 * and updates the GameState data with a new snapshot of the post-simulation 
 * game.
 * 
 * @author Simon Dicken
 * @version 2015-12-28
 */
public class PhysicsProcessorBox2D extends PhysicsProcessor {
	
	// The game world
	private final PhysicsGameWorld world;
	
	// Used to convert speed from an index value to an actual speed.
	private final PhysicsSpeedConverter speedConverter;
	
	// Physics simulation step variables
	private final float dt;
	private float accumulator;
	
	// The type of debug information to display
	private PhysicsDebugType debugType;
	private boolean debugBodiesCreated;
	
	/**
	 * Constructor for PhysicsProcessorBox2D.
	 * 
	 * @param world - the Box2D world to be used in the physics simulation.
	 * @param initialState - the initial snapshot of the game data. Should 
	 * include the fully formed Maze and all playing agents.
	 * @param config - the configuration data used to set up the Physics
	 * simulation.
	 */
	public PhysicsProcessorBox2D(GameState initialState, 
			PhysicsConfiguration config) {
		
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		World box2DWorld = new World(gravity, doSleep);
		this.world = new PhysicsGameWorld(box2DWorld, initialState, config);
		
		this.dt = config.getTimestep();
		this.accumulator = 0;
		
		PhysicsContact contact = new PhysicsContact(this);
		this.world.addContactListener(contact);
		
		this.speedConverter = config.getSpeedConverter();
		
		this.debugType = PhysicsDebugType.DebugNone;
		this.debugBodiesCreated = false;
	}
	
	@Override
	public PhysicsGameWorld getWorld() {
		return world;
	}

	@Override
	public int stepSimulation(float timestep, GameState state) {
		
		// Grab the time difference. Limit the maximum amount of time we can 
		// progress the physics simulation for a given render frame.
		float delta = (float) Math.min(timestep, 0.25);
		
		// Add this frame's time to the accumulator.
		accumulator += delta;
		
		int numSimSteps = 0;
		
		// Step the simulation at the given fixed rate for as many times as 
		// required. Any left over time is passed over to the next frame.
		while (accumulator >= dt) {
			
			// Run the simulation for one timestep.
			preStep(state);		
			world.getBox2DWorld().step(dt, 8, 3);			
			postStep(state);
			
			accumulator -= dt;
			++numSimSteps;
		}
		
		return numSimSteps;
	}
	
	/**
	 * Carry out any work that needs to be done immediately BEFORE the 
	 * simulation is stepped. This includes extracting the game state data and
	 * applying it to the world (e.g. each Agent's next move).
	 * 
	 * @param state - a snapshot of the current game data.
	 */
	private void preStep(GameState state) {
		
		Maze maze = state.getMaze();
		
		List<PhysicsBodyAgent> allAgents = world.getAgents();
		for (PhysicsBodyAgent agent : allAgents) {
			preProcessAgentPowerUps(agent, state);
		}
		
		for (PhysicsBodyAgent agent : allAgents) {
			preProcessAgent(agent, maze);
		}
				
		// Process any debug information
		preProcessDebugInfo(state);
	}

	/**
	 * Carry out any work that needs to be done immediately AFTER the simulation 
	 * is stepped. This involves updating the game state with the 
	 * post-simulation data (e.g. new positions of each Agent).
	 * 
	 * @param state - a snapshot of the current game data.
	 */
	private void postStep(GameState state) {
		
		List<PhysicsBody> bodiesToProcess = new ArrayList<PhysicsBody>();
		bodiesToProcess.addAll(world.getPredators());
		bodiesToProcess.addAll(world.getPrey());
		bodiesToProcess.addAll(world.getPills());
		bodiesToProcess.addAll(world.getPredatorPowerUps());
		
		// Remove 'dead' bodies from the game and inform the GameState of the 
		// changes.
		for (PhysicsBody body : bodiesToProcess) {
			if (body.isFlaggedForDelete()) {
				world.removeBody(body);
				deleteFromGameState(body, state);
			}
		}
		
		List<PhysicsBodyAgent> allAgents = world.getAgents();
		for (PhysicsBodyAgent agent : allAgents) {
			postProcessAgent(agent, state);
		}
		
	}
	
	/**
	 * Finds the Agent which corresponds to the physics body, extracts the next
	 * move from the Agent and updates the velocity of the physics body 
	 * accordingly, along with processing any activated power up.
	 * 
	 * @param body - the body thats velocity should be updated.
	 * @param agents - the list of Agents, one of which corresponds to the body.
	 * @param speed - the magnitude of the velocity that will be applied to 
	 * the body.
	 */
	private void preProcessAgent(PhysicsBodyAgent physicsBody, Maze maze) {
		
		// Grab the actual body
		Body body = physicsBody.getBody();
		
		// Find the agent associated with this body.
		Agent agent = physicsBody.getAgent();
		int speedIndex = agent.getSpeedIndex();
		float speed = speedConverter.getSpeed(speedIndex);
		//float speed = physicsBody.getBaseSpeed();
		
		// Round the position of the body so that we don't lose accuracy due to
		// floating point arithmetic. (This will only change the position if the
		// current position is within a small tolerance of a multiple of half 
		// the square size.)
		roundPosition(body, speed);
		Vector2 bodyWorldPos = body.getPosition();
		
		Move move = agent.getNextMove();
		Vector2 velocity = body.getLinearVelocity();
		
		// Is the proposed move valid? (e.g. trying to move left when there is 
		// a wall there is not valid)
		if (moveValid(move, body, maze)) {
			
			// The proposed move is ok, so update the velocity vector as 
			// appropriate and set it on the body.
			updateVelocity(velocity, move.getDirection(), speed);
			body.setLinearVelocity(velocity);
			
			agent.setCurrentDirection(move.getDirection());
			
		} else {
			// The proposed move isn't ok. Note that we might be coming out of a
			// power up at this point, so the velocity may be incorrect. We 
			// work out the direction from the velocity, then the correct
			// velocity (i.e. of the right magnitude) from the direction.
			Direction currentDir = getDirectionFromVelocity(velocity);
			updateVelocity(velocity, currentDir, speed);
			body.setLinearVelocity(velocity);
		}
		
		// This bit of code prevents agents moving past the square centre when
		// there's a wall ahead. If the agent is at a square centre and the node
		// in the direction of travel is not a neighbouring node, the velocity
		// is set to zero.
		PointXY bodyStatePos = worldToState(bodyWorldPos);
		Vector2 squareWorldPos = stateToWorld(bodyStatePos);
		if (bodyWorldPos.equals(squareWorldPos)) {
			Direction dir = getDirectionFromVelocity(body.getLinearVelocity());
			MazeNode node = maze.getNode(bodyStatePos);

			PointXY targetPos = getTarget(bodyStatePos, dir);
			
			if (!node.isNeighbour(targetPos)) {
				body.setLinearVelocity(new Vector2(0, 0));
				agent.setCurrentDirection(Direction.None);
			}
		}
	}

	/**
	 * If the x or y-coordinate of the position of the provided body is within a
	 * small tolerance of a multiple of half the square size, then the 
	 * coordinate is rounded such that it is an exact multiple of half the 
	 * square size.
	 * 
	 * @param body - the body which should have its position rounded.
	 */
	private void roundPosition(Body body, float speed) {
		{
			float squareSize = world.getSquareSize();
			float halfSquare = squareSize / 2;
			Vector2 bodyWorldPos = body.getPosition();
			float x = bodyWorldPos.x;
			float y = bodyWorldPos.y;
			
			float tol = 0.0001f;
			if (x % halfSquare < tol || (halfSquare - x % halfSquare) < tol) {
				int multiple = Math.round(x / halfSquare);
				x = multiple * halfSquare;
			}
			
			if (y % halfSquare < tol || (halfSquare - y % halfSquare) < tol) {
				int multiple = Math.round(y / halfSquare);
				y = multiple * halfSquare;
			}		
			
			body.setTransform(x, y, 0);
		}
		
//		int squareSize = speedConverter.getSquareSize();
//		float timestep = speedConverter.getTimestep();
//		float interval = speed * timestep;
//		
//		Vector2 bodyWorldPos = body.getPosition();
//		PointXY statePos = worldToState(bodyWorldPos);
//		Vector2 squarePos = stateToWorld(statePos);
//		
//		// Sort out the x position
//		float xdiff = bodyWorldPos.x - squarePos.x;
//		float remainder = xdiff % interval;
//		if (remainder > (interval / 2)) {
//			remainder = remainder - interval;
//		}
//		float x = bodyWorldPos.x - remainder;
//		
//		// Sort out the y position
//		float ydiff = bodyWorldPos.y - squarePos.y;
//		float remainderY = ydiff % interval;
//		if (remainderY > (interval / 2)) {
//			remainderY = remainderY - interval;
//		}
//		float y = bodyWorldPos.y - remainderY;
//		
//		body.setTransform(x,  y, 0);
		
	}
	
	/**
	 * Is the provided move for the provided body valid? 
	 * 
	 * A move is valid if the body is at a square centre and the move direction
	 * is to a square which is a neighbour of this square. A move is also valid 
	 * if the body is currently transitioning in the x direction and the move 
	 * direction is left/right or if the cody is transitioning in the y 
	 * direction and the move direction us up/down.
	 * 
	 * @param move - the proposed move for the provided body.
	 * @param body - the body to which the move is to be applied.
	 * @param maze - the maze in which the body resides.
	 * @return true if the provided move is valid, false otherwise.
	 */
	private boolean moveValid(Move move, Body body, Maze maze) {
		
		Direction moveDir = move.getDirection();
		Direction centreDir = getDirectionToNearestCentre(body, maze);
		if (centreDir == Direction.None) {
			// We're currently at a square centre, so find whether the target
			// square is a neighbour of the current square.
			
			Vector2 bodyWorldPos = body.getPosition();
			PointXY bodyStatePos = worldToState(bodyWorldPos);

			PointXY targetPos = getTarget(bodyStatePos, moveDir);
			
			MazeNode node = maze.getNode(bodyStatePos);
			
			if (node.isNeighbour(targetPos) || bodyStatePos.equals(targetPos)) {
				return true;
			} else {
				return false;
			}
			
		} else {
			// We're transitioning between squares. If the closest square centre
			// is left/right (i.e. we're transitioning to the left or right 
			// node), then check the move direction matches that. And similar
			// for up/down.
			if (centreDir == Direction.Left || centreDir == Direction.Right) {
				if (moveDir == Direction.Left || moveDir == Direction.Right) {
					return true;
				} else {
					return false;
				}
			} else {
				if (moveDir == Direction.Up || moveDir == Direction.Down) {
					return true;
				} else {
					return false;
				}
			}
		}
		
	}

	/**
	 * Find the direction from the provided body to the nearest square centre. 
	 * Return 'None' if the body is already at a square centre.
	 * 
	 * @param body - the body for which to find the direction.
	 * @param maze - the maze in which the body resides.
	 * @return the direction from the provided body to the nearest square 
	 * centre.
	 */
	private Direction getDirectionToNearestCentre(Body body, Maze maze) {
		
		Vector2 bodyWorldPos = body.getPosition();
		PointXY bodyStatePos = worldToState(bodyWorldPos);
		Vector2 squareWorldPos = stateToWorld(bodyStatePos);
		
		if (bodyWorldPos.equals(squareWorldPos)) {
			// The body is at a square centre
			return Direction.None;
		}
		
		float xDiff = bodyWorldPos.x - squareWorldPos.x;
		float yDiff = bodyWorldPos.y - squareWorldPos.y;
		
		if (Math.abs(xDiff) > Math.abs(yDiff)) {
			if (xDiff > 0) {
				return Direction.Left;
			} else {
				return Direction.Right;
			}
		} else {
			if (yDiff > 0) {
				return Direction.Down;
			} else {
				return Direction.Up;
			}
		}
	}
	
	/**
	 * Convert the provided velocity into a direction.
	 * 
	 * @param velocity - the velocity to convert.
	 * @return the velocity converted to a direction.
	 */
	private Direction getDirectionFromVelocity(Vector2 velocity) {
		
		if (velocity.x == 0 && velocity.y == 0) {
			return Direction.None;
		}
		
		if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
			 if (velocity.x > 0) {
				 return Direction.Right;
			 } else {
				 return Direction.Left;
			 }
		} else {
			if (velocity.y > 0) {
				return Direction.Up;
			} else {
				return Direction.Down;
			}
		}
	}
	
	/**
	 * Find the target square position which will be hit if a body moves in the 
	 * provided direction from the provided current position (ignoring all 
	 * walls).
	 * 
	 * @param currentPos - the current position.
	 * @param moveDir - the direction of movement.
	 * @return the target position that will be hit by moving in the provided
	 * direction from the provided current position.
	 */
	private PointXY getTarget(PointXY currentPos, Direction moveDir) {
		
		int targetX = currentPos.getX();
		int targetY = currentPos.getY();
		
		switch (moveDir) {
			case Down:
				--targetY;
				break;
			case Left:
				--targetX;
				break;
			case Right:
				++targetX;
				break;
			case Up:
				++targetY;
				break;
			case None:
				break;
			default:
				break;
		}
		PointXY targetPos = new PointXY(targetX, targetY);
		return targetPos;
	}
	
	private void checkForMagnet(PhysicsBodyAgent physicsBody) {
		
		Agent agent = physicsBody.getAgent();
		
		if (agent.magnetApplied()) {
			
			Magnet magnet = agent.getMagnet();
			int strength = magnet.getStrength();
			PointXY focusState = magnet.getFocalPoint();
			Vector2 focusWorld = stateToWorld(focusState);
			
			int range = 10 + strength;
			int force = -1000 * strength;
			
			Body body = physicsBody.getBody();
			Vector2 worldPos = body.getPosition();
			
			// Check whether this body is within range of the magnet.
			Vector2 dir = worldPos.sub(focusWorld);
			if (dir.len() <= range * getSquareSize()) {
			
				// Normalise the direction vector
				Vector2 normDir = dir.nor();
				
				// Apply the magnet force
				body.setLinearVelocity(0, 0);
				float xForce = normDir.x * force;
				float yForce = normDir.y * force;
				body.applyForceToCenter(xForce, yForce, true);
				
			}
			
		}
		
	}
	
	/**
	 * Activates a power up if the provided Move indicates that one should be. 
	 * If a power up is already activated, any action related to this power up
	 * is carried out.
	 * 
	 * @param agent - the Agent currently being processed.
	 * @param move - the current Move of the Agent.
	 * @param physicsBody - the body associated with the agent.
	 * @param state - the current game state.
	 */
	private void preProcessAgentPowerUps(PhysicsBodyAgent physicsBody, 
			GameState state) {

		Agent agent = physicsBody.getAgent();
		Move move = agent.getNextMove();
		
		List<Agent> allAgents = state.getAgents();
		
		if (move.getUsePowerUp()) {
			move.setUsePowerUp(false);
			agent.activatePowerUp(allAgents);
			checkForPositionChange();
			jogAgents(state);
		}

		checkForMagnet(physicsBody);
		
		boolean powerUpRemoved = agent.updateActivatedPowerUps(allAgents);
		
		if (powerUpRemoved) {
			jogAgents(state);
		}
	}
	
	private void checkForPositionChange() {
		List<PhysicsBodyAgent> agentBodies = world.getAgents();
		
		for (PhysicsBodyAgent agentBody : agentBodies) {
			Agent agent = agentBody.getAgent();
			PointXY statePos = agent.getPosition();
			
			Body body = agentBody.getBody();
			Vector2 bodyWorldPos = body.getPosition();
			PointXY bodyStatePos = worldToState(bodyWorldPos);
			
			if (!statePos.equals(bodyStatePos)) {
				Vector2 statePosWorld = stateToWorld(statePos);
				body.setTransform(statePosWorld, 0);
			}
		}
	}

	/**
	 * Remove the entity defined by the provided data from the game state.
	 * 
	 * @param data - the data of the entity to be removed from the game state.
	 * @param state - the game state to update.
	 */
	private void deleteFromGameState(PhysicsBody physicsBody, GameState state) {
		PhysicsBodyType type = physicsBody.getType();
		
		switch (type) {
			case Pill:
				PointXY pillPos = getPosition(physicsBody);
				state.removePill(pillPos);
				return;
			
			case Walls:
				System.err.println("Should not remove walls from game.");
				return;
	
			case Prey:
				PhysicsBodyAgent prey = (PhysicsBodyAgent) physicsBody;
				int preyID = prey.getAgent().getID();
				state.removePrey(preyID);
				break;
			
			case Predator:
				PhysicsBodyAgent predator = (PhysicsBodyAgent) physicsBody;
				int predatorID = predator.getAgent().getID();
				state.removePredator(predatorID);
				break;
				
			case PowerUpPrey:
				PhysicsBodyPowerUp preyPowerUp = 
					(PhysicsBodyPowerUp) physicsBody;
				int preyID2 = preyPowerUp.getAgentID();
				PointXY preyPowerUpPos = getPosition(physicsBody);
				state.predatorPowerUpCollected(preyID2, preyPowerUpPos);

			case PowerUpPredator:
				PhysicsBodyPowerUp predPowerUp = 
					(PhysicsBodyPowerUp) physicsBody;
				int predatorID2 = predPowerUp.getAgentID();
				PointXY predPowerUpPos = getPosition(physicsBody);
				state.predatorPowerUpCollected(predatorID2, predPowerUpPos);
				
			default:
				break;
		}
		
	}
	
	private void postProcessAgent(PhysicsBodyAgent agentBody, GameState state) {
		
		int agentID = agentBody.getAgent().getID();
		PointXY pos = getPosition(agentBody);
		
		PhysicsBodyType type = agentBody.getType();
		
		if (type == PhysicsBodyType.Predator) {
			state.updatePredatorPosition(agentID, pos);
		} else if (type == PhysicsBodyType.Prey) {
			state.updatePreyPosition(agentID, pos);
		} else {
			System.err.println("Unexpected agent body type.");
		}
	}
	
	@Override
	public Vector2 stateToWorld(PointXY pos) {
		float squareSize = world.getSquareSize();
		return PhysicsUtils.stateToWorld(pos, squareSize);
	}
	
	@Override
	public PointXY worldToState(Vector2 pos) {
		float squareSize = world.getSquareSize();
		return PhysicsUtils.worldToState(pos, squareSize);
	}
	
	/**
	 * Update the given velocity based on the given Direction and magnitude.
	 * 
	 * @param velocity - the velocity to update.
	 * @param direction - the direction of travel.
	 * @param magnitude - the magnitude of the velocity (i.e. the speed)
	 */
	private void updateVelocity(Vector2 velocity, Direction direction, 
			float magnitude) {
		
		switch (direction) {
			case None:
				velocity.set(0, 0);
				break;
			case Up:
				velocity.set(0, magnitude);
				break;
			case Down:
				velocity.set(0, -magnitude);
				break;
			case Left:
				velocity.set(-magnitude, 0);
				break;
			case Right:
				velocity.set(magnitude, 0);
				break;
			default:
				throw new IllegalArgumentException("Unknown direction enum.");
		}
		
	}
	
	private PointXY getPosition(PhysicsBody physicsBody) {
		Body body = physicsBody.getBody();
		Vector2 worldPos = body.getPosition();
		PointXY statePos = worldToState(worldPos);
		return statePos;
	}
	
	@Override
	public float getSquareSize() {
		return world.getSquareSize();
	}
	
	private void jogAgents(GameState state) {
		Maze maze = state.getMaze();
		
		List<PhysicsBodyAgent> agentBodies = world.getAgents();
		for (PhysicsBodyAgent agentBody : agentBodies) {
			
			Body body = agentBody.getBody();
			Agent agent = agentBody.getAgent();
			MazeNode node = maze.getNode(agent.getPosition());
			float speed = speedConverter.getSpeed(agent.getSpeedIndex()); 
			
			jogBody(body, node, speed);
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
	private void jogBody(Body body, MazeNode node, float speed) {
		
		// Work out some useful position information
		Vector2 bodyPos = body.getPosition();
		PointXY statePos = worldToState(bodyPos);
		Vector2 worldPos = stateToWorld(statePos);
		
		// Grab the neighbouring positions of the current maze square
//		MazeNode node = state.getMaze().getNode(statePos);
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
			
			Vector2 neighbourWorldPos = stateToWorld(neighbour);
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
		//PhysicsData data = (PhysicsData) body.getUserData();
//		float speed = physProc.getBodySpeed(data.getType()) * speedFactor;
		float simStep = speedConverter.getTimestep();
		float step = speed * simStep;
		if (step == 0) {
			return;
		}
		
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
//		Vector2 vel = getVelocity(body.getPosition(), worldPos, speed);
//		body.setLinearVelocity(vel);
	}
	
	// *************************************************************************
	// ***** The following methods are used for debugging purposes only. *******
	// *************************************************************************
	
	@Override
	public void setDebugCategory(PhysicsDebugType type) {
		this.debugType = type;
	}
	

	private void preProcessDebugInfo(GameState state) {
		
		if (debugType != PhysicsDebugType.DebugNone && !debugBodiesCreated) {
			world.createDebugBodies(state);
			debugBodiesCreated = true;
		}
		
		switch (debugType) {
			case DebugNone:
				break;
			case DebugPartition:
				processPartition(state);
				break;
			case DebugSaferPositions:
				processSaferPositions(state);
				break;
			default:
				break;
		}
	}
	
	private void processPartition(GameState state) {
		
		Map<Agent, Set<PointXY>> partition = state.getPartition();
		
		Set<Agent> agents = partition.keySet();
		
		for (Agent agent : agents) {
			
			Set<PointXY> agentPoints = partition.get(agent);
			
			for (PointXY pos : agentPoints) {
				PhysicsBodyDebug body = findDebugBody(pos);
				body.setAgentID(agent.getID());
			}
			
		}
	}
	
	private void processSaferPositions(GameState state) {
		
		Map<Agent, Set<PointXY>> saferPositions = state.getSaferPositions();
		if (saferPositions == null) {
			return;
		}
		
		Set<Agent> agents = saferPositions.keySet();
		
		Set<PointXY> positionsProcessed = new HashSet<PointXY>();
		
		for (Agent agent : agents) {
			
			Agent check = state.getAgent(agent.getID());
			if (check == null) {
				continue;
			}
			
			Set<PointXY> agentPoints = saferPositions.get(agent);
			
			for (PointXY pos : agentPoints) {
				PhysicsBodyDebug body = findDebugBody(pos);
				body.setAgentID(agent.getID());
				positionsProcessed.add(pos);
			}
			
		}
		
		Set<PointXY> allPositions = state.getMaze().getNodes().keySet();
		
		for (PointXY pos : allPositions) {
			if (!positionsProcessed.contains(pos)) {
				PhysicsBodyDebug body = findDebugBody(pos);
				body.setAgentID(-1);			
			}
		}
		
	}
	
	private PhysicsBodyDebug findDebugBody(PointXY pos) {
		
		Vector2 worldPos = stateToWorld(pos);
		
		List<PhysicsBodyDebug> bodies = world.getDebugBodies();
		
		for (PhysicsBodyDebug physicsBody : bodies) {
			Body body = physicsBody.getBody();
			Vector2 bodyPos = body.getPosition();
			if (bodyPos.equals(worldPos)) {
				return physicsBody;
			}
		}
		
		throw new IllegalArgumentException(
				"No debug body at position " + pos.toString());
	}
	
}