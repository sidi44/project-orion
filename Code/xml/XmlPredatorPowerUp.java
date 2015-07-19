package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import logic.PredatorPowerType;

@XmlRootElement (name = "PredatorPowerUp")
public class XmlPredatorPowerUp {

	private PredatorPowerType powerUpType;
	private int timeLimit;
	
	/**
	 * @return the powerUpType
	 */
	public PredatorPowerType getPowerUpType() {
		return powerUpType;
	}
	
	/**
	 * @param powerUpType the powerUpType to set
	 */
	@XmlElement (name = "PredatorPowerUpType")
	@XmlJavaTypeAdapter(PredatorPowerUpTypeAdapter.class)
	public void setPowerUpType(PredatorPowerType powerUpType) {
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
