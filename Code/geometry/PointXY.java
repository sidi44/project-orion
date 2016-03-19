package geometry;

/**
 * Represents an (x, y) co-ordinate, where x and y are integers.
 * 
 * This class is immutable.
 * 
 * @author Martin Wong
 * @version 2015-08-09
 */
final public class PointXY {
	
	private final int x;
	private final int y;
	
	/**
	 * Creates an instance of PointXY, with co-ordinates (x, y).
	 * 
	 * @param x (int)
	 * @param y (int)
	 */
	public PointXY(int x, int y) {
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
	 * Default constructor.
	 * 
	 * (This is only intended to be used when deserializing .json files.)
	 */
	@SuppressWarnings("unused")
	private PointXY() {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Returns the x co-ordinate.
	 * 
	 * @return x (int)
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Returns the y co-ordinate.
	 * 
	 * @return y (int)
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Adds the coordinates of the points together.
	 * 
	 * @param p (PointXY)
	 * @return newPoint (PointXY)
	 */
	public PointXY addPoint(PointXY p) {
		int newX = x + p.getX();
		int newY = y + p.getY();
		return new PointXY (newX, newY);
	}
	
	/**
	 * Calculates the distance between 2 points using Pythagoras.
	 * 
	 * @param point (PointXY)
	 * @return distance (double)
	 */
	public double getDistance(PointXY point) {
		return Math.sqrt(getSumSquareDifference(point));
	}
	
	/**
	 * Calculates the sum of squared differences.
	 * 
	 * @param point (PointXY)
	 * @return sum of square difference (long)
	 */
	public long getSumSquareDifference(PointXY point) {
		long a = point.getX() - this.x;
		long b = point.getY() - this.y;
		return (a * a) + (b * b);
	}
	
	/**
	 * Generates hash code for object.
	 * If objects are "equal" then will have same hash code.
	 * 
	 * @return result (int)
	 */
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
	
	/**
	 * Checks whether two objects are equal.
	 * 
	 * @param o (Object)
	 * @return isEqual (boolean)
	 */
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
