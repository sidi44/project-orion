package ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

class LevelButtonPanel extends Table {

	// The UI skin
	private Skin skin;
	
	// The widgets used in this panel
	private Button button;
	private Image bronze;
	private Image silver;
	private Image gold;
	
	// The names of the images in the Skin file used by this panel
	private static final String IMG_EMPTY = "star_empty";
	private static final String IMG_BRONZE = "star_bronze";
	private static final String IMG_SILVER = "star_silver";
	private static final String IMG_GOLD = "star_gold";
	
	
	LevelButtonPanel(Skin skin, Button button) {
		super();
		
		this.skin = skin;
		this.button = button;
		
		this.bronze = new Image();
		this.silver = new Image();
		this.gold = new Image();
		
		setNotComplete();
		
		layoutWidgets();
	}
	
	
	private void layoutWidgets() {
		
        HorizontalGroup stars = new HorizontalGroup();
        stars.addActor(bronze);
        stars.addActor(silver);
        stars.addActor(gold);
        
        add(button);
        row();
        add(stars).height(Value.percentWidth(0.33f, this));

	}
	
	public void setNotComplete() {
		bronze.setDrawable(skin.getDrawable(IMG_EMPTY));
		silver.setDrawable(skin.getDrawable(IMG_EMPTY));
		gold.setDrawable(skin.getDrawable(IMG_EMPTY));
	}
	
	public void setCompleteBronze() {
		bronze.setDrawable(skin.getDrawable(IMG_BRONZE));
		silver.setDrawable(skin.getDrawable(IMG_EMPTY));
		gold.setDrawable(skin.getDrawable(IMG_EMPTY));
	}
	
	public void setCompleteSilver() {
		bronze.setDrawable(skin.getDrawable(IMG_BRONZE));
		silver.setDrawable(skin.getDrawable(IMG_SILVER));
		gold.setDrawable(skin.getDrawable(IMG_EMPTY));
	}
	
	public void setCompleteGold() {
		bronze.setDrawable(skin.getDrawable(IMG_BRONZE));
		silver.setDrawable(skin.getDrawable(IMG_SILVER));
		gold.setDrawable(skin.getDrawable(IMG_GOLD));
	}
	
	public void setLocked(boolean locked) {
		button.setDisabled(locked);
		if (locked) {
			button.setTouchable(Touchable.disabled);
		} else {
			button.setTouchable(Touchable.enabled);
		}
	}
	
}
