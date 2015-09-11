package render;

import game.PredatorPreyGame;
import geometry.PointXY;
import geometry.PolygonShape;
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
import com.badlogic.gdx.math.Vector2;
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
	
	private float widthToHeight;
	
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
		
		camera = new OrthographicCamera(); // TODO, investigate when this method call is needed.
		camera.update();
		setInitialViewport(1.5f);
//		trackPlayer(1.4f, false);
		
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
		
//		setViewportJump(5);
		setViewport(8, 0.5f);
		trackPlayer(1.4f, false);
		
		checkForGameOver();
	}
	
	private void setInitialViewport(float factor) {
		Vector2[] mazeBoundaries = getWorldMazeBoundaries();
		Vector2 mazeLL = mazeBoundaries[0];
		Vector2 mazeUR = mazeBoundaries[1];
		
		float mazeWidth = (mazeUR.x - mazeLL.x) * factor;
		float mazeHeight = (mazeUR.y - mazeLL.y) * factor;
	
		widthToHeight = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		
		if (widthToHeight <= 0) {
			camera.viewportWidth = mazeWidth;
			camera.viewportHeight = mazeWidth * widthToHeight;
		} else { // mazeHeight > mazeWidth
			camera.viewportHeight = mazeHeight;
			camera.viewportWidth = mazeHeight / widthToHeight;
		}
		
		camera.position.x = (mazeUR.x + mazeLL.x) / 2;
		camera.position.y = (mazeUR.y + mazeLL.y) / 2;
	}
	
//	private void setViewportJump(float maxSquaresX) {
//		widthToHeight = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
//		
//		camera.viewportWidth = maxSquaresX * physProc.getSquareSize();
//		camera.viewportHeight = camera.viewportWidth * widthToHeight;
//		
//		Vector2[] mazeBoundaries = getWorldMazeBoundaries();
//		Vector2 mazeLL = mazeBoundaries[0];
//		Vector2 mazeUR = mazeBoundaries[1];
//		
//		float mazeWidth = (mazeUR.x - mazeLL.x);
//		float mazeHeight = (mazeUR.y - mazeLL.y);
//		
//		float widthDiff = camera.viewportWidth - mazeWidth;
//		float heightDiff = camera.viewportHeight - mazeHeight;
//		
//		float scale = 0f;
//		
//		if (widthDiff > 0 && heightDiff > 0) {
//			if (widthDiff <= heightDiff) {
//				scale = mazeWidth / camera.viewportWidth;
//				camera.viewportWidth = camera.viewportWidth * scale;
//				camera.viewportHeight = (camera.viewportHeight * scale) * widthToHeight;
//			} else { // heightDiff < widthDiff
//				scale = mazeHeight / camera.viewportHeight;
//				camera.viewportHeight = camera.viewportHeight * scale;
//				camera.viewportWidth = (camera.viewportWidth * scale) / widthToHeight;
//			}
//		}
//		
//		camera.update();
//	}
	
	private void setViewport(float maxSquaresX, float factor) {
		widthToHeight = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		
		float targetWidth = maxSquaresX * physProc.getSquareSize();
		float targetHeight = targetWidth * widthToHeight;
		float newHeight = 0f;
		float newWidth = 0f;
		
		Vector2[] mazeBoundaries = getWorldMazeBoundaries();
		Vector2 mazeLL = mazeBoundaries[0];
		Vector2 mazeUR = mazeBoundaries[1];
		
		float mazeWidth = (mazeUR.x - mazeLL.x);
		float mazeHeight = (mazeUR.y - mazeLL.y);
		
		float widthDiff = targetWidth - mazeWidth;
		float heightDiff = targetHeight - mazeHeight;
		
		float scale = 0f;
		float delta = Gdx.graphics.getDeltaTime();
		
		newWidth = camera.viewportWidth + (targetWidth - camera.viewportWidth) * delta * factor;
		scale = camera.viewportWidth / newWidth;
		newHeight = (newWidth * scale) * widthToHeight;
		
		if (widthDiff > 0 && heightDiff > 0) {
			if (widthDiff <= heightDiff) {
				newWidth = camera.viewportWidth + (mazeWidth - camera.viewportWidth) * delta * factor;
				scale = camera.viewportWidth / newWidth;
				newHeight = (newWidth * scale) * widthToHeight;
			} else { // heightDiff < widthDiff
				newHeight = camera.viewportHeight + (mazeHeight - camera.viewportHeight) * delta * factor;
				scale = camera.viewportHeight / newHeight;
				newWidth = (newHeight * scale) / widthToHeight;
			}
		}
		
		camera.viewportWidth = newWidth;
		camera.viewportHeight = newHeight;
		
		camera.update();
	}
	
