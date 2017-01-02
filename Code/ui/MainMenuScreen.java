package ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import game.PredatorPreyGame;
import logic.GameOver;
import logic.Move;

class MainMenuScreen extends MenuScreen {
	
	private MainMenuGameViewport viewport;
	
	private Label title;
	private HorizontalGroup buttons;
	
	private final static String GAME_TITLE = "STINGRAY!";
	
	public MainMenuScreen(ScreenManager manager) {
		super(manager);
	
		// Initialise our viewport now so it's not null, but this should get
		// called again when the screen is shown so the right maze dimensions 
		// are picked up
		initialiseViewport();
	}
	
	private void initialiseViewport() {
		
		// Work out the maze dimensions, we want to show the full maze in our
		// viewport
		PredatorPreyGame game = getManager().getGame();
		Vector2 min = game.getMazeMinimumPointWorld();
		Vector2 max = game.getMazeMaximumPointWorld();
		int worldWidth = Math.round(max.x - min.x);
		int worldHeight = Math.round(max.y - min.y);
		
		// Create the game camera and construct our game viewport 
		Camera camera = new OrthographicCamera();
		viewport = new MainMenuGameViewport(worldWidth, worldHeight, camera);
	}
	
	@Override
	protected void addActors() {
		
		// Create the game title label
		title = new Label(GAME_TITLE, getSkin());
		title.setFontScale(1.5f);
		
		// Create our buttons
		Button levelsButton = createScreenChangeButton(
				"Levels", ScreenName.Levels);
		Button sandboxButton = createScreenChangeButton(
				"Sandbox", ScreenName.Sandbox);
		
		// Create the Settings button. It needs to tell the Settings screen 
		// that this is the screen to return to afterwards, as well as changing
		// the screen
		Button settingsButton = new TextButton("Settings", getSkin());
		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
		    	SettingsScreen settingsScreen = 
		    			(SettingsScreen) getManager().getScreen(ScreenName.Settings);
		    	settingsScreen.setPreviousScreen(ScreenName.MainMenu);
				getManager().changeScreen(ScreenName.Settings);
			}
		});
		
		// Create a table and layout the buttons
		Table table = new Table();
		
		table.add(title).pad(20f).top().expand();
		table.row();
		
		buttons = new HorizontalGroup();
		buttons.addActor(levelsButton);
		buttons.addActor(sandboxButton);
		buttons.addActor(settingsButton);
		buttons.space(20f);
		table.add(buttons).pad(20f).bottom().expand();
		
		table.setFillParent(true);
		table.setDebug(false);
		getUIStage().addActor(table);
	}
	
	@Override
	protected void doShow() {
		
		// Set up the game for the main menu
		PredatorPreyGame game = getManager().getGame();
		game.setGameTypeMainMenu();
		
		// Re-initialise our viewport to make sure it's using the right maze
		// dimensions
		initialiseViewport();
		
		// Carry out any base class actions
		super.doShow();
	}
	
	@Override
	protected void doRender(float delta) {

		// Render the game
		viewport.apply();
		PredatorPreyGame game = getManager().getGame();
		Camera gameCamera = viewport.getCamera();
		game.getRenderer().render(game.getWorld(), gameCamera.combined);

		// Check whether the game is finished and if so, inform the game
		GameOver reason = game.update(delta, new Move());
		if (reason != GameOver.No) {
			game.gameOver(reason);
		}
		
		// Let the base class to do any rendering
		super.doRender(delta);
	}
	
	@Override 
	protected void doResize(int width, int height) {
		
		// Get the base class to sort out the UI elements
		super.doResize(width, height);
		
		// Normally, draw() doesn't need to be called until the next render(), 
		// but we need the title and buttons to be re-laid out following the 
		// resize so that the viewport can be positioned correctly, so do a 
		// draw() now.
		getUIStage().getViewport().apply();
		getUIStage().draw();
		
		// Get the y-limits by which the viewport is bounded 
		int minY = Math.round(buttons.getTop());
		int maxY = Math.round(title.getY());
		
		// Update our game viewport
		viewport.update(width, height, minY, maxY);
	}
	
}
