package render;

import game.PredatorPreyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MainMenuScreen implements Screen {

	private PredatorPreyGame game;
	private Stage stage;
	
	public MainMenuScreen(final PredatorPreyGame game) {
		this.game = game;

		stage = new Stage();
		this.game.addInputProcessor(stage);
		
		// TODO create more buttons in an organised layout.
		// The below chunk needs to be reorganised to make it more
		// reusable and avoid repetitive code bloat.
		// Button sizes & locations should be derived from the viewport / camera
		Texture buttonTexture = new Texture(Gdx.files.internal("button_next.png"));
		Sprite buttonSprite = new Sprite(buttonTexture);
		SpriteDrawable buttonDrawable = new SpriteDrawable(buttonSprite);
		ImageButton button = new ImageButton(buttonDrawable);
		button.setSize(200, 100);
		button.setPosition(200, 300);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.switchToScreen("GAME");
				System.out.println("clicked - MainMenu");
			}
		});

		stage.addActor(button);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
		// if some inputProcessor flag was set
		// player pressed some button to switch screens
//		if (switchScreen) {
//			game.setScreen(game.getScreenByName("SETTINGS"));
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