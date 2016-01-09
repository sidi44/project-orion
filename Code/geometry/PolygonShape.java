package geometry;

//import java.awt.geom.Line2D;
//import java.awt.geom.PathIterator;
//import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shape with more than two sides.
 * 
 * This class is immutable.
 * 
 * @author Martin Wong
 * @version 2015-06-19
 */
public class PolygonShape {
	
	// Serial ID
//	private static final long serialVersionUID = 1L;
	//private final double offset = 0.0000000001;
	
	private List<PointXY> coordinates;
	
	/**
	 * Creates an instance of PolygonShape from a list of coordinates
	 * (draw in the order of the coordinates).
	 * 
	 * @param coordinates (List<PointXY>)
	 */
	public PolygonShape(List<PointXY> coordinates) {		
		this.coordinates = coordinates;
	}
	
	/**
	 * Gets the minimum x point of the shape.
	 * 
	 * @return minX (int)
	 */
	public int getMinX() {
		if (coordinates.size() == 0) {
			System.err.println("Polygon has no points.");
			return 0;
		}
		
		int minX = Integer.MAX_VALUE;
		for (PointXY point : coordinates) {
			if (point.getX() < minX) {
				minX = point.getX();
			}
		}
		
		return minX;
	}
	
	/**
	 * Gets the minimum y point of the shape.
	 * 
	 * @return minY (int)
	 */
	public int getMinY() {
		if (coordinates.size() == 0) {
			System.err.println("Polygon has no points.");
			return 0;
		}
		
		int minY = Integer.MAX_VALUE;
		for (PointXY point : coordinates) {
			if (point.getY() < minY) {
				minY = point.getY();
			}
		}
		
		return minY;
	}
	
	/**
	 * Gets the maximum x point of the shape.
	 * 
	 * @return maxX (int)
	 */
	public int getMaxX() {
		if (coordinates.size() == 0) {
			System.err.println("Polygon has no points.");
			return 0;
		}
		
		int maxX = Integer.MIN_VALUE;
		for (PointXY point : coordinates) {
			if (point.getX() > maxX) {
				maxX = point.getX();
			}
		}
		
		return maxX;
	}
	
	/**
	 * Gets the maximum y point of the shape.
	 * 
	 * @return maxY (int)
	 */
	public int getMaxY() {
		if (coordinates.size() == 0) {
			System.err.println("Polygon has no points.");
			return 0;
		}
		
		int maxY = Integer.MIN_VALUE;
		for (PointXY point : coordinates) {
			if (point.getY() > maxY) {
				maxY = point.getY();
			}
		}
		
		return maxY;
	}
	
//	// More efficient but has error margin of: offset * 2.
//	/**
//	 * Checks whether the given point is within the shape.
//	 * 
//	 * @param p (PointXY)
//	 * @return isInside (boolean)
//	 */
//	public boolean contains(PointXY p) {
//		boolean isInside = contains((int) p.getX(), (int) p.getY());
//		
//		if (!isInside) {
//			/*
//			 * Contains method is outline exclusive, to make this inclusive:
//			 * form a small square with size: offset * 2 and check if
//			 * intersect with outline, if does then assume 
//			 * (error margin = offset * 2) p is part of boundary.
//			 */
//			isInside = intersects(p.getX() - offset, p.getY() - offset,
//					offset * 2, offset * 2);
//		}
//		
//		return isInside;
//	}
	
	// Has no error margin but less efficient.
	/**
	 * Checks whether the given point is within the shape (outline inclusive).
	 * 
	 * @param p (PointXY)
	 * @return isInside (boolean)
	 */
	public boolean contains(PointXY p) {
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
		return pointInPolygon(p);
//		return contains((int) p.getX(), (int) p.getY());
	}
	
	private boolean pointInPolygon(PointXY point) {
		  int nvert = coordinates.size();
		  boolean result = false;

		  int j = nvert - 1;
		  for(int i = 0; i < nvert; i++) {
		    if ( ( (coordinates.get(i).getY() >= point.getY() ) != (coordinates.get(j).getY() >= point.getY()) ) &&
		        (point.getX() <= (coordinates.get(j).getX() - coordinates.get(i).getX()) * (point.getY() - coordinates.get(i).getY()) / (coordinates.get(j).getY() - coordinates.get(i).getY()) + coordinates.get(i).getX())
		      ) {
		      result = !result;
		    }
		    j = i;
		  }

		  return result;
	}
	
	/**
	 * Checks whether the given point is on the outline of the shape.
	 * 
	 * @param p (PointXY)
	 * @return isOutline (boolean)
	 */
	public boolean onOutline(PointXY p) {
		return false;
//		boolean isOutline = false;
//		List<PointXY> allPoints = getAllPoints();
//		
//		
//		if (allPoints.size() > 0) {
//			int j = 0;
//			PointXY point1;
//			PointXY point2;
//			Line2D.Double line;
//			
//			for (int i = 0; i < allPoints.size(); i++) {
//				j = (i == allPoints.size() - 1) ? 0 : i + 1;
//				point1 = allPoints.get(i);
//				point2 = allPoints.get(j);
//				
//				line = new Line2D.Double(point1.getX(), point1.getY(), 
//						point2.getX(), point2.getY());
//				
//				if (line.intersectsLine(p.getX(), p.getY(), p.getX(), p.getY())) {
//					isOutline = true;
//					break;
//				}
//			}
//		}
//		return isOutline;
	}
	
	/**
	 * Gets the points which form the polygon.
	 * 
	 * @return allPoints (List<PointXY)
	 */
	public List<PointXY> getAllPoints() {
		return coordinates;
//		PathIterator pathIt = getPathIterator(null);
//		List<PointXY> allPoints = new ArrayList<PointXY>();
//		double[] dArray = new double[6]; // Stores the segment details
//		PointXY previousPoint = null;
//		PointXY currentPoint = null;
//		
//		while (!pathIt.isDone()) {
//			pathIt.currentSegment(dArray); // Gets part of polygon
//			currentPoint = new PointXY((int)dArray[0], (int)dArray[1]);
//			
//			// Do not include successive repeated points
//			if (!currentPoint.equals(previousPoint)) {
//				allPoints.add(currentPoint);
//			}
//			previousPoint = currentPoint;
//			pathIt.next(); // Move on to next segment
//		}
//		return allPoints;
	}
	
//	/**
//	 * Extracts all x coordinates in a list of PointXY and put them
//	 * in an integer array.
//	 * 
//	 * @param coordinates (List<PointXY>)
//	 * @return x (int[])
//	 */
//	private static int[] setupX(List<PointXY> coordinates) {
//		int size = coordinates.size();
//		int[] x = new int[size];
//		for (int i = 0; i < size; i++) {
//			x[i] = (int) coordinates.get(i).getX();
//		}
//		return x;
//	}
//	
//	/**
//	 * Extracts all y coordinates in a list of PointXY and put them
//	 * in an integer array.
//	 * 
//	 * @param coordinates (List<PointXY>)
//	 * @return y (int[])
//	 */
//	private static int[] setupY(List<PointXY> coordinates) {
//		int size = coordinates.size();
//		int[] y = new int[size];
//		for (int i = 0; i < size; i++) {
//			y[i] = (int) coordinates.get(i).getY();
//		}
//		return y;
//	}
	
	
}
