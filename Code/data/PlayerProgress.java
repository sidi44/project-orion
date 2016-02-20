package data;

public class PlayerProgress {

	private int predatorSpeed;
	
	// numPowerUps, powerUpStrengths, levelLocked, levelHighScore,
	
	public PlayerProgress() {
		predatorSpeed = 1;
	}

	public int getPredatorSpeed() {
		return predatorSpeed;
	}

	public void setPredatorSpeed(int predatorSpeed) {
		this.predatorSpeed = predatorSpeed;
	}	
	
}
