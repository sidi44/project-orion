package render;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.badlogic.gdx.math.Vector2;

/**
 * This class should be used by the PredatorPreyGame class to store all the
 * settings for loading spritesheets, their associated animations, etc. Once
 * all the values have been set, an instance of this class should then be 
 * passed on to the renderer.
 */
@XmlRootElement(name = "RendererConfiguration")
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
	
	private Vector2 backgroundBottomLeft;
	private Vector2 backgroundTopRight;
	
	private String backroundFilename;
	
	private List<AnimationGroupDefinition> animationGroupDefinitions =
			  new ArrayList<AnimationGroupDefinition>();

	public void addAnimationGroup(AnimationGroupDefinition animGroupDef) {
		if (animGroupDef != null) {
			animationGroupDefinitions.add(animGroupDef);
		}
	}
	
	public List<AnimationGroupDefinition> getAnimationGroupDefinitions() {
		return animationGroupDefinitions;
	}
	
	@XmlElement (name = "AnimationGroupDefinition")
	public void setAnimationGroupDefinitions(
			List<AnimationGroupDefinition> animationGroupDefinitions) {
		this.animationGroupDefinitions = animationGroupDefinitions;
	}
	
	public String getBackgroundFilename() {
		return this.backroundFilename;
	}
	
	@XmlElement (name = "BackgroundFilename")
	public void setBackgroundFilename(String filename) {
		this.backroundFilename = filename;
	}
	
	/**
	 * The background dimensions should be given in world measurements and not
	 * pixels
	 * @param bottomLeft - the bottom left coordinate of the background image
	 * @param topRight - the top right coordinate of the background image
	 */
	public void setBackgroundDimensions(Vector2 bottomLeft, Vector2 topRight) {
		this.backgroundBottomLeft = bottomLeft;
		this.backgroundTopRight = topRight;
	}
	
	public Vector2 getBackgroundBottomLeft() {
		return this.backgroundBottomLeft;
	}
	
	public Vector2 getBackgroundTopRight() {
		return this.backgroundTopRight;
	}
	
	public float getWallTextureScale() {
		return this.wallTextureScale;
	}
	
	@XmlElement (name = "WallTextureScale")
	public void setWallTextureScale(float scale) {
		if (scale < 0 || scale > 1) {
			throw new IllegalArgumentException("Scale must be between 0 and 1");
		}
		this.wallTextureScale = scale;
	}
	
	public String getWallTextureFilename() {
		return this.wallTextureFilename;
	}
	
	@XmlElement (name = "WallTextureFilename")
	public void setWallTextureFilename(String filename) {
		this.wallTextureFilename = filename;
	}

	public boolean isAllowRotations() {
		return allowRotations;
	}

	@XmlElement (name = "AllowRotations")
	public void setAllowRotations(boolean allowRotations) {
		this.allowRotations = allowRotations;
	}
}
