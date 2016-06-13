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
	private final PowerUpType type;
	
	private PowerUpTarget target;
	
	/**
	 * Constructor for PowerUp.
	 * 
	 * @param timeLimit - the duration of the power up.
	 */
	public PowerUp(int timeLimit, PowerUpTarget target, PowerUpType type) {
		this.timeLimit = timeLimit;
		this.timeRemaining = -1;
		this.target = target;
		this.type = type;
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
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			agent.powerUpApplied(this);
		}
	}
	
	public boolean isActivated() {
		return (timeRemaining > 0);
	}
	
	public PowerUpTarget getTarget() {
		return target;
	}
	
	protected void deactivate(List<Agent> allAgents) {
		unapply(allAgents);
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			agent.powerUpTerminated(this);
		}
	}
	
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

	protected abstract void unapply(List<Agent> allAgents);
	
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
	
	public PowerUpType getType() {
		return type;
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
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + timeLimit;
		result = prime * result + timeRemaining;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowerUp other = (PowerUp) obj;
		if (target != other.target)
			return false;
		if (timeLimit != other.timeLimit)
			return false;
		if (timeRemaining != other.timeRemaining)
			return false;
		return true;
	}
	
}
