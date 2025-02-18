package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Texture;

public abstract class Scene {
	private Texture background;
	private String name;
	private List<Entity> entityList = new ArrayList<Entity>();
	
	public Scene(String string) {
		// TODO Auto-generated constructor stub
	}
	public abstract void load();
	public abstract void unload();

	
}
