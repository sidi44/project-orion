package game;

import geometry.PolygonShape;

import java.util.HashMap;
import java.util.Map;

import logic.GameConfiguration;
import logic.GameLogic;
import physics.PhysicsConfiguration;
import physics.PhysicsProcessor;
import physics.PhysicsProcessorBox2D;
import render.AnimationDefinition;
import render.AnimationGroupDefinition;
import render.GameScreen;
import render.MainMenuScreen;
import render.Renderer;
import render.RendererConfiguration;
import render.SettingsScreen;
import render.SplashScreen;
import xml.ConfigurationXMLParser;

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
	private InputMultiplexer inputMultiplexer;

	// Screens
	private final Map<String, Screen> screens;

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
		GameConfiguration gameConfig = xmlParser.getGameConfig();
		PhysicsConfiguration physicsConfig = xmlParser.getPhysicsConfig();
		RendererConfiguration rendererConfig = xmlParser.getRendererConfig();
		
		// Create the world.
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);

//		// Do not set numPlayers > 1 for now (see below).
//		int numPlayers = 1; 
//		MazeConfig mazeConfig = new MazeConfig(10, 50, 0.0, 0.8);
//		List<PointXY> vertices = new ArrayList<PointXY>();
//		vertices.add(new PointXY(0, 0));
//		vertices.add(new PointXY(0, 12));
//		vertices.add(new PointXY(12, 12));
//		vertices.add(new PointXY(12, 0));
//		List<PredatorPowerUp> predPowerUps = new ArrayList<PredatorPowerUp>();
//		PredatorPowerUp pow1 = 
//				new PredatorPowerUp(PredatorPowerType.SpeedUpPredator, 300);
//		predPowerUps.add(pow1);
//		
//		AgentConfig agentConfig = new AgentConfig(numPlayers, numPlayers, 5, 0);
//		PowerConfig powerUpConfig = new PowerConfig(1, predPowerUps, 0, 
//				new ArrayList<PreyPowerUp>());
//		GameConfig config = new GameConfig(new PolygonShape(vertices), true,
//				mazeConfig, agentConfig, powerUpConfig);
		gameLogic = new GameLogic(gameConfig);

//		PhysicsConfiguration physConfig = 
//				new PhysicsConfiguration(5f, 0.1f, 0.2f, 0.7f, 40f, 25f);

		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
				physicsConfig);

		renderer = new Renderer(false, false);
		
