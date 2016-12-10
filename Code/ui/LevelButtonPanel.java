package ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class LevelButtonPanel extends Table {

	private StarDisplayPanel starPanel;
	private Button button;

	LevelButtonPanel(Skin skin, Button button) {

		this.button = button;
		this.starPanel = new StarDisplayPanel(skin);
		
		layoutWidgets();
	}
	
	private void layoutWidgets() {
		
		add(button).expand();
		row();
		
		// Suggest that the stars should be twice as wide as the button for 
		// now and that their height should be 1/3 of the width (so the stars
		// would be 'square')
		float width = button.getWidth() * 2;
		add(starPanel).width(width).height(width/3);
		
	}

	public StarDisplayPanel getStarPanel() {
		return starPanel;
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
