package render;

import game.GameResult;
import game.PredatorPreyGame;
import geometry.PointXY;
import geometry.PolygonShape;
import input.UserInputProcessor;

import java.util.List;
import java.util.Locale;

import logic.Agent;
import logic.GameLogic;
import logic.GameOver;
import logic.GameState;
import logic.Move;
import logic.Predator;
import logic.Prey;
import physics.PhysicsProcessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class GameScreen implements Screen {

	private int numSimSteps;
	private final int maxSimSteps;
	
	private final PredatorPreyGame game;
	private World world;
	private final OrthographicCamera gameCamera;
	private final OrthographicCamera stageCamera;
	private ScalingViewport viewport;
	private GameLogic gameLogic;
	private final Renderer renderer;
	private BitmapFont font;
	private SpriteBatch batch;
	private PhysicsProcessor physProc;
	private final InputMultiplexer inputMultiplexer;
	private final UserInputProcessor inputProc;

	// HUD fields
	private Stage gameStage;
	
	private final float dt = 1.0f / 60.0f;
	private float accumulator;
	// We need a flag to know when the game is over to stop all processing
	// and animations, but we may still want to render static content in the 
	// background in case the screen is resized.
	private boolean gameIsOver;
	
	// Gameplay fields
//	private final static long nanoToSeconds = 1000000000;
	private long startTime;
	private long elapsedTime;
//	private long timeLimit;
	
	private float widthToHeight;
	
	public GameScreen(PredatorPreyGame game) {
		this.game = game;
		world = game.getWorld();
		renderer = game.getRenderer();
		gameLogic = game.getGameLogic();
		physProc = game.getPhysicsProcessor();
		
		startTime = TimeUtils.nanoTime();
//		this.timeLimit = 200; // seconds.
		
		// Process gameplay inputs
		inputMultiplexer = new InputMultiplexer();
		inputProc = new UserInputProcessor();
		inputMultiplexer.addProcessor(inputProc);
		game.addInputProcessor("GAME", inputMultiplexer);
				
		gameCamera = new OrthographicCamera();
		stageCamera = new OrthographicCamera();
		// TODO this should be reworked and moved into a separate class.
		setupStage();

		setInitialViewport(1f);
//		setInitialViewport(1.5f);
//		trackPlayer(1.4f, false);
		
		accumulator = 0;
		
		numSimSteps = 0;
		maxSimSteps = 5000;
	}
	
	private void setupStage() {
		
		// TODO Read this from config
		float buttonWidthCm = 2;
		float buttonHeightCm = 1;

		ImageButton menuButton = game.createButton("button_menu.png",
												   "button_menu_highlight.png",
												   "GAME",
												   "MAIN_MENU",
												   0, 0,
												   buttonWidthCm,
												   buttonHeightCm);
		
		// ============================= DEBUG =================================
		System.out.println("Button width / height: " 
							+ menuButton.getWidth()
							+ " | " + menuButton.getHeight());
		// ============================= DEBUG =================================
		
		float displayWidth = Gdx.graphics.getWidth();
		float displayHeight = Gdx.graphics.getHeight();
		
		menuButton.setPosition(displayWidth - menuButton.getWidth(),
							   displayHeight - menuButton.getHeight());
		
		// ================================ DEBUG ==============================
		System.out.println("------ Graphics ------");
		System.out.println("Density: " + Gdx.graphics.getDensity());
		System.out.println("Height: " + Gdx.graphics.getHeight());
		System.out.println("Width: " + Gdx.graphics.getWidth());
		System.out.println("Pixels per cm X: " + Gdx.graphics.getPpcX());
		System.out.println("Pixels per cm Y: " + Gdx.graphics.getPpcY());
		System.out.println("Pixels per inch X: " + Gdx.graphics.getPpiX());
		System.out.println("Pixels per inch Y: " + Gdx.graphics.getPpiY());
		System.out.println("----------------------");
		// ================================ DEBUG ==============================
		
		viewport = new ScalingViewport(Scaling.stretch, displayWidth, displayHeight, stageCamera);
//		viewport = new ScalingViewport(Scaling.stretch, 1200, 800, stageCamera);
//		viewport = new FitViewport(640, 480, stageCamera);
//		viewport = new ScreenViewport(640, 480, stageCamera);
		gameStage = new Stage(viewport);
		
		// Game panel (UI) inputs
		inputMultiplexer.addProcessor(gameStage);
		font = new BitmapFont(Gdx.files.internal("Calibri_72.fnt")); 
		font.setScale(0.4f);
		batch = new SpriteBatch();
		
		// TODO debug
//		System.out.println("displayWidth: " + displayWidth);
//		System.out.println("display height: " + displayHeight);
//		System.out.println("menuButtonPosX: " + menuButtonPosX);
//		System.out.println("menuButtonPosY: " + menuButtonPosY);

		gameStage.addActor(menuButton);
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (!gameIsOver) {
			renderer.render(world, gameCamera.combined, delta);
			gameStage.draw();
			
			// TODO
//			System.out.println("ViewportHeight: " + viewport.getViewportHeight());
//			System.out.println("ViewportWidth: " + viewport.getViewportWidth());
//			System.out.println("ViewportX: " + viewport.getViewportX());
//			System.out.println("ViewportY: " + viewport.getViewportY());
//			System.out.println("WorldHeight: " + viewport.getWorldHeight());
//			System.out.println("WorldWidth: " + viewport.getWorldWidth());
			
			// TODO
			// 1. Reuse the batch from the renderer
			// 2. Use a custom font with a skin that's defined in a texture packer.
			//    The default font is Arial-15
			batch.begin();
			font.draw(batch, "Time " + getFormattedTime(), 10, 470);
			font.draw(batch, "Score: ", 10, 450);
			batch.end();
			
			processMoves();
			inputProc.processCameraInputs(gameCamera);

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
				++numSimSteps;
			}

			physProc.postStep(state);
			
//			setViewportJump(5);
//			setViewport(12, 0.5f);
			trackPlayer(1.4f, false);
			checkForGameOver();
		}
		else {
			renderer.render(world, gameCamera.combined, 0);
			gameStage.draw();
			
			// TODO draw a pop-up box / game over screen
		}
		
		gameCamera.update();
		stageCamera.update();
	}
	
	private void setInitialViewport(float factor) {
		Vector2[] mazeBoundaries = getWorldMazeBoundaries();
		Vector2 mazeLL = mazeBoundaries[0];
		Vector2 mazeUR = mazeBoundaries[1];
		
		float mazeWidth = (mazeUR.x - mazeLL.x) * factor;
		float mazeHeight = (mazeUR.y - mazeLL.y) * factor;
	
		widthToHeight = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		
		if (widthToHeight <= 0) {
			gameCamera.viewportWidth = mazeWidth;
			gameCamera.viewportHeight = mazeWidth * widthToHeight;
		} else { // mazeHeight > mazeWidth
			gameCamera.viewportHeight = mazeHeight;
			gameCamera.viewportWidth = mazeHeight / widthToHeight;
		}
		
		gameCamera.position.x = (mazeUR.x + mazeLL.x) / 2;
		gameCamera.position.y = (mazeUR.y + mazeLL.y) / 2;
	}
	
