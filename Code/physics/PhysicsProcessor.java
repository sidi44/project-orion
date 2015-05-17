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
 * @version 2015-05-16
 */
public interface PhysicsProcessor {
	
	/**
	 * Process the provided GameState. This involves extracting the game state
	 * data and applying it to the world (e.g. each Agent's next move), 
	 * simulating the world for a single timestep and updating the GameState
	 * with the post-simulation data.
	 * 
	 * @param state - a snapshot of the current game data.
	 */
	void processGameState(GameState state);
	
}
