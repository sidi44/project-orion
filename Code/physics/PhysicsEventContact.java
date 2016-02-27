package physics;

import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsEventContact extends PhysicsEvent {

	private Body first;
	private Body second;
	
	protected PhysicsEventContact(Body first, Body second) {
		super("PhysicsContact");
		
		this.first = first;
		this.second = second;
	}

	public PhysicsBodyType getFirstType() {
		return getPhysicsType(first);
	}
	
	public PhysicsBodyType getSecondType() {
		return getPhysicsType(second);
	}
	
	private PhysicsBodyType getPhysicsType(Body body) {
		PhysicsData data = (PhysicsData) body.getUserData();
		return data.getType();
	}
	
	public boolean isPredatorPreyContact() {
		
		PhysicsData dataFirst = (PhysicsData) first.getUserData();
		PhysicsData dataSecond = (PhysicsData) second.getUserData();
		
		PhysicsBodyType typeFirst = dataFirst.getType();
		PhysicsBodyType typeSecond = dataSecond.getType();
		
		if (typeFirst == PhysicsBodyType.Predator && 
			typeSecond == PhysicsBodyType.Prey) {
			return true;
		} else if (typeFirst == PhysicsBodyType.Prey && 
			typeSecond == PhysicsBodyType.Predator) {
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public void accept(PhysicsEventVisitor visitor) {
		visitor.visit(this);
	}

}
