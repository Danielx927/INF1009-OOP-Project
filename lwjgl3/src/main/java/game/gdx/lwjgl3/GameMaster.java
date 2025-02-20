package game.gdx.lwjgl3;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameMaster extends ApplicationAdapter{
	private IOManager IOmgr;
	private Tool tool;
	
	private SpriteBatch batch;
	private EntityManager em;
	private GameTile[][] grid;
	//private List<String> players;
	private float spawnTimer;
	private float spawnInterval;
	//private int totalPoints;
	//private String playerName;
	public static CollisionManager collisionManager;

	
	@Override
	public void create()
	{

		tool = new Tool("sprites/red_circle2.png", 100, 100);
        collisionManager = new CollisionManager(new ArrayList<>());
        collisionManager.addCollidable(tool); // Add the Tool (red circle)
        new IOManager();
		IOmgr = IOManager.getInstance();
		IOmgr.addTool(tool);

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
		em.addEntity(tool);
		IOmgr.playMusic("jungle", true, 0.5f);

		
	}
	
	private void spawnIO() {
		int col_index = (int) (Math.random() * grid[0].length);
		int row_index = (int) (Math.random() * grid.length);
		GameTile tile = grid[row_index][col_index];
		if (!tile.getOccupied()) {
			InteractiveObject io = new InteractiveObject("sprites/black_square.png", tile.getX() + 10, tile.getY() + 10, 60, 60, 20, 2f);
			em.addEntity(io);
			IOmgr.playSound("entitySpawn1", 1.0f);
		}
	}
	
	@Override
	public void render()
	{
		ScreenUtils.clear(0, 0, 0.2f, 1);
	
		spawnTimer += Gdx.graphics.getDeltaTime();
		if (spawnTimer >= spawnInterval) {
			spawnIO();
			spawnTimer = 0;
		}
		
		em.update(Gdx.graphics.getDeltaTime());
		collisionManager.checkCollisions();
		em.render(batch);
				
	}
	
	@Override
	public void dispose()
	{
		IOmgr.dispose();
		batch.dispose();
	}
}
