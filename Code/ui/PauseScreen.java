package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;

class PauseScreen extends MenuScreen {

    private static final float VERTICAL_BUTTON_PADDING = 10f;
    private static final float HORIZONTAL_BUTTON_PADDING = 5f;

    private static final String RESUME_BUTTON = "resume_button";
    private static final String RESET_BUTTON = "reset_button";
    private static final String HOME_BUTTON = "home_button";
    private static final String SETTINGS_BUTTON = "settings_button";

    private Dialog dialog;

    public PauseScreen(ScreenManager manager) {
        super(manager);
    }

    @Override
    public void addActors() {

        final int buttonSizeX = 75;
        final int buttonSizeY = 75;

        // Create a dialog
        dialog = new Dialog("Game Paused", getSkin(), "dialog") {
            @Override
            public void result(Object obj) {

                String buttonId = (String) obj;
                ScreenName screenName;

                if (RESUME_BUTTON.equals(buttonId)) {
                    screenName = ScreenName.Game;
                }
                else if (RESET_BUTTON.equals(buttonId)) {
                    getManager().getGame().resetGame();
                    screenName = ScreenName.Loading;
                }
                else if (SETTINGS_BUTTON.equals(buttonId)) {
                    screenName = ScreenName.Settings;
                }
                else if (HOME_BUTTON.equals(buttonId)) {
                    screenName = ScreenName.MainMenu;
                }
                else {
                    throw new IllegalArgumentException("Invalid button ID: " + buttonId);
                }

                getManager().changeScreen(screenName);
            }
        };

        // Pad it
        dialog.getButtonTable().padTop(VERTICAL_BUTTON_PADDING);
        dialog.getButtonTable().padBottom(VERTICAL_BUTTON_PADDING);
        dialog.getButtonTable().padLeft(HORIZONTAL_BUTTON_PADDING);
        dialog.getButtonTable().padRight(HORIZONTAL_BUTTON_PADDING);

        // Align the title text
        dialog.getTitleLabel().setAlignment(Align.center);

        addDialogButton(RESUME_BUTTON, buttonSizeX, buttonSizeY);
        addDialogButton(RESET_BUTTON, buttonSizeX, buttonSizeY);
        addDialogButton(HOME_BUTTON, buttonSizeX, buttonSizeY);
        addDialogButton(SETTINGS_BUTTON, buttonSizeX, buttonSizeY);

        dialog.pack();
        dialog.debug();
    }

    @Override
    public void doShow() {
        // This might be really inefficient, but prevents the dialog from disappearing
        // after being accessed more than once
        dialog.show(getStage());
    }


    private void addDialogButton(String styleName,
                                 int buttonSizeX,
                                 int buttonSizeY) {

        final ImageButton dialogButton = new ImageButton(getSkin(), styleName);
        dialog.getButtonTable().add(dialogButton).size(buttonSizeX, buttonSizeY);
        dialog.setObject(dialogButton, styleName);
    }
}
