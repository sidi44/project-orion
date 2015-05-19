package logic;

import geometry.PointXY;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameState {
	
	private Maze maze;
	private Set<PointXY> pills;
	private Map<Integer, Predator> pred;
	private Map<Integer, Prey> prey;
	private Map<PointXY, Powerup> powers;

	public GameState(Maze maze, Map<Integer, Predator> pred, Map<Integer, Prey> prey,
			Set<PointXY> pills, Map<PointXY, Powerup> powers) {
		this.maze = maze;
		this.pred = pred;
		this.prey = prey;
		this.pills = pills;
		this.powers = powers;
	}

	public Maze getMaze() {
		return this.maze;
	}

	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	public Set<PointXY> getPills() {
		return this.pills;
	}

	public void setPills(Set<PointXY> pills) {
		this.pills = pills;
	}

	public Map<Integer, Predator> getPred() {
		return this.pred;
	}

	public void setPred(Map<Integer, Predator> pred) {
		this.pred = pred;
	}

	public Map<Integer, Prey> getPrey() {
		return this.prey;
	}

	public void setPrey(Map<Integer, Prey> prey) {
		this.prey = prey;
	}

	public Map<PointXY, Powerup> getPowers() {
		return this.powers;
	}

	public void setPowers(Map<PointXY, Powerup> powers) {
		this.powers = powers;
	}
	
	public Map<Integer, Agent> getAgents() {
		Map<Integer, Agent> agents = new HashMap<Integer, Agent>();
		agents.putAll(pred);
		agents.putAll(prey);
		return agents;
	}
	
	public void removePill(PointXY pos) {
		if (pills.contains(pos)) {
			pills.remove(pos);
		}
	}
	
 	public void addPredator(int id, Predator predator) {
 		if (!pred.containsKey(id)) {
 			pred.put(id, predator);
 		}
 	}
	
	public void removePredator(int id) {
		if (pred.containsKey(id)) {
			pred.remove(id);
		}
	}
	
	public void updatePredatorPosition(int id, PointXY pos) {
		if (pred.containsKey(id)) {
			Predator p = pred.get(id);
			p.setPosition(pos);
		}
	}
	
 	public void addPrey(int id, Prey p) {
 		if (!prey.containsKey(id)) {
 			prey.put(id, p);
 		}
 	}
 	
	public void removePrey(int id) {
		if (prey.containsKey(id)) {
			prey.remove(id);
		}
	}
	
	public void updatePreyPosition(int id, PointXY pos) {
		if (prey.containsKey(id)) {
			Prey p = prey.get(id);
			p.setPosition(pos);
		}
	}
	
}
