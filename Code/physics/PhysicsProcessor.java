package physics;

import geometry.PointXY;

import com.badlogic.gdx.math.Vector2;

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
	
	/**
	 * Convert a maze position from the back-end logic into a world coordinate.
	 * 
	 * @param pos - the back-end logic maze position.
	 * @return a physics world coordinate equivalent to the provided position.
	 */
	Vector2 stateToWorld(PointXY pos);
	
	/**
	 * Convert a physics world position into a back-end logic maze coordinate.
	 * 
	 * @param pos - the physics world position to convert.
	 * @return a back-end logic maze position that is equivalent to the provided
	 * physics world coordiante.
	 */
	PointXY worldToState(Vector2 pos);
	
	/**
	 * Gets the size of a maze square.
	 * 
	 * @return squareSize - the size of a maze square
	 */
	float getSquareSize();
	
}
