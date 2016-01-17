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
		game.addInputProcessor("MAIN_MENU",stage);
		
		batch = new SpriteBatch();
		screenTexture = new Texture(Gdx.files.internal("menu_screen.png"));
		
		// TODO create more buttons in an organised layout.
		// The below chunk needs to be reorganised to make it more
		// reusable and avoid repetitive code bloat.
		
		// TODO Read this from config
		float buttonWidthCm = 2;
		float buttonHeightCm = 1;
		
		Button gameButton = game.createButton("button_game.png",
										 "button_game_highlight.png",
										 "MAIN_MENU",
										 "GAME",
										 0, 0,
										 buttonWidthCm,
										 buttonHeightCm);
		
		Button settingsButton = game.createButton("button_settings.png",
								  		 	 "button_settings_highlight.png",
								  		 	 "MAIN_MENU",
								  		 	 "SETTINGS",
										 	 0, 0,
										 	buttonWidthCm,
										 	buttonHeightCm);
		
		float displayWidth = Gdx.graphics.getWidth();
		float displayHeight = Gdx.graphics.getHeight();
		
		gameButton.setPosition(displayWidth - gameButton.getWidth(),
							   displayHeight - gameButton.getHeight());
		
		settingsButton.setPosition(0, 
								   displayHeight - settingsButton.getHeight());
		
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
		stage.getViewport().update(width, height);
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