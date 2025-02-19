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
		            InteractiveObject mole = (InteractiveObject) e;


		            // Remove if mole is inactive (expired) OR not visible (was clicked)
		            if (!mole.isActive(delta) || !mole.getIsVisible()) {
		                molesToRemove.add(mole);
		            }
		        }
		    }

		    // Remove from entity list
		    entityList.removeAll(molesToRemove);

		    // Also remove from CollisionManager
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
