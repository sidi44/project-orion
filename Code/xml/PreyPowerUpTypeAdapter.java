package xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import logic.PreyPowerType;

public class PreyPowerUpTypeAdapter extends XmlAdapter<Integer, PreyPowerType> {

	@Override
	public Integer marshal(PreyPowerType v) throws Exception {
		throw new UnsupportedOperationException("Marshalling not implemented.");
	}

	@Override
	public PreyPowerType unmarshal(Integer v) throws Exception {
		return PreyPowerType.values()[v];
	}

}
