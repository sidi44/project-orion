package logic.powerup;

/**
 * Represents the type of powerup and acts as base class for more complex 
 * predator powerups.
 * 
 * @author Martin Wong
 * @version 2015-10-18
 */
public abstract class PredatorPowerUp extends PowerUp {
	
	private final PredatorPowerUpType pType;
	
	/**
	 * Creates an instance of PredatorPowerUp.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 */
	public PredatorPowerUp(int timeLimit, PredatorPowerUpType pType) {
		super(timeLimit);
		this.pType = pType;
	}
	
	/**
	 * Gets the power type.
	 * 
	 * @return pType (PowerType)
	 */
	public PredatorPowerUpType getPType() {
		return this.pType;
	}
	
	@Override
	public int getPowerVal() {
		return getPType().ordinal();
	}
	
	@Override
	public String getName() {
		return getPType().name();
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
		if (!(obj instanceof PredatorPowerUp)) {
			return false;
		}
		PredatorPowerUp other = (PredatorPowerUp) obj;
		if (pType != other.pType) {
			return false;
		}
		return true;
	}
	
}
