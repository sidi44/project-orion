package physics;

public class PhysicsEventContact extends PhysicsEvent {

	private PhysicsBody first;
	private PhysicsBody second;
	
	protected PhysicsEventContact(PhysicsBody first, PhysicsBody second) {
		super("PhysicsContact");
		
		this.first = first;
		this.second = second;
	}

	public PhysicsBodyType getFirstType() {
		return first.getType();
	}
	
	public PhysicsBodyType getSecondType() {
		return second.getType();
	}
	
	public boolean isPredatorPreyContact() {
		
		PhysicsBodyType typeFirst = first.getType();
		PhysicsBodyType typeSecond = second.getType();
		
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
