package logic;

/**
 * Represents the type of powerup and acts as base class for
 * more complex powerups.
 * 
 * @author Martin Wong
 * @version 2015-01-06
 */
public class Powerup {
	
	private PowerType pType;
	private long timeLimit;
	
	/**
	 * Creates an instance of Powerup.
	 * 
	 * @param pType (PowerType)
	 * @param timeLimit (long)
	 */
	public Powerup(PowerType pType, long timeLimit) {
		this.pType = pType;
		this.timeLimit = timeLimit;
	}
	
	/**
	 * Gets the power type.
	 * 
	 * @return pType (PowerType)
	 */
	public PowerType getPType() {
		return this.pType;
	}
	
	/**
	 * Gets the time limit for the powerup, i.e. duration.
	 * 
	 * @return timeLimit (long)
	 */
	public long getTimeLimit() {
		return this.timeLimit;
	}
	
}
