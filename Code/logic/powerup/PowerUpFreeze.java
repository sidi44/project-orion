package logic.powerup;

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
		super(target, strength, PowerUpType.Freeze);
	}

	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void apply(Agent agent) {
		int baseSpeed = agent.getBaseSpeedIndex();
		agent.setVariableSpeedIndex(-baseSpeed);
	}
	
	@Override
	protected void unapply(Agent agent) {
		agent.setVariableSpeedIndex(0);
	}
	
	@Override
	public String getName() {
		return "Freeze";
	}

}
