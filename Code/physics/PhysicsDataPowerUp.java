package physics;

import geometry.PointXY;

/**
 * PhysicsDataPowerUp class.
 * 
 * This class extends PhysicsData and allows additional data to be included in 
 * the user data for bodies which are of type PowerUp.
 * 
 * The additional information comprises a PointXY position which defines in 
 * which MazeNode the power up is positioned, along with the ID of the agent 
 * which has activated the power up (only set after contact). There is also a 
 * String representation of the power up name.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public class PhysicsDataPowerUp extends PhysicsData {

	private PointXY position;
	private int agentID;
	private String powerUpName;
	
	/**
	 * Constructor for PhysicsDataPowerUp.
	 * 
	 * @param type - the type of the body to which the data is attached.
	 * @param position - the position of the MazeNode in which this power up 
	 * is placed.
	 * 
	 * @throws IllegalArgumentException - if the type is not PowerUp.
	 */
	public PhysicsDataPowerUp(PhysicsBodyType type, PointXY position, 
			String powerUpName) {
		super(type);
		
		if (type != PhysicsBodyType.PowerUpPredator && 
			type != PhysicsBodyType.PowerUpPrey) {
			throw new IllegalArgumentException(
				"Invalid type. Should be PowerUpPredator or PowerUpPrey."
			);
		}

		this.position = position;
		this.agentID = -1;
		this.powerUpName = powerUpName;
	}

	/**
	 * Get position.
	 * 
	 * @return position - the position of the MazeNode in which this power up 
	 * is placed.
	 */
	public PointXY getPosition() {
		return position;
	}
	
	/**
	 * Get agentID.
	 * 
	 * Will return -1 if the power up has not yet been activated.
	 * 
	 * @return agentID - the ID of the agent that has activated this power up.
	 */
	public int getAgentID() {
		return agentID;
	}
	
	/**
	 * Set agentID.
	 * 
	 * @param agentID - the ID of the agent that has activated this power up.
	 */
	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}

	/**
	 * Get the name of the power up with which this data is associated.
	 * 
	 * @return the name of the power up with which this data is associated.
	 */
	public String getPowerUpName() {
		return powerUpName;
	}
}