package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScene extends Scene {
	private TextButton startButton;
	private TextButton quitButton;
	private Skin skin;
	
	public MainMenuScene() {
		super("Main Menu");
	}
	@Override
	public void load() {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		startButton = new TextButton("Start Game!", skin);
		quitButton = new TextButton("Quit", skin);
		
		startButton.setPosition(50, 100);
		quitButton.setPosition(50, 200);
		
		//Add a listener to handle user input
		startButton.addListener(new ClickListener() {
			public void click(InputEvent event, float x, float y) {
				startGame();
			}
		});
		
		quitButton.addListener(new ClickListener() {
			public void click(InputEvent event, float x, float y) {
				quitGame();
			}
		});
	}
	private void startGame() {
		SceneManager.getInstance().changetoScene(new GameScene());
	}
	
	private void quitGame() {
		Gdx.app.exit();
	}
	
	@Override
	public void unload() {
		// TODO Auto-generated method stub
		skin.dispose();
	}
	
	
}
