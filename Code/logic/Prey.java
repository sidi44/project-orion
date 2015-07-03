package logic;

import java.util.HashMap;
import java.util.Map;

import geometry.PointXY;

/**
 * Represents a prey agent.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */
public class Prey extends Agent {
	
	private Map<PreyPowerUp, Integer> storedPowers;
	private Map<PreyPowerUp, Integer> activatedPowers;
	
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
		this.activatedPowers = new HashMap<PreyPowerUp, Integer>();
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
	 * @return activatedPowers (Map<PreyPowerUp, Integer>)
	 */
	public Map<PreyPowerUp, Integer> getActivatedPowers() {
		return activatedPowers;
	}
	
	/**
	 * Adds a powerup to the prey's activatedPowers.
	 * 
	 * @param activatedPowers (PreyPowerUp)
	 */
	public void addActivatedPower(PreyPowerUp preyPowerUp) {
		activatedPowers.put(preyPowerUp, preyPowerUp.getTimeLimit());
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
		
		for (Map.Entry<PreyPowerUp, Integer> aPowers : activatedPowers.entrySet()) {
			PreyPowerUp power = aPowers.getKey();
			Integer updatedTimeLeft = aPowers.getValue() - 1;
			
			if (updatedTimeLeft <= 0) {
				removeActivatedPower(power);
			} else {
				activatedPowers.put(power, updatedTimeLeft);
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
		
		for (Map.Entry<PreyPowerUp, Integer> aPowers : activatedPowers.entrySet()) {
			if (aPowers.getKey().getPType() == pType) {
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
	
	
	
	
}
