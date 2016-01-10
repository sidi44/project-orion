package logic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import logic.powerup.PredatorPowerUp;
import logic.powerup.PredatorPowerUpFreezePrey;
import logic.powerup.PredatorPowerUpMagnet;
import logic.powerup.PredatorPowerUpSlowDownPrey;
import logic.powerup.PredatorPowerUpSpeedUp;
import logic.powerup.PredatorPowerUpTeleport;
import logic.powerup.PreyPowerUp;
import xml.PredatorPowerUpAdapter;
import xml.PreyPowerUpAdapter;

/**
 * The configuration for the powerups.
 * 
 * @author Martin Wong
 * @version 2015-10-18
 */
@XmlRootElement(name = "PowerUpConfiguration")
public class PowerUpConfig {
	
	private int numPredPow;
	private List<PredatorPowerUp> predatorPowerUps;
	private int numPreyPow;
	private List<PreyPowerUp> preyPowerUps;
	
	/**
	 * Default constructor for PowerConfig.
	 * 
	 * Sets parameters to their default values.
	 */
	public PowerUpConfig() {
		this.numPredPow = 5;
		this.numPreyPow = 0;
		this.predatorPowerUps = new ArrayList<PredatorPowerUp>();
		this.preyPowerUps = new ArrayList<PreyPowerUp>();
		
		initialisePowerUps();
	}
	
	private void initialisePowerUps() {
		int duration = 200;
		float speedUpFactor = 2.0f;
		float slowDownFactor = 2.0f;
		int magnetForce = 2000;
		int magnetRange = 3;
		
		PredatorPowerUp powerUpSpeedUp = 
				new PredatorPowerUpSpeedUp(duration, speedUpFactor);
		PredatorPowerUp powerUpSlowDown = 
				new PredatorPowerUpSlowDownPrey(duration, slowDownFactor);
		PredatorPowerUp powerUpFreeze = new PredatorPowerUpFreezePrey(duration);
		PredatorPowerUp powerUpMagnet = 
				new PredatorPowerUpMagnet(duration, magnetForce, magnetRange);
		PredatorPowerUp powerUpTeleport = new PredatorPowerUpTeleport();
		
		predatorPowerUps.add(powerUpSpeedUp);
		predatorPowerUps.add(powerUpSlowDown);
		predatorPowerUps.add(powerUpFreeze);
		predatorPowerUps.add(powerUpMagnet);
		predatorPowerUps.add(powerUpTeleport);
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
	public PowerUpConfig(int numPredPow, List<PredatorPowerUp> predatorPowerUps, 
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
		
}
