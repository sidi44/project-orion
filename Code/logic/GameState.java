package logic;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.powerup.PowerUp;

/**
 * Represents the state of the game.
 * 
 * @author Martin Wong, Simon Dicken
 * @version 2015-12-28
 */
public class GameState {
	
	private Maze maze;
	private Set<PointXY> pills;
	private List<Predator> predators;
	private List<Prey> prey;
	private Map<PointXY, PowerUp> predatorPowerUps;
	private Map<PointXY, PowerUp> preyPowerUps;
	
	private float timeRemaining;
	
	private Map<Agent, Set<PointXY>> partition;
	private Map<Agent, Set<PointXY>> saferPositions;
	
	private final static int PILL_SCORE_VALUE = 100;
	private final static int SECONDS_SCORE_VALUE = 10;
	
	/**
	 * Creates an instance of GameState.
	 * 
	 * @param maze (Maze)
	 * @param pred (Map<Integer, Predator>)
	 * @param prey (Map<Integer, Prey>)
	 * @param pills (Set<PointXY>)
	 * @param predatorPowerUps (Map<PointXY, PredatorPowerUp>)
	 * @param preyPowerUps (Map<PointXY, PreyPowerUp>)
	 * @param timeLimit (int)
	 */
	public GameState(Maze maze, List<Predator> pred, List<Prey> prey,
			Set<PointXY> pills, Map<PointXY, PowerUp> predatorPowerUps,
			Map<PointXY, PowerUp> preyPowerUps, int timeLimit) {
		this.maze = maze;
		this.predators = pred;
		this.prey = prey;
		this.pills = pills;
		this.predatorPowerUps = predatorPowerUps;
		this.preyPowerUps = preyPowerUps;
		
		this.timeRemaining = timeLimit;
	}
	
	/**
	 * Gets the maze.
	 * @return maze (Maze)
	 */
	public Maze getMaze() {
		return this.maze;
	}
	
	/**
	 * Gets the pills.
	 * 
	 * @return pills (Set<PointXY>)
	 */
	public Set<PointXY> getPills() {
		return this.pills;
	}
	
	public boolean hasPill(PointXY pos) {
		return pills.contains(pos);
	}
	
	/**
	 * Get the predators.
	 * 
	 * @return predators (Map<Integer, Predator>)
	 */
	public List<Predator> getPredators() {
		return this.predators;
	}
	
	/**
	 * Gets the prey.
	 * 
	 * @return prey (Map<Integer, Prey>)
	 */
	public List<Prey> getPrey() {
		return this.prey;
	}
	
	/**
	 * Gets the predator powerups.
	 * 
	 * @return predatorPowerUps (Map<PointXY, PredatorPowerUp>)
	 */
	public Map<PointXY, PowerUp> getPredatorPowerUps() {
		return this.predatorPowerUps;
	}
	
	/**
	 * Gets the prey powerups.
	 * 
	 * @return preyPowerUps (Map<PointXY, PreyPowerUp>)
	 */
	public Map<PointXY, PowerUp> getPreyPowerUps() {
		return this.preyPowerUps;
	}
	
	/**
	 * Gets the agents.
	 * 
	 * @return agents (List<Agent>)
	 */
	public List<Agent> getAgents() {
		List<Agent> agents = new ArrayList<Agent>();
		agents.addAll(predators);
		agents.addAll(prey);
		return agents;
	}
	
	/**
	 * Gets the powerUps.
	 * 
	 * @return powerUps (Map<PointXY, PowerUp>)
	 */
	public Map<PointXY, PowerUp> getPowerUps() {
		Map<PointXY, PowerUp> powerUps = new HashMap<PointXY, PowerUp>();
		powerUps.putAll(predatorPowerUps);
		powerUps.putAll(preyPowerUps);
		return powerUps;
	}
	
	/**
	 * Removes pills by position.
	 * 
	 * @param pos (PointXY)
	 */
	public void removePill(PointXY pos) {
		if (pills.contains(pos)) {
			pills.remove(pos);
		}
	}
	
	/**
	 * Removes predator power ups by position.
	 * 
	 * @param pos (PointXY)
	 */
	public void removePredatorPowerUp(PointXY pos) {
		if (predatorPowerUps.containsKey(pos)) {
			predatorPowerUps.remove(pos);
		}
	}

	/**
	 * Removes prey power ups by position.
	 * 
	 * @param pos (PointXY)
	 */
	public void removePreyPowerUp(PointXY pos) {
		if (preyPowerUps.containsKey(pos)) {
			preyPowerUps.remove(pos);
		}
	}
	
	/**
	 * Add a predator.
	 * 
	 * @param id (int)
	 * @param predator (Predator)
	 */
 	public void addPredator(Predator predator) {
 		predators.add(predator);
 	}
	
 	/**
 	 * Remove a predator by id.
 	 * 
 	 * @param id (int)
 	 */
	public void removePredator(int id) {
		Iterator<Predator> iter = predators.iterator();
		while(iter.hasNext()) {
			Predator p = iter.next();
			if (p.getID() == id) {
				iter.remove();
			}
		}
	}
	
	/**
	 * Updates the position of the specified predator.
	 * 
	 * @param id (int)
	 * @param pos (PointXY)
	 */
	public void updatePredatorPosition(int id, PointXY pos) {
		Iterator<Predator> iter = predators.iterator();
		while(iter.hasNext()) {
			Predator p = iter.next();
			if (p.getID() == id) {
				p.setPosition(pos);
			}
		}
	}
	
	/**
	 * Add a prey.
	 * 
	 * @param id (int)
	 * @param p (Prey)
	 */
 	public void addPrey(Prey p) {
 		this.prey.add(p);
 	}
 	
