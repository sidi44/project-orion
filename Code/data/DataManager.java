package data;

import java.util.List;

import logic.GameConfiguration;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;
import sound.SoundConfiguration;

public interface DataManager {

	RendererConfiguration getRendererConfig();
	
	PhysicsConfiguration getPhysicsConfig(int levelNumber);
	
	PhysicsConfiguration getPhysicsConfigSandbox();
	
	PhysicsConfiguration getPhysicsConfigMainMenu();
	
	GameConfiguration getGameConfig(int levelNumber);
	
	GameConfiguration getGameConfigSandbox();
	
	GameConfiguration getGameConfigMainMenu();
	
	SandboxConfiguration getSandboxConfig();
	
	void saveSandboxData(SandboxConfiguration sandboxConfig);
	
	SoundConfiguration getSoundConfiguration();
	
	void saveSoundData(SoundConfiguration soundData);
	
	PlayerProgress getPlayerProgress();
	
	void savePlayerProgress();
	
	List<Integer> getLevelStarScores(int levelNumber);
	
}
