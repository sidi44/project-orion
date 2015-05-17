package logic;

import geometry.PointXY;

import java.util.HashMap;
import java.util.Map;

public class MazeNode {
	
	private Map<PointXY, MazeNode> neighbours;
	private boolean pill;

	public MazeNode() {
		this.neighbours = new HashMap<PointXY, MazeNode>();
		this.pill = true;
	}
	
	public MazeNode(Map<PointXY, MazeNode> neighbours) {
		this.neighbours = neighbours;
	}

	public Map<PointXY, MazeNode> getNeighbours() {
		return this.neighbours;
	}
	
	public boolean getPill() {
		return pill;
	}
	
	public void setPill(boolean pill) {
		this.pill = pill;
	}

	public void addNeighbour(PointXY position, MazeNode node) {
		if (!this.neighbours.containsKey(position)) {
			this.neighbours.put(position, node);
		}
	}
	
	public boolean hasNeighbour(PointXY pos) {
		return neighbours.containsKey(pos);		
	}
	
}
