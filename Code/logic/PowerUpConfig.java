package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.powerup.PowerUp;
import logic.powerup.PowerUpType;

/**
 * The configuration for the powerups.
 * 
 * @author Martin Wong
 * @version 2016-03-25
 */
public class PowerUpConfig {
	
	private int numPredPow;
	private Map<PowerUpType, Integer> predatorPowerUps;
	private int numPreyPow;
	private List<PowerUp> preyPowerUps;
	
	/**
	 * Default constructor for PowerConfig.
	 * 
	 * Sets parameters to their default values.
	 */
	public PowerUpConfig() {
		this.numPredPow = 0;
		this.numPreyPow = 0;
		this.predatorPowerUps = new HashMap<PowerUpType, Integer>();
		this.preyPowerUps = new ArrayList<PowerUp>();
		
		initialisePowerUps();
	}
	
	private void initialisePowerUps() {
		int strength = 1;
		for (PowerUpType type : PowerUpType.values()) {
			predatorPowerUps.put(type, strength);
		}
	}
	
	/**
	 * Creates an instance of PowerConfig.
	 * 
	 * @param numPredPow (int)
	 * @param numPreyPow (int)
	 * @param predatorPowerUps (List<PredatorPowerUp>)
	 * @param preyPowerUps (List<PreyPowerUp>)
	 * @param powerUpTypeConfig (PowerUpTypeConfig)
	 */
	public PowerUpConfig(int numPredPow, 
						 Map<PowerUpType, Integer> predatorPowerUps, 
						 int numPreyPow, 
						 List<PowerUp> preyPowerUps) {
		
		this.numPredPow = numPredPow;
		this.numPreyPow = numPreyPow;
		this.predatorPowerUps = predatorPowerUps;
		this.preyPowerUps = preyPowerUps;
	}
	
	/**
	 * Gets the total number of predatorPowerUps.
	 * 
	 * @return numPredPow (int)
	 */
	public int getNumPredPow() {
		return this.numPredPow;
	}
	
	/**
	 * Sets the total number of predatorPowerUps.
	 * 
	 * @param numPredPow (int)
	 */
	public void setNumPredPow(int numPredPow) {
		this.numPredPow = numPredPow;
	}
	
	/**
	 * Gets the total number of preyPowerUps.
	 * 
	 * @return numPreyPow (int)
	 */
	public int getNumPreyPow() {
		return this.numPreyPow;
	}
	
	/**
	 * Sets the total number of preyPowerUps.
	 * 
	 * @param numPreyPow (int)
	 */
	public void setNumPreyPow(int numPreyPow) {
		this.numPreyPow = numPreyPow;
	}
	
	/**
	 * Gets all the powerups for predators.
	 * 
	 * @return predatorPowerUps (List<PredatorPowerUp>)
	 */
	public Map<PowerUpType, Integer> getPredatorPowerUps() {
		return this.predatorPowerUps;
	}
	
	/**
	 * Sets the powerups for predators.
	 * 
	 * @param predatorPowerUps (List<PredatorPowerUp>)
	 */
	public void setPredatorPowerUps(Map<PowerUpType, Integer> predatorPowerUps) {
		this.predatorPowerUps = predatorPowerUps;
	}
	
	/**
	 * Gets all the powerups for prey.
	 * 
	 * @return preyPowerUps (List<PreyPowerUp>)
	 */
	public List<PowerUp> getPreyPowerUps() {
		return this.preyPowerUps;
	}
	
	/**
	 * Sets the powerups for prey.
	 * 
	 * @param preyPowerUps (List<PreyPowerUp>)
	 */
	public void setPreyPowerUps(List<PowerUp> preyPowerUps) {
		this.preyPowerUps = preyPowerUps;
	}
		
}
