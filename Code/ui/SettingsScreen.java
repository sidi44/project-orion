package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class SettingsScreen extends MenuScreen {
	
	public SettingsScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void addActors() {
		
		FileHandle file = Gdx.files.internal("data/ui/settings_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		CheckBox soundCheck = createCheckBox("Sound");
		CheckBox musicCheck = createCheckBox("Music");
		
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		
		Table table = new Table();
		float pad = 20f;
		
		Cell<CheckBox> soundCheckCell = table.add(soundCheck);
		soundCheckCell.pad(pad);
		table.row();
		
		Cell<CheckBox> musicCheckCell = table.add(musicCheck);
		musicCheckCell.pad(pad);
		table.row();

		Cell<Button> menuCell = table.add(menuButton);
		menuCell.pad(pad);
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
	
}