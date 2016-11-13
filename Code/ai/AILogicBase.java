package ai;

import java.util.ArrayList;
import java.util.List;

import logic.Maze;
import pathfinding.PathFinder;
import pathfinding.PathFinderCreator;
import pathfinding.PathFinderType;
import progress.ProgressTask;

public abstract class AILogicBase implements AILogic {

	private PathFinder pathFinder;
	
	public AILogicBase(Maze maze) {
		pathFinder = PathFinderCreator.create(PathFinderType.BFS, maze);
	}
	
	protected PathFinder getPathFinder() {
		return pathFinder;
	}

	@Override
	public List<ProgressTask> getProgressTasks() {
		List<ProgressTask> tasks = new ArrayList<ProgressTask>();
		tasks.add(getPathFinder());
		return tasks;
	}
	
}
