package logic;

import geometry.PointXY;

import java.util.Set;

/**
 * Represents a node which is used to build a maze.
 * 
 * @author Martin Wong
 * @version 2015-06-01
 */
public class MazeNode {
	
	private Set<PointXY> neighbours;
	
	/**
	 * Creates an instance of MazeNode.
	 * 
	 * @param neighbours (Set<PointXY>)
	 */
	public MazeNode(Set<PointXY> neighbours) {
		this.neighbours = neighbours;
	}
	
	/**
	 * Gets the neighbours of this maze node.
	 * 
	 * @return neighbours (Set<PointXY>)
	 */
	public Set<PointXY> getNeighbours() {
		return this.neighbours;
	}
	
	/**
	 * Add a new neighbour to this maze node and returns a boolean.
	 * True is successful and False is unsuccessful.
	 * 
	 * @param position (PointXY)
	 * @return success (boolean)
	 */
	public boolean addNeighbour(PointXY position) {
		boolean success = false;
		if (!this.neighbours.contains(position)) {
			success = this.neighbours.add(position);
		}
		return success;
	}
	
	/**
	 * Removes an existing neighbour from this maze node and returns a boolean.
	 * True is successful and False is unsuccessful.
	 * 
	 * @param position (PointXY)
	 * @return success (boolean)
	 */
	public boolean removeNeighbour(PointXY position) {
		boolean success = false;
		if (this.neighbours.contains(position)) {
			success = this.neighbours.remove(position);
		}
		return success;
	}
	
	/**
	 * Checks whether a certain point on a graph is a neighbour of this maze node.
	 * 
	 * @param position (PointXY)
	 * @return success (boolean)
	 */
	public boolean isNeighbour(PointXY position) {
		return this.neighbours.contains(position);
	}
	
	/**
	 * Gets the number of neighbours this maze node has.
	 * 
	 * @return numberOfNeighbours (int)
	 */
	public int numberOfNeighbours() {
		return neighbours.size();
	}
	

}
