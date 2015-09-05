package logic;

import geometry.PointXY;

/**
 * This is a power up for predators:
 * it teleports the predator to a random point.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
public class PredatorPowerUpTeleport extends PredatorPowerUp {
	
	private final PointXY nextPoint; // The coordinates to teleport to

	/**
	 * Creates an instance of PredatorPowerUpTeleport.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param nextPoint (PointXY)
	 */
	public PredatorPowerUpTeleport(int timeLimit, PredatorPowerUpType pType, PointXY nextPoint) {
		super(timeLimit, pType);
		this.nextPoint = nextPoint;
	}
	
	/**
	 * Creates an instance of PredatorPowerUpTeleport.
	 * 
	 * @param predatorPower (PredatorPowerUp)
	 * @param nextPoint (PointXY)
	 */
	public PredatorPowerUpTeleport(PredatorPowerUp predatorPower, PointXY nextPoint) {
		super(predatorPower.getTimeLimit(), predatorPower.getPType());
		this.nextPoint = nextPoint;
	}
	
	/**
	 * Gets the coordinates to teleport to.
	 * 
	 * @return nextPoint (PointXY)
	 */
	public PointXY getNextPoint() {
		return nextPoint;
	}
	
}
