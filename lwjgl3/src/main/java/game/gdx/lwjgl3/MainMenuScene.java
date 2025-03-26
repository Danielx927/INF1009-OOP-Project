package game.gdx.lwjgl3;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
		
		Texture playTexture = new Texture(Gdx.files.internal("buttons/playButton.png"));
		Texture playHoverTexture = new Texture(Gdx.files.internal("buttons/playButtonHover.png"));
		Texture quitTexture = new Texture(Gdx.files.internal("buttons/quitButton.png"));
		Texture quitHoverTexture = new Texture(Gdx.files.internal("buttons/quitButtonHover.png"));
		
		ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
		startButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(playTexture));
		startButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(playHoverTexture));
		
		Texture playClickTexture = new Texture(Gdx.files.internal("buttons/playButtonClick.png"));
        startButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(playClickTexture));
		
		ImageButton startButton = new ImageButton(startButtonStyle);
		startButton.setSize(300, 200);
		startButton.setPosition(410, 90);
		
		ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
		quitButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(quitTexture));
		quitButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(quitHoverTexture));
        Texture quitClickTexture = new Texture(Gdx.files.internal("buttons/quitButtonClick.png"));
        quitButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(quitClickTexture));
		
		ImageButton quitButton = new ImageButton(quitButtonStyle);
		quitButton.setSize(300, 200);
		quitButton.setPosition(410, 20);

        startButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                GameMaster.ioManager.newSound(Gdx.files.internal("sounds/button_hover.mp3")).play(1.0f);  // Hover sound
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the next scene (e.g., GameScene2)
            	GameMaster.ioManager.newSound(Gdx.files.internal("sounds/button_click.mp3")).play(0.3f);
                GameMaster.sceneManager.setScene(new GameScene(game));
            }
        });
        
        quitButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                GameMaster.ioManager.newSound(Gdx.files.internal("sounds/button_hover.mp3")).play(1.0f);  // Hover sound
            }
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	GameMaster.ioManager.newSound(Gdx.files.internal("sounds/button_click.mp3")).play(0.3f);
                //Gdx.app.exit(); // Exit the application
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                }, 0.4f); // Adjust delay as needed (0.3 seconds)
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
		background = new Texture(Gdx.files.internal("backgrounds/-MainMenuv2.png"));
	}

	@Override
	public void render(float delta) {
		EquationGenerator randomEq = EquationGeneratorFactory.randomGenerator();
		System.out.println(randomEq.generateEquation());
		Gdx.gl.glClearColor(0, 0, 0, 1);
    	batch.begin();
    	batch.draw(this.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	batch.end();
    	
        // Update and draw the stage (UI)
        stage.act(delta);
        stage.draw();
	}
}
