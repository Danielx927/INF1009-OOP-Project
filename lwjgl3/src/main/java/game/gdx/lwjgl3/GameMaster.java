package game.gdx.lwjgl3;

import java.util.ArrayList;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends Game{
	
	protected SpriteBatch batch;
	public static CollisionManager collisionManager;
	public static IOManager IOmgr;
	public static EntityManager em;
	public static SceneManager sceneManager;
	public Tool tool;

	@Override
	public void create()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1); // Set clear color to black
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
	    
	    sceneManager = new SceneManager(this);
	    collisionManager = new CollisionManager(new ArrayList<>());
	    em = new EntityManager();
        tool = new Tool("sprites/red_circle2.png", 100, 100);
        collisionManager.addCollidable(tool); // Add the Tool (red circle)
        
        IOmgr = IOManager.getInstance();
		IOmgr.addTool(tool);
		Gdx.input.setInputProcessor(IOmgr);
		em.addEntity(tool);
		
	    batch = new SpriteBatch();
		IOmgr.playMusic("jungle", true, 0.5f);
		sceneManager = new SceneManager(this);
		sceneManager.setScene(new MainMenuScene(this));
		System.out.println(sceneManager.getGame());
		
	}
	
	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1); // Set clear color to black (or any other color)
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		em.render(batch);

	}
	
	@Override
	public void dispose()
	{
		sceneManager.dispose();
		collisionManager.dispose();
		em.dispose();
		IOmgr.dispose();
		batch.dispose();
	}
}
