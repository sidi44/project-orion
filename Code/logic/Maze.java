package logic;

import geometry.PointXY;
import geometry.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import utils.NumberUtils;

public class Maze {
	
	private Map<PointXY, MazeNode> nodes;
	private Rectangle dimensions;
	private MazeConfig mConfig;
	
	private List<PointXY> deadends;
	private List<PointXY> filled;
	
	public Maze(Rectangle dimensions) {
		this.dimensions = dimensions;
		configureDefault();
		buildMaze();
	}
	
	public Maze(Rectangle dimensions, MazeConfig mConfig) {
		this.dimensions = dimensions;
		this.mConfig = mConfig;
		buildMaze();
	}
	
	private void configureDefault() {
		int rows = dimensions.getMaxX() - dimensions.getMinX() + 1;
		int columns = dimensions.getMaxY() - dimensions.getMinY() + 1;
		
		int maxLength = (rows * columns) - 1;
		double deadEndMinProp = 0.1;
		double ranPathMaxProp = 0.8;
		int loopLimit = 20;
		
		this.mConfig = new MazeConfig(maxLength, loopLimit, deadEndMinProp, ranPathMaxProp);
	}
	
	private void buildMaze() {
		this.nodes = new HashMap<PointXY, MazeNode>();
		this.deadends = new ArrayList<PointXY>();
		this.filled = new ArrayList<PointXY>();
		
		createEmptyMaze();
		addInitialPath();
		fillRandom();
		fillIterate();
		appendDeadends();
		removeSquares();
		
		try {
			if (this.nodes.size() != this.filled.size()) throw new Exception("Illegal Maze: nodes not all filled.");
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}
	
	private void createEmptyMaze() {
		PointXY position = null;
		MazeNode node = null;
		
		for (int i = dimensions.getMinX(); i <= dimensions.getMaxX(); i++) {
			for(int j = dimensions.getMinY(); j <= dimensions.getMaxY(); j++) {
				position = new PointXY(i, j);
				node = new MazeNode(new HashSet<PointXY>());
				
				nodes.put(position, node);
			}
		}
	}
	
	private void addInitialPath() {
		PointXY startPos = new PointXY(dimensions.getMinX(), dimensions.getMinY());
		filled.add(startPos);
		buildPathRandom();
		deadends.add(startPos);
	}
	
	private void fillRandom() {
		double nSize = nodes.size();
		int counter = 0;
		
		while ((filled.size() / nSize) <= mConfig.getRanPathMaxProp() && counter < mConfig.getLoopLimit()) {
			buildPathRandom();
			counter++;
		}
	}
	
	private void fillIterate() {
		PointXY emptyPos = null;
		for (int i = dimensions.getMinX(); i <= dimensions.getMaxX(); i++) {
			for (int j = dimensions.getMinY(); j <= dimensions.getMaxY(); j++) {
				emptyPos = new PointXY(i, j);
				if (withinDimensions(emptyPos) && !filled.contains(emptyPos)) {
					buildPathFromPoint(emptyPos);
				}
			}
		}
	}
	
	private void appendDeadends() {
		double nSize = nodes.size();
		int counter = 0;
		int randomPos = 0;
		PointXY dEnd = null;
		
		while ((deadends.size() / nSize) >= mConfig.getDeadEndMinProp() && deadends.size() > 0 && counter < mConfig.getLoopLimit()) {
			randomPos = NumberUtils.randomInt(0, deadends.size() - 1);
			dEnd = deadends.get(randomPos);
			buildPathFromPoint(dEnd);
			counter++;
		}
	}
	
	private void removeSquares() {
		PointXY ll = null;
		for (int i = dimensions.getMinX(); i <= dimensions.getMaxX() - 1; i++) {
			for (int j = dimensions.getMinY(); j <= dimensions.getMaxY() - 1; j++) {
				ll = new PointXY(i, j);
				if (isSquare(ll)) {
					removeRandom(ll);
				}
			}
		}
	}
	
	private void buildPathRandom() {
		buildPathHelper(null);
	}
	
	private void buildPathFromPoint(PointXY gPos) {
		int randomPos = 0;
		double newX = 0;
		double newY = 0;
		boolean success = false;
		boolean isEmpty = !filled.contains(gPos);
		PointXY filledPos = null;
		PointXY givenPos = gPos;
		List<int[]> nesw = new ArrayList<int[]>();
		
		nesw.add(new int[]{0, 1});
		nesw.add(new int[]{1, 0});
		nesw.add(new int[]{0, -1});
		nesw.add(new int[]{-1, 0});
		
		while (nesw.size() > 0) {
			randomPos = NumberUtils.randomInt(0, nesw.size() - 1);
			newX = givenPos.getX() + nesw.get(randomPos)[0];
			newY = givenPos.getY() + nesw.get(randomPos)[1];
			
			filledPos = new PointXY(newX, newY);
			nesw.remove(randomPos);
			
			if (withinDimensions(filledPos) && filled.contains(filledPos) && !isPath(givenPos, filledPos)) {
				addPath(givenPos, filledPos);
				
				if (!filled.contains(givenPos)) filled.add(givenPos);
				success = true;
				break;
			}
		}
		
		if (success && isEmpty) {
			deadends.add(givenPos);
			buildPathHelper(givenPos);
		}
	}
	
	private void buildPathHelper(PointXY cPos) {
		int pathLength = 0;
		int randomPos = 0;
		double newX = 0;
		double newY = 0;
		boolean extend = true;
		List<int[]> nesw = null;
		PointXY neighbourPos = null;
		PointXY currentPos = (cPos != null) ? cPos : getRandomPathPosition();
		PointXY originalPos = currentPos;
		
		while (extend && pathLength < mConfig.getMaxLength()) {
			nesw = new ArrayList<int[]>();
			nesw.add(new int[]{0, 1});
			nesw.add(new int[]{1, 0});
			nesw.add(new int[]{0, -1});
			nesw.add(new int[]{-1, 0});
			
			while (nesw.size() > 0) {
				randomPos = NumberUtils.randomInt(0, nesw.size() - 1);
				newX = currentPos.getX() + nesw.get(randomPos)[0];
				newY = currentPos.getY() + nesw.get(randomPos)[1];
				
				neighbourPos = new PointXY(newX, newY);
				nesw.remove(randomPos);
				
				if (withinDimensions(neighbourPos) && !filled.contains(neighbourPos)) {
					addPath(currentPos, neighbourPos);
					
					filled.add(neighbourPos);
					currentPos = neighbourPos;
					pathLength++;
					
					extend = true;
					break;
				}
				
				extend = false;
			}
		}
		
		if (!currentPos.equals(originalPos)) deadends.add(currentPos);
	}
	
	private boolean isSquare(PointXY ll){
		boolean isSquare = false;
		PointXY ul = new PointXY(ll.getX(), ll.getY() + 1);
		PointXY ur = new PointXY(ll.getX() + 1, ll.getY() + 1);
		PointXY lr = new PointXY(ll.getX() + 1, ll.getY());
		
		boolean allWithin = withinDimensions(ll) && withinDimensions(ul)
				&& withinDimensions(ur) && withinDimensions(lr);
		
		if (allWithin) {
			isSquare = isPath(ll, ul) && isPath(ul, ur) && isPath(ur, lr) && isPath(lr, ll);
		}
		
		return isSquare;
	}
	
	private void removeRandom(PointXY ll) {
		PointXY ul = new PointXY(ll.getX(), ll.getY() + 1);
		PointXY ur = new PointXY(ll.getX() + 1, ll.getY() + 1);
		PointXY lr = new PointXY(ll.getX() + 1, ll.getY());
		
		switch (NumberUtils.randomInt(0, 3)) {
			case 0:
				removePath(ll, ul);
				break;
			case 1:
				removePath(ul, ur);
				break;
			case 2:
				removePath(ur, lr);
				break;
			case 3:
				removePath(lr, ll);
				break;
		}
	}
	
	private PointXY getRandomPathPosition() {
		int randomPos = NumberUtils.randomInt(0, filled.size() - 1);
		return filled.get(randomPos);
	}
	
	public boolean withinDimensions(PointXY pos) {
		return NumberUtils.withinLimits(pos.getX(), dimensions.getMinX(), dimensions.getMaxX())
				&& NumberUtils.withinLimits(pos.getY(), dimensions.getMinY(), dimensions.getMaxY());
	}
	
	public boolean addPath(PointXY p1, PointXY p2) {
		MazeNode n1 = nodes.get(p1);
		MazeNode n2 = nodes.get(p2);
		
		if (deadends.contains(p1)) deadends.remove(p1);
		if (deadends.contains(p2)) deadends.remove(p2);
		
		return n1.addNeighbour(p2) && n2.addNeighbour(p1);
	}
	
	public boolean removePath(PointXY p1, PointXY p2) {
		MazeNode n1 = nodes.get(p1);
		MazeNode n2 = nodes.get(p2);
		
		if (!deadends.contains(p1)) deadends.add(p1);
		if (!deadends.contains(p2)) deadends.add(p2);
		
		return n1.removeNeighbour(p2) && n2.removeNeighbour(p1);
	}
	
	public boolean isPath(PointXY p1, PointXY p2) {
		MazeNode n1 = nodes.get(p1);
		MazeNode n2 = nodes.get(p2);
		
		return n1.isNeighbour(p2) && n2.isNeighbour(p1);
	}
	
	public Rectangle getDimensions() {
		return this.dimensions;
	}
	
	public Map<PointXY, MazeNode> getNodes() {
		return this.nodes;
	}
	
	public MazeNode getNode(PointXY pos) {
		return this.nodes.get(pos);
	}
	
	@Override
	public String toString() {
		PointXY pos = null;
		int val = 0;
		int[][] offset = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
		StringBuffer sb = new StringBuffer();
		sb.append("Key: NESW");
		sb.append("\n\n");
		
		for (int j = dimensions.getMaxY(); j >= dimensions.getMinY(); j--) {
			for (int i = dimensions.getMinX(); i <= dimensions.getMaxX(); i++) {
				pos = new PointXY(i, j);
				
				if (withinDimensions(pos)) {
					for (int[] k : offset) {
						val = (isPath(pos, new PointXY(i + k[0], j + k[1]))) ? 1 : 0;
						sb.append(val);
					}
					sb.append(" ");
				}
			}
			if (withinDimensions(pos)) sb.append("\n");
		}
		return sb.toString();
	}
	
}
