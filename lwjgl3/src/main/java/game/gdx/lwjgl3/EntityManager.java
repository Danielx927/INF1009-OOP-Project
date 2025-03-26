package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.gdx.lwjgl3.entity.Tool;

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
            if (e instanceof InteractiveObject) {
                InteractiveObject obj = (InteractiveObject) e;
                if (!obj.isActive(delta)) {
                    ioToRemove.add(obj);
                    if (gameScene != null && !obj.wasHit) {
                        // Only reset streak and clear tile if not hit (missed)
                        System.out.println("Resetting streak due to miss");
                        gameScene.resetStreak();
                        gameScene.clearTileForObject(obj); // Clear tile on miss
                    }
                    // Note: No need to clear tile here if hit, as it’s done in onCollision
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
        entityList = null;
    }
}