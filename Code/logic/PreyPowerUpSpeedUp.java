package logic;

/**
 * This is a power up for preys:
 * it speeds up the prey.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
public class PreyPowerUpSpeedUp extends PreyPowerUp {
	
	private final double speedUpFactor; // Factor to increase speed by

	/**
	 * Creates an instance of PreyPowerUpSpeedUp.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param speedUpFactor (double)
	 */
	public PreyPowerUpSpeedUp(int timeLimit, PreyPowerUpType pType, double speedUpFactor) {
		super(timeLimit, pType);
		this.speedUpFactor = speedUpFactor;
	}
	
	/**
	 * Creates an instance of PreyPowerUpSpeedUp.
	 * 
	 * @param preyPower (PreyPowerUp)
	 * @param speedUpFactor (double)
	 */
	public PreyPowerUpSpeedUp(PreyPowerUp preyPower, double speedUpFactor) {
		super(preyPower.getTimeLimit(), preyPower.getPType());
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
