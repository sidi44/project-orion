package render;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * This class should be used by the PredatorPreyGame class to store all the
 * settings for loading spritesheets, their associated animations, etc. Once
 * all the values have been set, an instance of this class should then be 
 * passed on to the renderer.
 * 
 * @version 2016-03-25
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
	
	private Vector2 backgroundSize;
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
		this.backgroundSize = new Vector2(100f, 100f);
		this.backroundFilename = "Background_Purple.png";
		
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
	
	public void setAnimationGroupDefinitions(
			List<AnimationGroupDefinition> animationGroupDefinitions) {
		this.animationGroupDefinitions = animationGroupDefinitions;
	}
	
	public String getBackgroundFilename() {
		return this.backroundFilename;
	}
	
	public void setBackgroundFilename(String filename) {
		this.backroundFilename = filename;
	}
	
	/**
	 * Set the background size. The dimensions should be given in world 
	 * measurements and not pixels.
	 * The x value of the provided vector is the width and the y value the 
	 * height. 
	 * 
	 * @param size - the width and height of the background in world 
	 * coordinates.
	 */
	public void setBackgroundSize(Vector2 size) {
		this.backgroundSize = size;
	}
	
	public Vector2 getBackgroundSize() {
		return this.backgroundSize;
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
		pillDef.setFilename("Coins.png");
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
		predatorPowerUpDef.setFilename("power_ups.png");
		predatorPowerUpDef.setColumns(3);
		predatorPowerUpDef.setRows(4);

		// Define the Predator Power Up animations
		AnimationDefinition def18 = new AnimationDefinition();
		def18.setAnimationName("SlowDown");
		def18.setStartFrame(7);
		def18.setEndFrame(7);
		def18.setFrameDuration(1.0f);
		predatorPowerUpDef.addAnimation(def18);
		
		AnimationDefinition def19 = new AnimationDefinition();
		def19.setAnimationName("Teleport");
		def19.setStartFrame(8);
		def19.setEndFrame(8);
		def19.setFrameDuration(1.0f);
		predatorPowerUpDef.addAnimation(def19);
		
		AnimationDefinition def20 = new AnimationDefinition();
		def20.setAnimationName("Magnet");
		def20.setStartFrame(10);
		def20.setEndFrame(10);
		def20.setFrameDuration(1.0f);
		predatorPowerUpDef.addAnimation(def20);
		
		AnimationDefinition def21 = new AnimationDefinition();
		def21.setAnimationName("Freeze");
		def21.setStartFrame(11);
		def21.setEndFrame(11);
		def21.setFrameDuration(1.0f);
		predatorPowerUpDef.addAnimation(def21);
		
		AnimationDefinition def22 = new AnimationDefinition();
		def22.setAnimationName("SpeedUp");
		def22.setStartFrame(12);
		def22.setEndFrame(12);
		def22.setFrameDuration(1.0f);
		predatorPowerUpDef.addAnimation(def22);
		
		
		// Define the Predator Power Up Animation Group
		AnimationGroupDefinition preyPowerUpDef = 
				new AnimationGroupDefinition();
		preyPowerUpDef.setAnimationGroupName("PowerUpPrey");
		preyPowerUpDef.setFilename("icons-pow-up.png");
		preyPowerUpDef.setColumns(3);
		preyPowerUpDef.setRows(4);
		
		// Define the Prey Power Up animations
		AnimationDefinition def23 = new AnimationDefinition();
		def23.setAnimationName("");
		def23.setStartFrame(9);
		def23.setEndFrame(9);
		def23.setFrameDuration(1.0f);
		preyPowerUpDef.addAnimation(def23);
		
		
		// Finally, add the animations groups.
		addAnimationGroup(predatorDef);
		addAnimationGroup(preyDef);
		addAnimationGroup(pillDef);
		addAnimationGroup(predatorPowerUpDef);
		addAnimationGroup(preyPowerUpDef);
	}
}
