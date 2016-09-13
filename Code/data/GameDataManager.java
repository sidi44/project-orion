package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geometry.PointXY;
import geometry.PolygonShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import logic.AgentConfig;
import logic.GameConfiguration;
import logic.MazeConfig;
import logic.PowerUpConfig;
import logic.powerup.PowerUpType;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;
import sound.SoundConfiguration;

public class GameDataManager implements DataManager {

	// Variables used to store data that has been read in
	private SoundConfiguration defaultSoundConfig;
	private PlayerProgress playerProgress;
	private LevelsData levelsData;
	private SandboxConfiguration defaultSandboxConfig;
	private RendererConfiguration rendererConfig;
	private PhysicsConfiguration physicsConfig;
	
	// Variables used for reading / writing data from / to storage
	private Json json;
	private Preferences soundPrefs;
	private Preferences sandboxPrefs;
	
	// Preferences keys
	private final String SOUNDS_PREFS = "sounds";
	private final String SANDBOX_PREFS = "sandbox";
	
	// Sandbox preferences
	private final String PREF_SANDBOX_MAZE_WIDTH = "mazewidth";
	private final String PREF_SANDBOX_MAZE_HEIGHT = "mazeheight";
	private final String PREF_SANDBOX_NUM_PREY = "numprey";
	private final String PREF_SANDBOX_PREDATOR_SPEED = "predatorspeed";
	private final String PREF_SANDBOX_PREY_SPEED = "preyspeed";
	
	// Sound preferences
	private final String PREF_SOUND_PLAY_SOUNDS = "playsounds";
	private final String PREF_SOUND_PLAY_MUSIC = "playmusic";
	private final String PREF_SOUND_SOUND_LEVEL = "soundlevel";
	private final String PREF_SOUND_MUSIC_LEVEL = "musiclevel";
	
	
	public GameDataManager() {
		
		json = new Json();
		json.setUsePrototypes(false);
		
		readSoundConfig();
		readSandboxConfig();
		readPhysicsConfig();
		readRendererConfig();
		readLevelsConfig();
		readGameData();
	}
	
	private void readSoundConfig() {
		
		// Get a handle to the general config data file
		FileHandle handle = Gdx.files.internal("data/config/general_config.json");
		
		// Check whether there's a problem reading the file...
		if (!handle.exists()) {
			System.err.println("Couldn't read general configuration file.");
			defaultSoundConfig = new SoundConfiguration();
			return;
		}
		
		// Parse the json in the file into the default sound configuration
		defaultSoundConfig = getJson().fromJson(SoundConfiguration.class, handle);
	}
	
	private void readSandboxConfig() {
		
		// Get a handle to the sandbox config data file
		FileHandle handle = Gdx.files.internal("data/config/sandbox_config.json");
		
		// Check whether there's a problem reading the file...
		if (!handle.exists()) {
			System.err.println("Couldn't read sandbox configuration file.");
			defaultSandboxConfig = new SandboxConfiguration();
			return;
		}
		
		// Parse the json in the file into the default sandbox configuration
		defaultSandboxConfig = getJson().fromJson(SandboxConfiguration.class, handle);
	}
	
	private void readPhysicsConfig() {
		
		// Get a handle to the physics config data file
		FileHandle handle = Gdx.files.internal("data/config/physics_config.json");
		
		// Check whether there's a problem reading the file...
		if (!handle.exists()) {
			System.err.println("Couldn't read physics configuration file.");
			physicsConfig = new PhysicsConfiguration();
			return;
		}
		
		// Parse the json in the file into the physics configuration
		physicsConfig = getJson().fromJson(PhysicsConfiguration.class, handle);
	}
	
	private void readRendererConfig() {
		
		// Get a handle to the renderer config data file
		FileHandle handle = Gdx.files.internal("data/config/renderer_config.json");
		
		// Check whether there's a problem reading the file...
		if (!handle.exists()) {
			System.err.println("Couldn't read renderer configuration file.");
			rendererConfig = new RendererConfiguration();
			return;
		}
		
		// Parse the json in the file into the renderer configuration
		rendererConfig = getJson().fromJson(RendererConfiguration.class, handle);
	}
	
