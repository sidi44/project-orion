package input;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.InputProcessor;

public class InputFilter implements InputProcessor {

	private InputProcessor activeInputProcessor;
	private final Map<String, InputProcessor> screenInputProcessors;
	
	public InputFilter() {
		screenInputProcessors = new HashMap<String, InputProcessor>();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.keyDown(keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.keyUp(keycode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.keyTyped(character);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.touchDown(screenX, screenY, 
					   pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.touchUp(screenX, screenY, 
						 pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if (activeInputProcessor != null) {
			return activeInputProcessor.scrolled(amount);
		}
		return false;
	}
	
	/**
	 * @param inputProcessor The root input processor used by the specified screen.
	 * @param screenName The screen name.
	 */
	public void addInputProcessorForScreen(String screenName,
										   InputProcessor inputProcessor) {
		
		if (screenName == null || inputProcessor == null) {
			throw new IllegalArgumentException("The screen name and the input"
											 + " processor cannot be null.");
		}
		
		screenInputProcessors.put(screenName, inputProcessor);
	}
	
	
	/**
	 * Sets the active input processor based on the screen name.
	 * 
	 * @param screenName The name of the active screen.
	 * @throws IllegalArgumentException if there is no corresponding input 
	 * processor for the active screen.
	 */
	public void setActiveInputProcessor(String screenName) {
		
		activeInputProcessor = screenInputProcessors.get(screenName);
		if (activeInputProcessor == null) {
			throw new IllegalArgumentException("No input processor for screen " 
										     + screenName + " has been added.");
		}
	}
}
