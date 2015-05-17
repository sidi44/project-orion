package geometry;

/**
 * Represents an (x, y) co-ordinate.
 * 
 * This class is immutable.
 * 
 * @author Martin Wong
 * @version 2015-05-16
 */
final public class PointXY {
	
	final private double x;
	final private double y;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof PointXY) {
			PointXY other = (PointXY) obj;
			
			boolean xEqual = (Double.doubleToLongBits(x) == 
							  Double.doubleToLongBits(other.x));
			boolean yEqual = (Double.doubleToLongBits(y) == 
							  Double.doubleToLongBits(other.y));
			
			return xEqual && yEqual;
			
		} else {
			return false;
		}
	}
	
}
