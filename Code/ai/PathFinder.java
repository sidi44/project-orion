package ai;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.Maze;
import logic.MazeNode;

/**
 * PathFinder class.
 * 
 * This class is primarily used to find the shortest path between points or the
 * shortest point from a start point to the closest point in a set of 'goal' 
 * points.
 * 
 * If generateAllPaths() is called, the getPath(start, end) will (probably) be
 * more efficient for longer paths. Note that generateAllPaths() may take time 
 * to complete, particularly for large mazes.
 * generateAllPaths() does not calculate a path from every node to every other 
 * node in the maze, but rather calculates a collections of paths such that at
 * least one path exists which contains each pair of points in the maze.
 * 
 * @author Simon Dicken
 * @version 2015-05-31
 */
public class PathFinder {

	private Maze maze;
	
	private Map<PointXY, Integer> nodeCosts;
	private int shortestPath;
	
	private Map<PointXY, Set<PointXY>> pathExists;
	
	private List<Path> allPaths;
	
	/**
	 * Constructor for PathFinder.
	 * 
	 * @param maze - the 
	 */
	public PathFinder(Maze maze) {
		this.maze = maze;
		
		reset();
		
		initialisePathExists();
		
		allPaths = new ArrayList<Path>();
	}
	
	/**
	 * Reset the variables used in the shortest path calculations.
	 */
	private void reset() {
		this.shortestPath = Integer.MAX_VALUE;
		
		Set<PointXY> nodes = maze.getNodes().keySet();
		nodeCosts = new HashMap<PointXY, Integer>();
		
		for (PointXY point : nodes) {
			nodeCosts.put(point, Integer.MAX_VALUE);
		}
	}
	
	/**
	 * Initialise the pathExists field variable which is used when generating
	 * allPaths.
	 */
	private void initialisePathExists() {
		pathExists = new HashMap<PointXY, Set<PointXY>>();
		
		Set<PointXY> nodes = maze.getNodes().keySet();
		for (PointXY point : nodes) {
			Set<PointXY> pathsToNodes = new HashSet<PointXY>();
			pathsToNodes.add(point);
			pathExists.put(point, pathsToNodes);
		}
	}
	
	/**
	 * Calculate the shortest path between the provided start and end points for
	 * the PathFinder's maze.
	 * 
	 * If no such path exists, an empty Path is returned.
	 * 
	 * @param start - the starting point in the shortest path.
	 * @param end - the goal point for the shortest path.
	 * @return the shortest path from the provided start and end points.
	 */
	public Path shortestPath(PointXY start, PointXY end) {
		reset();
		
		Path path = new Path();
		Set<PointXY> goals = new HashSet<PointXY>();
		goals.add(end);
		shortestPath(start, goals, 0, path);
		path.reversePath();
		
		return path;
	}
	
	/**
	 * Calculate the shortest path between the provided start point and the 
	 * first (closest) point found in the provided set of goal points.
	 * 
	 * If no such path exists, an empty Path is returned.
	 * 
	 * @param start - the starting point in the shortest path.
	 * @param goals - the set of goal point for the shortest path.
	 * @return the shortest path from the provided start point to the closest
	 * point in the set of goal points provided.
	 */
	public Path shortestPath(PointXY start, Set<PointXY> goals) {
		reset();
		
		Path path = new Path();
		shortestPath(start, goals, 0, path);
		path.reversePath();
		
		return path;
	}
	
	/**
	 * Recursive method to find the shortest path between a start point and 
	 * the closest point in a set of goal points.
	 * 
	 * @param start - the starting point in the shortest path. 
	 * @param goals - the set of goal point for the shortest path.
	 * @param pathLength - the current length of the path.
	 * @param path - the Path to populate with the points along the shortest 
	 * path. NOTE: This will be the reverse path from goal back to start.
	 * @return true if a shortest path was found, false otherwise.
	 */
	private boolean shortestPath(PointXY start, Set<PointXY> goals, 
			int pathLength, Path path) {
		
		// Check if we've been to this node before on a shorter or equal path.
		if (pathLength >= nodeCosts.get(start)) {
			return false;
		} else {
			nodeCosts.put(start, pathLength);
		}
		
		// Check if we're at our goal.
		if (goals.contains(start)) {
			shortestPath = pathLength;
			path.clear();
			path.addToEnd(start);
			return true;
		}
		
		// Check if we've already found a shorter path elsewhere in the maze.
		if (pathLength >= shortestPath) {
			return false;
		}
		
		// Explore all the neighbouring nodes.
		Map<PointXY, MazeNode> nodes = maze.getNodes();
		MazeNode startNode = nodes.get(start);
		Set<PointXY> neighbours = startNode.getNeighbours();
		
		boolean success = false;
		for (PointXY pos : neighbours) {
			boolean output = shortestPath(pos, goals, pathLength + 1, path);
			success = success || output;
		}
		
		// If this node was found to be on the shortest path, add it to the
		// path object.
		if (success) {
			path.addToEnd(start);
		}
		
		return success;
	}
	
