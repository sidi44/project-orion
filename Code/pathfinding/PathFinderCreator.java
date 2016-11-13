package pathfinding;

import logic.Maze;

public class PathFinderCreator {
	
	public static PathFinder create(PathFinderType type, Maze maze) {
		
		PathFinder pathFinder = null;
		
		switch (type) {
			case Recursive:
				pathFinder = new RecursivePathFinder(maze);
				break;
			case BFS:
				pathFinder = new BFSPathFinder(maze);
				break;
			default:
				System.err.println("Unknown path finder type");
				break;
		
		}
		
		return pathFinder;
		
	}

}
