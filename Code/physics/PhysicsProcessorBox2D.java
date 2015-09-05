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
import logic.PowerUp;
import logic.Predator;
import logic.PredatorPowerUp;
import logic.Prey;
import logic.PreyPowerUp;

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
 * @version 2015-08-15
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
	
	// This determines how much of a square from each of its borders is
	// considered as a 'transition zone'. If the centre of an agent is in this
	// zone it is flagged as 'inTransition' (i.e. moving between maze squares).
	private float transZone = 0.4f;
	
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
		
		this.powerUpProc = new PowerUpProcessor(world, squareSize);
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
		Maze m = state.getMaze();
		Map<PointXY, MazeNode> nodes = m.getNodes();
		Set<PointXY> keys = nodes.keySet();
		
		for (PointXY pos : keys) {
			MazeNode node = nodes.get(pos);
			createNode(pos, node);
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
	}
	
	/**
	 * Creates the physics body for the walls from the given MazeNode and its 
	 * position in the maze.
	 * 
	 * @param pos - the position of the MazeNode in the maze.
	 * @param node - the MazeNode from which to create the walls.
	 */
	private void createNode(PointXY pos, MazeNode node) {
		
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
		PointXY northPos = new PointXY(pos.getX(), pos.getY() + 1);
		if (!node.isNeighbour(northPos)) {
			Vector2 centre = new Vector2(0, (squareSize/2 - wallWidth/2));
			float hx = squareSize/2;
			float hy = wallWidth/2;
			createWall(nodeBody, centre, hx, hy);
		}
		
		PointXY eastPos = new PointXY(pos.getX() + 1, pos.getY());
		if (!node.isNeighbour(eastPos)) {
			Vector2 centre = new Vector2((squareSize/2 - wallWidth/2), 0);
			float hx = wallWidth/2;
			float hy = squareSize/2;
			createWall(nodeBody, centre, hx, hy);
		}
		
		PointXY southPos = new PointXY(pos.getX(), pos.getY() - 1);
		if (!node.isNeighbour(southPos)) {
			Vector2 centre = new Vector2(0, (-squareSize/2 + wallWidth/2));
			float hx = squareSize/2;
			float hy = wallWidth/2;
			createWall(nodeBody, centre, hx, hy);
		}
		
		PointXY westPos = new PointXY(pos.getX() - 1, pos.getY());
		if (!node.isNeighbour(westPos)) {
			Vector2 centre = new Vector2((-squareSize/2 + wallWidth/2), 0);
			float hx = wallWidth/2;
			float hy = squareSize/2;
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

		PhysicsData data = new PhysicsDataPowerUp(bodyType, pos);
		powerUpBody.setUserData(data);
	}
	
	@Override
	public void preStep(GameState state) {

		List<Predator> predators = state.getPredators();
		List<Prey> prey = state.getPrey();
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for (Body b : bodies) {
			preStepProcess(b, predators, prey);
		}
		
	}

	@Override
	public void postStep(GameState state) {
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		// Remove 'dead' bodies from the game and inform the GameState of the 
		// changes.
		for (Body b : bodies) {
			postStepProcess(b, state);
		}
		
	}

	@Override
	public void stepSimulation(float timestep) {
		
		// Run the simulation for one timestep.
		world.step(timestep, 8, 3);
		
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
			List<Prey> prey) {
		
		PhysicsData data = (PhysicsData) body.getUserData();
		PhysicsBodyType type = data.getType();
		
		switch (type) {
			case PowerUpPredator:
			case PowerUpPrey:
			case Pill:
			case Walls:
				return;
		
			case Prey:
				processAgent(body, prey, preySpeed);
				break;
				
			case Predator:
				processAgent(body, predators, predatorSpeed);
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
			float speed) {
		
		Agent agent = findAgent(body, agents);
		
		if (agent != null) {
			PhysicsDataAgent data = (PhysicsDataAgent) body.getUserData();
			
			Move move = agent.getNextMove();
			Vector2 velocity = body.getLinearVelocity();
			updateVelocity(velocity, move, speed);
			body.setLinearVelocity(velocity);
			
			if (data.getCurrentMove() != Direction.None) {
				data.setPreviousMove(data.getCurrentMove());
			}
			data.setCurrentMove(move.getDirection());
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
	
	/**
	 * Find the agent equivalent to the provided body if there is one. If there 
	 * is then, based on the agent's move, either activate a power up, apply the
	 * currently activated power up's action, or do nothing.
	 * 
	 * @param body - the body to process.
	 * @param predators - the list of all Predator agents currently in the game.
	 * @param prey - the list of all Prey agents currently in the game.
	 */
	private void processPowerUps(Body body, List<Predator> predators, 
			List<Prey> prey) {
		
		PhysicsData data = (PhysicsData) body.getUserData();
		Agent agent = null;
		if (data.getType() == PhysicsBodyType.Predator) {
			agent = findAgent(body, predators);
		} else if (data.getType() == PhysicsBodyType.Prey) {
			agent = findAgent(body, prey);
		}
		
		if (agent != null) {
			Move move = agent.getNextMove();
			processAgentPowerUps(agent, move, body);
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
	 */
	private void processAgentPowerUps(Agent agent, Move move, Body body) {

		if (move.getUsePowerUp()) {
			agent.activatePowerUp();
		}

		if (agent.hasActivatedPowerUp()) {
			List<? extends PowerUp> powerUps = agent.getActivatedPowerUps();
			for (PowerUp powerUp : powerUps) {
				powerUpProc.processPowerUp(powerUp, body);
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
				state.PredatorPowerUpCollected(preyID2, preyPowerUpPos);

			case PowerUpPredator:
				PhysicsDataPowerUp predPowerUpData = (PhysicsDataPowerUp) data;
				PointXY predPowerUpPos = predPowerUpData.getPosition();
				int predatorID2 = predPowerUpData.getAgentID();
				state.PredatorPowerUpCollected(predatorID2, predPowerUpPos);
				
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
	
	/**
	 * Convert a maze position from the back-end logic into a world coordinate.
	 * 
	 * @param pos - the back-end logic maze position.
	 * @return a physics world coordinate equivalent to the provided position.
	 */
	private Vector2 stateToWorld(PointXY pos) {
		return stateToWorld(pos, squareSize);
	}
	
	/**
	 * Convert a physics world position into a back-end logic maze coordinate.
	 * 
	 * @param pos - the physics world position to convert.
	 * @return a back-end logic maze position that is equivalent to the provided
	 * physics world coordinate.
	 */
	private PointXY worldToState(Vector2 pos) {
		return worldToState(pos, squareSize);
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
	
	/**
	 * For the given square size, convert the provided position in the back-end 
	 * logic coordinate system to the equivalent point in the Box2D world 
	 * coordinate system.
	 * 
	 * @param pos - the back-end logic position to convert.
	 * @param squareSize - the size of a maze square in the Box2D world.
	 * @return the equivalent position in the Box2D world coordinate system.
	 */
	public static Vector2 stateToWorld(PointXY pos, float squareSize) {
		// Adding 0.5 offsets us to the centre of the square.
		float centreX = (float) ((pos.getX() + 0.5) * squareSize); 
		float centreY = (float) ((pos.getY() + 0.5) * squareSize);
		return new Vector2(centreX, centreY);
	}
	
	/**
	 * For the given square size, convert the provided position in the Box2D 
	 * coordinate system to the equivalent point in the back-end logic 
	 * coordinate system.
	 * 
	 * @param pos - the Box2D world position to convert.
	 * @param squareSize - the size of a maze square in the Box2D world.
	 * @return the equivalent position in the back-end logic coordinate system.
	 */
	public static PointXY worldToState(Vector2 pos, float squareSize) {
		// Do the inverse of the stateToWorld calculation.
		int centreX = (int) Math.round((pos.x / squareSize) - 0.5);
		int centreY = (int) Math.round((pos.y / squareSize) - 0.5);
		return new PointXY(centreX, centreY);
	}
}
