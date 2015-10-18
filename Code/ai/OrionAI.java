package ai;

import geometry.PointXY;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.Agent;
import logic.GameState;
import logic.Direction;
import logic.Maze;
import logic.MazeNode;
import logic.Path;
import logic.Predator;
import logic.Prey;

public class OrionAI implements AILogic {

	private double pillFactor;
	private double preyFactor;
	private double predatorFactor;
	
	private double pillDistFactor;
	private double preyDistFactor;
	private double predatorDistFactor;
	
	private Map<Direction, Double> dirWeights;
	
	private Map<Direction, Double> pillsInDirection;
	private Map<Direction, Integer> nodesInDirection;
	
	private Map<Direction, Double> preyInDirection;
	
	private Map<Direction, Double> predatorsInDirection;
	
	public OrionAI(double pillFactor, double preyFactor, double predatorFactor, 
			double pillDistFactor, double preyDistFactor, double predatorDistFactor) {
		this.pillFactor = pillFactor;
		this.preyFactor = preyFactor;
		this.predatorFactor = predatorFactor;
		
		this.pillDistFactor = pillDistFactor;
		this.preyDistFactor = preyDistFactor;
		this.predatorDistFactor = predatorDistFactor;
		
		this.dirWeights = new HashMap<Direction, Double>();
		
		this.pillsInDirection = new HashMap<Direction, Double>();
		this.nodesInDirection = new HashMap<Direction, Integer>();
		this.preyInDirection = new HashMap<Direction, Double>();
		this.predatorsInDirection = new HashMap<Direction, Double>();
		
		reset();
	}	

	/**
	 * @return the pillFactor
	 */
	public double getPillFactor() {
		return pillFactor;
	}

	/**
	 * @param pillFactor the pillFactor to set
	 */
	public void setPillFactor(double pillFactor) {
		this.pillFactor = pillFactor;
	}

	/**
	 * @return the preyFactor
	 */
	public double getPreyFactor() {
		return preyFactor;
	}

	/**
	 * @param preyFactor the preyFactor to set
	 */
	public void setPreyFactor(double preyFactor) {
		this.preyFactor = preyFactor;
	}

	/**
	 * @return the predatorFactor
	 */
	public double getPredatorFactor() {
		return predatorFactor;
	}

	/**
	 * @param predatorFactor the predatorFactor to set
	 */
	public void setPredatorFactor(double predatorFactor) {
		this.predatorFactor = predatorFactor;
	}

	/**
	 * @return the pillDistFactor
	 */
	public double getPillDistFactor() {
		return pillDistFactor;
	}

	/**
	 * @param pillDistFactor the pillDistFactor to set
	 */
	public void setPillDistFactor(double pillDistFactor) {
		this.pillDistFactor = pillDistFactor;
	}

	/**
	 * @return the preyDistFactor
	 */
	public double getPreyDistFactor() {
		return preyDistFactor;
	}

	/**
	 * @param preyDistFactor the preyDistFactor to set
	 */
	public void setPreyDistFactor(double preyDistFactor) {
		this.preyDistFactor = preyDistFactor;
	}

	/**
	 * @return the predatorDistFactor
	 */
	public double getPredatorDistFactor() {
		return predatorDistFactor;
	}

	/**
	 * @param predatorDistFactor the predatorDistFactor to set
	 */
	public void setPredatorDistFactor(double predatorDistFactor) {
		this.predatorDistFactor = predatorDistFactor;
	}

	@Override
	public void calcNextMove(List<Agent> agents, GameState state) {
		
		for (Agent agent : agents) {
			
			if (agent.isInTransition()) {
				continue;
			}
			
			if (agent instanceof Predator) {
				calcNextMovePredator(agent, state);
			} else if (agent instanceof Prey) {
				calcNextMovePrey(agent, state);
			}
		}
	}
	
	private void reset() {
		
		Direction[] dirs = Direction.values();
		
		for (int i = 0; i < dirs.length; ++i) {
			dirWeights.put(dirs[i], 0d);
			pillsInDirection.put(dirs[i], 0d);
			nodesInDirection.put(dirs[i], 0);
			preyInDirection.put(dirs[i], 0d);
			predatorsInDirection.put(dirs[i], 0d);
		}
	}
	
