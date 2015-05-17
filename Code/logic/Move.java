package logic;

/**
 * Move class.
 * 
 * Encapsulates a playing agent's move. This is as a minimum defined by 
 * direction (which could be no direction), but may also include using a powerup 
 * (to be implemented later).
 * 
 * @author Simon Dicken
 * @version 2015-05-16
 */
public class Move {

	private Direction dir;
	
	/**
	 * Constructor for Move.
	 * 
	 * @param dir - the direction the agent should travel in this move.
	 */
	public Move(Direction dir) {
		this.dir = dir;
	}
	
	/**
	 * Default Constructor for Move.
	 * 
	 * Sets the move direction to None.
	 */
	public Move() {
		this.dir = Direction.None;
	}
	
	/**
	 * Get the direction defined by this move.
	 * 
	 * @return the direction defined by this move.
	 */
	public Direction getDirection() {
		return dir;
	}
	
	/**
	 * Set the direction for this move.
	 * 
	 * @param dir - the direction to set.
	 */
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
	
}
