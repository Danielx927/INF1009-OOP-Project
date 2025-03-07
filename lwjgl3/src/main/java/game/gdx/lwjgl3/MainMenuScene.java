package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScene extends Scene {
	protected GameMaster game;
	
	public MainMenuScene(GameMaster game) {
		super(game);
		this.game = game;
		GameMaster.ioManager.playMusic("main-menu", true, 0.2f);	
		
		TextButton startButton = new TextButton("Start Game!", skin);
		startButton.setColor(Color.YELLOW);
		TextButton quitButton = new TextButton("Quit", skin);
		quitButton.setColor(Color.RED);
        startButton.setSize(200, 50); // Set button size
        quitButton.setSize(200, 50); // Set button size
        startButton.setPosition(
            (Gdx.graphics.getWidth() - startButton.getWidth()) / 2, // Center horizontally
            (Gdx.graphics.getHeight() - startButton.getHeight()) / 2 // Center vertically
        );
        quitButton.setPosition(
                (Gdx.graphics.getWidth() - startButton.getWidth()) / 2, // Center horizontally
                (Gdx.graphics.getHeight() - startButton.getHeight()) / 2 - startButton.getHeight() - 10// Center vertically
            );
        
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next scene (e.g., GameScene2)
                GameMaster.sceneManager.setScene(new GameScene(game));
            }
        });
        
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next scene (e.g., GameScene2)
                Gdx.app.exit(); // Exit the application
            }
        });
        
        stage.addActor(startButton);
        stage.addActor(quitButton);
 
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
	public void setBackground(String bgPath) {
		background = new Texture(Gdx.files.internal("game.png"));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
    	batch.begin();
    	batch.draw(this.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	batch.end();
    	
        // Update and draw the stage (UI)
        stage.act(delta);
        stage.draw();
	}
}
