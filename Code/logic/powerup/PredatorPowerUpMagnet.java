package logic.powerup;

/**
 * A power up for predators which will apply a force on prey within a certain 
 * range pulling them towards the predator. 
 * 
 * The force is defined in Newtons and the range is defined in terms of the 
 * number of maze squares.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public class PredatorPowerUpMagnet extends PredatorPowerUp {

	private int force;
	private int range;

	/**
	 * Constructor for PredatorPowerUpMagnet.
	 * 
	 * @param timeLimit - the duration of this power up.
	 * @param force - the magnet's force in Newtons.
	 * @param range - the range of the magnet in maze squares.
	 */
	public PredatorPowerUpMagnet(int timeLimit, int force, int range) {
		super(timeLimit, PredatorPowerUpType.Magnet);
		
		this.force = force;
		this.range = range;
	}

	/**
	 * Return the magnet's force.
	 * 
	 * @return the magnet's force.
	 */
	public int getMagnetForce() {
		return force;
	}
	
	/**
	 * Return the magnet's range.
	 * 
	 * @return the magnet's range.
	 */
	public int getMagnetRange() {
		return range;
	}
	
	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

}
