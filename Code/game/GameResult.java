package game;

import logic.GameOver;

public class GameResult {

	private GameOver result;
	private int numSimSteps;
	private int numPillsRemaining;
	private int numSquares;
	
	public GameResult() {
		this.result = GameOver.No;
		this.numSimSteps = 0;
		this.numPillsRemaining = 0;
		this.numSquares = 0;
	}
	
	public GameResult(GameOver result, int numSimSteps, int numPillsRemaining, 
			int numSquares) {
		this.result = result;
		this.numSimSteps = numSimSteps;
		this.numPillsRemaining = numPillsRemaining;
		this.numSquares = numSquares;
	}

	/**
	 * @return the gameResult
	 */
	public GameOver getGameResult() {
		return result;
	}

	/**
	 * @param gameResult the gameResult to set
	 */
	public void setGameResult(GameOver gameResult) {
		this.result = gameResult;
	}

	/**
	 * @return the numSimSteps
	 */
	public int getNumSimSteps() {
		return numSimSteps;
	}

	/**
	 * @param numSimSteps the numSimSteps to set
	 */
	public void setNumSimSteps(int numSimSteps) {
		this.numSimSteps = numSimSteps;
	}

	/**
	 * @return the numPillsRemaining
	 */
	public int getNumPillsRemaining() {
		return numPillsRemaining;
	}

	/**
	 * @param numPillsRemaining the numPillsRemaining to set
	 */
	public void setNumPillsRemaining(int numPillsRemaining) {
		this.numPillsRemaining = numPillsRemaining;
	}

	/**
	 * @return the numSquares
	 */
	public int getNumSquares() {
		return numSquares;
	}

	/**
	 * @param numSquares the numSquares to set
	 */
	public void setNumSquares(int numSquares) {
		this.numSquares = numSquares;
	}
	
}
