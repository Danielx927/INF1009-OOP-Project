package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameMaster extends ApplicationAdapter{
	private IOManager IOmgr;
	public Tool redc;
	
	private SpriteBatch batch;
	private EntityManager em;
	private GameTile[][] grid;
	//private List<String> players;
	private float spawnTimer;
	private float spawnInterval;
	//private int totalPoints;
	//private String playerName;
	
	@Override
	public void create()
	{
		Gdx.graphics.setSystemCursor(SystemCursor.None);
		redc = new Tool("sprites/red_circle.png", 100, 100);
		IOmgr = new IOManager(redc);
		Gdx.input.setInputProcessor(IOmgr);
		batch = new SpriteBatch();
		em = new EntityManager();
		spawnTimer = 0;
		spawnInterval = 1f;
		
		int gridRows = 3, gridCols = 3;
		grid = new GameTile[gridRows][gridCols];
		//players = new ArrayList<>();
		//totalPoints = 0;
		
		// Codes for grid
		int leftMargin = 170, bottomMargin = 100;
		int gameTileWH = 80;
		
		for (int row = 0; row < gridRows; row++) {
			for (int col = 0; col < gridCols; col++) {
				grid[row][col] = new GameTile("sprites/yellow_circle.png", leftMargin + 110 * col,
					bottomMargin + 90 * row, gameTileWH, gameTileWH, false);
				em.addEntity(grid[row][col]);
			}
		}
		
	}
	
	private void spawnMole() {
		int col_index = (int) (Math.random() * grid[0].length);
		int row_index = (int) (Math.random() * grid.length);
		GameTile tile = grid[row_index][col_index];
		if (!tile.getOccupied()) {
			InteractiveObject mole = new InteractiveObject("sprites/black_square.png", tile.getX() + 10, tile.getY() + 10, 60, 60, 20, 2f);
			em.addEntity(mole);
		}
	}
	
	@Override
	public void render()
	{
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		spawnTimer += Gdx.graphics.getDeltaTime();
		if (spawnTimer >= spawnInterval) {
			spawnMole();
			spawnTimer = 0;
		}
		
		em.update(Gdx.graphics.getDeltaTime());
		
		em.render(batch);
		redc.render(batch);
		
	}
	
	@Override
	public void dispose()
	{
		IOmgr.dispose();
		batch.dispose();
	}
}
