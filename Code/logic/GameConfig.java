package logic;

import java.util.List;

import geometry.PolygonShape;

/**
 * Represents the configuration of the game.
 * 
 * @author Martin Wong
 * @version 2015-05-19
 */
public class GameConfig {
	
	private PolygonShape dimensions;
	private MazeConfig mConfig;
	private int numPred;
	private int numPredPlayer;
	private int numPrey;
	private int numPreyPlayer;
	private boolean hasPills;
	private List<Powerup> powerups;
	
	/**
	 * Creates an instance of GameConfig.
	 * 
	 * @param dimensions (Rectangle)
	 * @param mConfig (MazeConfig)
	 * @param numPred (int)
	 * @param numPredPlayer (int)
	 * @param numPrey (int)
	 * @param numPreyPlayer (int)
	 * @param hasPills (boolean)
	 * @param powerups (List<Powerup>)
	 */
	public GameConfig(PolygonShape dimensions, MazeConfig mConfig, int numPred,
			int numPredPlayer, int numPrey, int numPreyPlayer,
			boolean hasPills, List<Powerup> powerups) {
		this.dimensions = dimensions;
		this.mConfig = mConfig;
		this.numPred = numPred;
		this.numPredPlayer = numPredPlayer;
		this.numPrey = numPrey;
		this.numPreyPlayer = numPreyPlayer;
		this.hasPills = hasPills;
		this.powerups = powerups;
		
		try {
			if ((this.numPred < this.numPredPlayer) || (this.numPrey < this.numPreyPlayer)){
				throw new Exception("Illegal Game Configuration: the predator / prey configurations are incorrect.");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * Gets the dimensions of the game.
	 * 
	 * @return dimensions (Rectangle)
	 */
	public PolygonShape getDimensions() {
		return this.dimensions;
	}
	
	/**
	 * Sets the dimensions of the game.
	 * 
	 * @param dimensions (Rectangle)
	 */
	public void setDimensions(PolygonShape dimensions) {
		this.dimensions = dimensions;
	}
	
	/**
	 * Gets the maze configurations of the game.
	 * 
	 * @return mConfig (MazeConfig)
	 */
	public MazeConfig getMConfig() {
		return this.mConfig;
	}
	
	/**
	 * Sets the maze configurations of the game.
	 * 
	 * @param mConfig (MazeConfig)
	 */
	public void setMConfig(MazeConfig mConfig) {
		this.mConfig = mConfig;
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
	
	/**
	 * Checks to find whether maze should have pills
	 * (true = has pills, false = has no pills)
	 * 
	 * @return hasPills (boolean)
	 */
	public boolean getHasPills() {
		return this.hasPills;
	}
	
	/**
	 * Switching on and off pills for maze.
	 * 
	 * @param hasPills (boolean)
	 */
	public void setHasPills(boolean hasPills) {
		this.hasPills = hasPills;
	}
	
	/**
	 * Gets all the powerups.
	 * 
	 * @return powerups (List<Powerup>)
	 */
	public List<Powerup> getPowerups() {
		return this.powerups;
	}
	
	/**
	 * Sets the powerups.
	 * 
	 * @param powerups (List<Powerup>)
	 */
	public void setPTypes(List<Powerup> powerups) {
		this.powerups = powerups;
	}
	
	
}
