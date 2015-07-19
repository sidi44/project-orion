package xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PredatorPowerType;

public class PredatorPowerUpTypeAdapter 
	extends XmlAdapter<Integer, PredatorPowerType> {

	@Override
	public Integer marshal(PredatorPowerType v) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public PredatorPowerType unmarshal(Integer v) throws Exception {
		PredatorPowerType[] types = PredatorPowerType.values();
		return types[v];
	}

}
