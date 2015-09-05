package ai;

import java.util.List;

import logic.Agent;
import logic.GameState;

/**
 * AILogic interface.
 * 
 * AILogic classes should calculate and set the next moves for a given list of 
 * game Agents based on the provided current GameState snapshot.
 * 
 * @author Simon Dicken
 * @version 2015-05-31
 *
 */
public interface AILogic {

	/**
	 * Calculates and sets the agent's next move based on the provided GameState
	 * snapshot.
	 * 
	 * @param agents - the Agents whose moves should be calculated (and set).
	 * @param state - the current state of the game.
	 */
	void calcNextMove(List<Agent> agents, GameState state);
	
}
