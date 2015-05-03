package logic;

/**
 * Move class.
 * Encapsulates a playing agent's move. 
 * This is as a minimum defined by direction (which could be no direction), 
 * but may also include using a powerup (to be implemented later).
 * 
 * @author Simon Dicken
 * @version 2015-05-03
 */
public class Move {

	private MoveDir dir;
	// Defines the move direction.
	
	/**
	 * Constructor for Move.
	 * 
	 * @param dir - the direction the agent should travel in this move.
	 */
	public Move(MoveDir dir) {
		this.dir = dir;
	}
	
	public MoveDir getDirection() {
		return dir;
	}
	
}
