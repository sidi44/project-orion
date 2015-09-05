package ai;

import geometry.PointXY;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.Agent;
import logic.Direction;
import logic.GameState;
import logic.Maze;
import logic.Predator;
import logic.Prey;

/**
 * AILogicSimple class.
 * 
 * This class implements the AILogic interface and provides a simple AI for 
 * calculating the next move of Predator and Prey agents.
 * 
 * For the Predator, the shortest path to the nearest Prey Agent is used to 
 * select the next direction.
 * 
 * For the Prey, if the shortest path to the nearest Predator is below a certain
 * threshold, the Prey does it's best to run from the Predator by trying to move
 * in the opposite direction, or if that direction is a wall, one of the other
 * two directions other than the Predator direction.
 * If the Predator is not within the threshold, the Prey calculates the shortest
 * path to the nearest Pill and heads in that direction.
 * 
 * @author Simon Dicken
 * @version 2015-05-31
 */
public class AILogicSimple implements AILogic {

	private int runFromPredDist = 5;
	private Map<Direction, Direction[]> runDirections;
	
	public AILogicSimple() {
		initialiseRunDirections();
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
		
		for (Agent agent : agents) {
			if (agent instanceof Predator) {
				calcNextMovePredator(agent, state);
			} else if (agent instanceof Prey) {
				calcNextMovePrey(agent, state);
			}
		}
		
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
	
	private void calcNextMovePrey(Agent agent, GameState state) {
		
		if (agent.isInTransition()) {
			return;
		}
		
		// Find the closest Predator.
		Path closestPredatorPath = findClosestPredatorPath(agent, state);
		
		// Find the closest Pill.
		Path closestPillPath = findClosestPillPath(agent, state);
		
		// Is the predator too close? If so, run away! If not, head for a pill.
		if (!closestPredatorPath.empty() &&  
			closestPredatorPath.getLength() <= runFromPredDist) {
			
			setNextMoveAvoidPredator(agent, closestPredatorPath, 
					state.getMaze());
			
		} else {
			// Use the closestPillPath to get the direction in which to travel.
			List<PointXY> path = closestPillPath.getPathNodes();
			if (path.size() > 1) {
				Direction dir = getDirection(path.get(0), path.get(1));
				agent.setNextMoveDirection(dir);
			}
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

	private Path findClosestPillPath(Agent agent, GameState state) {
		PointXY preyPos = agent.getPosition();
		return state.getClosestPillPath(preyPos);
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
