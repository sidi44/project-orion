package logic.powerup;

import logic.Agent;

/**
 * A power up for which will attract target agents towards the agent-owner of 
 * this power up. 
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public class PowerUpMagnet extends PowerUpWithStrength {

	/**
	 * Constructor for PredatorPowerUpMagnet.
	 * 
	 * @param target - the agents which this power up targets.
	 * @param strength - the strength of this power up.
	 */
	public PowerUpMagnet(PowerUpTarget target, int strength) {
		super(target, strength, PowerUpType.Magnet);
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void apply(Agent agent) {

		// Set a 'magnet' on an agent, this contains info on which agent to 
		// pull towards and how strong the pull is (as an index).
		Agent owner = getOwner();
		Magnet magnet = new Magnet(owner, getStrength());
		agent.setMagnet(magnet);
	}
	
	@Override
	protected void unapply(Agent agent) {
		agent.setMagnet(null);
	}

	@Override
	public String getName() {
		return "Magnet";
	}

}
