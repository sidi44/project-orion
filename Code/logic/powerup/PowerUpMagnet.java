package logic.powerup;

import java.util.List;

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
	 * @param timeLimit - the duration of this power up.
	 * @param force - the magnet's force in Newtons.
	 * @param range - the range of the magnet in maze squares.
	 */
	public PowerUpMagnet(PowerUpTarget target, int strength) {
		super(target, strength, PowerUpType.Magnet);
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void apply(List<Agent> allAgents) {
		//throw new IllegalStateException("Not implemented yet...");
		// Set a 'magnet' on an agent, this contains info on which agent to 
		// pull towards and how strong the pull is (as an index).
		// Will need to reset this at the end of the power up.
		Agent owner = findOwner(allAgents);
		Magnet magnet = new Magnet(owner, getStrength());
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			agent.setMagnet(magnet);
		}
	}
	
	@Override
	protected void unapply(List<Agent> allAgents) {
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			agent.setMagnet(null);
		}
	}

	@Override
	public String getName() {
		return "Magnet";
	}

}
