package xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PolygonShape")
public class XmlPolygonShape {

	List<XmlPoint> points;

	/**
	 * @return the points
	 */
	public List<XmlPoint> getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	@XmlElement (name = "XmlPoint")
	public void setPoints(List<XmlPoint> points) {
		this.points = points;
	}
	
}
