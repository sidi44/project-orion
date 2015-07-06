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
import render.AnimationConfiguration;
import render.AnimationDefinition;
import render.Renderer;
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
	public void create () {

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
		vertices.add(new PointXY(0, 10));
		vertices.add(new PointXY(10, 10));
		vertices.add(new PointXY(10, 0));
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
				new PhysicsConfiguration(5f, 0.05f, 0.1f, 40f, 25f);

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

		camera = new OrthographicCamera(70, 70);
		camera.position.x = 25;
		camera.position.y = 25;
		camera.update();
		renderer = new Renderer(true, false);
		
		defineAnimations();
		
		//shortestPath();
	}

	@Override
	public void render () {
		
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
				System.out.println("Prey won.");
				break;

			case Prey:
				System.out.println("Predators won.");
				break;

			case No:
			default:
				break;
		}

	}

	private void defineAnimations() {

		// Define the Predator animations
		AnimationDefinition def = new AnimationDefinition();
		def.setAnimationGroupName("Predator");
		def.setAnimationName(AnimationConfiguration.ANIMATION_DOWN_STOP);
		def.setFilename("assets/predator2.png");
		def.setColumns(11);
		def.setRows(4);
		def.setStartFrame(1);
		def.setEndFrame(1);
		def.setFrameDuration(1 / 60f);

		AnimationDefinition def2 = new AnimationDefinition();
		def2.setAnimationGroupName("Predator");
		def2.setAnimationName(AnimationConfiguration.ANIMATION_DOWN);
		def2.setFilename("assets/predator2.png");
		def2.setColumns(11);
		def2.setRows(4);
		def2.setStartFrame(2);
		def2.setEndFrame(7);
		def2.setFrameDuration(1 / 60f);

		AnimationDefinition def3 = new AnimationDefinition();
		def3.setAnimationGroupName("Predator");
		def3.setAnimationName(AnimationConfiguration.ANIMATION_UP_STOP);
		def3.setFilename("assets/predator2.png");
		def3.setColumns(11);
		def3.setRows(4);
		def3.setStartFrame(12);
		def3.setEndFrame(12);
		def3.setFrameDuration(1 / 60f);

		AnimationDefinition def4 = new AnimationDefinition();
		def4.setAnimationGroupName("Predator");
		def4.setAnimationName(AnimationConfiguration.ANIMATION_UP);
		def4.setFilename("assets/predator2.png");
		def4.setColumns(11);
		def4.setRows(4);
		def4.setStartFrame(13);
		def4.setEndFrame(18);
		def4.setFrameDuration(1 / 60f);

		AnimationDefinition def5 = new AnimationDefinition();
		def5.setAnimationGroupName("Predator");
		def5.setAnimationName(AnimationConfiguration.ANIMATION_LEFT_STOP);
		def5.setFilename("assets/predator2.png");
		def5.setColumns(11);
		def5.setRows(4);
		def5.setStartFrame(23);
		def5.setEndFrame(23);
		def5.setFrameDuration(1 / 60f);

		AnimationDefinition def6 = new AnimationDefinition();
		def6.setAnimationGroupName("Predator");
		def6.setAnimationName(AnimationConfiguration.ANIMATION_LEFT);
		def6.setFilename("assets/predator2.png");
		def6.setColumns(11);
		def6.setRows(4);
		def6.setStartFrame(24);
		def6.setEndFrame(29);
		def6.setFrameDuration(1 / 60f);

		AnimationDefinition def7 = new AnimationDefinition();
		def7.setAnimationGroupName("Predator");
		def7.setAnimationName(AnimationConfiguration.ANIMATION_RIGHT_STOP);
		def7.setFilename("assets/predator2.png");
		def7.setColumns(11);
		def7.setRows(4);
		def7.setStartFrame(34);
		def7.setEndFrame(34);
		def7.setFrameDuration(1 / 60f);

		AnimationDefinition def8 = new AnimationDefinition();
		def8.setAnimationGroupName("Predator");
		def8.setAnimationName(AnimationConfiguration.ANIMATION_RIGHT);
		def8.setFilename("assets/predator2.png");
		def8.setColumns(11);
		def8.setRows(4);
		def8.setStartFrame(35);
		def8.setEndFrame(40);
		def8.setFrameDuration(1 / 60f);

		// Define the Prey animations
		AnimationDefinition def9 = new AnimationDefinition();
		def9.setAnimationGroupName("Prey");
		def9.setAnimationName(AnimationConfiguration.ANIMATION_DOWN_STOP);
		def9.setFilename("assets/prey.png");
		def9.setColumns(11);
		def9.setRows(4);
		def9.setStartFrame(1);
		def9.setEndFrame(1);
		def9.setFrameDuration(1 / 60f);

		AnimationDefinition def10 = new AnimationDefinition();
		def10.setAnimationGroupName("Prey");
		def10.setAnimationName(AnimationConfiguration.ANIMATION_DOWN);
		def10.setFilename("assets/prey.png");
		def10.setColumns(11);
		def10.setRows(4);
		def10.setStartFrame(2);
		def10.setEndFrame(7);
		def10.setFrameDuration(1 / 60f);

		AnimationDefinition def11 = new AnimationDefinition();
		def11.setAnimationGroupName("Prey");
		def11.setAnimationName(AnimationConfiguration.ANIMATION_UP_STOP);
		def11.setFilename("assets/prey.png");
		def11.setColumns(11);
		def11.setRows(4);
		def11.setStartFrame(12);
		def11.setEndFrame(12);
		def11.setFrameDuration(1 / 60f);

		AnimationDefinition def12 = new AnimationDefinition();
		def12.setAnimationGroupName("Prey");
		def12.setAnimationName(AnimationConfiguration.ANIMATION_UP);
		def12.setFilename("assets/prey.png");
		def12.setColumns(11);
		def12.setRows(4);
		def12.setStartFrame(13);
		def12.setEndFrame(18);
		def12.setFrameDuration(1 / 60f);

		AnimationDefinition def13 = new AnimationDefinition();
		def13.setAnimationGroupName("Prey");
		def13.setAnimationName(AnimationConfiguration.ANIMATION_LEFT_STOP);
		def13.setFilename("assets/prey.png");
		def13.setColumns(11);
		def13.setRows(4);
		def13.setStartFrame(23);
		def13.setEndFrame(23);
		def13.setFrameDuration(1 / 60f);

		AnimationDefinition def14 = new AnimationDefinition();
		def14.setAnimationGroupName("Prey");
		def14.setAnimationName(AnimationConfiguration.ANIMATION_LEFT);
		def14.setFilename("assets/prey.png");
		def14.setColumns(11);
		def14.setRows(4);
		def14.setStartFrame(24);
		def14.setEndFrame(29);
		def14.setFrameDuration(1 / 60f);

		AnimationDefinition def15 = new AnimationDefinition();
		def15.setAnimationGroupName("Prey");
		def15.setAnimationName(AnimationConfiguration.ANIMATION_RIGHT_STOP);
		def15.setFilename("assets/prey.png");
		def15.setColumns(11);
		def15.setRows(4);
		def15.setStartFrame(34);
		def15.setEndFrame(34);
		def15.setFrameDuration(1 / 60f);

		AnimationDefinition def16 = new AnimationDefinition();
		def16.setAnimationGroupName("Prey");
		def16.setAnimationName(AnimationConfiguration.ANIMATION_RIGHT);
		def16.setFilename("assets/prey.png");
		def16.setColumns(11);
		def16.setRows(4);
		def16.setStartFrame(35);
		def16.setEndFrame(40);
		def16.setFrameDuration(1 / 60f);	

		AnimationConfiguration anConfig = new AnimationConfiguration();
		anConfig.addAnimation(def);
		anConfig.addAnimation(def2);
		anConfig.addAnimation(def3);
		anConfig.addAnimation(def4);
		anConfig.addAnimation(def5);
		anConfig.addAnimation(def6);
		anConfig.addAnimation(def7);
		anConfig.addAnimation(def8);
		anConfig.addAnimation(def9);
		anConfig.addAnimation(def10);
		anConfig.addAnimation(def11);
		anConfig.addAnimation(def12);
		anConfig.addAnimation(def13);
		anConfig.addAnimation(def14);
		anConfig.addAnimation(def15);
		anConfig.addAnimation(def16);

		renderer.loadAnimations(anConfig);

	}
}
