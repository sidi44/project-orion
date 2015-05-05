package logic;

import geometry.PointXY;
import geometry.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class Maze {
	
	private Map<PointXY, MazeNode> nodes;
	private Rectangle dimensions;
	private int maxLength;
	
	public Maze(Rectangle dimensions, int maxLength) {
		this.dimensions = dimensions;
		this.maxLength = maxLength;
		this.nodes = new HashMap<PointXY, MazeNode>();
		
		buildMaze();
	}
	
	public Rectangle getDimensions() {
		return this.dimensions;
	}
	
	public Map<PointXY, MazeNode> getNodes() {
		return this.nodes;
	}
	
	public boolean withinMaze(PointXY pos) {
		return NumberUtils.withinLimits(pos.getX(), dimensions.getMinX(), dimensions.getMaxX())
				&& NumberUtils.withinLimits(pos.getY(), dimensions.getMinY(), dimensions.getMaxY());
	}
	
	public void addPath(PointXY p1, PointXY p2) {
		MazeNode n1 = nodes.get(p1);
		MazeNode n2 = nodes.get(p2);
		
		n1.addNeighbours(p2, n2);
		n2.addNeighbours(p1, n1);
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
