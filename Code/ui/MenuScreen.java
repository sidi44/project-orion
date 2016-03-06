package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

abstract class MenuScreen extends AbstractScreen {

	private Skin skin;
	
	public MenuScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void initialise() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"), atlas);
	}
	
	protected Skin getSkin() {
		return skin;
	}
	
	protected Button createScreenChangeButton(String text, final ScreenName name) {

		Button button = new TextButton(text, getSkin());
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getManager().changeScreen(name);
			}
		});
		
		return button;
	}

	protected CheckBox createCheckBox(String text) {
		CheckBox checkbox = new CheckBox(text, getSkin());
		return checkbox;
	}
	
	protected Slider createSlider(float min, float max, float step) {
		Slider slider = new Slider(min, max, step, false, getSkin());
		return slider;
	}
	
}
