package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InteractiveObject extends Entity implements Collidable{

	private int points;
	private float lifeTime;
	private float maxLifeTime;
	private boolean isVisible;
	
	public InteractiveObject(String t, float x, float y, float w, float h, int p, float d) {
        super(t, x, y, w, h);
        points = p;
        maxLifeTime = d;
        lifeTime = 0;
        isVisible = true; // Start as visible
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
	

	@Override
	public void render(SpriteBatch b) {
		b.draw(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
	
	@Override
    public void onCollision(Collidable other) {
        if (other instanceof Tool && isVisible) { // Only process collision if still visible
            System.out.println("✅ Mole hit by Tool!");
            isVisible = false; // Mark mole as invisible (removal now handled in EntityManager.update())
        }
    }

    @Override
    public void onNoCollision() {
        System.out.println("❌ InteractiveObject at (" + getX() + ", " + getY() + ") was NOT hit.");
    }
}
