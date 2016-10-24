package logic;

import java.util.ArrayList;
import java.util.List;

import geometry.PointXY;
import geometry.PolygonShape;

/**
 * Represents the configuration of the game.
 * 
 * @author Martin Wong
 * @version 2015-08-18
 */
public class GameConfiguration {
	
	private PolygonShape dimensions;
	private boolean hasPills;
	private int timeLimit;
	private MazeConfig mConfig;
	private AgentConfig aConfig;
	private PowerUpConfig pConfig;
	
	/**
	 * Default constructor for GameConfiguration.
	 * 
	 * Sets parameters to their default values.
	 */
	public GameConfiguration() {
		
		PointXY bottomLeft = new PointXY(0, 0);
		PointXY topLeft = new PointXY(0, 10);
		PointXY topRight = new PointXY(10, 10);
		PointXY bottomRight = new PointXY(10, 0);
		List<PointXY> vertices = new ArrayList<PointXY>();
		vertices.add(bottomLeft);
		vertices.add(topLeft);
		vertices.add(topRight);
		vertices.add(bottomRight);
		this.dimensions = new PolygonShape(vertices);
		
		this.hasPills = true;
		this.timeLimit = 200;
		this.mConfig = new MazeConfig();
		this.aConfig = new AgentConfig();
		this.pConfig = new PowerUpConfig();
	}
	
	/**
	 * Creates an instance of GameConfig.
	 * 
	 * @param dimensions (Rectangle)
	 * @param hasPills (boolean)
	 * @param timeLimit (int)
	 * @param mConfig (MazeConfig)
	 * @param aConfig (AgentConfig)
	 * @param pConfig (PowerUpConfig)
	 */
	public GameConfiguration(PolygonShape dimensions, boolean hasPills,
			int timeLimit, MazeConfig mConfig, AgentConfig aConfig, 
			PowerUpConfig pConfig) {
		
		this.dimensions = dimensions;
		this.hasPills = hasPills;
		this.timeLimit = timeLimit;
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
	 * Get the time limit.
	 * 
	 * @return the time limit for this game.
	 */
	public int getTimeLimit() {
		return timeLimit;
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
	 * Gets the powerup configurations of the game.
	 * 
	 * @return pConfig (PowerConfig)
	 */
	public PowerUpConfig getPConfig() {
		return this.pConfig;
	}
	
	/**
	 * Sets the powerup configurations of the game.
	 * 
	 * @param pConfig (PowerUpConfig)
	 */
	public void setPConfig(PowerUpConfig pConfig) {
		this.pConfig = pConfig;
	}
	
}
