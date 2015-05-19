package logic;

import geometry.PointXY;

import java.util.Set;

public class MazeNode {
	
	private Set<PointXY> neighbours;
	
	public MazeNode(Set<PointXY> neighbours) {
		this.neighbours = neighbours;
	}
	
	public Set<PointXY> getNeighbours() {
		return this.neighbours;
	}
	
	public boolean addNeighbour(PointXY position) {
		boolean success = false;
		if (!this.neighbours.contains(position)) {
			success = this.neighbours.add(position);
		}
		return success;
	}
	
	public boolean removeNeighbour(PointXY position) {
		boolean success = false;
		if (this.neighbours.contains(position)) {
			success = this.neighbours.remove(position);
		}
		return success;
	}
	
	public boolean isNeighbour(PointXY position) {
		return this.neighbours.contains(position);
	}
	

}
