package game.gdx.lwjgl3;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class EndScene extends Scene implements Screen {

	private List<Float> highScoreList;
	private TextButton restartButton;
	private TextButton quitButton;
    private Stage stage;
    private Skin skin;
    private Game game;
	
	public EndScene(Game game) {
		super("End Scene");
		this.game = game;
		this.highScoreList = new ArrayList<>();
	}

	public void populateList() { //For finalization just e.g.
		highScoreList.add(10f);
		highScoreList.add(8f);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		load();
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen

        // Draw background and other elements
        batch.begin();
        drawBackground(batch);  // Assuming you have a method to render background
        batch.end();

        // Draw high scores and other text
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
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
	public void load() {
		// TODO Auto-generated method stub
		stage = new Stage(new ScreenViewport());
		skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
		
		restartButton = new TextButton("Restart", skin);
		quitButton = new TextButton("Quit", skin);	
		
        restartButton.setPosition(220, 250);
        restartButton.setSize(200, 50);
        restartButton.setColor(Color.GREEN);
        
        quitButton.setPosition(220, 180);
        quitButton.setSize(200, 50);
        quitButton.setColor(Color.GRAY);
        
        // Button listeners
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScene(game));  // Restart game
            }
        });
        
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // Quit game
            }
        });
        
        stage.addActor(restartButton);
        stage.addActor(quitButton);
        
        populateList();
		}

	@Override
	public void unload() {
		// TODO Auto-generated method stub
		if(stage != null) {
			stage.dispose();
		}
		if (skin != null) {
			skin.dispose();
		}
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}
	
}
