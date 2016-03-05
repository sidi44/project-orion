package ui;

import game.PredatorPreyGame;
import input.UserInputProcessor;

import logic.GameOver;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameScreen extends MenuScreen {
	
	private PredatorPreyGame game;
	
	private final CameraManager cameraManager;
	
	private final UserInputProcessor inputProc;
	
	private GameType gameType;
	private int levelNumber;
	
	public GameScreen(ScreenManager manager, PredatorPreyGame game) {
		super(manager);
		
		this.game = game;
		
		this.inputProc = new UserInputProcessor();
		
		Camera gameCamera = new OrthographicCamera();
		cameraManager = new CameraManager(gameCamera, game);
		
		gameType = GameType.Sandbox;
		levelNumber = 1;
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
	
	public void setGameType(GameType type) {
		gameType = type;
	}
	
	public void setLevelNumber(int number) {
		levelNumber = number;
	}
	
	@Override
	protected void doShow() {
		// Set up the intial view
		cameraManager.setInitialViewport();
	}
	
	@Override
	protected void doRender(float delta) {

		Camera gameCamera = cameraManager.getCamera();
		game.getRenderer().render(game.getWorld(), gameCamera.combined);
		inputProc.processCameraInputs(gameCamera);

		GameOver gameOver = game.update(delta, inputProc.getNextMove());
		
		cameraManager.update();
		
		if (gameOver != GameOver.No) {
			getManager().changeScreen(ScreenName.MainMenu);
			game.resetGame();
		}
	}

}
