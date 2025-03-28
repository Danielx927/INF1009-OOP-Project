package game.gdx.lwjgl3.collision;

import com.badlogic.gdx.physics.box2d.Body;

public interface Collidable {
    float getX();
    float getY();
    float getWidth();
    float getHeight();

    // New methods for Box2D integration
    Body getBody();           // Get the associated Box2D body
    void setBody(Body body);  // Set the Box2D body when created
    void updatePosition();    // Update position based on Box2D body

    // Optional: If you want to keep this for non-Box2D cases or debugging
    default void onNoCollision() {
        System.out.println("❌ No collision detected for: " + this);
    }

    void onCollision(Collidable other); // Handle collision event
}