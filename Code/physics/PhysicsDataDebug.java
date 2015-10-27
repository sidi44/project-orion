package physics;

public class PhysicsDataDebug extends PhysicsData {

	private int agentID;
	private int numAgents;
	
	public PhysicsDataDebug(PhysicsBodyType type) {
		super(type);
		agentID = -1;
		numAgents = -1;
	}

	public int getAgentID() {
		return agentID;
	}

	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}

	public int getNumAgents() {
		return numAgents;
	}

	public void setNumAgents(int numAgents) {
		this.numAgents = numAgents;
	}
	
	

}
