package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;

import game.GameType;
import game.PredatorPreyGame;
import logic.GameState;

class PauseScreen extends MenuScreen {

    // TODO
    // Use some DialogBuilder or this will get messy

    // Button constants
    private static final float VERTICAL_BUTTON_PADDING = 10f;
    private static final float HORIZONTAL_BUTTON_PADDING = 5f;
    private static final float BUTTON_SCALE = 0.15f;
    private static final String RESUME_BUTTON = "resume_button";
    private static final String RESET_BUTTON = "reset_button";
    private static final String HOME_BUTTON = "home_button";
    private static final String SETTINGS_BUTTON = "settings_button";
    private static final String NEXT_BUTTON = "next_button";
    private static final String EXIT_BUTTON = "exit_button";

    private int buttonWidth;
    private int buttonHeight;

    // Dialog key constants
    private Dialog gamePausedDialog;
    private Dialog gameWonDialog;
    private Dialog gameLostDialog;

    private StarDisplayPanel gameWonStarPanel;

    public PauseScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    public void addActors() {

        buttonWidth = getScaledSmallerScreenDimension(BUTTON_SCALE);
        buttonHeight = getScaledSmallerScreenDimension(BUTTON_SCALE);

        gameWonStarPanel = new StarDisplayPanel(getSkin());
        
        gamePausedDialog = createGamePausedDialog();
        gameWonDialog = createGameWonDialog();
        gameLostDialog = createGameLostDialog();
    }


    @Override
    public void doShow() {

        switch (getManager().getGame().getGameOverReason()) {

        	case No:
        		gamePausedDialog.show(getUIStage());
        		break;

        	case Prey:
        		PredatorPreyGame game = getManager().getGame();
        		int levelNumber = game.getLevelNumber();
        		GameState state = game.getGameLogic().getGameState();
        		int score = state.getScore();
        		setStarsComplete(gameWonStarPanel, levelNumber, score);
        		gameWonDialog.show(getUIStage());
        		break;

        	default:
        		gameLostDialog.show(getUIStage());
        		break;
        }
        
        super.doShow();
    }


    @Override
    public void doRender(float delta) {
        super.doRender(delta);
        //getManager().getScreen(ScreenName.Game).render(0);
    }


    /**
     * Helper method that creates and adds a button with the specified style
     * and dimensions to the dialog.
     *
     * @param dialog The dialog.
     * @param styleName The style name for the dialog button.
     * @param buttonWidth The button width.
     * @param buttonHeight The button height.
     */
    private Button addDialogButton(Dialog dialog,
                                 String styleName,
                                 int buttonWidth,
                                 int buttonHeight) {

        final ImageButton dialogButton = new ImageButton(getSkin(), styleName);
        dialog.getButtonTable().add(dialogButton).size(buttonWidth, buttonHeight);
        dialog.setObject(dialogButton, styleName);

        return dialogButton;
    }


    private Dialog createGamePausedDialog() {

        Dialog gamePausedDialog = new Dialog("Game Paused", getSkin(), "dialog_transparent_background") {
            @Override
            public void result(Object obj) {
                changeScreenOnButtonPressed((String) obj);
            }
        };

        // Pad it
        gamePausedDialog.getButtonTable().padTop(VERTICAL_BUTTON_PADDING);
        gamePausedDialog.getButtonTable().padBottom(VERTICAL_BUTTON_PADDING);
        gamePausedDialog.getButtonTable().padLeft(HORIZONTAL_BUTTON_PADDING);
        gamePausedDialog.getButtonTable().padRight(HORIZONTAL_BUTTON_PADDING);

        // Align the title text
        gamePausedDialog.getTitleLabel().setAlignment(Align.center);

        addDialogButton(gamePausedDialog, RESUME_BUTTON, buttonWidth, buttonHeight);
        addDialogButton(gamePausedDialog, RESET_BUTTON, buttonWidth, buttonHeight);
        addDialogButton(gamePausedDialog, SETTINGS_BUTTON, buttonWidth, buttonHeight);
        addDialogButton(gamePausedDialog, EXIT_BUTTON, buttonWidth, buttonHeight);

        gamePausedDialog.pack();
        gamePausedDialog.debug();

        return gamePausedDialog;
    }


