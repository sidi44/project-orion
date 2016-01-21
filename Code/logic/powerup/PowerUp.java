package logic.powerup;

/**
 * Abstract class for power-ups for both prey and predators.
 * 
 * @author Martin Wong, Simon Dicken
 * @version 2015-10-18
 */
public abstract class PowerUp {
	
	private final int timeLimit;
	private int timeRemaining;
	
	/**
	 * Constructor for PowerUp.
	 * 
	 * @param timeLimit - the duration of the power up.
	 */
	public PowerUp(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	/**
	 * Gets the numerical value representing the powerup.
	 * 
	 * @return powerVal (int)
	 */
	public abstract int getPowerVal();
	
	/**
	 * Gets the time limit for the powerup, i.e. duration.
	 * 
	 * @return timeLimit (int)
	 */
	public int getTimeLimit() {
		return timeLimit;
	}
	
	/**
	 * Obtain the amount of time remaining for this power up.
	 * 
	 * @return the amount of time remaining for this power up.
	 */
	public int getTimeRemaining() {
		return timeRemaining;
	}
	
	/**
	 * Decrease the time remaining for this power up by 1.
	 * If the current time remaining is already zero, the time remaining is not
	 * decreased.
	 */
	public void decrementTimeRemaining() {
		if (timeRemaining > 0) {
			--timeRemaining;
		}
	}
	
	/**
	 * Activate this power up.
	 */
	public void activate() {
		timeRemaining = timeLimit;
	}

	/**
	 * Return the name of this power up. 
	 * This should be the String representation of the enum type of the power 
	 * up. 
	 * 
	 * @return the name of this power up.
	 */
	public abstract String getName();
	
	/**
	 * Accept the power up visitor.
	 * 
	 * All concrete classes should implement this method and call visit() on the
	 * provided visitor, passing this class as argument.
	 * 
	 * @param visitor - the visitor to accept.
	 */
	public abstract void accept(PowerUpVisitor visitor);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + timeLimit;
		result = prime * result + timeRemaining;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PowerUp)) {
			return false;
		}
		PowerUp other = (PowerUp) obj;
		if (timeLimit != other.timeLimit) {
			return false;
		}
		if (timeRemaining != other.timeRemaining) {
			return false;
		}
		return true;
	}
	
}
