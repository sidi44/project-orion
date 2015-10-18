package logic.powerup;

/**
 * A power up for prey which will apply a force on predators within a certain 
 * range pushing them away from the prey. 
 * 
 * The force is defined in Newtons and the range is defined in terms of the 
 * number of maze squares.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public class PreyPowerUpMagnet extends PreyPowerUp {

	private int magnetForce;
	private int magnetRange;
	
	/**
	 * Constructor for PreyPowerUpMagnet.
	 * 
	 * @param timeLimit - the duration of this power up.
	 * @param force - the magnet's force in Newtons.
	 * @param range - the range of the magnet in maze squares.
	 */
	public PreyPowerUpMagnet(int timeLimit, int magnetForce, 
			int magnetRange) {
		super(timeLimit, PreyPowerUpType.Magnet);
		
		this.magnetForce = magnetForce;
		this.magnetRange = magnetRange;
	}

	/**
	 * Return the magnet's force.
	 * 
	 * @return the magnet's force.
	 */
	public int getMagnetForce() {
		return magnetForce;
	}
	
	/**
	 * Return the magnet's range.
	 * 
	 * @return the magnet's range.
	 */
	public int getMagnetRange() {
		return magnetRange;
	}
	
	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

}
