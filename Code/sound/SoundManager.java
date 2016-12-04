package sound;

import game.GameStatus;
import game.GameType;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import physics.PhysicsEvent;
import ui.UIEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import callback.Event;
import callback.Receiver;

public class SoundManager implements Receiver {

	private Map<SoundType, Sound> allSounds;
	private Map<MusicType, Music> allMusic;
	
	private boolean soundOn;
	private boolean musicOn;
	
	private int soundVolume;
	private int musicVolume;
	
	private EventSoundProcessor eventSoundProcessor;
	private EventMusicProcessor eventMusicProcessor;
	
	private GameStatus gameStatus;
	
	public SoundManager(SoundConfiguration config, GameStatus status) {
		eventSoundProcessor = new EventSoundProcessor();
		eventMusicProcessor = new EventMusicProcessor(status);
		
		this.gameStatus = status;
		
		loadSounds();
		loadMusic();
		
		update(config);
	}
	
	public void update(SoundConfiguration config) {
		
		boolean oldMusicOn = musicOn;
		
		soundOn = config.playSounds();
		musicOn = config.playMusic();
		soundVolume = config.getSoundLevel();
		musicVolume = config.getMusicLevel();
		
		if (oldMusicOn != musicOn) {
			if (musicOn) {
				playMusic(SoundUtils.musicFromStatus(gameStatus));
			} else {
				stopMusicPlaying();
			}
		}
		
		updateMusicVolume();
	}
	
	private void loadSounds() {
		allSounds = new HashMap<SoundType, Sound>();
		
		allSounds.put(
			SoundType.Collision, 
			Gdx.audio.newSound(Gdx.files.internal("data/sound/slurp.mp3"))
		);
	}
	
	private void loadMusic() {
		allMusic = new HashMap<MusicType, Music>();
		
		allMusic.put(
			MusicType.Game, 
			Gdx.audio.newMusic(Gdx.files.internal("data/music/undisturbed.mp3"))
		);
		allMusic.put(
			MusicType.Menu, 
			Gdx.audio.newMusic(Gdx.files.internal("data/music/solitude.mp3"))
		);
		
		// We want all our music to loop
		for (Music music : allMusic.values()) {
			music.setLooping(true);
		}
	}
	
	@Override
	public void receive(Event event) {
		
		if (event instanceof PhysicsEvent) {
			PhysicsEvent physicsEvent = (PhysicsEvent) event;
			processPhysicsEvent(physicsEvent);
		} else if (event instanceof UIEvent) {
			UIEvent uiEvent = (UIEvent) event;
			processUIEvent(uiEvent);
		}
		
	}
	
	private void processPhysicsEvent(PhysicsEvent event) {
		SoundType type = convertToSoundType(event);
		Sound sound = allSounds.get(type);
		if (sound != null && soundOn()) {
			sound.play(convertSoundVolume());
		}
	}
	
	private SoundType convertToSoundType(PhysicsEvent event) {
		
		SoundType soundType = SoundType.None;
		
		// Pick the right sound based on the game type. If the game is actually
		// being played, process the physics event to work out the sound. If
		// the game isn't being played, use no sound.
		GameType gameType = gameStatus.getGameType();
		switch (gameType) {
			case Levels:
			case Sandbox:
				event.accept(eventSoundProcessor);
				soundType = eventSoundProcessor.getSoundType();
				break;
			case MainMenu:
			case NotPlaying:
				soundType = SoundType.None;
				break;
			default:
				System.err.println("Unknown game type");
				break;		
		}

		return soundType;
	}
	
	private void processUIEvent(UIEvent event) {
		MusicType type = convertToMusicType(event);
		playMusic(type);
	}
	
	private MusicType convertToMusicType(UIEvent event) {
		event.accept(eventMusicProcessor);
		MusicType type = eventMusicProcessor.getMusicType();
		
		return type;
	}
	
	private float convertSoundVolume() {
		float maxSoundVolume = 11f;
		return 1.0f - (maxSoundVolume - soundVolume) / maxSoundVolume;
	}
	
	private float convertMusicVolume() {
		float maxMusicVolume = 11f;
		return 1.0f - (maxMusicVolume - musicVolume) / maxMusicVolume;
	}
	
	private boolean soundOn() {
		return soundOn;
	}
	
	private boolean musicOn() {
		return musicOn;
	}
	
	private MusicType musicPlaying() {
		
		for (Entry<MusicType, Music> entry : allMusic.entrySet()) {
			if (entry.getValue().isPlaying()) {
				return entry.getKey();
			}
		}
		
		return MusicType.None;
	}
	
	private void stopMusicPlaying() {
		for (Entry<MusicType, Music> entry : allMusic.entrySet()) {
			Music music = entry.getValue();
			if (music.isPlaying()) {
				music.stop();
			}
		}
	}
	
	private void updateMusicVolume() {
		MusicType type = musicPlaying();
		if (allMusic.containsKey(type)) {
			Music music = allMusic.get(type);
			music.setVolume(convertMusicVolume());
		}
	}
	
	private void playMusic(MusicType type) {
		
		MusicType current = musicPlaying();
		if (current == type) {
			updateMusicVolume();
			return;
		}
		
		stopMusicPlaying();
		
		if (musicOn() && allMusic.containsKey(type)) {
			Music music = allMusic.get(type);	
			music.setVolume(convertMusicVolume());
			music.play();
		}

	}
	
}
