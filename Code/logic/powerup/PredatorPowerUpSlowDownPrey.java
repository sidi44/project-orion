package logic.powerup;

/**
 * This is a power up for predators: it slows down the prey.
 * 
 * @author Martin Wong
 * @version 2015-10-18
 */
public class PredatorPowerUpSlowDownPrey extends PredatorPowerUp {
	
	private final double slowDownFactor; // Factor to decrease speed by

	/**
	 * Creates an instance of PredatorPowerUpSlowDown.
	 * 
	 * @param timeLimit (int)
	 * @param slowDownFactor (double)
	 */
	public PredatorPowerUpSlowDownPrey(int timeLimit, double slowDownFactor) {
		super(timeLimit, PredatorPowerUpType.SlowDownPrey);
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
