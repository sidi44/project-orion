package data;

import geometry.PointXY;

import java.util.List;
import java.util.Map;

class Level {

	private List<PointXY> mazeDimensions;
	private int numPrey;
	private int preySpeed;
	private int numPowerUps;
	private int squareSize;
	// maze generation parameters...
	private Map<Integer, Integer> starScores;
	
	public Level() {

	}

	public List<PointXY> getMazeDimensions() {
		return mazeDimensions;
	}

	public int getNumPrey() {
		return numPrey;
	}

	public int getPreySpeed() {
		return preySpeed;
	}

	public int getNumPowerUps() {
		return numPowerUps;
	}

	public int getSquareSize() {
		return squareSize;
	}

	public Map<Integer, Integer> getStarScores() {
		return starScores;
	}
	
}
