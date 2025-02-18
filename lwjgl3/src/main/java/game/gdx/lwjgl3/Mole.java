package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.Texture;

public class Mole extends Entity {

	private int points;
	private boolean isVisible;
	private float speed;
	
	public Mole(Texture t, float x, float y, int p, boolean iv, float s) {
		super(t, x, y);
		points = p;
		isVisible = iv;
		speed = s;
	}
	
	public int getPoints() {
		return points;
	}
	
	void setPoints(int p) {
		points = p;
	}
	
	public boolean getIsVisible() {
		return isVisible;
	}
	
	void setIsVisible(boolean b) {
		isVisible = b;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	void setSpeed(float s) {
		speed = s;
	}
}
