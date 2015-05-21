package logic;

import geometry.PointXY;

/**
 * Represents an agent which has a position and is able to move.
 * 
 * @author Martin Wong
 * @version 2015-05-19
 */
public class Agent {
	
	private PointXY position;
	private Move nextMove;
	private boolean isPlayer;
	
	/**
	 * Creates an instance of Agent.
	 * 
	 * @param position (PointXY)
	 * @param isPlayer (boolean)
	 */
	public Agent(PointXY position, boolean isPlayer) {
		this.position = position;
		this.nextMove = new Move();
		this.isPlayer = isPlayer;
	}
	
	/**
	 * Get the current position of the agent.
	 * 
	 * @return position (PointXY)
	 */
	public PointXY getPosition() {
		return this.position;
	}
	
	/**
	 * Sets the position of the agent.
	 * 
	 * @param position (PointXY)
	 */
	public void setPosition(PointXY position) {
		this.position = position;
	}
	
	/**
	 * Gets the next move for the agent.
	 * 
	 * @return nextMove (Move)
	 */
	public Move getNextMove() {
		return this.nextMove;
	}
	
	/**
	 * Sets the next move for the agent.
	 * 
	 * @param nextMove (Move)
	 */
	public void setNextMove(Move nextMove) {
		this.nextMove = nextMove;
	}
	
	/**
	 * Sets the direction for the next move.
	 * 
	 * @param dir (Direction)
	 */
	public void setNextMoveDirection(Direction dir) {
		nextMove.setDirection(dir);
	}
	
	/**
	 * Checks whether the agent is human or computer controlled.
	 * 
	 * @return isPlayer (boolean)
	 */
	public boolean isPlayer() {
		return this.isPlayer;
	}
	
	/**
	 * Sets the control of the agent  to human (true)
	 * or computer (false).
	 * 
	 * @param isPlayer (boolean)
	 */
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	
	
}
