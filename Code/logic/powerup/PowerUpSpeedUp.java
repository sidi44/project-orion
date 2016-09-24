package logic.powerup;

import logic.Agent;

/**
 * A power up to speed up the targeted agents.
 */
public class PowerUpSpeedUp extends PowerUpWithStrength {
	
	public PowerUpSpeedUp(PowerUpTarget target, int strength) {
		super(target, strength, PowerUpType.SpeedUp);
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
		int baseIncrease = 1;
		return baseIncrease * strength;
	}
	
	@Override
	protected void unapply(Agent agent) {
		agent.setVariableSpeedIndex(0);
	}
	
	@Override
	public String getName() {
		return "SpeedUp";
	}
	
}
