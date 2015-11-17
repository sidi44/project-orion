package physics;

import logic.GameState;
import geometry.PointXY;

import com.badlogic.gdx.math.Vector2;

/**
 * PhysicsProcessor interface.
 * 
 * PhysicsProcessors can take a GameState object, apply the data held by it to 
 * the modelled world, simulate the world for a timestep and update the 
 * GameState snapshot with the post-simulation data.
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public interface PhysicsProcessor {
	
	/**
	 * Advance the physics simulation in its current state by the specified 
	 * amount of time. 
	 * 
	 * @param timestep - the amount of time to simulate.
	 */
	int stepSimulation(float timestep, GameState state);
	
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
	 * physics world coordinate.
	 */
	PointXY worldToState(Vector2 pos);
	
	/**
	 * Gets the size of a maze square.
	 * 
	 * @return squareSize - the size of a maze square
	 */
	float getSquareSize();
	
	/**
	 * Return the default speed of the given physics body type.
	 * 
	 * @param type - the physics body type for which to return the speed.
	 * @return the speed of the provided physics body type.
	 */
	float getBodySpeed(PhysicsBodyType type);
	
}
