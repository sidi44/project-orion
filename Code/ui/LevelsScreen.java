package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class LevelsScreen extends MenuScreen {

	public LevelsScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	protected void addActors() {
		
		// Add our background image
		FileHandle file = Gdx.files.internal("data/ui/levels_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		// Create our levels buttons
		Button levelButton1 = createLevelButton("Level 1", 1);
		Button levelButton2 = createLevelButton("Level 2", 2);
		Button levelButton3 = createLevelButton("Level 3", 3);
		Button levelButton4 = createLevelButton("Level 4", 4);
		Button levelButton5 = createLevelButton("Level 5", 5);
		Button levelButton6 = createLevelButton("Level 6", 6);
		
		// Create the button to get back to the main menu
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		// Create a table and layout the buttons
		float pad = 20f;
		Table table = new Table();
		
		table.add(levelButton1).pad(pad);
		table.add(levelButton2).pad(pad);
		table.add(levelButton3).pad(pad);
		table.row();
		
		table.add(levelButton4).pad(pad);
		table.add(levelButton5).pad(pad);
		table.add(levelButton6).pad(pad);
		table.row();
		
		table.add(menuButton).pad(pad).colspan(3);
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
	
	protected Button createLevelButton(String text, final int levelNumber) {

		Button button = new TextButton(text, getSkin());
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager manager = getManager();
				GameScreen screen = (GameScreen) manager.getScreen(ScreenName.Game);
				screen.setGameType(GameType.Levels);
				screen.setLevelNumber(levelNumber);
				manager.changeScreen(ScreenName.Game);
			}
		});
		
		return button;
	}
	
}