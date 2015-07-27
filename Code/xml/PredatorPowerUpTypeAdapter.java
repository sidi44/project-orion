package xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PredatorPowerUpType;

public class PredatorPowerUpTypeAdapter 
	extends XmlAdapter<Integer, PredatorPowerUpType> {

	@Override
	public Integer marshal(PredatorPowerUpType v) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public PredatorPowerUpType unmarshal(Integer v) throws Exception {
		PredatorPowerUpType[] types = PredatorPowerUpType.values();
		return types[v];
	}

}
