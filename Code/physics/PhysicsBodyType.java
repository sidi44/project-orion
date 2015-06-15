package physics;

/**
 * PhysicsBodyType enum.
 * 
 * Contains each category of body that is used in the Physics world.
 * 
 * @author Simon Dicken
 * @version 2015-05-16
 */
public enum PhysicsBodyType {

	Predator(1),
	Prey(2),
	Pill(3),
	Walls(4);
	
	private final int rank;
	
	PhysicsBodyType(int rank) {
	    this.rank = rank;
	}
	
	public int getRank() {
	    return this.rank;
	}
}
