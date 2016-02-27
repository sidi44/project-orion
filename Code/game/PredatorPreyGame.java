package game;

import java.util.List;

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
import physics.PhysicsProcessor;
import physics.PhysicsProcessorBox2D;
import render.Renderer;
import render.RendererConfiguration;
import sound.SoundManager;
import ui.ScreenManager;
import ui.ScreenName;
import xml.ConfigurationXMLParser;
import ai.AILogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PredatorPreyGame extends Game	 {

	private World world;

	private GameLogic gameLogic;
	private Renderer renderer;
	private PhysicsProcessor physProc;
	private SoundManager soundManager;
	
	private GameConfiguration gameConfig;
	private PhysicsConfiguration physicsConfig;
	private RendererConfiguration rendererConfig;
	
	private ResultLogger logger;
	
	private ScreenManager screenManager;
	
	// Physics debug information
	private final PhysicsDebugType debugType = PhysicsDebugType.DebugNone;
	
	@Override
	public void create() {

		String filename = "Configuration.xml";
		String schemaFilename = "Configuration.xsd";
		ConfigurationXMLParser xmlParser = 
				new ConfigurationXMLParser(filename, schemaFilename);
		xmlParser.parseXML();
		gameConfig = xmlParser.getGameConfig();
		physicsConfig = xmlParser.getPhysicsConfig();
		rendererConfig = xmlParser.getRendererConfig();

		// Create the world.
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);

		gameLogic = new GameLogic(gameConfig);

		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
				physicsConfig);
		physProc.setDebugCategory(debugType);
		
		renderer = new Renderer(false, false);
		
		renderer.loadTextures(rendererConfig);

//		Gdx.input.setInputProcessor(inputMultiplexer);
//		setScreen(getScreenByName("SPLASH"));
		screenManager = new ScreenManager(this);
		screenManager.changeScreen(ScreenName.Splash);
		
		logger = new ResultLogger();
		
		soundManager = new SoundManager();
		physProc.addReceiver(soundManager);
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
	
	public World getWorld() {
		return world;
	}
	
	/**
	 * Adds an input processer to the input multiplexer if it hasn't been 
	 * already added. Throws an exception 
	 * @param inputProc - the input processor.
	 */
	public void addInputProcessor(InputProcessor inputProc) {
		
//		if (inputProc == null) {
//			throw new IllegalArgumentException("Input processor can't be null");
//		}
//		
//		if (!inputMultiplexer.getProcessors().contains(inputProc, true)) {
//			inputMultiplexer.addProcessor(inputProc);
//		}
	}
	
	public PhysicsProcessor getPhysicsProcessor() {
		return physProc;
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
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);
		gameLogic = new GameLogic(gameConfig);
		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
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
	
}
