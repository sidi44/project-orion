package game;

import java.util.List;

import ai.AILogic;
import data.DataManager;
import data.GameDataManager;
import data.PlayerProgress;
import geometry.PointXY;
import geometry.PolygonShape;
import logic.Agent;
import logic.GameConfiguration;
import logic.GameLogic;
import logic.GameOver;
import logic.GameState;
import logic.Move;
import logic.Predator;
import logic.Prey;
import physics.PhysicsConfiguration;
import physics.PhysicsDebugType;
import physics.PhysicsGameWorld;
import physics.PhysicsProcessor;
import physics.PhysicsProcessorBox2D;
import render.Renderer;
import render.RendererConfiguration;
import ui.ScreenManager;
import ui.ScreenName;
import sound.SoundConfiguration;
import sound.SoundManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;

public class PredatorPreyGame extends Game implements GameStatus {

	private GameLogic gameLogic;
	private Renderer renderer;
	private PhysicsProcessor physProc;
	private SoundManager soundManager;
	
	private GameConfiguration gameConfig;
	private PhysicsConfiguration physicsConfig;
	private RendererConfiguration rendererConfig;
	
	private ResultLogger logger;
	
	private ScreenManager screenManager;
	private DataManager dataManager;
	
	private GameType gameType;
	private int currentLevel;
	
	// Physics debug information
	private final PhysicsDebugType debugType = PhysicsDebugType.DebugNone;
	
	@Override
	public void create() {
		
		gameType = GameType.NotPlaying;
		currentLevel = -1;
		
		dataManager = new GameDataManager();
		rendererConfig = dataManager.getRendererConfig();
		
		// Create dummy game and physics configuration class. These will be 
		// replaced for the actual level / sandbox versions via the UI.
		gameConfig = new GameConfiguration();
		physicsConfig = new PhysicsConfiguration();
		
		gameLogic = new GameLogic(gameConfig);

		physProc = new PhysicsProcessorBox2D(gameLogic.getGameState(), 
				physicsConfig);
		physProc.setDebugCategory(debugType);
		
		renderer = new Renderer(false, false);
		renderer.loadTextures(rendererConfig);

		SoundConfiguration soundConfig = dataManager.getSoundConfiguration();
		soundManager = new SoundManager(soundConfig, this);
		physProc.addReceiver(soundManager);
		
		screenManager = new ScreenManager(this);
		screenManager.changeScreen(ScreenName.Splash);
		screenManager.addReceiver(soundManager);
		
		logger = new ResultLogger();
	}

	public void setAI(AILogic ai) {
		gameLogic.setAILogic(ai);
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public GameLogic getGameLogic() {
		return gameLogic;
	}
	
	public PhysicsGameWorld getWorld() {
		return physProc.getWorld();
	}
	
	public PhysicsProcessor getPhysicsProcessor() {
		return physProc;
	}
	
	public DataManager getDataManager() {
		return dataManager;
	}
	
	public ResultLogger getLogger() {
		return logger;
	}
	
	public void resetLogger() {
		logger.reset();
	}
	
	public void addResult(GameResult result) {
		logger.addResult(result);
	}
	
	public void resetGame() {
		gameLogic = new GameLogic(gameConfig);
		physProc = new PhysicsProcessorBox2D(gameLogic.getGameState(), 
				physicsConfig);
		physProc.setDebugCategory(debugType);
		physProc.addReceiver(soundManager);
	}
	
	public Vector2[] getWorldMazeBoundaries() {
		PolygonShape pShape = gameLogic.getGameState().getMaze().getDimensions();
		Vector2 mazeLL = physProc.stateToWorld(new PointXY(pShape.getMinX() - 1, pShape.getMinY() - 1));
		Vector2 mazeUR = physProc.stateToWorld(new PointXY(pShape.getMaxX() + 1, pShape.getMaxY() + 1));
		
		Vector2[] mazeBoundaries = new Vector2[] {mazeLL, mazeUR};
		
		return mazeBoundaries;
	}
	
	public GameOver update(float delta, Move move) {
		
		processMoves(move);

		GameState state = gameLogic.getGameState();
		physProc.stepSimulation(delta, state);
		
		return gameLogic.isGameOver(1);
	}
	
	private void processMoves(Move move) {

		// Do the player moves.
		List<Agent> players = gameLogic.getAllPlayers();
		for (Agent a : players) {
			int id = a.getID();
			if (a instanceof Predator) {
				gameLogic.setPredNextMove(id, move);
			} else if (a instanceof Prey) {
				gameLogic.setPreyNextMove(id, move);
			}
		}

		gameLogic.setNonPlayerMoves();
	}
	
	public void setGameTypeLevel(int levelNumber) {
		gameType = GameType.Levels;
		currentLevel = levelNumber;
		gameConfig = dataManager.getGameConfig(levelNumber);
		physicsConfig = dataManager.getPhysicsConfig(levelNumber);
		resetGame();
	}
	
	public void setGameTypeSandbox() {
		gameType = GameType.Sandbox;
		currentLevel = -1;
		gameConfig = dataManager.getGameConfigSandbox();
		physicsConfig = dataManager.getPhysicsConfigSandbox();
		resetGame();
	}
	
	public void gameOver(GameOver reason) {
		
		// Check whether the player was playing a in level mode and whether 
		// they won.
		if (gameType == GameType.Levels && reason == GameOver.Prey) {
			
			// The player completed the level. Update their progress
			PlayerProgress progress = dataManager.getPlayerProgress();
			
			// Unlock the next level
			progress.setLevelLocked(currentLevel + 1, false);
			
			// TODO Save the score here as well
			
			
			// Save the player progress
			dataManager.savePlayerProgress();
		}
		
		// Work out which screen we should change to depending on the game type
		GameType type = getGameType();
		ScreenName name = ScreenName.MainMenu;
		switch (type) {
			case Levels:
				name = ScreenName.Levels;
				break;
			case Sandbox:
				name = ScreenName.Sandbox;
				break;
			case NotPlaying:
			default:
				// We're leaving a game but not in a game mode. This is strange.
				System.err.println("How did we get here?");
				break;			
		}
		
		// We're no longer playing a game (i.e. back in the menu screens)
		gameType = GameType.NotPlaying;
		currentLevel = -1;
		
		// Change the screen now
		screenManager.changeScreen(name);
	}
	
	public void updateSoundManager() {
		SoundConfiguration config = getDataManager().getSoundConfiguration();
		soundManager.update(config);
	}

	@Override
	public GameType getGameType() {
		return gameType;
	}
	
	@Override
	public int getLevelNumber() {
		return currentLevel;
	}
}
