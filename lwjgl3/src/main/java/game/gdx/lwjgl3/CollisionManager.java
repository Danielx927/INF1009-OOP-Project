package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import java.util.HashSet;

public class CollisionManager {
    private World world;
    private Array<Body> bodies;
    private ContactListener contactListener;
    private Array<Collidable> collidedThisFrame;
    private HashSet<String> collisionPairsThisFrame;

    public CollisionManager() {
        world = new World(new Vector2(0, 0), true);
        bodies = new Array<>();
        collidedThisFrame = new Array<>();
        collisionPairsThisFrame = new HashSet<>();
        setupContactListener();
    }

    private void setupContactListener() {
        contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                
                Object userDataA = bodyA.getUserData();
                Object userDataB = bodyB.getUserData();

                if (userDataA instanceof Collidable && userDataB instanceof Collidable) {
                    Collidable collidableA = (Collidable) userDataA;
                    Collidable collidableB = (Collidable) userDataB;
                    
                    String pairKey = Math.min(System.identityHashCode(collidableA), System.identityHashCode(collidableB)) + "_" +
                                   Math.max(System.identityHashCode(collidableA), System.identityHashCode(collidableB));
                    
                    if (collisionPairsThisFrame.add(pairKey)) {
                        collidableA.onCollision(collidableB);
                        collidableB.onCollision(collidableA);
                        collidedThisFrame.add(collidableA);
                        collidedThisFrame.add(collidableB);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}
            
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        };
        world.setContactListener(contactListener);
    }

    public void checkCollisions() {
        if (Gdx.input.justTouched()) {
            collidedThisFrame.clear();
            collisionPairsThisFrame.clear();

            world.step(1/60f, 6, 2);

            Array<Body> currentBodies = new Array<>();
            world.getBodies(currentBodies);

            // Update positions
            for (Body body : currentBodies) {
                Object userData = body.getUserData();
                if (userData instanceof Collidable) {
                    Collidable collidable = (Collidable) userData;
                    collidable.updatePosition();
                }
            }

            // Check if the Tool collided with anything
            boolean toolHitSomething = false;
            Tool tool = null;
            for (Body body : currentBodies) {
                Object userData = body.getUserData();
                if (userData instanceof Tool) {
                    tool = (Tool) userData;
                }
                if (userData instanceof Collidable && collidedThisFrame.contains((Collidable) userData, true)) {
                    if (userData instanceof Tool) {
                        toolHitSomething = true; // Tool hit something
                    }
                }
            }

            // If the Tool didn't hit anything, reset the streak
            if (tool != null && !toolHitSomething && GameMaster.sceneManager.getCurrentScene() instanceof GameScene) {
                GameScene gameScene = (GameScene) GameMaster.sceneManager.getCurrentScene();
                System.out.println("Missed! Resetting streak due to hammer miss.");
                gameScene.resetStreak();
            }

            // Notify non-collided entities
            for (Body body : currentBodies) {
                Object userData = body.getUserData();
                if (userData instanceof Collidable) {
                    Collidable collidable = (Collidable) userData;
                    if (!collidedThisFrame.contains(collidable, true)) {
                        collidable.onNoCollision();
                    }
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
        } else if (c instanceof InteractiveObject) {
            fixtureDef.filter.categoryBits = 0x0002;
            fixtureDef.filter.maskBits = 0x0001;
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
                break;
            }
        }
    }

    public void dispose() {
        if (world != null) {
            world.dispose();
        }
        bodies.clear();
        collidedThisFrame.clear();
        collisionPairsThisFrame.clear();
        world = null;
    }

    public World getWorld() {
        return world;
    }
}