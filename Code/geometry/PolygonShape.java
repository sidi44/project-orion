package geometry;

import java.util.List;

import com.badlogic.gdx.math.Polygon;

import utils.NumberUtils;

/**
 * Represents a shape with more than two sides.
 * 
 * This class is immutable.
 * 
 * @author Martin Wong
 * @version 2016-03-25
 */
public class PolygonShape extends Polygon {
	
	private final List<PointXY> coordinates;
	
	/**
	 * Creates an instance of PolygonShape from a list of coordinates
	 * (draw in the order of the coordinates).
	 * 
	 * @param coordinates (List<PointXY>)
	 */
	public PolygonShape(List<PointXY> coordinates) {
		super(setUpXY(coordinates));
		this.coordinates = coordinates;
	}
	
	/**
	 * Return List<PointXY> as float[].
	 *  
	 * @param coordinates (List<PointXY>)
	 * @return vertices (float[])
	 */
	private static float[] setUpXY(List<PointXY> coordinates) {
		if (coordinates == null) return new float[0];
		
		float[] vertices = new float[coordinates.size() * 2];
		
		int i = 0;
		for (PointXY p : coordinates) {
			vertices[i++] = p.getX();
			vertices[i++] = p.getY();
		}
		
		return vertices;
	}
	
	/**
	 * Gets the points which form the polygon.
	 * 
	 * @return allPoints (List<PointXY)
	 */
	public List<PointXY> getCoordinates() {
		return coordinates;
	}
	
	/**
	 * Gets the minimum x point of the shape.
	 * 
	 * @return minX (int)
	 */
	public int getMinX() {
		return (int) getBoundingRectangle().getX();
	}
	
	/**
	 * Gets the minimum y point of the shape.
	 * 
	 * @return minY (int)
	 */
	public int getMinY() {
		return (int) getBoundingRectangle().getY();
	}
	
	/**
	 * Gets the maximum x point of the shape.
	 * 
	 * @return maxX (int)
	 */
	public int getMaxX() {
		return getMinX() + (int) getBoundingRectangle().getWidth();
	}
	
	/**
	 * Gets the maximum y point of the shape.
	 * 
	 * @return maxY (int)
	 */
	public int getMaxY() {
		return getMinX() + (int) getBoundingRectangle().getHeight();
	}
	
	/**
	 * Checks whether point is within the bounding box.
	 * 
	 * @param p (PointXY)
	 * @return inBoundingBox (boolean)
	 */
	public boolean withinBoundingBox(PointXY p) {
		return (p.getX() >= getMinX() && p.getX() <= getMaxX() 
				&& p.getY() >= getMinY() && p.getY() <= getMaxY());
	}
	
	
	/**
	 * Checks whether the given point is within the shape (outline inclusive).
	 * 
	 * @param p (PointXY)
	 * @return isInside (boolean)
	 */
	public boolean containsInclusive(PointXY p) {
		boolean isInside = containsExclusive(p);

		if (!isInside) {
			isInside = onOutline(p);
		}
		
		return isInside;
	}
	
	/**
	 * Checks whether the given point is within the shape (outline exclusive).
	 * 
	 * @param p (PointXY)
	 * @return isInside (boolean)
	 */
	public boolean containsExclusive(PointXY p) {
		if (!withinBoundingBox(p)) {
			return false;
		}
		
		return contains((float) p.getX(), (float) p.getY());
	}
	
	/**
	 * Checks whether the given point is on the outline of the shape.
	 * 
	 * @param p (PointXY)
	 * @return isOutline (boolean)
	 */
	public boolean onOutline(PointXY p) {
		
		if (!withinBoundingBox(p)) {
			return false;
		}
		
		PointXY p1 = coordinates.get(coordinates.size() - 1);
		double d1;
		double d2;
		double d3;
		
		for (PointXY p2 : coordinates) {
			// Check within mini-bounding box
			if (NumberUtils.inRange(p.getX(), p1.getX(), p2.getX()) 
				&& NumberUtils.inRange(p.getY(), p1.getY(), p2.getY())) {
			
				d1 = p2.getDistance(p1);
				d2 = p.getDistance(p1);
				d3 = p2.getDistance(p);
				
				if (NumberUtils.compareDouble(d1, d2 + d3, 0.001)) {
					return true;
				}
			}
			p1 = p2;
		}
		
		return false;
	}
	
}
