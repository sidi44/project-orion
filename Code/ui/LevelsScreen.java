package ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.PredatorPreyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import data.DataManager;
import data.PlayerProgress;

class LevelsScreen extends MenuScreen {

	Map<Integer, LevelButtonPanel> levelButtons;
	
	private static final int PADDING = 20;
	
	private static final int NUM_LEVEL_ROWS = 3;
	private static final int NUM_LEVEL_COLS = 3;
	
	public LevelsScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void initialise() {
		levelButtons = new HashMap<Integer, LevelButtonPanel>();
		super.initialise();
	}

	@Override
	protected void addActors() {
		
		// Add our background image
		FileHandle file = Gdx.files.internal("data/ui/levels_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		// Create our levels buttons
		int numLevels = NUM_LEVEL_ROWS * NUM_LEVEL_COLS;
		for (int i = 1; i <= numLevels; ++i) {
			LevelButtonPanel button = createLevelButtonPanel(i);
			levelButtons.put(i, button);
		}
		
		// Create the button to get back to the main menu
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		// Create a table and layout the buttons
		float pad = 20f;
		Table table = new Table();
		
		for (int i = 0; i < NUM_LEVEL_ROWS; ++i) {
			for (int j = 0; j < NUM_LEVEL_COLS; ++j) {
				int num = i * NUM_LEVEL_COLS + j + 1;
				table.add(levelButtons.get(num)).pad(PADDING).expand();
			}
			table.row();
		}
		
		table.add(menuButton).pad(pad).colspan(NUM_LEVEL_COLS);
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
	
	private LevelButtonPanel createLevelButtonPanel(int levelNumber) {
		
		String text = "Level " + levelNumber;
		Button button = createLevelButton(text, levelNumber);
		LevelButtonPanel panel = new LevelButtonPanel(getSkin(), button);
		
		return panel;
	}
	
	private Button createLevelButton(String text, final int levelNumber) {

		Button button = new TextButton(text, getSkin());
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager manager = getManager();
				manager.getGame().setGameTypeLevel(levelNumber);
				manager.changeScreen(ScreenName.Game);
			}
		});
		
		return button;
	}
	
	@Override
	protected void doShow() {
		updateLevelsLocked();
		updateStars();
	}
	
	private void updateLevelsLocked() {
		
		PredatorPreyGame game = getManager().getGame();
		DataManager dataManager = game.getDataManager();
		PlayerProgress progress = dataManager.getPlayerProgress();
		
		for (Integer levelNumber : levelButtons.keySet()) {
			LevelButtonPanel button = levelButtons.get(levelNumber);
			boolean locked = progress.isLevelLocked(levelNumber);
			button.setLocked(locked);
		}
		
	}
	
	private void updateStars() {
		
		// Grab the data manager
		PredatorPreyGame game = getManager().getGame();
		DataManager dataManager = game.getDataManager();
		
		// Get the player's progress
		PlayerProgress progress = dataManager.getPlayerProgress();
		
		for (Integer levelNumber : levelButtons.keySet()) {
			LevelButtonPanel button = levelButtons.get(levelNumber);
			
			List<Integer> starScores = 
					dataManager.getLevelStarScores(levelNumber);
			
			int score = progress.getLevelScore(levelNumber);
			
			if (starScores.size() != 3) {
				button.setNotComplete();
			} else if (score >= starScores.get(2)) {
				button.setCompleteGold();
			} else if (score >= starScores.get(1)) {
				button.setCompleteSilver();
			} else if (score >= starScores.get(0)) {
				button.setCompleteBronze();
			} else {
				button.setNotComplete();
			}
			
		}
		
		
		
	}
	
}
