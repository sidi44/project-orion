package logic.powerup;

import geometry.PointXY;
import logic.Agent;

public class Magnet {

	private Agent agent;
	private int strength;
	
	public Magnet(Agent agent, int strength) {
		this.agent = agent;
		this.strength = strength;
	}
	
	public PointXY getFocalPoint() {
		return agent.getPosition();
	}
	
	public int getStrength() {
		return strength;
	}
	
}
