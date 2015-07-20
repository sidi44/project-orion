package render;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AnimationGroupDefinition")
public class AnimationGroupDefinition {
	
	private String animationGroupName;
	private int rows;
	private int columns;
	private String filename;
	private List<AnimationDefinition> animationDefinitions;
	
	public AnimationGroupDefinition() {
		animationDefinitions = new ArrayList<AnimationDefinition>();	
	}
	
	public String getAnimationGroupName() {
		return animationGroupName;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public List<AnimationDefinition> getAnimationDefinitions() {
		return animationDefinitions;
	}
	
	@XmlElement (name = "AnimationDefinition")
	public void setAnimationDefinitions(
			List<AnimationDefinition> animationDefinitions) {
		this.animationDefinitions = animationDefinitions;
	}
	
	@XmlElement (name = "AnimationGroupName")
	public void setAnimationGroupName(String animationGroupName) {
		this.animationGroupName = animationGroupName;
	}
	
	@XmlElement (name = "Rows")
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	@XmlElement (name = "Columns")
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	@XmlElement (name = "Filename")
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void addAnimation(AnimationDefinition animationDef) {
		
		if (animationDef != null) {
			animationDefinitions.add(animationDef);
		}
	}		
}
