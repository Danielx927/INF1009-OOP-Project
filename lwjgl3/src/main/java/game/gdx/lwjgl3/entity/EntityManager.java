package game.gdx.lwjgl3.entity;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.gdx.lwjgl3.GameMaster;
import game.gdx.lwjgl3.collision.Collidable;
import game.gdx.lwjgl3.scenes.GameScene;

public class EntityManager {
    private GameScene gameScene;
    private List<Entity> entityList = new ArrayList<>();

    public EntityManager() {
        this(null);
    }

    public EntityManager(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public void addEntity(Entity e) {
        if (e == null) return;
        entityList.add(e);
        if (e instanceof Collidable) {
            GameMaster.collisionManager.addCollidable((Collidable) e);
        }
    }

    public void removeEntity(Entity e) {
        if (e == null) return;
        entityList.remove(e);
        if (e instanceof Collidable) {
            GameMaster.collisionManager.removeCollidable((Collidable) e);
        }
    }

    public void update(float delta) {
        List<Entity> ioToRemove = new ArrayList<>();

        for (Entity e : entityList) {
            if (e instanceof Mole) {
            	Mole obj = (Mole) e;
                if (!obj.isActive(delta)) { // Updates lifeTime and checks expiration
                    ioToRemove.add(obj);
                    // If mole with correct answer timed out
                    if (gameScene != null && !obj.wasHit && obj.isCorrect()) { 
                        System.out.println("Mole timed out! Losing a heart and resetting streak.");
                        if (GameMaster.heartSystem.getCurrentHearts() > 0) { // Ensure heart decreases only if available
                            GameMaster.heartSystem.decreaseHeart();
                        }
                        gameScene.resetStreak();
                    }
                    gameScene.clearTileForObject(obj);
                }
            }
        }

        for (Entity e : ioToRemove) {
            removeEntity(e);
        }
    }

    public void render(SpriteBatch b) {
        Tool tool = null;
        b.begin();
        for (Entity e : entityList) {
            if (!(e instanceof Tool)) {
                e.render(b);
            } else {
                tool = (Tool) e;
            }
        }
        if (tool != null) {
            tool.render(b);
        }
        b.end();
    }

    public void dispose() {
        for (Entity e : entityList) {
            e.dispose();
        }
        entityList.clear();
    }
}