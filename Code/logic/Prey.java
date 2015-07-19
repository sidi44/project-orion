package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geometry.PointXY;

/**
 * Represents a prey agent.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
public class Prey extends Agent {
	
	private Map<PreyPowerUp, Integer> storedPowers;
	private List<PreyPowerUp> activatedPowers;
	
	/**
	 * Creates an instance of Prey, using id, position, isPlayer
	 * and stacking.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param stacking (boolean)
	 */
	public Prey(int id, PointXY pos, boolean isPlayer, boolean stacking) {
		super(id, pos, isPlayer, stacking);
		
		this.storedPowers = new HashMap<PreyPowerUp, Integer>();
		this.activatedPowers = new ArrayList<PreyPowerUp>();
	}
	
	/**
	 * Gets the storedPowers of the prey.
	 * 
	 * @return storedPowers (Map<PreyPowerUp, Integer>)
	 */
	public Map<PreyPowerUp, Integer> getStoredPowers() {
		return storedPowers;
	}
	
	/**
	 * Adds a powerup to the prey's storedPowers.
	 * 
	 * @param storedPowers (PreyPowerUp)
	 */
	public void addStoredPower(PreyPowerUp preyPowerUp) {
		
		int amount = 0;
		
		if (storedPowers.containsKey(preyPowerUp)) {
			amount = storedPowers.get(preyPowerUp);
		}
		
		// Either adds or overwrites
		storedPowers.put(preyPowerUp, amount + 1);
	}
	
	/**
	 * Removes a powerup from the prey's storeedPowers.
	 * 
	 * @param storedPowers (PreyPowerUp)
	 */
	public void removeStoredPower(PreyPowerUp preyPowerUp) {
		if (storedPowers.containsKey(preyPowerUp)) {
			int updatedAmount = storedPowers.get(preyPowerUp) - 1;
			
			if (updatedAmount <= 0) {
				storedPowers.remove(preyPowerUp);
			} else {
				storedPowers.put(preyPowerUp, updatedAmount);
			}
		}
	}
	
	/**
	 * Gets the activatedPowers of the prey.
	 * 
	 * @return activatedPowers (List<PreyPowerUp>)
	 */
	public List<PreyPowerUp> getActivatedPowers() {
		return activatedPowers;
	}
	
	/**
	 * Adds a powerup to the prey's activatedPowers.
	 * 
	 * @param activatedPowers (PreyPowerUp)
	 */
	public void addActivatedPower(PreyPowerUp preyPowerUp) {
		activatedPowers.add(preyPowerUp);
	}
	
	/**
	 * Removes a powerup from the prey's activatedPowers.
	 * 
	 * @param activatedPowers (PreyPowerUp)
	 */
	public void removeActivatedPower(PreyPowerUp preyPowerUp) {
		activatedPowers.remove(preyPowerUp);
	}
	
	/**
	 * Activates the powerup, if conditions are correct.
	 * 
	 * @param preyPowerUp (PreyPowerUp)
	 * @return success (boolean)
	 */
	public boolean activatePowerUp(PreyPowerUp preyPowerUp) {
		
		boolean success = (getStacking() && !isActivated(preyPowerUp))
				|| !hasActivatedPower();

		if (success) {
			addActivatedPower(preyPowerUp);
			removeStoredPower(preyPowerUp);
		}
		
		return success;
	}
	
	/**
	 * Updates activatedPowers of the prey (i.e. remove expired ones).
	 */
	public void updateActivatedPowerUps() {
		for (PreyPowerUp powerUp : activatedPowers) {
			powerUp.decrementTimeRemaining();
			double timeRemaining = powerUp.getTimeRemaining();
			if (timeRemaining <= 0) {
				removeActivatedPower(powerUp);
			}
		}
	}
	
	
	/**
	 * Checks whether the same powerup type has already been activated.
	 * 
	 * @param preyPowerUp (PreyPowerUp)
	 * @return isActivated (boolean)
	 */
	public boolean isActivated(PreyPowerUp preyPowerUp) {
		boolean isActivated = false;
		PreyPowerType pType = preyPowerUp.getPType();
		
		for (PreyPowerUp powerUp : activatedPowers) {
			if (powerUp.getPType() == pType) {
				isActivated = true;
				break;
			}
		}
		
		return isActivated;
	}
	
	/**
	 * Checks whether the prey has an activated power.
	 * 
	 * @return hasActivatedPower (boolean)
	 */
	public boolean hasActivatedPower() {
		return (activatedPowers.size() > 0 );
	}

	@Override
	public PowerUp getFirstStoredPowerUp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activatePowerUp(PowerUp powerUp) {
		// TODO Auto-generated method stub
		
	}
	
}
