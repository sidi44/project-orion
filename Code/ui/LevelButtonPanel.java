package ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

class LevelButtonPanel extends StarDisplayPanel {

	// The widgets used in this panel
	private Button button;

	LevelButtonPanel(Skin skin, Button button) {
		super(skin);

		this.button = button;

		row();
		add(button).expand();
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
