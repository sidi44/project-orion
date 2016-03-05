package sound;

import physics.PhysicsEventContact;
import physics.PhysicsEventVisitor;

public class EventSoundProcessor implements PhysicsEventVisitor {

	private SoundType type;
	
	public EventSoundProcessor() {
		type = SoundType.None;
	}
	
	public SoundType getSoundType() {
		return type;
	}
	
	@Override
	public void visit(PhysicsEventContact event) {
		if (event.isPredatorPreyContact()) {
			type = SoundType.Collision;
		} else {
			type = SoundType.None;
		}
	}

}
