package ui;

import sound.SoundConfiguration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import data.DataManager;
import functional.Consumer;
import functional.IntConsumer;
import game.PredatorPreyGame;

class SettingsScreen extends MenuScreen {
	
	private SoundConfiguration soundConfig;
	
	private final float SLIDER_PADDING = 5f;
	private final float TABLE_PADDING = 20f;
	
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

		// Create the sound volume slider
        final IntConsumer soundFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                soundConfig.setSoundLevel(value);
                saveData();
            }
        };
        
        int soundLevel = soundConfig.getSoundLevel();
        Slider soundSlider = createIntSlider(0, 11, 1, soundLevel, soundFunc);
        SliderPanel soundSliderPanel = createIntSliderPanel(soundSlider, 
        													"Sound",
        													SLIDER_PADDING);

		// Create the music volume slider
        final IntConsumer musicFunc = new IntConsumer() {
            @Override
            public void accept(int value) {
                soundConfig.setMusicLevel(value);
                saveData();
            }
        };

        int musicLevel = soundConfig.getMusicLevel();
        Slider musicSlider = createIntSlider(0, 11, 1, musicLevel, musicFunc);
        SliderPanel musicSliderPanel = createIntSliderPanel(musicSlider, 
        													"Music",
        													SLIDER_PADDING);

        // Add the main menu button
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		
		// Layout the widgets in a table
		Table table = new Table();
		
		Cell<CheckBox> soundCheckCell = table.add(soundCheck);
		soundCheckCell.pad(TABLE_PADDING);
		Cell<SliderPanel> soundSliderCell = table.add(soundSliderPanel);
		soundSliderCell.pad(TABLE_PADDING);
		table.row();
		
		Cell<CheckBox> musicCheckCell = table.add(musicCheck);
		musicCheckCell.pad(TABLE_PADDING);
		Cell<SliderPanel> musicSliderCell = table.add(musicSliderPanel);
		musicSliderCell.pad(TABLE_PADDING);
		table.row();

		Cell<Button> menuCell = table.add(menuButton);
		menuCell.pad(TABLE_PADDING);
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
