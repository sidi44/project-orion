package ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.badlogic.gdx.Screen;

import callback.Event;
import callback.Sender;
import game.PredatorPreyGame;

public class ScreenManager extends Sender {

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
		screens.put(ScreenName.Pause, new PauseScreen(this));
		screens.put(ScreenName.Loading, new LoadingScreen(this));
	}
	
	public void changeScreen(ScreenName name) {
		
		// Prepare the screen change event
		ScreenName from = currentScreen();
		ScreenName to = name;
		Event event = new UIEventScreenChange("ScreenChange", from, to);
		
		// Do the screen change
		Screen screen = screens.get(name);
		game.setScreen(screen);
		
		// Broadcast the screen change event to receivers
		sendToAll(event);
	}
	
	Screen getScreen(ScreenName name) {
		return screens.get(name);
	}
	
	PredatorPreyGame getGame() {
		return game;
	}
	
	private ScreenName currentScreen() {
		Screen screen = game.getScreen();
		for (Entry<ScreenName, Screen> entry : screens.entrySet()) {
			if (Objects.equals(screen, entry.getValue())) {
				return entry.getKey();
	        }
	    }
		return null;
	}
}
