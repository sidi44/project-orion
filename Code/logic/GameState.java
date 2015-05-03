package logic;

import java.util.ArrayList;
import java.util.List;

public class GameState {

	private Maze maze;
	private List<Predator> predators;
	private List<Prey> prey;
	
	public GameState() {
		predators = new ArrayList<Predator>();
		prey = new ArrayList<Prey>();
	}
	
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	public void addPredator(Predator predator) {
		predators.add(predator);
	}
	
	public void addPrey(Prey prey) {
		this.prey.add(prey);
	}	
	
	public Maze getMaze() {
		return maze;
	}
	
	public List<Predator> getPredators() {
		return predators;
	}
	
	public List<Prey> getPrey() {
		return prey;
	}
	
}