	private void processPills(Agent agent, GameState state) {
		
		PointXY pos = agent.getPosition();
		
		Maze maze = state.getMaze();
		Map<PointXY, MazeNode> nodes = maze.getNodes();
		Set<PointXY> mazeCoords = nodes.keySet();
		
		for (PointXY mazePos : mazeCoords) {
			Path path = state.getPath(pos, mazePos);
			List<PointXY> pathNodes = path.getPathNodes();
			if (pathNodes.size() < 2) {
				continue;
			}
			Direction dir = getDirection(pathNodes.get(0), pathNodes.get(1));
			boolean hasPill = state.hasPill(mazePos);
			if (hasPill) {
				double current = pillsInDirection.get(dir);
				current += (1.0 / (pillDistFactor * path.getLength()));
				pillsInDirection.put(dir, current);
			}
			int currentNumNodes = nodesInDirection.get(dir);
			++currentNumNodes;
			nodesInDirection.put(dir, currentNumNodes);
		}
		
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length; ++i) {
			
			int numNodes = nodesInDirection.get(dirs[i]);
			double numPills = pillsInDirection.get(dirs[i]);
			
			if (numNodes > 0) {
				double baseWeight = numPills * 1.0 / numNodes;
				double weight = dirWeights.get(dirs[i]);
				weight += baseWeight * pillFactor;
				dirWeights.put(dirs[i], weight);
			} else {
				if (dirs[i] != Direction.None) {
					// We assume there's a wall in this direction. Give it a
					// negative weight so that 'No move' is preferable to moving
					// into a wall!
					dirWeights.put(dirs[i], -Double.MAX_VALUE);
				} else {
					dirWeights.put(dirs[i], -Double.MAX_VALUE);
				}
			}
		}
		
	}
	
	private void processPrey(Agent agent, GameState state) {
		
		PointXY pos = agent.getPosition();
		
		List<Prey> allPrey = state.getPrey();
		//int numPrey = allPrey.size();
		
		for (Prey prey : allPrey) {
			if (agent.getID() == prey.getID()) {
				continue;
			}
			PointXY preyPos = prey.getPosition();
			Path path = state.getPath(pos, preyPos);
			List<PointXY> pathNodes = path.getPathNodes();
			if (pathNodes.size() < 2) {
				continue;
			}
			Direction dir = getDirection(pathNodes.get(0), pathNodes.get(1));
			
			double current = preyInDirection.get(dir);
			current += 1.0 / (preyDistFactor * path.getLength());
			preyInDirection.put(dir, current);
		}
		 
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length; ++i) {
			double baseWeight = preyInDirection.get(dirs[i]);
			double weight = dirWeights.get(dirs[i]);
			weight += baseWeight * preyFactor;
			dirWeights.put(dirs[i], weight);
		}
		
	}
	
	private void processPredators(Agent agent, GameState state) {
		
		PointXY pos = agent.getPosition();
		
		List<Predator> allPredators = state.getPredators();
		//int numPredators = allPredators.size();
		
		for (Predator predator : allPredators) {
			if (agent.getID() == predator.getID()) {
				continue;
			}
			PointXY predatorPos = predator.getPosition();
			Path path = state.getPath(pos, predatorPos);
			List<PointXY> pathNodes = path.getPathNodes();
			if (pathNodes.size() < 2) {
				continue;
			}
			Direction dir = getDirection(pathNodes.get(0), pathNodes.get(1));
			
			double current = predatorsInDirection.get(dir);
			current += 1.0 / (predatorDistFactor * path.getLength());
			predatorsInDirection.put(dir, current);
		}
		 
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length; ++i) {
			double baseWeight = predatorsInDirection.get(dirs[i]);
			double weight = dirWeights.get(dirs[i]);
			weight += baseWeight * predatorFactor;
			dirWeights.put(dirs[i], weight);
		}
		
	}
	
	private Direction getDirection(PointXY start, PointXY end) {
		
		Direction dir = Direction.None;
		if (start.getX() == end.getX() - 1) {
			dir = Direction.Right;
		} else if (start.getX() == end.getX() + 1) {
			dir = Direction.Left;
		} else if (start.getY() == end.getY() - 1) {
			dir = Direction.Up;
		} else if (start.getY() == end.getY() + 1) {
			dir = Direction.Down;
		}
		
		return dir;
	}
	
	private void calcNextMovePrey(Agent agent, GameState state) {
		
		reset();
		processPills(agent, state);
		processPrey(agent, state);
		processPredators(agent, state);
		
		double maxWeight = -Double.MAX_VALUE;
		Direction chosenDirection = Direction.None;
		Direction[] dirs = Direction.values();
		
		for (int i = 0; i < dirs.length; ++i) {
			double weight = dirWeights.get(dirs[i]);
			if (weight > maxWeight) {
				maxWeight = weight;
				chosenDirection = dirs[i];
			}
		}
		
		agent.setNextMoveDirection(chosenDirection);
	}
	
	private void calcNextMovePredator(Agent agent, GameState state) {
		
		// Find the closest prey.
		Path closestPreyPath = findClosestPreyPath(agent, state);
		
		// Use the closestPreyPath to get the direction in which to travel.
		List<PointXY> path = closestPreyPath.getPathNodes();
		if (path.size() > 1) {
			Direction dir = getDirection(path.get(0), path.get(1));
			agent.setNextMoveDirection(dir);
		}
	}
	
	private Path findClosestPreyPath(Agent agent, GameState state) {
		PointXY predatorPos = agent.getPosition();
		List<Prey> prey = state.getPrey();
		
		// Find the closest Prey.
		Path closestPreyPath = new Path();
		int closestPreyPathLength = Integer.MAX_VALUE;
		for (Prey p : prey) {
			PointXY preyPos = p.getPosition();
			Path path = state.getPath(predatorPos, preyPos);
			if (path.getLength() < closestPreyPathLength) {
				closestPreyPathLength = path.getLength();
				closestPreyPath = path;
			}
		}
		
		return closestPreyPath;
	}
	
	public String toString() {
		String out = 
				"Pill factor = " + pillFactor + System.lineSeparator() +
				"Pill dist factor = " + pillDistFactor + System.lineSeparator() +
				"Prey factor = " + preyFactor + System.lineSeparator() +
				"Prey dist factor = " + preyDistFactor + System.lineSeparator() +
				"Predator factor = " + predatorFactor + System.lineSeparator() +
				"Predator dist factor = " + predatorDistFactor + System.lineSeparator();
		return out;
	}
}
