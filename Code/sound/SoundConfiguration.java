package sound;

public class SoundConfiguration {

	private boolean playSounds;
	private boolean playMusic;
	private int soundLevel;
	private int musicLevel;
	
	public SoundConfiguration() {
		playSounds = true;
		playMusic = true;
		soundLevel = 5;
		musicLevel = 5;
	}

	public boolean playSounds() {
		return playSounds;
	}

	public void setPlaySounds(boolean playSounds) {
		this.playSounds = playSounds;
	}

	public boolean playMusic() {
		return playMusic;
	}

	public void setPlayMusic(boolean playMusic) {
		this.playMusic = playMusic;
	}

	public int getSoundLevel() {
		return soundLevel;
	}

	public void setSoundLevel(int soundLevel) {
		if (soundLevel < 0 || soundLevel > 11) {
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
		if (soundLevel < 0 || soundLevel > 11) {
			System.err.println("Sound level out of range. "
					+ "Sound level will not be set.");
			return;
		}
		this.musicLevel = musicLevel;
	}
	
}
