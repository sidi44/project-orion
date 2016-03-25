package ai;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.NumberUtils;
import logic.Agent;
import logic.Direction;
import logic.GameState;
import logic.Maze;
import logic.Path;
import logic.Predator;
import logic.Prey;

/**
 * AILogicPartition class.
 * 
 * This class implements the AILogic interface. To calculate the next move for 
 * the prey, the maze is partitioned such that each maze square is assigned to 
 * the nearest prey. The prey then go to the nearest pill in their partition. 
 * 
 * If the predator is within a certain distance of a prey, the prey will assess
 * which positions in the maze are safer than its current position, then pick
 * one of those safer positions at random to move to. (This is continually 
 * re-assessed in case the movement of the predator changes the situation.) 
 * The safer positions which contain pills are prioritised over those which 
 * don't.
 * 
 * If the predator is further than the specified distance, but moving to the 
 * next square will make the prey within the distance, the prey is set to stay
 * still until it is safe to move again.
 * 
 * For the predator, the shortest path to the nearest prey is calculated and 
 * used to determine the movement of the predator.
 * 
 * @author Simon Dicken
 * @version 2015-12-28
 */
public class AILogicPartition implements AILogic {

	private Map<Agent, Set<PointXY>> partition;
	private Map<Agent, PointXY> targets;
	
	private int runFromPredDist = 5;
	private Map<Direction, Direction[]> runDirections;
	
	private Map<Agent, Set<PointXY>> saferPositions;
	
	public AILogicPartition() {
		this.partition = new HashMap<Agent, Set<PointXY>>();
		this.targets = new HashMap<Agent, PointXY>();
		
		this.saferPositions = new HashMap<Agent, Set<PointXY>>();
		
		initialiseRunDirections();
	}
	
	public Map<Agent, Set<PointXY>> getPartition() {
		return partition;
	}
	
