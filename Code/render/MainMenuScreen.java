package render;

import game.PredatorPreyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class MainMenuScreen implements Screen {

	private PredatorPreyGame game;
	private Stage stage;
	private Texture screenTexture;
	private SpriteBatch batch;
	
	public MainMenuScreen(final PredatorPreyGame game) {
		this.game = game;
		
		stage = new Stage();
		game.addInputProcessor(stage);
		
		batch = new SpriteBatch();
		screenTexture = new Texture(Gdx.files.internal("menu_screen.png"));
		
		// TODO create more buttons in an organised layout.
		// The below chunk needs to be reorganised to make it more
		// reusable and avoid repetitive code bloat.
		// Button sizes & locations should be derived from the viewport / camera

		// Game button
		Button gameButton = game.createButton("button_game.png",
										 "button_game_highlight.png",
										 "GAME",
										 500, 400);
		
		Button settingsButton = game.createButton("button_settings.png",
								  		 	 "button_settings_highlight.png",
										 	 "SETTINGS",
										 	 50, 400);
		
		stage.addActor(gameButton);
		stage.addActor(settingsButton);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(screenTexture, 0, 0);
		batch.end();
		
		stage.act(delta);
		stage.draw();
		
		// if some inputProcessor flag was set
		// player pressed some button to switch screens
//		if (switchScreen) {
//			game.switchToScreen("SETTINGS"));
//		}
//		else {
//			
//		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
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