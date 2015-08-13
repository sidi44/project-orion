package logic;

/**
 * This is a power up for predators:
 * it slows down the prey.
 * 
 * @author Martin Wong
 * @version 2015-08-13
 */
public class PredatorPowerUpSlowDown extends PredatorPowerUp {
	
	private final double downFactor; // Factor to decrease speed by

	/**
	 * Creates an instance of PredatorPowerUpSlowDown.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param downFactor (double)
	 */
	public PredatorPowerUpSlowDown(int timeLimit, PredatorPowerUpType pType, double downFactor) {
		super(timeLimit, pType);
		this.downFactor = downFactor;
	}
	
	/**
	 * Creates an instance of PredatorPowerUpSlowDown.
	 * 
	 * @param predatorPower (PredatorPowerUp)
	 * @param downFactor (double)
	 */
	public PredatorPowerUpSlowDown(PredatorPowerUp predatorPower, double downFactor) {
		super(predatorPower.getTimeLimit(), predatorPower.getPType());
		this.downFactor = downFactor;
	}
	
	/**
	 * Gets the slow down factor.
	 * 
	 * @return downFactor (double)
	 */
	public double getDownFactor() {
		return downFactor;
	}
	
}
