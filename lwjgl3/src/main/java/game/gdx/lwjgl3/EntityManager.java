package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityManager {
	private List<Entity> entityList;
	
	public EntityManager() {
		entityList = new ArrayList<>();
	}
	
	public void addEntity(Entity e) {
	    if (e == null) return; // Prevent adding null entities
	    entityList.add(e);
	    
	    if (e instanceof Collidable) {
	        GameMaster.collisionManager.addCollidable((Collidable) e);
	    }
	}

	
	public void removeEntity(Entity e) {
	    if (e == null) return; // Prevent null entity removal
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
	            if (!obj.isActive(delta)) { // ✅ Automatically removes objects with expired lifetime
	                ioToRemove.add(obj);
	            }
	        }
	    }

	    entityList.removeAll(ioToRemove);
	    for (Entity e : ioToRemove) {
	        GameMaster.collisionManager.removeCollidable((Collidable) e);
	    }
	}

	
	public void render(SpriteBatch b) {
		Tool tool = null;
		// Call draw methods for all entities.
		// Ensures that if a Tool object is in the entityList, they are render last so that
		// it is drawn above all the other entities.
		b.begin();
		for (Entity e : entityList) {
			if (e instanceof Tool) tool = (Tool) e;
			e.render(b);
		}
		try {
			tool.render(b);
		} catch(NullPointerException e) {}
			
		
		b.end();
	}
	
	public void dispose() {
		entityList = null;
	}
}