	public Map<Agent, Set<PointXY>> getSaferPositions() {
		return saferPositions;
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
		state.setSaferPositions(saferPositions);
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
		
		// If there aren't any prey we don't need to do anything
		if (allPrey.size() == 0) {
			return;
		}
		
		// Sort the prey into Agent ID order
		Collections.sort(allPrey, new AgentComparator());
		
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
		
		// Find the closest Predator.
		Path closestPredatorPath = findClosestPredatorPath(agent, state);
		
		// Is the predator too close? If so, run away!
		if (!closestPredatorPath.empty() &&  
					closestPredatorPath.getLength() <= runFromPredDist) {
			
			setNextMoveAvoidPredator(agent, state);
			return;
		}
		
		Set<PointXY> empty = new HashSet<PointXY>();
		saferPositions.put(agent, empty);
		
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
				
				if (closestPillPath.getLength() > 1) {
					PointXY nextSquare = closestPillPath.getPathNodes().get(1);
					
					List<Predator> predators = state.getPredators();
					
					// Find the closest Predator.
					int closestPredPathLength = Integer.MAX_VALUE;
					for (Predator p : predators) {
						PointXY predatorPos = p.getPosition();
						Path path = state.getPath(nextSquare, predatorPos);
						if (path.getLength() < closestPredPathLength) {
							closestPredPathLength = path.getLength();
						}
					}
					if (closestPredPathLength <= runFromPredDist) {
						agent.setNextMoveDirection(Direction.None);
						return;
					}
				}
				
				// Use the closestPillPath to get the direction in which to 
				// travel.
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
	
	private void setNextMoveAvoidPredator(Agent agent, GameState state) {
		
		PointXY agentPos = agent.getPosition();
		
		// Find the distance to all the predators within the run from predator
		// distance.
		Map<Predator, Integer> predatorDist = findClosePredators(agent, state);
		if (predatorDist.size() == 0) {
			// This method should only be called when at least one predator is
			// close by - something's gone wrong.
			return;
		}
		
		PointXY target = targets.get(agent);
		boolean reevaluatePath = false;
		if (target != null) {
			// Check whether the current target is still reasonable (i.e. if the
			// predator has moved onto or close to the target path, we should 
			// find a new path.)
			Path targetPath = state.getPath(agentPos, target);
			if (pathTooCloseToPredators(targetPath, predatorDist, state)) {
				reevaluatePath = true;
			}
		} 
		
		// The current target node is ok, so set the move using that.
		if (!reevaluatePath && target != null) {
			setMoveFromTarget(agent, state);
			return;
		}
		
		// Either the current target now involves going too close to a predator
		// or no target exists yet. Either way, we should find a new target.
		
		// The list of positions in the maze which we can get too without 
		// getting any closer to the nearby predators.
		List<PointXY> saferPositions = 
				findSaferPositions(agentPos, state, predatorDist);
			
		// Add the data to the member variable (currently only used for 
		// debugging).
		Set<PointXY> safe = new HashSet<PointXY>();
		safe.addAll(saferPositions);
		this.saferPositions.put(agent, safe);
		
		if (saferPositions.size() == 0) {
			// We're in the safest possible position...for now!
			agent.setNextMoveDirection(Direction.None);
			return;
		}
		
		// Find a new target
		Set<Predator> closePredators = predatorDist.keySet();
		findNewTarget(agent, state, saferPositions, closePredators);
		
		// Set the move using the target
		setMoveFromTarget(agent, state);
	}
	
	private Map<Predator, Integer> findClosePredators(Agent agent, 
			GameState state) {
		
		PointXY agentPos = agent.getPosition();
		
		List<Predator> allPredators = state.getPredators();
		Map<Predator, Integer> predatorDist = new HashMap<Predator, Integer>();
		
		for (Predator predator : allPredators) {
			PointXY predatorPos = predator.getPosition();
			Path path = state.getPath(agentPos, predatorPos);
			Integer length = path.getLength();
			if (length <= runFromPredDist) {
				predatorDist.put(predator, length);
			}
		}
		
		return predatorDist;
	}
	
	private void setMoveFromTarget(Agent agent, GameState state) {
		
		PointXY agentPos = agent.getPosition();
		
		PointXY target = targets.get(agent);
		
		Path targetPath = state.getPath(agentPos, target);
		// Use the targetPath to get the direction in which to travel.
		if (!setDirectionFromPath(agent, targetPath)) {
			targets.remove(agent);
		}
	}
	
	private boolean pathTooCloseToPredators(Path path, 
			Map<Predator, Integer> predatorDist, GameState state) {
		
		Set<Predator> closePredators = predatorDist.keySet();
		List<PointXY> pathNodes = path.getPathNodes();
		PointXY startPos = pathNodes.get(0);
		
		boolean tooClose = false;
		for (Predator predator : closePredators) {
			PointXY predatorPos = predator.getPosition();
			for (PointXY pathNode : pathNodes) {
				if (pathNode.equals(startPos)) {
					continue;
				}
				int dist = state.getPath(pathNode, predatorPos).getLength();
				int currentDist = predatorDist.get(predator);
				if (dist <= currentDist) {
					tooClose = true;
					break;
				}
			}
			if (tooClose) {
				break;
			}
		}
		
		return tooClose;
	}
	
	private List<PointXY> findSaferPositions(PointXY agentPos, GameState state, 
			Map<Predator, Integer> predatorDist) {
		
		List<PointXY> saferPositions = new ArrayList<PointXY>();
		
		Maze maze = state.getMaze();
		Set<PointXY> allPositions = maze.getNodes().keySet();
		
		for (PointXY pos : allPositions) {
			Path path = state.getPath(agentPos, pos);
			
			boolean tooClose = 
					pathTooCloseToPredators(path, predatorDist, state);
			if (!tooClose) {
				saferPositions.add(pos);
			}
		}
		
		return saferPositions;
	}
	
	private void findNewTarget(Agent agent, GameState state, 
			List<PointXY> saferPositions, Set<Predator> closePredators) {
		
		List<PointXY> pickFromPositions = new ArrayList<PointXY>();
		
		Set<PointXY> pills = state.getPills();
		for (PointXY safePos : saferPositions) {
			if (pills.contains(safePos)) {
				pickFromPositions.add(safePos);
			}
		}
		
		if (pickFromPositions.size() == 0) {
			pickFromPositions.addAll(saferPositions);
		}
		
		int furthestDist = -1;
		for (int i = 0; i < 1; ++i) {
			// Pick a random point from the list of safer positions
			int index = NumberUtils.randomInt(0, pickFromPositions.size() - 1);
			PointXY newPos = pickFromPositions.get(index);
			int closestPredatorDist = Integer.MAX_VALUE;
			for (Predator predator : closePredators) {
				PointXY predatorPos = predator.getPosition();
				Path path = state.getPath(predatorPos, newPos);
				int pathLength = path.getLength();
				if (pathLength < closestPredatorDist) {
					closestPredatorDist = pathLength;
				}
			}
			if (closestPredatorDist > furthestDist) {
				furthestDist = closestPredatorDist;
				targets.put(agent, newPos);
			}
		}
		
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
