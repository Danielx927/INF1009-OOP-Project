package game.gdx.lwjgl3;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class PhysicsManager {
    private World world;
    private Array<Body> bodies;

    public PhysicsManager() {
        world = new World(new Vector2(0, 0), true);
        bodies = new Array<>();
    }

    public void step() {
        world.step(1/60f, 6, 2);
    }

    public void addBody(Collidable c, CollisionConfig config) {
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
        fixtureDef.filter.categoryBits = config.categoryBits;
        fixtureDef.filter.maskBits = config.maskBits;

        body.createFixture(fixtureDef);
        bodies.add(body);
        shape.dispose();
    }

    public void removeBody(Collidable c) {
        for (Body body : bodies) {
            if (body.getUserData() == c) {
                world.destroyBody(body);
                bodies.removeValue(body, true);
                c.setBody(null);
                break;
            }
        }
    }

    public void queryAABB(float minX, float minY, float maxX, float maxY, Array<Body> overlappingBodies) {
        world.QueryAABB(fixture -> {
            Body body = fixture.getBody();
            overlappingBodies.add(body);
            return true;
        }, minX, minY, maxX, maxY);
    }

    public void updatePositions() {
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

    public void dispose() {
        if (world != null) {
            world.dispose();
        }
        bodies.clear();
    }

    public World getWorld() {
        return world;
    }
}