package pathfinding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import geometry.PointXY;
import geometry.PointXYPair;

class ShortestPathStore {

	private Map<PointXYPair, Path> shortestPaths;
	private Map<PointXY, Set<PointXY>> pathExists;
	
	public ShortestPathStore() {
		this.shortestPaths = new HashMap<PointXYPair, Path>();
		this.pathExists = new HashMap<PointXY, Set<PointXY>>();
	}
	
	public Path getPath(PointXY p1, PointXY p2) {
		PointXYPair pair = new PointXYPair(p1, p2);
		return shortestPaths.get(pair);
	}
	
	public void addPath(Path path) {
		
		if (path.empty()) {
			throw new IllegalArgumentException("Trying to add an empty path.");
		}
		
		add(path);
	}
	
	public void addPathAndSubPaths(Path path) {
		
		if (path.empty()) {
			throw new IllegalArgumentException("Trying to add an empty path.");
		}
		
		for (int i = 0; i < path.getLength(); ++i) {
			for (int j = 0; j < path.getLength(); ++j) {
				Path subPath = path.subPath(path.getPoint(i), path.getPoint(j));
				add(subPath);
			}
		}

	}
	
	private void add(Path path) {	
		
		PointXY start = path.getStart();
		PointXY end = path.getEnd();
		
		PointXYPair pair = new PointXYPair(start, end);
		if (!shortestPaths.containsKey(pair)) {
			shortestPaths.put(pair, path);
		} else {
			Path existing = shortestPaths.get(pair);
			if (existing.getLength() > path.getLength()) {
				shortestPaths.put(pair, path);
			}
		}
		
		if (pathExists.containsKey(start)) {
			Set<PointXY> endPoints = pathExists.get(start);
			endPoints.add(end);
		} else {
			Set<PointXY> endPoints = new HashSet<PointXY>();
			endPoints.add(end);
			pathExists.put(start, endPoints);
		}
		
	}
	
	public boolean pathExists(PointXY p1, PointXY p2) {
		PointXYPair pair = new PointXYPair(p1, p2);
		return shortestPaths.containsKey(pair);
	}
	
	public int numPaths() {
		return shortestPaths.size();
	}
	
}
