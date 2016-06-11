package ui;

import functional.Consumer;
import functional.IntConsumer;
import game.PredatorPreyGame;
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

class SettingsScreen extends MenuScreen {
	
	SoundConfiguration soundConfig;
	
	public SettingsScreen(ScreenManager manager) {
		super(manager);
	}
	
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
		int soundVolume = soundConfig.getSoundLevel();
		IntConsumer soundVolumeFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				soundConfig.setSoundLevel(value);
				saveData();
			}
		};
		Slider soundSlider = createIntSlider(0, 11, 1, soundVolume, soundVolumeFunc);
		
		// Create the music volume slider
		int musicVolume = soundConfig.getMusicLevel();
		IntConsumer musicVolumeFunc = new IntConsumer() {
			@Override
			public void accept(int value) {
				soundConfig.setMusicLevel(value);
				saveData();
			}
		};
		Slider musicSlider = createIntSlider(0, 11, 1, musicVolume, musicVolumeFunc);
		
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		
		Table table = new Table();
		float pad = 20f;
		
		Cell<CheckBox> soundCheckCell = table.add(soundCheck);
		soundCheckCell.pad(pad);
		Cell<Slider> soundSliderCell = table.add(soundSlider);
		soundSliderCell.pad(pad);
		table.row();
		
		Cell<CheckBox> musicCheckCell = table.add(musicCheck);
		musicCheckCell.pad(pad);
		Cell<Slider> musicSliderCell = table.add(musicSlider);
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