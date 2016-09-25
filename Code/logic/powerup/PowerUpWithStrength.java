package logic.powerup;

public abstract class PowerUpWithStrength extends PowerUp {

	private int strength;
	
	private final static int TIME_LIMIT_BASE = 120;
	
	public PowerUpWithStrength(PowerUpTarget target, int strength, PowerUpType type) {
		super(getTimeLimit(strength), target, type);
		
		if (strength < 1 || strength > 3) {
			throw new IllegalArgumentException("Invalid strength value: " + 
											   strength);
		}
		
		this.strength = strength;
	}

	protected int getStrength() {
		return strength;
	}

	protected static int getTimeLimit(int strength) {
		return TIME_LIMIT_BASE * strength;
	}
	
}
