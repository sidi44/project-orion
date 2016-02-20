package data;

import logic.GameConfiguration;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;
import sound.SoundConfiguration;

public interface DataManager {

	RendererConfiguration getRendererConfig();
	
	PhysicsConfiguration getPhysicsConfig(int levelNumber);
	
	PhysicsConfiguration getPhysicsConfigSandbox();
	
	GameConfiguration getGameConfig(int levelNumber);
	
	GameConfiguration getGameConfigSandbox();
	
	void saveSandboxData(PhysicsConfiguration physicsConfig, 
			GameConfiguration gameConfig);
	
	SoundConfiguration getSoundConfiguration();
	
	void saveSoundData(SoundConfiguration soundData);
	
	PlayerProgress getPlayerProgress();
	
	void savePlayerProgress(PlayerProgress progress);
	
}
