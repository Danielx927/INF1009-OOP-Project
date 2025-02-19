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
            return; // ✅ Only check for collisions when a new click is detected
        }

        List<Collidable> objectsThatCollided = new ArrayList<>();

        for (int i = 0; i < collidables.size(); i++) {
            for (int j = 0; j < collidables.size(); j++) {
                Collidable a = collidables.get(i);
                Collidable b = collidables.get(j);

                if (a == null || b == null || a == b) continue;

                if ((a instanceof Tool && b instanceof Mole) || (a instanceof Mole && b instanceof Tool)) {
                    System.out.println("Click collision check: " + a + " vs " + b);

                    if (a.collidesWith(b)) {
                        System.out.println("✅ CLICK COLLISION DETECTED: " + a + " with " + b);
                        a.onCollision(b);
                        b.onCollision(a);
                        objectsThatCollided.add(a);
                        objectsThatCollided.add(b);
                    }
                }
            }
        }

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
}
