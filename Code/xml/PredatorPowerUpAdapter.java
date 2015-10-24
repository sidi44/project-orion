package xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.powerup.PredatorPowerUp;
import logic.powerup.PredatorPowerUpFreezePrey;
import logic.powerup.PredatorPowerUpMagnet;
import logic.powerup.PredatorPowerUpSlowDownPrey;
import logic.powerup.PredatorPowerUpSpeedUp;
import logic.powerup.PredatorPowerUpTeleport;
import logic.powerup.PredatorPowerUpType;

/**
 * A JAXB adaptor used to convert an XML version of a collection of predator
 * power up into a list of PredatorPowerUp.  The XmlPredatorPowerUps can be 
 * understood by the XML parser.
 * 
 * This is currently only intended to be used for unmarshalling.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
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
			
			PredatorPowerUp powerUp = convertPowerUp(xmlPowerUp);
			powerUps.add(powerUp);
		}
		
		return powerUps;
	}
	
	/**
	 * Converts an xmlPredatorPowerUp into a PredatorPowerUp of the correct 
	 * type.
	 * 
	 * @param xmlPowerUp - the xml power up to convert
	 * @return a predator power up created from the provided xml power up.
	 */
	private PredatorPowerUp convertPowerUp(XmlPredatorPowerUp xmlPowerUp) {
		
		int timeLimit = xmlPowerUp.getTimeLimit();
		double speedUpFactor = xmlPowerUp.getSpeedUpFactor();
		double slowDownFactor = xmlPowerUp.getSlowDownFactor();
		int force = xmlPowerUp.getMagnetForce();
		int range = xmlPowerUp.getMagnetRange();
		
		PredatorPowerUp powerUp = null;
		
		PredatorPowerUpType type = xmlPowerUp.getPowerUpType();
		switch (type) {
			case Freeze:
				powerUp = new PredatorPowerUpFreezePrey(timeLimit);
				break;
			case Magnet:
				powerUp = new PredatorPowerUpMagnet(timeLimit, force, range);
				break;
			case SlowDownPrey:
				powerUp = 
					new PredatorPowerUpSlowDownPrey(timeLimit, slowDownFactor);
				break;
			case SpeedUpPredator:
				powerUp = new PredatorPowerUpSpeedUp(timeLimit, speedUpFactor);
				break;
			case Teleport:
				powerUp = new PredatorPowerUpTeleport();
				break;
			default:
				break;
		}
		
		return powerUp;
	}
}
