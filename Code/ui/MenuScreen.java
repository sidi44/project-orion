package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import functional.Consumer;
import functional.IntConsumer;

abstract class MenuScreen extends AbstractScreen {

	private Skin skin;
	
	public MenuScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void initialise() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"), atlas);
		super.initialise();
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

	protected CheckBox createCheckBox(String text, boolean initialState, 
			final Consumer<Boolean> func) {
		
		// Create the checkbox
		final CheckBox checkbox = new CheckBox(text, getSkin());
		
		// Set the initial state
		checkbox.setChecked(initialState);
		
		// Add a listener which calls the provided function when the checkbox's
		// state is changed
		checkbox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean value = checkbox.isChecked();
				func.accept(value);
			}
		});
		
		return checkbox;
	}
	
	protected Slider createIntSlider(int min, int max, int step, 
			int initialValue, final IntConsumer func) {
		
		// Create the slider
		final Slider slider = new Slider(min, max, step, false, getSkin());
		
		// Set the initial value
		slider.setValue(initialValue);
		
		// Add a listener which calls the provided function when the sliders
		// value is changed
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {			
				int value = (int) slider.getValue();
				func.accept(value);
			}
		});
		
		// That's it
		return slider;
	}
	
}
