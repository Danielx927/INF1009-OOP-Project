package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mole extends Entity {

	private int points;
	private boolean isVisible;
	private float lifeTime;
	private float maxLifeTime;
	
	public Mole(String t, float x, float y, float w, float h, int p, boolean iv, float d) {
		super(t, x, y, w, h);
		points = p;
		isVisible = iv;
		maxLifeTime = d;
		lifeTime = 0;
	}
	
	public boolean isActive(float delta) {
		lifeTime += delta;
		if (lifeTime >= maxLifeTime) {
			return false;
		}
		return true;
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
	

	@Override
	public void render(SpriteBatch b) {
		if (isVisible) {
			b.draw(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
	}
}
