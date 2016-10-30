package pathfinding;

import java.util.Set;

import geometry.PointXY;

public interface PathFinder {
	
	public void generateAllPaths();
	
	/**
	 * Get the shortest path from the provided start point to the provided end
	 * point.
	 * 
	 * @param start - the first point in the shortest path to find.
	 * @param end - the last point in the shortest path to find.
	 * @return the shortest path from the provided start point to the provided 
	 * end point.
	 */
	public Path getPath(PointXY start, PointXY end);
	
	/**
	 * Get the shortest path from the provided start point to the provided set 
	 * of 'goal' points
	 * 
	 * @param start - the first point in the shortest path to find.
	 * @param goals - the set of goal points to find the shortest path too.
	 * @return the shortest path from the provided start point to the closest 
	 * point in the provided set of goal points.
	 */
	public Path getPath(PointXY start, Set<PointXY> goals);
	
}
