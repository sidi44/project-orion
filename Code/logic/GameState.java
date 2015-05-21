package logic;

import geometry.PointXY;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents the state of the game.
 * 
 * @author Martin Wong
 * @version 2015-05-19
 */
public class GameState {
	
	private Maze maze;
	private Set<PointXY> pills;
	private Map<Integer, Predator> pred;
	private Map<Integer, Prey> prey;
	private Map<PointXY, Powerup> powers;
	
	/**
	 * Creates an instance of GameState.
	 * 
	 * @param maze (Maze)
	 * @param pred (Map<Integer, Predator>)
	 * @param prey (Map<Integer, Predy>)
	 * @param pills (Set<PointXY>)
	 * @param powers (Map<PointXY, Powerup>)
	 */
	public GameState(Maze maze, Map<Integer, Predator> pred, Map<Integer, Prey> prey,
			Set<PointXY> pills, Map<PointXY, Powerup> powers) {
		this.maze = maze;
		this.pred = pred;
		this.prey = prey;
		this.pills = pills;
		this.powers = powers;
	}
	
	/**
	 * Gets the maze.
	 * @return maze (Maze)
	 */
	public Maze getMaze() {
		return this.maze;
	}
	
	/**
	 * Sets the maze.
	 * @param maze (Maze)
	 */
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	/**
	 * Gets the pills.
	 * 
	 * @return pills (Set<PointXY>)
	 */
	public Set<PointXY> getPills() {
		return this.pills;
	}
	
	/**
	 * Sets the piils
	 * 
	 * @param pills (Set<PointXY>)
	 */
	public void setPills(Set<PointXY> pills) {
		this.pills = pills;
	}
	
	/**
	 * Get the predators.
	 * 
	 * @return pred (Map<Integer, Predator>)
	 */
	public Map<Integer, Predator> getPred() {
		return this.pred;
	}
	
	/**
	 * Set the predators.
	 * 
	 * @param pred (Map<Integer, Predator>)
	 */
	public void setPred(Map<Integer, Predator> pred) {
		this.pred = pred;
	}
	
	/**
	 * Gets the prey.
	 * 
	 * @return prey (Map<Integer, Prey>)
	 */
	public Map<Integer, Prey> getPrey() {
		return this.prey;
	}
	
	/**
	 * Set the prey.
	 * 
	 * @param prey (Map<Integer, Prey>)
	 */
	public void setPrey(Map<Integer, Prey> prey) {
		this.prey = prey;
	}
	
	/**
	 * Gets the powerups.
	 * 
	 * @return powers (Map<PointXY, Powerup>)
	 */
	public Map<PointXY, Powerup> getPowers() {
		return this.powers;
	}
	
	/**
	 * Sets the powerups.
	 * 
	 * @param powers (Map<PointXY, Powerup>)
	 */
	public void setPowers(Map<PointXY, Powerup> powers) {
		this.powers = powers;
	}
	
	/**
	 * Gets the agents.
	 * 
	 * @return agents (Map<Integer, Agent>)
	 */
	public Map<Integer, Agent> getAgents() {
		Map<Integer, Agent> agents = new HashMap<Integer, Agent>();
		agents.putAll(pred);
		agents.putAll(prey);
		return agents;
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
	 * Add predators.
	 * 
	 * @param id (int)
	 * @param predator (Predator)
	 */
 	public void addPredator(int id, Predator predator) {
 		if (!pred.containsKey(id)) {
 			pred.put(id, predator);
 		}
 	}
	
 	/**
 	 * Remove predators by id.
 	 * 
 	 * @param id (int)
 	 */
	public void removePredator(int id) {
		if (pred.containsKey(id)) {
			pred.remove(id);
		}
	}
	
	/**
	 * Updates the position of the specified predator.
	 * 
	 * @param id (int)
	 * @param pos (PointXY)
	 */
	public void updatePredatorPosition(int id, PointXY pos) {
		if (pred.containsKey(id)) {
			Predator p = pred.get(id);
			p.setPosition(pos);
		}
	}
	
	/**
	 * Add prey.
	 * 
	 * @param id (int)
	 * @param p (Prey)
	 */
 	public void addPrey(int id, Prey p) {
 		if (!prey.containsKey(id)) {
 			prey.put(id, p);
 		}
 	}
 	
 	/**
 	 * Remove prey by id.
 	 * 
 	 * @param id (int)
 	 */
	public void removePrey(int id) {
		if (prey.containsKey(id)) {
			prey.remove(id);
		}
	}
	
	/**
	 * Updates the position of the specified prey.
	 * 
	 * @param id (int)
	 * @param pos (PointXY)
	 */
	public void updatePreyPosition(int id, PointXY pos) {
		if (prey.containsKey(id)) {
			Prey p = prey.get(id);
			p.setPosition(pos);
		}
	}
	
}
