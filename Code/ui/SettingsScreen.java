package ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import data.DataManager;
import game.PredatorPreyGame;
import sound.SoundConfiguration;

class SettingsScreen extends MenuScreen {
	
	private SoundConfiguration soundConfig;
	
	private ScreenName previousScreen;
	
	private static final float SLIDER_PADDING = 5f;
	private static final float TABLE_PADDING = 20f;
	
	public SettingsScreen(ScreenManager manager) {
		super(manager);
		
		previousScreen = ScreenName.MainMenu;
	}
	
	public void setPreviousScreen(ScreenName previousScreen) {
		this.previousScreen = previousScreen;
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
		Image background = new Image(new Texture(file));
		setBackgroundImage(background);

		Table soundButtonWithSlider = createSoundButtonWithSlider();
		Table musicButtonWithSlider = createMusicButtonWithSlider();
		
		// Layout the widgets in a table
        Table table = new Table();
        table.add(soundButtonWithSlider);
        table.row();
        table.add(musicButtonWithSlider);

        table.setFillParent(true);
        table.setDebug(true);
        getUIStage().addActor(table);
		
		
		// Create a separate table for the back button. This allows us to anchor 
		// it to the corner of the screen without affecting the other buttons.
		Table backButtonTable = new Table();
		backButtonTable.align(Align.bottomLeft);
		backButtonTable.pad(TABLE_PADDING).add(createBackButton()).bottom().left();
		
		backButtonTable.setFillParent(true);
		backButtonTable.setDebug(true);
		
		getUIStage().addActor(backButtonTable);
	}
	
	
	private ImageButton createBackButton() {
        ImageButton backButton = new ImageButton(getSkin(), "back_button");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getManager().changeScreen(previousScreen);
            }
        });
        
        return backButton;
	}
	
	
	private Table createSoundButtonWithSlider() {
	   
	    // Button - if checked, means sound is disabled
        final int soundLevel = soundConfig.getSoundLevel();

        final ImageButton soundButton = new ImageButton(getSkin(), "sound_button");
        soundButton.setChecked(soundLevel == 0);
        
        // Slider
        final Slider soundSlider = new Slider(0, 10, 1, false, getSkin());
        soundSlider.setValue(soundLevel);
        
        // Click listener will change the value of the slider resulting in the 
        // below change listener being called that will save the data
        soundButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean soundEnabled = !soundButton.isChecked();
                soundConfig.setSoundLevel(soundEnabled ? 5 : 0); // Default the level to 5/10 when enabled via click
                soundSlider.setValue(soundConfig.getSoundLevel());
            }
        });
        
        soundSlider.addCaptureListener(new ChangeListener() {
            
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int value = (int) soundSlider.getValue();
                soundButton.setChecked(value == 0);
                soundConfig.setSoundLevel(value);
                saveData();
            }
        });
        
        // Add button and slider to the table
        Table table = new Table();
        
        table.add(soundButton).pad(TABLE_PADDING);
        table.add(soundSlider).pad(TABLE_PADDING);
        
        table.setDebug(true);
        
        return table;
	}
	
	
	private Table createMusicButtonWithSlider() {
        
        // Button - if checked, means sound is disabled
        final int musicLevel = soundConfig.getMusicLevel();

        final ImageButton musicButton = new ImageButton(getSkin(), "music_button");
        musicButton.setChecked(musicLevel == 0);

        // Slider
        final Slider musicSlider = new Slider(0, 10, 1, false, getSkin());
        musicSlider.setValue(musicLevel);

        // Click listener will change the value of the slider resulting in the 
        // below change listener being called that will save the data
        musicButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean musicEnabled = !musicButton.isChecked();
                soundConfig.setMusicLevel(musicEnabled ? 5 : 0); // Default the level to 5/10 when enabled via click
                musicSlider.setValue(soundConfig.getMusicLevel());
            }
        });

        musicSlider.addCaptureListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int value = (int) musicSlider.getValue();
                musicButton.setChecked(value == 0);
                soundConfig.setMusicLevel(value);
                saveData();
            }
        });

        // Add button and slider to the table
        Table table = new Table();

        table.add(musicButton).pad(TABLE_PADDING);
        table.add(musicSlider).pad(TABLE_PADDING);
        
        table.setDebug(true);

        return table;
	}
		
	
	private void saveData() {
		ScreenManager manager = getManager();
		PredatorPreyGame game = manager.getGame();
		DataManager dataManager = game.getDataManager();
		dataManager.saveSoundData(soundConfig);
		game.updateSoundManager();
	}
}
