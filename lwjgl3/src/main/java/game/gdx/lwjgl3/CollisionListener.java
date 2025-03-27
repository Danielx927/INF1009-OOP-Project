package game.gdx.lwjgl3;

public interface CollisionListener {
    void onMoleHit(Collidable mole);
    void onMiss();
    void onMoleExpired(Mole mole);
}
