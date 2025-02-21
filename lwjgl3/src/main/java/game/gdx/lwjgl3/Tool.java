package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tool extends Entity implements Collidable{
	private Sprite sprite;
	private float cooldown;
	private boolean cdIsRunning;
	private static final float CD_TIMER = 60;
	private static final Vector2 OFFSET = new Vector2(-60, Gdx.graphics.getHeight() - 70);
	
	public Tool(String t, float x, float y) {
		super(t, x, y, 128, 128);
		this.cooldown = 0;
		this.cdIsRunning = false;
		this.setSprite(super.getTexture());
	}
	
	public void setCoords(int X, int Y) {
		super.setX(X + OFFSET.x);
		super.setY(-Y + OFFSET.y);
		this.sprite.setPosition(X + OFFSET.x, -Y + OFFSET.y);

	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public void setSprite(Texture t) {
		this.sprite = new Sprite(super.getTexture(), super.getTexture().getWidth(), super.getTexture().getHeight());
	}
	
	public float getCooldown() {
		return this.cooldown;
	}
	
	public float getCDTimer() {
		return CD_TIMER;
	}
	
	public void clickEvent() {
		if (this.cdIsRunning == false) {
			this.cooldown = CD_TIMER;
			this.cdIsRunning = true;
			this.sprite.setAlpha((float) 1);
		}
		else {
			this.sprite.setColor(Color.WHITE);
			this.sprite.setAlpha((float) 0.5);
		}
		
		this.sprite.setColor(Color.WHITE);
		this.sprite.setAlpha((float) 0.5);
		IOManager.getInstance().playSound("generic1", 0.3f);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (this.cdIsRunning == true) {
			if (this.cooldown <= CD_TIMER & this.cooldown != 0) {
				this.cooldown -= 1;
			} 
			else {
				this.cdIsRunning = false;
				this.sprite.setAlpha((float) 1);
			}
		}
		sprite.draw(batch);

	}
	
	@Override
    public void onCollision(Collidable other) {
        System.out.println("✅Tool collided with: " + other);
    }
	
	

	
}
		