//	private Vector2[] getViewportBoundaries() {
//		float viewportWidthHalf = (camera.viewportWidth / 2);
//		float viewportHeightHalf = (camera.viewportHeight / 2);
//		
//		Vector2 viewportLL = new Vector2(camera.position.x - viewportWidthHalf, camera.position.y - viewportHeightHalf);
//		Vector2 viewportUR = new Vector2(camera.position.x - viewportWidthHalf, camera.position.y - viewportHeightHalf);
//		
//		Vector2[] viewportBoundaries = new Vector2[] {viewportLL, viewportUR};
//		
//		return viewportBoundaries;
//	}
	
	
	private Vector2[] getWorldMazeBoundaries() {
		PolygonShape pShape = gameLogic.getGameState().getMaze().getDimensions();
		Vector2 mazeLL = physProc.stateToWorld(new PointXY(pShape.getMinX() - 1, pShape.getMinY() - 1));
		Vector2 mazeUR = physProc.stateToWorld(new PointXY(pShape.getMaxX() + 1, pShape.getMaxY() + 1));
		
		Vector2[] mazeBoundaries = new Vector2[] {mazeLL, mazeUR};
		
		return mazeBoundaries;
	}
	
	private void trackPlayer(float factor, boolean jump) {
		
		Vector2[] mazeBoundaries = getWorldMazeBoundaries();
		Vector2 mazeLL = mazeBoundaries[0];
		Vector2 mazeUR = mazeBoundaries[1];
		
		float mazeWidth = (mazeUR.x - mazeLL.x);
		float mazeHeight = (mazeUR.y - mazeLL.y);
		
		float widthDiff = camera.viewportWidth - mazeWidth;
		float heightDiff = camera.viewportHeight - mazeHeight;
		
		float delta = Gdx.graphics.getDeltaTime(); // Or 0.1;
		float viewportWidthHalf = (camera.viewportWidth / 2);
		float viewportHeightHalf = (camera.viewportHeight / 2);
		
		Predator firstPredator = gameLogic.getGameState().getPredators().get(0);
		Vector2 playerVector = physProc.stateToWorld(firstPredator.getPosition());
		double newX = 0;
		double newY = 0;
		
		if (widthDiff >= 0) {
			newX = (mazeUR.x + mazeLL.x) / 2;
		} else {
			if (jump) {
				newX = playerVector.x;
			} else {
				newX = camera.position.x + (playerVector.x - camera.position.x) * delta * factor;
			}
			
			if (newX - viewportWidthHalf < mazeLL.x) newX = mazeLL.x + viewportWidthHalf;
			if (newX + viewportWidthHalf > mazeUR.x) newX = mazeUR.x - viewportWidthHalf;
		}
		
		if (heightDiff >= 0) {
			newY = (mazeUR.y + mazeLL.y) / 2;
		} else {
			if (jump) {
				newY = playerVector.y;
			} else {
				newY = camera.position.y + (playerVector.y - camera.position.y) * delta * factor;
			}
			
			if (newY - viewportHeightHalf < mazeLL.y) {
				newY = mazeLL.y + viewportHeightHalf;
			} else if (newY + viewportHeightHalf > mazeUR.y){
				newY = mazeUR.y - viewportHeightHalf;
			}
		}
		
		camera.position.x = (float) newX;
		camera.position.y = (float) newY;
		
		camera.update();
	}
	
	
	@Override
	public void resize(int width, int height) {
//		setViewportJump(5);
//		trackPlayer(1.4f, true);
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