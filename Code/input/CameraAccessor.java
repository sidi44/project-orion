package input;

import com.badlogic.gdx.math.Vector3;

public interface CameraAccessor {
	
	Vector3 screenToWorld(Vector3 screenCoords);
	
	Vector3 cameraPosition();
	
}
