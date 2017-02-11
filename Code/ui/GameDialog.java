package ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import game.GameType;
import game.PredatorPreyGame;

/**
 * Base class for in-game dialog boxes.
 * 
 * This class provides some standard buttons that can be added to the dialog's
 * button table. Derived classes can just use the add[*]Button() methods to add
 * the buttons they need in their constructors. When a button is pressed on the 
 * dialog, the buttonPressed() method is called which will carry out an action 
 * for the button. 
 * 
 * Other (non-base class) buttons can be added using the addButton() method 
 * providing a new style, but in this case the buttonPressedSpecial() method 
 * should be overridden by the derived class and the new style handled.
 * 
 * It is left up to derived classes to add their own content to the dialog's
 * content table.
 * 
 * @author Simon Dicken
 */
abstract class GameDialog extends Dialog {

	private ScreenManager screenManager;
	
    private static final float VERTICAL_BUTTON_PADDING = 10f;
    private static final float HORIZONTAL_BUTTON_PADDING = 5f;
    private static final String RESUME_BUTTON = "resume_button";
    private static final String RESET_BUTTON = "reset_button";
    private static final String HOME_BUTTON = "home_button";
    private static final String SETTINGS_BUTTON = "settings_button";
    private static final String NEXT_BUTTON = "next_button";
    private static final String EXIT_BUTTON = "exit_button";
	
    /**
     * Constructor.
     * 
     * @param title - the title to use for the dialog.
     * @param skin - the skin to use for this dialog.
     * @param screenManager - the game's screen manager, required so that the 
     * dialog's buttons can change the screen as required and send info back
     * to the game.
     */
    public GameDialog(String title, Skin skin, ScreenManager screenManager) {
		super(title, skin, "dialog_transparent_background");
		this.screenManager = screenManager;
	}

	@Override
    protected void result(Object obj) {
        buttonPressed((String) obj);
    }
    
    /**
     * Helper method that creates and adds a button with the specified style
     * and dimensions to the dialog.
     *
     * @param styleName The style name for the dialog button.
     * @param buttonWidth The button width.
     * @param buttonHeight The button height.
     */
    protected void addButton(String buttonStyle) {

        ImageButton dialogButton = new ImageButton(getSkin(), buttonStyle);
        button(dialogButton, buttonStyle);
    }

    /**
     * Add a Resume button to the dialog.
     */
    protected void addResumeButton() {
    	addButton(RESUME_BUTTON);
    }
    
    /**
     * Add a Reset button to the dialog.
     */
    protected void addResetButton() {
    	addButton(RESET_BUTTON);
    }
    
    /**
     * Add a Settings button to the dialog.
     */
    protected void addSettingsButton() {
    	addButton(SETTINGS_BUTTON);
    }
    
    /**
     * Add a Home button to the dialog.
     */
    protected void addHomeButton() {
    	addButton(HOME_BUTTON);
    }
    
    /**
     * Add a Next button to the dialog.
     */
    protected void addNextButton() {
    	addButton(NEXT_BUTTON);
    }
    
    /**
     * Add a Exit button to the dialog.
     */
    protected void addExitButton() {
    	addButton(EXIT_BUTTON);
    }
    
    /**
     * Apply the default padding to the buttons in this dialog's button table.
     */
    protected void padButtons() {
		Table buttonTable = getButtonTable();
		buttonTable.padTop(VERTICAL_BUTTON_PADDING);
        buttonTable.padBottom(VERTICAL_BUTTON_PADDING);
        buttonTable.padLeft(HORIZONTAL_BUTTON_PADDING);
        buttonTable.padRight(HORIZONTAL_BUTTON_PADDING);
    }
    
    /**
     * Called when a button is pressed with the user data attached to the 
     * button (which in our case is the button style). 
     * 
     * @param buttonStyle - the style of the button that has been pressed.
     */
    private final void buttonPressed(String buttonStyle) {

    	PredatorPreyGame game = screenManager.getGame();
    	
        if (RESUME_BUTTON.equals(buttonStyle)) {
        	
        	game.resumeGame();
        	
        } else if (RESET_BUTTON.equals(buttonStyle)) {
        	
        	game.resetGame();
        	
        } else if (SETTINGS_BUTTON.equals(buttonStyle)) {
        	
        	// Tell the Settings screen which screen to return to when it is 
        	// exited
            SettingsScreen settingsScreen =
            		(SettingsScreen) screenManager.getScreen(ScreenName.Settings);
            settingsScreen.setPreviousScreen(ScreenName.Game);
            
            // Go to the Settings screen
            screenManager.changeScreen(ScreenName.Settings);
            
        } else if (HOME_BUTTON.equals(buttonStyle)) {
        	
        	screenManager.changeScreen(ScreenName.MainMenu);
        	
        } else if (NEXT_BUTTON.equals(buttonStyle)) {
        	
            // TODO
            throw new IllegalStateException("Not implemented");
            
        } else if (EXIT_BUTTON.equals(buttonStyle)) {
        	
        	// First get the name of the screen we should change to (this is 
        	// dependent on the game type)
            ScreenName screenName = getExitScreenName();
            
            // Tell the game the player has quit (this sets the game type to 
            // 'NotPlaying')
            game.quitGame();
            
            // Change the screen. (This has to come after quitGame() as the 
            // SoundManager currently relies on the game type being correct.)
            screenManager.changeScreen(screenName);
            
        } else {
        	
        	// Derived classes may have added their own buttons with their own
        	// styles, so give them chance to handle it.
            buttonPressedSpecial(buttonStyle);
        }
        
    }
    
    /**
     * Derived classes should override this method if they have added a 
     * non-base class button with its own button style. 
     * 
     * This default version throws an exception.
     * 
     * @param buttonStyle - the button style of the button that has been 
     * pressed.
     */
    protected void buttonPressedSpecial(String buttonStyle) {
    	String msg = "Unknown button style name: " + buttonStyle;
    	throw new IllegalArgumentException(msg);
    }
    
    /**
     * Return the name of the screen to change to when exiting the game screen.
     * This depends on the game type.
     * 
     * @return The name of the screen to change to when exiting the game.
     */
    protected ScreenName getExitScreenName() {

        GameType gameType = screenManager.getGame().getGameType();
        ScreenName screenName;

        switch (gameType) {

	        case Levels:
	            screenName = ScreenName.Levels;
	            break;
	
	        case Sandbox:
	            screenName = ScreenName.Sandbox;
	            break;
	
	        default:
	            throw new IllegalStateException("Invalid game type: " + gameType);
        }

        return screenName;
    }
    
    @Override
	public void hide() {
    	// We override the Dialog base class version of hide() so that we can 
    	// 'fade out' the dialog instantly, rather than with a small delay.
		hide(sequence(Actions.alpha(0), 
					  Actions.removeListener(ignoreTouchDown, true), 
					  Actions.removeActor()));
	}
}
