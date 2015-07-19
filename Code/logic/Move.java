package logic;

/**
 * Move class.
 * 
 * Encapsulates a playing agent's move. This is as a minimum defined by 
 * direction (which could be no direction), but may also include using a power
 * up.
 * 
 * @author Simon Dicken
 * @version 2015-07-19
 */
public class Move {

	private Direction dir;
	private boolean usePowerUp;
	
	/**
	 * Constructor for Move.
	 * 
	 * @param dir - the direction the agent should travel in this move.
	 */
	public Move(Direction dir) {
		this.dir = dir;
		this.usePowerUp = false;
	}
	
	/**
	 * Default Constructor for Move.
	 * 
	 * Sets the move direction to None.
	 */
	public Move() {
		this.dir = Direction.None;
		this.usePowerUp = false;
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
	
	/**
	 * Get whether the move includes using a power up.
	 * 
	 * @return true if the move includes using a power up.
	 */
	public boolean getUsePowerUp() {
		return usePowerUp;
	}

	/**
	 * Set whether the move includes using a power up.
	 * 
	 * @param usePowerUp - true if the move should include using a power up.
	 */
	public void setUsePowerUp(boolean usePowerUp) {
		this.usePowerUp = usePowerUp;
	}
}
