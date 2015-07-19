package xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PredatorPowerUp;

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
