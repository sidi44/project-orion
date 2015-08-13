package logic;

/**
 * This is a power up for preys:
 * it slows down the predator.
 * 
 * @author Martin Wong
 * @version 2015-08-13
 */
public class PreyPowerUpSlowDown extends PreyPowerUp {
	
	private final double downFactor; // Factor to decrease speed by

	/**
	 * Creates an instance of PreyPowerUpSlowDown.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param downFactor (double)
	 */
	public PreyPowerUpSlowDown(int timeLimit, PreyPowerUpType pType, double downFactor) {
		super(timeLimit, pType);
		this.downFactor = downFactor;
	}
	
	/**
	 * Creates an instance of PreyPowerUpSlowDown.
	 * 
	 * @param preyPower (PreyPowerUp)
	 * @param downFactor (double)
	 */
	public PreyPowerUpSlowDown(PreyPowerUp preyPower, double downFactor) {
		super(preyPower.getTimeLimit(), preyPower.getPType());
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
