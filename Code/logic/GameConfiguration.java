package logic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import xml.PolygonShapeAdapter;
import geometry.PointXY;
import geometry.PolygonShape;

/**
 * Represents the configuration of the game.
 * 
 * @author Martin Wong
 * @version 2015-08-13
 */
@XmlRootElement(name = "GameConfiguration")
public class GameConfiguration {
	
	private PolygonShape dimensions;
	private boolean hasPills;
	private MazeConfig mConfig;
	private AgentConfig aConfig;
	private PowerUpConfig pConfig;
	private PowerUpTypeConfig pTConfig;
	
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
		this.mConfig = new MazeConfig();
		this.aConfig = new AgentConfig();
		this.pConfig = new PowerUpConfig();
		this.pTConfig = new PowerUpTypeConfig();
	}
	
	/**
	 * Creates an instance of GameConfig.
	 * 
	 * @param dimensions (Rectangle)
	 * @param hasPills (boolean)
	 * @param mConfig (MazeConfig)
	 * @param aConfig (AgentConfig)
	 * @param pConfig (PowerUpConfig)
	 * @param pTConfig (PowerUpTypeConfig)
	 */
	public GameConfiguration(PolygonShape dimensions, boolean hasPills,
			MazeConfig mConfig, AgentConfig aConfig, PowerUpConfig pConfig,
			PowerUpTypeConfig pTConfig) {
		
		this.dimensions = dimensions;
		this.hasPills = hasPills;
		this.mConfig = mConfig;
		this.aConfig = aConfig;
		this.pConfig = pConfig;
		this.pTConfig = pTConfig;
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
	@XmlElement (name = "PolygonShape")
	@XmlJavaTypeAdapter(PolygonShapeAdapter.class)
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
	@XmlElement (name = "HasPills")
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
	@XmlElement (name = "MazeConfiguration")
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
	@XmlElement (name = "AgentConfiguration")
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
	@XmlElement (name = "PowerUpConfiguration")
	public void setPConfig(PowerUpConfig pConfig) {
		this.pConfig = pConfig;
	}
	
	/**
	 * Gets the powerup type configurations of the game.
	 * 
	 * @return pTConfig (PowerUpTypeConfig)
	 */
	public PowerUpTypeConfig getPTConfig() {
		return this.pTConfig;
	}
	
	/**
	 * Sets the powerup type configurations of the game.
	 * 
	 * @param pTConfig (PowerUpTypeConfig)
	 */
	public void setPConfig(PowerUpTypeConfig pTConfig) {
		this.pTConfig = pTConfig;
	}
}
