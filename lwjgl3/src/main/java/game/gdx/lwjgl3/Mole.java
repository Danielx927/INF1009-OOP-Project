package game.gdx.lwjgl3;

public class Mole {

	private int points;
	private boolean isVisible;
	private float speed;
	
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
