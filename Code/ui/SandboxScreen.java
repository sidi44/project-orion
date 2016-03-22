package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SandboxScreen extends MenuScreen {

	public SandboxScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	protected void addActors() {
		
		FileHandle file = Gdx.files.internal("data/ui/sandbox_screen.png");
		Image screenImage = new Image(new Texture(file));
		getStage().addActor(screenImage);

		final Slider widthSlider = createSlider(4f, 30f, 1f);
		Slider heightSlider = createSlider(4f, 30f, 1f);
		Slider numPreySlider = createSlider(1f, 10f, 1f);
				
        widthSlider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Width slider value: " + widthSlider.getValue());
            }

        });
		
        // TODO
        // 1. Display buttons differently when they're disabled.
        // 2. Labels on sliders
        // 3. Document that says what needs to be done to get the game fully
        // done.
        // 4. Next meeting Wednesday the 30th
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
