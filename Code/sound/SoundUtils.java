package sound;

import game.GameStatus;
import game.GameType;

class SoundUtils {

	public static MusicType musicFromStatus(GameStatus status) {
		
		GameType gameType = status.getGameType();
		switch (gameType) {
			case Levels:
				return MusicType.Game;
			case NotPlaying:
				return MusicType.Menu;
			case Sandbox:
				return MusicType.Game;
			case MainMenu:
				return MusicType.Menu;
			default:
				System.err.println("Unknown game type");
				return MusicType.None;
		}
		
	}
	
}