	/**
	 * Generates a collection of paths such that there is at least one path 
	 * which contains each pair of points in the maze.
	 */
	public void generateAllPaths() {
		
		Set<PointXY> mazePoints = pathExists.keySet();
		
		for (PointXY point : mazePoints) {
			
			while (!allPathsExist(point)) {
				Path p = findNewPath(point);
				if (!p.empty()) {
					addToPathExists(p);
					allPaths.add(p);
				}
			}
		}
		
//		if (allPathsExist()) {
//			System.out.println("All paths exist!");
//		} else {
//			System.out.println("Not all paths exist!");
//		}
		
	}
	
	/**
	 * Populates the pathExists field variable with information in the given 
	 * Path. (i.e. every pair of points in the given Path is added to the 
	 * pathExists map).
	 * 
	 * @param path
	 */
	private void addToPathExists(Path path) {
		
		List<PointXY> pathNodes = path.getPathNodes();
		
		for (int i = 0; i < pathNodes.size(); ++i) {
			Set<PointXY> pathToNodes = pathExists.get(pathNodes.get(i));
			for (int j = i + 1; j < pathNodes.size(); ++j) {
				pathToNodes.add(pathNodes.get(j));
			}
		}
		
	}
	
	/**
	 * Checks whether a path exists between every pair of points in the maze.
	 * 
	 * @return true if a path exists between every pair of points in the maze,
	 * false otherwise.
	 */
	@SuppressWarnings("unused")
	private boolean allPathsExist() {
		
		Set<PointXY> points = pathExists.keySet();
		for (PointXY point : points) {
			allPathsExist(point);
		}
		
		return true;
	}
	
	/**
	 * Checks whether a path exists to every other point in the maze for the 
	 * given point.
	 * 
	 * @param point - the point to check
	 * @return true if a path exists from this point to every other point in 
	 * the maze, false otherwise.
	 */
	private boolean allPathsExist(PointXY point) {
		
		int numNodes = maze.getNodes().size();
		
		Set<PointXY> pathsToNode = pathExists.get(point);
		if (pathsToNode.size() < numNodes) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Finds a path from the given point to a point in the maze for which there
	 * does not currently exist a path from the start point.
	 * 
	 * @param start - the point for which to find a new path.
	 * @return a Path from the given start point to another point in the maze
	 * for which there does not currently exist a path from the start point.
	 */
	private Path findNewPath(PointXY start) {
		
		// Get the set of points not visited by a path through the start node.
		Set<PointXY> notVisited = new HashSet<PointXY>();
		notVisited.addAll(maze.getNodes().keySet());
		Set<PointXY> pathsToNodes = pathExists.get(start);
		notVisited.removeAll(pathsToNodes);
		
		PointXY end = furthestPoint(start, notVisited);
		
		Path p = shortestPath(start, end);
		
		return p;
	}
	
	/**
	 * Finds the point in the provided set of points which is has the greatest 
	 * Euclidean distance from the given start point.
	 * 
	 * @param start - the point from which to find the furthest point.
	 * @param allPoints - the set of all points to look in for the furthest 
	 * point.
	 * @return the point with the greatest Euclidean distance from the provided
	 * set of all points.
	 */
	private PointXY furthestPoint(PointXY start, Set<PointXY> allPoints) {
		
		double maxDistance = 0;
		PointXY end = null;
		
		for (PointXY point : allPoints) {
			double distance = start.getDistance(point);
			if (distance > maxDistance) {
				maxDistance = distance;
				end = point;
			}
		}
		
		return end;
	}
	
	/**
	 * Get the shortest path from the provided start point to the provided end
	 * point.
	 * 
	 * If generateAllPaths() has been called prior to this method, this is 
	 * (probably) more efficient than calling shortestPath() for long paths.
	 * If generateAllPaths() has not been called, this method simply returns the
	 * result of shortestPath()
	 * 
	 * @param start - the first point in the shortest path to find.
	 * @param end - the last point in the shortest path to find.
	 * @return the shortest path from the provided start point to the provided 
	 * end point.
	 */
	public Path getPath(PointXY start, PointXY end) {
		
		Path path = new Path();
		
		if (!allPaths.isEmpty()) {
			for (Path p : allPaths) {
				if (p.contains(start) && p.contains(end)) {
					path = p.subPath(start, end);
					break;
				}
			}
		} else {
			path = shortestPath(start, end);
		}
		
		return path;
	}
	
} 
