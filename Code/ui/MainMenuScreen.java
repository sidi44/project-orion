package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class MainMenuScreen extends MenuScreen {
	
	private final int VIRTUAL_WIDTH = 640;
	private final int VIRTUAL_HEIGHT = 480;
	
	public MainMenuScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected Viewport getScreenViewport() {
		
		Camera camera = new OrthographicCamera();
		Viewport viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		viewport.apply(true);
		return viewport;
	}
	
	@Override
	protected void addActors() {
		
		// Add our background image
		FileHandle file = Gdx.files.internal("data/ui/menu_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		// Create our buttons
		Button levelsButton = createScreenChangeButton(
				"Levels", ScreenName.Levels);
		Button sandboxButton = createScreenChangeButton(
				"Sandbox", ScreenName.Sandbox);
		Button settingsButton = createScreenChangeButton(
				"Settings", ScreenName.Settings);
		
		// Create a table and layout the buttons
		Table table = new Table();
		Cell<Button> levelsCell = table.add(levelsButton);
		levelsCell.pad(20f);
		table.row();
		
		Cell<Button> sandboxCell = table.add(sandboxButton);
		sandboxCell.pad(20f);
		table.row();
		
		Cell<Button> settingsCell = table.add(settingsButton);
		settingsCell.pad(20f);
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
}