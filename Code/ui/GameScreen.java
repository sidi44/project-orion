package ui;

import game.PredatorPreyGame;
import input.UserInputProcessor;
import logic.GameOver;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class GameScreen extends MenuScreen {
	
	private final CameraManager cameraManager;
	private final UserInputProcessor inputProc;
	
	public GameScreen(ScreenManager manager) {
		super(manager);
		
		Camera gameCamera = new OrthographicCamera();
		cameraManager = new CameraManager(gameCamera, manager.getGame());
		
		this.inputProc = new UserInputProcessor(cameraManager);
	}
	
	@Override
	protected void addInputProcessor(InputMultiplexer multiplexer) {
		multiplexer.addProcessor(inputProc);
		multiplexer.addProcessor(new GestureDetector(inputProc));
	}
	
	@Override
	protected void addActors() {
		
		// Create our main menu button
		Button menuButton = new TextButton("Main menu", getSkin());
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameFinished(GameOver.Exit);
			}
		});
		
		// Create the Score and Time remaining text fields
		Label timeLabel = new Label("Time remaining: ", getSkin());
		Label scoreLabel = new Label("Score: ", getSkin());
		
		// Create and setup our table
		float pad = 10f;
		Table table = new Table();
		
		table.add(timeLabel).pad(pad).expandX().left();
		table.add(menuButton).pad(pad).expandX().right();
		
		table.row();
		
		table.add(scoreLabel).pad(pad).expandX().left();
		
		table.top();
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
	@Override
	protected void doShow() {
		// Set up the intial view
		cameraManager.setInitialViewport();
	}
	
	@Override
	protected void doRender(float delta) {

		PredatorPreyGame game = getManager().getGame();
		
		Camera gameCamera = cameraManager.getCamera();
		game.getRenderer().render(game.getWorld(), gameCamera.combined);
		inputProc.processCameraInputs(gameCamera);

		GameOver gameOver = game.update(delta, inputProc.getNextMove());
		if (gameOver != GameOver.No) {
			gameFinished(gameOver);
		}
		cameraManager.update();
	}
	
	private void gameFinished(GameOver reason) {
		
		// Grab the game from the screen manager
		PredatorPreyGame game = getManager().getGame();
		
		// Tell the game its finished and why
		game.gameOver(reason);
	}

}
