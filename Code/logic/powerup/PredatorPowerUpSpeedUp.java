package logic.powerup;

/**
 * This is a power up for predators: it speeds up the predator.
 * 
 * @author Martin Wong
 * @version 2015-10-18
 */
public class PredatorPowerUpSpeedUp extends PredatorPowerUp {
	
	private final double speedUpFactor; // Factor to increase speed by

	/**
	 * Creates an instance of PredatorPowerUpSpeedUp.
	 * 
	 * @param timeLimit (int)
	 * @param speedUpFactor (double)
	 */
	public PredatorPowerUpSpeedUp(int timeLimit, double speedUpFactor) {
		super(timeLimit, PredatorPowerUpType.SpeedUpPredator);
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
