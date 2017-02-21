package sound;

public class SoundConfiguration {

    private boolean soundOn;
    private boolean musicOn;
    private int soundLevel;
    private int musicLevel;
    
    public SoundConfiguration() {
        soundOn = true;
        musicOn = true;
        soundLevel = 5;
        musicLevel = 5;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void setSoundOn(boolean playSounds) {
        this.soundOn = playSounds;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setMusicOn(boolean playMusic) {
        this.musicOn = playMusic;
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