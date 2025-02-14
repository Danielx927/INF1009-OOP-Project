package game.gdx.lwjgl3;

public class GameTile extends Entity {
	private boolean isOccupied;
	
	public boolean getOccupied() {
		return isOccupied;
	}
	
	void setOccupied(boolean b) {
		isOccupied = b;
	}
}
