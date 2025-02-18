package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tool extends Entity {
	private Sprite sprite;
	private float cooldown;
	private boolean cdIsRunning;
	private static final float CD_TIMER = 120;
	
	public Tool(String t, float x, float y) {
		super(t, x, y);
		this.cooldown = 0;
		this.cdIsRunning = false;
		this.setSprite(super.getTexture());
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
			this.sprite.setColor(Color.WHITE);
			this.sprite.setAlpha((float) 0.5);
			//System.out.println("start");
		}
		else {
			this.sprite.setColor(Color.WHITE);
			this.sprite.setAlpha((float) 0.5);
		}
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		if (this.cdIsRunning == true) {
			if (this.cooldown <= CD_TIMER & this.cooldown != 0) {
				this.cooldown -= 1;
			} 
			else {
				this.cdIsRunning = false;
				this.sprite.setAlpha((float) 1);
			}
		}
		
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
}
		
