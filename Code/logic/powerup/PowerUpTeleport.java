package logic.powerup;

import java.util.List;

import logic.Agent;
import geometry.PointXY;

public class PowerUpTeleport extends PowerUp {
	
	private final PointXY teleportPoint;

	public PowerUpTeleport(PointXY teleportPoint) {
		super(1, PowerUpTarget.Owner);
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
	protected void apply(List<Agent> allAgents) {
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			agent.setPosition(teleportPoint);
		}
	}

	@Override
	protected void deactivate(List<Agent> allAgents) {
		// Nothing to for this power up
	}
	
	@Override
	public String getName() {
		return "Teleport";
	}
	
}
