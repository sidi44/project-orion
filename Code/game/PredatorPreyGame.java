package game;

import input.InputFilter;

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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class PredatorPreyGame extends Game	 {

	private World world;

	private GameLogic gameLogic;
	private Renderer renderer;
	private AssetManager assetManager;
	private PhysicsProcessor physProc;
	
	private GameConfiguration gameConfig;
	private PhysicsConfiguration physicsConfig;
	private RendererConfiguration rendererConfig;
	
	private ResultLogger logger;
	private OrthographicCamera stageCamera;
	
	// Screens
	private final Map<String, Screen> screens;
	private final InputFilter inputFilter;

	// Final initialisers
	{
		screens = new HashMap<String, Screen>();
		inputFilter = new InputFilter();
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
		assetManager = new AssetManager();
		
		renderer.loadTextures(rendererConfig);

		Gdx.input.setInputProcessor(inputFilter);
		loadScreens();
		setScreen(getScreenByName("SPLASH"));
		
		logger = new ResultLogger();
	}

	@Override
	public void render() {
		super.render();
		
		
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
	 * Adds an input processor to the input multiplexer if it hasn't been 
	 * already added. Throws an exception 
	 * @param inputProc - the input processor.
	 */
	public void addInputProcessor(String screenName, InputProcessor inputProc) {
		inputFilter.addInputProcessorForScreen(screenName, inputProc);
	}
	
	public PhysicsProcessor getPhysicsProcessor() {
		return physProc;
	}
	
	public Screen getScreenByName(String screenName) {
		return screens.get(screenName);
	}
	
	public void switchToScreen(String screenName) {
		setScreen(getScreenByName(screenName));
		inputFilter.setActiveInputProcessor(screenName);
	}
	
	public ImageButton createButton(final String buttonUpName,
									final String buttonOverName,
									final String locatedInScreenName,
									final String switchToScreenName,
									final float posX,
									final float posY,
									final float buttonWidthCm,
									final float buttonHeightCm) {
		
		float buttonWidth = buttonWidthCm * Gdx.graphics.getPpcX();
		float buttonHeight = buttonHeightCm * Gdx.graphics.getPpcY();

		Sprite buttonUp = getSprite(buttonUpName);
		Sprite buttonOver = getSprite(buttonOverName);

		buttonUp.setSize(buttonWidth, buttonHeight);
		buttonOver.setSize(buttonWidth, buttonHeight);
		
		ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
		buttonStyle.imageUp = new SpriteDrawable(buttonUp);
		buttonStyle.imageOver = new SpriteDrawable(buttonOver);

		ImageButton button = new ImageButton(buttonStyle);

		button.setPosition(posX, posY);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Event received by button: " + buttonUpName);

				// Take action only if the event originated in the current active screen.
				if (getScreenByName(locatedInScreenName) == getScreen()) {
					switchToScreen(switchToScreenName);
				}
			}
		});

		return button;
	}
	
//	private SpriteDrawable getDrawableFromFile(String filename) {
//		Texture texture = new Texture(Gdx.files.internal(filename));
//		return new SpriteDrawable(new Sprite(texture));
//	}
	
	public Sprite getSprite(String filename) {
		Texture texture = new Texture(Gdx.files.internal(filename));
		return new Sprite(texture);
	}
	
	public ResultLogger getLogger() {
		return logger;
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
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
