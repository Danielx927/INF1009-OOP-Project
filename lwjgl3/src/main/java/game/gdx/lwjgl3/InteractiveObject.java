package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InteractiveObject extends Entity {

	private int points;
	private float lifeTime;
	private float maxLifeTime;
	
	public InteractiveObject(String t, float x, float y, float w, float h, int p, float d) {
		super(t, x, y, w, h);
		points = p;
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
	

	@Override
	public void render(SpriteBatch b) {
		b.draw(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
