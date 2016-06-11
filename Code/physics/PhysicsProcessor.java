package physics;

import logic.GameState;
import geometry.PointXY;
import callback.Sender;

import com.badlogic.gdx.math.Vector2;

/**
 * PhysicsProcessor interface.
 * 
 * PhysicsProcessors can take a GameState object, apply the data held by it to 
 * the modelled world, simulate the world for a timestep and update the 
 * GameState snapshot with the post-simulation data.
 * 
 * @author Simon Dicken
 * @version 2015-12-28
 */
public abstract class PhysicsProcessor extends Sender {
	
	
	public abstract PhysicsGameWorld getWorld();
	
	/**
	 * Advance the physics simulation in its current state by the specified 
	 * amount of time. 
	 * 
	 * @param timestep - the amount of time to simulate.
	 */
	public abstract int stepSimulation(float timestep, GameState state);
	
	/**
	 * Convert a maze position from the back-end logic into a world coordinate.
	 * 
	 * @param pos - the back-end logic maze position.
	 * @return a physics world coordinate equivalent to the provided position.
	 */
	public abstract Vector2 stateToWorld(PointXY pos);
	
	/**
	 * Convert a physics world position into a back-end logic maze coordinate.
	 * 
	 * @param pos - the physics world position to convert.
	 * @return a back-end logic maze position that is equivalent to the provided
	 * physics world coordinate.
	 */
	public abstract PointXY worldToState(Vector2 pos);
	
	/**
	 * Gets the size of a maze square.
	 * 
	 * @return squareSize - the size of a maze square
	 */
	public abstract float getSquareSize();
	
	/**
	 * Set what type of debug information to process and display. The default is
	 * DebugNone.
	 * 
	 * @param type - the type of debug information to process and display.
	 */
	public abstract void setDebugCategory(PhysicsDebugType type);
	
}
