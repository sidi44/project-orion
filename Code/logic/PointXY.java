package logic;

/**
 * Represents an (x, y) co-ordinate.
 * 
 * @author Martin Wong
 * @version 2015-01-13
 */
public class PointXY {
	
	private double x;
	private double y;
	
	/**
	 * Creates an instance of PointXY, with co-ordinates (x, y).
	 * 
	 * @param x (double)
	 * @param y (double)
	 */
	public PointXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor which creates a deep copy of PointXY.
	 * 
	 * @param p (PointXY)
	 */
	public PointXY(PointXY p) {
		this(p.getX(), p.getY());
	}
	
	/**
	 * Returns the x co-ordinate.
	 * 
	 * @return x (double)
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the y co-ordinate.
	 * 
	 * @return y (double)
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Sets x to the parameter value provided.
	 * 
	 * @param x (double)
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets y to the parameter value provided.
	 * 
	 * @param y (double)
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Sets x and y to the parameter values provided.
	 * 
	 * @param x (double)
	 * @param y (double)
	 */
	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates the distance between 2 points using Pythagoras.
	 * 
	 * @param point (PointXY)
	 * @return distance (double)
	 */
	public double getDistance(PointXY point) {
		double a = point.getX() - this.x;
		double b = point.getY() - this.y;
		double result = (a * a) + (b * b);
		return Math.sqrt(result);
	}
	
	/**
	 * String representation of a PointXY co-ordinate.
	 * 
	 * @return PointXY in the form: "(x, y)" (string)
	 */
	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}

}
