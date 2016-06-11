package logic.powerup;

import java.util.ArrayList;
import java.util.List;

import logic.Agent;
import logic.Predator;
import logic.Prey;

/**
 * Abstract class for power-ups for both prey and predators.
 * 
 * @author Martin Wong, Simon Dicken
 * @version 2015-10-18
 */
public abstract class PowerUp {
	
	private final int timeLimit;
	private int timeRemaining;
	
	private PowerUpTarget target;
	
	/**
	 * Constructor for PowerUp.
	 * 
	 * @param timeLimit - the duration of the power up.
	 */
	public PowerUp(int timeLimit, PowerUpTarget target) {
		this.timeLimit = timeLimit;
		this.timeRemaining = -1;
		this.target = target;
	}
	
	/**
	 * Decrease the time remaining for this power up by 1.
	 * If the current time remaining is already zero, the time remaining is not
	 * decreased.
	 */
	public void update(List<Agent> allAgents) {
		if (timeRemaining > 0) {
			--timeRemaining;
		}
		if (timeRemaining == 0) {
			deactivate(allAgents);
		}
	}
	
	/**
	 * Activate this power up.
	 */
	public void activate(List<Agent> allAgents) {
		timeRemaining = timeLimit;
		apply(allAgents);
	}
	
	public boolean isActivated() {
		return (timeRemaining > 0);
	}
	
	protected abstract void deactivate(List<Agent> allAgents);
	
	protected List<Agent> filterToTarget(List<Agent> allAgents) {
		
		List<Agent> filtered = new ArrayList<Agent>();
		
		switch (target) {
		
			case AllPredators:
				for (Agent agent : allAgents) {
					if (agent instanceof Predator) {
						filtered.add(agent);
					}
				}
				break;
				
			case AllPrey:
				for (Agent agent : allAgents) {
					if (agent instanceof Prey) {
						filtered.add(agent);
					}
				}
				break;
				
			case Owner:
				Agent owner = findOwner(allAgents);
				if (owner != null) {
					filtered.add(owner);
				}
				break;
				
			default:
				throw new IllegalArgumentException("Unknown enum constant");
		}
		
		return filtered;
	}
	
	protected abstract void apply(List<Agent> allAgents);

	protected Agent findOwner(List<Agent> allAgents) {
		
		Agent owner = null;
		int num = 0;
		for (Agent agent : allAgents) {
			List<PowerUp> powerUps = agent.getActivatedPowerUps();
			if (powerUps.contains(this)) {
				owner = agent;
				++num;
			}
		}
		if (num != 1) {
			System.err.println("A power up should have exactly one owner.");
		}
		
		return owner;
	}
	
	/**
	 * Return the name of this power up. 
	 * This should be the String representation of the type of the power up. 
	 * 
	 * @return the name of this power up.
	 */
	public abstract String getName();
	
	/**
	 * Accept the power up visitor.
	 * 
	 * All concrete classes should implement this method and call visit() on the
	 * provided visitor, passing this class as argument.
	 * 
	 * @param visitor - the visitor to accept.
	 */
	public abstract void accept(PowerUpVisitor visitor);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + timeLimit;
		result = prime * result + timeRemaining;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PowerUp)) {
			return false;
		}
		PowerUp other = (PowerUp) obj;
		if (timeLimit != other.timeLimit) {
			return false;
		}
		if (timeRemaining != other.timeRemaining) {
			return false;
		}
		return true;
	}
	
}
