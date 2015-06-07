package logic;

/**
 * Interface for power-ups, which gives predators and prey
 * special powers for a limited time.
 * 
 * @author Martin Wong
 * @version 2015-06-04
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
