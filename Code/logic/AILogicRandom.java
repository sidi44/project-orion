package logic;

import java.util.Random;

/**
 * AILogicRandom class.
 * Implements the AILogic interface by simply setting the Agent's next move 
 * to be a random direction. 
 * 
 * @author Simon Dicken
 * @version 2015-05-03
 */
public class AILogicRandom implements AILogic {

	/**
	 * Constructor for AILogicRandom.
	 */
	public AILogicRandom() {
		
	}
	
	@Override
	public void calcNextMove(Agent agent, GameState state) {
		
		Random r = new Random(System.currentTimeMillis());
		
		// Get all possible moves.
		MoveDir[] allMoves = MoveDir.values();
		
		// Get a random move and set the Agent's next move.
		int dirNum = r.nextInt(allMoves.length);
		Move m = new Move(allMoves[dirNum]);
		agent.setNextMove(m);
		
	}

}
