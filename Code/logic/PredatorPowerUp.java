package logic;

/**
 * Represents the type of powerup and acts as base class for
 * more complex predator powerups.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
public class PredatorPowerUp extends PowerUp {
	
	private final PredatorPowerType pType;
	
	/**
	 * Creates an instance of PredatorPowerUp.
	 * 
	 * @param pType (PowerType)
	 * @param timeLimit (int)
	 */
	public PredatorPowerUp(PredatorPowerType pType, int timeLimit) {
		super(timeLimit);
		this.pType = pType;
	}
	
	/**
	 * Gets the power type.
	 * 
	 * @return pType (PowerType)
	 */
	public PredatorPowerType getPType() {
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
