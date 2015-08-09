package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class representing a point which can be interpreted by a JAXB Xml parser.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
@XmlRootElement(name = "XmlPoint")
public class XmlPoint {

	private double x;
	private double y;
	
	/**
	 * Get the X coordinate.
	 * 
	 * @return the X coordinate.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Set the X coordinate.
	 * 
	 * @param x - the value to set the X coordinate to.
	 */
	@XmlElement (name = "X")
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Get the Y coordinate.
	 * 
	 * @return the Y coordinate.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Set the Y coordinate.
	 * 
	 * @param y - the value to set the Y coordinate to.
	 */
	@XmlElement (name = "Y")
	public void setY(double y) {
		this.y = y;
	}
	
}
