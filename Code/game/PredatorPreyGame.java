package game;

import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

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
import progress.ProgressTask;
import render.Renderer;
import render.RendererConfiguration;
import sound.SoundConfiguration;
import sound.SoundManager;
import ui.FontConfiguration;
import ui.ScreenManager;
import ui.ScreenName;

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
	private AssetManager assetManager;
	
	private GameType gameType;
	private int currentLevel;
	private GameOver gameOverReason;

	// Physics debug information
	private final PhysicsDebugType debugType = PhysicsDebugType.DebugNone;
	
	@Override
	public void create() {
		
	    dataManager = new GameDataManager();

	    assetManager = new AssetManager();
        prepareAssetsForLoading();
	    
		gameType = GameType.NotPlaying;
		currentLevel = -1;
		
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
	
	public AssetManager getAssetManager() {
	    return assetManager;
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

	    gameOverReason = GameOver.No;

		// Calculate the size of the maze in world coordinates. Use this for
		// the background image size.
		float squareSize = physicsConfig.getSquareSize();
		PolygonShape shape = gameConfig.getDimensions();
		float width = (shape.getMaxX() - shape.getMinX() + 1) * squareSize;
		float height = (shape.getMaxY() - shape.getMinY() + 1) * squareSize;
		renderer.setBackgroundSize(new Vector2(width, height));
		
		// Rebuild the back end game logic and physics processor from the 
		// configurations.
		gameLogic = new GameLogic(gameConfig);
		physProc = new PhysicsProcessorBox2D(gameLogic.getGameState(), 
				physicsConfig);
		physProc.setDebugCategory(debugType);
		physProc.addReceiver(soundManager);
	}
	
	/**
	 * Returns the coordinates of the lower left corner of the maze in world
	 * coordinates (i.e. the minimum x-y coordinates of the maze)
	 * 
	 * @return the coordinates of the lower left corner of the maze.
	 */
	public Vector2 getMazeMinimumPointWorld() {
		
		// Get the minimum (lower left) point of the maze and convert to world 
		// coordinates
		PolygonShape dims = gameLogic.getGameState().getMaze().getDimensions();
		PointXY minPoint = new PointXY(dims.getMinX(), dims.getMinY());
		Vector2 mazeLL = physProc.stateToWorld(minPoint);
		
		// The above point is at the centre of a maze square, so we need to
		// correct for that to get the true lower left point
		float halfSquareSize = physProc.getSquareSize() / 2;
		mazeLL.sub(halfSquareSize, halfSquareSize);
		
		return mazeLL;
	}
	
	/**
	 * Returns the coordinates of the upper right corner of the maze in world
	 * coordinates (i.e. the maximum x-y coordinates of the maze).
	 * 
	 * @return the coordinates of the upper right corner of the maze.
	 */
	public Vector2 getMazeMaximumPointWorld() {
		
		// Get the maximum (upper right) point of the maze and convert to world 
		// coordinates
		PolygonShape dims = gameLogic.getGameState().getMaze().getDimensions();
		PointXY maxPoint = new PointXY(dims.getMaxX(), dims.getMaxY());
		Vector2 mazeUR = physProc.stateToWorld(maxPoint);
		
		// The above point is at the centre of a maze square, so we need to
		// correct for that to get the true upper right point
		float halfSquareSize = physProc.getSquareSize() / 2;
		mazeUR.add(halfSquareSize, halfSquareSize);
		
		return mazeUR;
	}
	
	public GameOver update(float delta, Move move) {
		
		processMoves(move);

		GameState state = gameLogic.getGameState();
		physProc.stepSimulation(delta, state);
		
		state.decreaseTimeRemaining(delta);
		
		if (move.getForceGameOver() != GameOver.No) {
			return move.getForceGameOver();
		} else {
			return gameLogic.isGameOver();
		}
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
		renderer.setDrawBackground(true);
		resetGame();
	}
	
	public void setGameTypeSandbox() {
		gameType = GameType.Sandbox;
		currentLevel = -1;
		gameConfig = dataManager.getGameConfigSandbox();
		physicsConfig = dataManager.getPhysicsConfigSandbox();
		renderer.setDrawBackground(true);
		resetGame();
	}
	
	public void setGameTypeMainMenu() {
		gameType = GameType.MainMenu;
		currentLevel = -1;
		gameConfig = dataManager.getGameConfigMainMenu();
		physicsConfig = dataManager.getPhysicsConfigMainMenu();
		renderer.setDrawBackground(false);
		resetGame();
	}
	
	public void gameOver(GameOver reason) {

	    gameOverReason = reason;

		// Check whether the player was playing a in level mode and whether
		// they won.
		if (gameType == GameType.Levels && reason == GameOver.Prey) {
			
			// The player completed the level. Update their progress
			PlayerProgress progress = dataManager.getPlayerProgress();
			
			// Unlock the next level
			progress.setLevelLocked(currentLevel + 1, false);
			
			// Save the score
			GameState state = gameLogic.getGameState();
			int score = state.getScore();
			progress.setLevelScore(currentLevel, score);
			
			// Save the player progress
			dataManager.savePlayerProgress();
		} else if (gameType == GameType.MainMenu) {
			// We don't want to change screen or game type, so just reset 
			// the game and get out of here
			setGameTypeMainMenu();
			return;
		}
		
		// Change the screen now
		screenManager.changeScreen(ScreenName.Pause);
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
	
	public List<ProgressTask> getLoadingTasks() {
		return gameLogic.getProgressTasks();
	}
	
	public GameOver getGameOverReason() {
	    return gameOverReason;
	}

	
	private void prepareAssetsForLoading() {
	    
	    FontConfiguration fontConfig = getDataManager().getFontConfig();

	    String fontFilePath = fontConfig.getFontFilePath();
	    String fontName = fontConfig.getFontName();

	    String skinAtlasFilePath = "data/ui/uiskin.atlas";
	    String skinJsonFilePath = "data/ui/uiskin.json";
	    
	    
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFilePath));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        parameter.size = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 25;
        
        parameter.borderWidth = fontConfig.getFontBorderWidth();
        parameter.color = fontConfig.getFontColour();
        
        BitmapFont droidSerifBoldFont = generator.generateFont(parameter);
        
        /* Create the ObjectMap and add the fonts to it */
        ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
        fontMap.put(fontName, droidSerifBoldFont);

        /* Create the SkinParameter and supply the ObjectMap to it */
        SkinParameter skinParameter = new SkinParameter(skinAtlasFilePath, fontMap);

        /* Load the skin as usual */
        assetManager.load(skinJsonFilePath, Skin.class, skinParameter);
        assetManager.finishLoading();
	}
}
