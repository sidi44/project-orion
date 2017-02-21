package data;

import java.util.List;

import logic.GameConfiguration;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;
import sound.SoundConfiguration;
import ui.FontConfiguration;

public interface DataManager {

	RendererConfiguration getRendererConfig();
	
	PhysicsConfiguration getPhysicsConfig(int levelNumber);
	
	PhysicsConfiguration getPhysicsConfigSandbox();
	
	PhysicsConfiguration getPhysicsConfigMainMenu();
	
	GameConfiguration getGameConfig(int levelNumber);
	
	GameConfiguration getGameConfigSandbox();
	
	GameConfiguration getGameConfigMainMenu();
	
	SandboxConfiguration getSandboxConfig();
	
	FontConfiguration getFontConfig();
	
	void saveSandboxData(SandboxConfiguration sandboxConfig);
	
	SoundConfiguration getSoundConfiguration();
	
	void saveSoundData(SoundConfiguration soundData);
	
	PlayerProgress getPlayerProgress();
	
	void savePlayerProgress();
	
	List<Integer> getLevelStarScores(int levelNumber);
	
}
