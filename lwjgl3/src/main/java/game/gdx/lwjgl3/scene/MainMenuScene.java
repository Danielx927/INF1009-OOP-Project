package game.gdx.lwjgl3.scene;

import com.badlogic.gdx.utils.Timer;

import game.gdx.lwjgl3.GameMaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainMenuScene extends Scene {
	protected GameMaster game;

	public MainMenuScene(GameMaster game) {
		super(game);
		this.game = game;

		GameMaster.ioManager.clearMusicQueue();
		GameMaster.ioManager.pushMusic("main-menu", true, 0.2f);

		// Create a table for buttons
		Table buttonTable = new Table();

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		// Scaling factors
		float scale = Math.min(screenWidth / 1920f, screenHeight / 1080f);

		// Scaled button size
		float buttonWidth = 700f * scale;
		float buttonHeight = 500f * scale;
		
		float playButtonY = 1.6f; 
		float quitButtonY = 1.3f; 
		float buttonsX = 1.9f;    

		// Individual button padding
		float playPadLeft = (screenWidth * buttonsX) - (buttonWidth/2);
		float playPadTop = screenHeight - (screenHeight * playButtonY) - (buttonHeight/2);
		float quitPadLeft = (screenWidth * buttonsX) - (buttonWidth/2);
		float quitPadTop = screenHeight - (screenHeight * quitButtonY) - (buttonHeight/2);

		// Move all buttons together
		float globalMarginY = 100f * scale;
		float globalMarginX = 100f * scale;

		// Add Textures for the button
		Texture playTexture = new Texture(Gdx.files.internal("buttons/playButton.png"));
		Texture playHoverTexture = new Texture(Gdx.files.internal("buttons/playButtonHover.png"));
		Texture playClickTexture = new Texture(Gdx.files.internal("buttons/playButtonClick.png"));
		Texture quitTexture = new Texture(Gdx.files.internal("buttons/quitButton.png"));
		Texture quitHoverTexture = new Texture(Gdx.files.internal("buttons/quitButtonHover.png"));
		Texture quitClickTexture = new Texture(Gdx.files.internal("buttons/quitButtonClick.png"));

		// Create stretched TextureRegionDrawables
		TextureRegionDrawable playUp = new TextureRegionDrawable(new TextureRegion(playTexture));
		TextureRegionDrawable playHover = new TextureRegionDrawable(new TextureRegion(playHoverTexture));
		TextureRegionDrawable playClick = new TextureRegionDrawable(new TextureRegion(playClickTexture));

		// play button force stretch
		playUp.setMinSize(buttonWidth, buttonHeight);
		playHover.setMinSize(buttonWidth, buttonHeight);
		playClick.setMinSize(buttonWidth, buttonHeight);

		ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
		startButtonStyle.imageUp = playUp;
		startButtonStyle.imageOver = playHover;
		startButtonStyle.imageDown = playClick;

		ImageButton startButton = new ImageButton(startButtonStyle);
		startButton.getImageCell().size(buttonWidth, buttonHeight);

		// Quit button texture region drawables
		TextureRegionDrawable quitUp = new TextureRegionDrawable(new TextureRegion(quitTexture));
		TextureRegionDrawable quitHover = new TextureRegionDrawable(new TextureRegion(quitHoverTexture));
		TextureRegionDrawable quitClick = new TextureRegionDrawable(new TextureRegion(quitClickTexture));

		// Force stretching by setting min size
		quitUp.setMinSize(buttonWidth, buttonHeight);
		quitHover.setMinSize(buttonWidth, buttonHeight);
		quitClick.setMinSize(buttonWidth, buttonHeight);

		ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
		quitButtonStyle.imageUp = quitUp;
		quitButtonStyle.imageOver = quitHover;
		quitButtonStyle.imageDown = quitClick;

		ImageButton quitButton = new ImageButton(quitButtonStyle);
		quitButton.getImageCell().size(buttonWidth, buttonHeight);

		startButton.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				GameMaster.ioManager.playSound("buttonHover", 1.0f); // Hover sound xD
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Transition to the next scene (e.g., GameScene2)
				GameMaster.ioManager.playSound("buttonClick", 0.3f);
				GameMaster.sceneManager.setScene(new GameScene(game));
			}
		});

		quitButton.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				GameMaster.ioManager.playSound("buttonHover", 1.0f); // Hover sound xD
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameMaster.ioManager.playSound("buttonClick", 0.3f);
				// Gdx.app.exit(); // Exit the application
				Timer.schedule(new Timer.Task() {
					@Override
					public void run() {
						Gdx.app.exit();
					}
				}, 0.4f); // Adjust delay as needed (0.3 seconds)
			}
		});

		buttonTable.add(startButton).size(buttonWidth, buttonHeight).padTop(playPadTop + globalMarginY).padLeft(playPadLeft + globalMarginX);
		buttonTable.row();
		buttonTable.add(quitButton).size(buttonWidth, buttonHeight).padTop(quitPadTop + globalMarginY).padLeft(quitPadLeft + globalMarginX);

		stage.addActor(buttonTable);

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
		Gdx.gl.glClearColor(0, 0, 0, 1);

		batch.begin();
		batch.draw(this.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		// Update and draw the stage (UI)
		stage.act(delta);
		stage.draw();
	}
}
