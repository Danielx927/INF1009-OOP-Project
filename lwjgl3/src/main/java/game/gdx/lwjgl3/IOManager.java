package game.gdx.lwjgl3;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class IOManager implements InputProcessor, Audio {
	private Vector2 touchPos, offset;
	private HashMap<String, Sound> soundEffects;
	private HashMap<String, Music> playlist;
	private Music currentTrack;
    private Tool tool;
	private SpriteBatch batch;


    public IOManager(Tool tooll) {
		Gdx.graphics.setSystemCursor(SystemCursor.None);
    	this.tool = tooll;
    	offset = new Vector2(-60,410);
    	batch = new SpriteBatch();
    	touchPos = new Vector2();
    	soundEffects = new HashMap<String, Sound>();
    	playlist = new HashMap<String, Music>();

    	this.populateSfxList();
    	this.populatePlaylist();
    	
    }
    
	public void populateSfxList() {
		Sound sfx1 = this.newSound(Gdx.files.internal("sounds/sfx1.mp3"));
		Sound entitySpawn1 = this.newSound(Gdx.files.internal("sounds/entity_spawn1.mp3"));
		
		soundEffects.put("generic1", sfx1);
		soundEffects.put("entitySpawn1", entitySpawn1);

	}
	
	public void populatePlaylist() {
		Music starlings = this.newMusic(Gdx.files.internal("music/starlings.mp3"));
		
		playlist.put("starlings", starlings);

	}
	

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		
        touchPos.set(screenX, screenY);
		tool.clickEvent();
		if (tool.getCooldown() == tool.getCDTimer()) { // Sound only plays off cd
			this.playSound("generic1", 0.3f);
		}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		tool.getSprite().setPosition(screenX + offset.x, -screenY + offset.y);
		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sound newSound(FileHandle fileHandle) {
		return Gdx.audio.newSound(fileHandle);
	}
		
	public void playSound(String code, float vol) {
		try {
			soundEffects.get(code).play(vol);
		}  
		catch (NullPointerException e) {
            System.out.print("NullPointerException: Ensure code exists as a key in the soundEffects HashMap.\n");
        }
	}
	
	public void playMusic(String code, Boolean looping, float vol) {
		if (currentTrack != null) currentTrack.stop();
		try {
			currentTrack = playlist.get(code);
			currentTrack.setLooping(looping);
			currentTrack.setVolume(vol);
			currentTrack.play();
		}
		catch (NullPointerException e) {
            System.out.print("NullPointerException: Ensure code exists as a key in the playlist HashMap.\n");
		}
	}

	@Override
	public Music newMusic(FileHandle file) {
		return Gdx.audio.newMusic(file);
	}

	@Override
	public boolean switchOutputDevice(String deviceIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getAvailableOutputDevices() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void dispose() {
		for (Map.Entry<String, Sound> item : soundEffects.entrySet())
			soundEffects.get(item.getKey()).dispose();
		
		for (Map.Entry<String, Music> item : playlist.entrySet())
			playlist.get(item.getKey()).dispose();
		
		batch.dispose();
	}
}
