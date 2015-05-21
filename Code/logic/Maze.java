package logic;

import geometry.PointXY;
import geometry.Rectangle;

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
 * @version 2015-05-19
 */
public class Maze {
	
	private Map<PointXY, MazeNode> nodes; // The maze
	private Rectangle dimensions; // Dimensions of the maze
	private MazeConfig mConfig; // Configurations for setting up the maze
	
	private List<PointXY> deadends; // Records the deadends in the maze
	private List<PointXY> filled; // Records the nodes which are part of paths
	
	/**
	 * Creates an instance of a Maze with default configurations.
	 * 
	 * @param dimensions (Rectangle)
	 */
	public Maze(Rectangle dimensions) {
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
	public Maze(Rectangle dimensions, MazeConfig mConfig) {
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
		double deadEndMinProp = 0.1; // Minimum deadend nodes / total nodes
		double ranPathMaxProp = 0.8; // Maximum filled nodes / total nodes
		int loopLimit = 20; // Maximum number of loops before stopping
		
		this.mConfig = new MazeConfig(maxLength, loopLimit, deadEndMinProp, ranPathMaxProp);
	}
	
	/**
	 * Builds the maze: nodes (Map<PointXY, MazeNode>)
	 */
	private void buildMaze() {
		this.nodes = new HashMap<PointXY, MazeNode>();
		this.deadends = new ArrayList<PointXY>();
		this.filled = new ArrayList<PointXY>();
		
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
		
		for (int i = dimensions.getMinX(); i <= dimensions.getMaxX(); i++) {
			for(int j = dimensions.getMinY(); j <= dimensions.getMaxY(); j++) {
				position = new PointXY(i, j);
				node = new MazeNode(new HashSet<PointXY>());
				
				nodes.put(position, node);
			}
		}
	}
	
	/**
	 * Creates the first path of the maze.
	 */
	private void addInitialPath() {
		PointXY startPos = new PointXY(dimensions.getMinX(), dimensions.getMinY()); // Starting point
		filled.add(startPos);
		buildPathRandom(); // Build a path from the starting point
		deadends.add(startPos); // At the starting point to deadends
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
	 */
	private void fillIterate() {
		PointXY emptyPos = null;
		for (int i = dimensions.getMinX(); i <= dimensions.getMaxX(); i++) {
			for (int j = dimensions.getMinY(); j <= dimensions.getMaxY(); j++) {
				emptyPos = new PointXY(i, j);
				
				// Checks that MazeNode is a part of the maze and is empty
				if (withinDimensions(emptyPos) && !filled.contains(emptyPos)) {
					buildPathFromPoint(emptyPos);
				}
			}
		}
	}
	
	/**
	 * Join deadends to existing paths based on the deadEndMinProp value.
	 */
	private void appendDeadends() {
		double nSize = nodes.size();
		int counter = 0;
		int randomPos = 0;
		PointXY dEnd = null;
		
		// Only join deadends if over the deadEndMinProp and counter is under the loopLimit
		while ((deadends.size() / nSize) >= mConfig.getDeadEndMinProp() && deadends.size() > 0 && counter < mConfig.getLoopLimit()) {
			randomPos = NumberUtils.randomInt(0, deadends.size() - 1);
			dEnd = deadends.get(randomPos); // Get a random deadend
			buildPathFromPoint(dEnd);
			counter++;
		}
	}
	
	/**
	 * Removes paths formed by a square of adjacent nodes (minimal square),
	 * e.g. (0, 0), (0, 1), (1, 1), (1, 0).
	 */
	private void removeSquares() {
		PointXY ll = null;
		for (int i = dimensions.getMinX(); i <= dimensions.getMaxX() - 1; i++) {
			for (int j = dimensions.getMinY(); j <= dimensions.getMaxY() - 1; j++) {
				ll = new PointXY(i, j);
				if (isSquare(ll)) {
					removeRandom(ll);
				}
			}
		}
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
	private void buildPathFromPoint(PointXY gPos) {
		int randomPos = 0;
		double newX = 0;
		double newY = 0;
		boolean success = false;
		boolean isEmpty = !filled.contains(gPos); // Determines whether is a deadend or empty node
		PointXY filledPos = null;
		PointXY givenPos = gPos;
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
				
				// Break after successfully adding path
				if (!filled.contains(givenPos)) filled.add(givenPos);
				success = true;
				break;
			}
		}
		
		// If successfully built a path from empty node then continue to build paths from the empty node
		if (success && isEmpty) {
			deadends.add(givenPos);
			buildPathHelper(givenPos);
		}
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
		double newX = 0;
		double newY = 0;
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
	 * Removes a random point in a minimal square from the existing path,
	 * e.g. (0, 0), (0, 1), (1, 1), (1, 0).
	 * 
	 * @param ll: lower left corner of a minimal square (PointXY)
	 */
	private void removeRandom(PointXY ll) {
		PointXY ul = new PointXY(ll.getX(), ll.getY() + 1);
		PointXY ur = new PointXY(ll.getX() + 1, ll.getY() + 1);
		PointXY lr = new PointXY(ll.getX() + 1, ll.getY());
		
		// Choose a random path from the minimal square
		switch (NumberUtils.randomInt(0, 3)) {
			case 0:
				removePath(ll, ul);
				break;
			case 1:
				removePath(ul, ur);
				break;
			case 2:
				removePath(ur, lr);
				break;
			case 3:
				removePath(lr, ll);
				break;
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
		return NumberUtils.withinLimits(pos.getX(), dimensions.getMinX(), dimensions.getMaxX())
				&& NumberUtils.withinLimits(pos.getY(), dimensions.getMinY(), dimensions.getMaxY());
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
		
		if (deadends.contains(p1)) deadends.remove(p1);
		if (deadends.contains(p2)) deadends.remove(p2);
		
		return n1.addNeighbour(p2) && n2.addNeighbour(p1);
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
		
		if (!deadends.contains(p1)) deadends.add(p1);
		if (!deadends.contains(p2)) deadends.add(p2);
		
		return n1.removeNeighbour(p2) && n2.removeNeighbour(p1);
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
		
		return n1.isNeighbour(p2) && n2.isNeighbour(p1);
	}
	
	/**
	 * Gets the dimensions of the maze.
	 * 
	 * @return dimensions (Rectangle)
	 */
	public Rectangle getDimensions() {
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
				}
			}
			if (withinDimensions(pos)) sb.append("\n");
		}
		return sb.toString();
	}
	
}
