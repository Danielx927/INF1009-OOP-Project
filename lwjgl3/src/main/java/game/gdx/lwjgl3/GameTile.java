package game.gdx.lwjgl3;

public class GameTile extends Entity {
	private boolean isOccupied;
	
	public GameTile(String t, float x, float y, float w, float h, boolean io) {
		super(t, x, y, w, h);
		isOccupied = io;
	}
	
	public boolean getOccupied() {
		return isOccupied;
	}
	
	void setOccupied(boolean b) {
		isOccupied = b;
	}
}
