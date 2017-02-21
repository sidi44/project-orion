package ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import game.GameType;
import game.PredatorPreyGame;
import input.UserInputProcessor;
import logic.GameOverReason;
import logic.GameState;
import logic.Predator;
import logic.powerup.PowerUp;

class GameScreen extends MenuScreen {
	
	private final GameViewport viewport;
	private final UserInputProcessor inputProc;
	
	private Label scoreLabel;
	private Label timeLabel;
	private List<PowerUpButton> powerUpButtons;
	
	private GamePausedDialog pausedDialog;
	private GameWonDialog wonDialog;
	private GameLostDialog lostDialog;
	
	private final static int NUM_POWER_UP_BUTTONS = 5;

	// The desired dimensions of our viewport in world coordinates. This is 
	// ideally how much of the world we see. What we actually see depends on 
	// the screen dimensions and the viewport's scaling strategy - see the 
	// GameViewport class.
	private final static int VIEWPORT_WIDTH_WORLD = 90;
	private final static int VIEWPORT_HEIGHT_WORLD = 90;
	
	public GameScreen(ScreenManager manager) {
		super(manager);
		
		// Create our camera and setup the game viewport
		Camera gameCamera = new OrthographicCamera();
		viewport = new GameViewport(VIEWPORT_WIDTH_WORLD, 
									VIEWPORT_HEIGHT_WORLD, 
									gameCamera, 
									manager.getGame());
		
		// Create the input processor
		inputProc = new UserInputProcessor();
		
		// Add the input processors
		addInputProcessor(inputProc);
		addInputProcessor(new GestureDetector(inputProc));
		
		// Create our dialogs
		pausedDialog = new GamePausedDialog(getSkin(), manager);
		wonDialog = new GameWonDialog(getSkin(), manager);
		lostDialog = new GameLostDialog(getSkin(), manager);
	}
	
	@Override
	protected void addActors() {
		
		// Create pause button
        Button pauseButton = new ImageButton(getSkin(), "pause_button");
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().getGame().pauseGame();
                pausedDialog.show(getUIStage());
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
		getUIStage().addActor(table);
	}
	
	@Override
	protected void doShow() {

		// Reset the input processor to clear any cached input
		inputProc.reset();
		
		// Make sure the right number of power up buttons are visible
		Predator predator = getPredator();
		int numPowerUps = predator.getMaxPowerUp(); 
		for (int i = 0; i < NUM_POWER_UP_BUTTONS; ++i) {
			PowerUpButton button = powerUpButtons.get(i);
			boolean visible = (i < numPowerUps);
			button.setVisible(visible);
		}
		
		// Should the pause dialog be raised?
		if (!getManager().getGame().isGameRunning()) {
			pausedDialog.show(getUIStage());
		}
		
		// Call the base class version
		super.doShow();
	}
	
	@Override
	protected void doRender(float delta) {
		
		// Get gold of the game and the game state
		PredatorPreyGame game = getManager().getGame();
		GameState state = game.getGameLogic().getGameState();
		
		// Update the current score
		int score = state.getScore();
		String scoreText = "Score : " + score;
		scoreLabel.setText(scoreText);
		
		// Update the time remaining
		int timeRemaining = (int) state.getTimeRemaining();
		String timeRemainingText = "Time remaining : " + timeRemaining + "s";
		timeLabel.setText(timeRemainingText);
		
		// Update the power up buttons
		Predator predator = getPredator();
		int numPowerUps = predator.getMaxPowerUp();
		for (int i = 0; i < NUM_POWER_UP_BUTTONS; ++i) {
			if (i < numPowerUps) {
				PowerUp powerUp = predator.getStoredPowerUp(i);
				powerUpButtons.get(i).setPowerUp(powerUp);
			}
		}
		
		// Render the game 
		viewport.apply();
		Camera gameCamera = viewport.getCamera();
		game.getRenderer().render(game.getWorld(), gameCamera.combined);
		
		// Update the camera's position, smoothly following the player
		viewport.updateCameraPosition(false);		
		
		// Only update the game if it's running
		if (game.isGameRunning()) {
			// Update the game
			GameOverReason reason = game.update(delta, inputProc.getNextMove());
			
			// Examine whether the game over and take any appropriate action if
			// it is
			checkGameOver(reason);
		}
		
		// Let the base class do any rendering
		super.doRender(delta);
	}
	
	@Override
	protected void doResize(int width, int height) {
		
		// Inform the viewport of the resize
		viewport.update(width, height);
		
		// Ask the viewport to move the camera position to the player, jumping 
		// directly to the player's position without smoothing
		viewport.updateCameraPosition(true);
		
		// Let the base class do any resize work
		super.doResize(width, height);
	}
	
	private void gameFinished() {
		
		// Clear any stored user input
		inputProc.reset();
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
	
	private void checkGameOver(GameOverReason gameOverReason) {
		
		switch (gameOverReason) {
			case NotFinished:
				// Game still running, don't do anything
				break;
			case PredatorWon:
				// The predator has won
				// If this is a level, set the stars complete on the won dialog
        		PredatorPreyGame game = getManager().getGame();
        		if (game.getGameType() == GameType.Levels) {
	        		int levelNumber = game.getLevelNumber();
	        		GameState state = game.getGameLogic().getGameState();
	        		int score = state.getScore();
	        		setStarsComplete(wonDialog.getStarPanel(), 
	        						 levelNumber, 
	        						 score);
        		}
        		
        		// Show the won dialog
				wonDialog.show(getUIStage());
				gameFinished();
				break;
			case PreyWon_Pills:
			case PreyWon_Timeout:
				// The predator has lost, show the game lost dialog
				lostDialog.show(getUIStage());
				gameFinished();
				break;
			default:
				System.err.println("Unknown GameOver state.");
				break;
		
		}
		
	}

}

