package logic;

/**
 * This is a power up for predators:
 * it speeds up the predator.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
public class PredatorPowerUpSpeedUp extends PredatorPowerUp {
	
	private final double speedUpFactor; // Factor to increase speed by

	/**
	 * Creates an instance of PredatorPowerUpSpeedUp.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param speedUpFactor (double)
	 */
	public PredatorPowerUpSpeedUp(int timeLimit, PredatorPowerUpType pType, double speedUpFactor) {
		super(timeLimit, pType);
		this.speedUpFactor = speedUpFactor;
	}
	
	/**
	 * Creates an instance of PredatorPowerUpSpeedUp.
	 * 
	 * @param predatorPower (PredatorPowerUp)
	 * @param speedUpFactor (double)
	 */
	public PredatorPowerUpSpeedUp(PredatorPowerUp predatorPower, double speedUpFactor) {
		super(predatorPower.getTimeLimit(), predatorPower.getPType());
		this.speedUpFactor = speedUpFactor;
	}
	
	/**
	 * Gets the speed up factor.
	 * 
	 * @return speedUpFactor (double)
	 */
	public double getUpFactor() {
		return speedUpFactor;
	}
	
}
