package logic;

import java.util.ArrayList;
import java.util.List;

public class MazeNode {
	
	private PointXY position;
	private boolean pill;
	private List<MazeNode> neighbours;
	
	public MazeNode(PointXY position, boolean pill) {
		this.position = position;
		this.pill = pill;
		neighbours = new ArrayList<MazeNode>();
	}
	
	public PointXY getPosition() {
		return position;
	}
	
	public boolean hasPill() {
		return pill;
	}
	
	public List<MazeNode> getNeighbours() {
		return neighbours;
	}
	
	public void addNeighbour(MazeNode neighbour) {
		if (!isNeighbour(neighbour)) {
			neighbours.add(neighbour);
		}
	}
	
	public boolean isNeighbour(MazeNode neighbour) {
		return neighbours.contains(neighbour);
	}
	
}
