package game.gdx.lwjgl3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer; // Add if using stage
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.gdx.lwjgl3.animation.AnimManager;
import game.gdx.lwjgl3.collision.CollisionManager;
import game.gdx.lwjgl3.entity.EntityManager;
import game.gdx.lwjgl3.entity.Hammer;
import game.gdx.lwjgl3.entity.HeartSystem;
import game.gdx.lwjgl3.input.IOManager;

public class GameMaster extends Game {
    protected SpriteBatch batch;
    public static CollisionManager collisionManager;
    public static IOManager ioManager;
    public static AnimManager animManager;
    public static EntityManager em;
    public static SceneManager sceneManager;
    public static HeartSystem heartSystem; // Keep for access, but don’t render here
    public Hammer hammer;

    @Override
    public void create() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch = new SpriteBatch();
        collisionManager = new CollisionManager(null);
        em = new EntityManager();

        hammer = new Hammer("sprites/hammer.png", 100, 100);
        new IOManager();
        ioManager = IOManager.getInstance();
        ioManager.addTool(hammer);
        collisionManager.setTool(hammer);       
        // If you have a stage (e.g., for UI like pause button), use InputMultiplexer
        // Otherwise, keep it as is
        InputMultiplexer multiplexer = new InputMultiplexer();
        // multiplexer.addProcessor(stage); // Uncomment and add stage if needed
        multiplexer.addProcessor(ioManager);
        Gdx.input.setInputProcessor(multiplexer);
        // Gdx.input.setInputProcessor(ioManager); // Use this if no stage

        em.addEntity(hammer);

        new AnimManager();
        animManager = AnimManager.getInstance();
        animManager.useTemplate(hammer, "hammer_template");

        sceneManager = new SceneManager(this); // Moved after batch creation
        sceneManager.setScene(new MainMenuScene(this));
        System.out.println(sceneManager.getGame());
        heartSystem = new HeartSystem();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Always handle inputs for hammer movement
        ioManager.handleInputs();

        // Step the world and check collisions every frame, but process clicks only when notified
        collisionManager.checkCollisions();

        // Update music queue every frame
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
        heartSystem.dispose();
        batch.dispose();
    }
}