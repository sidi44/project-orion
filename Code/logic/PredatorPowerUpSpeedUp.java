package logic;

/**
 * This is a power up for predators:
 * it speeds up the predator.
 * 
 * @author Martin Wong
 * @version 2015-08-13
 */
public class PredatorPowerUpSpeedUp extends PredatorPowerUp {
	
	private final double upFactor; // Factor to increase speed by

	/**
	 * Creates an instance of PredatorPowerUpSpeedUp.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param upFactor (double)
	 */
	public PredatorPowerUpSpeedUp(int timeLimit, PredatorPowerUpType pType, double upFactor) {
		super(timeLimit, pType);
		this.upFactor = upFactor;
	}
	
	/**
	 * Creates an instance of PredatorPowerUpSpeedUp.
	 * 
	 * @param predatorPower (PredatorPowerUp)
	 * @param upFactor (double)
	 */
	public PredatorPowerUpSpeedUp(PredatorPowerUp predatorPower, double upFactor) {
		super(predatorPower.getTimeLimit(), predatorPower.getPType());
		this.upFactor = upFactor;
	}
	
	/**
	 * Gets the speed up factor.
	 * 
	 * @return upFactor (double)
	 */
	public double getUpFactor() {
		return upFactor;
	}
	
}
