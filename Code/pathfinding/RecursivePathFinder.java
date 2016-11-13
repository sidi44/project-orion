package pathfinding;

import geometry.PointXY;
import geometry.PointXYPair;
import logic.Maze;
import logic.MazeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * PathFinder class.
 * 
 * This class is primarily used to find the shortest path between points or the
 * shortest point from a start point to the closest point in a set of 'goal' 
 * points.
 * 
 * If generateAllPaths() is called, the shortest path from every node in the 
 * maze to every other node will be calculated and stored. The stored paths will 
 * be used when getPath(start, end) is called to return the path in constant 
 * time. If generateAllPaths() has not been called, getPath(start, end) will 
 * do the shortest path calculation at this time.
 * 
 * Note that generateAllPaths() may take time to complete, particularly for 
 * large mazes.
 * 
 * @author Simon Dicken
 * @version 2015-12-28
 */
class RecursivePathFinder implements PathFinder {

	// The maze we're searching
	private Maze maze;
	
	// These variables are used in the shortest path calculations 
	private Map<PointXY, Integer> nodeCosts;
	private int shortestPath;
	
	// Store whether a path exists from a start node to other nodes in the maze
	private Map<PointXY, Set<PointXY>> pathExists;
	
	// The collection of all shortest paths between pairs of points in the maze
	private Map<PointXYPair, Path> allPaths;
	
	/**
	 * Constructor for PathFinder.
	 * 
	 * @param maze - the maze in which to find paths.
	 */
	public RecursivePathFinder(Maze maze) {
		this.maze = maze;
		
		reset();
		
		initialisePathExists();
		
		allPaths = new HashMap<PointXYPair, Path>();
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
		
		// Add each point to the map, with the corresponding set just containing
		// that point initially.
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
	private Path shortestPath(PointXY start, PointXY end) {

		// Call the overloaded method to do the work
		Set<PointXY> goals = new HashSet<PointXY>();
		goals.add(end);
		Path path = shortestPath(start, goals);
		
		return path;
	}
	
	/**
	 * Calculate the shortest path between the provided start point and the 
	 * closest point found in the provided set of goal points.
	 * 
	 * If no such path exists, an empty Path is returned.
	 * 
	 * @param start - the starting point in the shortest path.
	 * @param goals - the set of goal point for the shortest path.
	 * @return the shortest path from the provided start point to the closest
	 * point in the set of goal points provided.
	 */
	private Path shortestPath(PointXY start, Set<PointXY> goals) {
		reset();
		
		Path path = createPath();
		shortestPath(start, goals, 0, path);
		
		// The returned shortest path is from the goal to the start, so we need  
		// to reverse it.
		path.reverse();
		
		return path;
	}
	
	/**
	 * Recursive method to find the shortest path between a start point and 
	 * the closest point in a set of goal points.
	 * 
	 * @param start - the starting point in the shortest path. 
	 * @param goals - the set of goal points for the shortest path.
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
	 * Generates (and stores) the shortest path from every point in the maze to 
	 * every other point.
	 * 
	 * (This may take some time for larger mazes!)
	 */
	public void generateAllPaths() {
		
		Set<PointXY> mazePoints = pathExists.keySet();
		
		for (PointXY point : mazePoints) {
			
			while (!allPathsExist(point)) {
				// Find a path from this point to another point in the maze 
				// which we haven't calculated yet.
				Path p = findNewPath(point);
				
				// Add this path (and all sub-paths) to our stored paths.
				addToAllPaths(p);
			}
		}
		
//		if (allPathsExist()) {
//			System.out.println("All paths exist!");
//		} else {
//			System.out.println("Not all paths exist!");
//		}		
	}
	
	/**
	 * Add the provided path and all of its sub-paths to the stored paths.
	 * All sub-paths are added i.e. the path from each pair of points 
	 * 
	 * @param path
	 */
	private void addToAllPaths(Path path) {

		// Find the sub path between each pair of nodes and add it to allPaths
		for (int i = 0; i < path.getLength(); ++i) {
			Set<PointXY> pathToNodes = pathExists.get(path.getPoint(i));
			for (int j = i; j < path.getLength(); ++j) {
				
				// Find the sub path
				PointXY start = path.getPoint(i);
				PointXY end = path.getPoint(j);
				Path subPath = path.subPath(start, end);
				
				// Add the sub path to allPaths if there is not one already
				PointXYPair pair = new PointXYPair(start, end);
				if (!allPaths.containsKey(pair)) {
					allPaths.put(pair, subPath);
					pathToNodes.add(end);
				}
				
				// Also store the reverse path
				Set<PointXY> pathToNodesJ = pathExists.get(path.getPoint(j));
				Path reverseSubPath = subPath.deepCopy();
				reverseSubPath.reverse();
				
				PointXYPair reversePair = new PointXYPair(end, start);
				if (!allPaths.containsKey(reversePair)) {
					allPaths.put(reversePair, reverseSubPath);
					pathToNodesJ.add(start);
				}				
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
			if (!allPathsExist(point)) {
				return false;
			}
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
		if (pathsToNode.size() != numNodes) {
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
		
		// Find the furthest (Euclidean distance) point from the start point in 
		// the set of not visited nodes.
		PointXY end = furthestPoint(start, notVisited);
		
		// Get the shortest path between the start and end nodes.
		Path path = shortestPath(start, end);
		
		return path;
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
	 * will obtain the path efficiently by looking up the path in stored paths.
	 * If generateAllPaths() has not been called, this method will do the 
	 * shortest path calculation between the provided nodes at this time.
	 * 
	 * @param start - the first point in the shortest path to find.
	 * @param end - the last point in the shortest path to find.
	 * @return the shortest path from the provided start point to the provided 
	 * end point.
	 */
	public Path getPath(PointXY start, PointXY end) {
		
		Path path = createPath();
		
		if (!allPaths.isEmpty()) {
			PointXYPair pair = new PointXYPair(start, end);
			path = allPaths.get(pair);
		} else {
			path = shortestPath(start, end);
		}
		
		return path;
	}
	
	/**
	 * Get the shortest path from the provided start point to the provided set 
	 * of 'goal' points
	 * 
	 * If generateAllPaths() has been called prior to this method, this is 
	 * will obtain the path efficiently by looking each path in the stored paths
	 * and returning the shortest path.
	 * If generateAllPaths() has not been called, this method will do the 
	 * shortest path calculation for each pair of points before determining the
	 * shortest path.
	 * 
	 * @param start - the first point in the shortest path to find.
	 * @param goals - the set of goal points to find the shortest path too.
	 * @return the shortest path from the provided start point to the closest 
	 * point in the provided set of goal points.
	 */
	public Path getPath(PointXY start, Set<PointXY> goals) {
		
		Path path = createPath();
		
		if (!allPaths.isEmpty()) {
			int shortestPath = Integer.MAX_VALUE;
			
			for (PointXY goal : goals) {
				PointXYPair pair = new PointXYPair(start, goal);
				Path p = allPaths.get(pair);
				int length = p.getLength();
				if (length < shortestPath) {
					path = p;
					shortestPath = length;
				}
			}
			
		} else {
			path = shortestPath(start, goals);
		}
		
		return path;
	}
	
	private Path createPath() {
		return new PointXYPath();
	}
	
	@Override
	public float getProgress() {
		int total = maze.getNodes().size() * maze.getNodes().size();
		
		int current = allPaths.size();
		
		float progress = current * 100f / total;
		return progress;
	}

	@Override
	public void run() {
		generateAllPaths();
	}

} 


