package logic;

/**
 * Represents the type of powerup and acts as base class for
 * more complex prey powerups.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */
public class PreyPowerUp implements PowerUp {
	
	private final PreyPowerType pType;
	private final int timeLimit;
	
	/**
	 * Creates an instance of PreyPowerUp.
	 * 
	 * @param pType (PowerType)
	 * @param timeLimit (int)
	 */
	public PreyPowerUp(PreyPowerType pType, int timeLimit) {
		this.pType = pType;
		this.timeLimit = timeLimit;
	}
	
	/**
	 * Gets the power type.
	 * 
	 * @return pType (PowerType)
	 */
	public PreyPowerType getPType() {
		return this.pType;
	}
	
	/**
	 * Gets the numerical value representing the powerup.
	 * 
	 * @return powerVal (int)
	 */
	@Override
	public int getPowerVal() {
		return getPType().ordinal();
	}
	
	/**
	 * Gets the time limit for the powerup, i.e. duration.
	 * 
	 * @return timeLimit (int)
	 */
	public int getTimeLimit() {
		return this.timeLimit;
	}
	
	/**
	 * Generates hash code for object.
	 * If objects are "equal" then will have same hash code.
	 * 
	 * @return result (int)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pType == null) ? 0 : pType.hashCode());
		result = prime * result + timeLimit;
		return result;
	}
	
	/**
	 * Checks whether two objects are equal.
	 * 
	 * @param o (Object)
	 * @return isEqual (boolean)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PreyPowerUp other = (PreyPowerUp) obj;
		if (pType != other.pType)
			return false;
		if (timeLimit != other.timeLimit)
			return false;
		return true;
	}

	
}
