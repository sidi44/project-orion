package physics;

import geometry.PointXY;

/**
 * PhysicsDataPill class.
 * 
 * This class extends PhysicsData and allows additional data to be included in 
 * the user data for bodies which are of type Pill.
 * 
 * The additional information comprises a PointXY position which defines to 
 * which MazeNode the pill belongs.
 * 
 * @author Simon Dicken
 * @version 2015-05-16
 */
public class PhysicsDataPill extends PhysicsData {

	private PointXY position;
	
	/**
	 * Constructor for PhysicsDataPill.
	 * 
	 * @param type - the type of the body to which the data is attached.
	 * @param position - the position of the MazeNode to which this pill 
	 * belongs.
	 * 
	 * @throws IllegalArgumentException - if the type is not Pill.
	 */
	public PhysicsDataPill(PhysicsBodyType type, PointXY position) {
		super(type);
		
		if (type != PhysicsBodyType.Pill) {
			throw new IllegalArgumentException("Invalid type. Should be Pill.");
		}
		
		this.position = position;
	}
	
	/**
	 * Get position.
	 * 
	 * @return position - the position of the MazeNode to which this pill 
	 * belongs.
	 */
	public PointXY getPosition() {
		return position;
	}

}
