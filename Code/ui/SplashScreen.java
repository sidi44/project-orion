package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

class SplashScreen extends AbstractScreen {

	private long startTime;
	private final long displayTime = 2000000000l; // 2 seconds;
	
	public SplashScreen(ScreenManager manager) {
		super(manager);
	}
	
	@Override
	protected void addActors() {
		FileHandle file = Gdx.files.internal("data/ui/splash_screen.png");
		Image background = new Image(new Texture(file));
		setBackgroundImage(background);
	}
	
	@Override
	protected void doRender(float delta) {
		if (TimeUtils.timeSinceNanos(startTime) > displayTime) {
			getManager().changeScreen(ScreenName.MainMenu);
		}
	}

	@Override
	protected void doShow() {
		startTime = TimeUtils.nanoTime();
	}
	
}