 	/**
 	 * Remove a prey by id.
 	 * 
 	 * @param id (int)
 	 */
	public void removePrey(int id) {
		Iterator<Prey> iter = prey.iterator();
		while(iter.hasNext()) {
			Prey p = iter.next();
			if (p.getID() == id) {
				iter.remove();
			}
		}
	}
	
	/**
	 * Updates the position of the specified prey.
	 * 
	 * @param id (int)
	 * @param pos (PointXY)
	 */
	public void updatePreyPosition(int id, PointXY pos) {
		Iterator<Prey> iter = prey.iterator();
		while(iter.hasNext()) {
			Prey p = iter.next();
			if (p.getID() == id) {
				p.setPosition(pos);
			}
		}
	}
	
	/**
	 * Obtain the Predator with the given ID. If no Predator exists with that
	 * ID, null is returned.
	 * 
	 * @param id - the ID of the Predator to get.
	 * @return the Predator with the given ID, or null if no such Predator 
	 * exists.
	 */
	public Predator getPredator(int id) {
		for (Predator p : predators) {
			if (p.getID() == id) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Obtain the Prey with the given ID. If no Prey exists with that ID, null 
	 * is returned.
	 * 
	 * @param id - the ID of the Prey to get.
	 * @return the Prey with the given ID, or null if no such Prey exists.
	 */
	public Prey getPrey(int id) {
		for (Prey p : prey) {
			if (p.getID() == id) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Inform the game state that the predator with the given id has collected 
	 * the power up at the given position.
	 * 
	 * @param agentID - the id of the predator that has collected the power up.
	 * @param powerUpPos - the position in the maze of the power up that has 
	 * been collected.
	 */
	public void predatorPowerUpCollected(int agentID, PointXY powerUpPos) {
		Predator p = getPredator(agentID);
		PowerUp powerUp = predatorPowerUps.get(powerUpPos);

		if (p != null && powerUp != null) {
			p.addStoredPowerUp(powerUp);
			powerUp.collected(p);
			removePredatorPowerUp(powerUpPos);
		}
	}

	/**
	 * Inform the game state that the prey with the given id has collected the
	 * power up at the given position.
	 * 
	 * @param agentID - the id of the prey that has collected the power up.
	 * @param powerUpPos - the position in the maze of the power up that has 
	 * been collected.
	 */
	public void preyPowerUpCollected(int agentID, PointXY powerUpPos) {
		Prey p = getPrey(agentID);
		PowerUp powerUp = preyPowerUps.get(powerUpPos);

		if (p != null && powerUp != null) {
			p.addStoredPowerUp(powerUp);
			powerUp.collected(p);
			removePreyPowerUp(powerUpPos);
		}
	}
	
	/**
	 * Return the agent (either predator or prey) with the given ID.
	 * If no such agent exists, null is returned.
	 * 
	 * @param agentID - the ID of the agent to find.
	 * @return the agent with the given id, or null if no such agent exists.
	 */
	public Agent getAgent(int agentID) {
		
		List<Agent> agents = getAgents();
		for (Agent agent : agents) {
			if (agent.getID() == agentID) {
				return agent;
			}
		}
		
		return null;
	}
	
	/**
	 * ** Intended for Debug only. **
	 * 
	 * Set the partitioned maze. The partitioned maze is created by assigning 
	 * each node in the maze to a Prey.
	 * 
	 * @param partition - the partition to set.
	 */
	public void setPartition(Map<Agent, Set<PointXY>> partition) {
		this.partition = partition;
	}
	
	/**
	 * ** Intended for Debug only **
	 * 
	 * Get the current partitioned maze. The partitioned maze is created by 
	 * assigning each node in the maze to a Prey.
	 * 
	 * @return Get the current partitioned maze.
	 */
	public Map<Agent, Set<PointXY>> getPartition() {
		return partition;
	}

	/**
	 * ** Intended for Debug only. **
	 * 
	 * Set the safer positions map. Safer positions are the collection of points 
	 * which a prey considers safer than its current position.
	 * 
	 * @param saferPositions - maps agents to the set of positions that they 
	 * consider safer than their current position.
	 */
	public void setSaferPositions(Map<Agent, Set<PointXY>> saferPositions) {
		this.saferPositions = saferPositions;
	}

	/**
	 * ** Intended for Debug only. **
	 * 
	 * Get the safer positions map. Safer positions are the collection of points 
	 * which a prey considers safer than its current position.
	 * 
	 * @return saferPositions - maps agents to the set of positions that they 
	 * consider safer than their current position.
	 */
	public Map<Agent, Set<PointXY>> getSaferPositions() {
		return saferPositions;
	}
	
	/**
	 * Get the time remaining for the game in seconds. 
	 * When this value hits zero, the game is over.
	 * 
	 * @return the time remaining in seconds.
	 */
	public float getTimeRemaining() {
		return timeRemaining;
	}
	
	/**
	 * Reduce the time remaining for the game by a specified amount in seconds.
	 * 
	 * @param seconds - the amount of seconds by which to reduce the time 
	 * remaining.
	 */
	public void decreaseTimeRemaining(float seconds) {
		timeRemaining -= seconds;
		if (timeRemaining < 0) {
			timeRemaining = 0;
		}
	}
	
	/**
	 * Return the current score.
	 * 
	 * @return the current score.
	 */
	public int getScore() {
		// The score has two parts to it which are functions of the number of 
		// pills remaining in the maze and the number of seconds remaining. 
		int pillScore = pills.size() * PILL_SCORE_VALUE;
		int timeScore = (int) timeRemaining * SECONDS_SCORE_VALUE;
		
		int score = pillScore + timeScore;
		return score;
	}
	
}
