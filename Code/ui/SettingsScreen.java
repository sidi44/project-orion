package ui;

import java.util.ArrayList;
import java.util.List;

import sound.SoundConfiguration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import data.DataManager;
import functional.Consumer;
import functional.IntConsumer;
import game.PredatorPreyGame;

class SettingsScreen extends MenuScreen {
	
	SoundConfiguration soundConfig;
	
	public SettingsScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
    protected void initialise() {
		// This is called before addActors() and we must make sure the
		// soundConfig is initialised before that method is called, so do
		// that now.
		DataManager dataManager = getManager().getGame().getDataManager();
		soundConfig = dataManager.getSoundConfiguration();
		
		super.initialise();
	}
	
	@Override
	protected void addActors() {
		
		FileHandle file = Gdx.files.internal("data/ui/settings_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		boolean soundOn = soundConfig.playSounds();
		Consumer<Boolean> soundOnFunc = new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				soundConfig.setPlaySounds(value);
				saveData();
			}
		};
		CheckBox soundCheck = createCheckBox("Sound", soundOn, soundOnFunc);
		
		boolean musicOn = soundConfig.playMusic();
		Consumer<Boolean> musicOnFunc = new Consumer<Boolean>() {
			@Override
			public void accept(Boolean value) {
				soundConfig.setPlayMusic(value);
				saveData();
			}
		};
		CheckBox musicCheck = createCheckBox("Music", musicOn, musicOnFunc);

		final float sliderPanelPadding = 5f;
		List<SliderPanel> sliderPanels = new ArrayList<SliderPanel>();

		// Create the sound volume slider
        final Label soundSliderValueLabel = new Label(Integer.toString(soundConfig.getSoundLevel()), getSkin());
        final IntConsumer soundFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                soundConfig.setSoundLevel(value);
                soundSliderValueLabel.setText(Integer.toString(value));
                saveData();
            }
        };

        sliderPanels.add(createIntSliderPanel("Sound",
                                              soundSliderValueLabel,
                                              soundConfig.getSoundLevel(),
                                              sliderPanelPadding,
                                              soundFunc));

		// Create the music volume slider
        final Label musicSliderValueLabel = new Label(Integer.toString(soundConfig.getMusicLevel()), getSkin());
        final IntConsumer musicFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                soundConfig.setMusicLevel(value);
                musicSliderValueLabel.setText(Integer.toString(value));
                saveData();
            }
        };

        sliderPanels.add(createIntSliderPanel("Music",
                                              musicSliderValueLabel,
                                              soundConfig.getMusicLevel(),
                                              sliderPanelPadding,
                                              musicFunc));

		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		
		Table table = new Table();
		float pad = 20f;
		
		Cell<CheckBox> soundCheckCell = table.add(soundCheck);
		soundCheckCell.pad(pad);
		Cell<SliderPanel> soundSliderCell = table.add(sliderPanels.get(0));
		soundSliderCell.pad(pad);
		table.row();
		
		Cell<CheckBox> musicCheckCell = table.add(musicCheck);
		musicCheckCell.pad(pad);
		Cell<SliderPanel> musicSliderCell = table.add(sliderPanels.get(1));
		musicSliderCell.pad(pad);
		table.row();

		Cell<Button> menuCell = table.add(menuButton);
		menuCell.pad(pad);
		menuCell.colspan(2);
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
	private void saveData() {
		ScreenManager manager = getManager();
		PredatorPreyGame game = manager.getGame();
		DataManager dataManager = game.getDataManager();
		dataManager.saveSoundData(soundConfig);
		game.updateSoundManager();
	}
	
}
