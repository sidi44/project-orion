package logic;

import geometry.PointXY;

/**
 * Represents an agent which has a position and is able to move.
 * 
 * @author Martin Wong, Simon Dicken
 * @version 2015-06-11
 */
public abstract class Agent {

	private PointXY position;
	private Move nextMove;
	private final int id;
	private boolean isPlayer;
	private boolean stacking;
	private boolean inTransition;
	
	/**
	 * Creates an instance of Agent.
	 * 
	 * @param id - the Agent's ID number (int)
	 * @param position - the Agent's starting position in the maze (PointXY)
	 * @param isPlayer - whether the Agent is human-controlled (boolean)
	 * @param stacking - whether stacking of activated powerups
	 * 					 is allowed (boolean)
	 */
	public Agent(int id, PointXY position, boolean isPlayer, boolean stacking) {
		this.id = id;
		this.nextMove = new Move();
		this.position = position;
		this.isPlayer = isPlayer;
		this.stacking = stacking;
		this.inTransition = false;
	}
	
	/**
	 * Get the current position of the agent.
	 * 
	 * @return position - the Agent's current position in the maze (PointXY)
	 */
	public PointXY getPosition() {
		return position;
	}
	
	/**
	 * Sets the position of the agent.
	 * 
	 * @param position - the Agent's position in the maze (PointXY)
	 */
	public void setPosition(PointXY position) {
		this.position = position;
	}
	
	/**
	 * Gets the next move for the agent.
	 * 
	 * @return nextMove - the move the Agent will make at the next step of the 
	 * simulation. (Move)
	 */
	public Move getNextMove() {
		return nextMove;
	}
	
	/**
	 * Sets the next move for the agent.
	 * 
	 * @param nextMove - the move the Agent will make at the next step of the 
	 * simulation. (Move)
	 */
	public void setNextMove(Move nextMove) {
		this.nextMove = nextMove;
	}
	
	/**
	 * Sets only the direction part of the next move.
	 * 
	 * @param dir - the direction of travel for the next move (Direction)
	 */
	public void setNextMoveDirection(Direction dir) {
		nextMove.setDirection(dir);
	}
	
	/**
	 * Get the Agent's ID number.
	 * 
	 * @return id - the Agent's ID number (int)
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Get whether the Agent is human-controlled.
	 * 
	 * @return isPlayer - true if the Agent is human-controlled (boolean) 
	 */
	public boolean isPlayer() {
		return isPlayer;
	}
	
	/**
	 * Sets the control of the Agent (human or computer).
	 * 
	 * @param isPlayer (boolean)
	 */
	public void setIsPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	
	/**
	 * Get whether the Agent is in transition. 
	 * 
	 * Agent's are in transition when they are moving into a new Maze square. 
	 * The inTransition flag indicates that the Agent is only partially in the 
	 * new Maze square and part of its area is still in the Maze square it was
	 * leaving.
	 * 
	 * @return inTransition - true if the Agent is transitioning into a new Maze
	 * square (boolean)
	 */
	public boolean isInTransition() {
		return inTransition;
	}
	
	/**
	 * Set whether the Agent is in transition.
	 * 
	 * Agent's are in transition when they are moving into a new Maze square. 
	 * The inTransition flag indicates that the Agent is only partially in the 
	 * new Maze square and part of its area is still in the Maze square it was
	 * leaving.
	 * 
	 * @param inTransition - true if the Agent is transitioning into a new Maze
	 * square (boolean)
	 */
	public void setInTransition(boolean inTransition) {
		this.inTransition = inTransition;
	}
	
	/**
	 * Checks to see whether stacking of activated powerups is allowed.
	 * 
	 * @return stacking (boolean)
	 */
	public boolean getStacking() {
		return stacking;
	}
	
	/**
	 * Sets the stacking status to determine whether stacking of activated
	 * powerups is allowed.
	 * 
	 * @param stacking (boolean)
	 */
	public void setStacking(boolean stacking) {
		this.stacking = stacking;
	}
	
	/**
	 * Updates activatedPowers of the agent (i.e. remove expired ones).
	 */
	public abstract void updateActivatedPowerUps();
	
	/**
	 * Checks whether the agent has an activated power.
	 * 
	 * @return hasActivatedPower (boolean)
	 */
	public abstract boolean hasActivatedPower();
	
}