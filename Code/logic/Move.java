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
	private int usePowerUpIndex;
	private GameOver forceGameOver;
	
	/**
	 * Constructor for Move.
	 * 
	 * @param dir - the direction the agent should travel in this move.
	 */
	public Move(Direction dir) {
		this.dir = dir;
		this.usePowerUpIndex = -1;
		this.forceGameOver = GameOver.No;
	}
	
	/**
	 * Default Constructor for Move.
	 * 
	 * Sets the move direction to None.
	 */
	public Move() {
		clear();
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
		return usePowerUpIndex != -1;
	}

	/**
	 * Get the index of the power up to use this move.
	 * 
	 * @return the index of the power up to use this move.
	 */
	public int getUsePowerUpIndex() {
		return usePowerUpIndex;
	}
	
	/**
	 * Set the power up index for this move.
	 * Set this to -1 to indicate that no power up should be used this move.
	 * 
	 * @param powerUpIndex - the index of the agent's stored power up to use 
	 * this move. 
	 */
	public void setUsePowerUpIndex(int powerUpIndex) {
		this.usePowerUpIndex = powerUpIndex;
	}
	
	public GameOver getForceGameOver() {
		return forceGameOver;
	}
	
	public void setForceGameOver(GameOver forceGameOver) {
		this.forceGameOver = forceGameOver;
	}
	
	/**
	 * Remove any move information. 
	 * More specifically, this sets the move direction to 'None' and sets the 
	 * use power up index to -1 to indicate no power up should be used.
	 */
	public void clear() {
		setDirection(Direction.None);
		setUsePowerUpIndex(-1);
		setForceGameOver(GameOver.No);
	}
}
