package render;

import game.PredatorPreyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class SettingsScreen implements Screen {

	private PredatorPreyGame game;
	private SpriteBatch batch;
	private Texture screenTexture;
	private Stage stage;
	
	public SettingsScreen(PredatorPreyGame game) {
		this.game = game;
		
		batch = new SpriteBatch();
		screenTexture = new Texture(Gdx.files.internal("settings_screen.png"));
		
		Button menuButton = game.createButton("button_menu.png",
											  "button_menu_highlight.png",
											  "MAIN_MENU",
											  500, 100);
		stage = new Stage();
		game.addInputProcessor(stage);
		//stage.addActor(menuButton);
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