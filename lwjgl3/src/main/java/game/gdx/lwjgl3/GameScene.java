package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScene extends Scene {
    protected GameMaster game;
    private float spawnTimer = 0f;
    private float spawnInterval = 2f;
    private float minSpawnInterval = 0.5f;
    private float timeElapsed = 0f;
    private GameTile[][] grid;
    private EntityManager em;
    private boolean isPaused = false;
    private Stage pauseMenu;
    private Skin skin;
    private int points = 0;
    private int streak = 0;
    private Label scoreLabel;
    private float elapsedTime = 0f;  


    public GameScene(GameMaster game) {
        super(game);
        this.game = game;
        this.em = new EntityManager(this);
        this.generateGrid();
        
        GameMaster.ioManager.clearMusicQueue();
        GameMaster.ioManager.pushMusic("jungle", true, 0.5f);

        pauseMenu = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton pauseButton = new TextButton("Pause", skin);
        pauseButton.setSize(100, 50);
        pauseButton.setPosition(
            Gdx.graphics.getWidth() - pauseButton.getWidth() - 10,
            Gdx.graphics.getHeight() - pauseButton.getHeight() - 10
        );

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = true;
                pause();
            }
        });

        scoreLabel = new Label("Points: 0  Streak: 0", skin);
        scoreLabel.setPosition(
            (Gdx.graphics.getWidth() - scoreLabel.getWidth()) / 2,
            Gdx.graphics.getHeight() - scoreLabel.getHeight() - 10
        );

        stage.addActor(pauseButton);
        stage.addActor(scoreLabel);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(GameMaster.ioManager);
        Gdx.input.setInputProcessor(multiplexer);
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
        System.out.println("Checking tile at (" + row_index + ", " + col_index + "), occupied: " + tile.getOccupied());
        if (!tile.getOccupied()) {
            InteractiveObject io = new InteractiveObject("sprites/black_square.png", tile.getX() + 10, tile.getY() + 10, 60, 60, 100, 2f);
            em.addEntity(io);
            GameMaster.ioManager.playSound("entitySpawn1", 1.0f);
            GameMaster.animManager.useTemplate(io, "mole_template");
            io.setCurrentAnim("mole_popup");
            tile.setOccupied(true);
            System.out.println("Spawned mole at (" + tile.getX() + ", " + tile.getY() + ")");
        } else {
            System.out.println("Tile occupied, skipping spawn");
        }
    }

    public void addPoints(int pointsGained) {
        System.out.println("Before adding points: points=" + points + ", streak=" + streak);
        points += pointsGained;
        streak++;
        System.out.println("After adding points: points=" + points + ", streak=" + streak);
        updateScoreLabel();
    }

    public int getPointsToAward(int basePoints) {
        int currentStreak = getStreak();
        float multiplier = 1.0f;
        if (currentStreak >= 10) {
            multiplier = 3.0f;
        } else if (currentStreak >= 5) {
            multiplier = 2.0f;
        } else if (currentStreak >= 3) {
            multiplier = 1.5f;
        }
        return (int) (basePoints * multiplier);
    }

    public int getStreak() {
        return streak;
    }

    public void resetStreak() {
        System.out.println("Resetting streak due to miss, current streak before reset: " + streak);
        streak = 0;
        updateScoreLabel();
    }

    public void clearTileForObject(InteractiveObject io) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                GameTile tile = grid[row][col];
                if (tile.getX() + 10 == io.getX() && tile.getY() + 10 == io.getY()) {
                    tile.setOccupied(false);
                    //System.out.println("Cleared tile at (" + row + ", " + col + ")");
                    return;
                }
            }
        }
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Points: " + points + "  Streak: " + streak);
        scoreLabel.setPosition(
            (Gdx.graphics.getWidth() - scoreLabel.getWidth()) / 2,
            Gdx.graphics.getHeight() - scoreLabel.getHeight() - 10
        );
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        pauseMenu.getViewport().update(width, height, true);
        updateScoreLabel();
    }

    @Override
    public void pause() {
        pauseMenu.clear();

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.setSize(200, 50);
        resumeButton.setPosition(
            (Gdx.graphics.getWidth() - resumeButton.getWidth()) / 2,
            (Gdx.graphics.getHeight() - resumeButton.getHeight()) / 2 + 60
        );

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false;
                Gdx.input.setCursorCatched(false);

                InputMultiplexer multiplexer = new InputMultiplexer();
                multiplexer.addProcessor(stage);
                multiplexer.addProcessor(GameMaster.ioManager);
                Gdx.input.setInputProcessor(multiplexer);
            }
        });

        TextButton endSceneButton = new TextButton("Go to End Scene", skin);
        endSceneButton.setSize(200, 50);
        endSceneButton.setPosition(
            (Gdx.graphics.getWidth() - endSceneButton.getWidth()) / 2,
            (Gdx.graphics.getHeight() - endSceneButton.getHeight()) / 2
        );

        endSceneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	GameMaster.sceneManager.setScene(new EndScene(game, points, elapsedTime));

            }
        });

        pauseMenu.addActor(resumeButton);
        pauseMenu.addActor(endSceneButton);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(pauseMenu);
        multiplexer.addProcessor(GameMaster.ioManager);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void setBackground(String bgPath) {
        background = new Texture(Gdx.files.internal(bgPath));
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(this.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (!isPaused) {
            timeElapsed += delta;
            // Adjusted for faster spawning
            spawnInterval = Math.max(minSpawnInterval, 2f - (timeElapsed / 5f) * 0.5f);
            //System.out.println("Current spawnInterval: " + spawnInterval + ", spawnTimer: " + spawnTimer);

            spawnTimer += delta;
            if (spawnTimer >= spawnInterval) {
                //System.out.println("Attempting to spawn mole at time: " + timeElapsed);
                spawnIO();
                spawnTimer = 0f;
            }
            em.update(delta);
        }

        em.render(batch);

        if (isPaused) {
            pauseMenu.act(delta);
            pauseMenu.draw();
        }

        stage.act(delta);
        stage.draw();
    }
}