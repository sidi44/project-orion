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
	private boolean locked;
	private Agent owner;
	
	// This is used by all power ups to work out how to combine the effects
	// of various power ups applied to an agent.
	private final static PowerUpCombiner combiner = new PowerUpCombiner();
	
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
		this.locked = false;
		this.owner = null;
	}
	
	/**
	 * Decrease the time remaining for this power up by 1.
	 * If the current time remaining is already zero, the time remaining is not
	 * decreased.
	 */
	public void update(List<Agent> allAgents) {
		if (!isLocked()) {
			if (timeRemaining > 0) {
				--timeRemaining;
			}
			if (timeRemaining == 0) {
				deactivate(allAgents);
			}
		}
	}
	
	/**
	 * Activate this power up.
	 * This will apply the effects of the power up to the agent or agents which
	 * this power up targets.
	 * 
	 * @param allAgents - all the agents currently active in the game.
	 */
	public void activate(List<Agent> allAgents) {
		timeRemaining = timeLimit;
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			combiner.add(agent, this);
		}
	}
	
	/**
	 * Is the power up currently activated?
	 * 
	 * @return true if the power up is activated, false otherwise.
	 */
	public boolean isActivated() {
		return (timeRemaining > 0);
	}
	
	/**
	 * Return the group of agents to which this power up will be applied.
	 * 
	 * @return the target group of agents for this power up.
	 */
	public PowerUpTarget getTarget() {
		return target;
	}
	
	/**
	 * Deactivate the power up.
	 * This removes the power up's effects from all affected agents.
	 * 
	 * @param allAgents - all the agents currently active in the game.
	 */
	protected void deactivate(List<Agent> allAgents) {
		List<Agent> toApply = filterToTarget(allAgents);
		for (Agent agent : toApply) {
			combiner.remove(agent, this);
		}
	}
	
	/**
	 * Filter the provided agents to only those that are targeted by this 
	 * power up.
	 * 
	 * @param allAgents - The initial group of agents to filter.
	 * @return a list containing those agents in the provided list which are 
	 * targeted by this power up.
	 */
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
				if (owner != null) {
					filtered.add(owner);
				}
				break;
				
			default:
				throw new IllegalArgumentException("Unknown enum constant");
		}
		
		return filtered;
	}
	
	/**
	 * Apply the effects of this power up to the provided agent.
	 * 
	 * @param agent - the agent to which to apply the power up's effects.
	 */
	protected abstract void apply(Agent agent);
	
	/**
	 * Remove the effects of this power up from the provided agent.
	 * 
	 * @param agent - the agent from which to remove the power up's effects.
	 */
	protected abstract void unapply(Agent agent);
	
	/**
	 * Get the type of this power up.
	 * 
	 * @return the type of this power up.
	 */
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
	 * Tell the power up it's been collected by the given agent.
	 * 
	 * @param agent
	 */
	public void collected(Agent agent) {
		this.owner = agent;
	}
	
	/**
	 * Get the agent which owns this power up.
	 * Will return null if no agent currently owns the power up.
	 * 
	 * @return the agent which owns this power up, or null if no agent owns the
	 * power up.
	 */
	protected Agent getOwner() {
		return owner;
	}
	
	/**
	 * Accept the power up visitor.
	 * 
	 * All concrete classes should implement this method and call visit() on the
	 * provided visitor, passing this class as argument.
	 * 
	 * @param visitor - the visitor to accept.
	 */
	public abstract void accept(PowerUpVisitor visitor);
	
	/**
	 * Lock this power up.
	 * This prevents the time remaining of an activated power up from 
	 * decreasing.
	 */
	public void lock() {
		locked = true;
	}
	
	/**
	 * Unlock this power up.
	 * This allows the time remaining of an activated power up to decrease.
	 */
	public void unlock() {
		locked = false;
	}
	
	/**
	 * Is this power up locked?
	 * 
	 * @return true if the power up is locked, false otherwise.
	 */
	public boolean isLocked() {
		return locked;
	}
	
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
