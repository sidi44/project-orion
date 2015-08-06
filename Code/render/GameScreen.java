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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class GameScreen implements Screen {

	private final PredatorPreyGame game;
	private final World world;
	private final OrthographicCamera camera;
	private final GameLogic gameLogic;
	private final Renderer renderer;
	private final PhysicsProcessor physProc;
	private final UserInputProcessor inputProc;
	private Stage gameStage;
	
	// Gameplay fields
	private final static long nanoToSeconds = 1000000000;
	private float timestep;
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
		camera.position.x = 35;
		camera.position.y = 35;
		camera.update();
	}
	
	private void setupStage() {
		
		// Game panel (UI) inputs
		gameStage = new Stage();
		game.addInputProcessor(gameStage);
		
		Texture buttonTexture = new Texture(Gdx.files.internal("button_previous.png"));
		Sprite buttonSprite = new Sprite(buttonTexture);
		SpriteDrawable buttonDrawable = new SpriteDrawable(buttonSprite);
		ImageButton button = new ImageButton(buttonDrawable);
		button.setPosition(200, 300);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.switchToScreen("MAIN_MENU");
				System.out.println("clicked - game");
			}
		});

		gameStage.addActor(button);
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
		timestep = Gdx.graphics.getDeltaTime();
		physProc.processGameState(state, timestep);

		checkForGameOver();
		
		// TODO
//		if (inputToSwitch) {
//			game.switchToScreen("MAIN_MENU");
//		}
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