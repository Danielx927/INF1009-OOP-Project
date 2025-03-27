package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class HeartSystem {
    private int maxHearts = 3;
    private int currentHearts;
    private Array<Sprite> heartSprites;
    private Texture heartTexture;
    private float scale = 0.3f; // Scale factor to make hearts smaller (50% of original size)

    public HeartSystem() {
        currentHearts = maxHearts;
        heartSprites = new Array<>();
        heartTexture = new Texture("sprites/heartPH.png"); // Replace with your heart sprite path

        for (int i = 0; i < maxHearts; i++) {
            Sprite heart = new Sprite(heartTexture);
            heart.setScale(scale); // Make hearts smaller
            heartSprites.add(heart);
        }
    }

    public void setPosition(float x, float y) {
        for (int i = 0; i < heartSprites.size; i++) {
            Sprite heart = heartSprites.get(i);
            // Adjust spacing for scaled size (heartTexture.getWidth() * scale + 5)
            heart.setPosition(x + i * (heartTexture.getWidth() * scale + 5), y);
        }
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
    
    public boolean isGameOver() {
        return currentHearts <= 0;
    }
    
 // Add reset method to restore hearts
    public void reset() {
        currentHearts = maxHearts;
        heartSprites.clear();
        for (int i = 0; i < maxHearts; i++) {
            Sprite heart = new Sprite(heartTexture);
            heart.setScale(scale);
            heartSprites.add(heart);
        }
    }

    public void render(SpriteBatch batch) {
        for (Sprite heart : heartSprites) {
            heart.draw(batch);
        }
    }

    public int getCurrentHearts() {
        return currentHearts;
    }

    public void dispose() {
        heartTexture.dispose();
    }
}