package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import geometry.PointXY;

/**
 * Path class.
 * 
 * A Path is an ordered list of points which describes a viable route from one
 * point to another.
 * 
 * @author Simon Dicken
 * @version 2015-12-28
 */
public class Path {

	// The ordered list of points which describes the path.
	List<PointXY> path;
	
	// Also hold the points as a Set for efficient 'contains' checks.
	Set<PointXY> points;
	
	/**
	 * Constructor for Path.
	 */
	public Path() {
		path = new ArrayList<PointXY>();
		points = new HashSet<PointXY>();
	}
	
	/**
	 * Copy constructor for Path. 
	 * 
	 * (Note that this makes a shallow copy of the Path's internal structures.)
	 * 
	 * @param path - the Path to copy
	 */
	public Path(Path path) {
		this.path = new ArrayList<PointXY>();
		this.points = new HashSet<PointXY>();
		
		this.path.addAll(path.path);
		this.points.addAll(path.points);
	}
	
	/**
	 * Add the given point to the end of the Path.
	 * 
	 * @param point - the point to add to the end of the Path.
	 */
	public void addToEnd(PointXY point) {
		path.add(point);
		points.add(point);
	}
	
	/**
	 * Check whether a given point is within the Path.
	 * 
	 * @param point - the point to check whether it's in the Path.
	 * @return true if the provided point is in the Path, false otherwise.
	 */
	public boolean contains(PointXY point) {
		return points.contains(point);
	}
	
	/**
	 * Get the length of the Path (i.e. the number of points in the path from 
	 * start to end inclusive).
	 * 
	 * @return the length of the Path.
	 */
	public int getLength() {
		return path.size();
	}
	
	/**
	 * Check whether the Path is empty.
	 * 
	 * @return true if the path is empty, false otherwise.
	 */
	public boolean empty() {

		if (path.size() != points.size()) {
			throw new IllegalStateException("Internal state out of sync.");
		}
		
		return (path.size() == 0);
	}
	
	/**
	 * Reverse the points in the path.
	 */
	public void reversePath() {
		ArrayList<PointXY> newPath =  new ArrayList<PointXY>();
		
		for (int i = path.size() - 1; i >= 0; --i) {
			newPath.add(path.get(i));
		}
		
		path = newPath;
	}
	
	/**
	 * Get the points in the path as a List.
	 * 
	 * @return the points in the path as a List.
	 */
	public List<PointXY> getPathNodes() {
		return path;
	}
	
	/**
	 * Get a sub path of path from the given start point to the given end point, 
	 * inclusive of both points.
	 * 
	 * If the start point is after the end point, a Path from start to end is 
	 * still provided (i.e. the sub-path is internally reversed to match the 
	 * order of the arguments prior to being returned).
	 * 
	 * @param start - the first point in the sub-path.
	 * @param end - the last point in the sub-path.
	 * @return the Path from start to end which is a sub-path of this Path.
	 * @throws IllegalArgumentException - if either or both of start and end are
	 * not in the path.
	 */
	public Path subPath(PointXY start, PointXY end) {
		
		if (!points.contains(start) || !points.contains(end)) {
			throw new IllegalArgumentException("One or both of the points are"
					+ "not in the Path.");
		}
		
		// Quick check to efficiently return if start and end are equal.
		if (start.equals(end)) {
			Path p = new Path();
			p.addToEnd(start);
			return p;
		}
		
		Path p = new Path();
		boolean adding = false;
		boolean forwards = true;
		for (PointXY point : path) {
			
			// Check if we're at the 'start' of the sub-path.
			if (point.equals(start) && !adding) {
				adding = true;
				forwards = true;
			} else if (point.equals(end) && !adding) {
				adding = true;
				forwards = false;
			} 
			
			// Add the point to the path if we're between start and end.
			if (adding) {
				p.addToEnd(point);
			}
			
			// Check if we've reached the 'end' of the sub-path.
			if ((forwards && point.equals(end)) || 
				(!forwards && point.equals(start))) {
				
				break;
			}
			
		}
		
		if (!forwards) {
			p.reversePath();
		}
		
		return p;
	}
	
	/**
	 * Removes all points from the Path.
	 */
	public void clear() {
		path.clear();
		points.clear();
	}
	
}
