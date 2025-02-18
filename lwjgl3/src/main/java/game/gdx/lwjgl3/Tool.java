package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tool extends Entity {
	private Sprite sprite;
	private float cooldown;
	private static final float CD_TIMER = 120;
	
	public Tool(String t, float x, float y) {
		super(t, x, y);
		cooldown = CD_TIMER;
		this.setSprite(super.getTexture());
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
	public void setSprite(Texture t) {
		this.sprite = new Sprite(super.getTexture(), super.getTexture().getWidth(), super.getTexture().getHeight());
	}
	
	public void clickEvent() {
		cooldown = CD_TIMER;
		this.sprite.setColor(Color.WHITE);
		this.sprite.setAlpha((float) 0.5);

	}
	
	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		sprite.draw(batch);
		batch.end();
		
		if (cooldown != 0) {
			cooldown -= 1;
		}
		else if (cooldown == 0) {
			this.sprite.setAlpha((float) 1);
		}

	}
	
}
		
