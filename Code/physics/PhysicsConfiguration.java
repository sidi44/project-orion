package physics;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PhysicsConfiguration class.
 * 
 * Holds and provides access to the parameters that are used by the Physics 
 * Processor.
 * 
 * @author Simon Dicken
 * @version 2015-07-19
 */
@XmlRootElement(name = "PhysicsConfiguration")
public class PhysicsConfiguration {

	private float squareSize;
	private float wallWidthRatio;
	private float pillRadiusRatio;
	private float powerUpRadiusRatio;
	private float predatorSpeed;
	private float preySpeed;
	
	/**
	 * Default constructor for PhysicsConfiguration. 
	 * 
	 * Sets all values to their default values.
	 */
	public PhysicsConfiguration() {
		squareSize = 10.0f;
		wallWidthRatio = 0.1f;
		pillRadiusRatio = 0.2f;
		powerUpRadiusRatio = 0.3f;
		predatorSpeed = 35f;
		preySpeed = 25f;
	}
	
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
	 * @param powerUpRadiusRatio - How much of the space between the square's
	 * walls is occupied by the pill. Must be in the range 0 < x <= 1.
	 * @param predatorSpeed - The speed of all predator agents.
	 * @param preySpeed - The speed of all prey agents.
	 * 
	 * @throws IllegalArgumentExpcetion - if wallWidthRatio or pillRadiusRatio
	 * are not within their valid ranges.
	 */
	public PhysicsConfiguration(float squareSize, float wallWidthRatio, 
			float pillRadiusRatio, float powerUpRadiusRatio, 
			float predatorSpeed, float preySpeed) {
		
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
		
		this.squareSize = squareSize;
		this.wallWidthRatio = wallWidthRatio;
		this.pillRadiusRatio = pillRadiusRatio;
		this.powerUpRadiusRatio = powerUpRadiusRatio;
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
	 * Get the powerUpRadiusRatio.
	 * 
	 * @return powerUpRadiusRatio - How much of the space between the square's 
	 * walls is occupied by the power up.
	 */
	public float getPowerUpRadiusRatio() {
		return powerUpRadiusRatio;
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
	
	/**
	 * @param squareSize the squareSize to set
	 */
	@XmlElement (name = "SquareSize")
	public void setSquareSize(float squareSize) {
		this.squareSize = squareSize;
	}

	/**
	 * @param wallWidthRatio the wallWidthRatio to set
	 */
	@XmlElement (name = "WallWidthRatio")
	public void setWallWidthRatio(float wallWidthRatio) {
		this.wallWidthRatio = wallWidthRatio;
	}

	/**
	 * @param pillRadiusRatio the pillRadiusRatio to set
	 */
	@XmlElement (name = "PillRadiusRatio")
	public void setPillRadiusRatio(float pillRadiusRatio) {
		this.pillRadiusRatio = pillRadiusRatio;
	}

	/**
	 * @param powerUpRadiusRatio the powerUpRadiusRatio to set
	 */
	@XmlElement (name = "PowerUpRadiusRatio")
	public void setPowerUpRadiusRatio(float powerUpRadiusRatio) {
		this.powerUpRadiusRatio = powerUpRadiusRatio;
	}
	
	/**
	 * @param predatorSpeed the predatorSpeed to set
	 */
	@XmlElement (name = "PredatorSpeed")
	public void setPredatorSpeed(float predatorSpeed) {
		this.predatorSpeed = predatorSpeed;
	}

	/**
	 * @param preySpeed the preySpeed to set
	 */
	@XmlElement (name = "PreySpeed")
	public void setPreySpeed(float preySpeed) {
		this.preySpeed = preySpeed;
	}
}
