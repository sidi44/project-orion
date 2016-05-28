package physics;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.GameState;
import logic.Maze;
import logic.MazeNode;
import logic.Predator;
import logic.Prey;
import logic.powerup.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsGameWorld {

	// The Box2D world
	private final World world;
	
	// The physics bodies in the world
	private List<PhysicsBodyMazeSquare> mazeSquares;
	private List<PhysicsBodyPill> pills;
	private List<PhysicsBodyPredator> predators;
	private List<PhysicsBodyPrey> prey;
	private List<PhysicsBodyPredatorPowerUp> predatorPowerUps;
	
	// The type of debug information to process and display
	private List<PhysicsBodyDebug> debugBodies;
	
	public PhysicsGameWorld(World world, GameState initialState, 
			PhysicsConfiguration config) {
		
		this.world = world;
		
		this.mazeSquares = new ArrayList<PhysicsBodyMazeSquare>();
		this.pills = new ArrayList<PhysicsBodyPill>();
		this.predators = new ArrayList<PhysicsBodyPredator>();
		this.prey = new ArrayList<PhysicsBodyPrey>();
		this.predatorPowerUps = new ArrayList<PhysicsBodyPredatorPowerUp>();
		this.debugBodies = new ArrayList<PhysicsBodyDebug>();
		
		buildWorld(config, initialState);
	}
	
	private void buildWorld(PhysicsConfiguration config, GameState state) {
		createSquares(config, state);
		createPills(config, state);
		createPredators(config, state);
		createPrey(config, state);
		createPredatorPowerUps(config, state);
	}
	
	private void createSquares(PhysicsConfiguration config, GameState state) {
		
		float squareSize = config.getSquareSize();
		float wallWidth = (config.getWallWidthRatio() / 2) * squareSize;
		
		Maze maze = state.getMaze();
		Map<PointXY, MazeNode> nodes = maze.getNodes();
		Set<PointXY> keys = nodes.keySet();
		
		for (PointXY pos : keys) {
			Vector2 worldPos = PhysicsUtils.stateToWorld(pos, squareSize);
			PhysicsBodyMazeSquare square = new PhysicsBodyMazeSquare(world, 
					worldPos, squareSize, wallWidth, maze);
			mazeSquares.add(square);
		}
	}
	
	private void createPills(PhysicsConfiguration config, GameState state) {
		
		float radiusRatio = config.getPillRadiusRatio();
		float pillRadius = findRadius(config, radiusRatio);
		
		Set<PointXY> keys = state.getPills();
		float squareSize = config.getSquareSize();
		for (PointXY pos : keys) {
			Vector2 worldPos = PhysicsUtils.stateToWorld(pos, squareSize);
			PhysicsBodyPill pill = new PhysicsBodyPill(world, worldPos, 
					pillRadius);
			pills.add(pill);
		}
		
	}
	
	private void createPredators(PhysicsConfiguration config, GameState state) {
		
		float radiusRatio = config.getAgentRadiusRatio();
		float agentRadius = findRadius(config, radiusRatio);
		
		List<Predator> keys = state.getPredators();
		float squareSize = config.getSquareSize();
		for (Predator pred : keys) {
			PointXY pos = pred.getPosition();
			Vector2 worldPos = PhysicsUtils.stateToWorld(pos, squareSize);
			float predatorSpeed = config.getSpeed(pred.getSpeedIndex());
			PhysicsBodyPredator body = new PhysicsBodyPredator(world, worldPos, 
					agentRadius, predatorSpeed, pred);
			predators.add(body);
		}
		
	}
	
	private void createPrey(PhysicsConfiguration config, GameState state) {
		
		float radiusRatio = config.getAgentRadiusRatio();
		float agentRadius = findRadius(config, radiusRatio);
		
		List<Prey> keys = state.getPrey();
		float squareSize = config.getSquareSize();
		for (Prey p : keys) {
			PointXY pos = p.getPosition();
			Vector2 worldPos = PhysicsUtils.stateToWorld(pos, squareSize);
			float preySpeed = config.getSpeed(p.getSpeedIndex());
			PhysicsBodyPrey body = new PhysicsBodyPrey(world, worldPos, 
					agentRadius, preySpeed, p);
			prey.add(body);
		}
		
	}
	
	private void createPredatorPowerUps(PhysicsConfiguration config, 
			GameState state) {
		
		float radiusRatio = config.getPowerUpRadiusRatio();
		float powerUpRadius = findRadius(config, radiusRatio);
		
		Map<PointXY, PowerUp> powerUps = state.getPredatorPowerUps();
		float squareSize = config.getSquareSize();
		for (PointXY pos : powerUps.keySet()) {
			PowerUp powerUp = powerUps.get(pos);
			Vector2 worldPos = PhysicsUtils.stateToWorld(pos, squareSize);
			PhysicsBodyPredatorPowerUp body = new PhysicsBodyPredatorPowerUp(
					world, worldPos, powerUpRadius, powerUp);
			predatorPowerUps.add(body);
		}
		
	}
	
	public void createDebugBodies(GameState state) {
		
		if (debugBodies.size() != 0) {
			throw new IllegalStateException("Debug bodies already created.");
		}
		
		float squareSize = getSquareSize();
		
		Maze maze = state.getMaze();
		Map<PointXY, MazeNode> nodes = maze.getNodes();
		Set<PointXY> keys = nodes.keySet();
		
		for (PointXY pos : keys) {
			Vector2 worldPos = PhysicsUtils.stateToWorld(pos, squareSize);
			PhysicsBodyDebug body = new PhysicsBodyDebug(world, worldPos, 
					squareSize);
			debugBodies.add(body);
		}
		
	}
	
	private float findRadius(PhysicsConfiguration config, float radiusRatio) {
		float squareSize = config.getSquareSize();
		float wallWidth = (config.getWallWidthRatio() / 2) * squareSize;
		float radius = (squareSize / 2 - wallWidth) * radiusRatio;
		return radius;
	}
	
	public void addContactListener(ContactListener listener) {
		world.setContactListener(listener);
	}
	
	public World getBox2DWorld() {
		return world;
	}
	
	public float getSquareSize() {
		
		if (mazeSquares.size() > 0) {
			return mazeSquares.get(0).getSquareSize();
		} else {
			System.err.println("Maze squares not initialised.");
			return -1.0f;
		}
		
	}
	
	public float getPredatorSpeed() {
		
		// TODO Remove this method.
		
		if (predators.size() > 0) {
			return predators.get(0).getBaseSpeed();
		} else {
			System.err.println("Predators not initialised.");
			return -1.0f;
		}
		
	}
	
	public float getPreySpeed() {
		
		// TODO Remove this method.
		
		if (prey.size() > 0) {
			return prey.get(0).getBaseSpeed();
		} else {
			System.err.println("Prey not initialised.");
			return -1.0f;
		}
		
	}
	
	public List<PhysicsBodyMazeSquare> getMazeSquares() {
		return mazeSquares;
	}
	
	public List<PhysicsBodyPill> getPills() {
		return pills;
	}
	
	public List<PhysicsBodyPredator> getPredators() {
		return predators;
	}
	
	public List<PhysicsBodyPrey> getPrey() {
		return prey;
	}
	
	public List<PhysicsBodyAgent> getAgents() {
		List<PhysicsBodyAgent> agents = new ArrayList<PhysicsBodyAgent>();
		agents.addAll(predators);
		agents.addAll(prey);
		return agents;
	}
	
	public List<PhysicsBodyPredatorPowerUp> getPredatorPowerUps() {
		return predatorPowerUps;
	}
	
	public List<PhysicsBodyDebug> getDebugBodies() {
		return debugBodies;
	}
	
	public void removeBody(PhysicsBody toRemove) {
		
		PhysicsBodyType type = toRemove.getType();
		
		switch (type) {
			case Debug:
				throw new RuntimeException("Not implemented");
			case Pill:
				removeBodyFromList(toRemove, pills);
				break;
			case PowerUpPredator:
				removeBodyFromList(toRemove, predatorPowerUps);
				break;
			case PowerUpPrey:
				throw new RuntimeException("Not implemented");
			case Predator:
				removeBodyFromList(toRemove, predators);
				break;
			case Prey:
				removeBodyFromList(toRemove, prey);
				break;
			case Walls:
				removeBodyFromList(toRemove, mazeSquares);
				break;
			default:
				break;
		}
		
	}
	
	private void removeBodyFromList(PhysicsBody toRemove, 
			List<? extends PhysicsBody> list) {
		
		for (PhysicsBody physicsBody : list) {
			if (physicsBody == toRemove) {
				world.destroyBody(physicsBody.getBody());
				list.remove(physicsBody);
				break;
			}
		}
		
	}
	
//	private List<PhysicsBody> allBodies() {
//		List<PhysicsBody> allBodies = new ArrayList<PhysicsBody>();
//		allBodies.addAll(mazeSquares);
//		allBodies.addAll(pills);
//		allBodies.addAll(predators);
//		allBodies.addAll(prey);
//		allBodies.addAll(predatorPowerUps);
//		return allBodies;
//	}
	
	
}
