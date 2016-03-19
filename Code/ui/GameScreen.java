package ui;

import game.GameType;
import game.PredatorPreyGame;
import input.UserInputProcessor;
import logic.GameOver;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class GameScreen extends MenuScreen {
	
	private final CameraManager cameraManager;
	private final UserInputProcessor inputProc;
	
	public GameScreen(ScreenManager manager) {
		super(manager);
		
		this.inputProc = new UserInputProcessor();
		
		Camera gameCamera = new OrthographicCamera();
		cameraManager = new CameraManager(gameCamera, manager.getGame());
	}
	
	@Override
	protected void addInputProcessor(InputMultiplexer multiplexer) {
		multiplexer.addProcessor(inputProc);
	}
	
	@Override
	protected void addActors() {
		
		// Create our main menu button
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
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
		
		// Work out which screen we should change to depending on the game type
		GameType type = game.getGameType();
		ScreenName name = ScreenName.MainMenu;
		switch (type) {
			case Levels:
				name = ScreenName.Levels;
				break;
			case Sandbox:
				name = ScreenName.Sandbox;
				break;			
		}
		getManager().changeScreen(name);
		
		// Tell the game its finished and why
		game.gameOver(reason);
	}

}

