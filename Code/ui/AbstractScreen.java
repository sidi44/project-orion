package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

abstract class AbstractScreen implements Screen {

	private Stage backgroundStage;
	private ScreenManager manager;
	private InputMultiplexer inputMultiplexer;
	
	public AbstractScreen(ScreenManager manager) {
		this.manager = manager;
		
		Camera camera = new OrthographicCamera();
		Viewport viewport = new FillViewport(Gdx.graphics.getWidth(), 
											 Gdx.graphics.getHeight(), 
											 camera);
		this.backgroundStage = new Stage(viewport);
		
		this.inputMultiplexer = new InputMultiplexer();
		
		initialise();
		addActors();
	}
	
	protected void initialise() {
		// Base class does nothing
	}
	
	protected void addActors() {
		// Base class does nothing
	}
	
	protected Stage getBackgroundStage() {
		return backgroundStage;
	}
	
	protected ScreenManager getManager() {
		return manager;
	}
	
	protected void setBackgroundImage(Image background) {
		backgroundStage.addActor(background);
	}
	
	protected void addInputProcessor(InputProcessor processor) {
		inputMultiplexer.addProcessor(processor);
	}
	
	@Override
	public final void show() {
		inputMultiplexer.addProcessor(backgroundStage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		doShow();
	}
	
	protected void doShow() {
		// Bass class does nothing.
	}

	@Override
	public final void render(float delta) {
		
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Draw our stage
		backgroundStage.getViewport().apply(true);
		backgroundStage.act(delta);
		backgroundStage.draw();
		
		// Carry out any derived class rendering
		doRender(delta);
	}
	
	protected void doRender(float delta) {
		// Base class does nothing
	}

	@Override
	public final void resize(int width, int height) {
		backgroundStage.getViewport().update(width, height, true);
		doResize(width, height);
	}
	
	protected void doResize(int width, int height) {
		// Base class does nothing
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
		
	}

}