//	private void setViewportJump(float maxSquaresX) {
//		widthToHeight = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
//		
//		gameCamera.viewportWidth = maxSquaresX * physProc.getSquareSize();
//		gameCamera.viewportHeight = gameCamera.viewportWidth * widthToHeight;
//		
//		Vector2[] mazeBoundaries = getWorldMazeBoundaries();
//		Vector2 mazeLL = mazeBoundaries[0];
//		Vector2 mazeUR = mazeBoundaries[1];
//		
//		float mazeWidth = (mazeUR.x - mazeLL.x);
//		float mazeHeight = (mazeUR.y - mazeLL.y);
//		
//		float widthDiff = gameCamera.viewportWidth - mazeWidth;
//		float heightDiff = gameCamera.viewportHeight - mazeHeight;
//		
//		float scale = 0f;
//		
//		if (widthDiff > 0 && heightDiff > 0) {
//			if (widthDiff <= heightDiff) {
//				scale = mazeWidth / gameCamera.viewportWidth;
//				gameCamera.viewportWidth = gameCamera.viewportWidth * scale;
//				gameCamera.viewportHeight = (gameCamera.viewportHeight * scale) * widthToHeight;
//			} else { // heightDiff < widthDiff
//				scale = mazeHeight / gameCamera.viewportHeight;
//				gameCamera.viewportHeight = gameCamera.viewportHeight * scale;
//				gameCamera.viewportWidth = (gameCamera.viewportWidth * scale) / widthToHeight;
//			}
//		}
//		
//	}
	
	private void setViewport(float maxSquaresX, float factor) {
		widthToHeight = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		
		float squareSize = physProc.getSquareSize();
		
		float targetWidth = maxSquaresX * squareSize;
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
		
		newWidth = gameCamera.viewportWidth + (targetWidth - gameCamera.viewportWidth) * delta * factor;
		scale = gameCamera.viewportWidth / newWidth;
		newHeight = (newWidth * scale) * widthToHeight;
		
		if (widthDiff > 0 && heightDiff > 0) {
			if (widthDiff <= heightDiff) {
				newWidth = gameCamera.viewportWidth + (mazeWidth - gameCamera.viewportWidth) * delta * factor;
				scale = gameCamera.viewportWidth / newWidth;
				newHeight = (newWidth * scale) * widthToHeight;
			} else { // heightDiff < widthDiff
				newHeight = gameCamera.viewportHeight + (mazeHeight - gameCamera.viewportHeight) * delta * factor;
				scale = gameCamera.viewportHeight / newHeight;
				newWidth = (newHeight * scale) / widthToHeight;
			}
		}
		
		gameCamera.viewportWidth = newWidth;
		gameCamera.viewportHeight = newHeight;
	}
	
