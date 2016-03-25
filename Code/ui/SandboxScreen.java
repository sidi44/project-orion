package ui;

import java.util.function.IntConsumer;

import game.PredatorPreyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import data.DataManager;
import data.SandboxConfiguration;

class SandboxScreen extends MenuScreen {
	
	private SandboxConfiguration sandboxConfig;
	
	public SandboxScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override 
	protected void initialise() {
		// This is called before addActors() and we must make sure the 
		// sandboxConfig is initialised before that method is called, so do 
		// that now.
		DataManager dataManager = getManager().getGame().getDataManager();
		sandboxConfig = dataManager.getSandboxConfig();
		
		super.initialise();
	}

	@Override
	protected void addActors() {
		
		// Get out background image and add it to the stage
		FileHandle file = Gdx.files.internal("data/ui/sandbox_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		// Create the maze width slider
		int width = sandboxConfig.getMazeWidth();
		IntConsumer widthFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				sandboxConfig.setMazeWidth(value);
			}
		};
		Slider widthSlider = createIntSlider(4, 30, 1, width, widthFunc);
		
		// Create the maze height slider
		int height = sandboxConfig.getMazeHeight();
		IntConsumer heightFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				sandboxConfig.setMazeHeight(value);
			}
		};
		Slider heightSlider = createIntSlider(4, 30, 1, height, heightFunc);
		
		// Create the predator speed slider
		int predatorSpeed = sandboxConfig.getPredatorSpeedIndex();
		IntConsumer predatorSpeedFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				sandboxConfig.setPredatorSpeedIndex(value);
			}
		};
		Slider predatorSpeedSlider = 
				createIntSlider(1, 10, 1, predatorSpeed, predatorSpeedFunc);
		
		// Create the prey speed slider
		int preySpeed = sandboxConfig.getPreySpeedIndex();
		IntConsumer preySpeedFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				sandboxConfig.setPreySpeedIndex(value);
			}
		};
		Slider preySpeedSlider = 
				createIntSlider(1, 10, 1, preySpeed, preySpeedFunc);
		
		// Create the number of prey slider
		int numPrey = sandboxConfig.getNumPrey();
		IntConsumer numPreyFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				sandboxConfig.setNumPrey(value);
			}
		};
		Slider numPreySlider = createIntSlider(1, 10, 1, numPrey, numPreyFunc);
		
		// Create the game button
		Button gameButton = createScreenChangeButton("Play", ScreenName.Game);
		gameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveData();
				PredatorPreyGame game = getManager().getGame();
				game.setGameTypeSandbox();
			}
		});
		
		// Create the menu button
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		// Set out the widgets using a table
		Table table = new Table();
		float pad = 20f;
		
		Cell<Slider> widthSliderCell = table.add(widthSlider);
		widthSliderCell.pad(pad);
		widthSliderCell.colspan(2);
		table.row();
		
		Cell<Slider> heightSliderCell = table.add(heightSlider);
		heightSliderCell.pad(pad);
		heightSliderCell.colspan(2);
		table.row();
		
		Cell<Slider> predatorSpeedSliderCell = table.add(predatorSpeedSlider);
		predatorSpeedSliderCell.pad(pad);
		predatorSpeedSliderCell.colspan(2);
		table.row();
		
		Cell<Slider> preySpeedSliderCell = table.add(preySpeedSlider);
		preySpeedSliderCell.pad(pad);
		preySpeedSliderCell.colspan(2);
		table.row();
		
		Cell<Slider> numPreySliderCell = table.add(numPreySlider);
		numPreySliderCell.pad(pad);
		numPreySliderCell.colspan(2);
		table.row();

		Cell<Button> gameCell = table.add(gameButton);
		gameCell.pad(pad);
		Cell<Button> menuCell = table.add(menuButton);
		menuCell.pad(pad);
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
	@Override
	protected void doHide() {
		saveData();
	}
	
	private void saveData() {
		ScreenManager manager = getManager();
		DataManager dataManager = manager.getGame().getDataManager();
		dataManager.saveSandboxData(sandboxConfig);
	}
}
