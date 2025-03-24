package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

    private Texture blackTexture;  // For the black grid lines in the table
    private static final String SCORE_FILE = "scores.txt";

    // Constructor
    public EndScene(GameMaster game, int score, float time) {
        super(game);
        this.finalScore = score;
        this.finalTime = time;

        // Set background image
        setBackground("backgrounds/-EndSceneFinalV2.png");

        // Create a black 1x1 texture for horizontal grid lines
        Pixmap blackPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        blackPixmap.setColor(Color.BLACK);
        blackPixmap.fill();
        blackTexture = new Texture(blackPixmap);
        blackPixmap.dispose();

        // Load, sort, save scores
        List<String> scores = loadScores();
        scores.add(finalScore + "," + String.format("%.2f", finalTime));
        scores = sortAndTrimScores(scores);
        saveScores(scores);

        // Create the UI
        createUI(scores);

        // Set stage as input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void createUI(List<String> scores) {
        // 1) Current score/time labels (top-left)
        createScoreTimeLabels();

        // 2) The scoreboard itself (white background, black lines)
        Table scoreboard = createScoreboard(scores);

        // Make the scoreboard smaller so it fits your background’s white area
        scoreboard.setSize(250, 240); 
        // Position it where your background’s white area is
        scoreboard.setPosition(30, 110); 
        //  ^^^ Tweak these numbers so the scoreboard lines up perfectly

        stage.addActor(scoreboard);

        // 3) Buttons below the scoreboard
        Table buttonTable = createButtons();
        // Pack it so it knows its own width/height
        buttonTable.pack();
        // Left-align it under the scoreboard. We'll place it a bit below scoreboard
        float btnX = scoreboard.getX();                      // same left as scoreboard
        float btnY = scoreboard.getY() - buttonTable.getHeight() - 20; // 20 px gap
        buttonTable.setPosition(btnX, btnY);

        stage.addActor(buttonTable);
    }

    /** 
     * Creates and positions labels for the current game’s SCORE and TIME 
     * in the top-left corner. Adjust to match your background text.
     */
    private void createScoreTimeLabels() {
        // 1) Create the label for the SCORE value (just the number).
        Label scoreValueLabel = new Label(String.valueOf(finalScore), skin);
        scoreValueLabel.setColor(Color.WHITE);
        scoreValueLabel.setFontScale(2f);

        // Place it exactly where you want, so it lines up with the background's "SCORE:" text.
        // You must tweak these numbers to match your image.
        float scoreX = 120;  // e.g. 150 px from left
        float scoreY = 420;  // e.g. 420 px from bottom (or use from top if you prefer)
        scoreValueLabel.setPosition(scoreX, scoreY);

        stage.addActor(scoreValueLabel);

        // 2) Create the label for the TIME value (just the number + "s").
        Label timeValueLabel = new Label(String.format("%.2f", finalTime) + "s", skin);
        timeValueLabel.setColor(Color.WHITE);
        timeValueLabel.setFontScale(2f);

        // Again, place it to match the background's "TIME:" text.
        // Adjust these numbers until it visually lines up with the "TIME:" line in your background.
        float timeX = 100;
        float timeY = 370;
        timeValueLabel.setPosition(timeX, timeY);

        stage.addActor(timeValueLabel);
    }


    /**
     * Creates a scoreboard table with a white background and black horizontal lines.
     */
    private Table createScoreboard(List<String> scores) {
        Table table = new Table();

        // White background
        Pixmap whitePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fill();
        Texture whiteTexture = new Texture(whitePixmap);
        whitePixmap.dispose();
        TextureRegionDrawable whiteBg = new TextureRegionDrawable(whiteTexture);
        table.setBackground(whiteBg);

        // Header row
        Label rankHeader = new Label("RANK", skin);
        rankHeader.setColor(Color.BLACK);
        Label pointsHeader = new Label("POINTS", skin);
        pointsHeader.setColor(Color.BLACK);
        Label timeHeader = new Label("TIME", skin);
        timeHeader.setColor(Color.BLACK);

        table.row();
        table.add(rankHeader).align(Align.center).pad(5);
        table.add(pointsHeader).align(Align.center).pad(5);
        table.add(timeHeader).align(Align.center).pad(5);
        addHorizontalLine(table, 3);

        // Score rows
        int rank = 1;
        for (String entry : scores) {
            String[] parts = entry.split(",");
            Label rankLabel = new Label(String.valueOf(rank), skin);
            rankLabel.setColor(Color.BLACK);

            Label pointsLabel = new Label(parts[0], skin);
            pointsLabel.setColor(Color.BLACK);

            Label timeLabel = new Label(parts[1] + "s", skin);
            timeLabel.setColor(Color.BLACK);

            table.row();
            table.add(rankLabel).align(Align.center).pad(5);
            table.add(pointsLabel).align(Align.center).pad(5);
            table.add(timeLabel).align(Align.center).pad(5);

            addHorizontalLine(table, 3);
            rank++;
        }
        return table;
    }

    /**
     * Creates a Table with the "Play Again" and "Quit" buttons (yellow),
     * but does NOT position it. We do that in createUI().
     */
    private Table createButtons() {
        Table buttonTable = new Table();

		Texture playAgainTexture = new Texture(Gdx.files.internal("buttons/playAgainButton.png"));
		Texture playAgainHoverTexture = new Texture(Gdx.files.internal("buttons/playAgainButtonHover.png"));
		
		ImageButton.ImageButtonStyle playAgainButtonStyle = new ImageButton.ImageButtonStyle();
		playAgainButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(playAgainTexture));
		playAgainButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(playAgainHoverTexture));
		
		Texture playAgainClickTexture = new Texture(Gdx.files.internal("buttons/playAgainButtonClick.png"));
		playAgainButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(playAgainClickTexture));
		
		ImageButton playAgainButton = new ImageButton(playAgainButtonStyle);
		
		playAgainButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                GameMaster.ioManager.newSound(Gdx.files.internal("sounds/button_hover.mp3")).play(1.0f);  // Hover sound
            }
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	GameMaster.ioManager.newSound(Gdx.files.internal("sounds/button_click.mp3")).play(0.3f);
                GameMaster.sceneManager.setScene(new GameScene(game));
            }
        });
		
		Texture quitTexture = new Texture(Gdx.files.internal("buttons/quitButton.png"));
		Texture quitHoverTexture = new Texture(Gdx.files.internal("buttons/quitButtonHover.png"));
		
		ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
		quitButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(quitTexture));
		quitButtonStyle.imageOver = new TextureRegionDrawable(new TextureRegion(quitHoverTexture));
		
        Texture quitClickTexture = new Texture(Gdx.files.internal("buttons/quitButtonClick.png"));
        quitButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(quitClickTexture));
		
		ImageButton quitButton = new ImageButton(quitButtonStyle);

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

        buttonTable.add(playAgainButton).size(200, 100).pad(10).padLeft(-20).padTop(-40);
        buttonTable.row();
        buttonTable.add(quitButton).size(200, 100).pad(10).padLeft(-120).padTop(-70);
        buttonTable.setPosition(buttonTable.getX() - 20, buttonTable.getY());

        return buttonTable;
    }

    /** Adds a black horizontal line across the table, spanning colSpan columns. */
    private void addHorizontalLine(Table table, int colSpan) {
        table.row();
        Image line = new Image(blackTexture);
        table.add(line).colspan(colSpan).growX().height(2);
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
        // Keep only top 5
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
        batch.begin();
        if (background != null) {
            // Draw background to fill screen
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (background != null) {
            background.dispose();
        }
        if (blackTexture != null) {
            blackTexture.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
