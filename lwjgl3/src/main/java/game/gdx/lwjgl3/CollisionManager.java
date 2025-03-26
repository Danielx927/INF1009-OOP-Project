package game.gdx.lwjgl3;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import game.gdx.lwjgl3.entity.Tool;

public class CollisionManager {
    private World world;
    private Array<Body> bodies;
    private Tool tool;
    private boolean clickOccurred = false;
    private Array<Collidable> moles; // To track all moles for onNoCollision

    public CollisionManager() {
        world = new World(new Vector2(0, 0), true);
        bodies = new Array<>();
        moles = new Array<>(); // Initialize list to track moles
    }

    public void notifyClick() {
        clickOccurred = true;
    }

    public void checkCollisions() {
        // Step the world every frame to keep physics updated
        world.step(1/60f, 6, 2);

        if (clickOccurred) {
            clickOccurred = false; // Reset flag after processing
            if (tool != null) {
                // Define AABB for the tool's area (assuming getX(), getY() are bottom-left)
                float minX = tool.getX();
                float minY = tool.getY();
                float maxX = tool.getX() + tool.getWidth();
                float maxY = tool.getY() + tool.getHeight();

                // Query for bodies overlapping the tool's area
                Array<Body> overlappingBodies = new Array<>();
                world.QueryAABB(fixture -> {
                    Body body = fixture.getBody();
                    // Include bodies that are InteractiveObjects (moles) and exclude the tool itself
                    if (body != tool.getBody() && body.getUserData() instanceof InteractiveObject) {
                        overlappingBodies.add(body);
                    }
                    return true; // Continue querying
                }, minX, minY, maxX, maxY);

                // Process overlaps (hit moles)
                Array<Collidable> hitMoles = new Array<>();
                for (Body body : overlappingBodies) {
                    Collidable mole = (Collidable) body.getUserData();
                    tool.onCollision(mole); // Hammer hits mole
                    mole.onCollision(tool); // Mole reacts to hammer
                    hitMoles.add(mole);
                }

                // Notify non-hit moles with onNoCollision
                for (Collidable mole : moles) {
                    if (!hitMoles.contains(mole, true)) {
                        mole.onNoCollision();
                    }
                }

                // If no overlaps, reset the streak
                if (overlappingBodies.isEmpty() && GameMaster.sceneManager.getCurrentScene() instanceof GameScene) {
                    GameScene gameScene = (GameScene) GameMaster.sceneManager.getCurrentScene();
                    System.out.println("Missed! Resetting streak due to hammer miss.");
                    gameScene.resetStreak();
                }
            }

            // Update positions for all collidables
            Array<Body> currentBodies = new Array<>();
            world.getBodies(currentBodies);
            for (Body body : currentBodies) {
                Object userData = body.getUserData();
                if (userData instanceof Collidable) {
                    Collidable collidable = (Collidable) userData;
                    collidable.updatePosition();
                }
            }
        }
    }

    public void addCollidable(Collidable c) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(c.getX() + c.getWidth() / 2f, c.getY() + c.getHeight() / 2f);

        Body body = world.createBody(bodyDef);
        body.setUserData(c);
        c.setBody(body);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(c.getWidth() / 2f, c.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        if (c instanceof Tool) {
            fixtureDef.filter.categoryBits = 0x0001;
            fixtureDef.filter.maskBits = 0x0002;
            this.tool = (Tool) c; // Store the tool instance
        } else if (c instanceof InteractiveObject) {
            fixtureDef.filter.categoryBits = 0x0002;
            fixtureDef.filter.maskBits = 0x0001;
            moles.add(c); // Add to moles list for onNoCollision
        }

        body.createFixture(fixtureDef);
        bodies.add(body);
        shape.dispose();
    }

    public void removeCollidable(Collidable c) {
        for (Body body : bodies) {
            if (body.getUserData() == c) {
                world.destroyBody(body);
                bodies.removeValue(body, true);
                c.setBody(null);
                if (c instanceof Tool) {
                    this.tool = null; // Clear tool reference if removed
                } else if (c instanceof InteractiveObject) {
                    moles.removeValue(c, true); // Remove from moles list
                }
                break;
            }
        }
    }

    public void dispose() {
        if (world != null) {
            world.dispose();
        }
        bodies.clear();
        moles.clear();
        world = null;
    }

    public World getWorld() {
        return world;
    }
}