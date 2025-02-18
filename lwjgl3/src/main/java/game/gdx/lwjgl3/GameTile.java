package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameTile extends Entity {
	public GameTile(String t, float x, float y) {
		super(t, x, y);
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public void render(SpriteBatch b) {
		b.draw(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
