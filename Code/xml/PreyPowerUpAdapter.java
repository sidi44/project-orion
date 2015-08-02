package xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PreyPowerUp;

public class PreyPowerUpAdapter extends XmlAdapter<XmlPreyPowerUps, List<PreyPowerUp>> {

	@Override
	public XmlPreyPowerUps marshal(List<PreyPowerUp> v) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public List<PreyPowerUp> unmarshal(XmlPreyPowerUps v) throws Exception {
		return convertXml(v.getXmlPowerUps());
	}
	
	private List<PreyPowerUp> convertXml(List<XmlPreyPowerUp> xmlPowerUps) {
		
		List<PreyPowerUp> powerUps = new ArrayList<PreyPowerUp>();
		
		for (XmlPreyPowerUp xmlPowerUp : xmlPowerUps) {
			PreyPowerUp powerUp = 
					new PreyPowerUp(xmlPowerUp.getPowerUpType(), 
							xmlPowerUp.getTimeLimit());
			powerUps.add(powerUp);
		}
		
		return powerUps;
	}

}
