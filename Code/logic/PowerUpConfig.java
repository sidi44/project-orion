package logic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import xml.PredatorPowerUpAdapter;
import xml.PreyPowerUpAdapter;

/**
 * The configuration for the powerups.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
@XmlRootElement(name = "PowerUpConfiguration")
public class PowerUpConfig {
	
	private int numPredPow;
	private List<PredatorPowerUp> predatorPowerUps;
	private int numPreyPow;
	private List<PreyPowerUp> preyPowerUps;
	private PowerUpTypeConfig powerUpTypeConfig;
	
	/**
	 * Default constructor for PowerConfig.
	 * 
	 * Sets parameters to their default values.
	 */
	 PowerUpConfig() {
		this.numPredPow = 0;
		this.numPreyPow = 0;
		this.predatorPowerUps = new ArrayList<PredatorPowerUp>();
		this.preyPowerUps = new ArrayList<PreyPowerUp>();
		this.powerUpTypeConfig = new PowerUpTypeConfig();
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
			List<PredatorPowerUp> predatorPowerUps, int numPreyPow,
			List<PreyPowerUp> preyPowerUps, PowerUpTypeConfig powerUpTypeConfig) {
		
		this.numPredPow = numPredPow;
		this.numPreyPow = numPreyPow;
		this.predatorPowerUps = predatorPowerUps;
		this.preyPowerUps = preyPowerUps;
		this.powerUpTypeConfig = powerUpTypeConfig;
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
	@XmlElement(name = "NumberPredatorPowerUps")
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
	@XmlElement(name = "NumberPreyPowerUps")
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
	@XmlElement (name = "PredatorPowerUps")
	@XmlJavaTypeAdapter (PredatorPowerUpAdapter.class)
	public void setPredatorPowerUps(List<PredatorPowerUp> predatorPowerUps) {
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
	@XmlElement (name = "PreyPowerUps")
	@XmlJavaTypeAdapter (PreyPowerUpAdapter.class)
	public void setPreyPowerUps(List<PreyPowerUp> preyPowerUps) {
		this.preyPowerUps = preyPowerUps;
	}
	
	/**
	 * Gets the powerup type configurations of the game.
	 * 
	 * @return powerUpTypeConfig (PowerUpTypeConfig)
	 */
	public PowerUpTypeConfig getPowerUpTypeConfig() {
		return this.powerUpTypeConfig;
	}
	
	/**
	 * Sets the powerup type configurations of the game.
	 * 
	 * @param powerUpTypeConfig (PowerUpTypeConfig)
	 */
	public void setPowerUpTypeConfig(PowerUpTypeConfig powerUpTypeConfig) {
		this.powerUpTypeConfig = powerUpTypeConfig;
	}
	
}
