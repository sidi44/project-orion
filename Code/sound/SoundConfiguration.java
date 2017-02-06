package sound;

public class SoundConfiguration {

	private int soundLevel;
	private int musicLevel;
	
	public SoundConfiguration() {
		soundLevel = 5;
		musicLevel = 5;
	}

	public boolean playSounds() {
		return soundLevel > 0;
	}

	public boolean playMusic() {
		return musicLevel > 0;
	}

	public int getSoundLevel() {
		return soundLevel;
	}

	public void setSoundLevel(int soundLevel) {
		if (soundLevel < 0 || soundLevel > 10) {
			System.err.println("Sound level out of range. "
					+ "Sound level will not be set.");
			return;
		}		
		this.soundLevel = soundLevel;
	}

	public int getMusicLevel() {
		return musicLevel;
	}

	public void setMusicLevel(int musicLevel) {
		if (soundLevel < 0 || soundLevel > 10) {
			System.err.println("Sound level out of range. "
					+ "Sound level will not be set.");
			return;
		}
		this.musicLevel = musicLevel;
	}
}
