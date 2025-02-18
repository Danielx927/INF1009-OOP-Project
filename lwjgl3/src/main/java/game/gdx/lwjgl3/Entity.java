package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
	private float x;
	private float y;
	private Texture texture;
	
	public Entity(String t, float x, float y) {
		this.setTexture(t);
		this.setX(x);
		this.setY(y);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(String t) {
		texture = new Texture(Gdx.files.internal(t));
	}
	
	public void draw(SpriteBatch b) {
		b.begin();
		b.draw(this.getTexture(), this.getX(), this.getY(), this.getTexture().getWidth(), this.getTexture().getHeight());
		b.end();
	}
}
