package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
	private Scene currentScene;
	private List<Scene> sceneList = new ArrayList<Scene>() ;

	public void changeToScene(Scene scene) {
		currentScene = scene;
	}
}
