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
	
	void draw(SpriteBatch b) {
		// Call draw methods for all entities	
		b.begin();
		for (Entity e : entityList) {
			e.draw(b);
		}
		b.end();
	}
}
