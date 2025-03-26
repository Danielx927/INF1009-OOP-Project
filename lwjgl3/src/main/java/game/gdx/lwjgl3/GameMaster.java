package game.gdx.lwjgl3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends Game {
    protected SpriteBatch batch;
    public static CollisionManager collisionManager;
    public static IOManager ioManager;
    public static AnimManager animManager;
    public static EntityManager em;
    public static SceneManager sceneManager;
    public Hammer hammer;

    @Override
    public void create() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sceneManager = new SceneManager(this);
        collisionManager = new CollisionManager();
        em = new EntityManager();

        hammer = new Hammer("sprites/hammer.png", 100, 100);
        new IOManager();
        ioManager = IOManager.getInstance();
        ioManager.addTool(hammer);
        Gdx.input.setInputProcessor(ioManager);
        em.addEntity(hammer);

        new AnimManager();
        animManager = AnimManager.getInstance();
        animManager.useTemplate(hammer, "hammer_template");

        batch = new SpriteBatch();
        sceneManager = new SceneManager(this);
        sceneManager.setScene(new MainMenuScene(this));
        System.out.println(sceneManager.getGame());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Skip collision checks in MainMenuScene or when paused in GameScene
        if (!(sceneManager.getCurrentScene() instanceof MainMenuScene) && 
            (!(sceneManager.getCurrentScene() instanceof GameScene) || 
             (sceneManager.getCurrentScene() instanceof GameScene && !((GameScene)sceneManager.getCurrentScene()).isPaused()))) {
            collisionManager.checkCollisions();
        }

        // Always handle inputs for the hammer (movement and clicks)
        ioManager.handleInputs();
        ioManager.updateMusicQueue();

        super.render();
        em.render(batch);
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        collisionManager.dispose();
        em.dispose();
        ioManager.dispose();
        animManager.dispose();
        batch.dispose();
    }
}