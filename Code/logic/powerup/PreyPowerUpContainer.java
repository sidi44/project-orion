package logic.powerup;

/**
 * This class holds a PreyPowerUp.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */
public class PreyPowerUpContainer {
	private PreyPowerUp powerUp;
	private int amount;
	
	/**
	 * Creates an instance of PreyPowerUpContainer.
	 * 
	 * @param powerUp (PreyPowerUp)
	 * @param amount (int)
	 */
	public PreyPowerUpContainer(PreyPowerUp powerUp, int amount) {
		this.powerUp = powerUp;
		this.amount = amount;
	}
	
	/**
	 * Gets the prey powerup.
	 * 
	 * @return powerUp (PreyPowerUp)
	 */
	public PreyPowerUp getPowerUp() {
		return powerUp;
	}
	
	/**
	 * Sets the prey powerup.
	 * 
	 * @return powerUp (PreyPowerUp)
	 */
	public void setPowerup(PreyPowerUp powerUp) {
		this.powerUp = powerUp;
	}
	
	/**
	 * Gets the total number of that prey powerup.
	 * 
	 * @return amount (int)
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Gets the total number of that prey powerup.
	 * 
	 * @return amount (int)
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
