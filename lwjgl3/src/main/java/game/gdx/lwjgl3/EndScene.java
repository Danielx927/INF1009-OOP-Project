package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndScene extends Scene {
	private int finalScore;
	private float finalTime;
	private Texture blackTexture; // For table grid lines
	private static final String SCORE_FILE = "scores.txt";
	private BitmapFont font;

	public EndScene(GameMaster game, int score, float time) {
		super(game);
		this.finalScore = score;
		this.finalTime = time;

		// Play music
		GameMaster.ioManager.clearMusicQueue();
		GameMaster.ioManager.pushMusic("endTriumph", false, 1f);
		GameMaster.ioManager.pushMusic("jungle", true, 1f);

		// Set background image
		setBackground("backgrounds/-EndSceneFinalV3.png");

		// Font for Timer and Score
		font = new BitmapFont(Gdx.files.internal("fonts/CharlemagneSTD_Size68.fnt"),
				Gdx.files.internal("fonts/CharlemagneSTD_Size68.png"), false);
		font.getData().setScale(0.5f);

		// LabelStyle for Timer and Score
		Label.LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;

		// Create black texture for table lines
		Pixmap blackPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		blackPixmap.setColor(Color.BLACK);
		blackPixmap.fill();
		blackTexture = new Texture(blackPixmap);
		blackPixmap.dispose();

		// Load, sort, and save scores
		List<String> scores = loadScores();
		// Add the *new* run’s score/time
		scores.add(finalScore + "," + String.format("%.2f", finalTime));
		scores = sortAndTrimScores(scores);
		saveScores(scores);

		// Create the UI
		createUI(scores);

		// Set stage as input processor
		Gdx.input.setInputProcessor(stage);
	}

	private void createUI(List<String> scores) {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		// Dynamically scale font size and adjust table padding to be smaller
		float fontScale = screenHeight / 600f;
		float tablePadding = screenWidth * 0.02f;

		// Add the numerical score and time values next to background text
		createScoreTimeValues(screenWidth, screenHeight);

		// Create and position the leaderboard table
		Table scoreboard = createScoreboard(scores, screenWidth, screenHeight, fontScale, tablePadding);
		scoreboard.pack();

		// Position the scoreboard below the displayed score/time
		float scoreValueY = screenHeight * 0.8f;
		float timeValueY = screenHeight * 0.75f;
		float tableY = Math.min(scoreValueY, timeValueY) - scoreboard.getHeight() - (screenHeight * 0.02f);
		float tableX = screenWidth * 0.06f;
		scoreboard.setPosition(tableX, tableY);
		stage.addActor(scoreboard);

		// Create and position the buttons (keeping their original placement)
		Table buttonTable = createButtons(screenWidth, screenHeight);
		buttonTable.pack();

		float btnX = scoreboard.getX();
		float btnY = scoreboard.getY() - buttonTable.getHeight() - (screenHeight * 0.03f);
		buttonTable.setPosition(btnX, btnY);
		stage.addActor(buttonTable);
	}

	private void createScoreTimeValues(float screenWidth, float screenHeight) {
		float fontScale = screenHeight / 600f; // Scale the font based on screen height

		float scoreValueX = screenWidth * 0.2f;
		float scoreValueY = screenHeight * 0.88f;

		float timeValueX = screenWidth * 0.2f;
		float timeValueY = screenHeight * 0.78f;

		// Create score label
		Label scoreValueLabel = new Label(String.valueOf(finalScore), new LabelStyle(font, Color.WHITE));
		scoreValueLabel.setFontScale(0.7f * fontScale);
		scoreValueLabel.setPosition(scoreValueX, scoreValueY);

		// Create time label showing this run’s final time
		Label timeValueLabel = new Label(String.format("%.2f", finalTime) + "s", new LabelStyle(font, Color.WHITE));
		// Optionally scale the font if needed
		float timeBaseScale = 0.7f * fontScale;
		int timeDigits = String.format("%.2f", finalTime).length();
		float timeScale = timeDigits >= 5 ? timeBaseScale * (5f / timeDigits) : timeBaseScale;
		timeValueLabel.setFontScale(timeScale);
		timeValueLabel.setPosition(timeValueX, timeValueY);

		stage.addActor(scoreValueLabel);
		stage.addActor(timeValueLabel);
	}

	private Table createScoreboard(List<String> scores, float screenWidth, float screenHeight, float fontScale,
			float tablePadding) {
		Table table = new Table();
		table.pad(tablePadding); // Use the smaller padding for the table

		// White background for the scoreboard
		Pixmap boardPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		boardPixmap.setColor(Color.WHITE);
		boardPixmap.fill();
		Texture boardTex = new Texture(boardPixmap);
		boardPixmap.dispose();
		table.setBackground(new TextureRegionDrawable(boardTex));

		// Headers
		Label rankHeader = new Label("RANK", skin);
		rankHeader.setColor(Color.BLACK);
		rankHeader.setFontScale(1.5f * fontScale);

		Label pointsHeader = new Label("POINTS", skin);
		pointsHeader.setColor(Color.BLACK);
		pointsHeader.setFontScale(1.5f * fontScale);

		Label timeHeader = new Label("TIME", skin);
		timeHeader.setColor(Color.BLACK);
		timeHeader.setFontScale(1.5f * fontScale);

		table.row();
		table.add(rankHeader).align(Align.center).pad(2);
		table.add(pointsHeader).align(Align.center).pad(2);
		table.add(timeHeader).align(Align.center).pad(2);
		addHorizontalLine(table, 3);

		// For each entry in the file, read the stored time (parts[1])
		int rank = 1;
		for (String entry : scores) {
			String[] parts = entry.split(",");
			Label rankLabel = new Label(String.valueOf(rank), skin);
			rankLabel.setColor(Color.BLACK);
			rankLabel.setFontScale(1.5f * fontScale);

			Label pointsLabel = new Label(parts[0], skin);
			pointsLabel.setColor(Color.BLACK);
			pointsLabel.setFontScale(1.5f * fontScale);

			// Show the time from the file for this row
			Label timeLabel = new Label(parts[1] + "s", skin);
			timeLabel.setColor(Color.BLACK);
			timeLabel.setFontScale(1.5f * fontScale);

			table.row();
			table.add(rankLabel).align(Align.center).pad(2);
			table.add(pointsLabel).align(Align.center).pad(2);
			table.add(timeLabel).align(Align.center).pad(2);
			addHorizontalLine(table, 3);
			rank++;
		}
		return table;
	}

	private Table createButtons(float screenWidth, float screenHeight) {
		Table buttonTable = new Table();

		// Scaling factors
		float scale = Math.min(screenWidth / 1920f, screenHeight / 1080f);

		// Scaled button size
		float buttonWidth = 600f * scale;
		float buttonHeight = 200f * scale;
		
	    // Individual button padding
	    float playAgainPadLeft = -160f * scale;
	    float playAgainPadTop = -170f * scale; 
	    float quitPadLeft = -490f * scale;
	    float quitPadTop = -340f * scale;
	    
	    //scale quit button
	    float quitButtonScale = 0.8f;
	    float quitWidth = buttonWidth * quitButtonScale;
	    float quitHeight = buttonWidth * quitButtonScale;
		
	    // Move all buttons together
	    float globalMarginX = 100f * scale;
	    float globalMarginY = 100f * scale;

		// Play Again Button for Texture
		Texture playAgainTexture = new Texture(Gdx.files.internal("buttons/playAgainButton.png"));
		Texture playAgainHoverTexture = new Texture(Gdx.files.internal("buttons/playAgainButtonHover.png"));
		Texture playAgainClickTexture = new Texture(Gdx.files.internal("buttons/playAgainButtonClick.png"));
		
		// Create stretched TextureRegionDrawables
		TextureRegionDrawable playAgainUp = new TextureRegionDrawable(new TextureRegion(playAgainTexture));
		TextureRegionDrawable playAgainHover = new TextureRegionDrawable(new TextureRegion(playAgainHoverTexture));
		TextureRegionDrawable playAgainClick = new TextureRegionDrawable(new TextureRegion(playAgainClickTexture));

		// Force stretching by setting min size
		playAgainUp.setMinSize(buttonWidth, buttonHeight);
		playAgainHover.setMinSize(buttonWidth, buttonHeight);
		playAgainClick.setMinSize(buttonWidth, buttonHeight);

		// Assign to button style
		ImageButton.ImageButtonStyle playAgainButtonStyle = new ImageButton.ImageButtonStyle();
		playAgainButtonStyle.imageUp = playAgainUp;
		playAgainButtonStyle.imageOver = playAgainHover;
		playAgainButtonStyle.imageDown = playAgainClick;

		// Create the button
		ImageButton playAgainButton = new ImageButton(playAgainButtonStyle);
		playAgainButton.getImageCell().size(buttonWidth, buttonHeight); // Force cell size

		playAgainButton.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				GameMaster.ioManager.playSound("buttonHover", 1.0f);
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameMaster.ioManager.playSound("buttonClick", 0.3f);
				GameMaster.heartSystem.reset(); // Reset hearts to 3
				GameMaster.sceneManager.setScene(new GameScene(game));
			}
		});

		// Quit Button
		Texture quitTexture = new Texture(Gdx.files.internal("buttons/quitButton.png"));
		Texture quitHoverTexture = new Texture(Gdx.files.internal("buttons/quitButtonHover.png"));
		Texture quitClickTexture = new Texture(Gdx.files.internal("buttons/quitButtonClick.png"));
		
		ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
		quitButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(quitTexture));
		quitButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(quitHoverTexture));
		quitButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(quitClickTexture));
		
		// Create stretched TextureRegionDrawables
		TextureRegionDrawable quitUp = new TextureRegionDrawable(new TextureRegion(playAgainTexture));
		TextureRegionDrawable quitHover = new TextureRegionDrawable(new TextureRegion(playAgainHoverTexture));
		TextureRegionDrawable quitClick = new TextureRegionDrawable(new TextureRegion(playAgainClickTexture));

		// Force stretching by setting min size
		quitUp.setMinSize(quitWidth, quitHeight);
		quitHover.setMinSize(quitWidth, quitHeight);
		quitClick.setMinSize(quitWidth, quitHeight);
		
		ImageButton quitButton = new ImageButton(quitButtonStyle);
		quitButton.getImageCell().size(quitWidth, quitHeight); // Force cell size
		
		quitButton.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				GameMaster.ioManager.playSound("buttonHover", 1.0f);
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameMaster.ioManager.playSound("buttonClick", 0.3f);
				// Exit the game after a short delay
				Timer.schedule(new Timer.Task() {
					@Override
					public void run() {
						Gdx.app.exit();
					}
				}, 0.4f);
			}
		});

	    buttonTable.add(playAgainButton)
        .size(buttonWidth, buttonHeight)
        .padLeft(playAgainPadLeft + globalMarginX)
        .padTop(playAgainPadTop + globalMarginY);
    
    buttonTable.row();
    
    buttonTable.add(quitButton)
        .size(quitWidth, quitHeight)
        .padLeft(quitPadLeft + globalMarginX)
        .padTop(quitPadTop + globalMarginY);
		return buttonTable;
	}

	private void addHorizontalLine(Table table, int colSpan) {
		table.row();
		Image line = new Image(blackTexture);
		// Use a smaller line height for the smaller table
		table.add(line).colspan(colSpan).growX().height(1);
	}

	private List<String> loadScores() {
		List<String> scores = new ArrayList<>();
		FileHandle file = Gdx.files.local(SCORE_FILE);
		if (file.exists()) {
			String content = file.readString();
			String[] lines = content.split("\n");
			Collections.addAll(scores, lines);
		}
		return scores;
	}

	private List<String> sortAndTrimScores(List<String> scores) {
		scores.sort((a, b) -> {
			int scoreA = Integer.parseInt(a.split(",")[0]);
			int scoreB = Integer.parseInt(b.split(",")[0]);
			return Integer.compare(scoreB, scoreA);
		});
		return scores.size() > 5 ? scores.subList(0, 5) : scores;
	}

	private void saveScores(List<String> scores) {
		FileHandle file = Gdx.files.local(SCORE_FILE);
		StringBuilder content = new StringBuilder();
		for (String score : scores) {
			content.append(score).append("\n");
		}
		file.writeString(content.toString(), false);
	}

	@Override
	public void setBackground(String bgPath) {
		if (background != null) {
			background.dispose();
		}
		background = new Texture(Gdx.files.internal(bgPath));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		float bgWidth = 640;
		float bgHeight = 480;

		if (Gdx.graphics.isFullscreen()) {
			float scale = Math.max(screenWidth / bgWidth, screenHeight / bgHeight);
			float scaledWidth = bgWidth * scale;
			float scaledHeight = bgHeight * scale;

			float x = (screenWidth - scaledWidth) / 2;
			float y = (screenHeight - scaledHeight) / 2;

			batch.begin();
			batch.draw(background, x, y, scaledWidth, scaledHeight);
			batch.end();

		} else {
			// Keep manual size when not fullscreen
			batch.begin();
			batch.draw(background, 0, 0, bgWidth, bgHeight);
			batch.end();
		}
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (background != null)
			background.dispose();
		if (blackTexture != null)
			blackTexture.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}
}
