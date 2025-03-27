package game.gdx.lwjgl3.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import game.gdx.lwjgl3.Collidable;
import game.gdx.lwjgl3.Entity;
import game.gdx.lwjgl3.input.IOManager;

public class Tool extends Entity implements Collidable {
    private Sprite sprite;
    private float cooldown;
    private static final float CD_TIMER = 60;
    private static final Vector2 OFFSET = new Vector2(-70, -60);
    private Body body;

    public Tool(String t, float x, float y) {
        super(t, x, y);
        this.cooldown = 0;
        this.body = null;
        this.setSprite(super.getTextureRegion());
    }

    // Called everytime mouse is moved, clicked
    public void setCoords(int X, int Y) {
        float newX = X + OFFSET.x;
        float newY = Gdx.graphics.getHeight() - Y + OFFSET.y;
        
        super.setX(newX);
        super.setY(newY);
        this.sprite.setPosition(newX, newY);
        if (body != null) body.setTransform(newX + getWidth() / 2f, newY + getHeight() / 2f, 0);

//        System.out.println("=======================================================");
//        System.out.println("Cursor Coords at (" + X + ", " + Y + ")");
//        System.out.println("Body Coords at (" + body.getPosition().x + ", " + body.getPosition().y + ")");
//        System.out.println("Sprite Coords (" + getX() + ", " + getY() + ")");

    }
    

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setSprite(TextureRegion t) {
        this.sprite = new Sprite(t);
        this.sprite.setPosition(getX(), getY());
    }

    public float getCooldown() {
        return this.cooldown;
    }

    public float getCDTimer() {
        return CD_TIMER;
    }

    public void clickEvent() {
        IOManager.getInstance().playSound("defaultClick", 1f);
    }

    @Override
    public float getX() {
        return super.getX();
    }

    @Override
    public float getY() {
        return super.getY();
    }

    @Override
    public float getWidth() {
        return super.getWidth();
    }

    @Override
    public float getHeight() {
        return super.getHeight();
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public void updatePosition() {}

    @Override
    public void onCollision(Collidable other) {
        System.out.println("✅ Tool collided with: " + other);
    }

    @Override
    public void onNoCollision() {
        System.out.println("❌ Tool at (" + body.getPosition().x + ", " + body.getPosition().y + ") did not collide.");

    }
}