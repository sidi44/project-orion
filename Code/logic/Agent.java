package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.powerup.Magnet;
import logic.powerup.PowerUp;
import geometry.PointXY;

/**
 * Represents an agent which has a position and is able to move.
 * 
 * @author Martin Wong, Simon Dicken
 * @version 2015-12-28
 */
public abstract class Agent {
	
	// Definition
	private final int id;
	private final boolean isPlayer;
	
	// Location and speed
	private PointXY position;
	private int baseSpeedIndex;
	private int variableSpeedIndex;
	
	// Movement and direction
	private Move nextMove;
	private Direction currentDirection;
	private Direction previousDirection;
	
	// PowerUps
	private final Map<Integer, PowerUp> storedPowerUps;
	private final List<PowerUp> activatedPowerUps;
	private final List<PowerUp> powerUpsAppliedToMe;
	private final int maxPowerUp;
	private final boolean stacking;
	private Magnet magnet;
	
	/**
	 * Creates an instance of Agent.
	 * 
	 * @param id - the Agent's ID number (int)
	 * @param position - the Agent's starting position in the maze (PointXY)
	 * @param isPlayer - whether the Agent is human-controlled (boolean)
	 * @param stacking - whether stacking of activated powerups
	 * 					 is allowed (boolean)
	 */
	public Agent(int id, boolean isPlayer, PointXY position, int baseSpeedIndex, 
			int maxPowerUp) {
		
		this.id = id;
		this.isPlayer = isPlayer;
		
		this.position = position;
		this.baseSpeedIndex = baseSpeedIndex;
		this.variableSpeedIndex = 0;
		
		this.nextMove = new Move();
		this.currentDirection = Direction.None;
		this.previousDirection = Direction.None;
		
		this.storedPowerUps = new HashMap<Integer, PowerUp>();
		this.activatedPowerUps = new ArrayList<PowerUp>();
		this.powerUpsAppliedToMe = new ArrayList<PowerUp>();
		this.stacking = false;
		this.maxPowerUp  = maxPowerUp;
		this.setMagnet(null);
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
	 * Checks to see whether stacking of activated powerups is allowed.
	 * 
	 * @return stacking (boolean)
	 */
	public boolean getStacking() {
		return stacking;
	}
	
	/**
	 * Gets the maximum number of power ups the agent can have.
	 * 
	 * @return maxPowerUp (int)
	 */
	public int getMaxPowerUp() {
		return maxPowerUp;
	}
	
	/**
	 * Returns true if this agent can currently collect a power up.
	 * In other words, the number of power ups this agent currently holds is 
	 * less than the maximum number of power ups the agent can hold.
	 *  
	 * @return true if a power up can be collected by this agent, false 
	 * otherwise.
	 */
	public boolean canCollectPowerUp() {
		return numStoredPowerUps() < maxPowerUp;
	}
	
	/**
	 * The number of power ups this agent is currently holding.
	 * 
	 * @return the number of power ups this agent is currently holding.
	 */
	private int numStoredPowerUps() {
		return storedPowerUps.size();
	}
	
	/**
	 * Add a power up to the agent's stored power ups. The power up will not be
	 * added if the agent is already storing the maximum number of power ups
	 * that it can hold.
	 * 
	 * @param powerUp - the power up to be stored.
	 * @return the index that this power up is stored at, or -1 if the power up
	 * could not be stored.
	 */
	public int addStoredPowerUp(PowerUp powerUp) {
		
		int index = availableStoredPowerUpIndex();
		if (index >= 0) {
			storedPowerUps.put(index, powerUp);
			return index;
		}
		
		return -1;
	}
	
	/**
	 * Obtain the first available index that at which a power up can be stored,
	 * or -1 if no slots are available.
	 * 
	 * @return the first available index that at which a power up can be stored,
	 * or -1 if no slots are available.
	 */
	private int availableStoredPowerUpIndex() {
		
		for (int i = 0; i < maxPowerUp; ++i) {
			if (!storedPowerUps.containsKey(i)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Updates activatedPowers of the agent (i.e. remove expired ones).
	 * 
	 * @param allAgents - all the agents currently active in the game.
	 */
	public boolean updateActivatedPowerUps(List<Agent> allAgents) {
		
		List<PowerUp> toRemove = new ArrayList<PowerUp>();
		
		for (PowerUp powerUp : activatedPowerUps) {
			powerUp.update(allAgents);
			if (!powerUp.isActivated()) {
				toRemove.add(powerUp);
			}
		}
		
		for (PowerUp powerUp : toRemove) {
			activatedPowerUps.remove(powerUp);
		}
		
		return toRemove.size() > 0;
	}
	
	/**
	 * Checks whether the agent has an activated power up.
	 * 
	 * @return true if the agent currently has at least one activated power up, 
	 * false otherwise. (boolean)
	 */
	public boolean hasActivatedPowerUp() {
		return activatedPowerUps.size() > 0;
	}
	
	/**
	 * Returns the list of power ups that the Agent currently has activated.
	 * 
	 * @return the list of power ups that the Agent currently has activated.
	 */
	public List<PowerUp> getActivatedPowerUps() {
		return activatedPowerUps;
	}
	
	/**
	 * Return the selected power up in the Agent's collection of stored power 
	 * ups.
	 * 
	 * @return the selected power up in the Agent's collection of stored power 
	 * ups.
	 */
	public PowerUp getStoredPowerUp(int index) {
		return storedPowerUps.get(index);
	}

	/**
	 * Activate the power up stored at the provided index.
	 * 
	 * The power up will not be activated if:
	 * 	- there is no power up stored at the given index.
	 *  - the Agent already has a power up activated and stacking is not 
	 *  enabled.
	 * 
	 * @param index - the index of the stored power up to acitvate.
	 * @param allAgents - all the agents currently in the game.
	 * @return true if the selected power up is activated, false otherwise.
	 */
	public boolean activatePowerUp(int index, List<Agent> allAgents) {
		
		boolean success = false;
		
		PowerUp powerUp = storedPowerUps.get(index);
		
		if (powerUp != null) {
			
			// The agent allows this power up to be activated if it allows 
			// power ups to be stacked, or it doesn't currently have an 
			// activated power up.
			boolean activationAllowed = getStacking() || !hasActivatedPowerUp();
			
			// Check the agent allows the power up to be activated and the given
			// power up is not already activated (this should never happen...).
			success = activationAllowed && !powerUp.isActivated();

			if (success) {
				activatedPowerUps.add(powerUp);
				storedPowerUps.remove(index);
				powerUp.activate(allAgents);
			}
		}
		
		return success;
	}
	
	/**
	 * Add the provided power up to the list of power ups that are currently 
	 * applied to this agent.
	 * 
	 * @param powerUp - the power up to add.
	 */
	public void powerUpApplied(PowerUp powerUp) {
		powerUpsAppliedToMe.add(powerUp);
	}
	
	/**
	 * Remove the provided power up from the list of power ups that are 
	 * currently applied to this agent.
	 * 
	 * @param powerUp - the power up to remove.
	 */
	public void powerUpTerminated(PowerUp powerUp) {
		powerUpsAppliedToMe.remove(powerUp);
	}
	
	/**
	 * Get the list of power ups that are currently applied to this agent.
	 * 
	 * @return the list of power ups applied to this agent.
	 */
	public List<PowerUp> getPowerUpsAppliedToMe() {
		return powerUpsAppliedToMe;
	}

	/**
	 * Get the direction this agent is currently travelling in.
	 * 
	 * @return - the direction this agent is travelling in.
	 */
	public Direction getCurrentDirection() {
		return currentDirection;
	}

	/**
	 * Set the direction this agent is currently travelling in.
	 * 
	 * @param newDirection - the direction this agent is travelling in.
	 */
	public void setCurrentDirection(Direction newDirection) {
		if (currentDirection != Direction.None) {
			previousDirection = currentDirection;
		}
		this.currentDirection = newDirection;
	}

	/**
	 * Get the previous direction this agent was travelling in. This does not 
	 * include the 'None' state. 
	 * For example, if the agent's direction states have gone 'Right', 'None',
	 * 'Down', then this will return 'Right'.
	 * 
	 * @return the previous direction this agent was travelling in.
	 */
	public Direction getPreviousDirection() {
		return previousDirection;
	}

	/**
	 * Get the agent's current speed index. 
	 * This is the agent's base speed index, plus any variable speed index 
	 * resulting from a power up applied to the agent.
	 * 
	 * @return the agent's current speed index.
	 */
	public int getSpeedIndex() {
		return baseSpeedIndex + variableSpeedIndex;
	}

	/**
	 * Set the variable portion of this agent's speed index.
	 * 
	 * @param variableSpeedIndex - the variable speed index to use for this 
	 * agent.
	 */
	public void setVariableSpeedIndex(int variableSpeedIndex) {
		this.variableSpeedIndex = variableSpeedIndex;
	}

	/**
	 * Returns true if a magnet is currently applied to this agent.
	 * 
	 * @return true if a magnet is currently applied to this agent, false 
	 * otherwise.
	 */
	public boolean magnetApplied() {
		return magnet != null;
	}
	
	/**
	 * Get the magnet currently applied to this agent.
	 * 
	 * @return the magnet applied to this agent.
	 */
	public Magnet getMagnet() {
		return magnet;
	}

	/**
	 * Set the magnet to apply to this agent.
	 * 
	 * @param magnet - the magnet to apply to this agent.
	 */
	public void setMagnet(Magnet magnet) {
		this.magnet = magnet;
	}
	
}

