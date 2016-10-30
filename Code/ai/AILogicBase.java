package ai;

import logic.Maze;
import pathfinding.PathFinder;
import pathfinding.PathFinderCreator;
import pathfinding.PathFinderType;

public abstract class AILogicBase implements AILogic {

	private PathFinder pathFinder;
	
	public AILogicBase(Maze maze) {
		pathFinder = PathFinderCreator.create(PathFinderType.BFS, maze);
		pathFinder.generateAllPaths();
	}
	
	protected PathFinder getPathFinder() {
		return pathFinder;
	}

}
