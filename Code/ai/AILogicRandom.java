package ai;

import java.util.List;
import java.util.Random;

import logic.Agent;
import logic.Direction;
import logic.GameState;

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
	public void calcNextMove(List<Agent> agents, GameState state) {

		Random r = new Random();
		
		// Get all possible directions.
		Direction[] allDir = Direction.values();
		
		for (Agent agent : agents) {
			// Get a random direction and set the Agent's next move.
			int dirNum = r.nextInt(allDir.length);
			agent.setNextMoveDirection(allDir[dirNum]);
		}
	}

}
