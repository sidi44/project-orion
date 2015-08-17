package game;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import logic.GameConfiguration;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;

/**
 * Configuration class for predator prey game. This class contains the 
 * parameters used by the game. The parameters are divided into three groups:
 * Game, Physics and Renderer.
 * 
 * The configuration group of classes are intended to be read in by a JAXB XML
 * parser.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
@XmlRootElement(name = "Configuration")
public class Configuration {

	private GameConfiguration gameConfig;
	private PhysicsConfiguration physicsConfig;
	private RendererConfiguration rendererConfig;

	/**
	 * Default constructor. 
	 * 
	 * Parameters are set to their default values.
	 */
	public Configuration() {
		gameConfig = new GameConfiguration();
		physicsConfig = new PhysicsConfiguration();
		rendererConfig = new RendererConfiguration();
	}
	
	/**
	 * Get the Game configuration.
	 * 
	 * @return the Game configuration.
	 */
	public GameConfiguration getGameConfig() {
		return gameConfig;
	}

	/**
	 * Set the Game configuration.
	 * 
	 * @param gameConfig - the Game configuration to set.
	 */
	@XmlElement (name = "GameConfiguration")
	public void setGameConfig(GameConfiguration gameConfig) {
		this.gameConfig = gameConfig;
	}
	
	/**
	 * Get the Physics configuration.
	 * 
	 * @return the Physics configuration.
	 */
	public PhysicsConfiguration getPhysicsConfig() {
		return physicsConfig;
	}

	/**
	 * Set the Physics configuration.
	 * 
	 * @param physicsConfig - the Physics configuration to set.
	 */
	@XmlElement (name = "PhysicsConfiguration")
	public void setPhysicsConfig(PhysicsConfiguration physicsConfig) {
		this.physicsConfig = physicsConfig;
	}
	
	/**
	 * Get the Renderer configuration.
	 * 
	 * @return the Renderer configuration.
	 */
	public RendererConfiguration getRendererConfig() {
		return rendererConfig;
	}

	/**
	 * Set the Renderer configuration.
	 * 
	 * @param rendererConfig - the Renderer configuration to set.
	 */
	@XmlElement (name = "RendererConfiguration")
	public void setRendererConfig(RendererConfiguration rendererConfig) {
		this.rendererConfig = rendererConfig;
	}
	
}
