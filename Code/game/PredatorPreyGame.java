package game;

import input.UserInputProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geometry.PointXY;
import geometry.PolygonShape;
import physics.PhysicsConfiguration;
import physics.PhysicsProcessor;
import physics.PhysicsProcessorBox2D;
import render.AnimationDefinition;
import render.AnimationGroupDefinition;
import render.Renderer;
import render.RendererConfiguration;
//import xml.ConfigurationXMLParser;
import logic.Agent;
import logic.AgentConfig;
import logic.GameConfig;
import logic.GameLogic;
import logic.GameOver;
import logic.GameState;
import logic.MazeConfig;
import logic.Move;
import logic.PowerConfig;
import logic.Predator;
import logic.PredatorPowerType;
import logic.PredatorPowerUp;
import logic.Prey;
import logic.PreyPowerUp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PredatorPreyGame extends ApplicationAdapter {

	private World world;
	private Camera camera;

	private PhysicsProcessor physProc;
	private Map<Integer, UserInputProcessor> inputProcs;

	private float timestep;

	private GameLogic gameLogic;

	private Renderer renderer;

	private long startTime;
	private long timeLimit;

	private final static long nanoToSeconds = 1000000000;

	@Override
	public void create() {

//		String filename = "xml\\Configuration.xml";
//		ConfigurationXMLParser xmlParser = new ConfigurationXMLParser(filename);
//		xmlParser.parseXML();
//		PhysicsConfiguration test = xmlParser.getPhysicsConfig();
//		RendererConfiguration test2 = xmlParser.getRendererConfig();
		
		startTime = System.nanoTime() / nanoToSeconds;
		timeLimit = 200; // seconds.

		// Create the world.
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);

		// Do not set numPlayers > 1 for now (see below).
		int numPlayers = 1; 
		MazeConfig mazeConfig = new MazeConfig(10, 50, 0.0, 0.8);
		List<PointXY> vertices = new ArrayList<PointXY>();
		vertices.add(new PointXY(0, 0));
		vertices.add(new PointXY(0, 12));
		vertices.add(new PointXY(12, 12));
		vertices.add(new PointXY(12, 0));
		List<PredatorPowerUp> predPowerUps = new ArrayList<PredatorPowerUp>();
		PredatorPowerUp pow1 = 
				new PredatorPowerUp(PredatorPowerType.SpeedUpPredator, 300);
		predPowerUps.add(pow1);
		
		AgentConfig agentConfig = new AgentConfig(numPlayers, numPlayers, 5, 0);
		PowerConfig powerUpConfig = new PowerConfig(1, predPowerUps, 0, 
				new ArrayList<PreyPowerUp>());
		GameConfig config = new GameConfig(new PolygonShape(vertices), true,
				mazeConfig, agentConfig, powerUpConfig);
		gameLogic = new GameLogic(config);

		PhysicsConfiguration physConfig = 
				new PhysicsConfiguration(5f, 0.1f, 0.2f, 40f, 25f);

		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
				physConfig);

		timestep = Gdx.graphics.getDeltaTime();

		inputProcs = new HashMap<Integer, UserInputProcessor>();

		List<Agent> players = gameLogic.getAllPlayers();
		for (Agent a : players) {
			UserInputProcessor inputProc = new UserInputProcessor();
			inputProcs.put(a.getID(), inputProc);
			
			// This currently only works for a single player game, but is set
			// up to work for multiplayer (i.e. each player would have their 
			// own input processor which wouldn't be given to libgdx. The should
			// probably be an inputProcessor interface to allow handling the 
			// two scenarios :-)
			Gdx.input.setInputProcessor(inputProc);
		}

//		UserInputProcessor inputProc = new UserInputProcessor();
//		inputProcs.put(-1, inputProc);
//		Gdx.input.setInputProcessor(inputProc);

		camera = new OrthographicCamera(75, 75);
		camera.position.x = 35;
		camera.position.y = 35;
		camera.update();
		renderer = new Renderer(false, false);
		
		defineAnimations(config, physConfig);
		
		//shortestPath();
	}

	@Override
	public void render() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render(world, camera.combined);

		processMoves();

		inputProcs.get(1).processCameraInputs(camera);

		GameState state = gameLogic.getGameState();
		timestep = Gdx.graphics.getDeltaTime();
		physProc.processGameState(state, timestep);

		checkForGameOver();
	}

	private void processMoves() {

		// Do the player moves.
		List<Agent> players = gameLogic.getAllPlayers();
		for (Agent a : players) {
			int id = a.getID();
			UserInputProcessor inputProc = inputProcs.get(id);
			Move m = inputProc.getNextMove();
			if (a instanceof Predator) {
				gameLogic.setPredNextMove(id, m);
			} else if (a instanceof Prey) {
				gameLogic.setPreyNextMove(id, m);
			}
		}

		gameLogic.setNonPlayerMoves();
	}

	private void checkForGameOver() {
		long elapsedTime = (System.nanoTime() / nanoToSeconds) - startTime;
		long gameTime = timeLimit - elapsedTime;
		GameOver gameOver = gameLogic.isGameOver((int) gameTime);

		switch (gameOver) {
			case Pills:
			case Time:
				//System.out.println("Prey won.");
				break;

			case Prey:
				//System.out.println("Predators won.");
				break;

			case No:
			default:
				break;
		}

	}

	private void defineAnimations(GameConfig gameConfig, 
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
		
		// Define the Prey animations
		AnimationDefinition def17 = new AnimationDefinition();
		def17.setAnimationName("");
		def17.setStartFrame(1);
		def17.setEndFrame(10);
		def17.setFrameDuration(1.0f);
		pillDef.addAnimation(def17);
		
		
		// Define the configuration
		RendererConfiguration rendererConfig = new RendererConfiguration();
		rendererConfig.addAnimationGroup(predatorDef);
		rendererConfig.addAnimationGroup(preyDef);
		rendererConfig.addAnimationGroup(pillDef);
		
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
}
