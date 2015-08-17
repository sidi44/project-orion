package xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class representing a list of points which can be interpreted by a JAXB 
 * Xml parser.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
@XmlRootElement(name = "XmlPoints")
public class XmlPoints {

	private List<XmlPoint> points;
	
	/**
	 * Get the list of XmlPoints.
	 * 
	 * @return the list of XmlPoints
	 */
	public List<XmlPoint> getPoints() {
		return points;
	}

	/**
	 * Set the list of XmlPoints.
	 * 
	 * @param xmlPoints - the list of XmlPoints to set.
	 */
	@XmlElement (name = "XmlPoint")
	public void setPoints(List<XmlPoint> xmlPoints) {
		this.points = xmlPoints;
	}
	
}
