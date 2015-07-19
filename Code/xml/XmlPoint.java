package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XmlPoint")
public class XmlPoint {

	private double x;
	private double y;
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @param x the x to set
	 */
	@XmlElement (name = "X")
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * @param y the y to set
	 */
	@XmlElement (name = "Y")
	public void setY(double y) {
		this.y = y;
	}
	
}