    private Dialog createGameLostDialog() {

        Dialog gameLostDialog = new Dialog("Game lost", getSkin(), "dialog_transparent_background") {

            @Override
            public void result(Object obj) {
                changeScreenOnButtonPressed((String) obj);
            }
        };

        // Pad it
        gameLostDialog.getButtonTable().padTop(VERTICAL_BUTTON_PADDING);
        gameLostDialog.getButtonTable().padBottom(VERTICAL_BUTTON_PADDING);
        gameLostDialog.getButtonTable().padLeft(HORIZONTAL_BUTTON_PADDING);
        gameLostDialog.getButtonTable().padRight(HORIZONTAL_BUTTON_PADDING);

        // Align the title text
        gameLostDialog.getTitleLabel().setAlignment(Align.center);

        // Set the dialog text
        gameLostDialog.text("You lost!");

        addDialogButton(gameLostDialog, RESET_BUTTON, buttonWidth, buttonHeight);
        addDialogButton(gameLostDialog, EXIT_BUTTON, buttonWidth, buttonHeight);

        gameLostDialog.pack();
        gameLostDialog.debug();

        return gameLostDialog;
    }


    private Dialog createGameWonDialog() {

        Dialog gameWonDialog = new Dialog("Game won", getSkin(), "dialog_transparent_background") {

            @Override
            public void result(Object obj) {
                changeScreenOnButtonPressed((String) obj);
            }
        };

        // Pad it
        gameWonDialog.getButtonTable().padTop(VERTICAL_BUTTON_PADDING);
        gameWonDialog.getButtonTable().padBottom(VERTICAL_BUTTON_PADDING);
        gameWonDialog.getButtonTable().padLeft(HORIZONTAL_BUTTON_PADDING);
        gameWonDialog.getButtonTable().padRight(HORIZONTAL_BUTTON_PADDING);

        // Align the title text
        gameWonDialog.getTitleLabel().setAlignment(Align.center);

        // Set the dialog text
        gameWonDialog.text("You won!");
		
		// Add the star panel
        gameWonStarPanel.setNotComplete();
        gameWonDialog.getContentTable().row();
        gameWonDialog.getContentTable().add(gameWonStarPanel);

        addDialogButton(gameWonDialog, NEXT_BUTTON, buttonWidth, buttonHeight);
        addDialogButton(gameWonDialog, RESET_BUTTON, buttonWidth, buttonHeight);
        addDialogButton(gameWonDialog, EXIT_BUTTON, buttonWidth, buttonHeight);

        gameWonDialog.pack();
        gameWonDialog.debug();

        return gameWonDialog;
    }

    private ScreenName getExitScreenName() {

        GameType gameType = getManager().getGame().getGameType();
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

    private void changeScreenOnButtonPressed(String buttonId) {

        ScreenName screenName;

        if (RESUME_BUTTON.equals(buttonId)) {
            screenName = ScreenName.Game;
        }
        else if (RESET_BUTTON.equals(buttonId)) {
            getManager().getGame().resetGame();
            screenName = ScreenName.Game;
        }
        else if (SETTINGS_BUTTON.equals(buttonId)) {
            SettingsScreen settingsScreen =
                    (SettingsScreen) getManager().getScreen(ScreenName.Settings);
            settingsScreen.setPreviousScreen(ScreenName.Pause);
            screenName = ScreenName.Settings;
        }
        else if (HOME_BUTTON.equals(buttonId)) {
            screenName = ScreenName.MainMenu;
        }
        else if (NEXT_BUTTON.equals(buttonId)) {
            // TODO
            throw new IllegalStateException("Not implemented");
        }
        else if (EXIT_BUTTON.equals(buttonId)) {
            screenName = getExitScreenName();
        }
        else {
            throw new IllegalArgumentException("Invalid button ID: " + buttonId);
        }

        getManager().changeScreen(screenName);
    }
}
