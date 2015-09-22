package game;

import java.util.HashMap;
import java.util.Map;

import logic.GameConfiguration;
import logic.GameLogic;
import physics.PhysicsConfiguration;
import physics.PhysicsProcessor;
import physics.PhysicsProcessorBox2D;
import render.GameScreen;
import render.MainMenuScreen;
import render.Renderer;
import render.RendererConfiguration;
import render.SettingsScreen;
import render.SplashScreen;
import xml.ConfigurationXMLParser;
import logic.GameOver;
import logic.GameState;
import ai.AILogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class PredatorPreyGame extends Game	 {

	private enum State {
		PRESTART,
		PAUSED,
		RUNNING,
		STOPPED
	}
	
	private World world;

	private GameLogic gameLogic;
	private Renderer renderer;
	private PhysicsProcessor physProc;
	private InputMultiplexer inputMultiplexer;
	
	private GameConfiguration gameConfig;
	private PhysicsConfiguration physicsConfig;
	private RendererConfiguration rendererConfig;
	
	private boolean doRender;
	
	private int timeLimit;
	
	private int numGames;
	private int numSimSteps;
	private ResultLogger logger;
	
	private State state;
	
	// Screens
	private final Map<String, Screen> screens;

	// Final initialisers
	{
		screens = new HashMap<String, Screen>();
		inputMultiplexer = new InputMultiplexer();
	}
	
	public PredatorPreyGame(int numGames) {
		this.numGames = numGames;
	}
	
	public PredatorPreyGame() {
		this.numGames = 1;
		this.state = State.PRESTART;
	}
	
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
		
		timeLimit = 1000; // simulation steps.

		// Create the world.
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);

		gameLogic = new GameLogic(gameConfig);

		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
				physicsConfig);

		renderer = new Renderer(false, false);
		
		renderer.loadTextures(rendererConfig);
		
		numSimSteps = 0;
		logger = new ResultLogger();
		
		state = State.PAUSED;
		doRender = true;

		Gdx.input.setInputProcessor(inputMultiplexer);
		loadScreens();
		setScreen(getScreenByName("SPLASH"));
	}

//	@Override
//	public void render() {
//		
//		switch (state) {
//			case PAUSED:
//				break;
//				
//			case RUNNING:
//				++numSimSteps;
//	
//				if (doRender) {
//					Gdx.gl.glClearColor(0, 0, 0, 1);
//					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//					renderer.render(world, camera.combined);
//				}
//				
//				processMoves();
//	
//				inputProcs.get(cameraID).processCameraInputs(camera);
//	
//				GameState state = gameLogic.getGameState();
//				physProc.preStep(state);
//
//				// Grab the time difference. Limit the maximum amount of time we can 
//				// progress the physics simulation for a given render frame.
//				float delta = Gdx.graphics.getDeltaTime();
//				delta = (float) Math.min(delta, 0.25);
//		
//				// Add this frame's time to the accumulator.
//				accumulator += delta;
//		
//				// Step the simulation at the given fixed rate for as many times as 
//				// required. Any left over time is passed over to the next frame.
//				while (accumulator >= dt) {
//					physProc.stepSimulation(dt);
//					accumulator -= dt;
//				}
//		
//				physProc.postStep(state);
//	
//				checkForGameOver();
//				break;
//			
//			case STOPPED:
//				break;
//				
//			default:
//				break;		
//		}
//	}

//	private void processMoves() {
//
//		// Do the player moves.
//		List<Agent> players = gameLogic.getAllPlayers();
//		for (Agent a : players) {
//			int id = a.getID();
//			UserInputProcessor inputProc = inputProcs.get(id);
//			Move m = inputProc.getNextMove();
//			if (a instanceof Predator) {
//				gameLogic.setPredNextMove(id, m);
//			} else if (a instanceof Prey) {
//				gameLogic.setPreyNextMove(id, m);
//			}
//		}
//
//		gameLogic.setNonPlayerMoves();
//	}

