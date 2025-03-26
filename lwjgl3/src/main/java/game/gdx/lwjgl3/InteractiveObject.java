package game.gdx.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import game.gdx.lwjgl3.entity.Tool;
import game.gdx.lwjgl3.input.IOManager;

public class InteractiveObject extends Entity implements Collidable {
    private int points;
    private float lifeTime;
    private float maxLifeTime;
    private Body body;
    public boolean wasHit = false;

    public InteractiveObject(String t, float x, float y, float w, float h, int p, float d) {
        super(t, x, y, w, h);
        points = p;
        maxLifeTime = d;
        lifeTime = 0;
        this.body = null;
    }

    public boolean isActive(float delta) {
        lifeTime += delta;
        return lifeTime < maxLifeTime;
    }

    public int getPoints() {
        return points;
    }

    void setPoints(int p) {
        points = p;
    }

    @Override
    public void render(SpriteBatch b) {
        if (getCurrentAnim() != null) {
            if (getCurrentAnim().isAnimationFinished()) {
                setCurrentAnimFinished();
                b.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
            } else {
                getCurrentAnim().render(b, this, false);
            }
        } else {
            b.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
        }
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
                GameScene gameScene = (GameScene) GameMaster.sceneManager.getCurrentScene();
                int pointsToAward = gameScene.getPointsToAward(this.points);
                System.out.println("Adding points with streak " + gameScene.getStreak() + ": " + pointsToAward);
                gameScene.addPoints(pointsToAward);
                // Fix: Clear the tile immediately when hit
                gameScene.clearTileForObject(this);
            }
        }
    }

    @Override
    public void onNoCollision() {
        System.out.println("❌ InteractiveObject at (" + getX() + ", " + getY() + ") was NOT hit.");
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}