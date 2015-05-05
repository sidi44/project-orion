package logic;

import geometry.PointXY;

import java.util.Map;

public class MazeNode {
	
	private Map<PointXY, MazeNode> neighbours;
	
	public MazeNode(Map<PointXY, MazeNode> neighbours) {
		this.neighbours = neighbours;
	}
	
	public Map<PointXY, MazeNode> getNeighbours() {
		return this.neighbours;
	}
	
	public void addNeighbours(PointXY position, MazeNode node) {
		if (!this.neighbours.containsKey(position)) {
			this.neighbours.put(position, node);
		}
	}

}
