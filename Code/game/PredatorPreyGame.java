package game;

import input.UserInputProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geometry.Rectangle;
import physics.PhysicsConfiguration;
import physics.PhysicsProcessor;
import physics.PhysicsProcessorBox2D;
import render.Renderer;
import logic.Agent;
import logic.GameConfig;
import logic.GameLogic;
import logic.GameState;
import logic.MazeConfig;
import logic.Move;
import logic.PowerType;
import logic.Predator;
import logic.Prey;

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
	
	private GameLogic gameLogic;
	
	private Renderer renderer;
	
	private long startTime;
	private long timeLimit;
	
	private final static long nanoToSeconds = 1000000000;
	
	@Override
	public void create () {
		
		startTime = System.nanoTime() * nanoToSeconds;
		timeLimit = 200; // seconds.
		
		// Create the world.
		Vector2 gravity = new Vector2(0f, 0f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);
		
		// Do not set numPlayers > 1 for now (see below).
		int numPlayers = 1; 
		MazeConfig mazeConfig = new MazeConfig(10, 50, 0.0, 0.8);
		GameConfig config = new GameConfig(new Rectangle(0, 0, 10, 10), 
				mazeConfig, numPlayers, numPlayers, 5, 0, true, 
				new ArrayList<PowerType>());
		gameLogic = new GameLogic(config);
		
		PhysicsConfiguration physConfig = 
				new PhysicsConfiguration(5f, 0.05f, 0.1f, 40f, 32f);
		
		physProc = new PhysicsProcessorBox2D(world, gameLogic.getGameState(), 
				physConfig);
		
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
		
		camera = new OrthographicCamera(100, 100);
		camera.position.x = 25;
		camera.position.y = 25;
		camera.update();
		renderer = new Renderer(world, camera);
		
		//shortestPath();
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		
		processMoves();
		
		inputProcs.get(1).processCameraInputs(camera);
		
		GameState state = gameLogic.getGameState();
		physProc.processGameState(state);
		
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
		
		// Do the nonplayer moves.
		//if (count % 30 == 0) {
			gameLogic.setNonPlayerMoves();
		//}
	}
	
	private void checkForGameOver() {
		long elapsedTime = (System.nanoTime() * nanoToSeconds) - startTime;
		long gameTime = timeLimit - elapsedTime;
		boolean gameOver = gameLogic.isGameOver(gameTime);
		if (gameOver) {
			if (gameLogic.predatorsWon()) {
				System.out.println("Predators won.");
			} else {
				System.out.println("Prey won.");
			}
		}
	}
	
}
