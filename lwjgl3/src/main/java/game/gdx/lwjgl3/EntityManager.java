package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
	private List<Entity> entityList;
	
	void addEntities(Entity e) {
		if (entityList == null) {
			entityList = new ArrayList<>();
		}
		entityList.add(e);
	}
}
