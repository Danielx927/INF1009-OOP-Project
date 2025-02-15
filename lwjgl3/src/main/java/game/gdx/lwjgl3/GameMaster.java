package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameMaster extends ApplicationAdapter{
	private IOManager IOmgr;
	
	private SpriteBatch batch;
	private EntityManager em;
	private GameTile[][] grid;
	private List<String> players;
	//Private Timer timer;
	private int totalPoints;
	private String playerName;
	
	@Override
	public void create()
	{
		IOmgr = new IOManager();
		Gdx.input.setInputProcessor(IOmgr);
		batch = new SpriteBatch();
		em = new EntityManager();
		
		grid = new GameTile[3][3];
		players = new ArrayList<>();
		totalPoints = 0;
	}
	
	@Override
	public void render()
	{
		ScreenUtils.clear(0, 0, 0.2f, 1);
		//System.out.println("Hello srinithi!!");
		
	}
	
	@Override
	public void dispose()
	{
		IOmgr.dispose();
		batch.dispose();
	}
}
