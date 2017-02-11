package logic;

/**
 * Defines the ways in which the game can be won or lost. 
 * 
 * A state also exists for 'NotFinished' for the case where the game has not 
 * yet been won/lost.
 * 
 * @author Simon Dicken
 */
public enum GameOverReason {

	NotFinished,
	PreyWon_Timeout,
	PreyWon_Pills,
	PredatorWon
	
}
