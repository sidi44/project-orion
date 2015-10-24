package xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.powerup.PreyPowerUp;
import logic.powerup.PreyPowerUpFreezePredator;
import logic.powerup.PreyPowerUpMagnet;
import logic.powerup.PreyPowerUpSlowDownPredator;
import logic.powerup.PreyPowerUpSpeedUp;
import logic.powerup.PreyPowerUpTeleport;
import logic.powerup.PreyPowerUpType;

/**
 * A JAXB adaptor used to convert an XML version of a collection of prey power
 * ups into a list of PreyPowerUp.  The XmlPreyPowerUps can be understood by 
 * the XML parser.
 * 
 * This is currently only intended to be used for unmarshalling.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
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
			PreyPowerUp powerUp = convertPowerUp(xmlPowerUp);
			powerUps.add(powerUp);
		}
		
		return powerUps;
	}

	/**
	 * Converts an xmlPreyPowerUp into a PreyPowerUp of the correct type.
	 * 
	 * @param xmlPowerUp - the xml power up to convert
	 * @return a prey power up created from the provided xml power up.
	 */
	private PreyPowerUp convertPowerUp(XmlPreyPowerUp xmlPowerUp) {
		
		int timeLimit = xmlPowerUp.getTimeLimit();
		double speedUpFactor = xmlPowerUp.getSpeedUpFactor();
		double slowDownFactor = xmlPowerUp.getSlowDownFactor();
		int force = xmlPowerUp.getMagnetForce();
		int range = xmlPowerUp.getMagnetRange();
		
		PreyPowerUp powerUp = null;
		
		PreyPowerUpType type = xmlPowerUp.getPowerUpType();
		switch (type) {
			case Freeze:
				powerUp = new PreyPowerUpFreezePredator(timeLimit);
				break;
			case Magnet:
				powerUp = new PreyPowerUpMagnet(timeLimit, force, range);
				break;
			case SlowDownPredator:
				powerUp = 
				new PreyPowerUpSlowDownPredator(timeLimit, slowDownFactor);
				break;
			case SpeedUpPrey:
				powerUp = new PreyPowerUpSpeedUp(timeLimit, speedUpFactor);
				break;
			case Teleport:
				powerUp = new PreyPowerUpTeleport();
				break;
			default:
				break;
		}
		
		return powerUp;
	}
	
}
