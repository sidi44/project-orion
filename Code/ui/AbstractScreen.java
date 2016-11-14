package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

abstract class AbstractScreen implements Screen {

	private Stage stage;
	private ScreenManager manager;
	
	public AbstractScreen(ScreenManager manager) {
		this.manager = manager;
		Viewport viewport = getScreenViewport();
		if (viewport != null) {
			this.stage = new Stage(viewport);
		} else {
			this.stage = new Stage();
		}
		
		initialise();
		addActors();
	}
	
	protected Viewport getScreenViewport() {
		return null;
	}
	
	protected void initialise() {
		// Base class does nothing
	}
	
	protected void addActors() {
		// Base class does nothing
	}
	
	protected Stage getStage() {
		return stage;
	}
	
	protected ScreenManager getManager() {
		return manager;
	}
	
	@Override
	public final void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		addInputProcessor(multiplexer);
		Gdx.input.setInputProcessor(multiplexer);
		doShow();
	}
	
	protected void addInputProcessor(InputMultiplexer multiplexer) {
		// Base class does nothing.
	}
	
	protected void doShow() {
		// Bass class does nothing.
	}

	@Override
	public final void render(float delta) {
		
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Carry out any derived class rendering
		doRender(delta);
		
		// Draw our stage
		stage.act(delta);
		stage.draw();
	}
	
	protected void doRender(float delta) {
		// Base class does nothing
	}

	@Override
	public final void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public final void pause() {
		doPause();
	}
	
	protected void doPause() {
		// Base class does nothing
	}

	@Override
	public final void resume() {
		doResume();
	}
	
	protected void doResume() {
		// Base class does nothing
	}

	@Override
	public final void hide() {
		doHide();
	}

	protected void doHide() {
		// Base class does nothing
	}
	
	@Override
	public final void dispose() {
		// TODO Auto-generated method stub
	}

}
