package logic.powerup;

/**
 * This is a power up for preys:
 * it speeds up the prey.
 * 
 * @author Martin Wong
 * @version 2015-10-18
 */
public class PreyPowerUpSpeedUp extends PreyPowerUp {
	
	private final double speedUpFactor; // Factor to increase speed by

	/**
	 * Creates an instance of PreyPowerUpSpeedUp.
	 * 
	 * @param timeLimit (int)
	 * @param speedUpFactor (double)
	 */
	public PreyPowerUpSpeedUp(int timeLimit, double speedUpFactor) {
		super(timeLimit, PreyPowerUpType.SpeedUpPrey);
		this.speedUpFactor = speedUpFactor;
	}
	
	/**
	 * Gets the speed up factor.
	 * 
	 * @return speedUpFactor (double)
	 */
	public double getSpeedUpFactor() {
		return speedUpFactor;
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}
	
}
