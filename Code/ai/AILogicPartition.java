package ai;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.Agent;
import logic.Direction;
import logic.GameState;
import logic.Maze;
import logic.Path;
import logic.Predator;
import logic.Prey;

public class AILogicPartition implements AILogic {

	private Map<Agent, Set<PointXY>> partition;
	private Map<Agent, PointXY> targets;
	
	private int runFromPredDist = 5;
	private Map<Direction, Direction[]> runDirections;
	
	public AILogicPartition() {
		this.partition = new HashMap<Agent, Set<PointXY>>();
		this.targets = new HashMap<Agent, PointXY>();
		
		initialiseRunDirections();
	}
	
	public Map<Agent, Set<PointXY>> getPartition() {
		return partition;
	}
	
	private void initialiseRunDirections() {
		runDirections = new HashMap<Direction, Direction[]>();
		
		Direction[] runFromRight = new Direction[3];
		runFromRight[0] = Direction.Left;
		runFromRight[1] = Direction.Up;
		runFromRight[2] = Direction.Down;
		runDirections.put(Direction.Right, runFromRight);
		
		Direction[] runFromLeft = new Direction[3];
		runFromLeft[0] = Direction.Right;
		runFromLeft[1] = Direction.Down;
		runFromLeft[2] = Direction.Up;
		runDirections.put(Direction.Left, runFromLeft);
		
		Direction[] runFromUp = new Direction[3];
		runFromUp[0] = Direction.Down;
		runFromUp[1] = Direction.Right;
		runFromUp[2] = Direction.Left;
		runDirections.put(Direction.Up, runFromUp);
		
		Direction[] runFromDown = new Direction[3];
		runFromDown[0] = Direction.Up;
		runFromDown[1] = Direction.Left;
		runFromDown[2] = Direction.Right;
		runDirections.put(Direction.Down, runFromDown);
	}
	
	@Override
	public void calcNextMove(List<Agent> agents, GameState state) {
		
		partitionMaze(agents, state);
		
		for (Agent agent : agents) {
			if (agent instanceof Predator) {
				calcNextMovePredator(agent, state);
			} else if (agent instanceof Prey) {
				calcNextMovePrey(agent, state);
			}
		}
		
		state.setPartition(partition);
	}
	
	
	private void partitionMaze(List<Agent> agents, GameState state) {
		
		// Extract just the prey and order than by ID
		List<Prey> allPrey = new ArrayList<Prey>();
		for (Agent agent : agents) {
			if (agent instanceof Prey) {
				Prey p = (Prey) agent;
				allPrey.add(p);
			}
		}
		allPrey.sort(new AgentComparator());
		
		// Reset the partition field
		resetPartition(allPrey);
		
		// Loop through each maze square, finding the closest Agent. 
		// If there's a tie, the agent with the lowest ID is always chosen.
		Maze maze = state.getMaze();
		Set<PointXY> mazeNodes = maze.getNodes().keySet();
		for (PointXY node : mazeNodes) {
			
			int shortestPath = Integer.MAX_VALUE;
			Prey nearestPrey = null;
			for (int i = 0; i < allPrey.size(); ++i) {
				Prey prey = allPrey.get(i);
				
				PointXY preyPos = prey.getPosition();
				Path path = state.getPath(node, preyPos);
				if (path == null) {
					System.out.println("");
				}
				int length = path.getLength();
				if (length < shortestPath) {
					nearestPrey = prey;
					shortestPath = length;
				}
				
			}
			
			if (nearestPrey != null) {
				Set<PointXY> preyNodes = partition.get(nearestPrey);
				preyNodes.add(node);
				partition.put(nearestPrey, preyNodes);
			} else {
				System.err.println("AILogicPartition: something's gone wrong");
			}
		}
		
	}
	
	private void resetPartition(List<Prey> allPrey) {
		partition.clear();
		
		for (Prey prey : allPrey) {
			Set<PointXY> nodes = new HashSet<PointXY>();
			partition.put(prey, nodes);
		}
		
	}
	
	private void calcNextMovePrey(Agent agent, GameState state) {
		
		if (agent.isInTransition()) {
			return;
		}
		
		// Find the closest Predator.
		Path closestPredatorPath = findClosestPredatorPath(agent, state);
		
		// Is the predator too close? If so, run away!
		if (!closestPredatorPath.empty() &&  
					closestPredatorPath.getLength() <= runFromPredDist) {
					
			Maze maze = state.getMaze();
			setNextMoveAvoidPredator(agent, closestPredatorPath, maze);
			return;
		}
		
		// If the predator isn't close, let's go to the nearest pill in this
		// prey's partition.
		// Extract all the maze squares assigned to this prey.
		Set<PointXY> preyNodes = partition.get(agent);
		if (preyNodes != null && preyNodes.size() != 0) {
			
			// Loop over each of this prey's assigned nodes.
			// Extract those that have a pill.
			Set<PointXY> pillNodes = new HashSet<PointXY>();
			for (PointXY node : preyNodes) {
				if (state.hasPill(node) ) {
					pillNodes.add(node);
				}
			}
			
			if (pillNodes.size() != 0) {
				PointXY preyPos = agent.getPosition();
				Path closestPillPath = state.getClosestPath(preyPos, pillNodes);
					
				// Use the closestPillPath to get the direction in which to travel.
				setDirectionFromPath(agent, closestPillPath);
			} else {
				pickRandomTarget(agent, state);
			}
			
		} else {
			pickRandomTarget(agent, state);			
		}
		
	}
	
