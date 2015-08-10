package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import logic.PredatorPowerType;

/**
 * A class representing a predator power up which can be interpreted by a JAXB 
 * Xml parser.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
@XmlRootElement (name = "PredatorPowerUp")
public class XmlPredatorPowerUp {

	private PredatorPowerType powerUpType;
	private int timeLimit;
	
	/**
	 * Get the power up type.
	 * 
	 * @return the type of this power up.
	 */
	public PredatorPowerType getPowerUpType() {
		return powerUpType;
	}
	
	/**
	 * Set the power up type.
	 * 
	 * @param powerUpType - the type of this power up.
	 */
	@XmlElement (name = "PredatorPowerUpType")
	public void setPowerUpType(PredatorPowerType powerUpType) {
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
	
}
