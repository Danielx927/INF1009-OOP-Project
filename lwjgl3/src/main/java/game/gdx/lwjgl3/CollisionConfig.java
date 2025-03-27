package game.gdx.lwjgl3;

public class CollisionConfig {
    public short categoryBits;
    public short maskBits;

    public CollisionConfig(short categoryBits, short maskBits) {
        this.categoryBits = categoryBits;
        this.maskBits = maskBits;
    }
}