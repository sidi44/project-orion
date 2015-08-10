package logic;

/**
 * Represents the type of powerup and acts as base class for
 * more complex prey powerups.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
public class PreyPowerUp extends PowerUp {
	
	private final PreyPowerUpType pType;
	
	/**
	 * Creates an instance of PreyPowerUp.
	 * 
	 * @param pType (PowerType)
	 * @param timeLimit (int)
	 */
	public PreyPowerUp(int timeLimit, PreyPowerUpType pType) {
		super(timeLimit);
		this.pType = pType;
	}
	
	/**
	 * Gets the power type.
	 * 
	 * @return pType (PowerType)
	 */
	public PreyPowerUpType getPType() {
		return this.pType;
	}
	
	@Override
	public int getPowerVal() {
		return getPType().ordinal();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pType == null) ? 0 : pType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof PreyPowerUp)) {
			return false;
		}
		PreyPowerUp other = (PreyPowerUp) obj;
		if (pType != other.pType) {
			return false;
		}
		return true;
	}
	
}
