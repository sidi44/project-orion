package logic;

/**
 * Interface for power-ups for both prey and predators.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */
public interface PowerUp {
	
	/**
	 * Gets the numerical value representing the powerup.
	 * 
	 * @return powerVal (int)
	 */
	public int getPowerVal();
	
	/**
	 * Gets the time limit for the powerup, i.e. duration.
	 * 
	 * @return timeLimit (int)
	 */
	public int getTimeLimit();
}
