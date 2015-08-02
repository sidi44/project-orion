package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import logic.PreyPowerType;

@XmlRootElement (name = "PreyPowerUp")
public class XmlPreyPowerUp {

	private PreyPowerType powerUpType;
	private int timeLimit;
	
	/**
	 * @return the powerUpType
	 */
	public PreyPowerType getPowerUpType() {
		return powerUpType;
	}
	
	/**
	 * @param powerUpType the powerUpType to set
	 */
	@XmlElement (name = "PreyPowerUpType")
	public void setPowerUpType(PreyPowerType powerUpType) {
		this.powerUpType = powerUpType;
	}
	
	/**
	 * @return the timeLimit
	 */
	public int getTimeLimit() {
		return timeLimit;
	}
	
	/**
	 * @param timeLimit the timeLimit to set
	 */
	@XmlElement (name = "TimeLimit")
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
}