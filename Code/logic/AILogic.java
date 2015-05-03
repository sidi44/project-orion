package logic;

/**
 * AILogic interface.
 * 
 * Classes which implement this interface should provide an implementation of
 * the calcNextMove() method which will use the current game state to 
 * calculate how the provided agent's will move. The agent's nextMove field 
 * should be set in the method.
 * 
 * @author Simon Dicken
 * @version 2015-05-03
 *
 */
public interface AILogic {

	/**
	 * Uses to the provided state to calculate the agent's next move. The 
	 * agent's nextMove field is set by the method.
	 * 
	 * @param agent - the Agent whose move should be calculated.
	 * @param state - the current state of the game.
	 */
	void calcNextMove(Agent agent, GameState state);
	
}
