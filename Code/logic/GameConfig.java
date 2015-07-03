package logic;

import geometry.PolygonShape;

/**
 * Represents the configuration of the game.
 * 
 * @author Martin Wong
 * @version 2015-05-19
 */
public class GameConfig {
	
	private PolygonShape dimensions;
	private boolean hasPills;
	private MazeConfig mConfig;
	private AgentConfig aConfig;
	private PowerConfig pConfig;
	
	/**
	 * Creates an instance of GameConfig.
	 * 
	 * @param dimensions (Rectangle)
	 * @param hasPills (boolean)
	 * @param mConfig (MazeConfig)
	 * @param aConfig (AgentConfig)
	 * @param pConfig (PowerConfig)
	 */
	public GameConfig(PolygonShape dimensions, boolean hasPills,
			MazeConfig mConfig, AgentConfig aConfig, PowerConfig pConfig) {
		
		this.dimensions = dimensions;
		this.hasPills = hasPills;
		this.mConfig = mConfig;
		this.aConfig = aConfig;
		this.pConfig = pConfig;
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
	 * Gets the agent configurations of the game.
	 * 
	 * @return aConfig (MazeConfig)
	 */
	public AgentConfig getAConfig() {
		return this.aConfig;
	}
	
	/**
	 * Sets the agent configurations of the game.
	 * 
	 * @param mConfig (MazeConfig)
	 */
	public void setAConfig(AgentConfig aConfig) {
		this.aConfig = aConfig;
	}
	
	/**
	 * Gets the power configurations of the game.
	 * 
	 * @return pConfig (PowerConfig)
	 */
	public PowerConfig getPConfig() {
		return this.pConfig;
	}
	
	/**
	 * Sets the power configurations of the game.
	 * 
	 * @param pConfig (PowerConfig)
	 */
	public void setPConfig(PowerConfig pConfig) {
		this.pConfig = pConfig;
	}
	
}
