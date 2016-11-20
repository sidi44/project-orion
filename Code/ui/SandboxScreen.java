package ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import data.DataManager;
import data.SandboxConfiguration;
import functional.IntConsumer;

class SandboxScreen extends MenuScreen {
	
	private SandboxConfiguration sandboxConfig;
	
	private final float SLIDER_PADDING = 5f;
	private final float TABLE_PADDING = 10f;
	
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

		// Convenience list to store all the slider panels
		List<SliderPanel> sliderPanels = new ArrayList<SliderPanel>();
		
		// Create the maze width slider
		int width = sandboxConfig.getMazeWidth();
        final IntConsumer widthFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setMazeWidth(value);
            }
        };
        Slider widthSlider = createIntSlider(4, 30, 1, width, widthFunc);
        sliderPanels.add(createIntSliderPanel(widthSlider, 
        									  "Width",
        									  SLIDER_PADDING));

		// Create the maze height slider
        int height = sandboxConfig.getMazeHeight();
		final IntConsumer heightFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				sandboxConfig.setMazeHeight(value);
			}
		};
        Slider heightSlider = createIntSlider(4, 30, 1, height, heightFunc);
        sliderPanels.add(createIntSliderPanel(heightSlider, 
											  "Height",
											  SLIDER_PADDING));

		// Create the predator speed slider
        int predatorSpeed = sandboxConfig.getPredatorSpeedIndex();
        final IntConsumer predatorSpeedFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setPredatorSpeedIndex(value);
            }
        };
        Slider predatorSpeedSlider = createIntSlider(1, 10, 1, predatorSpeed, predatorSpeedFunc);
        sliderPanels.add(createIntSliderPanel(predatorSpeedSlider,
        									  "Predator Speed",
        									  SLIDER_PADDING));


		// Create the prey speed slider
        int preySpeed = sandboxConfig.getPreySpeedIndex();
        final IntConsumer preySpeedFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setPreySpeedIndex(value);
            }
        };
        Slider preySpeedSlider = createIntSlider(1, 10, 1, preySpeed, preySpeedFunc);
        sliderPanels.add(createIntSliderPanel(preySpeedSlider, 
        									  "Prey Speed",
        									  SLIDER_PADDING));


		// Create the number of prey slider
        int numPrey = sandboxConfig.getNumPrey();
        final IntConsumer numPreyFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setNumPrey(value);
            }
        };
        Slider numPreySlider = createIntSlider(1, 10, 1, numPrey, numPreyFunc);
        sliderPanels.add(createIntSliderPanel(numPreySlider, 
        								      "Num prey",
        								      SLIDER_PADDING));


		// Create the game button
		Button gameButton = new TextButton("Play", getSkin());
		gameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveData();
				ScreenManager manager = getManager();
				manager.getGame().setGameTypeSandbox();
				manager.changeScreen(ScreenName.Loading);
			}
		});
		
		// Create the menu button
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		// Set out the widgets using a table
		Table table = new Table();

		for (SliderPanel panel : sliderPanels) {
		    table.add(panel).colspan(4).pad(TABLE_PADDING).right();
		    table.row();
		}

		Cell<Button> gameCell = table.add(gameButton);
		gameCell.pad(TABLE_PADDING);
		Cell<Button> menuCell = table.add(menuButton);
		menuCell.pad(TABLE_PADDING);
		
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
