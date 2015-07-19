package xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PredatorPowerUps")
public class XmlPredatorPowerUps {

	List<XmlPredatorPowerUp> xmlPowerUps;

	/**
	 * @return the xmlPowerUps
	 */
	public List<XmlPredatorPowerUp> getXmlPowerUps() {
		return xmlPowerUps;
	}

	/**
	 * @param xmlPowerUps the xmlPowerUps to set
	 */
	@XmlElement (name = "PredatorPowerUp")
	public void setXmlPowerUps(List<XmlPredatorPowerUp> xmlPowerUps) {
		this.xmlPowerUps = xmlPowerUps;
	}
	
	
}
