package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
	private float x;
	private float y;
	private Texture texture;
	
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
	
	void draw(SpriteBatch b) {
		this.draw(b);
	}
}
