package data;

import geometry.PointXY;

import java.util.List;
import java.util.Map;

class Level {

	private List<PointXY> mazeDimensions;
	private int numPrey;
	private int preySpeedIndex;
	private int numPowerUps;
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

	public int getPreySpeedIndex() {
		return preySpeedIndex;
	}

	public int getNumPowerUps() {
		return numPowerUps;
	}

	public Map<Integer, Integer> getStarScores() {
		return starScores;
	}
	
}
