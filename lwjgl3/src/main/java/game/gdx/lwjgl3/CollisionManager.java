package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class CollisionManager {
    private List<Collidable> collidables;

    public CollisionManager(List<Collidable> collidables) {
        this.collidables = collidables;
    }

    public void checkCollisions() {
        if (!Gdx.input.justTouched()) { 
            return; // ✅ Only check for collisions on the first frame of a click
        }

        List<Collidable> objectsThatCollided = new ArrayList<>();

        for (int i = 0; i < collidables.size(); i++) {
            Collidable a = collidables.get(i);

            if (a instanceof Tool) { // ✅ Only check collisions from Tool's perspective
                for (int j = 0; j < collidables.size(); j++) {
                    Collidable b = collidables.get(j);

                    if (b instanceof InteractiveObject) { 
                        //System.out.println("Click collision check: " + a + " vs " + b);

                        if (a.collidesWith(b)) {
                            //System.out.println("✅ CLICK COLLISION DETECTED: " + a + " with " + b);
                            a.onCollision(b); // Notify Tool
                            b.onCollision(a); // Notify InteractiveObject
                            objectsThatCollided.add(a);
                            objectsThatCollided.add(b);
                        }
                    }
                }
            }
        }
        // the collisionbox is update when onclick but when onclick the mousemove event doesnt fire which doesnt
        // update the coords
        // Handle objects that did not collide
        for (Collidable obj : collidables) {
            if (!objectsThatCollided.contains(obj)) {
                obj.onNoCollision();
            }
        }
    }





    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    public void removeCollidable(Collidable c) {
        collidables.remove(c);
    }
    
    public void dispose() {
    	collidables = null;
    }
}
