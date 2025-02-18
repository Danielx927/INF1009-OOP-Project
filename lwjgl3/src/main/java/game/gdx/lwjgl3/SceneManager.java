package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
	private Scene currentScene;
	private List<Scene> sceneList = new ArrayList<Scene>();
	private static SceneManager instance;
	
	public void changetoScene(Scene scene) {
		if (currentScene != null) {
			currentScene.unload();
		}
		
		currentScene = scene;
		currentScene.load();
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}

	public static SceneManager getInstance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new SceneManager();
		}
		return instance;
	}
}
