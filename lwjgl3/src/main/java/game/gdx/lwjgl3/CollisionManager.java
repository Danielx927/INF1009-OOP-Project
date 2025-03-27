package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import game.gdx.lwjgl3.entity.Tool;

public class CollisionManager {
    private PhysicsManager physicsManager;
    private Array<Body> bodies; // Kept for compatibility, but delegates to PhysicsManager
    private Tool tool;
    private boolean clickOccurred = false;
    private Array<Collidable> moles; // To track all moles for onNoCollision

    public CollisionManager() {
        this.physicsManager = new PhysicsManager();
        bodies = new Array<>(); // Unused directly, but kept for API compatibility
        moles = new Array<>(); // Initialize list to track moles
    }

    public void notifyClick() {
        clickOccurred = true;
    }

    public void checkCollisions() {
        physicsManager.step();

        // Check mole timeouts
        for (int i = moles.size - 1; i >= 0; i--) {
            Collidable mole = moles.get(i);
            if (mole instanceof InteractiveObject) {
                InteractiveObject io = (InteractiveObject) mole;
                io.decreaseTime(Gdx.graphics.getDeltaTime());
                if (io.getTimeLeft() <= 0) {
                    System.out.println("Mole timed out! Losing a heart.");
                    if (GameMaster.sceneManager.getCurrentScene() instanceof GameScene) {
                        GameScene gameScene = (GameScene) GameMaster.sceneManager.getCurrentScene();
                        gameScene.onMoleExpired(io); // Call GameScene method
                    }
                    removeCollidable(mole);
                }
            }
        }

        if (clickOccurred) {
            clickOccurred = false;
            if (tool != null) {
                float minX = tool.getX();
                float minY = tool.getY();
                float maxX = tool.getX() + tool.getWidth();
                float maxY = tool.getY() + tool.getHeight();

                Array<Body> overlappingBodies = new Array<>();
                physicsManager.queryAABB(minX, minY, maxX, maxY, overlappingBodies);

                Array<Collidable> hitMoles = new Array<>();
                for (Body body : overlappingBodies) {
                    if (body != tool.getBody() && body.getUserData() instanceof InteractiveObject) {
                        Collidable mole = (Collidable) body.getUserData();
                        tool.onCollision(mole);
                        mole.onCollision(tool);
                        hitMoles.add(mole);
                    }
                }

                for (Collidable mole : moles) {
                    if (!hitMoles.contains(mole, true)) {
                        mole.onNoCollision();
                    }
                }

                // Check if the click missed all moles
                if (hitMoles.isEmpty() && GameMaster.sceneManager.getCurrentScene() instanceof GameScene) {
                    GameScene gameScene = (GameScene) GameMaster.sceneManager.getCurrentScene();
                    System.out.println("Missed! Resetting streak and losing a heart.");
                    gameScene.resetStreak();
                    GameMaster.heartSystem.decreaseHeart(); // Decrease heart on miss
                }
            }

            physicsManager.updatePositions();
        }
    }

    public void addCollidable(Collidable c) {
        CollisionConfig config;
        if (c instanceof Tool) {
            config = new CollisionConfig((short) 0x0001, (short) 0x0002); // Cast to short
            this.tool = (Tool) c; // Store the tool instance
        } else {
            config = new CollisionConfig((short) 0x0002, (short) 0x0001); // Cast to short
            moles.add(c); // Add to moles list for onNoCollision
        }
        physicsManager.addBody(c, config);
    }

    public void removeCollidable(Collidable c) {
        physicsManager.removeBody(c);
        if (c instanceof Tool) {
            this.tool = null; // Clear tool reference if removed
        } else if (c instanceof InteractiveObject) {
            moles.removeValue(c, true); // Remove from moles list
        }
    }

    public void dispose() {
        physicsManager.dispose();
        bodies.clear();
        moles.clear();
    }

    public World getWorld() {
        return physicsManager.getWorld();
    }
}