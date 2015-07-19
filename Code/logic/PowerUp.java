package logic;

/**
 * Interface for power-ups for both prey and predators.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
public abstract class PowerUp {
	
	private final int timeLimit;
	private int timeRemaining;
	
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
	
	public int getTimeRemaining() {
		return timeRemaining;
	}
	
	public void decrementTimeRemaining() {
		if (timeRemaining > 0) {
			--timeRemaining;
		}
	}
	
	public void activate() {
		timeRemaining = timeLimit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + timeLimit;
		result = prime * result + timeRemaining;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
