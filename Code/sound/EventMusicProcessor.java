package sound;

import game.PredatorPreyGame;
import ui.UIEventScreenChange;
import ui.UIEventVisitor;

class EventMusicProcessor implements UIEventVisitor {

	private MusicType type;
	private PredatorPreyGame game;
	
	public EventMusicProcessor(PredatorPreyGame game) {
		type = MusicType.None;
		this.game = game;
	}
	
	public MusicType getMusicType() {
		return type;
	}
	
	@Override
	public void visit(UIEventScreenChange event) {
		type = SoundUtils.musicFromGameType(game.getGameType());
	}

}
