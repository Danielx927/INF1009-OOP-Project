package game.gdx.lwjgl3;

public class InputEvent {
	public int keyCode, xPos, yPos, ptr, mouseBtn;
	public float scrollAmtX, scrollAmtY;
	public char keyChar;
	// 0 = keyboard, 1 = mouse move, 2 = mouse click, 3 = mouse scroll
	public int inputType; 
	public IOManager ioMgr;
	
	public InputEvent() {
		ioMgr = IOManager.getInstance();
	}
		
	public int getInputType() {
		return inputType;
	}
	
	public boolean keyDown(int keycode) {
		keyCode = keycode;
		inputType = 0;
		ioMgr.addInputEvent(this);
		return false;
	}

	public boolean keyUp(int keycode) {
		keyCode = keycode;
		inputType = 0;
		ioMgr.addInputEvent(this);

		return false;
	}

	public boolean keyTyped(char character) {
		keyChar = character;
		inputType = 0;
		ioMgr.addInputEvent(this);

		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		xPos = screenX;
		yPos = screenY;
		ptr = pointer;
		mouseBtn = button;
		inputType = 2;
		ioMgr.addInputEvent(this);

		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		xPos = screenX;
		yPos = screenY;
		ptr = pointer;
		mouseBtn = button;
		inputType = 1;
		ioMgr.addInputEvent(this);

		return false;
	}

	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		xPos = screenX;
		yPos = screenY;
		ptr = pointer;
		mouseBtn = button;
		inputType = 1;
		ioMgr.addInputEvent(this);

		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		xPos = screenX;
		yPos = screenY;
		ptr = pointer;
		inputType = 1;
		ioMgr.addInputEvent(this);

		return false;
	}
	
	public boolean mouseMoved(int screenX, int screenY) {
		xPos = screenX;
		yPos = screenY;
		inputType = 1;
		ioMgr.addInputEvent(this);

		return false;
	}
	
	public boolean scrolled(float amountX, float amountY) {
		scrollAmtX = amountX;
		scrollAmtY = amountY;
		inputType = 3;
		ioMgr.addInputEvent(this);

		return false;
	}


}
