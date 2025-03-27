package game.gdx.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import game.gdx.lwjgl3.entity.Tool;

public class CollisionManager {
    private PhysicsManager physicsManager;
    private Array<Body> bodies;
    private Tool tool;
    private boolean clickOccurred = false;
    private Array<Collidable> moles;
    private CollisionListener listener;

    public CollisionManager(CollisionListener listener) {
        this.physicsManager = new PhysicsManager();
        this.listener = listener;
        this.bodies = new Array<>();
        this.moles = new Array<>();
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    
    public void setListener(CollisionListener listener) {
        this.listener = listener;
    }


    public void notifyClick() {
        clickOccurred = true;
    }

    public void checkCollisions() {
        physicsManager.step();

        for (int i = moles.size - 1; i >= 0; i--) {
            Collidable mole = moles.get(i);
            if (mole instanceof Mole io) {
                io.decreaseTime(Gdx.graphics.getDeltaTime());
                if (io.getTimeLeft() <= 0) {
                    if (listener != null) {
                        listener.onMoleExpired(io);
                    }
                    removeCollidable(mole);
                }
            }
        }

        if (clickOccurred) {
            clickOccurred = false;
            if (tool != null) {
                float minX = tool.getX();
                float minY = tool.getY();
                float maxX = tool.getX() + tool.getWidth();
                float maxY = tool.getY() + tool.getHeight();

                Array<Body> overlappingBodies = new Array<>();
                physicsManager.queryAABB(minX, minY, maxX, maxY, overlappingBodies);

                Array<Collidable> hitMoles = new Array<>();
                for (Body body : overlappingBodies) {
                    if (body != tool.getBody() && body.getUserData() instanceof InteractiveObject) {
                        Collidable mole = (Collidable) body.getUserData();
                        tool.onCollision(mole);
                        mole.onCollision(tool);
                        hitMoles.add(mole);

                        if (listener != null) {
                            listener.onMoleHit(mole);
                        }
                    }
                }

                for (Collidable mole : moles) {
                    if (!hitMoles.contains(mole, true)) {
                        mole.onNoCollision();
                    }
                }

                if (hitMoles.isEmpty() && listener != null) {
                    listener.onMiss();
                }
            }

            physicsManager.updatePositions();
        }
    }

    public void addCollidable(Collidable c) {
        CollisionConfig config;
        if (c instanceof Tool) {
            config = new CollisionConfig((short) 0x0001, (short) 0x0002);
        } else {
            config = new CollisionConfig((short) 0x0002, (short) 0x0001);
            moles.add(c);
        }
        physicsManager.addBody(c, config);
    }

    public void removeCollidable(Collidable c) {
        physicsManager.removeBody(c);
        if (c instanceof Tool) {
            this.tool = null;
        } else if (c instanceof InteractiveObject) {
            moles.removeValue(c, true);
        }
    }


    public void dispose() {
        physicsManager.dispose();
        bodies.clear();
        moles.clear();
    }

    public World getWorld() {
        return physicsManager.getWorld();
    }
}
