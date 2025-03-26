package game.gdx.lwjgl3;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.gdx.lwjgl3.animation.SpriteAnimation;

public abstract class Entity {
    private float x;
    private float y;
    private Texture texture;
    private float width;
    private float height;
    private HashMap<String, SpriteAnimation> animTemplate;
    private SpriteAnimation currentAnim;

    public Entity(String t, float x, float y, float w, float h) {
        texture = new Texture(Gdx.files.internal(t));
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        animTemplate = new HashMap<String, SpriteAnimation>();
    }

    public Entity(String t, float x, float y) {
        texture = new Texture(Gdx.files.internal(t));
        this.x = x;
        this.y = y;
        width = texture.getWidth();
        height = texture.getHeight();
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
        if (texture != null) {
            texture.dispose();
        }
        animTemplate.clear();
    }

    public abstract void render(SpriteBatch b);
}