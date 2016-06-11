package physics;

public class PhysicsData {

	private PhysicsBody parent;
	
	public PhysicsData(PhysicsBody parent) {
		this.parent = parent;
	}
	
	public PhysicsBody getParent() {
		return parent;
	}
	
}
