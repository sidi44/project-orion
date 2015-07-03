package logic;

import java.util.Random;

/**
 * AILogicRandom class.
 * 
 * Implements the AILogic interface by simply setting the Agent's next move 
 * to be a random direction. 
 * 
 * @author Simon Dicken
 * @version 2015-05-16
 */
public class AILogicRandom implements AILogic {
	
	/**
	 * Constructor for AILogicRandom.
	 */
	public AILogicRandom() {
		
	}
	
	@Override
	public void calcNextMove(Agent agent, GameState state) {

		Random r = new Random();
		
		// Get all possible directions.
		Direction[] allDir = Direction.values();
		
		// Get a random direction and set the Agent's next move.
		int dirNum = r.nextInt(allDir.length);
		agent.setNextMoveDirection(allDir[dirNum]);
		
	}

}
