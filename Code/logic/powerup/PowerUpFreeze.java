package logic.powerup;

import java.util.List;

import logic.Agent;

/**
 * A power up to freeze a target group of Agents for an amount of time.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public class PowerUpFreeze extends PowerUpWithStrength {

	/**
	 * Constructor for PowerUpFreeze.
	 */
	public PowerUpFreeze(PowerUpTarget target, int strength) {
		super(target, strength);
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void apply(List<Agent> allAgents) {
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			apply(agent);
		}
	}
	
	private void apply(Agent agent) {
		int currentSpeed = agent.getSpeedIndex();
		agent.setVariableSpeedIndex(-currentSpeed);
	}

	@Override
	protected void deactivate(List<Agent> allAgents) {
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			agent.setVariableSpeedIndex(0);
		}
	}
	
	@Override
	public String getName() {
		return "Freeze";
	}

}
