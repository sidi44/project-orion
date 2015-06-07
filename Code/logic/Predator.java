package logic;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import geometry.PointXY;

/**
 * Represents a predator agent.
 * 
 * @author Martin Wong
 * @version 2015-06-05
 */
public class Predator extends Agent {
	
	private Map<PredatorPowerUp, Integer> storedPowers;
	private Map<PredatorPowerUp, Date> activatedPowers;
	
	/**
	 * Creates an instance of Predator, using id, position, isPlayer and stacking.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param stacking (boolean)
	 */
	public Predator(int id, PointXY pos, boolean isPlayer, boolean stacking) {
		super(id, pos, isPlayer, stacking);
		
		this.storedPowers = new HashMap<PredatorPowerUp, Integer>();
		this.activatedPowers = new HashMap<PredatorPowerUp, Date>();
	}
	
	/**
	 * Creates an instance of Predator, using id, position, isPlayer
	 * storedPowers and activatedPowers.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param storedPowers (Map<PredatorPowerUp, Integer>)
	 * @param activatedPowers (Map<PredatorPowerUp, Date>)
	 */
	public Predator(int id, PointXY pos, boolean isPlayer,
			Map<PredatorPowerUp, Integer> storedPowers,
			Map<PredatorPowerUp, Date> activatedPowers,
			boolean stacking) {
		super(id, pos, isPlayer, stacking);
		
		this.storedPowers = storedPowers;
		this.activatedPowers = activatedPowers;
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
		
		storedPowers.put(predatorPowerUp, amount + 1);
	}
	
	/**
	 * Removes a powerup from the predator's storeedPowers.
	 * 
	 * @param storedPowers (PredatorPowerUp)
	 */
	public void removeStoredPower(PredatorPowerUp predatorPowerUp) {
		if (storedPowers.containsKey(predatorPowerUp)) {
			if (storedPowers.get(predatorPowerUp) > 1) {
				int amount = storedPowers.get(predatorPowerUp);
				storedPowers.put(predatorPowerUp, amount - 1);
			} else {
				storedPowers.remove(predatorPowerUp);
			}
		}
	}
	
	/**
	 * Gets the activatedPowers of the predator.
	 * 
	 * @return activatedPowers (Map<PredatorPowerUp, Integer>)
	 */
	public Map<PredatorPowerUp, Date> getActivatedPowers() {
		return activatedPowers;
	}
	
	/**
	 * Adds a powerup to the predator's activatedPowers.
	 * 
	 * @param activatedPowers (PredatorPowerUp)
	 */
	public void addActivatedPower(PredatorPowerUp predatorPowerUp) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.SECOND, predatorPowerUp.getTimeLimit());
		Date endTime = cal.getTime();
		
		activatedPowers.put(predatorPowerUp, endTime);
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
		
		// Checks to see whether stacking is allowed (> 1 activated power)
		boolean success = getStacking() || !hasActivatedPower();
		
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
		Date now = new Date();
		
		for (Map.Entry<PredatorPowerUp, Date> aPowers : activatedPowers.entrySet()) {
			PredatorPowerUp power = aPowers.getKey();
			Date endTime = aPowers.getValue();
			
			if (!now.before(endTime)) {
				removeActivatedPower(power);
			}
		}
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
