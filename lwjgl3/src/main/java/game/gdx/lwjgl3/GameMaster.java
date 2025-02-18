package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
		
		// Codes for grid
		int leftMargin = 170, bottomMargin = 100;
		int gameTileWH = 80;
		
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				grid[row][col] = new GameTile("sprites/red_circle.png", leftMargin + 110 * col,
					bottomMargin + 90 * row, gameTileWH, gameTileWH, false);
				em.addEntity(grid[row][col]);
			}
		}
		
	}
	
	@Override
	public void render()
	{
		ScreenUtils.clear(0, 0, 0.2f, 1);
		em.draw(batch);
	}
	
	@Override
	public void dispose()
	{
		IOmgr.dispose();
		batch.dispose();
	}
}
