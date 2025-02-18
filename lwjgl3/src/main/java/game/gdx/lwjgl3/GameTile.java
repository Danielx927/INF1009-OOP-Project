package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.Texture;

public class GameTile extends Entity {
	private boolean isOccupied;
	
	public GameTile(Texture t, float x, float y, boolean io) {
		super(t, x, y);
		isOccupied = io;
	}
	
	public boolean getOccupied() {
		return isOccupied;
	}
	
	void setOccupied(boolean b) {
		isOccupied = b;
	}
}
