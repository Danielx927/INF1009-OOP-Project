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
	
	public float getX() {
		return x;
	}
	
	void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	void setY(float y) {
		this.y = y;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	void setTexture(Texture t) {
		this.texture = t;
	}
	
	public float getWidth() {
		return width;
	}
	
	void setWidth(float w) {
		width = w;
	}
	
	public float getHeight() {
		return height;
	}
	
	void setHeight(float h) {
		height = h;
	}
	
	void draw(SpriteBatch b) {
		b.draw(this.getTexture(), this.getX(), this.getY(), 
				this.getWidth(), this.getHeight());
	}
}
