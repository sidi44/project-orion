package logic;

/**
 * This is a power up for preys:
 * it slows down the prey.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
public class PreyPowerUpSlowDown extends PreyPowerUp {
	
	private final double slowDownFactor; // Factor to decrease speed by

	/**
	 * Creates an instance of PreyPowerUpSlowDown.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param slowDownFactor (double)
	 */
	public PreyPowerUpSlowDown(int timeLimit, PreyPowerUpType pType, double slowDownFactor) {
		super(timeLimit, pType);
		this.slowDownFactor = slowDownFactor;
	}
	
	/**
	 * Creates an instance of PreyPowerUpSlowDown.
	 * 
	 * @param preyPower (PreyPowerUp)
	 * @param slowDownFactor (double)
	 */
	public PreyPowerUpSlowDown(PreyPowerUp preyPower, double slowDownFactor) {
		super(preyPower.getTimeLimit(), preyPower.getPType());
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
