package logic;

/**
 * The configuration for the powerups types.
 * 
 * @author Martin Wong
 * @version 2015-08-13
 */
public class PowerUpTypeConfig {
	
	private double predatorUpFactor;
	private double predatorDownFactor;
	private double preyUpFactor;
	private double preyDownFactor;
	
	/**
	 * Default constructor for PowerUpTypeConfig.
	 * 
	 * Sets parameters to their default values.
	 */
	 PowerUpTypeConfig() {
		this.predatorUpFactor = 2;
		this.predatorDownFactor = 2;
		this.preyUpFactor = 2;
		this.preyDownFactor = 2;
	}
	
	/**
	 * Creates an instance of PowerUpTypeConfig.
	 * 
	 * @param predatorUpFactor (double)
	 * @param predatorDownFactor (double)
	 * @param preyUpFactor (double)
	 * @param preyDownFactor (double)
	 */
	public PowerUpTypeConfig(double predatorUpFactor, double predatorDownFactor,
			double preyUpFactor, double preyDownFactor) {
		
		this.predatorUpFactor = predatorUpFactor;
		this.predatorDownFactor = predatorDownFactor;
		this.preyUpFactor = preyUpFactor;
		this.preyDownFactor = preyDownFactor;
	}
	
	/**
	 * Gets the factor to speed up predator by.
	 * 
	 * @return the predatorUpFactor (double)
	 */
	public double getPredatorUpFactor() {
		return predatorUpFactor;
	}

	/**
	 * Sets the factor to speed up predator by.
	 * 
	 * @param predatorUpFactor (double)
	 */
	public void setPredatorUpFactor(double predatorUpFactor) {
		this.predatorUpFactor = predatorUpFactor;
	}

	/**
	 * Gets the factor to slow down predator by.
	 * 
	 * @return predatorDownFactor (double)
	 */
	public double getPredatorDownFactor() {
		return predatorDownFactor;
	}

	/**
	 * Sets the factor to slow down predator by.
	 * 
	 * @param predatorDownFactor (double)
	 */
	public void setPredatorDownFator(double predatorDownFactor) {
		this.predatorDownFactor = predatorDownFactor;
	}

	/**
	 * Gets the factor to speed up prey by.
	 * 
	 * @return preyUpFactor (double)
	 */
	public double getPreyUpFactor() {
		return preyUpFactor;
	}

	/**
	 * Sets the factor to speed up prey by.
	 * 
	 * @param preyUpFactor (double)
	 */
	public void setPreyUpFactor(double preyUpFactor) {
		this.preyUpFactor = preyUpFactor;
	}

	/**
	 * Gets the factor to slow down prey by.
	 * 
	 * @return preyDownFactor (double)
	 */
	public double getPreyDownFactor() {
		return preyDownFactor;
	}

	/**
	 * Sets the factor to slow down prey by.
	 * 
	 * @param preyDownFactor (double)
	 */
	public void setPreyDownFactor(double preyDownFactor) {
		this.preyDownFactor = preyDownFactor;
	}
	
}
