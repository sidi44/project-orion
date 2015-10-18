package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import logic.powerup.PredatorPowerUpType;

/**
 * A class representing a predator power up which can be interpreted by a JAXB 
 * Xml parser.
 * 
 * This class has fields for all additional information that accompanies 
 * different types of predator power up. The fields which match a given type 
 * (and which are included in the XML file) will be set when the XML file is 
 * unmarshalled, others will be default initialised.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
@XmlRootElement (name = "PredatorPowerUp")
public class XmlPredatorPowerUp {

	private PredatorPowerUpType powerUpType;
	private int timeLimit;
	private double speedUpFactor;
	private double slowDownFactor;
	private int magnetForce;
	private int magnetRange;
	
	/**
	 * Get the power up type.
	 * 
	 * @return the type of this power up.
	 */
	public PredatorPowerUpType getPowerUpType() {
		return powerUpType;
	}
	
	/**
	 * Set the power up type.
	 * 
	 * @param powerUpType - the type of this power up.
	 */
	@XmlElement (name = "PredatorPowerUpType")
	public void setPowerUpType(PredatorPowerUpType powerUpType) {
		this.powerUpType = powerUpType;
	}
	
	/**
	 * Get the time limit of this power up in seconds.
	 * 
	 * @return The timeLimit of this power up in seconds.
	 */
	public int getTimeLimit() {
		return timeLimit;
	}
	
	/**
	 * Set the time limit of this power up in seconds.
	 * 
	 * @param timeLimit - the time limit of this power up in seconds.
	 */
	@XmlElement (name = "TimeLimit")
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * Get the speed up factor for this power up.
	 * 
	 * @return The speed up factor for this power up.
	 */
	public double getSpeedUpFactor() {
		return speedUpFactor;
	}

	/**
	 * Set the speed up factor for this power up.
	 * 
	 * @param speedUpFactor - the speed up factor for this power up.
	 */
	@XmlElement (name = "SpeedUpFactor")
	public void setSpeedUpFactor(double speedUpFactor) {
		this.speedUpFactor = speedUpFactor;
	}

	/**
	 * Get the slow down factor for this power up.
	 * 
	 * @return The slow down factor for this power up.
	 */
	public double getSlowDownFactor() {
		return slowDownFactor;
	}
	
	/**
	 * Set the slow down factor for this power up.
	 * 
	 * @param slowDownFactor - the slow down factor for this power up.
	 */
	@XmlElement (name = "SlowDownFactor")
	public void setSlowDownFactor(double slowDownFactor) {
		this.slowDownFactor = slowDownFactor;
	}

	/**
	 * Get the magnet force.
	 * 
	 * @return the magnet force.
	 */
	public int getMagnetForce() {
		return magnetForce;
	}

	/**
	 * Set the magnet force.
	 * 
	 * @param magnetForce - the force to be used for the magnet power up.
	 */
	@XmlElement (name = "MagnetForce")
	public void setMagnetForce(int magnetForce) {
		this.magnetForce = magnetForce;
	}

	/**
	 * Get the magnet range (measures in maze squares).
	 * 
	 * @return the magnet range.
	 */
	public int getMagnetRange() {
		return magnetRange;
	}

	/**
	 * Set the magnet range.
	 * 
	 * @param magnetRange - the distance measured in maze squares over which 
	 * the magnet power up works.
	 */
	@XmlElement (name = "MagnetRange")
	public void setMagnetRange(int magnetRange) {
		this.magnetRange = magnetRange;
	}
	
}
