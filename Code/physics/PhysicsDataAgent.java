package physics;

/**
 * PhysicsDataAgent class.
 * 
 * This class extends PhysicsData and allows additional data to be included in 
 * the user data for bodies which are either Predators or Prey.
 * 
 * The additional information comprises an ID number which is used to 
 * distinguish between different Predators/Prey.
 * 
 * @author Simon Dicken
 * @version 2015-05-16
 */
public class PhysicsDataAgent extends PhysicsData {

	private int id;
	
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
	}
	
	/**
	 * Get the id.
	 * 
	 * @return id - the Agent's ID value.
	 */
	public int getID() {
		return id;
	}

}
