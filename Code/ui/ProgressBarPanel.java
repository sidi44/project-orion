package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class ProgressBarPanel extends Table {

	// The widgets used in this panel
	private ProgressBar progressBar;
	private Label label;
	
	public ProgressBarPanel(Skin skin) {
		
		this.progressBar = new ProgressBar(0, 100, 1, false, skin);
		this.label = new Label("0 %", skin);
		
		layoutWidgets();
	}
	
	private void layoutWidgets() {
		add(label);
		row();
		add(progressBar);
	}
	
	public void setValue(float value) {
		progressBar.setValue(value);
		int percent = (int) (progressBar.getPercent() * 100);
		String text = percent + " %";
		label.setText(text);		
	}
	
}
