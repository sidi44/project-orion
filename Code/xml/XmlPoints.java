package xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XmlPoints")
public class XmlPoints {

	private List<XmlPoint> points;
	
	/**
	 * @return the xmlPoints
	 */
	public List<XmlPoint> getPoints() {
		return points;
	}

	/**
	 * @param xmlPoints the xmlPoints to set
	 */
	@XmlElement (name = "XmlPoint")
	public void setPoints(List<XmlPoint> xmlPoints) {
		this.points = xmlPoints;
	}
	
}
