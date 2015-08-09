package xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class representing a list of prey power ups which can be interpreted by 
 * a JAXB Xml parser.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
@XmlRootElement(name = "PreyPowerUps")
public class XmlPreyPowerUps {

	List<XmlPreyPowerUp> xmlPowerUps;

	/**
	 * Get the list of prey power ups.
	 * 
	 * @return the list of prey power ups.
	 */
	public List<XmlPreyPowerUp> getXmlPowerUps() {
		return xmlPowerUps;
	}

	/**
	 * Set the list of prey power ups.
	 * 
	 * @param xmlPowerUps - the list of prey power ups to set.
	 */
	@XmlElement (name = "PreyPowerUp")
	public void setXmlPowerUps(List<XmlPreyPowerUp> xmlPowerUps) {
		this.xmlPowerUps = xmlPowerUps;
	}
	
}