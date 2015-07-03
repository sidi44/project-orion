package logic;

import java.util.HashMap;
import java.util.Map;

import geometry.PointXY;

/**
 * Represents a predator agent.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */
public class Predator extends Agent {
	
	private Map<PredatorPowerUp, Integer> storedPowers;
	private Map<PredatorPowerUp, Integer> activatedPowers;
	
	/**
	 * Creates an instance of Predator, using id, position, isPlayer
	 * and stacking.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param stacking (boolean)
	 */
	public Predator(int id, PointXY pos, boolean isPlayer, boolean stacking) {
		super(id, pos, isPlayer, stacking);
		
		this.storedPowers = new HashMap<PredatorPowerUp, Integer>();
		this.activatedPowers = new HashMap<PredatorPowerUp, Integer>();
	}
	
	/**
	 * Gets the storedPowers of the predator.
	 * 
	 * @return storedPowers (Map<PredatorPowerUp, Integer>)
	 */
	public Map<PredatorPowerUp, Integer> getStoredPowers() {
		return storedPowers;
	}
	
	/**
	 * Adds a powerup to the predator's storedPowers.
	 * 
	 * @param storedPowers (PredatorPowerUp)
	 */
	public void addStoredPower(PredatorPowerUp predatorPowerUp) {
		
		int amount = 0;
		
		if (storedPowers.containsKey(predatorPowerUp)) {
			amount = storedPowers.get(predatorPowerUp);
		}
		
		// Either adds or overwrites
		storedPowers.put(predatorPowerUp, amount + 1);
	}
	
	/**
	 * Removes a powerup from the predator's storeedPowers.
	 * 
	 * @param storedPowers (PredatorPowerUp)
	 */
	public void removeStoredPower(PredatorPowerUp predatorPowerUp) {
		if (storedPowers.containsKey(predatorPowerUp)) {
			int updatedAmount = storedPowers.get(predatorPowerUp) - 1;
			
			if (updatedAmount <= 0) {
				storedPowers.remove(predatorPowerUp);
			} else {
				storedPowers.put(predatorPowerUp, updatedAmount);
			}
		}
	}
	
	/**
	 * Gets the activatedPowers of the predator.
	 * 
	 * @return activatedPowers (Map<PredatorPowerUp, Integer>)
	 */
	public Map<PredatorPowerUp, Integer> getActivatedPowers() {
		return activatedPowers;
	}
	
	/**
	 * Adds a powerup to the predator's activatedPowers.
	 * 
	 * @param activatedPowers (PredatorPowerUp)
	 */
	public void addActivatedPower(PredatorPowerUp predatorPowerUp) {
		activatedPowers.put(predatorPowerUp, predatorPowerUp.getTimeLimit());
	}
	
	/**
	 * Removes a powerup from the predator's activatedPowers.
	 * 
	 * @param activatedPowers (PredatorPowerUp)
	 */
	public void removeActivatedPower(PredatorPowerUp predatorPowerUp) {
		activatedPowers.remove(predatorPowerUp);
	}
	
	/**
	 * Activates the powerup, if conditions are correct.
	 * 
	 * @param predatorPowerUp (PredatorPowerUp)
	 * @return success (boolean)
	 */
	public boolean activatePowerUp(PredatorPowerUp predatorPowerUp) {
		
		boolean success = (getStacking() && !isActivated(predatorPowerUp))
				|| !hasActivatedPower();

		if (success) {
			addActivatedPower(predatorPowerUp);
			removeStoredPower(predatorPowerUp);
		}
		
		return success;
	}
	
	/**
	 * Updates activatedPowers of the predator (i.e. remove expired ones).
	 */
	public void updateActivatedPowerUps() {
		
		for (Map.Entry<PredatorPowerUp, Integer> aPowers : activatedPowers.entrySet()) {
			PredatorPowerUp power = aPowers.getKey();
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
	 * @param predatorPowerUp (PredatorPowerUp)
	 * @return isActivated (boolean)
	 */
	public boolean isActivated(PredatorPowerUp predatorPowerUp) {
		boolean isActivated = false;
		PredatorPowerType pType = predatorPowerUp.getPType();
		
		for (Map.Entry<PredatorPowerUp, Integer> aPowers : activatedPowers.entrySet()) {
			if (aPowers.getKey().getPType() == pType) {
				isActivated = true;
				break;
			}
		}
		return isActivated;
	}
	
	/**
	 * Checks whether the predator has an activated power.
	 * 
	 * @return hasActivatedPower (boolean)
	 */
	public boolean hasActivatedPower() {
		return (activatedPowers.size() > 0 );
	}
	
	
	
	
}
