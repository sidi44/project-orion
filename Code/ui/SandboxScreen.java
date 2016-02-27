package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SandboxScreen extends MenuScreen {

	public SandboxScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	protected void addActors() {
		
		FileHandle file = Gdx.files.internal("data/ui/sandbox_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		Slider widthSlider = createSlider(4f, 30f, 1f);
		Slider heightSlider = createSlider(4f, 30f, 1f);
		Slider numPreySlider = createSlider(1f, 10f, 1f);
		
		Button menuButton = createScreenChangeButton(
				"Main menu", ScreenName.MainMenu);
		
		Table table = new Table();
		float pad = 20f;
		
		Cell<Slider> widthSliderCell = table.add(widthSlider);
		widthSliderCell.pad(pad);
		table.row();
		
		Cell<Slider> heightSliderCell = table.add(heightSlider);
		heightSliderCell.pad(pad);
		table.row();
		
		Cell<Slider> numPreySliderCell = table.add(numPreySlider);
		numPreySliderCell.pad(pad);
		table.row();

		Cell<Button> menuCell = table.add(menuButton);
		menuCell.pad(pad);
		
		table.setFillParent(true);
		table.setDebug(true);
		getStage().addActor(table);
	}
	
}
