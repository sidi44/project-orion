package physics;

/**
 * PhysicsConfiguration class.
 * 
 * Holds and provides access to the parameters that are used by the Physics 
 * Processor.
 * 
 * @author Simon Dicken
 * @version 2015-06-02
 */
public class PhysicsConfiguration {

	private float squareSize;
	private float wallWidthRatio;
	private float pillRadiusRatio;
	private float predatorSpeed;
	private float preySpeed;
	
	/**
	 * Constructor for PhysicsConfiguration.
	 * 
	 * @param squareSize - The size of each maze square.
	 * @param wallWidthRatio - How much of the square is occupied by the walls. 
	 * Must be in the range 0 < x <= 0.4 . The upper limit is set to 0.4 for 
	 * practicality. Increasing the ratio above this limit would lead to 
	 * oddly-proportioned mazes.
	 * @param pillRadiusRatio - How much of the space between the square's walls 
	 * is occupied by the pill. Must be in the range 0 < x <= 1. 
	 * @param predatorSpeed - The speed of all predator agents.
	 * @param preySpeed - The speed of all prey agents.
	 * 
	 * @throws IllegalArgumentExpcetion - if wallWidthRatio or pillRadiusRatio
	 * are not within their valid ranges.
	 */
	public PhysicsConfiguration(float squareSize, float wallWidthRatio, 
			float pillRadiusRatio, float predatorSpeed, float preySpeed) {
		
		if (wallWidthRatio <= 0.0f || wallWidthRatio > 0.4f) {
			throw new IllegalArgumentException(
				"Wall width ratio should be in the range 0 < x <= 0.4."
			);
		}
		
		if (pillRadiusRatio <= 0.0f || pillRadiusRatio > 1.0f) {
			throw new IllegalArgumentException(
				"Pill radius ratio should be in the range 0 < x <= 1."
			);
		}
		
		this.squareSize = squareSize;
		this.wallWidthRatio = wallWidthRatio;
		this.pillRadiusRatio = pillRadiusRatio;
		this.predatorSpeed = predatorSpeed;
		this.preySpeed = preySpeed;
	}

	/**
	 * Get the squareSize.
	 * 
	 * @return squareSize - The size of each maze square.
	 */
	public float getSquareSize() {
		return squareSize;
	}

	/**
	 * Get the wallWidthRatio.
	 * 
	 * @return wallWidthRatio - How much of the square is occupied by the walls.
	 */
	public float getWallWidthRatio() {
		return wallWidthRatio;
	}
	
	/**
	 * Get the pillRadiusRatio.
	 * 
	 * @return pillRadiusRatio - How much of the space between the square's 
	 * walls is occupied by the pill.
	 */
	public float getPillRadiusRatio() {
		return pillRadiusRatio;
	}

	/**
	 * Get the predatorSpeed.
	 * 
	 * @return predatorSpeed - The speed of all predator agents.
	 */
	public float getPredatorSpeed() {
		return predatorSpeed;
	}

	/**
	 * Get the preySpeed.
	 * 
	 * @return preySpeed - The speed of all prey agents.
	 */
	public float getPreySpeed() {
		return preySpeed;
	}
	
}
