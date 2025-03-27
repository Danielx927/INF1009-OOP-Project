package game.gdx.lwjgl3;

import game.gdx.lwjgl3.entity.Mole;

public interface CollisionListener {
    void onMoleHit(Collidable mole);
    void onMiss();
    void onMoleExpired(Mole mole);
}
