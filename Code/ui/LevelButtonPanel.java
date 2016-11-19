package ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
	private static final String IMG_EMPTY = "Star_Incomplete_Icon";
	private static final String IMG_STAR = "Star_Complete_Icon";
	
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
		
		Table stars = new Table();
        stars.add(bronze);
        stars.add(silver);
        stars.add(gold);
        
        add(button).expand();
        row();
        add(stars).height(Value.percentWidth(0.33f, this)).expand();
	}
	
	public void setNotComplete() {
		bronze.setDrawable(skin.getDrawable(IMG_EMPTY));
		silver.setDrawable(skin.getDrawable(IMG_EMPTY));
		gold.setDrawable(skin.getDrawable(IMG_EMPTY));
	}
	
	public void setCompleteBronze() {
		bronze.setDrawable(skin.getDrawable(IMG_STAR));
		silver.setDrawable(skin.getDrawable(IMG_EMPTY));
		gold.setDrawable(skin.getDrawable(IMG_EMPTY));
	}
	
	public void setCompleteSilver() {
		bronze.setDrawable(skin.getDrawable(IMG_STAR));
		silver.setDrawable(skin.getDrawable(IMG_STAR));
		gold.setDrawable(skin.getDrawable(IMG_EMPTY));
	}
	
	public void setCompleteGold() {
		bronze.setDrawable(skin.getDrawable(IMG_STAR));
		silver.setDrawable(skin.getDrawable(IMG_STAR));
		gold.setDrawable(skin.getDrawable(IMG_STAR));
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
