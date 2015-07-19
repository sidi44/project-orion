package logic;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The configuration for the agents.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
@XmlRootElement(name = "AgentConfiguration")
public class AgentConfig {
	
	private int numPred;
	private int numPredPlayer;
	private int numPrey;
	private int numPreyPlayer;
	
	/**
	 * Default constructor for AgentConfig.
	 */
	public AgentConfig() {
		this.numPred = 1;
		this.numPredPlayer = 1;
		this.numPrey = 5;
		this.numPredPlayer = 0;
	}
	
	/**
	 * Creates an instance of AgentConfig.
	 * 
	 * @param numPred (int)
	 * @param numPredPlayer (int)
	 * @param numPrey (int)
	 * @param numPreyPlayer (int)
	 */
	public AgentConfig(int numPred, int numPredPlayer, int numPrey,
			int numPreyPlayer) {
		
		this.numPred = numPred;
		this.numPredPlayer = numPredPlayer;
		this.numPrey = numPrey;
		this.numPreyPlayer = numPreyPlayer;
		
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
	
}