//		defineAnimations(config, physConfig);
		
		renderer.loadTextures(rendererConfig);
		//shortestPath();
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		loadScreens();
		setScreen(getScreenByName("SPLASH"));
	}

	
	private void defineAnimations(GameConfiguration gameConfig, 
			PhysicsConfiguration physicsConfig) {

		// Define the Predator Animation Group
		AnimationGroupDefinition predatorDef = new AnimationGroupDefinition();
		predatorDef.setAnimationGroupName("Predator");
		predatorDef.setFilename("assets/predator3.png");
		predatorDef.setColumns(11);
		predatorDef.setRows(4);
		
		// Define the individual predator animations
		AnimationDefinition def = new AnimationDefinition();
		def.setAnimationName(RendererConfiguration.ANIMATION_DOWN_STOP);
		def.setStartFrame(1);
		def.setEndFrame(1);
		def.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def);
		
		AnimationDefinition def2 = new AnimationDefinition();
		def2.setAnimationName(RendererConfiguration.ANIMATION_DOWN);
		def2.setStartFrame(2);
		def2.setEndFrame(7);
		def2.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def2);
		
		AnimationDefinition def3 = new AnimationDefinition();
		def3.setAnimationName(RendererConfiguration.ANIMATION_UP_STOP);
		def3.setStartFrame(12);
		def3.setEndFrame(12);
		def3.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def3);
		
		AnimationDefinition def4 = new AnimationDefinition();
		def4.setAnimationName(RendererConfiguration.ANIMATION_UP);
		def4.setStartFrame(13);
		def4.setEndFrame(18);
		def4.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def4);
		
		AnimationDefinition def5 = new AnimationDefinition();
		def5.setAnimationName(RendererConfiguration.ANIMATION_LEFT_STOP);
		def5.setStartFrame(23);
		def5.setEndFrame(23);
		def5.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def5);
		
		AnimationDefinition def6 = new AnimationDefinition();
		def6.setAnimationName(RendererConfiguration.ANIMATION_LEFT);
		def6.setStartFrame(24);
		def6.setEndFrame(29);
		def6.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def6);
		
		AnimationDefinition def7 = new AnimationDefinition();
		def7.setAnimationName(RendererConfiguration.ANIMATION_RIGHT_STOP);
		def7.setStartFrame(34);
		def7.setEndFrame(34);
		def7.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def7);
		
		AnimationDefinition def8 = new AnimationDefinition();
		def8.setAnimationName(RendererConfiguration.ANIMATION_RIGHT);
		def8.setStartFrame(35);
		def8.setEndFrame(40);
		def8.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def8);
		
		// Define the Prey Animation Group
		AnimationGroupDefinition preyDef = new AnimationGroupDefinition();
		preyDef.setAnimationGroupName("Prey");
		preyDef.setFilename("assets/prey.png");
		preyDef.setColumns(11);
		preyDef.setRows(4);
		
		// Define the Prey animations
		AnimationDefinition def9 = new AnimationDefinition();
		def9.setAnimationName(RendererConfiguration.ANIMATION_DOWN_STOP);
		def9.setStartFrame(1);
		def9.setEndFrame(1);
		def9.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def9);
		
		AnimationDefinition def10 = new AnimationDefinition();
		def10.setAnimationName(RendererConfiguration.ANIMATION_DOWN);
		def10.setStartFrame(2);
		def10.setEndFrame(7);
		def10.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def10);

		AnimationDefinition def11 = new AnimationDefinition();
		def11.setAnimationName(RendererConfiguration.ANIMATION_UP_STOP);
		def11.setStartFrame(12);
		def11.setEndFrame(12);
		def11.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def11);
		
		AnimationDefinition def12 = new AnimationDefinition();
		def12.setAnimationName(RendererConfiguration.ANIMATION_UP);
		def12.setStartFrame(13);
		def12.setEndFrame(18);
		def12.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def12);
		
		AnimationDefinition def13 = new AnimationDefinition();
		def13.setAnimationName(RendererConfiguration.ANIMATION_LEFT_STOP);
		def13.setStartFrame(23);
		def13.setEndFrame(23);
		def13.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def13);
		
		AnimationDefinition def14 = new AnimationDefinition();
		def14.setAnimationName(RendererConfiguration.ANIMATION_LEFT);
		def14.setStartFrame(24);
		def14.setEndFrame(29);
		def14.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def14);
		
		AnimationDefinition def15 = new AnimationDefinition();
		def15.setAnimationName(RendererConfiguration.ANIMATION_RIGHT_STOP);
		def15.setStartFrame(34);
		def15.setEndFrame(34);
		def15.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def15);
		
		AnimationDefinition def16 = new AnimationDefinition();
		def16.setAnimationName(RendererConfiguration.ANIMATION_RIGHT);
		def16.setStartFrame(35);
		def16.setEndFrame(40);
		def16.setFrameDuration(1 / 60f);	
		preyDef.addAnimation(def16);
		
		
		// Define the Pill Animation Group
		AnimationGroupDefinition pillDef = new AnimationGroupDefinition();
		pillDef.setAnimationGroupName("Pill");
		pillDef.setFilename("assets/coin.png");
		pillDef.setColumns(10);
		pillDef.setRows(1);
		
		// Define the Pill animations
		AnimationDefinition def17 = new AnimationDefinition();
		def17.setAnimationName("");
		def17.setStartFrame(1);
		def17.setEndFrame(10);
		def17.setFrameDuration(1.0f);
		pillDef.addAnimation(def17);
		
		
		// Define the Predator Power Up Animation Group
		AnimationGroupDefinition predatorPowerUpDef = 
				new AnimationGroupDefinition();
		predatorPowerUpDef.setAnimationGroupName("PowerUpPredator");
		predatorPowerUpDef.setFilename("assets/icons-pow-up.png");
		predatorPowerUpDef.setColumns(3);
		predatorPowerUpDef.setRows(4);
		
		// Define the Predator Power Up animations
		AnimationDefinition def18 = new AnimationDefinition();
		def18.setAnimationName("");
		def18.setStartFrame(12);
		def18.setEndFrame(12);
		def18.setFrameDuration(1.0f);
		predatorPowerUpDef.addAnimation(def18);
		
		
		// Define the Predator Power Up Animation Group
		AnimationGroupDefinition preyPowerUpDef = 
				new AnimationGroupDefinition();
		preyPowerUpDef.setAnimationGroupName("PowerUpPrey");
		preyPowerUpDef.setFilename("assets/icons-pow-up.png");
		preyPowerUpDef.setColumns(3);
		preyPowerUpDef.setRows(4);
		
		// Define the Predator Power Up animations
		AnimationDefinition def19 = new AnimationDefinition();
		def19.setAnimationName("");
		def19.setStartFrame(9);
		def19.setEndFrame(9);
		def19.setFrameDuration(1.0f);
		preyPowerUpDef.addAnimation(def19);
		
		
		// Define the configuration
		RendererConfiguration rendererConfig = new RendererConfiguration();
		rendererConfig.addAnimationGroup(predatorDef);
		rendererConfig.addAnimationGroup(preyDef);
		rendererConfig.addAnimationGroup(pillDef);
		rendererConfig.addAnimationGroup(predatorPowerUpDef);
		rendererConfig.addAnimationGroup(preyPowerUpDef);
		
		rendererConfig.setAllowRotations(true);
		rendererConfig.setBackgroundFilename("assets/vortex_0.png");
		
		PolygonShape dimensions = gameConfig.getDimensions();
		int mazeWidth = dimensions.getMaxX() + 1 - dimensions.getMinX();
		float width = mazeWidth * physicsConfig.getSquareSize();
		int mazeHeight = dimensions.getMaxY() + 1 - dimensions.getMinY(); 
		float height = mazeHeight * physicsConfig.getSquareSize();
		Vector2 bottomLeft = new Vector2(0, 0);
		Vector2 topRight = new Vector2(width, height);
		rendererConfig.setBackgroundDimensions(bottomLeft, topRight);
		
		rendererConfig.setWallTextureFilename("assets/wall.png");
		rendererConfig.setWallTextureScale(1.0f);
		
		renderer.loadTextures(rendererConfig);

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
	
}
