package render;

import game.PredatorPreyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen implements Screen {

	private PredatorPreyGame game;
	private SpriteBatch batch;
	private Texture screenTexture;
	private long startTime;
	private final long displayTime = 1000000000l; // 1 second;
	private final AssetManager assetManager;
	
	public SplashScreen(PredatorPreyGame game) {
		this.game = game;
		batch = new SpriteBatch();
		screenTexture = new Texture(Gdx.files.internal("splash_screen.png"));
		
		assetManager = game.getAssetManager();
		assetManager.load("splash_screen.png", Texture.class);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (assetManager.isLoaded("splash_screen.png", Texture.class)) {
			batch.begin();
			batch.draw(screenTexture, 0, 0);
			batch.end();
		}
		
		if (assetManager.update()) {
			if (TimeUtils.timeSinceNanos(startTime) > displayTime) {
				game.switchToScreen("MAIN_MENU");
			}
		} else {
			// TODO draw progress bar using assetManager.getProgress()
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		startTime = TimeUtils.nanoTime();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}