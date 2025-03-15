package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainMenuScene extends Scene {
	protected GameMaster game;
	
	public MainMenuScene(GameMaster game) {
		super(game);
		this.game = game;
		GameMaster.ioManager.playMusic("main-menu", true, 0.2f);
        
		//Add Textures for the button
		Texture playTexture = new Texture(Gdx.files.internal("play_button.png"));
		Texture playHoverTexture = new Texture(Gdx.files.internal("play_button_hover.png"));
		Texture quitTexture = new Texture(Gdx.files.internal("quit_button.png"));
		Texture quitHoverTexture = new Texture(Gdx.files.internal("quit_button_hover.png"));
		
		ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
		startButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(playTexture));
		startButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(playHoverTexture));
		
		ImageButton startButton = new ImageButton(startButtonStyle);
		startButton.setSize(200, 70);
		startButton.setPosition(467, 165);
		
		ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
		quitButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(quitTexture));
		quitButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(quitHoverTexture));
		
		ImageButton quitButton = new ImageButton(quitButtonStyle);
		quitButton.setSize(200, 70);
		quitButton.setPosition(465, 90);
        
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
