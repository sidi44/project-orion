package game;

import input.UserInputProcessor;
import logic.GameLogic;
import physics.PhysicsProcessor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class PredatorPreyGame extends ApplicationAdapter {

	GameLogic gameLogic;
	PhysicsProcessor physProcessor;
	UserInputProcessor userInputProcessor;
	
	@Override
	public void create() {
		gameLogic = new GameLogic();
		// instantiate class implementing PhysicsProcessor
		userInputProcessor = new UserInputProcessor();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	}
}
