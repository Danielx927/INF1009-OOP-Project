package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class MoleGameLogicHandler implements GameLogicHandler {
    private Array<Collidable> moles;
    private GameScene gameScene;

    public MoleGameLogicHandler(GameScene gameScene) {
        this.gameScene = gameScene;
        this.moles = new Array<>();
    }

    @Override
    public void handleTimeouts(float delta) {
        for (int i = moles.size - 1; i >= 0; i--) {
            Collidable mole = moles.get(i);
            if (mole instanceof InteractiveObject) {
                InteractiveObject io = (InteractiveObject) mole;
                io.decreaseTime(delta);
                if (io.getTimeLeft() <= 0) {
                    System.out.println("Mole timed out! Losing a heart.");
                    gameScene.onMoleExpired(io);
                    removeMole(mole);
                }
            }
        }
    }

    @Override
    public void onCollision(Collidable source, Collidable target) {
        source.onCollision(target);
        target.onCollision(source);
    }

    @Override
    public void onNoCollision(Collidable collidable) {
        collidable.onNoCollision();
    }

    @Override
    public void onMiss() {
        System.out.println("Missed! Resetting streak and losing a heart.");
        gameScene.resetStreak();
        GameMaster.heartSystem.decreaseHeart();
    }

    @Override
    public void addMole(Collidable mole) {
        moles.add(mole);
    }

    @Override
    public void removeMole(Collidable mole) {
        moles.removeValue(mole, true);
    }
}