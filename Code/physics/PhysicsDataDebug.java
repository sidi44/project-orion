package physics;

/**
 * PhysicsDataDebug class.
 * 
 * This class extends PhysicsData and allows additional data to be included in 
 * the user data for bodies which are used for debugging purposes.
 * 
 * Currently, the debug data simply comprises an Agent ID. This is used to tag
 * certain squares as 'belonging' to a particular agent (this is specific to the
 * PartitionAI). Debug physics objects are specifically created for each maze 
 * square and this data is attached to them.
 * 
 * @author Simon Dicken
 * @version 2015-12-28
 */
public class PhysicsDataDebug extends PhysicsData {

	private int agentID;
	
	/**
	 * Constructor for PhysicsDataDebug.
	 * 
	 * @param type - the type of the body to which the data is attached.
	 */
	public PhysicsDataDebug(PhysicsBodyType type) {
		super(type);
		agentID = -1;
	}

	/**
	 *  Get the agent ID. 
	 *  
	 * @return the agent ID stored in this data object.
	 */
	public int getAgentID() {
		return agentID;
	}

	/**
	 * Set the agent ID.
	 * 
	 * @param agentID - the agent ID to store in this data object.
	 */
	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}

}
