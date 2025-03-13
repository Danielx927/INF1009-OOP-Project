package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScene extends Scene {
	
	protected GameMaster game;
	private float spawnTimer = 0f;
    private float spawnInterval = 2f;
    private GameTile[][] grid;
    private EntityManager em;
    
    private boolean isPaused = false;
    private Stage pauseMenu;
    private Skin skin;


	public GameScene(GameMaster game) {
		super(game);
		this.game = game;
		this.em = new EntityManager();
        this.generateGrid();
        
		GameMaster.ioManager.playMusic("jungle", true, 0.5f);	
        
        pauseMenu = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Load the default skin

        // Create the pause button
        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setSize(100, 50);
        pauseButton.setPosition(
            Gdx.graphics.getWidth() - pauseButton.getWidth() - 10, // Top-right corner
            Gdx.graphics.getHeight() - pauseButton.getHeight() - 10
        );

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	isPaused = true; // Pause the game
            	pause();
            }
        });

        // Add the pause button to the main stage
        stage.addActor(pauseButton);
	}
	
	
	private void generateGrid() {
		int gridRows = 3, gridCols = 3;
        grid = new GameTile[gridRows][gridCols];

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
	
	private void spawnIO() {
		int col_index = (int) (Math.random() * grid[0].length);
		int row_index = (int) (Math.random() * grid.length);
		GameTile tile = grid[row_index][col_index];
		if (!tile.getOccupied()) {
			InteractiveObject io = new InteractiveObject("sprites/black_square.png", tile.getX() + 10, tile.getY() + 10, 60, 60, 20, 2f);
			em.addEntity(io);
			GameMaster.ioManager.playSound("entitySpawn1", 1.0f);
			GameMaster.animManager.useTemplate(io, "mole_template");
			io.setCurrentAnim("mole_popup");

		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
        // Clear the pause stage
        pauseMenu.clear();

        // Create a "Resume" button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.setSize(200, 50);
        resumeButton.setPosition(
            (Gdx.graphics.getWidth() - resumeButton.getWidth()) / 2, 
            (Gdx.graphics.getHeight() - resumeButton.getHeight()) / 2 + 60 
        );

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false; // Resume the game
                Gdx.input.setCursorCatched(false);  
        		
        		InputMultiplexer multiplexer = new InputMultiplexer();
        		multiplexer.addProcessor(stage);  // Handle UI interactions
        		multiplexer.addProcessor(GameMaster.ioManager);  // Handle custom cursor and game controls
        		Gdx.input.setInputProcessor(multiplexer);
            }
        });

        // Create a "Go to End Scene" button
        TextButton endSceneButton = new TextButton("Go to End Scene", skin);
        endSceneButton.setSize(200, 50);
        endSceneButton.setPosition(
            (Gdx.graphics.getWidth() - endSceneButton.getWidth()) / 2, 
            (Gdx.graphics.getHeight() - endSceneButton.getHeight()) / 2 
        );

        endSceneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMaster.sceneManager.setScene(new EndScene(game)); 
            }
        });
        // Add buttons to the pause stage
        pauseMenu.addActor(resumeButton);
        pauseMenu.addActor(endSceneButton);

        // Set the pause stage as the input processor
        InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(pauseMenu);  
		multiplexer.addProcessor(GameMaster.ioManager); 
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBackground(String bgPath) {
		background = new Texture(Gdx.files.internal("game.png"));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClearColor(0, 0, 0, 1);
    	batch.begin();
    	batch.draw(this.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	batch.end();
    	
    	if (!isPaused) {
    		spawnTimer += Gdx.graphics.getDeltaTime();
            if (spawnTimer >= spawnInterval) {
                spawnIO();
                spawnTimer = 0;
            }
    	}
		
        em.update(delta);
        GameMaster.collisionManager.checkCollisions();
        em.render(batch);
        
        // Render the pause menu if the game is paused
        if (isPaused) {
            pauseMenu.act(delta);
            pauseMenu.draw();
        }

        // Render the main stage (pause button)
        stage.act(delta);
        stage.draw();
	}

}
