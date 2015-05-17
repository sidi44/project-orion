package logic;

/**
 * AILogic interface.
 * 
 * AILogic classes should calculate and set the next move of a given game Agent 
 * based on the provided current GameState snapshot.
 * 
 * @author Simon Dicken
 * @version 2015-05-16
 *
 */
public interface AILogic {

	/**
	 * Calculates and sets the agent's next move based on the provided GameState
	 * snapshot.
	 * 
	 * @param agent - the Agent whose move should be calculated (and set).
	 * @param state - the current state of the game.
	 */
	void calcNextMove(Agent agent, GameState state);
	
}
