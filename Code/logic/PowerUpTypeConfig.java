package logic;

/**
 * The configuration for the powerups types.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
public class PowerUpTypeConfig {
	
	private double predatorSpeedUpFactor;
	private double predatorSlowDownFactor;
	private double preySpeedUpFactor;
	private double preySlowDownFactor;
	
	/**
	 * Default constructor for PowerUpTypeConfig.
	 * 
	 * Sets parameters to their default values.
	 */
	 PowerUpTypeConfig() {
		this.predatorSpeedUpFactor = 2;
		this.predatorSlowDownFactor = 2;
		this.preySpeedUpFactor = 2;
		this.preySlowDownFactor = 2;
	}
	
	/**
	 * Creates an instance of PowerUpTypeConfig.
	 * 
	 * @param predatorSpeedUpFactor (double)
	 * @param predatorSlowDownFactor (double)
	 * @param preySpeedUpFactor (double)
	 * @param preySlowDownFactor (double)
	 */
	public PowerUpTypeConfig(double predatorSpeedUpFactor, double predatorSlowDownFactor,
			double preySpeedUpFactor, double preySlowDownFactor) {
		
		this.predatorSpeedUpFactor = predatorSpeedUpFactor;
		this.predatorSlowDownFactor = predatorSlowDownFactor;
		this.preySpeedUpFactor = preySpeedUpFactor;
		this.preySlowDownFactor = preySlowDownFactor;
	}
	
	/**
	 * Gets the factor to speed up predator by.
	 * 
	 * @return the predatorSpeedUpFactor (double)
	 */
	public double getPredatorSpeedUpFactor() {
		return predatorSpeedUpFactor;
	}

	/**
	 * Sets the factor to speed up predator by.
	 * 
	 * @param predatorSpeedUpFactor (double)
	 */
	public void setPredatorSpeedUpFactor(double predatorSpeedUpFactor) {
		this.predatorSpeedUpFactor = predatorSpeedUpFactor;
	}

	/**
	 * Gets the factor to slow down predator by.
	 * 
	 * @return predatorSlowDownFactor (double)
	 */
	public double getPredatorSlowDownFactor() {
		return predatorSlowDownFactor;
	}

	/**
	 * Sets the factor to slow down predator by.
	 * 
	 * @param predatorSlowDownFactor (double)
	 */
	public void setPredatorDownFactor(double predatorSlowDownFactor) {
		this.predatorSlowDownFactor = predatorSlowDownFactor;
	}

	/**
	 * Gets the factor to speed up prey by.
	 * 
	 * @return preySpeedUpFactor (double)
	 */
	public double getPreySpeedUpFactor() {
		return preySpeedUpFactor;
	}

	/**
	 * Sets the factor to speed up prey by.
	 * 
	 * @param preySpeedUpFactor (double)
	 */
	public void setPreySpeedUpFactor(double preySpeedUpFactor) {
		this.preySpeedUpFactor = preySpeedUpFactor;
	}

	/**
	 * Gets the factor to slow down prey by.
	 * 
	 * @return preySlowDownFactor (double)
	 */
	public double getPreySlowDownFactor() {
		return preySlowDownFactor;
	}

	/**
	 * Sets the factor to slow down prey by.
	 * 
	 * @param preySlowDownFactor (double)
	 */
	public void setPreySlowDownFactor(double preySlowDownFactor) {
		this.preySlowDownFactor = preySlowDownFactor;
	}
	
}
