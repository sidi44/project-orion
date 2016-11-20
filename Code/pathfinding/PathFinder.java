package pathfinding;

import java.util.Set;

import geometry.PointXY;
import logic.Maze;

/**
 * An abstract class that can be used to find Paths between different points in 
 * a Maze.
 * 
 * If generateAllPaths() is called, this will cache all the possible paths 
 * between points in the Maze, allowing for efficient lookup of a Path 
 * afterwards.
 * 
 * @author Simon Dicken
 */
public abstract class PathFinder {
	
	// The maze we're searching
	private Maze maze;
	
	/**
	 * Constructor.
	 * 
	 * @param maze - the maze in which to find Paths.
	 */
	public PathFinder(Maze maze) {
		this.maze = maze;
	}
	
	/**
	 * Calculates and stores the shortest path from every point in the maze to 
	 * every other point.
	 */
	public abstract void generateAllPaths();
	
	/**
	 * Get the shortest path from the provided start point to the provided end
	 * point.
	 * 
	 * @param start - the first point in the shortest path to find.
	 * @param end - the last point in the shortest path to find.
	 * @return the shortest path from the provided start point to the provided 
	 * end point.
	 */
	public abstract Path getPath(PointXY start, PointXY end);
	
	/**
	 * Get the shortest path from the provided start point to the provided set 
	 * of 'goal' points
	 * 
	 * @param start - the first point in the shortest path to find.
	 * @param goals - the set of goal points to find the shortest path too.
	 * @return the shortest path from the provided start point to the closest 
	 * point in the provided set of goal points.
	 */
	public abstract Path getPath(PointXY start, Set<PointXY> goals);
	
	/**
	 * Get the total number of Paths that have been calculated and cached by 
	 * this PathFinder.
	 * 
	 * @return the number of cached Paths.
	 */
	public abstract int numStoredPaths();
	
	/**
	 * Get the total number of possible Paths that this PathFinder could store.
	 * (There is one path from every point in the maze to every other point in
	 * the maze.)
	 * 
	 * @return the total number of possible Paths.
	 */
	public int numPossiblePaths() {
		int size = getMaze().getNodes().size();
		return size * size;
	}
	
	/**
	 * Get the Maze this PathFinder uses.
	 * 
	 * @return the Maze this PathFinder uses.
	 */
	protected Maze getMaze() {
		return maze;
	}
	
}
