package logic;

/**
 * The configuration for the agents.
 * 
 * @author Martin Wong
 * @version 2015-06-19
 */
public class AgentConfig {
	private int numPred;
	private int numPredPlayer;
	private int numPrey;
	private int numPreyPlayer;
	
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
	
}
