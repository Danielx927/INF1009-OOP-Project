package game.gdx.lwjgl3.audio;

import com.badlogic.gdx.audio.Music;

public class MusicQueueItem {
	private Music track;
	private Boolean looping;
	private float vol;
	
	public MusicQueueItem(Music track, Boolean loop, float volume) {
		setTrack(track);
		setLooping(loop);
		setVol(volume);
	}

	public Music getTrack() {
		return track;
	}

	public void setTrack(Music key) {
		this.track = key;
	}

	public Boolean getLooping() {
		return looping;
	}

	public void setLooping(Boolean looping) {
		this.looping = looping;
		this.track.setLooping(this.looping);
	}

	public float getVol() {
		return vol;
	}

	public void setVol(float vol) {
		this.vol = vol;
		this.track.setVolume(this.vol);
	}
	
	public void dispose() {
		track.dispose();
	}
}
