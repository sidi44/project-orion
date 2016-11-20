package logic;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.powerup.PowerUp;
import logic.powerup.PowerUpCreator;
import logic.powerup.PowerUpType;
import progress.ProgressTask;
import ai.AILogic;
import ai.AILogicPartition;
import utils.NumberUtils;

/**
 * Sets the state of the game and also manages the interaction between
 * different game components.
 * 
 * @author Martin Wong
 * @version 2015-12-28
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
		createGs();
		this.aiLogic = new AILogicPartition(gs.getMaze());
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
		Map<PointXY, PowerUp> predatorPowerUps = 
				new HashMap<PointXY, PowerUp>();
		Map<PointXY, PowerUp> preyPowerUps = new HashMap<PointXY, PowerUp>();
		
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
		
		// Populate predator power ups (assigned to random positions)
		PowerUpCreator powerUpCreator = new PowerUpCreator(maze);
		PowerUp predatorPowerUp = null;
		for (int i = 0; i < pConfig.getNumPredPow(); i++) {
			Map<PowerUpType, Integer> powerUpDefs = 
					pConfig.getPredatorPowerUps();
			if (powerUpDefs.size() > 0) {
				randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
				point = allPoints.get(randomNum);
				allPoints.remove(randomNum);
				
				predatorPowerUp = 
						powerUpCreator.createRandomPredatorPowerUp(powerUpDefs);
				predatorPowerUps.put(point, predatorPowerUp);
			}
		}
		
		// Populate prey powers (assigned to random positions)
//		PowerUp preyPowerUp = null;
//		for (int i = 0; i < pConfig.getNumPreyPow(); i++) {
//			if (pConfig.getPreyPowerUps().size() > 0) {
//				randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
//				point = allPoints.get(randomNum);
//				allPoints.remove(randomNum);
//				
//				randomNum = NumberUtils.randomInt(0, pConfig.getPreyPowerUps().size() - 1);
//				preyPowerUp = pConfig.getPreyPowerUps().get(randomNum);
//				
//				if (preyPowerUp instanceof PowerUpTeleport) {
//					preyPowerUp = new PowerUpTeleport();
//					PowerUpTeleport teleport = (PowerUpTeleport) preyPowerUp;
//					teleport.setTeleportPoint(maze.getRandomPoint());
//				}
//				
//				preyPowerUps.put(point, preyPowerUp);
//			}
//		}
		
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
			
			int baseSpeedIndex = aConfig.getPredBaseSpeedIndex();
			predators.add(new Predator(counter, isPlayer, point, baseSpeedIndex, 
					aConfig.getMaxPredPowerUp()));
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
			
			int baseSpeedIndex = aConfig.getPreyBaseSpeedIndex();
			prey.add(new Prey(counter, isPlayer, point, baseSpeedIndex, 
					aConfig.getMaxPreyPowerUp()));
			counter++;
			nPreyPlayer--;
		}
		
		// Create pills
		Set<PointXY> pills = (gc.getHasPills()) ? new HashSet<PointXY>(allPoints) : new HashSet<PointXY>();
		
		// Create game state
		int timeLimit = gc.getTimeLimit();
		this.gs = new GameState(maze, predators, prey, pills, predatorPowerUps, preyPowerUps, timeLimit);
		
		try {
			if ((allPoints.size() + prey.size() + predatorPowerUps.size() + preyPowerUps.size() != totalNodes) || usedPoints.size() != predators.size() || (predators.size() != aConfig.getNumPred() || (prey.size() != aConfig.getNumPrey()))){
				throw new Exception("Illegal Game State: the game state does to correspond to the game configurations.");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * Checks to see whether game is over, and in what way.
	 * 
	 * @return go (GameOver)
	 */
	public GameOver isGameOver() {
		GameOver go = GameOver.No;
		
		if (gs.getTimeRemaining() <= 0) {
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
	
	public void setAILogic(AILogic ai) {
		this.aiLogic = ai;
	}
	
	public AILogic getAILogic() {
		return aiLogic;
	}
	
	public List<ProgressTask> getProgressTasks() {
		List<ProgressTask> tasks = new ArrayList<ProgressTask>();
		tasks.addAll(aiLogic.getProgressTasks());
		return tasks;
	}
	
}
