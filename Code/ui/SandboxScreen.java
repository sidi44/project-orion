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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import data.DataManager;
import data.SandboxConfiguration;
import functional.IntConsumer;

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

		List<SliderPanel> sliderPanels = new ArrayList<SliderPanel>();

		final float sliderPanelPadding = 5f;

		// Create the maze width slider
        final Label widthSliderValueLabel = new Label(Integer.toString(sandboxConfig.getMazeWidth()), getSkin());
        final IntConsumer widthFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setMazeWidth(value);
                widthSliderValueLabel.setText(Integer.toString(value));
            }
        };

        sliderPanels.add(createIntSliderPanel("Width",
                                              widthSliderValueLabel,
                                              sandboxConfig.getMazeWidth(),
                                              sliderPanelPadding,
                                              widthFunc));

		// Create the maze height slider
        final Label heightSliderValueLabel = new Label(Integer.toString(sandboxConfig.getMazeHeight()), getSkin());
		final IntConsumer heightFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				sandboxConfig.setMazeHeight(value);
				heightSliderValueLabel.setText(Integer.toString(value));
			}
		};

		sliderPanels.add(createIntSliderPanel("Height",
		                                      heightSliderValueLabel,
                            		          sandboxConfig.getMazeHeight(),
                            		          sliderPanelPadding,
                            		          heightFunc));



		// Create the predator speed slider
        final Label predatorSpeedSliderValueLabel = new Label(Integer.toString(sandboxConfig.getPredatorSpeedIndex()), getSkin());
        final IntConsumer predatorSpeedFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setPredatorSpeedIndex(value);
                predatorSpeedSliderValueLabel.setText(Integer.toString(value));
            }
        };

        sliderPanels.add(createIntSliderPanel("Predator Speed",
                                              predatorSpeedSliderValueLabel,
                                              sandboxConfig.getPredatorSpeedIndex(),
                                              sliderPanelPadding,
                                              predatorSpeedFunc));


		// Create the prey speed slider
        final Label preySpeedSliderValueLabel = new Label(Integer.toString(sandboxConfig.getPreySpeedIndex()), getSkin());
        final IntConsumer preySpeedFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setPreySpeedIndex(value);
                preySpeedSliderValueLabel.setText(Integer.toString(value));
            }
        };

        sliderPanels.add(createIntSliderPanel("Prey Speed",
                                              preySpeedSliderValueLabel,
                                              sandboxConfig.getPreySpeedIndex(),
                                              sliderPanelPadding,
                                              preySpeedFunc));


		// Create the number of prey slider
        final Label numPreySliderValueLabel = new Label(Integer.toString(sandboxConfig.getNumPrey()), getSkin());
        final IntConsumer numPreySpeedFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                sandboxConfig.setNumPrey(value);
                numPreySliderValueLabel.setText(Integer.toString(value));
            }
        };

        sliderPanels.add(createIntSliderPanel("Num prey",
                                              numPreySliderValueLabel,
                                              sandboxConfig.getNumPrey(),
                                              sliderPanelPadding,
                                              numPreySpeedFunc));


		// Create the game button
		Button gameButton = new TextButton("Play", getSkin());
		gameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveData();
				ScreenManager manager = getManager();
				manager.getGame().setGameTypeSandbox();
				manager.changeScreen(ScreenName.Game);
			}
		});
		
		// Create the menu button
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		// Set out the widgets using a table
		Table table = new Table();
		float pad = 10f;

		for (SliderPanel panel : sliderPanels) {
		    table.add(panel).colspan(4).pad(pad);
		    table.row();
		}

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
