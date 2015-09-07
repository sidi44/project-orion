package render;

import game.PredatorPreyGame;
import input.UserInputProcessor;

import java.util.List;

import logic.Agent;
import logic.GameLogic;
import logic.GameOver;
import logic.GameState;
import logic.Move;
import logic.Predator;
import logic.Prey;
import physics.PhysicsProcessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class GameScreen implements Screen {

	private final PredatorPreyGame game;
	private final World world;
	private final OrthographicCamera camera;
	private final GameLogic gameLogic;
	private final Renderer renderer;
	private final PhysicsProcessor physProc;
	private final UserInputProcessor inputProc;
	private Stage gameStage;

	private final float dt = 1.0f / 60.0f;
	private float accumulator;
	
	// Gameplay fields
	private final static long nanoToSeconds = 1000000000;
	private long startTime;
	private long timeLimit;
	
	public GameScreen(PredatorPreyGame game) {
		this.game = game;
		this.world = game.getWorld();
		this.renderer = game.getRenderer();
		this.gameLogic = game.getGameLogic();
		this.physProc = game.getPhysicsProcessor();
		
		this.startTime = System.nanoTime() / nanoToSeconds;
		this.timeLimit = 200; // seconds.
		
		// Process gameplay inputs
		this.inputProc = new UserInputProcessor();
		game.addInputProcessor(inputProc);
		
		// TODO this should be reworked and moved into a separate class.
		setupStage();

		camera = new OrthographicCamera(75, 75);
		camera.setToOrtho(false, 75, 75); // TODO, investigate when this method call is needed.
		camera.position.x = 35;
		camera.position.y = 35;
		camera.update();
		
		accumulator = 0;
	}
	
	private void setupStage() {
		
		// Game panel (UI) inputs
		gameStage = new Stage();
		game.addInputProcessor(gameStage);

		Button menuButton = game.createButton("button_menu.png",
											  "button_menu_highlight.png",
											  "MAIN_MENU",
											  400, 400);
		gameStage.addActor(menuButton);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render(world, camera.combined);
		gameStage.draw();
		
		processMoves();
		inputProc.processCameraInputs(camera);

		GameState state = gameLogic.getGameState();
		physProc.preStep(state);
		
		// Grab the time difference. Limit the maximum amount of time we can 
		// progress the physics simulation for a given render frame.
		delta = (float) Math.min(delta, 0.25);
		
		// Add this frame's time to the accumulator.
		accumulator += delta;
		
		// Step the simulation at the given fixed rate for as many times as 
		// required. Any left over time is passed over to the next frame.
		while (accumulator >= dt) {
			physProc.stepSimulation(dt);
			accumulator -= dt;
		}
		
		physProc.postStep(state);
		
		checkForGameOver();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void processMoves() {

		// Do the player moves.
		List<Agent> players = gameLogic.getAllPlayers();
		for (Agent a : players) {
			int id = a.getID();
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
	
}