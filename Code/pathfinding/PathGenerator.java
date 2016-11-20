package pathfinding;

import progress.ProgressTask;

public class PathGenerator implements ProgressTask {

	private PathFinder pathFinder;
	
	public PathGenerator(PathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}
	
	@Override
	public void run() {
		pathFinder.generateAllPaths();
	}

	@Override
	public float getProgress() {
		int total = pathFinder.numPossiblePaths();
		
		int current = pathFinder.numStoredPaths();
		
		float progress = current * 100f / total;
		return progress;
	}

}
