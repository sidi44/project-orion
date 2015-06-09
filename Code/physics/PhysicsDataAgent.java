package physics;

import logic.Direction;

/**
 * PhysicsDataAgent class.
 * 
 * This class extends PhysicsData and allows additional data to be included in 
 * the user data for bodies which are either Predators or Prey.
 * 
 * The additional information comprises an ID number which is used to 
 * distinguish between different Predators/Prey. The direction of the Agent's 
 * current and previous moves is also included.
 * 
 * @author Simon Dicken
 * @version 2015-06-09
 */
public class PhysicsDataAgent extends PhysicsData {

	private final int id;
	private Direction previousMove;
	private Direction currentMove;
	
	/**
	 * Constructor for PhysicsDataAgent.
	 * 
	 * @param type - the type of the body to which the data is attached.
	 * @param id - the ID value of the agent.
	 * 
	 * @throws IllegalArgumentException - if the type is not Predator or Prey.
	 */
	public PhysicsDataAgent(PhysicsBodyType type, int id) {
		super(type);
		
		if (type != PhysicsBodyType.Predator && type != PhysicsBodyType.Prey) {
			throw new IllegalArgumentException(
				"Invalid type. Should be Predator or Prey."
			);
		}
		
		this.id = id;
		this.previousMove = Direction.None;
		this.currentMove = Direction.None;
	}
	
	/**
	 * Get the id.
	 * 
	 * @return id - the Agent's ID value.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Set the previous move direction.
	 * 
	 * @param dir - the direction of the previous move.
	 */
	public void setPreviousMove(Direction dir) {
		this.previousMove = dir;
	}
	
	/**
	 * Get the previous move direction.
	 * 
	 * @return previousMove - the previous move direction.
	 */
	public Direction getPreviousMove() {
		return previousMove;
	}

	/**
	 * Set the current move direction.
	 * 
	 * @param dir - the direction of the current move.
	 */
	public void setCurrentMove(Direction dir) {
		this.currentMove = dir;
	}
	
	/**
	 * Get the current move direction.
	 * 
	 * @return currentMove - the current move direction.
	 */
	public Direction getCurrentMove() {
		return currentMove;
	}
}
