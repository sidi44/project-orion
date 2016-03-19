package physics;


/**
 * PhysicsConfiguration class.
 * 
 * Holds and provides access to the parameters that are used by the Physics 
 * Processor.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
public class PhysicsConfiguration {

	private float wallWidthRatio;
	private float pillRadiusRatio;
	private float powerUpRadiusRatio;
	private int predatorSpeedIndex;
	private int preySpeedIndex;
	
	private PhysicsSpeedConverter speedConverter;
	
	/**
	 * Default constructor for PhysicsConfiguration. 
	 * 
	 * Sets parameters to their default values.
	 */
	public PhysicsConfiguration() {
		wallWidthRatio = 0.1f;
		pillRadiusRatio = 0.2f;
		powerUpRadiusRatio = 0.3f;
		predatorSpeedIndex = 10;
		preySpeedIndex = 8;
		speedConverter = new PhysicsSpeedConverter();
	}
	
	/**
	 * Constructor for PhysicsConfiguration.
	 * 
	 * @param wallWidthRatio - How much of the square is occupied by the walls. 
	 * Must be in the range 0 < x <= 0.4 . The upper limit is set to 0.4 for 
	 * practicality. Increasing the ratio above this limit would lead to 
	 * oddly-proportioned mazes.
	 * @param pillRadiusRatio - How much of the space between the square's walls 
	 * is occupied by the pill. Must be in the range 0 < x <= 1. 
	 * @param powerUpRadiusRatio - How much of the space between the square's
	 * walls is occupied by the pill. Must be in the range 0 < x <= 1.
	 * @param predatorSpeedIndex - The index for the speed of all predator 
	 * agents. Must be in the range 1 <= x <= 10, where 1 is the slowest.
	 * @param preySpeedIndex - The index for the speed of all prey agents. Must 
	 * be in the range 1 <= x <= 10, where 1 is the slowest.
	 * 
	 * @throws IllegalArgumentExpcetion - if wallWidthRatio or pillRadiusRatio
	 * are not within their valid ranges.
	 */
	public PhysicsConfiguration(float wallWidthRatio, float pillRadiusRatio, 
			float powerUpRadiusRatio, int predatorSpeedIndex, 
			int preySpeedIndex) {
		
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
		
		if (powerUpRadiusRatio <= 0.0f || powerUpRadiusRatio > 1.0f) {
			throw new IllegalArgumentException(
				"Power up radius ratio should be in the range 0 < x <= 1."
			);
		}
		
		this.wallWidthRatio = wallWidthRatio;
		this.pillRadiusRatio = pillRadiusRatio;
		this.powerUpRadiusRatio = powerUpRadiusRatio;
		this.predatorSpeedIndex = predatorSpeedIndex;
		this.preySpeedIndex = preySpeedIndex;
		
		this.speedConverter = new PhysicsSpeedConverter();
	}
	
	public PhysicsConfiguration(PhysicsConfiguration other) {
		this.wallWidthRatio = other.wallWidthRatio;
		this.pillRadiusRatio = other.pillRadiusRatio;
		this.powerUpRadiusRatio = other.powerUpRadiusRatio;
		this.predatorSpeedIndex = other.predatorSpeedIndex;
		this.preySpeedIndex = other.preySpeedIndex;
		
		this.speedConverter = new PhysicsSpeedConverter();
	}

	/**
	 * Get the squareSize.
	 * 
	 * @return squareSize - The size of each maze square.
	 */
	public float getSquareSize() {
		return speedConverter.getSquareSize();
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
	 * Get the powerUpRadiusRatio.
	 * 
	 * @return powerUpRadiusRatio - How much of the space between the square's 
	 * walls is occupied by the power up.
	 */
	public float getPowerUpRadiusRatio() {
		return powerUpRadiusRatio;
	}

//	/**
//	 * Get the predatorSpeedIndex.
//	 * 
//	 * @return predatorSpeedIndex - The index indicating the speed of all 
//	 * predator agents.
//	 */
//	public int getPredatorSpeedIndex() {
//		return predatorSpeedIndex;
//	}
//	
//	/**
//	 * Get the preySpeedIndex.
//	 * 
//	 * @return preySpeedIndex - The index indicating the speed of all prey 
//	 * agents.
//	 */
//	public int getPreySpeedIndex() {
//		return preySpeedIndex;
//	}
	
	/**
	 * Get the predatorSpeed.
	 * 
	 * @return predatorSpeed - The speed of all predator agents.
	 */
	public float getPredatorSpeed() {
		return speedConverter.getSpeed(predatorSpeedIndex);
	}

	/**
	 * Get the preySpeed.
	 * 
	 * @return preySpeed - The speed of all prey agents.
	 */
	public float getPreySpeed() {
		return speedConverter.getSpeed(preySpeedIndex);
	}

	public float getTimestep() {
		return speedConverter.getTimestep();
	}
	
	/**
	 * Set the wallWidthRatio.
	 * 
	 * @param wallWidthRatio - How much of the square is occupied by the walls.
	 */
	public void setWallWidthRatio(float wallWidthRatio) {
		this.wallWidthRatio = wallWidthRatio;
	}

	/**
	 * Set the pillRadiusRatio.
	 * 
	 * @param pillRadiusRatio - How much of the space between the square's 
	 * walls is occupied by the pill.
	 */
	public void setPillRadiusRatio(float pillRadiusRatio) {
		this.pillRadiusRatio = pillRadiusRatio;
	}

	/**
	 * Set the powerUpRadiusRatio.
	 * 
	 * @param powerUpRadiusRatio - How much of the space between the square's 
	 * walls is occupied by the power up.
	 */
	public void setPowerUpRadiusRatio(float powerUpRadiusRatio) {
		this.powerUpRadiusRatio = powerUpRadiusRatio;
	}
	
	/**
	 * Set the predatorSpeedIndex.
	 * 
	 * @param predatorSpeedIndex - The index indicating the speed of all 
	 * predator agents.
	 */
	public void setPredatorSpeedIndex(int predatorSpeedIndex) {
		this.predatorSpeedIndex = predatorSpeedIndex;
	}

	/**
	 * Set the preySpeedIndex.
	 * 
	 * @param preySpeedIndex - The index indicating the speed of all prey 
	 * agents.
	 */
	public void setPreySpeedIndex(int preySpeedIndex) {
		this.preySpeedIndex = preySpeedIndex;
	}
}
