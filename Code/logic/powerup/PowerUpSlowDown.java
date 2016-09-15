package logic.powerup;

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
	protected void apply(Agent agent) {
		int variableSpeedIndexChange = getVariableSpeedIndex();
		int currentVariableSpeedIndex = agent.getVariableSpeedIndex();
		int newVariableSpeedIndex = 
				currentVariableSpeedIndex + variableSpeedIndexChange;
		agent.setVariableSpeedIndex(newVariableSpeedIndex);
	}
	
	private int getVariableSpeedIndex() {
		int strength = getStrength();
		int baseDecrease = -1;
		return baseDecrease * strength;
	}

	@Override
	protected void unapply(Agent agent) {
		agent.setVariableSpeedIndex(0);
	}
	
	@Override
	public String getName() {
		return "SlowDown";
	}
	
}
