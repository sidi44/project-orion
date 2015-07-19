package game;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import logic.GameConfiguration;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;

@XmlRootElement(name = "Configuration")
public class Configuration {

	private GameConfiguration gameConfig;
	private PhysicsConfiguration physicsConfig;
	private RendererConfiguration rendererConfig;

	/**
	 * @return the gameConfig
	 */
	public GameConfiguration getGameConfig() {
		return gameConfig;
	}

	/**
	 * @param gameConfig the gameConfig to set
	 */
	@XmlElement (name = "GameConfiguration")
	public void setGameConfig(GameConfiguration gameConfig) {
		this.gameConfig = gameConfig;
	}
	
	/**
	 * @return the physicsConfig
	 */
	public PhysicsConfiguration getPhysicsConfig() {
		return physicsConfig;
	}

	/**
	 * @param physicsConfig the physicsConfig to set
	 */
	@XmlElement (name = "PhysicsConfiguration")
	public void setPhysicsConfig(PhysicsConfiguration physicsConfig) {
		this.physicsConfig = physicsConfig;
	}
	
	/**
	 * @return the rendererConfig
	 */
	public RendererConfiguration getRendererConfig() {
		return rendererConfig;
	}

	/**
	 * @param rendererConfig the rendererConfig to set
	 */
	@XmlElement (name = "RendererConfiguration")
	public void setRendererConfig(RendererConfiguration rendererConfig) {
		this.rendererConfig = rendererConfig;
	}
	
}
