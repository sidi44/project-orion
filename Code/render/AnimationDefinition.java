package render;

/**
 * This class should be used by an XML parser to set the field values.
 * An instance of this class should then be passed on to AnimationConfiguration. 
 *
 */
public class AnimationDefinition {

	private String animationGroupName;
	private String animationName;
	private String filename;
	private int rows;
	private int columns;
	private int startFrame;
	private int endFrame;
	private float frameDuration;
	
	public String getAnimationGroupName() {
		return animationGroupName;
	}
	public String getAnimationName() {
		return animationName;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getStartFrame() {
		return startFrame;
	}
	
	public int getEndFrame() {
		return endFrame;
	}
	
	public float getFrameDuration() {
		return frameDuration;
	}
	
	public void setAnimationGroupName(String animationGroupName) {
		this.animationGroupName = animationGroupName;
	}
	
	public void setAnimationName(String animationName) {
		this.animationName = animationName;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	public void setStartFrame(int startFrame) {
		this.startFrame = startFrame;
	}
	
	public void setEndFrame(int endFrame) {
		this.endFrame = endFrame;
	}
	
	public void setFrameDuration(float frameDuration) {
		this.frameDuration = frameDuration;
	}
}
