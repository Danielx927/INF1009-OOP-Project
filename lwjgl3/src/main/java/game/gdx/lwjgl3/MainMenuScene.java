package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScene extends Scene implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton startButton;
    private TextButton quitButton;
    private Game game; // Reference to main game class

    public MainMenuScene(Game game) {
        super("Main Menu");
        this.game = game;
        setBackground("forest.jpg");
        batch = new SpriteBatch(); // Initialize batch here
    }

    @Override
    public void show() {
        load();
        Gdx.input.setInputProcessor(stage); // Set stage as input handler
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        batch = new SpriteBatch();
        batch.begin();
        drawBackground(batch);  // Assuming drawBackground is implemented
        batch.end();

        // Update the stage and draw buttons
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Keep FPS-independent updates
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void load() {
        // UI setup
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Buttons
        startButton = new TextButton("Start Game!", skin);
        quitButton = new TextButton("Quit", skin);

        // Button positioning
        startButton.setPosition(220, 250);
        startButton.setSize(200, 50);
        startButton.setColor(Color.YELLOW);
        quitButton.setPosition(220, 180);
        quitButton.setSize(200, 50);
        quitButton.setColor(Color.GRAY);

        // Button Listeners
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScene(game)); // Pass the game reference to GameScene
                System.out.println("Start button clicked!");
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Quit game
                System.out.println("Quit button clicked!");
            }
        });

        // Add buttons to stage
        stage.addActor(startButton);
        stage.addActor(quitButton);
    }

    @Override
    public void unload() {
        // Cleanup resources
        if (batch != null) {
            batch.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}
}
	
	
}
