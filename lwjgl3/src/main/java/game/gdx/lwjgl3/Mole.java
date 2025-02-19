package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mole extends Entity implements Collidable{

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
	
	
	@Override
    public void onCollision(Collidable other) {
        if (other instanceof Tool && isVisible) { // Only process collision if still visible
            System.out.println("✅ Mole hit by Tool!");
            isVisible = false; // Mark mole as invisible (removal now handled in EntityManager.update())
        }
    }
	
	@Override
	public void onNoCollision() {
	    System.out.println("❌ Mole at (" + getX() + ", " + getY() + ") was NOT hit.");
	}





}
