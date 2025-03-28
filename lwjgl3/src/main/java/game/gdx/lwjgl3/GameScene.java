package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import game.gdx.lwjgl3.collision.Collidable;
import game.gdx.lwjgl3.collision.CollisionListener;
import game.gdx.lwjgl3.entity.EntityManager;
import game.gdx.lwjgl3.entity.GameTile;
import game.gdx.lwjgl3.entity.Mole;
import game.gdx.lwjgl3.equation.EquationGenerator;
import game.gdx.lwjgl3.equation.EquationGeneratorFactory;

public class GameScene extends Scene implements CollisionListener {
	protected GameMaster game;
	private float spawnTimer = 0f;
	private float spawnInterval = 3f;
	private float minSpawnInterval = 2f;
	private float timeElapsed = 0f;
	private GameTile[][] grid;
	private EntityManager em;
	private boolean isPaused = false;
	private Stage pauseMenu;
	private Skin skin;
	private int points = 0;
	private int streak = 0;
	private Label scoreLabel;
	private Label streakLabel;
	private BitmapFont Cfont;
	private Label equationLabel;
	private Label operand1;
	private Label operand2;
	private Label operator;
	private int correctAnswer;
	private EquationGenerator equationGenerator;

	public GameScene(GameMaster game) {
		super(game);
		GameMaster.collisionManager.setListener(this); // Attach GameScene as the listener
		this.game = game;
		this.em = new EntityManager(this);
		this.generateGrid();

		GameMaster.ioManager.clearMusicQueue();
		GameMaster.ioManager.pushMusic("jungle", true, 0.5f);

		// timer font
		Cfont = new BitmapFont(Gdx.files.internal("fonts/CharlemagneSTD_Size68.fnt"),
				Gdx.files.internal("fonts/CharlemagneSTD_Size68.png"), false);
		Cfont.getData().setScale(0.6f);

		// Math equation font
		BitmapFont Mfont = new BitmapFont(Gdx.files.internal("fonts/CharlemagneSTD_Size68.fnt"),
				Gdx.files.internal("fonts/CharlemagneSTD_Size68.png"), false);
		Mfont.getData().setScale(0.4f);

		// Create equation label
		Label.LabelStyle equationStyle = new Label.LabelStyle();
		equationStyle.font = Mfont;
		equationStyle.fontColor = Color.WHITE;

		operand1 = new Label("", equationStyle);
		operand2 = new Label("", equationStyle);
		operator = new Label("", equationStyle);

		equationLabel = new Label("", equationStyle);

		// Labels for score and streak
		BitmapFont streakFont = new BitmapFont();
		streakFont.getData().setScale(0.5f);

		Label.LabelStyle scoreStyle = new Label.LabelStyle();
		scoreStyle.font = Cfont;
		Label.LabelStyle streakStyle = new Label.LabelStyle();
		streakStyle.font = Cfont;

		scoreLabel = new Label("0", scoreStyle);
		streakLabel = new Label("0", streakStyle);

		pauseMenu = new Stage(new ScreenViewport());
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		TextButton pauseButton = new TextButton("Pause", skin);

		float basePButtonWidth = 100f;
		float basePButtonHeight = 50f;

		float basePButtonX = Gdx.graphics.getWidth() - basePButtonWidth - 500;
		float basePButtonY = Gdx.graphics.getHeight() - basePButtonHeight - 400;
		
	    float scaleX = Gdx.graphics.getWidth() / 640f;
	    float scaleY = Gdx.graphics.getHeight() / 480f;
	    float globalScale = Math.min(scaleX, scaleY);

		if (Gdx.graphics.isFullscreen()) {

			float buttonWidth = basePButtonWidth * globalScale * 0.7f;
			float buttonHeight = basePButtonHeight * globalScale * 0.6f;

			pauseButton.setSize(buttonWidth, buttonHeight);
			pauseButton.setPosition(50, 50);
		} else {
			pauseButton.setSize(basePButtonWidth, basePButtonHeight);
			pauseButton.setPosition(basePButtonX, basePButtonY);
		}

		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isPaused = true;
				pause();
			}
		});
		
		updateEquationPosition();

		refreshEquation();
		stage.addActor(pauseButton);
		stage.addActor(scoreLabel);
		stage.addActor(streakLabel);
		stage.addActor(equationLabel);
		stage.addActor(operand1);
		stage.addActor(operand2);
		stage.addActor(operator);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(GameMaster.ioManager);
		Gdx.input.setInputProcessor(multiplexer);

		// Use GameMaster's heartSystem instead of creating a new one
		updateScoreLabel(); // Set initial score position

	}
	
	public void updateEquationPosition() {
	    // Base scale for operator (windowed mode)
	    float operatorBaseScale = 0.6f;

	    // Calculate scale for fullscreen
	    float scaleX = Gdx.graphics.getWidth() / 640f;
	    float scaleY = Gdx.graphics.getHeight() / 480f;
	    float globalScale = Math.min(scaleX, scaleY);

	    // Apply scaling to operator and operands
	    float operatorScale = operatorBaseScale * globalScale;
	    operand1.setFontScale(operatorScale);
	    operand2.setFontScale(operatorScale);
	    operator.setFontScale(operatorScale);

	    // Positioning based on fullscreen or windowed mode
	    if (Gdx.graphics.isFullscreen()) {
	        operand1.setPosition(Gdx.graphics.getWidth() / 2 - 120, scoreLabel.getY() + 870);
	        operator.setPosition(Gdx.graphics.getWidth() / 2 - 20, scoreLabel.getY() + 870);
	        operand2.setPosition(Gdx.graphics.getWidth() / 2 + 90, scoreLabel.getY() + 870);
	    } else {
	        operand1.setPosition(Gdx.graphics.getWidth() / 2 - 50, scoreLabel.getY() + 385);
	        operator.setPosition(Gdx.graphics.getWidth() / 2 - 10, scoreLabel.getY() + 385);
	        operand2.setPosition(Gdx.graphics.getWidth() / 2 + 25, scoreLabel.getY() + 385);
	    }
	}

	private void generateGrid() {
		int gridRows = 3, gridCols = 3;
		grid = new GameTile[gridRows][gridCols];

		int leftMargin = 170;
		int bottomMargin = 60;
		int gameTileWH = 80;
		int colSpacing = 110;
		int rowSpacing = 90;

		if (Gdx.graphics.isFullscreen()) {
			float scaleX = Gdx.graphics.getWidth() / 640f;
			float scaleY = Gdx.graphics.getHeight() / 480f;

			// Apply scaling to fullscreen mole
			leftMargin = (int) (170 * scaleX) + 120;
			bottomMargin = (int) (60 * scaleY) - 30;
			gameTileWH = (int) (80 * Math.min(scaleX, scaleY));
			colSpacing = (int) (80 * scaleX);
			rowSpacing = (int) (90 * scaleY);
		}

		for (int row = 0; row < gridRows; row++) {
			for (int col = 0; col < gridCols; col++) {
				grid[row][col] = new GameTile("sprites/yellow_circle.png", leftMargin + colSpacing * col,
						bottomMargin + rowSpacing * row, gameTileWH, gameTileWH, false);
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

			int baseX = 10;
			int baseY = 10;
			int baseSize = 60;

			// Apply scaling
			int scaleX = baseX;
			int scaleY = baseY;
			int moleSize = baseSize;

			if (Gdx.graphics.isFullscreen()) {
				float scaleXfull = Gdx.graphics.getHeight() / 480f;
				float scaleYfull = Gdx.graphics.getWidth() / 680f;
				float globalScale = Math.min(scaleXfull, scaleYfull);

				scaleX = (int) (baseX * globalScale);
				scaleY = (int) (baseY * globalScale);
				moleSize = (int) (baseSize * globalScale);
			}

			Mole io = new Mole("sprites/black_square.png", tile.getX() + scaleX, tile.getY() + scaleY, moleSize,
					moleSize, 100, 5f);
			// Randomly decide whether to show set correct answer or wrong answer to mole
			// (30% chance)
			int valueToSet = Math.random() > 0.7 ? correctAnswer : correctAnswer + (int) (Math.random() * 10);
			if (valueToSet == correctAnswer) {
				io.setAnswerData(valueToSet, true);
			} else {
				io.setAnswerData(valueToSet, false);
			}

			em.addEntity(io);
			GameMaster.ioManager.playSound("entitySpawn1", 1.0f);
			GameMaster.animManager.useTemplate(io, "mole_template");
			io.setCurrentAnim("mole_popup");
			tile.setOccupied(true);
			System.out.println("Spawned mole at (" + (tile.getX() + scaleX) + ", " + (tile.getY() + scaleY) + ")");
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

	public void clearTileForObject(Mole io) {
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				GameTile tile = grid[row][col];
				if (tile.getX() + 10 == io.getX() && tile.getY() + 10 == io.getY()) {
					tile.setOccupied(false);
					// System.out.println("Cleared tile at (" + row + ", " + col + ")");
					return;
				}
			}
		}
	}

	@Override
	public void onMoleHit(Collidable mole) {
		if (mole instanceof Mole io) {
			if (io.isCorrect()) {
				int awarded = getPointsToAward(100);
				addPoints(awarded);
				GameMaster.ioManager.playSound("correct", 1.0f);
				refreshEquation();
			} else {
				GameMaster.ioManager.playSound("wrong", 1.0f);
				resetStreak();
				GameMaster.heartSystem.decreaseHeart();
			}
			clearTileForObject(io);
			em.removeEntity(io);
		}
	}

	@Override
	public void onMiss() {
		resetStreak();
		GameMaster.heartSystem.decreaseHeart();
	}

	@Override
	public void onMoleExpired(Mole io) {
		clearTileForObject(io);
		em.removeEntity(io);
		// Only decrease heart when mole with correct answer expires
		if (io.isCorrect()) {
			GameMaster.heartSystem.decreaseHeart();
		}
	}

	private void updateScoreLabel() {
		// Update texts (keep original font scale for timer)
		scoreLabel.setText("" + points);
		streakLabel.setText("streak: " + streak);

		// Base scales (windowed mode)
		float scoreBaseScale = 0.7f;
		float streakBaseScale = 0.5f; // Smaller base for entire streak label

		float baseScoreX = 535f;
		float baseScoreY = 390f;
		float baseStreakX = 415f;
		float baseStreakY = 0f;

		// Calculate scale for fullscreen
		float scaleX = Gdx.graphics.getWidth() / 640f;
		float scaleY = Gdx.graphics.getHeight() / 480f;
		float globalScale = Math.min(scaleX, scaleY);

		// Score scaling (unchanged)
		int digitCount = String.valueOf(points).length();
		float scoreScale = digitCount >= 4 ? scoreBaseScale * (3f / digitCount) * globalScale
				: scoreBaseScale * globalScale;
		scoreLabel.setFontScale(scoreScale);

		// Streak scaling (applies to whole label)
		int streakDigits = String.valueOf(streak).length();
		float streakScale = streakBaseScale * (streakDigits >= 4 ? (3f / streakDigits) : 1f) * globalScale;
		streakLabel.setFontScale(streakScale);

		// Base position for hearts (windowed mode)
		float baseHeartX = 450f;
		float baseHeartY = 285f;

		// Apply scaling for fullscreen
		if (Gdx.graphics.isFullscreen()) {
			// scalings
			float scoreX = baseScoreX * scaleX - 10f;
			float scoreY = baseScoreY * scaleY + 40f;
			float streakX = baseStreakX * scaleX + 150f;
			float streakY = baseStreakY * scaleY + 50f;
			float heartX = baseHeartX * scaleX + 200f;
			float heartY = baseHeartY * scaleY + 60f;

			scoreLabel.setPosition(scoreX, scoreY);
			streakLabel.setPosition(streakX, streakY);
			GameMaster.heartSystem.setPosition(heartX, heartY);

		} else {
			scoreLabel.setPosition(baseScoreX, baseScoreY);
			streakLabel.setPosition(baseStreakX, baseStreakY);
			GameMaster.heartSystem.setPosition(baseHeartX, baseHeartY);
		}

		float heartScale = globalScale * 0.3f;
		GameMaster.heartSystem.setScale(heartScale);
	}

	public void refreshEquation() {
		equationGenerator = EquationGeneratorFactory.randomGenerator();
		equationGenerator.generateEquation();
		correctAnswer = equationGenerator.getResult();

		// Update label if exists
		if (operand1 != null && operand2 != null && operator != null) {
			operand1.setText(equationGenerator.getOperand1());
			operand2.setText(equationGenerator.getOperand2());
			operator.setText(equationGenerator.getOperator());
		}
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
		resumeButton.setPosition((Gdx.graphics.getWidth() - resumeButton.getWidth()) / 2,
				(Gdx.graphics.getHeight() - resumeButton.getHeight()) / 2 + 60);

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
		endSceneButton.setPosition((Gdx.graphics.getWidth() - endSceneButton.getWidth()) / 2,
				(Gdx.graphics.getHeight() - endSceneButton.getHeight()) / 2);

		endSceneButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Use timeElapsed here instead of elapsedTime
				GameMaster.sceneManager.setScene(new EndScene(game, points, timeElapsed));
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
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void setBackground(String bgPath) {
		background = new Texture(Gdx.files.internal("backgrounds/-GameSceneWIP1.png"));
	}

	public boolean isPaused() {
		return isPaused;
	}

	@Override
	public void render(float delta) {
		if (GameMaster.heartSystem.isGameOver()) {
			GameMaster.sceneManager.setScene(new EndScene(game, points, timeElapsed));
			return;
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(this.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		float baseWidth = 640f;
		float baseHeight = 480f;

		float scale = Math.min(Gdx.graphics.getWidth() / baseWidth, Gdx.graphics.getHeight() / baseHeight);

		float timerX = 15f * scale;
		float timerY = Gdx.graphics.getHeight() - (40f * scale);
		float fontSize = 0.6f * scale;

		// Timer
		String timeText = String.format("%.2f", timeElapsed);
		Cfont.getData().setScale(fontSize);
		Cfont.draw(batch, timeText, timerX, timerY);
		GameMaster.heartSystem.render(batch); // Render hearts in the same batch block
		batch.end();

		// Then render main stage
		stage.act(delta);
		stage.draw();

		if (!isPaused) {
			timeElapsed += delta;
			// Adjusted for faster spawning
			spawnInterval = Math.max(minSpawnInterval, 4f - (timeElapsed / 5f) * 0.5f);
			// System.out.println("Current spawnInterval: " + spawnInterval + ", spawnTimer:
			// " + spawnTimer);

			spawnTimer += delta;
			if (spawnTimer >= spawnInterval) {
				// System.out.println("Attempting to spawn mole at time: " + timeElapsed);
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