	private void readLevelsConfig() {
		
		// Get a handle to the levels config data file
		FileHandle handle = Gdx.files.internal("data/config/levels_config.json");
		
		// Check whether there's a problem reading the file...
		if (!handle.exists()) {
			System.err.println("Couldn't read levels configuration file.");
			levelsData = new LevelsData();
			return;
		}
		
		// Parse the json in the file into the levels data configuration
		levelsData = getJson().fromJson(LevelsData.class, handle);
	}
	
	private void readGameData() {
		
		// Get a handle to the game data file (Note this file is stored in 
		// 'local' storage not 'internal').
		FileHandle handle = Gdx.files.local("data/config/game_data.json");
		
		// Check whether the file exists. It's possible that this won't exist
		// if this is the first time the game is played.
		if (!handle.exists()) {
			// We couldn't find the game data file, so we assume it's a new 
			// game and load the initial game data.
			FileHandle handleInitial = Gdx.files.internal("data/config/initial_game_data.json");
			if (!handleInitial.exists()) {
				System.err.println("Couldn't read initial game data file.");
				playerProgress = new PlayerProgress();
			} else {
				// Parse the json in the file into the player progress class
				playerProgress = getJson().fromJson(PlayerProgress.class, handleInitial);
			}
		} else {
			// Parse the json in the file into the player progress class
			playerProgress = getJson().fromJson(PlayerProgress.class, handle);
		}
	}
	
	private Json getJson() {
		return json;
	}
	
	private Preferences getSoundPrefs() {
		if (soundPrefs == null) {
			soundPrefs = Gdx.app.getPreferences(SOUNDS_PREFS);
		}
		return soundPrefs;
	}
	
	private Preferences getSandboxPrefs() {
		if (sandboxPrefs == null) {
			sandboxPrefs = Gdx.app.getPreferences(SANDBOX_PREFS);
		}
		return sandboxPrefs;
	}
	
	@Override
	public RendererConfiguration getRendererConfig() {
		return rendererConfig;
	}

	@Override
	public PhysicsConfiguration getPhysicsConfig(int levelNumber) {
		
		// Copy the fixed values from our physics configuration
		PhysicsConfiguration config = new PhysicsConfiguration(physicsConfig);
		
		// Check that the level exists
		if (!levelsData.levelExists(levelNumber)) {
			System.err.println("No data for requested level number.");
			return config;
		}
		
		return config;
	}

	@Override
	public PhysicsConfiguration getPhysicsConfigSandbox() {
		
		// Copy the fixed values from our physics configuration
		PhysicsConfiguration config = new PhysicsConfiguration(physicsConfig);
		
		return config;
	}

	@Override
	public GameConfiguration getGameConfig(int levelNumber) {
		
		// Check that the level exists
		if (!levelsData.levelExists(levelNumber)) {
			System.err.println("No data for requested level number.");
			return new GameConfiguration();
		}
		
		// Get the maze shape for this level
		Level level = levelsData.getLevel(levelNumber);
		List<PointXY> mazePoints = level.getMazeDimensions();
		PolygonShape mazeShape = new PolygonShape(mazePoints);
		
		// We always want pills for levels
		boolean hasPills = true;
		
		// Use the default maze config for now
		MazeConfig mazeConfig = level.getMazeConfig();
		
		// Create the agent config (using mostly default values)
		// TODO Replace max power up values with data read from the levels config
		AgentConfig agentConfig = new AgentConfig();
		int numPrey = level.getNumPrey();
		int preySpeedIndex = level.getPreySpeedIndex();
		int predatorSpeedIndex = playerProgress.getPredatorSpeedIndex();
		agentConfig.setNumPrey(numPrey);
		agentConfig.setPredBaseSpeedIndex(predatorSpeedIndex);
		agentConfig.setPreyBaseSpeedIndex(preySpeedIndex);
		
		// Use the default power up config for now
		PowerUpConfig powerUpConfig = new PowerUpConfig();
		powerUpConfig.setNumPredPow(level.getNumPowerUps());
		List<PowerUpType> powerUpTypes = level.getPowerUpTypes();
		Map<PowerUpType, Integer> powerUpDefs = 
				new HashMap<PowerUpType, Integer>();
		for (PowerUpType type : powerUpTypes) {
			int strength = playerProgress.getPowerUpStrength(type);
			powerUpDefs.put(type, strength);
		}
		powerUpConfig.setPredatorPowerUps(powerUpDefs);
		
		// Create our game configuration
		GameConfiguration gameConfig = new GameConfiguration(mazeShape, 
				hasPills, mazeConfig, agentConfig, powerUpConfig);
		
		// We're done
		return gameConfig;
	}

