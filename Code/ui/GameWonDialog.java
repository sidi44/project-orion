package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * In-game dialog to display when the game is won.
 * 
 * @author Simon Dicken
 */
class GameWonDialog extends GameDialog {

    private StarDisplayPanel starPanel;
	
	/**
	 * Constructor.
	 * 
     * @param skin - the skin to use for this dialog.
     * @param screenManager - the game's screen manager.
	 */
	public GameWonDialog(Skin skin, ScreenManager screenManager) {
		super("Game Won", skin, screenManager);

        // Align the title text
        getTitleLabel().setAlignment(Align.center);

        // Set the dialog text
        text("You won!");
		
		// Add the star panel
        starPanel = new StarDisplayPanel(skin);
        Table contentTable = getContentTable();
        contentTable.row();
        contentTable.add(starPanel);

        // Add the buttons we want on this dialog
        addNextButton();
        addResetButton();
        addExitButton();

        // Draw debug lines
        debug();
	}

	/**
	 * Get this dialog's star display panel.
	 * 
	 * @return this dialog's star display panel.
	 */
	public StarDisplayPanel getStarPanel() {
		return starPanel;
	}
	
}
