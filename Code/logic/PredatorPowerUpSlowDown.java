package logic;

/**
 * This is a power up for predators:
 * it slows down the prey.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
public class PredatorPowerUpSlowDown extends PredatorPowerUp {
	
	private final double slowDownFactor; // Factor to decrease speed by

	/**
	 * Creates an instance of PredatorPowerUpSlowDown.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param slowDownFactor (double)
	 */
	public PredatorPowerUpSlowDown(int timeLimit, PredatorPowerUpType pType, double slowDownFactor) {
		super(timeLimit, pType);
		this.slowDownFactor = slowDownFactor;
	}
	
	/**
	 * Creates an instance of PredatorPowerUpSlowDown.
	 * 
	 * @param predatorPower (PredatorPowerUp)
	 * @param slowDownFactor (double)
	 */
	public PredatorPowerUpSlowDown(PredatorPowerUp predatorPower, double slowDownFactor) {
		super(predatorPower.getTimeLimit(), predatorPower.getPType());
		this.slowDownFactor = slowDownFactor;
	}
	
	/**
	 * Gets the slow down factor.
	 * 
	 * @return slowDownFactor (double)
	 */
	public double getDownFactor() {
		return slowDownFactor;
	}
	
}
