package logic;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the state of the game.
 * 
 * @author Martin Wong, Simon Dicken
 * @version 2015-05-31
 */
public class GameState {
	
	private Maze maze;
	private Set<PointXY> pills;
	private List<Predator> predators;
	private List<Prey> prey;
	private Map<PointXY, PredatorPowerUp> predatorPowerUps;
	private Map<PointXY, PreyPowerUp> preyPowerUps;
	private PathFinder pathFinder;
	
	/**
	 * Creates an instance of GameState.
	 * 
	 * @param maze (Maze)
	 * @param pred (Map<Integer, Predator>)
	 * @param prey (Map<Integer, Prey>)
	 * @param pills (Set<PointXY>)
	 * @param predatorPowerUps (Map<PointXY, PredatorPowerUp>)
	 * @param preyPowerUps (Map<PointXY, PreyPowerUp>)
	 */
	public GameState(Maze maze, List<Predator> pred, List<Prey> prey,
			Set<PointXY> pills, Map<PointXY, PredatorPowerUp> predatorPowerUps,
			Map<PointXY, PreyPowerUp> preyPowerUps) {
		this.maze = maze;
		this.predators = pred;
		this.prey = prey;
		this.pills = pills;
		this.predatorPowerUps = predatorPowerUps;
		this.preyPowerUps = preyPowerUps;
		
		this.pathFinder = new PathFinder(maze);
		//this.pathFinder.generateAllPaths();
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
	
	public boolean hasPill(PointXY pos) {
		return pills.contains(pos);
	}
	
	/**
	 * Get the predators.
	 * 
	 * @return pred (Map<Integer, Predator>)
	 */
	public List<Predator> getPredators() {
		return this.predators;
	}
	
	/**
	 * Set the predators.
	 * 
	 * @param pred (Map<Integer, Predator>)
	 */
	public void setPred(List<Predator> predators) {
		this.predators = predators;
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
	 * Set the prey.
	 * 
	 * @param prey (Map<Integer, Prey>)
	 */
	public void setPrey(List<Prey> prey) {
		this.prey = prey;
	}
	
	/**
	 * Gets the predator powerups.
	 * 
	 * @return predatorPowerUps (Map<PointXY, PredatorPowerUp>)
	 */
	public Map<PointXY, PredatorPowerUp> getPredatorPowerUps() {
		return this.predatorPowerUps;
	}
	
	/**
	 * Sets the predator powerups.
	 * 
	 * @param predatorPowerUps (Map<PointXY, PredatorPowerUp>)
	 */
	public void setPredatorPowers(Map<PointXY, PredatorPowerUp> predatorPowerUps) {
		this.predatorPowerUps = predatorPowerUps;
	}
	
	/**
	 * Gets the prey powerups.
	 * 
	 * @return preyPowerUps (Map<PointXY, PreyPowerUp>)
	 */
	public Map<PointXY, PreyPowerUp> getPreyPowerUps() {
		return this.preyPowerUps;
	}
	
	/**
	 * Sets the prey powerups.
	 * 
	 * @param preyPowerUps (Map<PointXY, PreyPowerUp>)
	 */
	public void setPreyPowers(Map<PointXY, PreyPowerUp> preyPowerUps) {
		this.preyPowerUps = preyPowerUps;
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
	 * Add predators.
	 * 
	 * @param id (int)
	 * @param predator (Predator)
	 */
 	public void addPredator(Predator predator) {
 		predators.add(predator);
 	}
	
 	/**
 	 * Remove predators by id.
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
	public void updatePredatorPosition(int id, PointXY pos, boolean inTransition) {
		Iterator<Predator> iter = predators.iterator();
		while(iter.hasNext()) {
			Predator p = iter.next();
			if (p.getID() == id) {
				p.setPosition(pos);
				p.setInTransition(inTransition);
			}
		}
	}
	
	/**
	 * Add prey.
	 * 
	 * @param id (int)
	 * @param p (Prey)
	 */
 	public void addPrey(Prey p) {
 		this.prey.add(p);
 	}
 	
 	/**
 	 * Remove prey by id.
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
	public void updatePreyPosition(int id, PointXY pos, boolean inTransition) {
		Iterator<Prey> iter = prey.iterator();
		while(iter.hasNext()) {
			Prey p = iter.next();
			if (p.getID() == id) {
				p.setPosition(pos);
				p.setInTransition(inTransition);
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
	 * Get the shortest path in the GameState's maze between the provided start 
	 * and end points.
	 * 
	 * @param start - the start point on the Path.
	 * @param end - the end point on the Path.
	 * @return the shortest Path from start to end in the GameState's maze.
	 */
	public Path getPath(PointXY start, PointXY end) {
		return pathFinder.getPath(start, end);
	}
	
	/**
	 * Get the shortest path to the Pill closest to the given start point in the
	 * GameState's maze.
	 * 
	 * @param start - the point in the maze from which to find the closest pill.
	 * @return the shortest Path from start point to the closest pill in the 
	 * GameState's maze.
	 */
	public Path getClosestPillPath(PointXY start) {
		return pathFinder.shortestPath(start, pills);
	}

}