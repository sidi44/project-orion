package logic;

import java.util.ArrayList;
import java.util.List;

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
	private final List<PowerUp> storedPowerUps;
	private final List<PowerUp> activatedPowerUps;
	private final List<PowerUp> powerUpsAppliedToMe;
	private int selectedPowerUp;
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
		
		this.storedPowerUps = new ArrayList<PowerUp>();
		this.activatedPowerUps = new ArrayList<PowerUp>();
		this.powerUpsAppliedToMe = new ArrayList<PowerUp>();
		this.selectedPowerUp = -1;
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
	
	public void addStoredPowerUp(PowerUp powerUp) {
		
		storedPowerUps.add(powerUp);
		
		if (storedPowerUps.size() == 1) {
			selectedPowerUp = 0;
		}
	}
	
	/**
	 * Updates activatedPowers of the agent (i.e. remove expired ones).
	 */
	public boolean updateActivatedPowerUps(List<Agent> allAgent) {
		
		List<PowerUp> toRemove = new ArrayList<PowerUp>();
		
		for (PowerUp powerUp : activatedPowerUps) {
			powerUp.update(allAgent);
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
	 * Checks whether the agent has an activated power.
	 * 
	 * @return hasActivatedPower (boolean)
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
	public PowerUp getSelectedStoredPowerUp() {
		if (isSelectedValid()) {
			return storedPowerUps.get(selectedPowerUp);
		} else {
			return null;
		}
	}

	/**
	 * Activate the selected power up.
	 * 
	 * The power up will not be activated if:
	 * 	- the power up is not currently stored by the agent.
	 *  - the Agent already has a power up activated and stacking is not 
	 *  enabled.
	 *  - the power up is already activated.
	 * 
	 * @return true if the selected power up is activated, false otherwise.
	 */
	public boolean activatePowerUp(List<Agent> allAgents) {
		
		boolean success = false;
		
		if (isSelectedValid()) {
			PowerUp powerUp = storedPowerUps.get(selectedPowerUp);
			
			// The agent allows this power up to be activated if it allows 
			// power ups to be stacked, or it doesn't currently have an 
			// activated power up.
			boolean activationAllowed = getStacking() || !hasActivatedPowerUp();
			
			// Check the agent allows the power up to be activated and the given
			// power up is not already activated.
			success = activationAllowed && !powerUp.isActivated();

			if (success) {
				activatedPowerUps.add(powerUp);
				storedPowerUps.remove(powerUp);
				powerUp.activate(allAgents);
			}
		}
		
		return success;
		
	}
	
	public void powerUpApplied(PowerUp powerUp) {
		powerUpsAppliedToMe.add(powerUp);
	}
	
	public void powerUpTerminated(PowerUp powerUp) {
		powerUpsAppliedToMe.remove(powerUp);
	}
	
	public List<PowerUp> getPowerUpsAppliedToMe() {
		return powerUpsAppliedToMe;
	}
	
	/**
	 * Checks whether the selectedPowerUp is valid.
	 * 
	 * @return isValid (boolean)
	 */
	public boolean isSelectedValid() {
		//boolean inRangeMax = selectedPowerUp <= getMaxPowerUp();
		boolean inRange = selectedPowerUp >= 0 && 
						  selectedPowerUp < storedPowerUps.size();
		
		return inRange;
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(Direction newDirection) {
		if (currentDirection != Direction.None) {
			previousDirection = currentDirection;
		}
		this.currentDirection = newDirection;
	}

	public Direction getPreviousDirection() {
		return previousDirection;
	}

	public int getSpeedIndex() {
		return baseSpeedIndex + variableSpeedIndex;
	}

	public void setVariableSpeedIndex(int variableSpeedIndex) {
		this.variableSpeedIndex = variableSpeedIndex;
	}

	public boolean magnetApplied() {
		return magnet != null;
	}
	
	public Magnet getMagnet() {
		return magnet;
	}

	public void setMagnet(Magnet magnet) {
		this.magnet = magnet;
	}
	
}

