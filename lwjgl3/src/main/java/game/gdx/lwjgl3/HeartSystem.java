package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import game.gdx.lwjgl3.entity.Entity;

public class HeartSystem extends Entity {
    private int maxHearts = 3;
    private int currentHearts;
    private Array<Sprite> heartSprites;
    private Texture heartTexture;
    private float scale = 0.3f;

    public HeartSystem() {
        super("sprites/heartPH.png", 0, 0); // Dummy position
        currentHearts = maxHearts;
        heartTexture = getTextureRegion().getTexture(); // Use texture from Entity
        heartSprites = new Array<>();

        for (int i = 0; i < maxHearts; i++) {
            Sprite heart = new Sprite(heartTexture);
            heart.setScale(scale);
            heartSprites.add(heart);
        }
    }

    public void setPosition(float x, float y) {
        for (int i = 0; i < heartSprites.size; i++) {
            Sprite heart = heartSprites.get(i);
            heart.setPosition(x + i * (heartTexture.getWidth() * scale + 5), y);
        }
        setX(x); // Update inherited position
        setY(y);
    }

    public void decreaseHeart() {
        if (currentHearts > 0) {
            currentHearts--;
            heartSprites.removeIndex(currentHearts);
            if (currentHearts <= 0) {
                System.out.println("Game Over: No hearts left!");
            }
        }
    }

    public void reset() {
        currentHearts = maxHearts;
        heartSprites.clear();
        for (int i = 0; i < maxHearts; i++) {
            Sprite heart = new Sprite(heartTexture);
            heart.setScale(scale);
            heartSprites.add(heart);
        }
    }

    public int getCurrentHearts() {
        return currentHearts;
    }

    public boolean isGameOver() {
        return currentHearts <= 0;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Sprite heart : heartSprites) {
            heart.draw(batch);
        }
    }

    @Override
    public void dispose() {
        super.dispose(); // Disposes texture via Entity
    }
}
