package logic.powerup;

import java.util.List;

import logic.Agent;

/**
 * A power up to slow down the targeted agents.
 */
public class PowerUpSlowDown extends PowerUpWithStrength {

	public PowerUpSlowDown(PowerUpTarget target, int strength) {
		super(target, strength, PowerUpType.SlowDown);
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
		int variableSpeedIndex = getVariableSpeedIndex();
		agent.setVariableSpeedIndex(variableSpeedIndex);
	}
	
	private int getVariableSpeedIndex() {
		int strength = getStrength();
		int baseDecrease = -1;
		return baseDecrease * strength;
	}

	@Override
	protected void unapply(List<Agent> allAgents) {
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			agent.setVariableSpeedIndex(0);
		}
	}
	
	@Override
	public String getName() {
		return "SlowDown";
	}
	
}
