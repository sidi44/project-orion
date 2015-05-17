package logic;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.Iterator;
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
	
	public List<Agent> getAgents() {
		List<Agent> agents = new ArrayList<Agent>();
		agents.addAll(predators);
		agents.addAll(prey);
		return agents;
	}
	
	public void removePill(PointXY pos) {
		MazeNode node = maze.getNode(pos);
		node.setPill(false);
	}
	
	public void removePredator(int id) {
		Iterator<Predator> iter = predators.iterator();
		while(iter.hasNext()) {
			Predator p = iter.next();
			if (p.getID() == id) {
				iter.remove();
			}
		}
	}
	
	public void removePrey(int id) {
		Iterator<Prey> iter = prey.iterator();
		while(iter.hasNext()) {
			Prey p = iter.next();
			if (p.getID() == id) {
				iter.remove();
			}
		}
	}
	
	public void updatePredatorPosition(int id, PointXY pos) {
		Iterator<Predator> iter = predators.iterator();
		while(iter.hasNext()) {
			Predator p = iter.next();
			if (p.getID() == id) {
				p.setPosition(pos);
			}
		}
	}
	
	public void updatePreyPosition(int id, PointXY pos) {
		Iterator<Prey> iter = prey.iterator();
		while(iter.hasNext()) {
			Prey p = iter.next();
			if (p.getID() == id) {
				p.setPosition(pos);
			}
		}
	}
	
}
