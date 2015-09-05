package physics;

import logic.GameState;

/**
 * PhysicsProcessor interface.
 * 
 * PhysicsProcessors can take a GameState object, apply the data held by it to 
 * the modelled world, simulate the world for a timestep and update the 
 * GameState snapshot with the post-simulation data.
 * 
 * @author Simon Dicken
 * @version 2015-06-09
 */
public interface PhysicsProcessor {
	
	/**
	 * Carry out any work that needs to be done immediately BEFORE the 
	 * simulation is stepped. This includes extracting the game state data and
	 * applying it to the world (e.g. each Agent's next move).
	 * 
	 * @param state - a snapshot of the current game data.
	 */
	void preStep(GameState state);
	
	/**
	 * Advance the physics simulation in its current state by the specified 
	 * amount of time. 
	 * 
	 * @param timestep - the amount of time to simulate.
	 */
	void stepSimulation(float timestep);
	
	/**
	 * Carry out any work that needs to be done immediately AFTER the simulation 
	 * is stepped. This involves updating the game state with the 
	 * post-simulation data (e.g. new positions of each Agent).
	 * 
	 * @param state - a snapshot of the current game data.
	 */
	void postStep(GameState state);
	
}
