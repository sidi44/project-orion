package render;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * This class should be used by the PredatorPreyGame class to store all the
 * settings for loading spritesheets, their associated animations, etc. Once
 * all the values have been set, an instance of this class should then be 
 * passed on to the renderer.
 */
public class RendererConfiguration {
	
	public static final String ANIMATION_DOWN_STOP = "DOWN-STOP";
	public static final String ANIMATION_DOWN = "DOWN";
	public static final String ANIMATION_UP_STOP = "UP-STOP";
	public static final String ANIMATION_UP = "UP";
	public static final String ANIMATION_LEFT_STOP = "LEFT-STOP";
	public static final String ANIMATION_LEFT = "LEFT";
	public static final String ANIMATION_RIGHT_STOP = "RIGHT-STOP";
	public static final String ANIMATION_RIGHT = "RIGHT";
	private boolean allowRotations;
	
	private String wallTextureFilename;
	private float wallTextureScale;
	
	private Vector2 backgroundDimensions;
	private String backroundFilename;
	
	private final List<AnimationGroupDefinition> animationGroupDefinitions =
			  new ArrayList<AnimationGroupDefinition>();

	public void addAnimationGroup(AnimationGroupDefinition animGroupDef) {
		if (animGroupDef != null) {
			animationGroupDefinitions.add(animGroupDef);
		}
	}
	
	public List<AnimationGroupDefinition> getAnimationGroupDefinitions() {
		return animationGroupDefinitions;
	}
	
	public String getBackgroundFilename() {
		return this.backroundFilename;
	}
	
	public void setBackgroundFilename(String filename) {
		this.backroundFilename = filename;
	}
	
	public Vector2 getBackgroundDimensions() {
		return this.backgroundDimensions;
	}
	
	/**
	 * The background dimensions should be given in world measurements and not
	 * pixels
	 * @param width - the width of the background image
	 * @param height - the height of the background image
	 */
	public void setBackgroundDimensions(float width, float height) {
		if (backgroundDimensions == null) {
			backgroundDimensions = new Vector2();
		}
		backgroundDimensions.x = width;
		backgroundDimensions.y = height;
	}
	
	public float getWallTextureScale() {
		return this.wallTextureScale;
	}
	
	public void setWallTextureScale(float scale) {
		if (scale < 0 || scale > 1) {
			throw new IllegalArgumentException("Scale must be between 0 and 1");
		}
		this.wallTextureScale = scale;
	}
	
	public String getWallTextureFilename() {
		return this.wallTextureFilename;
	}
	
	public void setWallTextureFilename(String filename) {
		this.wallTextureFilename = filename;
	}

	public boolean isAllowRotations() {
		return allowRotations;
	}

	public void setAllowRotations(boolean allowRotations) {
		this.allowRotations = allowRotations;
	}
}
