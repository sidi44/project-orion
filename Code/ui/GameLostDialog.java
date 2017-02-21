package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * In-game dialog to display when the game is lost.
 * 
 * @author Simon Dicken
 */
class GameLostDialog extends GameDialog {

	/**
	 * Constructor.
	 * 
     * @param skin - the skin to use for this dialog.
     * @param screenManager - the game's screen manager.
	 */
	public GameLostDialog(Skin skin, ScreenManager screenManager) {
		super("Game Lost", skin, screenManager);
		
        // Align the title text
        getTitleLabel().setAlignment(Align.center);

        // Set the dialog text
        text("You lost!");

        // Add the buttons we want on this dialog
        addResetButton();
        addExitButton();

        // Pad the buttons
        padButtons();
        
        // Draw debug lines
        debug();
	}

}
