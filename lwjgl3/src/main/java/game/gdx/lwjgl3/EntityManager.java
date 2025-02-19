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
		entityList.add(e);
	}
	
	void removeEntity(Entity e) {
		entityList.remove(e);
	}
	
	void update(float delta) {
		List<Entity> molesToRemove = new ArrayList<>();
		for (Entity e : entityList) {
			if (e instanceof InteractiveObject) {
				if (!((InteractiveObject) e).isActive(delta)) {
					molesToRemove.add(e);
				}
			}
		}
		entityList.removeAll(molesToRemove);
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