//	private void checkForGameOver() {
//		int gameTime = timeLimit - numSimSteps;
//		GameOver gameOver = gameLogic.isGameOver(gameTime);
//
//		switch (gameOver) {
//			case Pills:
//			case Time:
//			case Prey:
//				logResult(gameOver);
//				--numGames;
//				if (numGames > 0) {
//					System.out.println(numGames + " games to go...");
//					resetGame();
//				} else {
//					// Stop the game
//					//Gdx.app.getApplicationListener().pause();
//					state = State.STOPPED;
//				}
//				break;
//
//			case No:
//			default:
//				break;
//		}
//	}

	public void resetGame() {
		numSimSteps = 0;
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);
		
		gameLogic = new GameLogic(gameConfig);
		
		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
				physicsConfig);
		
		logger = new ResultLogger();
		
		state = State.RUNNING;
	}
	
	public boolean isStopped() {
		return (state == State.STOPPED);
	}
	
	public void startGame() {
		state = State.RUNNING;
	}
	
	public boolean isLoading() {
		return (state == State.PRESTART);
	}
	
	public void setAI(AILogic ai) {
		gameLogic.setAILogic(ai);
	}
	
	@Override
	public void pause() {
		//state = State.PAUSED;
	}

	@Override
	public void resume() {
		//state = State.RUNNING;
	}
	
	private void logResult(GameOver result) {
		GameState gs = gameLogic.getGameState();
		int numPillsRemaining = gs.getPills().size();
		int numSquares = gs.getMaze().getNodes().keySet().size();
		GameResult gr = new GameResult(result, numSimSteps, numPillsRemaining,
				numSquares);
		logger.addResult(gr);
	}
	
	public ResultLogger getLogger() {
		return logger;
	}
	
	public void setDoRender(boolean doRender) {
		this.doRender = doRender;
	}
	
	private void loadScreens() {
		screens.put("SPLASH", new SplashScreen(this));
		screens.put("MAIN_MENU", new MainMenuScreen(this));
		screens.put("SETTINGS", new SettingsScreen(this));
		screens.put("GAME", new GameScreen(this));
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
		
		if (inputProc == null) {
			throw new IllegalArgumentException("Input processor can't be null");
		}
		
		if (!inputMultiplexer.getProcessors().contains(inputProc, true)) {
			inputMultiplexer.addProcessor(inputProc);
		}
	}
	
	public PhysicsProcessor getPhysicsProcessor() {
		return physProc;
	}
	
	public Screen getScreenByName(String screenName) {
		return screens.get(screenName);
	}
	
	public void switchToScreen(String name) {
		setScreen(getScreenByName(name));
	}
	
	public Button createButton(final String buttonName,
							   final String buttonHighlightName, 
							   final String screenName,
							   float posX, float posY) {
		
		SpriteDrawable buttonDrawable = getDrawableFromFile(buttonName);
		SpriteDrawable buttonDrawableMouseOver = getDrawableFromFile(buttonHighlightName);

		ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
		buttonStyle.imageUp = buttonDrawable;
		buttonStyle.imageOver = buttonDrawableMouseOver;

		ImageButton button = new ImageButton(buttonStyle);

		button.setPosition(posX, posY);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				switchToScreen(screenName);
			}
		});

		return button;
	}
	
	private SpriteDrawable getDrawableFromFile(String filename) {
		Texture texture = new Texture(Gdx.files.internal(filename));
		return new SpriteDrawable(new Sprite(texture));
	}
	
	/**
	 * @return the gameConfig
	 */
	public GameConfiguration getGameConfig() {
		return gameConfig;
	}

	/**
	 * @return the physicsConfig
	 */
	public PhysicsConfiguration getPhysicsConfig() {
		return physicsConfig;
	}

	/**
	 * @return the rendererConfig
	 */
	public RendererConfiguration getRendererConfig() {
		return rendererConfig;
	}
	
}
