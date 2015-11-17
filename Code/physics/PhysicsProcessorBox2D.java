package physics;

import geometry.PointXY;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import logic.Agent;
import logic.GameState;
import logic.Maze;
import logic.MazeNode;
import logic.Move;
import logic.Direction;
import logic.Predator;
import logic.Prey;
import logic.powerup.PowerUp;
import logic.powerup.PredatorPowerUp;
import logic.powerup.PreyPowerUp;

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
 * @version 2015-10-18
 */
public class PhysicsProcessorBox2D implements PhysicsProcessor {
	
	// The Box2D world.
	private World world;
	
	// The class used to handle the game's power ups, including changing the 
	// physics properties of the world as appropriate.
	private PowerUpProcessor powerUpProc;
	
	// Physics world geometry and kinematics.
	private float squareSize;
	private float wallWidth;
	private float pillRadius;
	private float powerUpRadius;
	private float predatorSpeed;
	private float preySpeed;
	
	// Defines the different physics body categories. These are used for 
	// collision filtering.
	private final short CATEGORY_WALL = 0x0001;
	private final short CATEGORY_PREDATOR = 0x0002;
	private final short CATEGORY_PREY = 0x0004;
	private final short CATEGORY_PILL = 0x0008;
	private final short CATEGORY_POWERUP_PREDATOR = 0x0016;
	private final short CATEGORY_POWERUP_PREY = 0x0032;
	private final short CATEGORY_DEBUG = 0x0064;
	
	// Masks are used in collision filtering. Define which physics bodies 
	// collide. E.g. Predators will collide with walls and prey, but not other 
	// predators or pills.
	private final short MASK_PREDATOR = CATEGORY_WALL | CATEGORY_PREY | 
			CATEGORY_POWERUP_PREDATOR;
	private final short MASK_PREY = CATEGORY_WALL | CATEGORY_PILL | 
			CATEGORY_PREDATOR | CATEGORY_POWERUP_PREY;
	private final short MASK_PILL = CATEGORY_WALL | CATEGORY_PREY;
	private final short MASK_POWERUP_PREDATOR = CATEGORY_WALL | 
			CATEGORY_PREDATOR;
	private final short MASK_POWERUP_PREY = CATEGORY_WALL | CATEGORY_PREY;
	private final short MASK_DEBUG = CATEGORY_WALL;
	
	// This determines how much of a square from each of its borders is
	// considered as a 'transition zone'. If the centre of an agent is in this
	// zone it is flagged as 'inTransition' (i.e. moving between maze squares).
	private float transZone = 0.4f;
	
	// Physics simulation step variables
	private final float dt = 1.0f / 60.0f;
	private float accumulator;
	
	/**
	 * Constructor for PhysicsProcessorBox2D.
	 * 
	 * @param world - the Box2D world to be used in the physics simulation.
	 * @param initialState - the initial snapshot of the game data. Should 
	 * include the fully formed Maze and all playing agents.
	 * @param config - the configuration data used to set up the Physics
	 * simulation.
	 */
	public PhysicsProcessorBox2D(World world, GameState initialState, 
		PhysicsConfiguration config) {
		
		this.world = world;
		
		this.squareSize = config.getSquareSize();
		this.wallWidth = (config.getWallWidthRatio() / 2) * squareSize;
		this.pillRadius = 
				(squareSize / 2 - wallWidth) * config.getPillRadiusRatio();
		this.powerUpRadius = 
				(squareSize / 2 - wallWidth) * config.getPowerUpRadiusRatio();
		
		this.predatorSpeed = config.getPredatorSpeed();
		this.preySpeed = config.getPreySpeed();
		
		buildPhysics(initialState);
		
		PhysicsContact contact = new PhysicsContact();
		this.world.setContactListener(contact);
		
		this.powerUpProc = new PowerUpProcessor(world, this);
		
		this.accumulator = 0;
	}
	
