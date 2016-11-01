package logic.powerup;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logic.Agent;

/**
 * This class knows how to combine the effects of multiple power ups applied to
 * an agent. 
 * A new power up can be applied to an agent, taking into account any existing 
 * power ups, by calling the add() method.
 * Similarly, the remove() method can remove the effects of a power up from an 
 * agent and leave only the effects of any other power ups that are still appied
 * to that agent.
 * 
 * @author Simon Dicken
 */
class PowerUpCombiner {
	
	/**
	 * Constructor.
	 */
	public PowerUpCombiner() {
	}
	
	/**
	 * Apply the provided power up to the provided agent, taking into account
	 * any existing power ups applied to the agent.
	 * 
	 * @param agent - the agent to apply the power up to.
	 * @param powerUp - the power up to apply to the agent.
	 */
	public void add(Agent agent, PowerUp powerUp) {
		
		agent.powerUpApplied(powerUp);
		
		combinePowerUps(agent);
	}
	
	/**
	 * Remove the provided power up from the provided agent, leaving the effects
	 * of any other power ups still applied to the agent.
	 * 
	 * @param agent - the agent to remove the power up from.
	 * @param powerUp - the power up to remove from the agent.
	 */
	public void remove(Agent agent, PowerUp powerUp) {
		
		powerUp.unapply(agent);
		agent.powerUpTerminated(powerUp);
		
		combinePowerUps(agent);
	}
	
	/**
	 * Combine the effects of all power ups applied to the provided agent 
	 * correctly.
	 * 
	 * @param agent - the agent to which to apply the effects of its power ups.
	 */
	private void combinePowerUps(Agent agent) {
		
		// Start with a clean slate by removing the effects of all the power 
		// ups that are currently applied to the agent
		removePowerUpEffects(agent);
		
		// Sort the power ups applied to the agent. Freeze power ups override
		// the effects of other speed altering power ups, so we need to ensure
		// that they are applied last.
		List<PowerUp> powerUps = agent.getPowerUpsAppliedToMe();
		Collections.sort(powerUps, new PowerUpSorter());
		
		// Keep track of the power up types that have been applied to the agent.
		// Only one of each type can be applied to a particular agent at once.
		Set<PowerUpType> types = new HashSet<PowerUpType>();
		for (PowerUp pu : powerUps) {
			if (types.contains(pu.getType())) {
				// There's already a power up of this type applied, so lock 
				// this one.
				pu.lock();
			} else {
				// Make sure the power up is unlocked and apply its effects to
				// the agent.
				pu.unlock();
				pu.apply(agent);
				types.add(pu.getType());
			}
		}
	}
	
	/**
	 * Remove the effects of any power ups applied to the provided agent.
	 * 
	 * @param agent - the agent from which to remove power ups.
	 */
	private void removePowerUpEffects(Agent agent) {
		
		List<PowerUp> powerUps = agent.getPowerUpsAppliedToMe();
		
		for (PowerUp powerUp : powerUps) {
			powerUp.unapply(agent);
		}
		
	}
	
	/**
	 * Class that can be used to sort a collection of power ups so that they 
	 * are applied in the correct order.
	 * The Freeze power up overrules other power ups that alter an agent's 
	 * speed, so these are moved to the end of the collection.
	 * 
	 * @author Simon Dicken
	 */
	private class PowerUpSorter implements Comparator<PowerUp> {

		@Override
		public int compare(PowerUp arg0, PowerUp arg1) {
			
			if (arg0 instanceof PowerUpFreeze && 
				arg1 instanceof PowerUpFreeze) {
				return 0;
			} else if (arg0 instanceof PowerUpFreeze) {
				return 1;
			} else if (arg1 instanceof PowerUpFreeze) {
				return -1;
			} else {
				return 0;
			}
		}
		
	}
	
}
