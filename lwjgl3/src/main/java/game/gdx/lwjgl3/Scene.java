package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	private Texture background;
	private String name;
	private List<Entity> entityList = new ArrayList<Entity>();
	
	//Initialize name and background
	public Scene(String name) {
		this.name = name;
	}
	
	public abstract void load();
	public abstract void unload();
	
	public void setBackground(String bgPath) {
		background = new Texture(Gdx.files.internal(bgPath));
	}
	
	public void drawBackground(SpriteBatch batch) {
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
    public void render(SpriteBatch batch) {
        drawBackground(batch); // Draw the background as a default
    }
    
    public void dispose() {
    	background.dispose();
    }
}
