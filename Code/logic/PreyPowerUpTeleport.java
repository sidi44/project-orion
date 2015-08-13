package logic;

import geometry.PointXY;

/**
 * This is a power up for preys:
 * it teleports the prey to a random point.
 * 
 * @author Martin Wong
 * @version 2015-08-13
 */
public class PreyPowerUpTeleport extends PreyPowerUp {
	
	private final PointXY nextPoint; // The coordinates to teleport to

	/**
	 * Creates an instance of PreyPowerUpTeleport.
	 * 
	 * @param timeLimit (int)
	 * @param pType (PowerType)
	 * @param nextPoint (PointXY)
	 */
	public PreyPowerUpTeleport(int timeLimit, PreyPowerUpType pType, PointXY nextPoint) {
		super(timeLimit, pType);
		this.nextPoint = nextPoint;
	}
	
	/**
	 * Creates an instance of PreyPowerUpTeleport.
	 * 
	 * @param preyPower (PreyPowerUp)
	 * @param nextPoint (PointXY)
	 */
	public PreyPowerUpTeleport(PreyPowerUp preyPower, PointXY nextPoint) {
		super(preyPower.getTimeLimit(), preyPower.getPType());
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