	@Override
	public GameConfiguration getGameConfigSandbox() {
		
		// Get the saved preferences
		Preferences prefs = getSandboxPrefs();
		
		// Now get the required values from the sandbox configuration, using the 
		// default value if none exists in the preferences
		int defaultMazeWidth = defaultSandboxConfig.getMazeWidth();
		int mazeWidth = prefs.getInteger(PREF_SANDBOX_MAZE_WIDTH, defaultMazeWidth);
		
		int defaultMazeHeight = defaultSandboxConfig.getMazeHeight();
		int mazeHeight = prefs.getInteger(PREF_SANDBOX_MAZE_HEIGHT, defaultMazeHeight);
		
		List<PointXY> mazePoints = new ArrayList<PointXY>();
		mazePoints.add(new PointXY(0, 0));
		mazePoints.add(new PointXY(0, mazeHeight));
		mazePoints.add(new PointXY(mazeWidth, mazeHeight));
		mazePoints.add(new PointXY(mazeWidth, 0));
		
		PolygonShape mazeShape = new PolygonShape(mazePoints);
		
		// We always want pills for sandbox
		boolean hasPills = true;
		
		// Create a default maze config, then ask for the parameters to be 
		// randomised. We don't allow these parameters to be set by the GUI, so
		// use random values to get some varied mazes.
		MazeConfig mazeConfig = new MazeConfig();
		mazeConfig.randomiseValues();
		
		// Create the agent config (using mostly default values)
		// TODO Replace max power up values with data read from the sandbox config
		AgentConfig agentConfig = new AgentConfig();
		int defaultNumPrey = defaultSandboxConfig.getNumPrey();
		int numPrey = prefs.getInteger(PREF_SANDBOX_NUM_PREY, defaultNumPrey);
		agentConfig.setNumPrey(numPrey);
		
		int defaultPredatorSpeedIndex = 
				defaultSandboxConfig.getPredatorSpeedIndex();
		int predatorSpeedIndex = 
				prefs.getInteger(PREF_SANDBOX_PREDATOR_SPEED, defaultPredatorSpeedIndex);
		agentConfig.setPredBaseSpeedIndex(predatorSpeedIndex);
		
		int defaultPreySpeedIndex = defaultSandboxConfig.getPreySpeedIndex();
		int preySpeedIndex = 
				prefs.getInteger(PREF_SANDBOX_PREY_SPEED, defaultPreySpeedIndex);
		agentConfig.setPreyBaseSpeedIndex(preySpeedIndex);
		
		// Use the default power up config for now
		// TODO Read in the correct power up strengths from the sandbox config
		PowerUpConfig powerUpConfig = new PowerUpConfig();
		
		// Create our game configuration
		GameConfiguration gameConfig = new GameConfiguration(mazeShape, 
				hasPills, mazeConfig, agentConfig, powerUpConfig);
		
		// We're done
		return gameConfig;
	}

