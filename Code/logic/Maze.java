package logic;

import geometry.PointXY;
import geometry.PolygonShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import utils.NumberUtils;

/**
 * Generates a maze which is composed of numerous MazeNodes.
 * 
 * @author Martin Wong
 * @version 2015-06-01
 */
public class Maze {
	
	private Map<PointXY, MazeNode> nodes; // The maze
	private PolygonShape dimensions; // Dimensions of the maze
	private MazeConfig mConfig; // Configurations for setting up the maze
	
	private List<PointXY> deadends; // Records the deadends in the maze
	private List<PointXY> filled; // Records the nodes which are part of paths
	private List<PointXY> squares;
	
	/**
	 * Creates an instance of a Maze with default configurations.
	 * 
	 * @param dimensions (Rectangle)
	 */
	public Maze(PolygonShape dimensions) {
		this.dimensions = dimensions;
		configureDefault();
		buildMaze();
	}
	
	/**
	 * Creates an instance of a maze based on custom configurations.
	 * 
	 * @param dimensions (Rectangle)
	 * @param mConfig (MazeConfig)
	 */
	public Maze(PolygonShape dimensions, MazeConfig mConfig) {
		this.dimensions = dimensions;
		this.mConfig = mConfig;
		buildMaze();
	}
	
	/**
	 * Sets up the default configurations for the maze.
	 */
	private void configureDefault() {
		int rows = dimensions.getMaxX() - dimensions.getMinX() + 1;
		int columns = dimensions.getMaxY() - dimensions.getMinY() + 1;
		
		int maxLength = (rows * columns) - 1; // Maximum path length
		double deadEndMinProp = 0.02; // Minimum deadend nodes / total nodes
		double ranPathMaxProp = 0.8; // Maximum filled nodes / total nodes
		int loopLimit = 30; // Maximum number of loops before stopping
		
		this.mConfig = new MazeConfig(maxLength, loopLimit, deadEndMinProp, ranPathMaxProp);
	}
	
