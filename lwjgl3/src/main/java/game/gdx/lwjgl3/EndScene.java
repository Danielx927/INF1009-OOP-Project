package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class EndScene extends Scene {

	protected GameMaster game;

	public EndScene(GameMaster game) {
		super(game);
		this.game = game;
		
		TextButton playButton = new TextButton("Play again", skin);
		playButton.setColor(Color.YELLOW);
		TextButton mainMenuButton = new TextButton("Main Menu", skin);
		mainMenuButton.setColor(Color.YELLOW);
        playButton.setSize(200, 50); // Set button size
        mainMenuButton.setSize(200, 50); // Set button size
        
        playButton.setPosition(
            (Gdx.graphics.getWidth() - playButton.getWidth()) / 2, // Center horizontally
            (Gdx.graphics.getHeight() - playButton.getHeight()) / 2 // Center vertically
        );
        
        mainMenuButton.setPosition(
                (Gdx.graphics.getWidth() - playButton.getWidth()) / 2, // Center horizontally
                (Gdx.graphics.getHeight() - playButton.getHeight()) / 2 - playButton.getHeight() - 10// Center vertically
            );
        
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next scene (e.g., GameScene2)
                GameMaster.sceneManager.setScene(new GameScene(game));
            }
        });
        
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next scene (e.g., GameScene2)
                GameMaster.sceneManager.setScene(new MainMenuScene(game));
            }
        });
        
        stage.addActor(playButton);
        stage.addActor(mainMenuButton);
        
        Gdx.input.setInputProcessor(stage);
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
		background = new Texture(Gdx.files.internal("forest.jpg"));
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
