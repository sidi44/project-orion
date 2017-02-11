package ui;

/**
 * In-game dialog to display when the game is paused.
 * 
 * @author Simon Dicken
 */
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

class GamePausedDialog extends GameDialog {
	
	/**
	 * Constructor.
	 * 
     * @param skin - the skin to use for this dialog.
     * @param screenManager - the game's screen manager.
	 */
	public GamePausedDialog(Skin skin, ScreenManager screenManager) {
		super("Game Paused", skin, screenManager);
		
        // Align the title text
        getTitleLabel().setAlignment(Align.center);
		
		// Add the buttons we want on this dialog
        addResumeButton();
        addResetButton();
        addSettingsButton();
        addExitButton();
		
        // Pad the dialog buttons with the default settings
		padButtons();
        
        // Draw debug lines
        debug();
	}

}
