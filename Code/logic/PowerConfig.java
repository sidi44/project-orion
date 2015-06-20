package logic;

import java.util.List;

/**
 * The configuration for the powerups.
 * 
 * @author Martin Wong
 * @version 2015-06-17
 */
public class PowerConfig {
	private int numPredPow;
	private List<PredatorPowerUp> predatorPowerUps;
	private int numPreyPow;
	private List<PreyPowerUp> preyPowerUps;
	
	/**
	 * Creates an instance of PowerConfig.
	 * 
	 * @param numPredPow (int)
	 * @param numPreyPow (int)
	 * @param predatorPowerUps (List<PredatorPowerUp>)
	 * @param preyPowerUps (List<PreyPowerUp>)
	 */
	public PowerConfig(int numPredPow, List<PredatorPowerUp> predatorPowerUps,
			int numPreyPow, List<PreyPowerUp> preyPowerUps) {
		
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
	public List<PredatorPowerUp> getPredatorPowerUps() {
		return this.predatorPowerUps;
	}
	
	/**
	 * Sets the powerups for predators.
	 * 
	 * @param predatorPowerUps (List<PredatorPowerUp>)
	 */
	public void setPredatorPowreUps(List<PredatorPowerUp> predatorPowerUps) {
		this.predatorPowerUps = predatorPowerUps;
	}
	
	/**
	 * Gets all the powerups for prey.
	 * 
	 * @return preyPowerUps (List<PreyPowerUp>)
	 */
	public List<PreyPowerUp> getPreyPowerUps() {
		return this.preyPowerUps;
	}
	
	/**
	 * Sets the powerups for prey.
	 * 
	 * @param preyPowerUps (List<PreyPowerUp>)
	 */
	public void setPreyPowreUps(List<PreyPowerUp> preyPowerUps) {
		this.preyPowerUps = preyPowerUps;
	}
	
}
