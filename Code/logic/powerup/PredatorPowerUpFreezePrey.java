package logic.powerup;

/**
 * A power up for predators which will set the prey's velocity to zero for the 
 * provided amount of time.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public class PredatorPowerUpFreezePrey extends PredatorPowerUp {

	/**
	 * Constructor for PredatorPowerUpFreezePrey.
	 * 
	 * @param timeLimit - the duration of the power up.
	 */
	public PredatorPowerUpFreezePrey(int timeLimit) {
		super(timeLimit, PredatorPowerUpType.Freeze);
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

}
