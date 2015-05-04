package input;

import logic.Direction;
import logic.Move;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class UserInputProcessor implements InputProcessor {

	private Move mMove;
	
	public UserInputProcessor() {
		mMove = new Move();
	}
	
	public Move getNextMove() {
		return mMove;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		
		boolean keyProcessed = true;
		
		switch (keycode) {
		
			case Input.Keys.LEFT:
				mMove.setDirection( Direction.Left );
				break;
			
			case Input.Keys.RIGHT:
				mMove.setDirection( Direction.Right );
				break;
			
			case Input.Keys.UP:
				mMove.setDirection( Direction.Up );
				break;
				
			case Input.Keys.DOWN:
				mMove.setDirection( Direction.Down );
				break;
			
			default:
				keyProcessed = false;
				break;
		}
		
		return keyProcessed;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
