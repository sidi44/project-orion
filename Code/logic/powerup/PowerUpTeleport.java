package logic.powerup;

import logic.Agent;
import geometry.PointXY;

public class PowerUpTeleport extends PowerUp {
	
	private final PointXY teleportPoint;

	public PowerUpTeleport(PointXY teleportPoint) {
		super(1, PowerUpTarget.Owner, PowerUpType.Teleport);
		this.teleportPoint = teleportPoint;
	}
	
	/**
	 * Gets the coordinates to teleport to.
	 * 
	 * @return nextPoint (PointXY)
	 */
	public PointXY getTeleportPoint() {
		return teleportPoint;
	}
	
	@Override
	public void accept(PowerUpVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void apply(Agent agent) {
		agent.setPosition(teleportPoint);
	}

	@Override
	protected void unapply(Agent agent) {
		// Nothing to for this power up
	}
	
	@Override
	public String getName() {
		return "Teleport";
	}
	
}
