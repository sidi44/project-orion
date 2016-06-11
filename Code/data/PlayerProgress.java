package data;

import java.util.HashMap;
import java.util.Map;

import logic.powerup.PowerUpType;

public class PlayerProgress {

	private int predatorSpeedIndex;
	private Map<String, Boolean> levelsLocked;
	private Map<String, Integer> levelScores;
	private Map<String, Integer> powerUpDefinitions;
	
	// numPowerUps?
	
	public PlayerProgress() {
		predatorSpeedIndex = 1;
		levelsLocked = new HashMap<String, Boolean>();
		levelScores = new HashMap<String, Integer>();
		powerUpDefinitions = new HashMap<String, Integer>();
	}

	public int getPredatorSpeedIndex() {
		return predatorSpeedIndex;
	}

	public void setPredatorSpeedIndex(int predatorSpeedIndex) {
		this.predatorSpeedIndex = predatorSpeedIndex;
	}
	
	public boolean isLevelLocked(int levelNumber) {
		String num = Integer.toString(levelNumber);
		return levelsLocked.get(num);
	}
	
	public void setLevelLocked(int levelNumber, boolean locked) {
		String num = Integer.toString(levelNumber);
		levelsLocked.put(num, locked);
	}
	
	public int getLevelScore(int levelNumber) {
		String num = Integer.toString(levelNumber);
		return levelScores.get(num);
	}
	
	public void setLevelScore(int levelNumber, int score) {
		String num = Integer.toString(levelNumber);
		
		if (levelScores.containsKey(num)) {
			if (levelScores.get(num) > score) {
				levelScores.put(num, score);
			}
		} else {
			levelScores.put(num, score);
		}	
	}

	public int getPowerUpStrength(PowerUpType type) {
		String typeName = type.toString();
		int strength = powerUpDefinitions.get(typeName);
		return strength;
	}

	public void setPowerUpStrength(PowerUpType type, int strength) {
		String typeName = type.toString();
		powerUpDefinitions.put(typeName, strength);
	}
	
}
