package game.gdx.lwjgl3;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneManager {
	private Scene currentScene;
	private List<Scene> sceneList = new ArrayList<Scene>();
	private static SceneManager instance;
	private Game game;
	
	
	public SceneManager(Game game) {
		this.game = game;
		instance = this;
	}
	public void addScene(Scene scene) {
		if(!sceneList.contains(scene)) {
			sceneList.add(scene);
		}
	}
	public void changetoScene(Class<? extends Scene> sceneClass) {
        Scene scene = getScene(sceneClass);
        if (scene == null) {
            throw new IllegalStateException("Scene not found: " + sceneClass.getSimpleName());
        }

        if (currentScene != null) {
            currentScene.unload();
        }

        currentScene = scene;
        currentScene.load();

        if (game == null) {
            throw new IllegalStateException("SceneManager's game instance is null!");
        }

        game.setScreen(currentScene);
    }
	public Scene getScene(Class<? extends Scene> sceneClass) {
        for (Scene scene : sceneList) {
            if (scene.getClass().equals(sceneClass)) {
                return scene;
            }
        }
        return null; // Return null if not found
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}

	public static SceneManager getInstance(Game game) {
		// TODO Auto-generated method stub
		
		if (instance == null) {
			instance = new SceneManager(game);
		}
		return instance;
	}
    
	public void render(SpriteBatch batch) {
		if (currentScene != null) {
			currentScene.render(batch);
		}
	}
}
