package game.gdx.lwjgl3;


public class Mole extends Entity {

	private int points;
	private boolean isVisible;
	private int duration;
	
	public Mole(String t, float x, float y, float w, float h, int p, boolean iv, int d) {
		super(t, x, y, w, h);
		points = p;
		isVisible = iv;
		duration = d;
	}
	
	public void spawn() {
		
	}
	
	public void disappear() {
		if (isVisible) {
			isVisible = false;
		}
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
	
	public float getDuration() {
		return duration;
	}
	
	void setSpeed(int d) {
		duration = d;;
	}
}
