package logic;

import java.util.HashMap;
import java.util.Map;

public class Maze {

	private Map<PointXY, MazeNode> nodes;
	private int rows;
	private int columns;
	//private int maxPathLength;
	
	public Maze(int rows, int columns, int maxPathLength) {
		this.rows = rows;
		this.columns = columns;
		//this.maxPathLength = maxPathLength;
		
		this.nodes = new HashMap<PointXY, MazeNode>();
	}
	
	public Rectangle getDimensions() {
		
		// This is a lazy implementation for my convenience!
		// Really, we should probably have a dimensions field variable which 
		// is initialised to (0, 0, 0, 0) and is updated every time a node is 
		// added as appropriate.
		
		return new Rectangle(0, 0, columns, rows);
	}
	
	public Map<PointXY, MazeNode> getNodes() {
		return nodes;
	}
	
	public void addNode(MazeNode newNode) {
		
		// This is just for my convenience at the moment to quickly build a
		// dummy maze to test with. 
		
		PointXY pos = newNode.getPosition();
		if (!nodes.containsKey(pos)) {
			nodes.put(pos, newNode);
		} else {
			// Something's gone wrong! (assuming we don't want to replace nodes.)
			System.err.println("Trying to add a node where one already exists.");
		}
	}
	
	
}
