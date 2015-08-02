package xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PreyPowerUps")
public class XmlPreyPowerUps {

	List<XmlPreyPowerUp> xmlPowerUps;

	/**
	 * @return the xmlPowerUps
	 */
	public List<XmlPreyPowerUp> getXmlPowerUps() {
		return xmlPowerUps;
	}

	/**
	 * @param xmlPowerUps the xmlPowerUps to set
	 */
	@XmlElement (name = "PreyPowerUp")
	public void setXmlPowerUps(List<XmlPreyPowerUp> xmlPowerUps) {
		this.xmlPowerUps = xmlPowerUps;
	}
	
}