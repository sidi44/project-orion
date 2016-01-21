package logic.powerup;

/**
 * A power up for prey which will set the predator's velocity to zero for the 
 * provided amount of time.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public class PreyPowerUpFreezePredator extends PreyPowerUp {

	/**
	 * Constructor for PreyPowerUpFreezePredator.
	 * 
	 * @param timeLimit - the duration of the power up.
	 */
	public PreyPowerUpFreezePredator(int timeLimit) {
		super(timeLimit, PreyPowerUpType.Freeze);
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

}
