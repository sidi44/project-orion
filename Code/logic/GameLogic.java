package logic;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.NumberUtils;

/**
 * Sets the state of the game and also manages the interaction between
 * different game components.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
public class GameLogic {
	
	private GameState gs;
	private GameConfiguration gc;
	private AILogic aiLogic;
	
	/**
	 * Creates an instance of GameLogic.
	 * 
	 * @param gc (GameConfig)
	 */
	public GameLogic(GameConfiguration gc) {
		this.gc = gc;
		this.aiLogic = new AILogicSimple();
		
		createGs();
	}
	
	/**
	 * Creates the game state.
	 * 
	 * Each predator and powerup position has pills removed,
	 * while each prey position still has pills.
	 * 
	 * IDs are assigned to predators and prey based on the following rules:
	 * 
	 * The lowest ID is 1.
	 * 
	 * ID for predators: 1 to numPred
	 * ID for predator human players: 1 to numPredPlayer
	 * ID for predator computer players: numPredPlayer + 1 to numPred
	 * 
	 * ID for prey: (numPred + 1) to (numPred) + numPrey
	 * ID for prey human players: (numPred + 1) to (numPred) + numPreyPlayer
	 * ID for prey computer players: (numPred + 1) + (numPreyPlayer + 1) to (numPred) + numPrey
	 * 
	 */
	private void createGs() {
		int defaultLoopLimit = 50;
		
		boolean isPlayer = false;
		int randomNum = 0;
		int totalNodes = 0;
		int nLimit = 0;
		int counter = 1;
		int limitCounter = 0;
		PointXY point = null;
		int loopLimit = (gc.getMConfig() != null) ? gc.getMConfig().getLoopLimit() : defaultLoopLimit;
		
		List<PointXY> allPoints = new ArrayList<PointXY>();
		Set<PointXY> usedPoints = new HashSet<PointXY>();
		AgentConfig aConfig = gc.getAConfig();
		PowerUpConfig pConfig = gc.getPConfig();
		int nPredPlayer = aConfig.getNumPredPlayer();
		int nPreyPlayer = aConfig.getNumPreyPlayer();
		List<Predator> predators = new ArrayList<Predator>();
		List<Prey> prey = new ArrayList<Prey>();
		Map<PointXY, PredatorPowerUp> predatorPowers = new HashMap<PointXY, PredatorPowerUp>();
		Map<PointXY, PreyPowerUp> preyPowers = new HashMap<PointXY, PreyPowerUp>();
		
		// Build maze
		Maze maze = (gc.getMConfig() == null) ? new Maze(gc.getDimensions()) : new Maze(gc.getDimensions(), gc.getMConfig());
		
		// Populate allPoints
		for (int i = gc.getDimensions().getMinX(); i <= gc.getDimensions().getMaxX(); i++) {
			for(int j = gc.getDimensions().getMinY(); j <= gc.getDimensions().getMaxY(); j++) {
				point = new PointXY(i, j);
				
				if (maze.withinDimensions(point)) {
					allPoints.add(point);
					totalNodes++;
				}
			}
		}
		
		// Populate predator powers (assigned to random positions)
		PredatorPowerUp predatorPowerUp = null;
		for (int i = 0; i < pConfig.getNumPredPow(); i++) {
			if (pConfig.getPredatorPowerUps().size() > 0) {
				randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
				point = allPoints.get(randomNum);
				allPoints.remove(randomNum);
				
				randomNum = NumberUtils.randomInt(0, pConfig.getPredatorPowerUps().size() - 1);
				predatorPowerUp = pConfig.getPredatorPowerUps().get(randomNum);
				
				switch (predatorPowerUp.getPType()){
					case SpeedUpPredator:
						predatorPowerUp = new PredatorPowerUpSpeedUp(predatorPowerUp, gc.getPTConfig().getPredatorUpFactor());
						break;
					case SlowDownPrey:
						predatorPowerUp = new PredatorPowerUpSlowDown(predatorPowerUp, gc.getPTConfig().getPreyDownFactor());
						break;
					case Teleport:
						predatorPowerUp = new PredatorPowerUpTeleport(predatorPowerUp, maze.getRandomPoint());
						break;
					default:
						break;
				}
				
				predatorPowers.put(point, predatorPowerUp);
			}
		}
		
		// Populate prey powers (assigned to random positions)
		PreyPowerUp preyPowerUp = null;
		for (int i = 0; i < pConfig.getNumPreyPow(); i++) {
			if (pConfig.getPreyPowerUps().size() > 0) {
				randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
				point = allPoints.get(randomNum);
				allPoints.remove(randomNum);
				
				randomNum = NumberUtils.randomInt(0, pConfig.getPreyPowerUps().size() - 1);
				preyPowerUp = pConfig.getPreyPowerUps().get(randomNum);
				
				switch (preyPowerUp.getPType()){
					case SpeedUpPrey:
						preyPowerUp = new PreyPowerUpSpeedUp(preyPowerUp, gc.getPTConfig().getPreyUpFactor());
						break;
					case SlowDownPredator:
						preyPowerUp = new PreyPowerUpSlowDown(preyPowerUp, gc.getPTConfig().getPredatorDownFactor());
						break;
					case Teleport:
						preyPowerUp = new PreyPowerUpTeleport(preyPowerUp, maze.getRandomPoint());
						break;
					default:
						break;
				}
				
				preyPowers.put(point, preyPowerUp);
			}
		}
		
		// Populate pred (assigned to random positions)
		nLimit = counter + aConfig.getNumPred();
		limitCounter = 0;
		for (int i = counter; i < nLimit; i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			
			// Points not removed from allpoints as prey do not interact with pills
			while (usedPoints.contains(point) && limitCounter < loopLimit) {
				randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
				point = allPoints.get(randomNum);
				limitCounter++;
			}
			
			usedPoints.add(point);
			isPlayer = (nPredPlayer > 0);
			
			predators.add(new Predator(counter, point, isPlayer, aConfig.getMaxPredPowerUp(), true));
			counter++;
			nPredPlayer--;
		}
		
		// Populate prey (assigned to random positions)
		nLimit = counter + aConfig.getNumPrey();
		limitCounter = 0;
		for (int i = counter; i < nLimit; i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			
			while (usedPoints.contains(point) && limitCounter < loopLimit) {
				randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
				point = allPoints.get(randomNum);
				counter++;
			}
			
			allPoints.remove(randomNum);
			isPlayer = (nPreyPlayer > 0);
			
			prey.add(new Prey(counter, point, isPlayer, aConfig.getMaxPreyPowerUp(), true));
			counter++;
			nPreyPlayer--;
		}
		
		// Create pills
		Set<PointXY> pills = (gc.getHasPills()) ? new HashSet<PointXY>(allPoints) : new HashSet<PointXY>();
		
		// Create game state
		this.gs = new GameState(maze, predators, prey, pills, predatorPowers, preyPowers);
		
		try {
			if ((allPoints.size() + prey.size() + predatorPowers.size() + preyPowers.size() != totalNodes) || usedPoints.size() != predators.size() || (predators.size() != aConfig.getNumPred() || (prey.size() != aConfig.getNumPrey()))){
				throw new Exception("Illegal Game State: the game state does to correspond to the game configurations.");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * Checks to see whether game is over, and in what way.
	 * 
	 * @param timeRemaining: in seconds (int)
	 * @return go (GameOver)
	 */
	public GameOver isGameOver(int timeRemaining) {
		GameOver go = GameOver.No;
		
		if (timeRemaining <= 0) {
			go = GameOver.Time; // Predators lose
		} else if (gs.getPills().size() <= 0) {
			go = GameOver.Pills; // Predators lose
		} else if (gs.getPrey().size() <= 0) {
			go = GameOver.Prey; // Predators win
		}
		
		return go;
	}
	
	/**
	 * Get all predators and prey who are players.
	 * 
	 * @return players (boolean)
	 */
	public List<Agent> getAllPlayers() {
		List<Agent> agents = gs.getAgents();
		List<Agent> players = new ArrayList<Agent>();
		for (Agent a : agents) {
			if (a.isPlayer()) {
				players.add(a);
			}
		}
		return players;
	}
	
	/**
	 * Get all predators and prey who are non-players.
	 * 
	 * @return nonPlayers (boolean)
	 */
	private List<Agent> getAllNonPlayers() {
		List<Agent> agents = gs.getAgents();
		List<Agent> nonPlayers = new ArrayList<Agent>();
		for (Agent a : agents) {
			if (!a.isPlayer()) {
				nonPlayers.add(a);
			}
		}
		return nonPlayers;
	}
	
	/**
	 * Sets the AI moves for non-players
	 * (predator and prey).
	 */
	public void setNonPlayerMoves() {
		List<Agent> nonPlayers = getAllNonPlayers();
		aiLogic.calcNextMove(nonPlayers, gs);
	}
	
	/**
	 * Gets the game state.
	 * 
	 * @return gs (GameState)
	 */
	public GameState getGameState() {
		return this.gs;
	}
	
	/**
	 * Sets the game state.
	 * 
	 * @param gs (GameState)
	 */
	public void setGameState(GameState gs) {
		this.gs = gs;
	}
	
	/**
	 * Sets the next move of a specified predator.
	 * 
	 * @param id (int)
	 * @param move (Move)
	 */
	public void setPredNextMove(int id, Move move) {
		List<Predator> predators = gs.getPredators();
		for (Predator p : predators) {
			if (p.getID() == id) {
				p.setNextMove(move);
			}
		}
	}
	
	/**
	 * Sets the next move of a specified prey.
	 * 
	 * @param id (int)
	 * @param move (Move)
	 */
	public void setPreyNextMove(int id, Move move) {
		this.gs.getPrey().get(id).setNextMove(move);
	}
	
}
