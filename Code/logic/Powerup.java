package logic;

/**
 * Represents the type of powerup and acts as base class for
 * more complex powerups.
 * 
 * @author Martin Wong
 * @version 2015-05-19
 */
public class Powerup {
	
	private PowerType pType;
	
	/**
	 * Creates an instance of Powerup.
	 * 
	 * @param pType (PowerType)
	 */
	public Powerup(PowerType pType) {
		this.pType = pType;
	}
	
	/**
	 * Gets the power type.
	 * 
	 * @return pType (PowerType)
	 */
	public PowerType getPType() {
		return this.pType;
	}
	
}
