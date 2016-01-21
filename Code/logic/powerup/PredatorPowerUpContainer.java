package logic.powerup;

/**
 * This class holds a PredatorPowerUp.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */
public class PredatorPowerUpContainer {
	private PredatorPowerUp powerUp;
	private int amount;
	
	/**
	 * Creates an instance of PredatorPowerUpContainer.
	 * 
	 * @param powerUp (PredatorPowerUp)
	 * @param amount (int)
	 */
	public PredatorPowerUpContainer(PredatorPowerUp powerUp, int amount) {
		this.powerUp = powerUp;
		this.amount = amount;
	}
	
	/**
	 * Gets the predator powerup.
	 * 
	 * @return powerUp (PredatorPowerUp)
	 */
	public PredatorPowerUp getPowerUp() {
		return powerUp;
	}
	
	/**
	 * Sets the predator powerup.
	 * 
	 * @return powerUp (PredatorPowerUp)
	 */
	public void setPowerup(PredatorPowerUp powerUp) {
		this.powerUp = powerUp;
	}
	
	/**
	 * Gets the total number of that predator powerup.
	 * 
	 * @return amount (int)
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Gets the total number of that predator powerup.
	 * 
	 * @return amount (int)
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