	/**
	 * Use the provided GameState snapshot to create all the Physics bodies and
	 * fixtures (Walls, pills, predators, prey).
	 * 
	 * @param state - the initial snapshot of the game data containing a fully 
	 * formed maze and all Agents.
	 */
	private void buildPhysics(GameState state) {
		
		// Create each maze square physics body (walls) and pill physics body 
		// from the corresponding maze node.
		Maze maze = state.getMaze();
		Map<PointXY, MazeNode> nodes = maze.getNodes();
		Set<PointXY> keys = nodes.keySet();
		
		for (PointXY pos : keys) {
			MazeNode node = nodes.get(pos);
			createNode(pos, node, maze);
			if (state.hasPill(pos)) {
				createPill(pos, node);
			}
		}
		
		// Create each Predator and Prey physics body.
		List<Predator> predators = state.getPredators();
		List<Prey> prey = state.getPrey();
		
		for (Predator pred : predators) {
			createPredator(pred);
		}
		
		for (Prey p : prey) {
			createPrey(p);
		}
		
		Map<PointXY, PredatorPowerUp> predatorPowerUps = 
				state.getPredatorPowerUps();
		for (PointXY pos : predatorPowerUps.keySet()) {
			createPredatorPowerUp(predatorPowerUps.get(pos), pos);
		}

		Map<PointXY, PreyPowerUp> preyPowerUps = state.getPreyPowerUps();
		for (PointXY pos : preyPowerUps.keySet()) {
			createPreyPowerUp(preyPowerUps.get(pos), pos);
		}
		
		// Create the debugging partitioned maze info
		createPartition(keys);
	}
	
