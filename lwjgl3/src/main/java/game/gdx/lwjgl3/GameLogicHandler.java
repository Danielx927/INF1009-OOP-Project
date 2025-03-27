package game.gdx.lwjgl3;

public interface GameLogicHandler {
    void handleTimeouts(float delta);
    void onCollision(Collidable source, Collidable target);
    void onNoCollision(Collidable collidable);
    void onMiss();
    void addMole(Collidable mole);
    void removeMole(Collidable mole);
}