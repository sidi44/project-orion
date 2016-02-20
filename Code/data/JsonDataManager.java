package data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import logic.GameConfiguration;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;
import sound.SoundConfiguration;

public class JsonDataManager implements DataManager {

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
	
	
	public JsonDataManager() {
		
		json = new Json();
		
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
		
		// Now set the prey speed and square size from the level data, depending
		// on the provided level number
		Level level = levelsData.getLevel(levelNumber);
		config.setPreySpeed(level.getPreySpeed());
		config.setSquareSize(level.getSquareSize());
		
		// Finally set the predator speed from the player data
		config.setPredatorSpeed(playerProgress.getPredatorSpeed());
		
		return config;
	}

	@Override
	public PhysicsConfiguration getPhysicsConfigSandbox() {
		
		// Copy the fixed values from our physics configuration
		PhysicsConfiguration config = new PhysicsConfiguration(physicsConfig);
		
		// Get the saved preferences
		Preferences prefs = getSandboxPrefs();
		
		// Now set the other values from the sandbox configuration, using the 
		// default value if none exists in the preferences
		int defaultPredatorSpeed = defaultSandboxConfig.getPredatorSpeed();
		int predatorSpeed = prefs.getInteger("predatorspeed", defaultPredatorSpeed);
		
		int defaultPreySpeed = defaultSandboxConfig.getPreySpeed();
		int preySpeed = prefs.getInteger("preyspeed", defaultPreySpeed);
		
		config.setPredatorSpeed(predatorSpeed);
		config.setPreySpeed(preySpeed);
		config.setSquareSize(8.0f);
		// NOTE: NEED TO SORT OUT predator/prey speed conversions and square 
		// sizes
		
		return config;
	}

	@Override
	public GameConfiguration getGameConfig(int levelNumber) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameConfiguration getGameConfigSandbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSandboxData(PhysicsConfiguration physicsConfig,
			GameConfiguration gameConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public SoundConfiguration getSoundConfiguration() {
		
		SoundConfiguration soundConfig = new SoundConfiguration();
		
		Preferences prefs = getSoundPrefs();
		boolean playSounds = 
				prefs.getBoolean("playsounds", defaultSoundConfig.playSounds());
		boolean playMusic = 
				prefs.getBoolean("playmusic", defaultSoundConfig.playMusic());
		int soundLevel = 
				prefs.getInteger("soundlevel", defaultSoundConfig.getSoundLevel());
		int musicLevel = 
				prefs.getInteger("musiclevel", defaultSoundConfig.getMusicLevel());
		
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
		prefs.putBoolean("playsounds", soundData.playSounds());
		prefs.putBoolean("playmusic", soundData.playMusic());
		prefs.putInteger("soundlevel", soundData.getSoundLevel());
		prefs.putInteger("musiclevel", soundData.getMusicLevel());
		prefs.flush();
	}

	@Override
	public PlayerProgress getPlayerProgress() {
		return playerProgress;
	}

	@Override
	public void savePlayerProgress(PlayerProgress progress) {
		
		// Get a handle to the player progress data file
		FileHandle handle = Gdx.files.internal("data/config/player_progress.json");
		
		// Check whether there's a problem with the file...
		if (!handle.exists()) {
			System.err.println("Couldn't get the player progress file.");
			return;
		}
		
		// Convert the progress to a Json string
		String progressString = getJson().toJson(progress);
		
		// Write the string to the file
		handle.writeString(progressString, false);
		
	}

}
