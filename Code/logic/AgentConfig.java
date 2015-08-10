package logic;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The configuration for the agents.
 * 
 * @author Martin Wong
 * @version 2015-08-09
 */
@XmlRootElement(name = "AgentConfiguration")
public class AgentConfig {
	
	private int numPred;
	private int numPredPlayer;
	private int maxPredPowerUp;
	private int numPrey;
	private int numPreyPlayer;
	private int maxPreyPowerUp;
	
	/**
	 * Default constructor for AgentConfig.
	 * 
	 * Sets parameters to their default values.
	 */
	public AgentConfig() {
		this.numPred = 1;
		this.numPredPlayer = 1;
		this.maxPredPowerUp = 5;
		this.numPrey = 5;
		this.numPredPlayer = 0;
		this.maxPreyPowerUp = 5;
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
			int numPrey, int numPreyPlayer, int maxPreyPowerUp) {
		
		this.numPred = numPred;
		this.numPredPlayer = numPredPlayer;
		this.maxPredPowerUp = maxPredPowerUp;
		this.numPrey = numPrey;
		this.numPreyPlayer = numPreyPlayer;
		this.maxPreyPowerUp = maxPreyPowerUp;
		
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
	@XmlElement (name = "NumPredator")
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
	@XmlElement (name = "NumPredatorPlayer")
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
	@XmlElement (name = "MaxPredatorPowerUp")
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
	@XmlElement (name = "NumPrey")
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
	@XmlElement (name = "NumPreyPlayer")
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
	@XmlElement (name = "MaxPreyPowerUp")
	public void setMaxPreyPowerUp(int maxPreyPowerUp) {
		this.maxPreyPowerUp = maxPreyPowerUp;
	}
	
	
}
