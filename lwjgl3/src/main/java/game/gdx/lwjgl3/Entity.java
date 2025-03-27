package game.gdx.lwjgl3;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.gdx.lwjgl3.animation.SpriteAnimation;

public abstract class Entity {
    private float x;
    private float y;
    private TextureRegion textureRegion;
    private float width;
    private float height;
    private HashMap<String, SpriteAnimation> animTemplate;
    private SpriteAnimation currentAnim;

    public Entity(String t, float x, float y, float w, float h) {
    	Texture texture = new Texture(Gdx.files.internal(t));
    	textureRegion = new TextureRegion(texture);
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        animTemplate = new HashMap<String, SpriteAnimation>();
    }

    public Entity(String t, float x, float y) {
//    	Texture texture = new Texture(Gdx.files.internal(t));
    	textureRegion = new TextureRegion(new Texture(Gdx.files.internal(t)));        
    	this.x = x;
        this.y = y;
        width = textureRegion.getRegionWidth();
        height = textureRegion.getRegionHeight();
        animTemplate = new HashMap<String, SpriteAnimation>();
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

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setTexture(String t) {
    	 if (textureRegion != null && textureRegion.getTexture() != null) {
             textureRegion.getTexture().dispose();
         }
         this.textureRegion = new TextureRegion(new Texture(Gdx.files.internal(t)));
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public SpriteAnimation getCurrentAnim() {
        return this.currentAnim;
    }

    public void setCurrentAnimFinished() {
        this.currentAnim = null;
    }

    public void setCurrentAnim(String s) {
    	if (getCurrentAnim() != null) this.getCurrentAnim().reset();

        if (animTemplate.containsKey(s)) {
            SpriteAnimation anim = animTemplate.get(s);
            this.currentAnim = anim;
        }
    }

    public HashMap<String, SpriteAnimation> getAnims() {
        return animTemplate;
    }

    public void setAnims(HashMap<String, SpriteAnimation> template) {
        animTemplate = template;
    }

    public void dispose() {
    	if (textureRegion != null && textureRegion.getTexture() != null) {
            textureRegion.getTexture().dispose();
        }
        animTemplate.clear();
    }

    public void render(SpriteBatch batch) {
        if (currentAnim != null) {
            if (currentAnim.isAnimationFinished()) {
                currentAnim.reset();
                setCurrentAnimFinished();
                drawEntity(batch);
            } else {
                currentAnim.render(batch, this, false);
            }
        } else {
            drawEntity(batch);
        }
    }
    
    public void drawEntity(SpriteBatch batch) {
        batch.draw(textureRegion, x, y, width, height);
    }

    
    
}