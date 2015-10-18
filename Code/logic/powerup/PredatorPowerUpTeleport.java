package logic.powerup;

import geometry.PointXY;

/**
 * This is a power up for predators:
 * it teleports the predator to a random point.
 * 
 * @author Martin Wong
 * @version 2015-10-18
 */
public class PredatorPowerUpTeleport extends PredatorPowerUp {
	
	private PointXY nextPoint; // The coordinates to teleport to

	/**
	 * Creates an instance of PredatorPowerUpTeleport.
	 */
	public PredatorPowerUpTeleport() {
		super(1, PredatorPowerUpType.Teleport);
		this.nextPoint = new PointXY(0, 0);
	}
	
	/**
	 * Gets the coordinates to teleport to.
	 * 
	 * @return nextPoint (PointXY)
	 */
	public PointXY getNextPoint() {
		return nextPoint;
	}
	
	/**
	 * Set the teleport coordinate position.
	 * 
	 * @param point - the maze coordinate to which to teleport.
	 */
	public void setNextPoint(PointXY point) {
		this.nextPoint = point;
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}
	
}
