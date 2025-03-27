package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import game.gdx.lwjgl3.entity.Tool;
import game.gdx.lwjgl3.input.IOManager;

public class InteractiveObject extends Entity implements Collidable {
    private int points;
    private float lifeTime; // Current lifetime elapsed
    private float maxLifeTime; // Total lifetime before expiration
    private Body body;
    public boolean wasHit = false;
    private int assignedAnswer;
    private boolean isCorrect;
    private NinePatch answerBox;
    private BitmapFont answerFont;
    private boolean showAnswer = true;

    public InteractiveObject(String t, float x, float y, float w, float h, int p, float d) {
        super(t, x, y, w, h);
        points = p;
        maxLifeTime = d; // Set maximum lifetime (e.g., 2f from GameScene)
        lifeTime = 0; // Start at 0, increases with delta time
        this.body = null;

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0.3f, 0.2f, 0.1f, 0.9f); // Brownish semi-transparent
        pix.fill();
        answerBox = new NinePatch(new Texture(pix), 1, 1, 1, 1);
        pix.dispose();

        answerFont = new BitmapFont(Gdx.files.internal("fonts/CharlemagneSTD_Size68.fnt"),
                Gdx.files.internal("fonts/CharlemagneSTD_Size68.png"), false);
        answerFont.getData().setScale(0.5f);
        answerFont.setColor(Color.WHITE);

        isCorrect = true;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int p) {
        points = p;
    }

    public void setAnswerData(int answer, boolean isCorrect) {
        this.assignedAnswer = answer;
        this.isCorrect = isCorrect;
    }

    public int getAssignedAnswer() {
        return assignedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
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
        if (showAnswer && assignedAnswer != 0) {
            float boxX = getX() + getWidth() / 2 - 25;
            float boxY = getY() + getHeight() + 5;

            b.setColor(isCorrect ? Color.GREEN : Color.RED);
            answerBox.draw(b, boxX, boxY, 50, 30);
            b.setColor(Color.WHITE);

            String answer = String.valueOf(assignedAnswer);
            GlyphLayout layout = new GlyphLayout(answerFont, answer);
            answerFont.draw(b, answer,
                    boxX + 25 - layout.width / 2,
                    boxY + 20 + layout.height / 2);
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

                wasHit = true;
                lifeTime = maxLifeTime;
                IOManager.getInstance().playHitSound();
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
        answerFont.dispose(); // Dispose of the font to avoid memory leaks
    }
}