//	private Vector2[] getViewportBoundaries() {
//		float viewportWidthHalf = (gameCamera.viewportWidth / 2);
//		float viewportHeightHalf = (gameCamera.viewportHeight / 2);
//		
//		Vector2 viewportLL = new Vector2(gameCamera.position.x - viewportWidthHalf, gameCamera.position.y - viewportHeightHalf);
//		Vector2 viewportUR = new Vector2(gameCamera.position.x - viewportWidthHalf, gameCamera.position.y - viewportHeightHalf);
//		
//		Vector2[] viewportBoundaries = new Vector2[] {viewportLL, viewportUR};
//		
//		return viewportBoundaries;
//	}
	
	
	private String getFormattedTime() {
		long minutes = elapsedTime / 60000000000l;
		long seconds = elapsedTime / 1000000000l;
		
		return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
	}
	
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
		
		float widthDiff = gameCamera.viewportWidth - mazeWidth;
		float heightDiff = gameCamera.viewportHeight - mazeHeight;
		
		float delta = Gdx.graphics.getDeltaTime(); // Or 0.1;
		float viewportWidthHalf = (gameCamera.viewportWidth / 2);
		float viewportHeightHalf = (gameCamera.viewportHeight / 2);
		
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
				newX = gameCamera.position.x + (playerVector.x - gameCamera.position.x) * delta * factor;
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
				newY = gameCamera.position.y + (playerVector.y - gameCamera.position.y) * delta * factor;
			}
			
			if (newY - viewportHeightHalf < mazeLL.y) {
				newY = mazeLL.y + viewportHeightHalf;
			} else if (newY + viewportHeightHalf > mazeUR.y){
				newY = mazeUR.y - viewportHeightHalf;
			}
		}
		
		gameCamera.position.x = (float) newX;
		gameCamera.position.y = (float) newY;
	}
	
	
	@Override
	public void resize(int width, int height) {
//		setViewportJump(5);
//		trackPlayer(1.4f, true);
		// TODO -> update gameCamera viewport
		gameStage.getViewport().update(width, height);
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
		elapsedTime = TimeUtils.timeSinceNanos(startTime);
		//long gameTime = timeLimit - elapsedTime;
		GameOver gameOver = gameLogic.isGameOver(1);//(int) gameTime);
		if (numSimSteps > maxSimSteps) {
			gameOver = GameOver.Time;
		}
		
		switch (gameOver) {
			case Pills:
			case Time:
			case Prey:
				logResult(gameOver);
				gameIsOver = true;
//				wait(1000);
//				resetGame();
//				game.switchToScreen("MAIN_MENU");
			case No:
			default:
				break;
		}

	}
	
	public void resetGame() {
		numSimSteps = 0;
		game.resetGame();
	}
	
	public void reset(World world, GameLogic gl, PhysicsProcessor physProc) {
		this.world = world;
		this.gameLogic = gl;
		this.physProc = physProc;
	}
	
	private void logResult(GameOver result) {
		GameState gs = gameLogic.getGameState();
		int numPillsRemaining = gs.getPills().size();
		int numSquares = gs.getMaze().getNodes().keySet().size();
		GameResult gr = new GameResult(result, numSimSteps, numPillsRemaining,
				numSquares);
		game.addResult(gr);
	}
	
	private void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

