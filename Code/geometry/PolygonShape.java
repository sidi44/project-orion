package geometry;

import java.util.List;

/**
 * Represents a shape with more than two sides.
 * 
 * This class is immutable.
 * 
 * @author Martin Wong
 * @version 2015-06-01
 */
public class PolygonShape extends java.awt.Polygon {
	
	// Serial ID
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an instance of PolygonShape from a list of coordinates.
	 * 
	 * @param coordinates (List<PointXY>)
	 */
	public PolygonShape(List<PointXY> coordinates) {		
		super(setupX(coordinates), setupY(coordinates), coordinates.size());
	}
	
	/**
	 * Gets the minimum x point of the shape.
	 * 
	 * @return minX (int)
	 */
	public int getMinX() {
		return (int) (getBounds().getX());
	}
	
	/**
	 * Gets the minimum y point of the shape.
	 * 
	 * @return minY (int)
	 */
	public int getMinY() {
		return (int) (getBounds().getY());
	}
	
	/**
	 * Gets the maximum x point of the shape.
	 * 
	 * @return maxX (int)
	 */
	public int getMaxX() {
		return (int) (getBounds().getX() + getBounds().getWidth() - 1);
	}
	
	/**
	 * Gets the maximum y point of the shape.
	 * 
	 * @return maxY (int)
	 */
	public int getMaxY() {
		return (int) (getBounds().getY() + getBounds().getHeight() - 1);
	}
	
	/**
	 * Checks whether the given point is within the shape.
	 * 
	 * @param p (PointXY)
	 * @return isInside (boolean)
	 */
	public boolean contains(PointXY p) {
		return contains((int) p.getX(), (int) p.getY());
	}
	
	/**
	 * Extracts all x coordinates in a list of PointXY and put them
	 * in an integer array.
	 * 
	 * @param coordinates (List<PointXY>)
	 * @return x (int[])
	 */
	private static int[] setupX(List<PointXY> coordinates) {
		int size = coordinates.size();
		int[] x = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (int) coordinates.get(i).getX();
		}
		return x;
	}
	
	/**
	 * Extracts all y coordinates in a list of PointXY and put them
	 * in an integer array.
	 * 
	 * @param coordinates (List<PointXY>)
	 * @return y (int[])
	 */
	private static int[] setupY(List<PointXY> coordinates) {
		int size = coordinates.size();
		int[] y = new int[size];
		for (int i = 0; i < size; i++) {
			y[i] = (int) coordinates.get(i).getY();
		}
		return y;
	}
	
	
}
