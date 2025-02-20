package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityManager {
	private List<Entity> entityList;
	
	public EntityManager() {
		entityList = new ArrayList<>();
	}
	
	void addEntity(Entity e) {
	    if (e == null) return; // Prevent adding null entities
	    entityList.add(e);
	    
	    if (e instanceof Collidable) {
	        GameMaster.collisionManager.addCollidable((Collidable) e);
	    }
	}

	
	void removeEntity(Entity e) {
	    if (e == null) return; // Prevent null entity removal
	    entityList.remove(e);
	    
	    if (e instanceof Collidable) {
	        GameMaster.collisionManager.removeCollidable((Collidable) e);
	    }
	}

	
	void update(float delta) {
	    List<Entity> molesToRemove = new ArrayList<>();

	    for (Entity e : entityList) {
	        if (e instanceof InteractiveObject) {
	            InteractiveObject obj = (InteractiveObject) e;
	            if (!obj.isActive(delta)) { // ✅ Automatically removes objects with expired lifetime
	                molesToRemove.add(obj);
	            }
	        }
	    }

	    entityList.removeAll(molesToRemove);
	    for (Entity e : molesToRemove) {
	        GameMaster.collisionManager.removeCollidable((Collidable) e);
	    }
	}

	
	void render(SpriteBatch b) {
		// Call draw methods for all entities	
		b.begin();
		for (Entity e : entityList) {
			e.render(b);
		}
		b.end();
	}
}
