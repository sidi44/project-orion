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
 * 
 * @version 2015-08-09
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

	
	/**
	 * Default constructor for RendererConfiguration.
	 * 
	 * Sets parameters to their default values.
	 */
	public RendererConfiguration() {
		this.allowRotations = true;
		this.wallTextureFilename = "wall.png";
		this.wallTextureScale = 1.0f;
		this.backgroundBottomLeft = new Vector2(0f, 0f);
		this.backgroundTopRight = new Vector2(100f, 100f);
		this.backroundFilename = "background_1.png";
		
		setupDefaultAnimationDefinitions();
	}
	
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
	
	/**
	 * Create and add the default animation definitions.
	 */
	private void setupDefaultAnimationDefinitions() {
		
		// Define the Predator Animation Group
		AnimationGroupDefinition predatorDef = new AnimationGroupDefinition();
		predatorDef.setAnimationGroupName("Predator");
		predatorDef.setFilename("predator.png");
		predatorDef.setColumns(11);
		predatorDef.setRows(4);
		
		// Define the individual predator animations
		AnimationDefinition def = new AnimationDefinition();
		def.setAnimationName(RendererConfiguration.ANIMATION_DOWN_STOP);
		def.setStartFrame(1);
		def.setEndFrame(1);
		def.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def);
		
		AnimationDefinition def2 = new AnimationDefinition();
		def2.setAnimationName(RendererConfiguration.ANIMATION_DOWN);
		def2.setStartFrame(2);
		def2.setEndFrame(7);
		def2.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def2);
		
		AnimationDefinition def3 = new AnimationDefinition();
		def3.setAnimationName(RendererConfiguration.ANIMATION_UP_STOP);
		def3.setStartFrame(12);
		def3.setEndFrame(12);
		def3.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def3);
		
		AnimationDefinition def4 = new AnimationDefinition();
		def4.setAnimationName(RendererConfiguration.ANIMATION_UP);
		def4.setStartFrame(13);
		def4.setEndFrame(18);
		def4.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def4);
		
		AnimationDefinition def5 = new AnimationDefinition();
		def5.setAnimationName(RendererConfiguration.ANIMATION_LEFT_STOP);
		def5.setStartFrame(23);
		def5.setEndFrame(23);
		def5.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def5);
		
		AnimationDefinition def6 = new AnimationDefinition();
		def6.setAnimationName(RendererConfiguration.ANIMATION_LEFT);
		def6.setStartFrame(24);
		def6.setEndFrame(29);
		def6.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def6);
		
		AnimationDefinition def7 = new AnimationDefinition();
		def7.setAnimationName(RendererConfiguration.ANIMATION_RIGHT_STOP);
		def7.setStartFrame(34);
		def7.setEndFrame(34);
		def7.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def7);
		
		AnimationDefinition def8 = new AnimationDefinition();
		def8.setAnimationName(RendererConfiguration.ANIMATION_RIGHT);
		def8.setStartFrame(35);
		def8.setEndFrame(40);
		def8.setFrameDuration(1 / 60f);
		predatorDef.addAnimation(def8);
		
		// Define the Prey Animation Group
		AnimationGroupDefinition preyDef = new AnimationGroupDefinition();
		preyDef.setAnimationGroupName("Prey");
		preyDef.setFilename("prey.png");
		preyDef.setColumns(11);
		preyDef.setRows(4);
		
		// Define the Prey animations
		AnimationDefinition def9 = new AnimationDefinition();
		def9.setAnimationName(RendererConfiguration.ANIMATION_DOWN_STOP);
		def9.setStartFrame(1);
		def9.setEndFrame(1);
		def9.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def9);
		
		AnimationDefinition def10 = new AnimationDefinition();
		def10.setAnimationName(RendererConfiguration.ANIMATION_DOWN);
		def10.setStartFrame(2);
		def10.setEndFrame(7);
		def10.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def10);

		AnimationDefinition def11 = new AnimationDefinition();
		def11.setAnimationName(RendererConfiguration.ANIMATION_UP_STOP);
		def11.setStartFrame(12);
		def11.setEndFrame(12);
		def11.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def11);
		
		AnimationDefinition def12 = new AnimationDefinition();
		def12.setAnimationName(RendererConfiguration.ANIMATION_UP);
		def12.setStartFrame(13);
		def12.setEndFrame(18);
		def12.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def12);
		
		AnimationDefinition def13 = new AnimationDefinition();
		def13.setAnimationName(RendererConfiguration.ANIMATION_LEFT_STOP);
		def13.setStartFrame(23);
		def13.setEndFrame(23);
		def13.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def13);
		
		AnimationDefinition def14 = new AnimationDefinition();
		def14.setAnimationName(RendererConfiguration.ANIMATION_LEFT);
		def14.setStartFrame(24);
		def14.setEndFrame(29);
		def14.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def14);
		
		AnimationDefinition def15 = new AnimationDefinition();
		def15.setAnimationName(RendererConfiguration.ANIMATION_RIGHT_STOP);
		def15.setStartFrame(34);
		def15.setEndFrame(34);
		def15.setFrameDuration(1 / 60f);
		preyDef.addAnimation(def15);
		
		AnimationDefinition def16 = new AnimationDefinition();
		def16.setAnimationName(RendererConfiguration.ANIMATION_RIGHT);
		def16.setStartFrame(35);
		def16.setEndFrame(40);
		def16.setFrameDuration(1 / 60f);	
		preyDef.addAnimation(def16);
		
		
		// Define the Pill Animation Group
		AnimationGroupDefinition pillDef = new AnimationGroupDefinition();
		pillDef.setAnimationGroupName("Pill");
		pillDef.setFilename("coins.png");
		pillDef.setColumns(8);
		pillDef.setRows(3);
		
		// Define the Pill animations
		AnimationDefinition def17 = new AnimationDefinition();
		def17.setAnimationName("");
		def17.setStartFrame(1);
		def17.setEndFrame(24);
		def17.setFrameDuration(1.0f);
		pillDef.addAnimation(def17);
		
		
		// Define the Predator Power Up Animation Group
		AnimationGroupDefinition predatorPowerUpDef = 
				new AnimationGroupDefinition();
		predatorPowerUpDef.setAnimationGroupName("PowerUpPredator");
		predatorPowerUpDef.setFilename("icons-pow-up.png");
		predatorPowerUpDef.setColumns(3);
		predatorPowerUpDef.setRows(4);
		
		// Define the Predator Power Up animations
		AnimationDefinition def18 = new AnimationDefinition();
		def18.setAnimationName("");
		def18.setStartFrame(12);
		def18.setEndFrame(12);
		def18.setFrameDuration(1.0f);
		predatorPowerUpDef.addAnimation(def18);
		
		
		// Define the Predator Power Up Animation Group
		AnimationGroupDefinition preyPowerUpDef = 
				new AnimationGroupDefinition();
		preyPowerUpDef.setAnimationGroupName("PowerUpPrey");
		preyPowerUpDef.setFilename("icons-pow-up.png");
		preyPowerUpDef.setColumns(3);
		preyPowerUpDef.setRows(4);
		
		// Define the Predator Power Up animations
		AnimationDefinition def19 = new AnimationDefinition();
		def19.setAnimationName("");
		def19.setStartFrame(9);
		def19.setEndFrame(9);
		def19.setFrameDuration(1.0f);
		preyPowerUpDef.addAnimation(def19);
		
		
		// Finally, add the animations groups.
		addAnimationGroup(predatorDef);
		addAnimationGroup(preyDef);
		addAnimationGroup(pillDef);
		addAnimationGroup(predatorPowerUpDef);
		addAnimationGroup(preyPowerUpDef);
	}
}
