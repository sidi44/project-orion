package xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PreyPowerUpType;

public class PreyPowerUpTypeAdapter extends XmlAdapter<Integer, PreyPowerUpType> {

	@Override
	public Integer marshal(PreyPowerUpType v) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public PreyPowerUpType unmarshal(Integer v) throws Exception {
		return PreyPowerUpType.values()[v];
	}

}
