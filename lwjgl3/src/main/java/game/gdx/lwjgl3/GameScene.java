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

import game.gdx.lwjgl3.entity.GameTile;
import game.gdx.lwjgl3.entity.Mole;
import game.gdx.lwjgl3.equation.EquationGenerator;
import game.gdx.lwjgl3.equation.EquationGeneratorFactory;

public class GameScene extends Scene implements CollisionListener{
	protected GameMaster game;
	private float spawnTimer = 0f;
	private float spawnInterval = 4f;
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
		Mfont.getData().setScale(0.45f);
		
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
		pauseButton.setSize(100, 50);
		pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - 500,
				Gdx.graphics.getHeight() - pauseButton.getHeight() - 400);

		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isPaused = true;
				pause();
			}
		});
		
		refreshEquation();
		stage.addActor(pauseButton);
		stage.addActor(scoreLabel);
		stage.addActor(streakLabel);
		stage.addActor(operand1);
		stage.addActor(operand2);
		stage.addActor(operator);
		stage.addActor(equationLabel);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(GameMaster.ioManager);
		Gdx.input.setInputProcessor(multiplexer);
		
		// Use GameMaster's heartSystem instead of creating a new one
        updateScoreLabel(); // Set initial score position
        GameMaster.heartSystem.setPosition(
            scoreLabel.getX() - 70,
            scoreLabel.getY() - 90
        );
        
        // Set Position for Equation
	    operand1.setPosition(Gdx.graphics.getWidth() / 2 - 45, scoreLabel.getY() - 5);
	    operator.setPosition(Gdx.graphics.getWidth() / 2 - 10, scoreLabel.getY() - 5);
	    operand2.setPosition(Gdx.graphics.getWidth() / 2 + 25, scoreLabel.getY() - 5);
	}

	private void generateGrid() {
		int gridRows = 3, gridCols = 3;
		grid = new GameTile[gridRows][gridCols];

		int leftMargin = 150, bottomMargin = 60;
		int gameTileWH = 80;

		for (int row = 0; row < gridRows; row++) {
			for (int col = 0; col < gridCols; col++) {
				grid[row][col] = new GameTile("sprites/yellow_circle.png", leftMargin + 130 * col,
						bottomMargin + 90 * row, gameTileWH, gameTileWH, false);
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
			Mole io = new Mole("sprites/black_square.png", tile.getX() + 10, tile.getY() + 10,
					60, 60, 100, 4f);
			// Randomly decide whether to show set correct answer or wrong answer to mole (30% chance)
			int valueToSet = Math.random() > 0.7 ? correctAnswer : correctAnswer + (int)(Math.random() * 10); 
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
			System.out.println("Spawned mole at (" + tile.getX() + ", " + tile.getY() + ")");
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
	    
	    // Base scales
	    float scoreBaseScale = 0.7f;
	    float streakBaseScale = 0.5f; // Smaller base for entire streak label

	    // Score scaling (unchanged)
	    int digitCount = String.valueOf(points).length();
	    scoreLabel.setFontScale(digitCount >= 4 ? scoreBaseScale * (3f/digitCount) : scoreBaseScale);

	    // Streak scaling (applies to whole label)
	    int streakDigits = String.valueOf(streak).length();
	    float streakScale = streakBaseScale * (streakDigits >= 4 ? (3f/streakDigits) : 1f);
	    streakLabel.setFontScale(streakScale);

	    // Positioning (unchanged)
	    scoreLabel.setPosition(Gdx.graphics.getWidth() - 105, Gdx.graphics.getHeight() - 90);
	    streakLabel.setPosition(Gdx.graphics.getWidth() - 225, Gdx.graphics.getHeight() - 480);
	    
	 // Update heart position when score moves
        GameMaster.heartSystem.setPosition(
            scoreLabel.getX() - 70,
            scoreLabel.getY() - 90
        );
	
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

		// Timer
		String timeText = String.format("%.2f", timeElapsed);
		Cfont.draw(batch, timeText, 10, Gdx.graphics.getHeight() - 40);
		GameMaster.heartSystem.render(batch); // Render hearts in the same batch block
		batch.end();
		

		if (!isPaused) {
			timeElapsed += delta;
			// Adjusted for faster spawning
			spawnInterval = Math.max(minSpawnInterval, 2f - (timeElapsed / 5f) * 0.5f);
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