package game.gdx.lwjgl3;

public class GameTile extends Entity {
	public GameTile(String t, float x, float y) {
		super(t, x, y);
		// TODO Auto-generated constructor stub
	}

	private boolean isOccupied;
	
	public boolean getOccupied() {
		return isOccupied;
	}
	
	void setOccupied(boolean b) {
		isOccupied = b;
	}
}