	private void pickRandomTarget(Agent agent, GameState state) {
		
		PointXY agentPos = agent.getPosition();
		
		PointXY target = targets.get(agent);
		if (target == null) {
			// Pick a random square from the maze.
			target = state.getMaze().getRandomPoint();
			targets.put(agent, target);
		}
		Path targetPath = state.getPath(agentPos, target);
		// Use the targetPath to get the direction in which to travel.
		if (!setDirectionFromPath(agent, targetPath)) {
			targets.remove(agent);
		}
		
	}
	
	private boolean setDirectionFromPath(Agent agent, Path path) {
		
		List<PointXY> pathNodes = path.getPathNodes();
		if (pathNodes.size() > 1) {
			Direction dir = getDirection(pathNodes.get(0), pathNodes.get(1));
			agent.setNextMoveDirection(dir);
			return true;
		}
		
		return false;
	}
	
	private Path findClosestPredatorPath(Agent agent, GameState state) {
		
		PointXY preyPos = agent.getPosition();
		List<Predator> predators = state.getPredators();
		
		// Find the closest Predator.
		Path closestPredPath = new Path();
		int closestPredPathLength = Integer.MAX_VALUE;
		for (Predator p : predators) {
			PointXY predatorPos = p.getPosition();
			Path path = state.getPath(preyPos, predatorPos);
			if (path.getLength() < closestPredPathLength) {
				closestPredPathLength = path.getLength();
				closestPredPath = path;
			}
		}
		
		return closestPredPath;
	}
	
	private void setNextMoveAvoidPredator(Agent agent, Path closestPredatorPath,
			Maze maze) {
		
		List<PointXY> path = closestPredatorPath.getPathNodes();
		
		// If the Predator is in our square or the path is empty, we are 
		// essentially caught so just continue what we were doing for the last
		// few moments.
		if (path.size() <= 1) {
			return;
		}
		
		// Use the first couple of points on the path to work out from which 
		// direction the Predator is coming.
		PointXY pos1 = path.get(0);
		PointXY pos2 = path.get(1);
		Direction runFromDir = getDirection(pos1, pos2);
		
		Direction dir = getPreferredMoveDirection(pos1, runFromDir, maze);
		agent.setNextMoveDirection(dir);
	}
	
	private Direction getPreferredMoveDirection(PointXY pos, 
			Direction runFromDir, Maze maze) {
		
		if (runFromDir == Direction.None) {
			return Direction.None;
		}
		
		Direction[] runDirs = runDirections.get(runFromDir);
		PointXY first = getPointFromDirection(pos, runDirs[0]);
		PointXY second = getPointFromDirection(pos, runDirs[1]);
		PointXY third = getPointFromDirection(pos, runDirs[2]);

		Direction dir;
		if (maze.areNeighbours(pos, first)) {
			dir = runDirs[0];
		} else if (maze.areNeighbours(pos, second)) {
			dir = runDirs[1];
		} else if (maze.areNeighbours(pos, third)) {
			dir = runDirs[2];
		} else {
			// We'll try this, to avoid getting stuck, even if it means getting
			// closer to the undesirable location (i.e. the Predator).
			dir = runFromDir;
		}
		return dir;
	}
	
	private PointXY getPointFromDirection(PointXY point, Direction dir) {
		
		PointXY newPoint;
		
		switch (dir) {
			case Down: {
				newPoint = new PointXY(point.getX(), point.getY() - 1);
				break;
			}
			case Left: {
				newPoint = new PointXY(point.getX() - 1, point.getY());
				break;
			}
			case Right: {
				newPoint = new PointXY(point.getX() + 1, point.getY());
				break;
			}
			case Up: {
				newPoint = new PointXY(point.getX(), point.getY() + 1);
				break;
			}
			case None:
			default: {
				newPoint = point;
				break;
			}
		}
		
		return newPoint;
	}
	
	private void calcNextMovePredator(Agent agent, GameState state) {
		
		if (agent.isInTransition()) {
			return;
		}
		
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
}

class AgentComparator implements Comparator<Agent> {

	@Override
	public int compare(Agent agent1, Agent agent2) {
		
		if (agent1.getID() < agent2.getID()) {
			return -1;
		} else if (agent1.getID() > agent2.getID()) {
			return 1;
		} else {
			return 0;
		}
		
	}
	
}
