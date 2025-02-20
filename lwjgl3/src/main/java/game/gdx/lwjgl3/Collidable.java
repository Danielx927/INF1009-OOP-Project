package game.gdx.lwjgl3;

public interface Collidable {
    float getX();
    float getY();
    float getWidth();
    float getHeight();

    default boolean collidesWith(Collidable other) {
        float ax = this.getX();
        float ay = this.getY();
        float aw = this.getWidth();
        float ah = this.getHeight();

        float bx = other.getX();
        float by = other.getY();
        float bw = other.getWidth();
        float bh = other.getHeight();

        //System.out.println("\nBounding box check:");
        //System.out.println(" - This object: (" + ax + ", " + ay + "), w=" + aw + ", h=" + ah);
        //System.out.println(" - Other object: (" + bx + ", " + by + "), w=" + bw + ", h=" + bh);

        boolean collision = ax < bx + bw &&
                            ax + aw > bx &&
                            ay < by + bh &&
                            ay + ah > by;

        //System.out.println("Collision result: " + collision);
        return collision;
    }

    default void onNoCollision() {
        System.out.println("❌ No collision detected for: " + this);
    }



    void onCollision(Collidable other); // Handle collision event
}
