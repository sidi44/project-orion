package xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class representing a list of predator power ups which can be interpreted by 
 * a JAXB Xml parser.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
@XmlRootElement(name = "PredatorPowerUps")
public class XmlPredatorPowerUps {

	List<XmlPredatorPowerUp> xmlPowerUps;

	/**
	 * Get the list of predator power ups.
	 * 
	 * @return the list of predator power ups.
	 */
	public List<XmlPredatorPowerUp> getXmlPowerUps() {
		return xmlPowerUps;
	}

	/**
	 * Set the list of predator power ups.
	 * 
	 * @param xmlPowerUps - the list of predator power ups to set.
	 */
	@XmlElement (name = "PredatorPowerUp")
	public void setXmlPowerUps(List<XmlPredatorPowerUp> xmlPowerUps) {
		this.xmlPowerUps = xmlPowerUps;
	}
	
	
}
