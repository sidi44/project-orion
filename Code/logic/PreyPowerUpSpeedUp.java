package logic;

/**
 * This is a power up for preys:
 * it speeds up the prey.
 * 
 * @author Martin Wong
 * @version 2015-08-13
 */
public class PreyPowerUpSpeedUp extends PreyPowerUp {
	
	private final double upFactor; // Factor to increase speed by

	/**
	 * Creates an instance of PreyPowerUpSpeedUp.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param upFactor (double)
	 */
	public PreyPowerUpSpeedUp(int timeLimit, PreyPowerUpType pType, double upFactor) {
		super(timeLimit, pType);
		this.upFactor = upFactor;
	}
	
	/**
	 * Creates an instance of PreyPowerUpSpeedUp.
	 * 
	 * @param preyPower (PreyPowerUp)
	 * @param upFactor (double)
	 */
	public PreyPowerUpSpeedUp(PreyPowerUp preyPower, double upFactor) {
		super(preyPower.getTimeLimit(), preyPower.getPType());
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
