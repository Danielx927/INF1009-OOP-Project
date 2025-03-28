
package game.gdx.lwjgl3.scenes;
import com.badlogic.gdx.Screen;

import game.gdx.lwjgl3.GameMaster;

public class SceneManager {
	private GameMaster game;
	private Screen currentScene;
	
	public SceneManager(GameMaster game) {
		this.game = game;
	}
	
	public GameMaster getGame() {
		return game;
	}
	
	public void setScene(Scene newScene) {
		if (currentScene != null) {
			currentScene.dispose(); // dispose the old scene
		}
		currentScene = newScene;
		game.setScreen(newScene);
	}
	
	public Screen getCurrentScene() {
		return currentScene;
	}
	
	public void render(float delta) {
		if (currentScene != null) {
			currentScene.render(delta);
		}
	}
	
	public void dispose() {
		if (currentScene != null) {
			currentScene.dispose();
		}
	}
};
