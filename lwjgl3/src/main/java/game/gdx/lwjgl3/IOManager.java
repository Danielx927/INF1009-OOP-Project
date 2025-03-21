package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class IOManager implements InputProcessor, Audio {
	private static IOManager instance;
	private List<InputEvent> inputList;
	private HashMap<String, Sound> soundEffects;
	private HashMap<String, Music> playlist;
	private Music currentTrack;
    private Tool tool;

    public IOManager() {
    	soundEffects = new HashMap<String, Sound>();
    	playlist = new HashMap<String, Music>();
    	inputList = new ArrayList<InputEvent>();

    	this.populateSfxList();
    	this.populatePlaylist();
    }
    
    public static IOManager getInstance() {
        if (instance == null) {
            instance = new IOManager();
        }
        return instance;
    }
    
    public void addTool(Tool tooll) {
    	Gdx.graphics.setSystemCursor(SystemCursor.None);
    	tool = tooll;
//        System.out.println("Tool created:." + tool);

    }
    
    public void addInputEvent(InputEvent IE) {
    	inputList.add(IE);
    }
    
	private void populateSfxList() {
		Sound hammerSmash = this.newSound(Gdx.files.internal("sounds/hammer_smash.mp3"));
		Sound entitySpawn1 = this.newSound(Gdx.files.internal("sounds/entity_spawn1.mp3"));
		Sound entityCollide1 = this.newSound(Gdx.files.internal("sounds/entity_collide1.mp3"));
		Sound damageTaken1 = this.newSound(Gdx.files.internal("sounds/damage_taken1.mp3"));

		soundEffects.put("damageTaken1", damageTaken1); // yet to be used
		soundEffects.put("entityCollide1", entityCollide1); // mole squish
		soundEffects.put("entitySpawn1", entitySpawn1); // mole pop up
		soundEffects.put("hammerSmash", hammerSmash); // hammer hit sound
	}
	
	private void populatePlaylist() {
		Music starlings = this.newMusic(Gdx.files.internal("music/starlings.mp3"));
		Music jungle = this.newMusic(Gdx.files.internal("music/jungle.mp3"));
		Music mainMenu = this.newMusic(Gdx.files.internal("music/main-menu.mp3"));

		playlist.put("starlings", starlings);
		playlist.put("jungle", jungle); // game bg music
		playlist.put("main-menu", mainMenu);

	}
	
	public void playHitSound() {
	    playSound("entityCollide1", 0.5f);  // ✅ Play hit sound at 50% volume
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
		InputEvent ie = new InputEvent();
		ie.touchDown(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		InputEvent ie = new InputEvent();
		ie.touchUp(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		InputEvent ie = new InputEvent();
		ie.touchCancelled(screenX, screenY, pointer, button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		InputEvent ie = new InputEvent();
		ie.touchDragged(screenX, screenY, pointer);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		InputEvent ie = new InputEvent();
		ie.mouseMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		InputEvent ie = new InputEvent();
		ie.scrolled(amountX, amountY);
		return false;
	}
	
	public void handleInputs() {
        for (int i = 0; i < inputList.size(); i++) {
        	InputEvent input = inputList.get(i);
        	switch(input.getInputType()) {
			case 0: // Keyboard event
				// No keyboard events...
				break;
			case 1: // Mouse move event
				if (tool != null) {
					tool.setCoords(input.xPos, input.yPos);
				}
				break;
			case 2: // Mouse click event
				if (tool != null) {
					tool.setCoords(input.xPos, input.yPos);
					tool.clickEvent();
				}
				break;
			case 3: // Mouse scroll
				// No scroll events...
				break;
			}
        	// Deletes the element from the list.
			inputList.remove(i);
			i--;
        }
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
		
	public void playSound(String key, float vol) {
		try {
			soundEffects.get(key).play(vol);
		}  
		catch (NullPointerException e) {
            System.out.print("NullPointerException: Ensure the key exists in the soundEffects HashMap.\n");
        }
	}
	
	// This playMusic function is for starting a new track.
	public void playMusic(String key, Boolean looping, float vol) {
		if (currentTrack != null) currentTrack.stop();
		try {
			currentTrack = playlist.get(key);
			currentTrack.setLooping(looping);
			currentTrack.setVolume(vol);
			currentTrack.play();
		}
		catch (NullPointerException e) {
            System.out.print("NullPointerException: Ensure the key exists in the playlist HashMap.\n");
		}
	}
	
	// This playMusic function is for resuming the current track.
	public void playMusic() {
		if (currentTrack != null) currentTrack.play();
	}
	
	public boolean isCurrentlyPlaying() {
		try {
			if (currentTrack.isPlaying()) {
				return true;
			} 
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public void pauseMusic() {
		currentTrack.pause();
	}
	
	public void stopMusic() {
		if (currentTrack != null) {
			currentTrack.stop();
			currentTrack = null;
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
		
		inputList = null;
	}
}
