package ui;

import game.PredatorPreyGame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Screen;

public class ScreenManager {

	private PredatorPreyGame game;
	private final Map<ScreenName, Screen> screens;
	
	public ScreenManager(PredatorPreyGame game) {
		this.game = game;
		screens = new HashMap<ScreenName, Screen>();
		createScreens();
	}
	
	private void createScreens() {
		screens.put(ScreenName.Splash, new SplashScreen(this));
		screens.put(ScreenName.MainMenu, new MainMenuScreen(this));
		screens.put(ScreenName.Settings, new SettingsScreen(this));
		screens.put(ScreenName.Levels, new LevelsScreen(this));
		screens.put(ScreenName.Sandbox, new SandboxScreen(this));
		screens.put(ScreenName.Game, new GameScreen(this));
	}
	
	public void changeScreen(ScreenName name) {
		Screen screen = screens.get(name);
		game.setScreen(screen);
	}
	
	Screen getScreen(ScreenName name) {
		return screens.get(name);
	}
	
	PredatorPreyGame getGame() {
		return game;
	}
}
