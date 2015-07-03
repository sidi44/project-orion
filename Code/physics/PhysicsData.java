package physics;

/**
 * PhysicsData class.
 * 
 * This class contains the basic user data that is attached to Box2D bodies. 
 * The basic data comprises an enum which describes the body type and a flag 
 * indicating if the Body should be removed from the world before the next 
 * simulation step.
 * 
 * @author Simon Dicken
 * @version 2015-06-09
 */
public class PhysicsData {

	private final PhysicsBodyType type;
	private boolean flaggedForDelete;
	
	/**
	 * Constructor for PhysicsData.
	 * 
	 * @param type - the type of the body to which the data is attached.
	 */
	public PhysicsData(PhysicsBodyType type) {
		this.type = type;
		this.flaggedForDelete = false;
	}
	
	/**
	 * Get the type.
	 * 
	 * @return type - the type of the body to which the data is attached.
	 */
	public PhysicsBodyType getType() {
		return type;
	}
	
	/**
	 * Get flaggedForDelete.
	 * 
	 * @return flaggedForDelete - whether the body should be removed from the 
	 * world before the next simulation step.
	 */
	public boolean isFlaggedForDelete() {
		return flaggedForDelete;
	}
	
	/**
	 * Set flaggedForDelete.
	 * 
	 * @param flag - set to true to remove the body from the world before the 
	 * next simulation step, false to leave the body in the world.
	 */
	public void setFlaggedForDelete(boolean flag) {
		flaggedForDelete = flag;
	}
	
}
