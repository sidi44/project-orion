package logic;

import geometry.PointXY;

/**
 * Represents a predator agent.
 * 
 * @author Martin Wong
 * @version 2015-05-31
 */
public class Predator extends Agent {
	
	/**
	 * Creates an instance of Predator, using id, position and isPlayer.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 */
	public Predator(int id, PointXY pos, boolean isPlayer) {
		super(id, pos, isPlayer);
	}
	
}
