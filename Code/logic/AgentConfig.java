package logic;

/**
 * The configuration for the agents.
 * 
 * @author Martin Wong
 * @version 2015-08-09
 */
public class AgentConfig {
	
	private int numPred;
	private int numPredPlayer;
	private int maxPredPowerUp;
	private int predBaseSpeedIndex;
	private int numPrey;
	private int numPreyPlayer;
	private int maxPreyPowerUp;
	private int preyBaseSpeedIndex;
	
	/**
	 * Default constructor for AgentConfig.
	 * 
	 * Sets parameters to their default values.
	 */
	public AgentConfig() {
		this.numPred = 1;
		this.numPredPlayer = 1;
		this.maxPredPowerUp = 5;
		this.predBaseSpeedIndex = 4;
		this.numPrey = 5;
		this.numPreyPlayer = 0;
		this.maxPreyPowerUp = 5;
		this.preyBaseSpeedIndex = 2;
	}
	
	/**
	 * Creates an instance of AgentConfig.
	 * 
	 * @param numPred (int)
	 * @param numPredPlayer (int)
	 * @param numPrey (int)
	 * @param numPreyPlayer (int)
	 */
	public AgentConfig(int numPred, int numPredPlayer, int maxPredPowerUp,
			int predBaseSpeedIndex, int numPrey, int numPreyPlayer, 
			int maxPreyPowerUp, int preyBaseSpeedIndex) {
		
		this.numPred = numPred;
		this.numPredPlayer = numPredPlayer;
		this.maxPredPowerUp = maxPredPowerUp;
		this.predBaseSpeedIndex = predBaseSpeedIndex;
		this.numPrey = numPrey;
		this.numPreyPlayer = numPreyPlayer;
		this.maxPreyPowerUp = maxPreyPowerUp;
		this.predBaseSpeedIndex = preyBaseSpeedIndex;
		
		try {
			if ((this.numPred < this.numPredPlayer)
					|| (this.numPrey < this.numPreyPlayer)) {
				
				throw new Exception(
						"Illegal Agent Configuration: the predator "
						+ "/ prey configurations are incorrect.");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * Gets the total number of predators.
	 * 
	 * @return numPred (int)
	 */
	public int getNumPred() {
		return this.numPred;
	}
	
	/**
	 * Sets the total number of predators.
	 * 
	 * @param numPred (int)
	 */
	public void setNumPred(int numPred) {
		this.numPred = numPred;
	}
	
	/**
	 * Gets the total number of human predator players.
	 * 
	 * @return numPredPlayer (int)
	 */
	public int getNumPredPlayer() {
		return this.numPredPlayer;
	}

	/**
	 * Sets the total number of human predator players.
	 * 
	 * @param numPredPlayer (int)
	 */
	public void setNumPredPlayer(int numPredPlayer) {
		this.numPredPlayer = numPredPlayer;
	}
	
	/**
	 * Gets the maximum number of powerups a predator can have.
	 * 
	 * @return maxPredPowerUp (int)
	 */
	public int getMaxPredPowerUp() {
		return this.maxPredPowerUp;
	}
	
	/**
	 * Sets the maximum number of powerups a predator can have.
	 * 
	 * @param maxPredPowerUp (int)
	 */
	public void setMaxPredPowerUp(int maxPredPowerUp) {
		this.maxPredPowerUp = maxPredPowerUp;
	}
	
	/**
	 * Gets the total number of prey.
	 * 
	 * @return numPrey (int)
	 */
	public int getNumPrey() {
		return this.numPrey;
	}
	
	/**
	 * Sets the total number of prey.
	 * 
	 * @param numPrey (int)
	 */
	public void setNumPrey(int numPrey) {
		this.numPrey = numPrey;
	}
	
	/**
	 * Gets the total number of human prey players.
	 * 
	 * @return numPreyPlayer (int)
	 */
	public int getNumPreyPlayer() {
		return this.numPreyPlayer;
	}
	
	/**
	 * Sets the total number of human prey players.
	 * 
	 * @param numPreyPlayer (int)
	 */
	public void setNumPreyPlayer(int numPreyPlayer) {
		this.numPreyPlayer = numPreyPlayer;
	}
	
	/**
	 * Gets the maximum number of powerups a prey can have.
	 * 
	 * @return maxPreyPowerUp (int)
	 */
	public int getMaxPreyPowerUp() {
		return this.maxPreyPowerUp;
	}
	
	/**
	 * Sets the maximum number of powerups a prey can have.
	 * 
	 * @param maxPreyPowerUp (int)
	 */
	public void setMaxPreyPowerUp(int maxPreyPowerUp) {
		this.maxPreyPowerUp = maxPreyPowerUp;
	}

	public int getPredBaseSpeedIndex() {
		return predBaseSpeedIndex;
	}

	public void setPredBaseSpeedIndex(int predBaseSpeedIndex) {
		this.predBaseSpeedIndex = predBaseSpeedIndex;
	}
	
	public int getPreyBaseSpeedIndex() {
		return preyBaseSpeedIndex;
	}

	public void setPreyBaseSpeedIndex(int preyBaseSpeedIndex) {
		this.preyBaseSpeedIndex = preyBaseSpeedIndex;
	}
}
