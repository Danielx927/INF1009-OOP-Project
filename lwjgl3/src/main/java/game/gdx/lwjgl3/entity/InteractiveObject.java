package game.gdx.lwjgl3.entity;

import com.badlogic.gdx.physics.box2d.Body;

import game.gdx.lwjgl3.GameMaster;
import game.gdx.lwjgl3.GameScene;
import game.gdx.lwjgl3.collision.Collidable;
import game.gdx.lwjgl3.input.IOManager;

public class InteractiveObject extends Entity implements Collidable {
    private float lifeTime; // Current lifetime elapsed
    private float maxLifeTime; // Total lifetime before expiration
    private Body body;
    public boolean wasHit = false;

    public InteractiveObject(String t, float x, float y, float w, float h, float d) {
        super(t, x, y, w, h);
        this.body = null;
        maxLifeTime = d; // Set maximum lifetime (e.g., 4f from GameScene)
        lifeTime = 0; // Start at 0, increases with delta time
    }

    // Check if the object is still active (used in GameScene or EntityManager if needed)
    public boolean isActive(float delta) {
        lifeTime += delta;
        return lifeTime < maxLifeTime;
    }

    // Getter for remaining time (for CollisionManager)
    public float getTimeLeft() {
        return maxLifeTime - lifeTime; // Remaining time before expiration
    }

    // Decrease lifetime (for CollisionManager to update each frame)
    public void decreaseTime(float delta) {
        lifeTime += delta;
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
    public void updatePosition() {
        if (body != null) {
            setX(body.getPosition().x - getWidth() / 2f);
            setY(body.getPosition().y - getHeight() / 2f);
        }
    }

    @Override
    public void onCollision(Collidable other) {

        if (other instanceof Tool) {
            System.out.println("Hit!");
            wasHit = true;
            lifeTime = maxLifeTime; // Mark for immediate removal
            IOManager.getInstance().playHitSound();
            if (GameMaster.sceneManager.getCurrentScene() instanceof GameScene) {
                wasHit = true;
                lifeTime = maxLifeTime;
                IOManager.getInstance().playHitSound();
            }
        }
    }

    @Override
    public void onNoCollision() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}