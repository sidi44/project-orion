package sound;

import java.util.HashMap;
import java.util.Map;

import physics.PhysicsEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import callback.Event;
import callback.Receiver;

public class SoundManager implements Receiver {

	private Map<SoundType, Sound> sounds;
	private Map<MusicType, Music> music;
	
	private EventSoundProcessor eventProcessor;
	
	public SoundManager() {
		eventProcessor = new EventSoundProcessor();
		
		loadSounds();
		loadMusic();
	}
	
	private void loadSounds() {
		sounds = new HashMap<SoundType, Sound>();
		
		sounds.put(
			SoundType.Collision, 
			Gdx.audio.newSound(Gdx.files.internal("data/sound/slurp.mp3"))
		);
	}
	
	private void loadMusic() {
		music = new HashMap<MusicType, Music>();
		
		music.put(
			MusicType.Default, 
			Gdx.audio.newMusic(Gdx.files.internal("data/music/undisturbed.mp3"))
		);
	}
	
	@Override
	public void receive(Event event) {
		
		SoundType type = convertToType(event);
		Sound sound = sounds.get(type);
		if (sound != null) {
			sound.play();
		}
		
	}
	
	public SoundType convertToType(Event event) {
		
		SoundType type = SoundType.None;
		
		if (event instanceof PhysicsEvent) {
			PhysicsEvent physicsEvent = (PhysicsEvent) event;
			physicsEvent.accept(eventProcessor);
			type = eventProcessor.getSoundType();
		}
		
		return type;
	}
	

}
