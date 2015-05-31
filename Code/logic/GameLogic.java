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
 * @version 2015-05-19
 */
public class GameLogic {
	
	private GameState gs;
	private GameConfig gc;
	private AILogic aiLogic;
	
	/**
	 * Creates an instance of GameLogic.
	 * 
	 * @param gc (GameConfig)
	 */
	public GameLogic(GameConfig gc) {
		this.gc = gc;
		
		createGs();
		
		this.aiLogic = new AILogicSimple();
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
		int loopLimit = (gc.getMConfig() != null) ? gc.getMConfig().getLoopLimit() : defaultLoopLimit;
		int nPredPlayer = gc.getNumPredPlayer();
		int nPreyPlayer = gc.getNumPreyPlayer();
		PointXY point = null;
		List<PointXY> allPoints = new ArrayList<PointXY>();
		Set<PointXY> usedByPrey = new HashSet<PointXY>();
		List<Predator> predators = new ArrayList<Predator>();
		List<Prey> prey = new ArrayList<Prey>();
		Map<PointXY, Powerup> powers = new HashMap<PointXY, Powerup>();
		
		// Populate allPoints
		for (int i = gc.getDimensions().getMinX(); i <= gc.getDimensions().getMaxX(); i++) {
			for(int j = gc.getDimensions().getMinY(); j <= gc.getDimensions().getMaxY(); j++) {
				point = new PointXY(i, j);
				allPoints.add(point);
				totalNodes++;
			}
		}
		
		// Populate powers (assigned to random positions)
		for (int i = 0; i < gc.getPTypes().size(); i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			allPoints.remove(randomNum);
			
			powers.put(point, new Powerup(gc.getPTypes().get(i)));
		}
		
		// Populate pred (assigned to random positions)
		nLimit = counter + gc.getNumPred();
		for (int i = counter; i < nLimit; i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			allPoints.remove(randomNum);
			
			isPlayer = (nPredPlayer > 0);
			
			predators.add(new Predator(counter, point, isPlayer));
			counter++;
			nPredPlayer--;
		}
		
		// Populate prey (assigned to random positions)
		nLimit = counter + gc.getNumPrey();
		limitCounter = 0;
		for (int i = counter; i < nLimit; i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			
			// Points not removed from allpoints as prey do not interact with pills
			while (usedByPrey.contains(point) && limitCounter < loopLimit) {
				randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
				point = allPoints.get(randomNum);
				limitCounter++;
			}
			
			usedByPrey.add(point);
			isPlayer = (nPreyPlayer > 0);
			
			prey.add(new Prey(counter, point, isPlayer));
			counter++;
			nPreyPlayer--;
		}
		
		// Build maze
		Maze maze = (gc.getMConfig() == null) ? new Maze(gc.getDimensions()) : new Maze(gc.getDimensions(), gc.getMConfig());
		
		// Create pills
		Set<PointXY> pills = (gc.getHasPills()) ? new HashSet<PointXY>(allPoints) : new HashSet<PointXY>();
		
		// Create game state
		this.gs = new GameState(maze, predators, prey, pills, powers);
		
		try {
			if ((allPoints.size() + predators.size() + powers.size() != totalNodes) || usedByPrey.size() != prey.size() || (predators.size() != gc.getNumPred()) || (prey.size() != gc.getNumPrey())){
				throw new Exception("Illegal Game State: the game state does to correspond to the game configurations.");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
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
	public void setGs(GameState gs) {
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
	
	public boolean isGameOver(long currentTime) {
		
		// Check is time limit exceeded?
		
		// Check is there any Prey left?
		
		// Check is there any Pills left?

		
		
		return false;
	}
	
	public boolean predatorsWon() {
		return false;
	}
	
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
	
	public void setNonPlayerMoves() {
		List<Agent> nonPlayers = getAllNonPlayers();
		aiLogic.calcNextMove(nonPlayers, gs);
	}
	
}
