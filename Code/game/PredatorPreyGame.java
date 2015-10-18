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

	private World world;

	private GameLogic gameLogic;
	private Renderer renderer;
	private PhysicsProcessor physProc;
	
	private GameConfiguration gameConfig;
	private PhysicsConfiguration physicsConfig;
	private RendererConfiguration rendererConfig;
	
	private ResultLogger logger;
	
	// Screens
	private final Map<String, Screen> screens;
	private final InputMultiplexer inputMultiplexer;

	// Final initialisers
	{
		screens = new HashMap<String, Screen>();
		inputMultiplexer = new InputMultiplexer();
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

		// Create the world.
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);

		gameLogic = new GameLogic(gameConfig);

		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
				physicsConfig);

		renderer = new Renderer(false, false);
		
		renderer.loadTextures(rendererConfig);

		Gdx.input.setInputProcessor(inputMultiplexer);
		loadScreens();
		setScreen(getScreenByName("SPLASH"));
		
		logger = new ResultLogger();
	}

	public void startGame() {
		switchToScreen("GAME");
	}
	
	public void setAI(AILogic ai) {
		gameLogic.setAILogic(ai);
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
		
		Screen screen = getScreenByName("GAME");
		GameScreen gameScreen = (GameScreen) screen;
		gameScreen.reset(world, gameLogic, physProc);
	}
}
