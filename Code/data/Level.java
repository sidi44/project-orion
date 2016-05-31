package data;

import geometry.PointXY;

import java.util.List;
import java.util.Map;

import logic.MazeConfig;
import logic.powerup.PowerUpType;

class Level {

	private List<PointXY> mazeDimensions;
	private MazeConfig mazeConfig;
	private int numPrey;
	private int preySpeedIndex;
	private int numPowerUps;
	private List<PowerUpType> powerUpTypes;
	private Map<Integer, Integer> starScores;
	
	public Level() {
		
	}

	public List<PointXY> getMazeDimensions() {
		return mazeDimensions;
	}
	
	public MazeConfig getMazeConfig() {
		return mazeConfig;
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
	
	public List<PowerUpType> getPowerUpTypes() {
		return powerUpTypes;
	}

	public Map<Integer, Integer> getStarScores() {
		return starScores;
	}
	
}
