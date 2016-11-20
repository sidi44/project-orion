package pathfinding;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import functional.BooleanSupplier;
import geometry.PointXY;
import logic.Maze;
import logic.MazeNode;

class BFSPathFinder extends PathFinder {
	
	private ShortestPathStore pathStore;
	
	public BFSPathFinder(Maze maze) {
		super(maze);
		
		this.pathStore = new ShortestPathStore();
	}
	
	@Override
	public void generateAllPaths() {
		
		Map<PointXY, MazeNode> mazeNodes = getMaze().getNodes();
		Set<PointXY> positions = mazeNodes.keySet();
		for (PointXY pos : positions) {
			allPathsSingleSource(pos);
		}
		
	}
	
	private void allPathsSingleSource(PointXY pos) {
		
		// Specify the break condition - we don't want to stop until all the 
		// paths have been found.
		BooleanSupplier breakFunc = new BooleanSupplier() {
			@Override
			public boolean getAsBoolean() {
				return false;
			}
		};
		
		// Find the paths from the point to all other points and add them to 
		// the path store
		allPathsSingleSource(pos, pathStore, breakFunc);
		
	}
	
	private void allPathsSingleSource(PointXY pos, ShortestPathStore store, 
			BooleanSupplier breakFunc) {
		
		// Set up our data structures
		Path path = new PointXYPath();
		Queue<PointXY> queue = new  LinkedList<PointXY>();
		
		// Add the initial data
		path.addToEnd(pos);
		queue.add(pos);
		store.addPath(path);
		
		// Get all the nodes in the maze
		Map<PointXY, MazeNode> mazeNodes = getMaze().getNodes();
		
		// Do Breadth-First Search through the maze
		while (!queue.isEmpty()) {
			
			PointXY current = queue.remove();
			Path currentPath = store.getPath(pos, current);
			
			MazeNode node = mazeNodes.get(current);
			Set<PointXY> neighbours = node.getNeighbours();
			
			for (PointXY neighbour : neighbours) {
				
				// Check if we've found a shorter path to this node previously.
				// If so, there's no point going any further on this path, so 
				// bail out.
				if (store.pathExists(pos, neighbour)) {
					Path neighbourPath = store.getPath(pos, neighbour);
					if (neighbourPath.getLength() <= currentPath.getLength() + 1) {
						continue;
					}
				}
				
				Path neighbourPath = currentPath.deepCopy();
				neighbourPath.addToEnd(neighbour);
				store.addPath(neighbourPath);
				
				queue.add(neighbour);
			}
			
			// If we've hit the break condition, stop searching
			if (breakFunc.getAsBoolean()) {
				break;
			}
			
		}
		
	}

	private Path pathToTarget(final PointXY pos, final PointXY target) {
		
		// We want to start with an empty store, as the member store is in an 
		// incomplete state, and may therefore prevent the algorithm finding 
		// our desired path.
		final ShortestPathStore localStore = new ShortestPathStore();
		
		// We want to stop searching as soon as we find the path we're looking 
		// for
		BooleanSupplier breakFunc = new BooleanSupplier() {
			@Override
			public boolean getAsBoolean() {
				return localStore.pathExists(pos, target);
			}
		};
		
		// Find the path
		allPathsSingleSource(pos, localStore, breakFunc);
	
		// Cache the paths we've found, so we don't have to do this work again
		pathStore.addPaths(localStore);
		
		// Return our found path
		return localStore.getPath(pos, target);
	}
	
	@Override
	public Path getPath(PointXY start, PointXY end) {
		if (pathStore.pathExists(start, end)) {
			return pathStore.getPath(start, end);
		} else {
			return pathToTarget(start, end);
		}
	}

	@Override
	public Path getPath(PointXY start, Set<PointXY> goals) {
		
		Path shortestPath = null;
		int shortestPathLength = Integer.MAX_VALUE;
		for (PointXY goal : goals) {
			Path path = getPath(start, goal);
			if (path.getLength() < shortestPathLength) {
				shortestPath = path;
				shortestPathLength = path.getLength();
			}
		}
		
		return shortestPath;
	}

	@Override
	public int numStoredPaths() {
		return pathStore.numPaths();
	}

}