	/**
	 * Builds the maze: nodes (Map<PointXY, MazeNode>)
	 */
	private void buildMaze() {
		this.nodes = new HashMap<PointXY, MazeNode>();
		this.deadends = new ArrayList<PointXY>();
		this.filled = new ArrayList<PointXY>();
		this.squares = new ArrayList<PointXY>();
		
		createEmptyMaze();
		addInitialPath();
		fillRandom();
		fillIterate();
		appendDeadends();
		removeSquares();
		
		// Checks that all nodes are used.
		try {
			if (this.nodes.size() != this.filled.size()) throw new Exception("Illegal Maze: nodes not all filled.");
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}
	
	/**
	 * Creates an empty maze (fills in nodes with MazeNodes).
	 * Each MazeNode would have a position but no neighbours.
	 */
	private void createEmptyMaze() {
		PointXY position = null;
		MazeNode node = null;
		
		for(int j = dimensions.getMinY(); j <= dimensions.getMaxY(); j++) {
			for (int i = dimensions.getMinX(); i <= dimensions.getMaxX(); i++) {
				position = new PointXY(i, j);
				
				if (withinDimensions(position)) {
					node = new MazeNode(new HashSet<PointXY>());
					nodes.put(position, node);
				}
			}
		}
	}
	
	/**
	 * Adds the initial pat to the maze from a random starting point.
	 */
	private void addInitialPath() {
		PointXY startingPoint = getRandomPoint();
		filled.add(startingPoint);
		buildPathRandom(); // Build a path from the starting point
		deadends.add(startingPoint); // At the starting point to deadends
	}
	
	/**
	 * Creates paths from any random point that is currently a part of the path.
	 */
	private void fillRandom() {
		double nSize = nodes.size();
		int counter = 0;
		
		// Only build random paths if: proportion is under ranPathMaxProp and counter is under loopLimit
		while ((filled.size() / nSize) <= mConfig.getRanPathMaxProp() && counter < mConfig.getLoopLimit()) {
			buildPathRandom();
			counter++;
		}
	}
	
	/**
	 * Creates paths from empty MazeNodes by iterating through the maze.
	 * Iterate in every direction starting from the initial starting point.
	 */
	private void fillIterate() {
		PointXY startingPoint = getRandomPathPosition();
		PointXY emptyPos = null;
		
		// Checks lower left of starting point
		for (int j = (int) startingPoint.getY(); j >= dimensions.getMinY(); j--) {
			for (int i = (int) startingPoint.getX(); i >= dimensions.getMinX(); i--) {
				emptyPos = new PointXY(i, j);
				
				if (withinDimensions(emptyPos) && !filled.contains(emptyPos)) {
					buildPathFromPoint(emptyPos, false);
				}
			}
		}
		
		// Checks lower right of starting point
		for (int j = (int) startingPoint.getY(); j >= dimensions.getMinY(); j--) {
			for (int i = (int) startingPoint.getX(); i <= dimensions.getMaxX(); i++) {
				emptyPos = new PointXY(i, j);
				
				if (withinDimensions(emptyPos) && !filled.contains(emptyPos)) {
					buildPathFromPoint(emptyPos, false);
				}
			}
		}
		
		// Checks upper left of starting point
		for (int j = (int) startingPoint.getY(); j <= dimensions.getMaxY(); j++) {
			for (int i = (int) startingPoint.getX(); i >= dimensions.getMinX(); i--) {
				emptyPos = new PointXY(i, j);
				
				if (withinDimensions(emptyPos) && !filled.contains(emptyPos)) {
					buildPathFromPoint(emptyPos, false);
				}
			}
		}
		
		// Checks upper right of starting point
		for (int j = (int) startingPoint.getY(); j <= dimensions.getMaxY(); j++) {
			for (int i = (int) startingPoint.getX(); i <= dimensions.getMaxX(); i++) {
				emptyPos = new PointXY(i, j);
				
				if (withinDimensions(emptyPos) && !filled.contains(emptyPos)) {
					buildPathFromPoint(emptyPos, false);
				}
			}
		}
		
		
	}
	
	/**
	 * Join deadends to existing paths based on the deadEndMinProp value.
	 */
	private void appendDeadends() {
		double nSize = nodes.size();
		int randomPos = 0;
		PointXY dEnd = null;
		
		// Only join deadends if over the deadEndMinProp
		while ((deadends.size() / nSize) >= mConfig.getDeadEndMinProp() && deadends.size() > 0) {
			randomPos = NumberUtils.randomInt(0, deadends.size() - 1);
			dEnd = deadends.get(randomPos); // Get a random deadend
			buildPathFromPoint(dEnd, true);
		}
	}
	
	/**
	 * Removes paths formed by a square of adjacent nodes (minimal square),
	 * e.g. (0, 0), (0, 1), (1, 1), (1, 0). It does this by inspecting the area
	 * around deadends.
	 */
	private void removeSquares() {
		PointXY[] quadrants = null;
		PointXY ll_ll = null;
		PointXY ul_ll = null;
		PointXY ur_ll = null;
		PointXY lr_ll = null;
		int random = 0;
		
		while (squares.size() > 0) {
			random = NumberUtils.randomInt(0, squares.size() - 1);
			
			ur_ll = squares.get(random);
			squares.remove(random);
			
			lr_ll = new PointXY (ur_ll.getX(), ur_ll.getY() - 1);
			ll_ll = new PointXY (ur_ll.getX() - 1, ur_ll.getY() - 1);
			ul_ll = new PointXY (ur_ll.getX() - 1, ur_ll.getY());
			
			quadrants = new PointXY[]{ll_ll, lr_ll, ul_ll, ur_ll};
			
			for (PointXY ll : quadrants) {
				if (isSquare(ll)) {
					removeAndMaintain(ll);
				}
			}
		}
	}
	
	/**
	 * Gets a random node in maze.
	 * 
	 * @return randomPoint (PointXY)
	 */
	public PointXY getRandomPoint() {
		List<PointXY> keys = new ArrayList<PointXY>(nodes.keySet());
		int random = NumberUtils.randomInt(0, keys.size() - 1);
		PointXY randomPoint = keys.get(random);
		
		return randomPoint;
	}
	
	/**
	 * Builds a random path from any point of the current path.
	 */
	private void buildPathRandom() {
		buildPathHelper(null);
	}
	
	/**
	 * Build paths from a specified point.
	 * This method is used for appending deadends and creating paths from empty nodes.
	 * 
	 * @param gPos (PointXY)
	 */
	private boolean buildPathFromPoint(PointXY gPos, boolean allowSquares) {
		int randomPos = 0;
		int newX = 0;
		int newY = 0;
		boolean success = false;
		boolean dEnd = filled.contains(gPos); // Determines whether is a deadend or empty node
		PointXY filledPos = null;
		PointXY givenPos = gPos;
		PointXY possiblePos = null;
		PointXY ll = null;
		PointXY ul = null;
		PointXY ur = null;
		PointXY lr = null;
		List<int[]> nesw = new ArrayList<int[]>();
		
		nesw.add(new int[]{0, 1});
		nesw.add(new int[]{1, 0});
		nesw.add(new int[]{0, -1});
		nesw.add(new int[]{-1, 0});
		
		// Try all 4 directions
		while (nesw.size() > 0) {
			randomPos = NumberUtils.randomInt(0, nesw.size() - 1); // Get a random direction: north, east, south, west
			newX = givenPos.getX() + nesw.get(randomPos)[0];
			newY = givenPos.getY() + nesw.get(randomPos)[1];
			
			filledPos = new PointXY(newX, newY);
			nesw.remove(randomPos); // Remove the direction used
			
			// If new point is within maze and is filled and there is currently no path between this and the given node
			if (withinDimensions(filledPos) && filled.contains(filledPos) && !isPath(givenPos, filledPos)) {
				addPath(givenPos, filledPos); // Create path
				
				ll = new PointXY(givenPos.getX() - 1, givenPos.getY() - 1);
				ul = new PointXY(givenPos.getX() - 1, givenPos.getY());
				ur = new PointXY(givenPos.getX(), givenPos.getY());
				lr = new PointXY(givenPos.getX(), givenPos.getY() - 1);
				
				if (dEnd && (isSquare(ll) || isSquare(ul) || isSquare(ur) || isSquare(lr))) {
					removePath(givenPos, filledPos);
					possiblePos = new PointXY (filledPos.getX(), filledPos.getY());
				} else {
					// Break after successfully adding path
					if (!filled.contains(givenPos)) {
						filled.add(givenPos);
					}
					success = true;
					break;
				}
			}
		}
		
		if (!success && possiblePos != null && allowSquares) {
			addPath(givenPos, possiblePos);
			if (!filled.contains(givenPos)) {
				filled.add(givenPos);
			}
			squares.add(givenPos);
			success = true;
		}
		
		// If successfully built a path from empty node then continue to build paths from the empty node
		if (success && !dEnd) {
			deadends.add(givenPos);
			buildPathHelper(givenPos);
		}
		
		return success;
	}
	
	/**
	 * A helper method for building paths.
	 * The parameter is optional.
	 * 
	 * @param cPos (PointXY)
	 */
	private void buildPathHelper(PointXY cPos) {
		int pathLength = 0;
		int randomPos = 0;
		int newX = 0;
		int newY = 0;
		boolean extend = true;
		List<int[]> nesw = null;
		PointXY neighbourPos = null;
		
		// If current position not provided, then get a random position that is currently in the exising path
		PointXY currentPos = (cPos != null) ? cPos : getRandomPathPosition();
		PointXY originalPos = currentPos;
		
		// Carry on building paths is not stuck and if haven't reached the maxLength yet
		while (extend && pathLength < mConfig.getMaxLength()) {
			nesw = new ArrayList<int[]>();
			nesw.add(new int[]{0, 1});
			nesw.add(new int[]{1, 0});
			nesw.add(new int[]{0, -1});
			nesw.add(new int[]{-1, 0});
			
			// Try every direction
			while (nesw.size() > 0) {
				randomPos = NumberUtils.randomInt(0, nesw.size() - 1); // Get a random direction: north, east, south, west
				newX = currentPos.getX() + nesw.get(randomPos)[0];
				newY = currentPos.getY() + nesw.get(randomPos)[1];
				
				neighbourPos = new PointXY(newX, newY);
				nesw.remove(randomPos); // Remove the direction
				
				// If new point is within maze and not part of existing path
				if (withinDimensions(neighbourPos) && !filled.contains(neighbourPos)) {
					addPath(currentPos, neighbourPos); // Build path between the points
					
					filled.add(neighbourPos);
					currentPos = neighbourPos; // New point is now the current position
					pathLength++;
					
					extend = true; // Continue building path from the current position
					break;
				}
				
				extend = false; // Stuck, so stop building path from current position
			}
		}
		
		if (!currentPos.equals(originalPos)) deadends.add(currentPos); // If a least 1 path is built
	}
	
	/**
	 * Checks the maze to see whether it contain paths which
	 * is a square of adjacent MazeNodes (minimal square), e.g. (0, 0), (0, 1), (1, 1), (1, 0).
	 * 
	 * @param ll: lower left corner of a minimal square (PointXY)
	 * @return
	 */
	private boolean isSquare(PointXY ll){
		boolean isSquare = false;
		PointXY ul = new PointXY(ll.getX(), ll.getY() + 1);
		PointXY ur = new PointXY(ll.getX() + 1, ll.getY() + 1);
		PointXY lr = new PointXY(ll.getX() + 1, ll.getY());
		
		// Check all 4 corners are within the dimension
		boolean allWithin = withinDimensions(ll) && withinDimensions(ul)
				&& withinDimensions(ur) && withinDimensions(lr);
		
		if (allWithin) {
			// Checks to see whether paths exists forms a minimal square
			isSquare = isPath(ll, ul) && isPath(ul, ur) && isPath(ur, lr) && isPath(lr, ll);
		}
		
		return isSquare;
	}
	
	/**
	 * Removes a a minimal square from the existing path,
	 * e.g. (0, 0), (0, 1), (1, 1), (1, 0). It does this while
	 * maintaining the same number of deadends before and after.
	 * 
	 * @param ll: lower left corner of a minimal square (PointXY)
	 */
	private void removeAndMaintain(PointXY ll) {
		Map<PointXY, List<PointXY>> original = null;
		Map<PointXY, List<PointXY>> unsaturated = unsaturatedAndPaths(ll);
		Map<PointXY, List<PointXY>> saturated = null;
		List<PointXY> keys = new ArrayList<PointXY>();
		List<PointXY> paths = null;
		PointXY key = null;
		PointXY pathPoint = null;
		int random = 0;
		boolean removed = false;
		
		if (unsaturated.size() > 0) {
			original = unsaturatedAndPaths(ll);
			
			for (PointXY k : unsaturated.keySet()) {
				keys.add(k);
			}
			
			while (keys.size() > 0) {
				random = NumberUtils.randomInt(0, keys.size() - 1);
				key = keys.get(random);
				keys.remove(random);
				
				paths = unsaturated.get(key);
				
				while (paths.size() > 0) {
					random = NumberUtils.randomInt(0, paths.size() - 1);
					pathPoint = paths.get(random);
					paths.remove(random);
					
					if (numberOfPaths(pathPoint) > 2) {
						removePath(key, pathPoint);
						
						if (buildPathFromPoint(key, false)) {
							removed = true;
							break;
						} else {
							addPath(key, pathPoint);
						}
					}
				}
				
				if (removed) {
					break;
				}
				
			}
			
			if (!removed && original.size() == 1) {
				key = new ArrayList<PointXY>(original.keySet()).get(0);
				
				paths = original.get(key);
				random = NumberUtils.randomInt(0, paths.size() - 1);
				pathPoint = paths.get(random);
				paths.remove(random);
				
				removePath(key, pathPoint);
			}
			
		} else {
			saturated = saturatedAndPaths(ll);
			
			for (PointXY k : saturated.keySet()) {
				keys.add(k);
			}
			
			while (keys.size() > 0) {
				random = NumberUtils.randomInt(0, keys.size() - 1);
				key = keys.get(random);
				keys.remove(random);
				
				paths = saturated.get(key);
				
				while (paths.size() > 0) {
					random = NumberUtils.randomInt(0, paths.size() - 1);
					pathPoint = paths.get(random);
					paths.remove(random);
					
					if (numberOfPaths(pathPoint) > 2 && numberOfPaths(key) > 2) {
						removePath(key, pathPoint);
						removed = true;
					}
				}
				
				if (removed) {
					break;
				}
				
			}
		}
		
	}
	
	/**
	 * Get a random point in the current path.
	 * 
	 * @return randomPathPosition (PointXY)
	 */
	private PointXY getRandomPathPosition() {
		int randomPos = NumberUtils.randomInt(0, filled.size() - 1);
		return filled.get(randomPos);
	}
	
	/**
	 * Checks whether the given point is within the maze,
	 * i.e. within the dimensions.
	 * 
	 * @param pos (PointXY)
	 * @return withinDimensions (boolean)
	 */
	public boolean withinDimensions(PointXY pos) {
		return dimensions.contains(pos);
	}
	
	/**
	 * Add path to the two given points, if path not already exists.
	 * 
	 * @param p1: Point 1 (PointXY)
	 * @param p2: Point 2 (PointXY)
	 * @return pathAdded (boolean)
	 */
	public boolean addPath(PointXY p1, PointXY p2) {
		MazeNode n1 = nodes.get(p1);
		MazeNode n2 = nodes.get(p2);
		
		boolean success = n1.addNeighbour(p2) && n2.addNeighbour(p1);
		
		if (deadends.contains(p1) && n1.numberOfNeighbours() > 1) {
			deadends.remove(p1);
		}
		
		if (deadends.contains(p2) && n2.numberOfNeighbours() > 1) {
			deadends.remove(p2);
		}
		
		return success;
	}
	
	/**
	 * Remove path from the two given points, if path already exists.
	 * 
	 * @param p1: Point 1 (PointXY)
	 * @param p2: Point 2 (PointXY)
	 * @return pathRemoved (boolean)
	 */
	public boolean removePath(PointXY p1, PointXY p2) {
		MazeNode n1 = nodes.get(p1);
		MazeNode n2 = nodes.get(p2);
		
		boolean success = n1.removeNeighbour(p2) && n2.removeNeighbour(p1);
		
		if (!deadends.contains(p1) && n1.numberOfNeighbours() <= 1) {
			deadends.add(p1);
		}
		
		if (!deadends.contains(p2) && n2.numberOfNeighbours() <= 1) {
			deadends.add(p2);
		}
		
		return success;
	}
	
	/**
	 * Checks whether the two given points form a path.
	 * 
	 * @param p1: Point 1 (PointXY)
	 * @param p2: Point 2 (PointXY)
	 * @return isPath (boolean)
	 */
	public boolean isPath(PointXY p1, PointXY p2) {
		MazeNode n1 = nodes.get(p1);
		MazeNode n2 = nodes.get(p2);
		
		boolean success = n1.isNeighbour(p2) && n2.isNeighbour(p1);
		
		return success;
	}
	
	/**
	 * Return whether the two provided points are neighbours of each other in
	 * the Maze.
	 * 
	 * If either point is not a neighbour of the other, or either of the points
	 * are not in the Maze, false is returned.
	 * 
	 * @param pos1 - the first point
	 * @param pos2 - the second point.
	 * @return true if the two provided points are both neighbours of each 
	 * other, false otherwise.
	 */
	public boolean areNeighbours(PointXY pos1, PointXY pos2) { // Same as isPath method?
		MazeNode n1 = nodes.get(pos1);
		MazeNode n2 = nodes.get(pos2);
		
		if (n1 == null || n2 == null) {
			return false;
		}
		
		return n1.isNeighbour(pos2) && n2.isNeighbour(pos1);
	}
	
	/**
	 * Finds the total number of possible neighbours a given point could have,
	 * assuming neighbours can only be vertically or horizontally adjacent.
	 * 
	 * @param p (PointXY)
	 * @return possibleNeighbours (int)
	 */
	public int possibleNeighbours(PointXY p) {
		int count = 0;
		int newX = 0;
		int newY = 0;
		
		if (withinDimensions(p)) {
			List<int[]> nesw = new ArrayList<int[]>();
			nesw.add(new int[]{0, 1});
			nesw.add(new int[]{1, 0});
			nesw.add(new int[]{0, -1});
			nesw.add(new int[]{-1, 0});
			
			for (int[] nextTo : nesw) {
				newX = p.getX() + nextTo[0];
				newY = p.getY() + nextTo[1];
				
				if (withinDimensions(new PointXY(newX, newY))) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Finds the total number of paths from a given point.
	 * 
	 * @param p (PointXY)
	 * @return numberOfPaths (int)
	 */
	public int numberOfPaths(PointXY p) {
		int count = 0;
		int newX = 0;
		int newY = 0;
		
		if (withinDimensions(p)) {
			List<int[]> nesw = new ArrayList<int[]>();
			nesw.add(new int[]{0, 1});
			nesw.add(new int[]{1, 0});
			nesw.add(new int[]{0, -1});
			nesw.add(new int[]{-1, 0});
			
			for (int[] nextTo : nesw) {
				newX = p.getX() + nextTo[0];
				newY = p.getY() + nextTo[1];
				
				if (isPath(p, new PointXY(newX, newY))) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Finds out whether the given node is saturated (i.e. a
	 * node which has reached its maximum number of paths and cannot
	 * have any more).
	 * 
	 * @param p (PointXY)
	 * @return isSaturated (boolean)
	 */
	public boolean isSaturated(PointXY p) {
		return possibleNeighbours(p) == numberOfPaths(p);
	}
	
	/**
	 * Gives a key-value pair, where the key is the unsaturated node and the value is the
	 * collection of adjacent paths which forms the minimal square.
	 * 
	 * @param lower left corner of a minimal square (PointXY)
	 * @return unsaturated (Map<PointXY, List<PointXY>)
	 */
	private Map<PointXY, List<PointXY>> unsaturatedAndPaths(PointXY ll){
		Map<PointXY, List<PointXY>> unsaturated = new HashMap<PointXY, List<PointXY>>();
		List<PointXY> paths = null;
		
		PointXY ul = new PointXY(ll.getX(), ll.getY() + 1);
		PointXY ur = new PointXY(ll.getX() + 1, ll.getY() + 1);
		PointXY lr = new PointXY(ll.getX() + 1, ll.getY());
		
		if (!isSaturated(ll)) {
			paths = new ArrayList<PointXY>();
			paths.add(ul);
			paths.add(lr);
			unsaturated.put(ll, paths);
		}
		
		if (!isSaturated(ul)) {
			paths = new ArrayList<PointXY>();
			paths.add(ll);
			paths.add(ur);
			unsaturated.put(ul, paths);
		}
		
		if (!isSaturated(ur)) {
			paths = new ArrayList<PointXY>();
			paths.add(ul);
			paths.add(lr);
			unsaturated.put(ur, paths);
		}
		
		if (!isSaturated(lr)) {
			paths = new ArrayList<PointXY>();
			paths.add(ur);
			paths.add(ll);
			unsaturated.put(lr, paths);
		}
		
		return unsaturated;
	}
	
	/**
	 * Gives a key-value pair, where the key is the saturated node and the value is the
	 * collection of adjacent paths which forms the minimal square.
	 * 
	 * @param lower left corner of a minimal square (PointXY)
	 * @return saturated (Map<PointXY, List<PointXY>)
	 */
	private Map<PointXY, List<PointXY>> saturatedAndPaths(PointXY ll){
		Map<PointXY, List<PointXY>> saturated = new HashMap<PointXY, List<PointXY>>();
		List<PointXY> paths = null;
		
		PointXY ul = new PointXY(ll.getX(), ll.getY() + 1);
		PointXY ur = new PointXY(ll.getX() + 1, ll.getY() + 1);
		PointXY lr = new PointXY(ll.getX() + 1, ll.getY());
		
		if (isSaturated(ll)) {
			paths = new ArrayList<PointXY>();
			paths.add(ul);
			paths.add(lr);
			saturated.put(ll, paths);
		}
		
		if (isSaturated(ul)) {
			paths = new ArrayList<PointXY>();
			paths.add(ll);
			paths.add(ur);
			saturated.put(ul, paths);
		}
		
		if (isSaturated(ur)) {
			paths = new ArrayList<PointXY>();
			paths.add(ul);
			paths.add(lr);
			saturated.put(ur, paths);
		}
		
		if (isSaturated(lr)) {
			paths = new ArrayList<PointXY>();
			paths.add(ur);
			paths.add(ll);
			saturated.put(lr, paths);
		}
		
		return saturated;
	}
	
	/**
	 * Gets the dimensions of the maze.
	 * 
	 * @return dimensions (Rectangle)
	 */
	public PolygonShape getDimensions() {
		return this.dimensions;
	}
	
	/**
	 * Gets the nodes in the maze.
	 * 
	 * @return nodse (Map<PointXY, MazeNode>)
	 */
	public Map<PointXY, MazeNode> getNodes() {
		return this.nodes;
	}
	
	/**
	 * Gets a MazeNode within the maze based on the position.
	 * 
	 * @param pos (PointXY)
	 * @return node (MazeNode)
	 */
	public MazeNode getNode(PointXY pos) {
		return this.nodes.get(pos);
	}
	
	/**
	 * Creates a string representation of the nodes within the maze,
	 * where each node is represented by 4 bits, 0000.
	 * 
	 * The position of the bits correspond to NESW (North, East, South, West).
	 * The value 1 represents a path in that direction, while 0 represents no path
	 * in that direction.
	 */
	@Override
	public String toString() {
		PointXY pos = null;
		int val = 0;
		int[][] offset = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
		StringBuffer sb = new StringBuffer();
		sb.append("Key: NESW");
		sb.append("\n\n");
		
		for (int j = dimensions.getMaxY(); j >= dimensions.getMinY(); j--) {
			for (int i = dimensions.getMinX(); i <= dimensions.getMaxX(); i++) {
				pos = new PointXY(i, j);
				
				if (withinDimensions(pos)) {
					for (int[] k : offset) {
						val = (isPath(pos, new PointXY(i + k[0], j + k[1]))) ? 1 : 0;
						sb.append(val);
					}
					sb.append(" ");
				} else {
					sb.append("0000");
					sb.append(" ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
