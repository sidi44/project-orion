package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	
	private GameViewport viewport;
	
	private Label title;
	private HorizontalGroup buttons;
	
	private final static float VIEWPORT_HEIGHT_FACTOR = 0.7f;
	private final static float VIEWPORT_WIDTH_FACTOR = 0.6f;
	
	private final static String GAME_TITLE = "STINGRAY!";
	private final static int VIEWPORT_MAX_SQUARES_X = 8;
	
	public MainMenuScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	public void initialise() {
		Camera camera = new OrthographicCamera();
		viewport = new GameViewport(camera, 
									getManager().getGame(), 
									VIEWPORT_MAX_SQUARES_X);
		super.initialise();
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
		
		// This is a bit unusual, but we need to have the UI widgets positioned
		// in order to position the game viewport in between them, so get the
		// UI stage to draw itself now.
		getUIStage().draw();
		
		// Position the game viewport
		positionViewport();
		
		// Carry out any base class actions
		super.doShow();
	}
	
	@Override
	protected void doRender(float delta) {

		viewport.apply();
		
		PredatorPreyGame game = getManager().getGame();
		
		Camera gameCamera = viewport.getCamera();
		game.getRenderer().render(game.getWorld(), gameCamera.combined);

		GameOver reason = game.update(delta, new Move());
		if (reason != GameOver.No) {
			game.gameOver(reason);
		}
		
		int viewportWidth = calculateViewportWidth();
		int viewportHeight = calculateViewportHeight();
		viewport.update(viewportWidth, viewportHeight);
		
		super.doRender(delta);
	}
	
	@Override 
	protected void doResize(int width, int height) {
		
		int viewportWidth = calculateViewportWidth();
		int viewportHeight = calculateViewportHeight();
		viewport.update(viewportWidth, viewportHeight);
		
		positionViewport();
		
		super.doResize(width, height);
	}
	
	private void positionViewport() {

		int availableHeight = getViewportAvailableHeight();
		int viewportHeight = calculateViewportHeight();
		float heightOffset = availableHeight / 2.0f - viewportHeight / 2.0f;
		float buttonsTop = buttons.getTop();
		int screenY = Math.round(buttonsTop + heightOffset);
		
		int availableWidth = getViewportAvailableWidth();
		int viewportWidth = calculateViewportWidth();
		float widthOffset = availableWidth / 2.0f - viewportWidth / 2.0f;
		int screenX = Math.round(widthOffset);
		
		viewport.setScreenPosition(screenX, screenY);
	}
	
	private int calculateViewportHeight() {
		int availableHeight = getViewportAvailableHeight();
		int viewportHeight = (int) (VIEWPORT_HEIGHT_FACTOR * availableHeight);
		return viewportHeight;
	}
	
	private int calculateViewportWidth() {
		int availableWidth = getViewportAvailableWidth();
		int viewportWidth = (int) (VIEWPORT_WIDTH_FACTOR * availableWidth);
		return viewportWidth;
	}
	
	private int getViewportAvailableHeight() {
		float titleBottom = title.getY();
		float buttonsTop = buttons.getTop();
		float availableHeight = titleBottom - buttonsTop;
		return (int) availableHeight;
	}
	
	private int getViewportAvailableWidth() {
		int clientWidth = Gdx.graphics.getWidth();
		return clientWidth;
	}
}
