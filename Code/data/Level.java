package data;

import geometry.PointXY;

import java.util.ArrayList;
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
	private Map<String, Integer> starScores;
	
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

	public List<Integer> getStarScores() {
		List<Integer> scores = new ArrayList<Integer>();
		if (starScores.size() == 3) {
			for (int i = 1; i <= 3; ++i) {
				String num = Integer.toString(i);
				int score = starScores.get(num);
				scores.add(score);
			}
		}
		return scores;
	}
	
}