	/**
	 * Creates the physics body for the walls from the given MazeNode and its 
	 * position in the maze.
	 * 
	 * @param pos - the position of the MazeNode in the maze.
	 * @param node - the MazeNode from which to create the walls.
	 * @param maze - the maze that is being built. This is used to check whether
	 * the node is a perimeter node and therefore draw the boundary wall in the 
	 * right position.
	 */
	private void createNode(PointXY pos, MazeNode node, Maze maze) {
		
		// Create the node body and add it to the world at the given location.
		Body nodeBody;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		
		Vector2 worldPos = stateToWorld(pos);
		bodyDef.position.set(worldPos);
		
		nodeBody = world.createBody(bodyDef);
		
		// Add the user data which tells us this body is of type Walls.
		PhysicsData data = new PhysicsData(PhysicsBodyType.Walls);
		nodeBody.setUserData(data);
		
		// Check the node positions to the four sides of the current node.
		// If the node does not have a neighbouring node in that position, 
		// add a wall fixture to the nodeBody.
		// A wall is also added to the 'outside' if the node is a perimeter 
		// node.
		PointXY northPos = new PointXY(pos.getX(), pos.getY() + 1);
		float northCentreX = 0;
		float northCentreY = (squareSize/2 - wallWidth/2);
		if (!node.isNeighbour(northPos)) {
			Vector2 centre = new Vector2(northCentreX, northCentreY);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			createWall(nodeBody, centre, hx, hy);
		}
		if (!maze.containsNodeAtPosition(northPos)) {
			Vector2 centre = new Vector2(northCentreX, northCentreY + wallWidth);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			createWall(nodeBody, centre, hx, hy);
		}
		
		PointXY eastPos = new PointXY(pos.getX() + 1, pos.getY());
		float eastCentreX = (squareSize/2 - wallWidth/2);
		float eastCentreY = 0;
		if (!node.isNeighbour(eastPos)) {
			Vector2 centre = new Vector2(eastCentreX, eastCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			createWall(nodeBody, centre, hx, hy);
		}
		if (!maze.containsNodeAtPosition(eastPos)) {
			Vector2 centre = new Vector2(eastCentreX + wallWidth, eastCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			createWall(nodeBody, centre, hx, hy);
		}
		
		PointXY southPos = new PointXY(pos.getX(), pos.getY() - 1);
		float southCentreX = 0;
		float southCentreY = (-squareSize/2 + wallWidth/2);
		if (!node.isNeighbour(southPos)) {
			Vector2 centre = new Vector2(southCentreX, southCentreY);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			createWall(nodeBody, centre, hx, hy);
		}
		if (!maze.containsNodeAtPosition(southPos)) {
			Vector2 centre = new Vector2(southCentreX, southCentreY - wallWidth);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			createWall(nodeBody, centre, hx, hy);
		}
		
		PointXY westPos = new PointXY(pos.getX() - 1, pos.getY());
		float westCentreX = (-squareSize/2 + wallWidth/2);
		float westCentreY = 0;
		if (!node.isNeighbour(westPos)) {
			Vector2 centre = new Vector2(westCentreX, westCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			createWall(nodeBody, centre, hx, hy);
		}
		if (!maze.containsNodeAtPosition(westPos)) {
			Vector2 centre = new Vector2(westCentreX - wallWidth, westCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			createWall(nodeBody, centre, hx, hy);
		}
		
	}
	
	/**
	 * Create a wall fixture at the given location (in the mazeBody's coordinate
	 * system) and add the fixture to the mazeBody.
	 * 
	 * @param mazeBody - the body to which to add the wall fixture.
	 * @param centre - the centre position of the wall in the mazeBody's 
	 * coordinate system.
	 * @param hx - the half-length of the wall in the x direction.
	 * @param hy - the half-length of the wall in the y direction.
	 */
	private void createWall(Body mazeBody, Vector2 centre, float hx, float hy) {
		FixtureDef fixtureDef = new FixtureDef();
	
		PolygonShape rect = new PolygonShape();
		rect.setAsBox(hx, hy, centre, 0);
		
		fixtureDef.shape = rect;
		
		mazeBody.createFixture(fixtureDef);
	}
	
	/**
	 * Creates the physics body for the pill from the given MazeNode and its 
	 * position in the maze.
	 * 
	 * @param pos - the position of the MazeNode in the maze.
	 * @param node - the MazeNode from which to create the pill.
	 */
	private void createPill(PointXY pos, MazeNode node) {
		
		Body pillBody;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		
		Vector2 worldPos = stateToWorld(pos);
		bodyDef.position.set(worldPos);
		
		pillBody = world.createBody(bodyDef);
		
		FixtureDef fixturePill = new FixtureDef();
		CircleShape circle = new CircleShape();
		circle.setRadius(pillRadius);
		fixturePill.shape = circle;
		fixturePill.filter.categoryBits = CATEGORY_PILL;
		fixturePill.filter.maskBits = MASK_PILL;
		
		pillBody.createFixture(fixturePill);
		
		PhysicsData data = new PhysicsDataPill(PhysicsBodyType.Pill, pos);
		pillBody.setUserData(data);
	}

	/**
	 * Creates the physics body for the Predator.
	 * 
	 * @param p - the Predator from which to create the Predator physics body.
	 */
	private void createPredator(Predator p) {
		createAgent(p, CATEGORY_PREDATOR, MASK_PREDATOR, 
				PhysicsBodyType.Predator);
	}
	
	/**
	 * Creates the physics body for the Prey.
	 * 
	 * @param p - the Prey from which to create the Prey physics body.
	 */
	private void createPrey(Prey p) {
		createAgent(p, CATEGORY_PREY, MASK_PREY, PhysicsBodyType.Prey);
	}
	
	/**
	 * Create an Agent physics body of the given physics body type and with the 
	 * given collision filtering information.
	 * 
	 * @param agent - the Agent from which to create the physics body.
	 * @param categoryBits - the category to use for this physics body (used for 
	 * collision filtering).
	 * @param maskBits - the mask to use for this physics body. Indicates with 
	 * which other bodies this body will collide.
	 * @param bodyType - the Agent's physics body type identifier.
	 */
	private void createAgent(Agent agent, short categoryBits, short maskBits, 
			PhysicsBodyType bodyType) {
		
		Body agentBody;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		
		Vector2 worldPos = stateToWorld(agent.getPosition());
		bodyDef.position.set(worldPos);
		
		agentBody = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		
		CircleShape circle = new CircleShape();
		float radius = (squareSize - wallWidth*2) / 2 * 0.95f;
		circle.setRadius(radius);
		
//		PolygonShape square = new PolygonShape();
//		square.setAsBox(radius, radius);
		
		fixtureDef.shape = circle;
//		fixtureDef.shape = square;
		
		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;
		
		agentBody.createFixture(fixtureDef);
		
		PhysicsData data = new PhysicsDataAgent(bodyType, agent.getID());
		agentBody.setUserData(data);
	}
	
	/**
	 * Creates the physics body for a Power Up used by Predators.
	 * 
	 * @param powerUp - the Predator Power Up from which to create the power up 
	 * physics body.
	 * @param pos - the position of the power up in the maze.
	 */
	private void createPredatorPowerUp(PredatorPowerUp powerUp, PointXY pos) {
		createPowerUp(powerUp, pos, CATEGORY_POWERUP_PREDATOR, 
				MASK_POWERUP_PREDATOR, PhysicsBodyType.PowerUpPredator);
	}

	/**
	 * Creates the physics body for a Power Up used by Prey.
	 * 
	 * @param powerUp - the Prey Power Up from which to create the power up 
	 * physics body.
	 * @param pos - the position of the power up in the maze.
	 */
	private void createPreyPowerUp(PreyPowerUp powerUp, PointXY pos) {
		createPowerUp(powerUp, pos, CATEGORY_POWERUP_PREY, 
				MASK_POWERUP_PREY, PhysicsBodyType.PowerUpPrey);
	}

	/**
	 * Creates the physics body for the power up from the given PowerUp and its 
	 * position in the maze.
	 * 
	 * @param powerUp - the Power Up from which to create the power up body.
	 * @param pos - the position of the power up in the maze.
	 * @param categoryBits - the category to use for this physics body (used for 
	 * collision filtering).
	 * @param maskBits - the mask to use for this physics body. Indicates with 
	 * which other bodies this body will collide.
	 * @param bodyType - the Power Up's physics body type identifier.
	 */
	private void createPowerUp(PowerUp powerUp, PointXY pos, short categoryBits, 
			short maskBits, PhysicsBodyType bodyType) {

		Body powerUpBody;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		Vector2 worldPos = stateToWorld(pos);
		bodyDef.position.set(worldPos);

		powerUpBody = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circle = new CircleShape();
		circle.setRadius(powerUpRadius);
		fixtureDef.shape = circle;

		fixtureDef.filter.categoryBits = categoryBits;
		fixtureDef.filter.maskBits = maskBits;
		
		powerUpBody.createFixture(fixtureDef);

		String powerUpName = powerUp.getName();
		
		PhysicsData data = new PhysicsDataPowerUp(bodyType, pos, powerUpName);
		powerUpBody.setUserData(data);
	}
	
	/**
	 * Carry out any work that needs to be done immediately BEFORE the 
	 * simulation is stepped. This includes extracting the game state data and
	 * applying it to the world (e.g. each Agent's next move).
	 * 
	 * @param state - a snapshot of the current game data.
	 */
	private void preStep(GameState state) {

		List<Predator> predators = state.getPredators();
		List<Prey> prey = state.getPrey();
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			preStepProcess(b, predators, prey, state.getMaze());
		}
		
		// The power ups are processed after each of the agent's moves have 
		// been applied in the pre-step processing.
		for (Body b : bodies) {
			processPowerUps(b, state);
		}
		
		processPartition(state);
//		processSaferPositions(state);
	}

	/**
	 * Carry out any work that needs to be done immediately AFTER the simulation 
	 * is stepped. This involves updating the game state with the 
	 * post-simulation data (e.g. new positions of each Agent).
	 * 
	 * @param state - a snapshot of the current game data.
	 */
	private void postStep(GameState state) {
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		// Remove 'dead' bodies from the game and inform the GameState of the 
		// changes.
		for (Body b : bodies) {
			postStepProcess(b, state);
		}
		
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
			world.step(dt, 8, 3);			
			postStep(state);
			
			accumulator -= dt;
			++numSimSteps;
		}
		
		return numSimSteps;
	}
	
	/**
	 * Carries out actions that should be done before the simulation step.
	 * 
	 * More specifically, updates the velocity of each body which represents 
	 * a Predator or Prey based on the next move of the equivalent Predator or
	 * Prey object. 
	 * 
	 * @param body - the body to process.
	 * @param predators - the list of predators for the current game state.
	 * @param prey - the list of prey for the current game state.
	 */
	private void preStepProcess(Body body, List<Predator> predators, 
			List<Prey> prey, Maze maze) {
		
		PhysicsData data = (PhysicsData) body.getUserData();
		PhysicsBodyType type = data.getType();
		
		switch (type) {
			case PowerUpPredator:
			case PowerUpPrey:
			case Pill:
			case Walls:
				return;
		
			case Prey:
				processAgent(body, prey, preySpeed, maze);
				break;
				
			case Predator:
				processAgent(body, predators, predatorSpeed, maze);
				break;
			
			default:
				break;
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
	private void processAgent(Body body, List<? extends Agent> agents, 
			float speed, Maze maze) {
		
		Agent agent = findAgent(body, agents);
		
		if (agent != null) {
			PhysicsDataAgent data = (PhysicsDataAgent) body.getUserData();
			
			Vector2 bodyWorldPos = body.getPosition();
			long xd = Math.round(bodyWorldPos.x);
			long yd = Math.round(bodyWorldPos.y);
			if (Math.abs(bodyWorldPos.x - xd) < 0.0001 &&
				Math.abs(bodyWorldPos.y - yd) < 0.0001) {
				body.setTransform((float) xd, (float) yd, 0);
				bodyWorldPos = body.getPosition();
			}
			
			Move move = agent.getNextMove();
			Vector2 velocity = body.getLinearVelocity();
			
			if (moveValid(move, body, maze)) {
				
				updateVelocity(velocity, move, speed);
				body.setLinearVelocity(velocity);
				
				if (data.getCurrentMove() != Direction.None) {
					data.setPreviousMove(data.getCurrentMove());
				}
				data.setCurrentMove(move.getDirection());
			}
			
			PointXY bodyStatePos = worldToState(bodyWorldPos);
			Vector2 squareWorldPos = stateToWorld(bodyStatePos);
			if (bodyWorldPos.equals(squareWorldPos)) {
				Direction dir = getDirectionFromVelocity(body.getLinearVelocity());
				MazeNode node = maze.getNode(bodyStatePos);
				
				int targetX = bodyStatePos.getX();
				int targetY = bodyStatePos.getY();
				
				switch (dir) {
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
				if (!node.isNeighbour(targetPos)) {
					body.setLinearVelocity(new Vector2(0, 0));
				}
			}

		}
	}
	
	/**
	 * Find the Agent which in the provided list of Agents which is equivalent 
	 * to the provided body. If there is no match, this method returns null.
	 * 
	 * @param body - the body for which to find the matching agent.
	 * @param agents - the list of agents to search through.
	 * @return the equivalent agent to the body in agents, or null if there is 
	 * no match.
	 */
	private Agent findAgent(Body body, List<? extends Agent> agents) {
		
		PhysicsDataAgent data = (PhysicsDataAgent) body.getUserData();
		int bodyID = data.getID();
		
		for (Agent agent : agents) {
			if (agent.getID() == bodyID) {
				return agent;
			}
		}
		
		return null;
	}
	
	private boolean moveValid(Move move, Body body, Maze maze) {
		
		Direction moveDir = move.getDirection();
		Direction dir = getDirectionToNearestCentre(body, maze);
		if (dir == Direction.None) {
			
			Vector2 bodyWorldPos = body.getPosition();
			PointXY bodyStatePos = worldToState(bodyWorldPos);
			MazeNode node = maze.getNode(bodyStatePos);
			
			int targetX = bodyStatePos.getX();
			int targetY = bodyStatePos.getY();
			
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
			if (node.isNeighbour(targetPos)) {
				return true;
			} else {
				return false;
			}
			
		} else {
			if (dir == Direction.Left || dir == Direction.Right) {
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
	
//	private boolean atSquareCentre(Vector2 pos) {
//		
//		PointXY statePos = worldToState(pos);
//		Vector2 worldPos = stateToWorld(statePos);
//		
//		return pos.equals(worldPos);
//	}
	
	/**
	 * Find the agent equivalent to the provided body if there is one. If there 
	 * is then, based on the agent's move, either activate a power up, apply the
	 * currently activated power up's action, or do nothing.
	 * 
	 * @param body - the body to process.
	 * @param state - the current game state.
	 */
	private void processPowerUps(Body body, GameState state) {
		
		PhysicsData data = (PhysicsData) body.getUserData();
		Agent agent = null;
		if (data.getType() == PhysicsBodyType.Predator) {
			agent = findAgent(body, state.getPredators());
		} else if (data.getType() == PhysicsBodyType.Prey) {
			agent = findAgent(body, state.getPrey());
		}
		
		if (agent != null) {
			Move move = agent.getNextMove();
			processAgentPowerUps(agent, move, body, state);
		}
	}
	
	/**
	 * Activates a power up if the provided Move indicates that one should be. 
	 * If a power up is already activated, any action related to this power up
	 * is carried out.
	 * 
	 * @param agent - the Agent currently being processed.
	 * @param move - the current Move of the Agent.
	 * @param body - the body associated with the agent.
	 * @param state - the current game state.
	 */
	private void processAgentPowerUps(Agent agent, Move move, Body body, 
			GameState state) {

		if (move.getUsePowerUp()) {
			agent.activatePowerUp();
		}

		if (agent.hasActivatedPowerUp()) {
			powerUpProc.setState(state);
			List<? extends PowerUp> powerUps = agent.getActivatedPowerUps();
			for (PowerUp powerUp : powerUps) {
				powerUpProc.setBody(body);
				powerUp.accept(powerUpProc);
			}
		}
		agent.updateActivatedPowerUps();
	}
	
	/**
	 * Carries out actions that should be done after the simulation step.
	 * 
	 * More specifically, remove any physics bodies from the world which have
	 * been flagged for deletion. Then remove the equivalent Agent for each 
	 * deleted body from the GameState.
	 * 
	 * If a predator or prey body has not been flagged for deletion, update its 
	 * position in the game state.
	 * 
	 * @param body - the body to process.
	 * @param state - the game state to update.
	 */
	private void postStepProcess(Body body, GameState state) {
		
		PhysicsData data = (PhysicsData) body.getUserData();
		if (data.isFlaggedForDelete()) {
			// Remove body from world.
			world.destroyBody(body);
			body.setUserData(null);
			body = null;
			
			// Inform GameState.
			deleteFromGameState(data, state);
		} else {
			updateGameState(body, state);
		}
		
	}
	
	/**
	 * Remove the entity defined by the provided data from the game state.
	 * 
	 * @param data - the data of the entity to be removed from the game state.
	 * @param state - the game state to update.
	 */
	private void deleteFromGameState(PhysicsData data, GameState state) {
		PhysicsBodyType type = data.getType();
		
		switch (type) {
			case Pill:
				PhysicsDataPill pillData = (PhysicsDataPill) data;
				PointXY pos = pillData.getPosition();
				state.removePill(pos);
				return;
			
			case Walls:
				return;
	
			case Prey:
				PhysicsDataAgent preyData = (PhysicsDataAgent) data;
				int preyID = preyData.getID();
				state.removePrey(preyID);
				break;
			
			case Predator:
				PhysicsDataAgent predatorData = (PhysicsDataAgent) data;
				int predatorID = predatorData.getID();
				state.removePredator(predatorID);
				break;
				
			case PowerUpPrey:
				PhysicsDataPowerUp preyPowerUpData = (PhysicsDataPowerUp) data;
				PointXY preyPowerUpPos = preyPowerUpData.getPosition();
				int preyID2 = preyPowerUpData.getAgentID();
				state.predatorPowerUpCollected(preyID2, preyPowerUpPos);

			case PowerUpPredator:
				PhysicsDataPowerUp predPowerUpData = (PhysicsDataPowerUp) data;
				PointXY predPowerUpPos = predPowerUpData.getPosition();
				int predatorID2 = predPowerUpData.getAgentID();
				state.predatorPowerUpCollected(predatorID2, predPowerUpPos);
				
			default:
				break;
		}
		
	}
	
	/**
	 * Updates the position of the entity in the game state that is equivalent
	 * to the provided body.
	 * 
	 * @param body - the body to process.
	 * @param state - the game state to update.
	 */
	private void updateGameState(Body body, GameState state) {
		
		PhysicsData data = (PhysicsData) body.getUserData();
		PhysicsBodyType type = data.getType();
		
		switch (type) {
			case PowerUpPredator:
			case PowerUpPrey:
			case Pill:
			case Walls:
				return;
	
			case Prey:
				PhysicsDataAgent preyData = (PhysicsDataAgent) data;
				int preyID = preyData.getID();
				Vector2 preyPos = body.getPosition();
				boolean preyInTransition = checkForTransition(preyPos);
				
				state.updatePreyPosition(preyID, worldToState(preyPos), 
						preyInTransition);
				break;
			
			case Predator:
				PhysicsDataAgent predatorData = (PhysicsDataAgent) data;
				int predatorID = predatorData.getID();
				Vector2 predPos = body.getPosition();				
				boolean predInTransition = checkForTransition(predPos);

				state.updatePredatorPosition(predatorID, worldToState(predPos), 
						predInTransition);
				break;
		
			default:
				break;
		}
	}
	
	/**
	 * Check whether the physics world position is in the 'transition zone' 
	 * part of a maze square. This zone is at the edges of the square and 
	 * can be used to indicate that an Agent is moving from being fully in one
	 * maze square to fully in another square.
	 * 
	 * @param position - the physics world position to check.
	 * @return true if the position is in a transition zone, false otherwise.
	 */
	private boolean checkForTransition(Vector2 position) {

		
		float XPosFactor = (position.x % squareSize) / squareSize;
		float YPosFactor = (position.y % squareSize) / squareSize;
		
		boolean inTransition = false;
		if (XPosFactor < transZone || XPosFactor > (1 - transZone)) {
			inTransition = true;
		} else if (YPosFactor < transZone || YPosFactor > (1 - transZone)) {
			inTransition = true;
		}
		
		return inTransition;
	}
	
	@Override
	public Vector2 stateToWorld(PointXY pos) {
		// Adding 0.5 offsets us to the centre of the square.
		float centreX = (float) ((pos.getX() + 0.5) * squareSize); 
		float centreY = (float) ((pos.getY() + 0.5) * squareSize);
		return new Vector2(centreX, centreY);
	}
	
	@Override
	public PointXY worldToState(Vector2 pos) {
		// Do the inverse of the stateToWorld calculation.
		int centreX = (int) Math.round((pos.x / squareSize) - 0.5);
		int centreY = (int) Math.round((pos.y / squareSize) - 0.5);
		return new PointXY(centreX, centreY);
	}
	
	/**
	 * Update the given velocity based on the given Move and magnitude.
	 * 
	 * @param velocity - the velocity to update.
	 * @param move - the move which gives the direction of travel.
	 * @param magnitude - the magnitude of the velocity (i.e. the speed)
	 */
	private void updateVelocity(Vector2 velocity, Move move, float magnitude) {
		
		Direction direction = move.getDirection();
		
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
	
//	private void updatePosition(Body body, Move move, int speedFactor) {
//		
//		int minHalfSquareDivision = 8;
//		float moveMagnitude = (squareSize /2) / (minHalfSquareDivision + speedFactor);
//		
//		Vector2 position = body.getPosition();
//		
//		Direction direction = move.getDirection();
//		switch (direction) {
//			case None:
//				// do nothing
//				break;
//			case Up:
//				position.y = position.y + moveMagnitude;
//				break;
//			case Down:
//				position.y = position.y - moveMagnitude;
//				break;
//			case Left:
//				position.x = position.x - moveMagnitude;
//				break;
//			case Right:
//				position.x = position.x + moveMagnitude;
//				break;
//			default:
//				throw new IllegalArgumentException("Unknown direction enum.");
//		}
//		
//		body.setTransform(position, 0);
//		
//	}
	
	@Override
	public float getSquareSize() {
		return squareSize;
	}

	@Override
	public float getBodySpeed(PhysicsBodyType type) {
		
		switch (type) {
			case Predator:
				return predatorSpeed;
			case Prey:
				return preySpeed;
			case Pill:
			case PowerUpPredator:
			case PowerUpPrey:
			case Walls:
			default:
				return 0;
		}
		
	}
	
	private void createPartition(Set<PointXY> mazeNodes) {
		
		for (PointXY pos : mazeNodes) {
			// Create the node body and add it to the world at the given location.
			Body debugBody;
			
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			
			Vector2 worldPos = stateToWorld(pos);
			bodyDef.position.set(worldPos);
			
			debugBody = world.createBody(bodyDef);
			
			// Add the user data which tells us this body is of type Walls.
			PhysicsData data = new PhysicsDataDebug(PhysicsBodyType.Debug);
			debugBody.setUserData(data);
			
			FixtureDef fixtureDef = new FixtureDef();
			
			PolygonShape rect = new PolygonShape();
			float hx = squareSize * 0.1f;
			float hy = hx;
			rect.setAsBox(hx, hy, new Vector2(0, 0), 0);
			
			fixtureDef.shape = rect;
			fixtureDef.filter.categoryBits = CATEGORY_DEBUG;
			fixtureDef.filter.maskBits = MASK_DEBUG;
			
			debugBody.createFixture(fixtureDef);
		}
		
	}
	
	private void processPartition(GameState state) {
		
		Map<Agent, Set<PointXY>> partition = state.getPartition();
		
		Set<Agent> agents = partition.keySet();
		int numAgents = agents.size();
		
		for (Agent agent : agents) {
			
			Set<PointXY> agentPoints = partition.get(agent);
			
			for (PointXY pos : agentPoints) {
				Body body = findPartitionBody(pos);
				PhysicsDataDebug data = (PhysicsDataDebug) body.getUserData();
				data.setAgentID(agent.getID());
				data.setNumAgents(numAgents);
			}
			
		}
	}
	
	private Body findPartitionBody(PointXY pos) {
		
		Vector2 worldPos = stateToWorld(pos);
		
		Body body = null;
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			PhysicsData data = (PhysicsData) b.getUserData();
			if (data.getType() == PhysicsBodyType.Debug) {
				Vector2 bodyPos = b.getPosition();
				if (bodyPos.equals(worldPos)) {
					return b;
				}
			}
		}
		
		return body;
	}
	
//	private void processSaferPositions(GameState state) {
//		
//		Map<Agent, Set<PointXY>> saferPositions = state.getSaferPositions();
//		if (saferPositions == null) {
//			return;
//		}
//		
//		Set<Agent> agents = saferPositions.keySet();
//		int numAgents = agents.size();
//		
//		Set<PointXY> positionsProcessed = new HashSet<PointXY>();
//		
//		for (Agent agent : agents) {
//			
//			Agent check = state.getAgent(agent.getID());
//			if (check == null) {
//				continue;
//			}
//			
//			Set<PointXY> agentPoints = saferPositions.get(agent);
//			
//			for (PointXY pos : agentPoints) {
//				Body body = findPartitionBody(pos);
//				PhysicsDataDebug data = (PhysicsDataDebug) body.getUserData();
//				data.setAgentID(agent.getID());
//				data.setNumAgents(numAgents);
//				positionsProcessed.add(pos);
//			}
//			
//		}
//		
//		Set<PointXY> allPositions = state.getMaze().getNodes().keySet();
//		
//		for (PointXY pos : allPositions) {
//			if (!positionsProcessed.contains(pos)) {
//				Body body = findPartitionBody(pos);
//				PhysicsDataDebug data = (PhysicsDataDebug) body.getUserData();
//				data.setAgentID(-1);
//				data.setNumAgents(numAgents);				
//			}
//		}
//		
//	}
}
