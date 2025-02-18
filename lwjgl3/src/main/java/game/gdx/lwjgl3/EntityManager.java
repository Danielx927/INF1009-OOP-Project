package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
	private List<Entity> entityList;
	
	public EntityManager() {
		entityList = new ArrayList<>();
	}
	
	void addEntities(Entity e) {
		entityList.add(e);
	}
	
	void removeEntity(Entity e) {
		entityList.remove(e);
	}
}
