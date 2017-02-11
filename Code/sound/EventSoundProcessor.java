package sound;

import game.GameType;
import game.PredatorPreyGame;
import physics.PhysicsEventContact;
import physics.PhysicsEventVisitor;

class EventSoundProcessor implements PhysicsEventVisitor {

	private SoundType type;
	private PredatorPreyGame game;
	
	public EventSoundProcessor(PredatorPreyGame game) {
		type = SoundType.None;
		this.game = game;
	}
	
	public SoundType getSoundType() {
		return type;
	}
	
	@Override
	public void visit(PhysicsEventContact event) {
		
		// Pick the right sound based on the game type. If the game is actually
		// being played, work out the sound based on the physics event. If the 
		// game isn't being played, use no sound.
		GameType gameType = game.getGameType();
		switch (gameType) {
			case Levels:
			case Sandbox:
				if (event.isPredatorPreyContact()) {
					type = SoundType.Collision;
				} else {
					type = SoundType.None;
				}
				break;
			case MainMenu:
			case NotPlaying:
				type = SoundType.None;
				break;
			default:
				System.err.println("Unknown game type");
				break;
		}
		

	}
	
	

}
