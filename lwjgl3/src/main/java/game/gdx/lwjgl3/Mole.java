package game.gdx.lwjgl3;

public class Mole extends Entity {

	public Mole(String t, float x, float y) {
		super(t, x, y);
		// TODO Auto-generated constructor stub
	}

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
