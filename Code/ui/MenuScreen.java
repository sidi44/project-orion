package ui;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import data.DataManager;
import functional.Consumer;
import functional.IntConsumer;
import game.PredatorPreyGame;

abstract class MenuScreen extends AbstractScreen {

	private Skin skin;
	private Stage uiStage;
	
	public MenuScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void initialise() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"), atlas);
		
		Camera camera = new OrthographicCamera();
		Viewport viewport = new ScreenViewport(camera);
		uiStage = new Stage(viewport);
		
		super.initialise();
	}
	
	protected Skin getSkin() {
		return skin;
	}
	
	protected Stage getUIStage() {
		return uiStage;
	}
	
	@Override
	protected void doShow() {
		addInputProcessor(getUIStage());
		super.doShow();
	}
	
	@Override
	protected void doRender(float delta) {
		uiStage.getViewport().apply();
		uiStage.act(delta);
		uiStage.draw();
		
		super.doRender(delta);
	}
	
	@Override 
	protected void doResize(int width, int height) {
		uiStage.getViewport().update(width, height, true);
		super.doResize(width, height);
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

	protected SliderPanel createIntSliderPanel(final Slider slider,
											   String labelName,
                                               float padding) {

		// Create the value label for the slider
		int initialValue = (int) slider.getValue();
		String valueLabel = Integer.toString(initialValue);
		final Label sliderValueLabel = new Label(valueLabel, getSkin());
	
		// Make sure the value label gets updated when the slider changes
        slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
                int value = (int) slider.getValue();
                sliderValueLabel.setText(Integer.toString(value));
			}
        });

        // Create the plus and minus buttons
	    final ImageButton minusButton = new ImageButton(getSkin(), "minus");
	    final ImageButton plusButton = new ImageButton(getSkin(), "plus");

	    minusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final int currentValue = (int) slider.getValue();
                if (currentValue > slider.getMinValue()) {
                    final int newValue = currentValue - 1;
                    slider.setValue(newValue);
                }
            }
	    });

	    plusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final int currentValue = (int) slider.getValue();
                if (currentValue < slider.getMaxValue()) {
                    final int newValue = currentValue + 1;
                    slider.setValue(newValue);
                }
            }
	    });

	    // Create the slider label widget
	    Label sliderLabel = new Label(labelName, getSkin());
	    
	    // Add everything into a SliderPanel which lays out the widgets nicely
	    return new SliderPanel(sliderLabel,
                               sliderValueLabel,
                               minusButton,
                               plusButton,
                               slider,
                               padding);
	}


	/**
	 * FIXME This method name sucks.
	 *
	 * @param scale The factor to scale the dimension by.
	 * @return The scaled screen dimension size, whichever is smaller.
	 */
	protected int getScaledSmallerScreenDimension(float scale) {

	    if (scale < 0) {
	        throw new IllegalArgumentException("Scaling factor cannot be negative.");
	    }

	    return (int) (scale * Math.min(getStage().getHeight(),
	                                   getStage().getWidth()));
	}
	
	protected void setStarsComplete(StarDisplayPanel starPanel, 
									int levelNumber, 
									int levelScore) {
		
		// Get the star threshold values for this level
		PredatorPreyGame game = getManager().getGame();
		DataManager dataManager = game.getDataManager();
		List<Integer> starScores = dataManager.getLevelStarScores(levelNumber);
		
		// Set the stars completed on the star display panel depending on 
		// the score
		if (starScores.size() != 3) {
			starPanel.setNotComplete();
		} else if (levelScore >= starScores.get(2)) {
			starPanel.setCompleteGold();
		} else if (levelScore >= starScores.get(1)) {
			starPanel.setCompleteSilver();
		} else if (levelScore >= starScores.get(0)) {
			starPanel.setCompleteBronze();
		} else {
			starPanel.setNotComplete();
		}
	}
}
