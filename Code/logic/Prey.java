package logic;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import geometry.PointXY;

/**
 * Represents a prey agent.
 * 
 * @author Martin Wong
 * @version 2015-06-05
 */
public class Prey extends Agent {
	
	private Map<PreyPowerUp, Integer> storedPowers;
	private Map<PreyPowerUp, Date> activatedPowers;
	
	/**
	 * Creates an instance of Prey, using id, position, isPlayer and stacking.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param stacking (boolean)
	 */
	public Prey(int id, PointXY pos, boolean isPlayer, boolean stacking) {
		super(id, pos, isPlayer, stacking);
		
		this.storedPowers = new HashMap<PreyPowerUp, Integer>();
		this.activatedPowers = new HashMap<PreyPowerUp, Date>();
	}
	
	/**
	 * Creates an instance of Prey, using id, position, isPlayer
	 * storedPowers and activatedPowers.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param storedPowers (Map<PreyPowerUp, Integer>)
	 * @param activatedPowers (Map<PreyPowerUp, Date>)
	 */
	public Prey(int id, PointXY pos, boolean isPlayer,
			Map<PreyPowerUp, Integer> storedPowers,
			Map<PreyPowerUp, Date> activatedPowers,
			boolean stacking) {
		super(id, pos, isPlayer, stacking);
		
		this.storedPowers = storedPowers;
		this.activatedPowers = activatedPowers;
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
		
		storedPowers.put(preyPowerUp, amount + 1);
	}
	
	/**
	 * Removes a powerup from the prey's storeedPowers.
	 * 
	 * @param storedPowers (PreyPowerUp)
	 */
	public void removeStoredPower(PreyPowerUp preyPowerUp) {
		if (storedPowers.containsKey(preyPowerUp)) {
			if (storedPowers.get(preyPowerUp) > 1) {
				int amount = storedPowers.get(preyPowerUp);
				storedPowers.put(preyPowerUp, amount - 1);
			} else {
				storedPowers.remove(preyPowerUp);
			}
		}
	}
	
	/**
	 * Gets the activatedPowers of the prey.
	 * 
	 * @return activatedPowers (Map<PreyPowerUp, Integer>)
	 */
	public Map<PreyPowerUp, Date> getActivatedPowers() {
		return activatedPowers;
	}
	
	/**
	 * Adds a powerup to the prey's activatedPowers.
	 * 
	 * @param activatedPowers (PreyPowerUp)
	 */
	public void addActivatedPower(PreyPowerUp preyPowerUp) {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.SECOND, preyPowerUp.getTimeLimit());
		Date endTime = cal.getTime();
		
		activatedPowers.put(preyPowerUp, endTime);
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
		
		// Checks to see whether stacking is allowed (> 1 activated power)
		boolean success = (getStacking() || !hasActivatedPower());
		
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
		Date now = new Date();
		
		for (Map.Entry<PreyPowerUp, Date> aPowers : activatedPowers.entrySet()) {
			PreyPowerUp power = aPowers.getKey();
			Date endTime = aPowers.getValue();
			
			if (!now.before(endTime)) {
				removeActivatedPower(power);
			}
		}
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
