package pathfinding;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import geometry.PointXY;
import logic.Maze;
import logic.MazeNode;

class BFSPathFinder implements PathFinder {

	private Maze maze;
	
	private ShortestPathStore pathStore;
	
	public BFSPathFinder(Maze maze) {
		this.maze = maze;
		this.pathStore = new ShortestPathStore();
	}
	
	@Override
	public void generateAllPaths() {
		
		Map<PointXY, MazeNode> mazeNodes = maze.getNodes();
		Set<PointXY> positions = mazeNodes.keySet();
		for (PointXY pos : positions) {
			allPathsSingleSource(pos);
		}
		
	}
	
	private void allPathsSingleSource(PointXY pos) {
		
		Path path = new PointXYPath();
		Queue<PointXY> queue = new  LinkedList<PointXY>();
		
		path.addToEnd(pos);
		queue.add(pos);
		pathStore.addPath(path);
		
		while (!queue.isEmpty()) {
			
			PointXY current = queue.remove();
			Path currentPath = pathStore.getPath(pos, current);
			
			Map<PointXY, MazeNode> mazeNodes = maze.getNodes();
			MazeNode node = mazeNodes.get(current);
			Set<PointXY> neighbours = node.getNeighbours();
			
			for (PointXY neighbour : neighbours) {
				
				if (pathStore.pathExists(pos, neighbour)) {
					Path neighbourPath = pathStore.getPath(pos, neighbour);
					if (neighbourPath.getLength() <= currentPath.getLength() + 1) {
						continue;
					}
				}
				
				Path neighbourPath = currentPath.deepCopy();
				neighbourPath.addToEnd(neighbour);
				pathStore.addPath(neighbourPath);
				
				queue.add(neighbour);
			}
			
		}
		
	}

	@Override
	public Path getPath(PointXY start, PointXY end) {
		return pathStore.getPath(start, end);
	}

	@Override
	public Path getPath(PointXY start, Set<PointXY> goals) {
		
		Path shortestPath = null;
		int shortestPathLength = Integer.MAX_VALUE;
		for (PointXY goal : goals) {
			Path path = pathStore.getPath(start, goal);
			if (path.getLength() < shortestPathLength) {
				shortestPath = path;
				shortestPathLength = path.getLength();
			}
		}
		
		return shortestPath;
	}
	
	@Override
	public float getProgress() {
		int total = maze.getNodes().size() * maze.getNodes().size();
		
		int current = pathStore.numPaths();
		
		float progress = current * 100f / total;
		return progress;
	}

	@Override
	public void run() {
		generateAllPaths();
	}

}
