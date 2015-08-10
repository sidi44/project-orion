package xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PreyPowerUp;

/**
 * A JAXB adaptor used to convert an XML version of a collection of prey power
 * ups into a list of PreyPowerUp.  The XmlPreyPowerUps can be understood by 
 * the XML parser.
 * 
 * This is currently only intended to be used for unmarshalling.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
public class PreyPowerUpAdapter 
				extends XmlAdapter<XmlPreyPowerUps, List<PreyPowerUp>> {

	@Override
	public XmlPreyPowerUps marshal(List<PreyPowerUp> v) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public List<PreyPowerUp> unmarshal(XmlPreyPowerUps v) throws Exception {
		return convertXml(v.getXmlPowerUps());
	}
	
	/**
	 * Convert the provided list of XmlPreyPowerUp into a list of PreyPowerUp.
	 * 
	 * @param xmlPowerUps - the list of power ups to convert.
	 * @return the list of provided power ups in PreyPowerUp format.
	 */
	private List<PreyPowerUp> convertXml(List<XmlPreyPowerUp> xmlPowerUps) {
		
		List<PreyPowerUp> powerUps = new ArrayList<PreyPowerUp>();
		
		for (XmlPreyPowerUp xmlPowerUp : xmlPowerUps) {
			PreyPowerUp powerUp = 
					new PreyPowerUp(xmlPowerUp.getTimeLimit(),
							xmlPowerUp.getPowerUpType());
			powerUps.add(powerUp);
		}
		
		return powerUps;
	}

}
