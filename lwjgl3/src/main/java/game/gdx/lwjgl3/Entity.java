package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
	private float x;
	private float y;
	private Texture texture;
	private float width;
	private float height;
	
	public Entity(String t, float x, float y, float w, float h) {
		texture = new Texture(Gdx.files.internal(t));
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}
	
	public Entity(String t, float x, float y) {
		texture = new Texture(Gdx.files.internal(t));
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
	    this.x = x;
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
	
	public float getWidth() {
		return width;
	}
	
	
	public float getHeight() {
		return height;
	}
	
	public abstract void render(SpriteBatch b);
}
