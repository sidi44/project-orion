package sound;

import game.GameStatus;
import ui.UIEventScreenChange;
import ui.UIEventVisitor;

class EventMusicProcessor implements UIEventVisitor {

	private MusicType type;
	private GameStatus gameStatus;
	
	public EventMusicProcessor(GameStatus status) {
		type = MusicType.None;
		this.gameStatus = status;
	}
	
	public MusicType getMusicType() {
		return type;
	}
	
	@Override
	public void visit(UIEventScreenChange event) {
		type = SoundUtils.musicFromStatus(gameStatus);
	}

}
