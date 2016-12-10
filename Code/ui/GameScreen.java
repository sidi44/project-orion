package ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import game.PredatorPreyGame;
import input.UserInputProcessor;
import logic.GameOver;
import logic.GameState;
import logic.Predator;
import logic.powerup.PowerUp;

class GameScreen extends MenuScreen {
	
	private final CameraManager cameraManager;
	private final UserInputProcessor inputProc;
	
	private Label scoreLabel;
	private Label timeLabel;
	private List<PowerUpButton> powerUpButtons;
	
	private final static int NUM_POWER_UP_BUTTONS = 5;
	
	public GameScreen(ScreenManager manager) {
		super(manager);
		
		Camera gameCamera = new OrthographicCamera();
		cameraManager = new CameraManager(gameCamera, manager.getGame());
		
		this.inputProc = new UserInputProcessor(cameraManager, manager.getGame());
	}
	
	@Override
	protected void addInputProcessor(InputMultiplexer multiplexer) {
		multiplexer.addProcessor(inputProc);
		multiplexer.addProcessor(new GestureDetector(inputProc));
	}
	
	@Override
	protected void addActors() {
		
		// Create pause button
        Button pauseButton = new ImageButton(getSkin(), "pause_button");
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().changeScreen(ScreenName.Pause);
            }
        });

        // Work out the size of the pause button based on the screen size
        final float pauseButtonScale = 0.1f;
        final int pauseButtonSize = getScaledSmallerScreenDimension(pauseButtonScale);

		// Create the Score and Time remaining text fields
		scoreLabel = new Label("Score: ", getSkin());
		timeLabel = new Label("Time remaining: ", getSkin());

		// Create the Power Up buttons
		powerUpButtons = new ArrayList<PowerUpButton>();
		for (int i = 0; i < NUM_POWER_UP_BUTTONS; ++i) {
			final int index = i;
			PowerUpButton button = new PowerUpButton(getSkin());
			powerUpButtons.add(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					inputProc.setUsePowerUpIndex(index);
				}
			});
		}
		
		// Create and setup our table. The UI has two 'panes'; the info pane 
		// which contains the score, time remaining and menu button, and the 
		// button pane which contains the power up buttons.
		
		// Pad our widgets a bit
		float pad = 10f;
		
		// First create the info pane. The time remaining label is expanded in
		// the X direction so the pane will fill the width of the screen. 
		Table infoPane = new Table();
		infoPane.add(timeLabel).pad(pad).left().expandX();
		infoPane.add(pauseButton).size(pauseButtonSize).pad(pad).right();
		infoPane.row();
		infoPane.add(scoreLabel).pad(pad).left();

		// Next the power up button pane.
		Table buttonPane = new Table();
		float dim = 40f;
		for (PowerUpButton button : powerUpButtons) {
			buttonPane.add(button).pad(pad).width(dim).height(dim);
			buttonPane.row();
		}
		
		// Now add the two panes to the main table. Make sure the info pane 
		// fills the full width of the table. The button pane should expand to 
		// fill the remaining space in both directions, but the widgets it holds
		// should be aligned to the left.
		Table table = new Table();
		table.add(infoPane).fillX();
		table.row();
		table.add(buttonPane).expand().left();
		
		// Add the table to our UI stage
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
	@Override
	protected void doShow() {
		// Set up the intial view
		cameraManager.setInitialViewport();
		
		// Make sure the right number of power up buttons are visible
		Predator predator = getPredator();
		int numPowerUps = predator.getMaxPowerUp(); 
		for (int i = 0; i < NUM_POWER_UP_BUTTONS; ++i) {
			PowerUpButton button = powerUpButtons.get(i);
			boolean visible = (i < numPowerUps);
			button.setVisible(visible);
		}
	}
	
	@Override
	protected void doRender(float delta) {
		
		PredatorPreyGame game = getManager().getGame();
		GameState state = game.getGameLogic().getGameState();
		
		int score = state.getScore();
		String scoreText = "Score : " + score;
		scoreLabel.setText(scoreText);
		
		int timeRemaining = (int) state.getTimeRemaining();
		String timeRemainingText = "Time remaining : " + timeRemaining + "s";
		timeLabel.setText(timeRemainingText);
		
		Predator predator = getPredator();
		int numPowerUps = predator.getMaxPowerUp();
		for (int i = 0; i < NUM_POWER_UP_BUTTONS; ++i) {
			if (i < numPowerUps) {
				PowerUp powerUp = predator.getStoredPowerUp(i);
				powerUpButtons.get(i).setPowerUp(powerUp);
			}
		}
		
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
		
		// Clear any stored user input
		inputProc.reset();
		
		// Grab the game from the screen manager
		PredatorPreyGame game = getManager().getGame();
		
		// Tell the game its finished and why
		game.gameOver(reason);
	}
	
	/**
	 * Convenience method to get the game's predator. 
	 * This assumes there is exactly one predator.
	 * 
	 * @return the game's predator.
	 */
	private Predator getPredator() {
		PredatorPreyGame game = getManager().getGame();
		GameState state = game.getGameLogic().getGameState();
		List<Predator> predators = state.getPredators();
		return predators.get(0);
	}

}

