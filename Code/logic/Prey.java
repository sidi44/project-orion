package logic;

import geometry.PointXY;

/**
 * Represents a prey agent.
 * 
 * @author Martin Wong
 * @version 2015-05-31
 */
public class Prey extends Agent {
	
	/**
	 * Creates an instance of Prey, using id, position and isPlayer.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 */
	public Prey(int id, PointXY pos, boolean isPlayer) {
		super(id, pos, isPlayer);
	}
	
}
