package xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PredatorPowerUp;

/**
 * A JAXB adaptor used to convert an XML version of a collection of predator
 * power up into a list of PredatorPowerUp.  The XmlPredatorPowerUps can be 
 * understood by the XML parser.
 * 
 * This is currently only intended to be used for unmarshalling.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
public class PredatorPowerUpAdapter 
				extends XmlAdapter<XmlPredatorPowerUps, List<PredatorPowerUp>>{

	@Override
	public XmlPredatorPowerUps marshal(
			List<PredatorPowerUp> v) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public List<PredatorPowerUp> unmarshal(
			XmlPredatorPowerUps v) throws Exception {
		return convertXml(v.getXmlPowerUps());
	}

	/**
	 * Convert the provided list of XmlPredatorPowerUp into a list of 
	 * PredatorPowerUp.
	 * 
	 * @param xmlPowerUps - the list of power ups to convert.
	 * @return the list of provided power ups in PredatorPowerUp format.
	 */
	private List<PredatorPowerUp> convertXml(
			List<XmlPredatorPowerUp> xmlPowerUps) {
		
		List<PredatorPowerUp> powerUps = new ArrayList<PredatorPowerUp>();
		
		for (XmlPredatorPowerUp xmlPowerUp : xmlPowerUps) {
			PredatorPowerUp powerUp = 
					new PredatorPowerUp(xmlPowerUp.getPowerUpType(), 
							xmlPowerUp.getTimeLimit());
			powerUps.add(powerUp);
		}
		
		return powerUps;
	}
	
}
