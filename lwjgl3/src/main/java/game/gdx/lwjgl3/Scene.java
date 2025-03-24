package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class Scene implements Screen {
	
	protected GameMaster game;
	protected Texture background;
	protected SpriteBatch batch;
	protected Stage stage;
	protected Skin skin;
	
	public Scene(GameMaster game) {
		this.game = game;
		this.batch = game.batch;
		
		stage = new Stage(new ScreenViewport());
		skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        Gdx.input.setInputProcessor(stage);
	}
	
	public void show() {
		Gdx.input.setCursorCatched(true);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);  // Handle UI interactions
		multiplexer.addProcessor(GameMaster.ioManager);  // Handle custom cursor and game controls
		Gdx.input.setInputProcessor(multiplexer);
		
		if (background == null) { // Only set default background if none exists
	        setBackground("game.png");
		}
	};
	public abstract void render(float delta);
	public abstract void setBackground(String bgPath);
	
	public Texture getBackground() {
		return background;
	}
	
	public void dispose() {
		if (background != null) {
			background.dispose();
		}
		stage.dispose();
	};
	
}
