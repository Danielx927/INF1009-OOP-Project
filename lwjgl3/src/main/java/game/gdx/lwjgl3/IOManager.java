package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class IOManager implements InputProcessor, Audio {
	private Vector2 touchPos, offset;
	private List<Sound> soundEffects;
    private Tool tool;
	private SpriteBatch batch;


    public IOManager(Tool tooll) {
    	this.tool = tooll;
    	offset = new Vector2(-60,410);
    	batch = new SpriteBatch();
    	touchPos = new Vector2();
    	soundEffects = new ArrayList<Sound>();
    	this.populateSfxList();
    	
    }
    
	public void populateSfxList() {
		Sound sfx1 = this.newSound(Gdx.files.internal("sounds/sfx1.mp3"));
		soundEffects.add(sfx1);

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
		System.out.println(touchPos);
		tool.clickEvent();
		if (tool.getCooldown() == tool.getCDTimer()) { // Sound only plays off cd
			soundEffects.get(0).play();
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
		//System.out.println(tool.getY());

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
		// TODO Auto-generated method stub
		return Gdx.audio.newSound(fileHandle);
	}

	@Override
	public Music newMusic(FileHandle file) {
		// TODO Auto-generated method stub
		return null;
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
		for (int i = 0; i < soundEffects.size(); i++) {
			soundEffects.get(i).dispose();
		}
		batch.dispose();
	}
}
