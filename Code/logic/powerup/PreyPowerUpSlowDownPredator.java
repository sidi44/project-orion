package logic.powerup;

/**
 * This is a power up for preys:
 * it slows down the prey.
 * 
 * @author Martin Wong
 * @version 2015-10-18
 */
public class PreyPowerUpSlowDownPredator extends PreyPowerUp {
	
	private final double slowDownFactor; // Factor to decrease speed by

	/**
	 * Creates an instance of PreyPowerUpSlowDown.
	 * 
	 * @param timeLimit (int)
	 * @param slowDownFactor (double)
	 */
	public PreyPowerUpSlowDownPredator(int timeLimit, double slowDownFactor) {
		super(timeLimit, PreyPowerUpType.SlowDownPredator);
		this.slowDownFactor = slowDownFactor;
	}
	
	/**
	 * Gets the slow down factor.
	 * 
	 * @return slowDownFactor (double)
	 */
	public double getSlowDownFactor() {
		return slowDownFactor;
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}
	
}