	@Override
	public SandboxConfiguration getSandboxConfig() {
		
		// Get the saved preferences
		Preferences prefs = getSandboxPrefs();
		
		int defaultPredatorSpeed = defaultSandboxConfig.getPredatorSpeedIndex();
		int predatorSpeed = prefs.getInteger(PREF_SANDBOX_PREDATOR_SPEED, defaultPredatorSpeed);
		
		int defaultPreySpeed = defaultSandboxConfig.getPreySpeedIndex();
		int preySpeed = prefs.getInteger(PREF_SANDBOX_PREY_SPEED, defaultPreySpeed);
		
		int defaultMazeWidth = defaultSandboxConfig.getMazeWidth();
		int mazeWidth = prefs.getInteger(PREF_SANDBOX_MAZE_WIDTH, defaultMazeWidth);
		
		int defaultMazeHeight = defaultSandboxConfig.getMazeHeight();
		int mazeHeight = prefs.getInteger(PREF_SANDBOX_MAZE_HEIGHT, defaultMazeHeight);
		
		int defaultNumPrey = defaultSandboxConfig.getNumPrey();
		int numPrey = prefs.getInteger(PREF_SANDBOX_NUM_PREY, defaultNumPrey);
		
		
		SandboxConfiguration sandboxConfig = 
				new SandboxConfiguration(predatorSpeed, preySpeed, mazeWidth, 
						mazeHeight, numPrey);
		
		return sandboxConfig;
	}
	
	@Override
	public void saveSandboxData(SandboxConfiguration sandboxConfig) {
		
		// We use Preferences to store the sandbox data
		Preferences prefs = getSandboxPrefs();
		prefs.putInteger(PREF_SANDBOX_PREDATOR_SPEED, sandboxConfig.getPredatorSpeedIndex());
		prefs.putInteger(PREF_SANDBOX_PREY_SPEED, sandboxConfig.getPreySpeedIndex());
		prefs.putInteger(PREF_SANDBOX_MAZE_WIDTH, sandboxConfig.getMazeWidth());
		prefs.putInteger(PREF_SANDBOX_MAZE_HEIGHT, sandboxConfig.getMazeHeight());
		prefs.putInteger(PREF_SANDBOX_NUM_PREY, sandboxConfig.getNumPrey());
		
		prefs.flush();
		
		// TODO save other data (see above method)
	}

	@Override
	public SoundConfiguration getSoundConfiguration() {
		
		SoundConfiguration soundConfig = new SoundConfiguration();
		
		Preferences prefs = getSoundPrefs();
		boolean playSounds = 
				prefs.getBoolean(PREF_SOUND_PLAY_SOUNDS, defaultSoundConfig.playSounds());
		boolean playMusic = 
				prefs.getBoolean(PREF_SOUND_PLAY_MUSIC, defaultSoundConfig.playMusic());
		int soundLevel = 
				prefs.getInteger(PREF_SOUND_SOUND_LEVEL, defaultSoundConfig.getSoundLevel());
		int musicLevel = 
				prefs.getInteger(PREF_SOUND_MUSIC_LEVEL, defaultSoundConfig.getMusicLevel());
		
		soundConfig.setPlaySounds(playSounds);
		soundConfig.setPlayMusic(playMusic);
		soundConfig.setSoundLevel(soundLevel);
		soundConfig.setMusicLevel(musicLevel);
		
		return soundConfig;
	}

	@Override
	public void saveSoundData(SoundConfiguration soundData) {
		// We use Preferences to store the sound data
		Preferences prefs = getSoundPrefs();
		prefs.putBoolean(PREF_SOUND_PLAY_SOUNDS, soundData.playSounds());
		prefs.putBoolean(PREF_SOUND_PLAY_MUSIC, soundData.playMusic());
		prefs.putInteger(PREF_SOUND_SOUND_LEVEL, soundData.getSoundLevel());
		prefs.putInteger(PREF_SOUND_MUSIC_LEVEL, soundData.getMusicLevel());
		prefs.flush();
	}

	@Override
	public PlayerProgress getPlayerProgress() {
		return playerProgress;
	}

	@Override
	public void savePlayerProgress() {
		
		// Get a handle to the player progress data file
		FileHandle handle = Gdx.files.local("data/config/game_data.json");
		
		// Convert the progress to a Json string
		String progressString = getJson().toJson(playerProgress);
		
		// Write the string to the file
		boolean append = false;
		handle.writeString(progressString, append);
	}

}
